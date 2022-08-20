// type CheckoutRequest:void {
// 	.cart?:void {
// 		.id:int
// 	}

//     .shipment?:void {
// 		.id:int
// 	}
// }

type CheckoutRequest:void {
	.cart?:void {
		.id:int
	}

    .shipment?:void {
		.address:int
	}

    .payment?:void {
		.card:int
	}
}



interface CheckoutInterface {
	RequestResponse:
		checkoutPay(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_CHECKOUT = "socket://localhost:9054",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_CHECKOUT_TABLE = "CREATE TABLE public.checkout (
                                    id numeric NOT NULL,
                                    card numeric NOT NULL,
                                    address numeric NULL
                                );
                                COMMENT ON TABLE public.payment IS 'Table that holds the several checkout information. This table holds the data necessary after a click on the checkout button.';"
}