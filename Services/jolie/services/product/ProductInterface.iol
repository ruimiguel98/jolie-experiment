type CreateRequest {
    .id: string // to accept UUID values
    .name: string
    .description: string
    .type: string
    .price: double
}

type CreateResponse {
    .message: string
}


interface ProductInterface {
    RequestResponse:
        all(void)(undefined),
        product(undefined)(undefined),
        create(CreateRequest)(undefined),
        update(undefined)(undefined),
        delete(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_PRODUCT = "socket://localhost:9051",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "localhost",
    // SQL_HOST = "172.30.0.1",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE product (
                                    id VARCHAR(40),
                                    description VARCHAR(50),
                                    product VARCHAR(50),
                                    price DECIMAL(5,2),
                                    type VARCHAR(50)
                                );

                                ALTER TABLE IF EXISTS public.product
                                    OWNER to postgres;

                                COMMENT ON TABLE public.product IS 'Table that holds all the products of the application.';",
}