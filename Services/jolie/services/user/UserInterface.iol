type CreateRequest {
    .id: int
    .address: string
    .cartProducts: string
    .creditCard: string
    .email: string
    .realName: string
    .phone: string
}

type CreateResponse {
    .message: string
}


interface UserInterface {
    RequestResponse:
        all(void)(undefined),
        user(undefined)(undefined),
        create(CreateRequest)(CreateResponse),
        update(undefined)(undefined),
        delete(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_USER = "socket://localhost:9052",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "localhost",
    // SQL_HOST = "172.30.0.1",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_USER = "CREATE TABLE public.users (
                                id int4 NOT NULL,
                                address varchar(255) NULL,
                                cart_products _int8 NULL,
                                credit_card varchar(255) NULL,
                                email varchar(255) NULL,
                                real_name varchar(40) NULL,
                                phone varchar(255) NULL,
                                CONSTRAINT users_pkey PRIMARY KEY (id)
                            );"
}