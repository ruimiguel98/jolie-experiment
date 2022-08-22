type CreateCartRequest {
    .id: string // to accept UUID values
    .userId: string // to accept UUID values
    .cartTotal: string
    .cartProducts: string
}

interface CartInterface {
    RequestResponse:
        // CRUD operations
        all(undefined)(undefined),
        cart(undefined)(undefined),
        create(CreateCartRequest)(undefined),
        update(CreateCartRequest)(undefined),
        delete(undefined)(undefined),

        // OTHER operations
}


constants {
    LOCATION_SERVICE_CART = "socket://localhost:9057",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_CART_INFO = "CREATE TABLE public.cart (
                                    id varchar(128) NOT NULL,
                                    cart_produtcs varchar(9999) NULL,
                                    user_id varchar(128) NULL,
                                    cart_total numeric NULL,
                                    CONSTRAINT cart_pkey PRIMARY KEY (id)
                            );
                            COMMENT ON TABLE public.cart IS 'Table that holds the cart information.';"
}