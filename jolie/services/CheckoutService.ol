include "console.iol"
include "string_utils.iol"

include "../interfaces/CheckoutInterface.iol"
include "../interfaces/PaymentInterface.iol"
include "../interfaces/CartInterface.iol"

execution { concurrent }

// deployment info
inputPort CheckoutPort {
    Location: "socket://localhost:8002/"
    Protocol: http { .format = "json" }
    Interfaces: CheckoutInterface
}

outputPort PaymentPort {
    Location: "socket://localhost:8003/"
    Protocol: http { .format = "json" }
    Interfaces: PaymentInterface
}

outputPort CartPort {
    Location: "socket://localhost:8001/"
    Protocol: http { .format = "json" }
    Interfaces: CartInterface
}

// behaviour info
main
{
    [ 
        checkoutPay( request )( response ) {
            cartRetrieve@CartPort( request )( response );

            print@Console( "Retrieving cart with ID " + response.cart.id )(  )

            response.cartId = response;

            // processPayment@PaymentPort( "mytest" )()
            response.values = "This is just a test" 
        } 
    ]
}