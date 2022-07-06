type OrderStatusRequest {
    .id: int
}

type OrderStatusResponse {
    .id: int
    .status: int
}

type OrdersListRequest {
    .user_id: int
}

type OrdersListResponse  {
    row[0, 999999]: void  {
        .id: int
        .cart_id: int
        .user_id: int
        .address_to_ship: string
        .payment_used: string
        .status: int
    }
}


interface OrderInterface {
    RequestResponse:
        placeOrder(undefined)(undefined),
        processOrder(undefined)(undefined),
        shipOrder(undefined)(undefined),
        finishOrder(undefined)(undefined),
        getOrderStatus(OrderStatusRequest)(OrderStatusResponse),
        getMyOrders(OrdersListRequest)(OrdersListResponse)
}


constants {
    LOCATION_SERVICE_ORDER = "socket://localhost:9005",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_ORDER_INFO = "CREATE TABLE public.orders (
                                    id numeric NOT NULL,
                                    cart_id numeric NOT NULL,
                                    user_id numeric NOT NULL,
                                    address_to_ship text NOT NULL,
                                    payment_used text NOT NULL,
                                    status numeric NOT NULL
                                );
                                COMMENT ON TABLE public.orders IS 'Table that holds the order information. One user can have multiple orders';"
}