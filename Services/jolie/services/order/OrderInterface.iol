type Product {
    .id: string // to accept UUID values
    .quantity: int
}

type CreateOrderRequest {
    .userId: string // to accept UUID values
    .status: string
    .addressToShip: string
    .products[1, *]: Product // an array of Products
    .orderPriceTotal: double
}

interface OrderInterface {
    RequestResponse:
        // CRUD operations
        all(undefined)(undefined),
        order(undefined)(undefined),
        create(undefined)(undefined),
        update(undefined)(undefined),
        delete(undefined)(undefined),

        // OTHER operations
        userOrders(undefined)(undefined)
}


constants {
    LOCATION_SERVICE_ORDER = "socket://host.docker.internal:9053",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_ORDER_INFO = "CREATE TABLE orders (
                                    id varchar(128) NOT NULL,
                                    address_to_ship varchar(255) NOT NULL,
                                    status varchar(255) NOT NULL,
                                    user_id varchar(128) NOT NULL,
                                    CONSTRAINT orders_pkey PRIMARY KEY (id)
                            );
                            COMMENT ON TABLE orders IS 'Table that holds the order information.';
                            
                            CREATE TABLE order_products (
                                    order_id varchar(128) NOT NULL,
                                    product_id varchar(128) NOT NULL,
                                    quantity numeric NOT NULL DEFAULT 1
                            );
                            COMMENT ON TABLE order_products IS 'Data relative to the products associated with an order';
                            "
}