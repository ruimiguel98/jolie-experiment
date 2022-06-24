type PaymentAddRequest {
    .id: int
    .type_payment: string
    .user_owner: int
    .card_number: string
    .cart_expire_date: string
    .name_on_card: string
    .card_security_code: int
}

type PaymentDeleteRequest {
    .id: int
}


interface PaymentInterface {
    RequestResponse:
        addPaymentInfo(PaymentAddRequest)(undefined),
        deletePaymentInfo(PaymentDeleteRequest)(undefined),
        processPayment(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_PAYMENT = "socket://localhost:9003",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    // SQL_HOST = "172.30.0.1",
    SQL_HOST = "localhost",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_PAYMENT_INFO = "CREATE TABLE public.payment (
                                    id numeric NOT NULL,
                                    type_payment varchar NOT NULL,
                                    user_owner numeric NULL,
                                    payment_info json NOT NULL
                                );
                                COMMENT ON TABLE public.payment IS 'Table that holds the several payment information for the user to not have to input it all the time, one or more payment info can be associated with the same user.';"
}