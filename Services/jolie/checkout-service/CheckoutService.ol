include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

include "./CheckoutInterface.iol"
include "../order-service/OrderInterface.iol"
include "../payment-service/PaymentInterface.iol"
include "../email-service/EmailInterface.iol"
include "../user-service/UserInterface.iol"
include "../cart-service/CartInterface.iol"

execution { concurrent }


outputPort OrderService {
    Location: LOCATION_SERVICE_ORDER
    Protocol: http { .format = "json" }
    Interfaces: OrderInterface
}
outputPort PaymentService {
    Location: LOCATION_SERVICE_PAYMENT
    Protocol: http { .format = "json" }
    Interfaces: PaymentInterface
}
outputPort EmailService {
    Location: LOCATION_SERVICE_EMAIL
    Protocol: http { .format = "json" }
    Interfaces: EmailInterface
}
outputPort CartService {
    Location: LOCATION_SERVICE_CART
    Protocol: http { .format = "json" }
    Interfaces: CartInterface
}


// deployment info
inputPort CheckoutPort {
    Location: LOCATION_SERVICE_CHECKOUT
    Protocol: http { .format = "json" }
    Interfaces: CheckoutInterface
    Redirects: 
        Order => OrderService,
        Payment => PaymentService,
        Email => EmailService,
        Cart => CartService
}

// prepare database connection (creates table if does not exist)
init
{
    with (connectionInfo) {
        .username = SQL_USERNAME;
        .password = SQL_PASSWORD;
        .host = SQL_HOST;
        .database = SQL_DATABASE; // "." for memory-only
        .driver = SQL_DRIVER
    };

    connect@Database(connectionInfo)();
    println@Console("Successfull connection to the PostgreSQL database")();

    // create check table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Checkout table already exists")());
        update@Database(SQL_CREATE_CHECKOUT_TABLE)(ret)
    }

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
}

// behaviour info
main
{
    [ 
        pay( request)( response ) {
            println@Console( "[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Initializing checkout process..." )( );

            //----------------------------- 0. GET TOTAL CART PRICE --------------------------------
            cart@CartService( request.cartId )( responseCart )
            println@Console( "[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Cart total price retrieved" )( );

            //----------------------------- 1. WITHDRAWL PROVIDED BANK ACCOUNT --------------------------------
            requestPayment.amount = double(responseCart.cartPriceTotal)
            requestPayment.cardNumber = request.cardNumber
            withdrawlAccount@PaymentService( requestPayment )( responsePayment )
            println@Console( "[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Bank account credited" )( );

            
            if ( responsePayment.status == "SUCCESS" ) {
                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Payment processed with success")( )


                //----------------------------- 2. PLACE THE ORDER --------------------------------
                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Placing the order")( )
                
                getRandomUUID@StringUtils( )( responseUUID )
                requestOrder.userId = request.userId
                requestOrder.orderPriceTotal = double(responseCart.cartPriceTotal)
                requestOrder.addressToShip = request.order.addressToShip
                requestOrder.status = request.order.status

                // an array can't be associated to a variable directly, needs to be iterated
                counter = 0;
                for( product in request.order.products ) {
                    requestOrder.products[counter].id = product.id
                    requestOrder.products[counter].quantity = product.quantity
                    counter = counter + 1
                }

                create@OrderService( requestOrder )( responseOrder )

                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Order placed with success")( )

                //----------------------------- 3. SEND ORDER PLACED EMAIL --------------------------------
                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Sending email")( )
                sendEmail@EmailService(  )( responseEmail )


                //----------------------------- 4. UPDATE CHECKOUT RECORDS DATABASE --------------------------------
                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Updating checkout database")( )

                getRandomUUID@StringUtils( )( checkoutUUID )

                update@Database(
                    "INSERT INTO checkout(id, order_id, cart_id) VALUES (:id::uuid, :orderId::uuid, :cartId::uuid);" {
                        .id = checkoutUUID,
                        .orderId = responseOrder.orderId,
                        .cartId = request.cartId
                    }
                )(sqlResponse)

                customResponse.id = checkoutUUID
                customResponse.cartId = request.cartId
                customResponse.orderId = responseOrder.orderId
            }
            else if (responsePayment.status == "ERROR") {
                println@Console("[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - ERROR not enough balance")( )
                customResponse.error = "Not enough balance"
            }

            response -> customResponse
        } 
    ]
}