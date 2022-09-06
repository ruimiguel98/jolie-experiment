interface CheckoutInterface {
	RequestResponse:
		checkoutPay(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_CHECKOUT = "socket://host.docker.internal:9054",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_CHECKOUT_TABLE = "CREATE TABLE public.checkout (
                                    id numeric NOT NULL,
                                    card numeric NOT NULL,
                                    address numeric NULL
                                );
                                COMMENT ON TABLE public.payment IS 'Table that holds the several checkout information. This table holds the data necessary after a click on the checkout button.';"
}