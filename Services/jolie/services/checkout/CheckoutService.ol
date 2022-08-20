include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./CheckoutInterface.iol"
include "../order/OrderInterface.iol"
include "../payment/PaymentInterface.iol"
include "../email/EmailInterface.iol"

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

// deployment info
inputPort CheckoutPort {
    Location: LOCATION_SERVICE_CHECKOUT
    Protocol: http { .format = "json" }
    Interfaces: CheckoutInterface
    Redirects: 
        Order => OrderService,
        Payment => PaymentService,
        Email => EmailService
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

            println@Console( "This is the checkout service trying to call a order service ")();

            create@OrderService( request.userId )( reponse )

            // order 
            if ( response ) {
                
            }
            else {
                
            }






            // update@Database(
            //     "INSERT INTO checkout(id, card, address) 
            //       VALUES (:id::numeric, :card::numeric, :address::numeric);" {
            //         .id = request.cart.id,
            //         .card = request.payment.card,
            //         .address = request.shipment.address
            //     }
            // )(response.status)

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