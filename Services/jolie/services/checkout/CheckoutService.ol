include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

include "./CheckoutInterface.iol"
include "../order/OrderInterface.iol"
include "../payment/PaymentInterface.iol"
include "../email/EmailInterface.iol"
include "../user/UserInterface.iol"

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
outputPort UserService {
    Location: LOCATION_SERVICE_USER
    Protocol: http { .format = "json" }
    Interfaces: UserInterface
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
        User => UserService
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
    // [ 
    //     checkoutPay( request )( response ) {

    //         println@Console( "Request for checkout has ID: " + request.cart.id )(  )

    //         cartRetrieve@CartPort( request.cart )( response )
    //         println@Console( "Retrieved cart with ID " + response.cart.id )(  )

    //         // response.cartId = response;

    //         // // processPayment@PaymentPort( "mytest" )()
    //         // response.values = "This is just a test" 

    //         // sendShipment@ShippingPort( request )( response )
    //     } 
    // ]

    [ 
        checkoutPay( request)( response ) {

            // user
            println@Console( "[CHECKOUT] - [" + currentDateTime + "] - [/checkoutPay] - Initializing checkout process..." )( );

            request.id = request.userId

            user@UserService( request )( response );

            println@Console( "[CHECKOUT] - Checkout being done by user with ID " + request.userId )( );
            println@Console( "[CHECKOUT] - Checkout being done for the following cart products " + response.cart_products )( );


            // confirming payment
            requestPayment.cardNumber = 23836798;
            requestPayment.amount = 100.00;
            withdrawlAccount@PaymentService( requestPayment )( responsePayment )

            if ( responsePayment.status == 1 ) {
                // create order
                println@Console( "[CHECKOUT] - This is the checkout service trying to call a order service ")()

                getRandomUUID@StringUtils( )( responseUUID )
                order.id = responseUUID;
                order.userId = int(request.userId);
                order.status = "1 - CREATED";
                order.orderAmount = "100";
                order.addressToShip = "Rua teste";
                order.orderProducts = response.cart_products;
                
                create@OrderService( order )( response )

                println@Console( "[CHECKOUT] - Order has been created ")()

                // send email
                println@Console( "[CHECKOUT] - Sending confirmation email ")()
                // sendEmail@EmailService(  )( responseEmail )

                // update@Database(
                //     "INSERT INTO checkout(id, card, address) 
                //       VALUES (:id::numeric, :card::numeric, :address::numeric);" {
                //         .id = request.cart.id,
                //         .card = request.payment.card,
                //         .address = request.shipment.address
                //     }
                // )(response.status)
            }
            else {
                response.message = "Not enought balance on user bank account"
            }

        } 
    ]

    // [ 
    //     checkoutPay( request )( response ) {
    //         update@Database(
    //             "INSERT INTO checkout(id, card, address) 
    //               VALUES (:id::numeric, :card::numeric, :address::numeric);" {
    //                 .id = request.cart.id,
    //                 .card = request.payment.card,
    //                 .address = request.shipment.address
    //             }
    //         )(response.status)

    //     } 
    // ]
}