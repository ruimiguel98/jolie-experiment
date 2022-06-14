include "console.iol"
include "string_utils.iol"

include "./CheckoutInterface.iol"
include "./PaymentInterface.iol"
include "./CartInterface.iol"
include "./ShippingInterface.iol"

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

outputPort ShippingPort {
    Location: "socket://localhost:8005/"
    Protocol: http { .format = "json" }
    Interfaces: ShippingInterface
}

// behaviour info
main
{
    [ 
        checkoutPay( request )( response ) {

            println@Console( "Request for checkout has ID: " + request.cart.id )(  )

            cartRetrieve@CartPort( request.cart )( response )
            println@Console( "Retrieved cart with ID " + response.cart.id )(  )

            // response.cartId = response;

            // // processPayment@PaymentPort( "mytest" )()
            // response.values = "This is just a test" 

            // sendShipment@ShippingPort( request )( response )
        } 
    ]
}