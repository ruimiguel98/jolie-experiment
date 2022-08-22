type CreateOrderRequest {
    .id: string // to accept UUID values
    .userId: int
    .status: string
    .orderAmount: string
    .addressToShip: string
    .orderProducts: string
}

interface OrderInterface {
    RequestResponse:
        // CRUD operations
        all(undefined)(undefined),
        order(undefined)(undefined),
        create(CreateOrderRequest)(undefined),
        update(CreateOrderRequest)(undefined),
        delete(undefined)(undefined),

        // OTHER operations
        userOrders(undefined)(undefined)
}


constants {
    LOCATION_SERVICE_ORDER = "socket://localhost:9053",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_ORDER_INFO = "CREATE TABLE public.orders (
                                    id varchar(128) NOT NULL,
                                    address_to_ship varchar(255) NULL,
                                    order_amount varchar(255) NULL,
                                    order_products varchar(9999) NULL,
                                    status varchar(255) NULL,
                                    user_id int4 NOT NULL,
                                    CONSTRAINT orders_pkey PRIMARY KEY (id)
                            );
                            COMMENT ON TABLE public.orders IS 'Table that holds the order information.';"
}