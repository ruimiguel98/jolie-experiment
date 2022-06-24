type CartChangeRequest {
    .id:int
    .product:int
}

interface CartInterface {
    RequestResponse:
        cartRetrieve(undefined)(undefined),
        cartCreate(undefined)(undefined),
        cartAppendProduct(CartChangeRequest)(undefined),
        cartRemoveProduct(CartChangeRequest)(undefined),
        cartDelete(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_CART = "socket://localhost:9002",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_CART = "CREATE TABLE public.cart (
                                id numeric NOT NULL,
                                products _numeric NULL
                            );
                            COMMENT ON TABLE public.cart IS 'Table that holds card information, user and products. Also has the status of the cart.';"
}