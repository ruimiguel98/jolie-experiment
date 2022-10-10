type Product {
    .id: string // to accept UUID values
    .quantity: int
}

type CheckoutPayRequest {
    .cartId: string
    .userId: string
    .cardNumber: string
    .cardCVV: string
    .order {
        .status: string
        .addressToShip: string
        .products[0, *]: Product // an array of Products
    }
}

interface CheckoutInterface {
	RequestResponse:
		pay(CheckoutPayRequest)(undefined)
}

constants {
    LOCATION_SERVICE_CHECKOUT = "socket://host.docker.internal:9054",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_CHECKOUT_TABLE = "CREATE TABLE checkout (
                                    id UUID NOT NULL,
                                    order_id UUID NOT NULL,
                                    cart_id UUID NULL
                                );
                                COMMENT ON TABLE checkout IS 'Table that holds the order and cart ids about checkouts with success.';"
}