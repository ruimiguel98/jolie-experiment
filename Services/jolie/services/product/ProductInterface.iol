interface ProductInterface {
    RequestResponse:
        getProducts(void)(undefined),
        getProduct(undefined)(undefined),
        createProduct(undefined)(undefined),
        updateProduct(undefined)(undefined),
        deleteProduct(undefined)(undefined)
}

constants {
    LOCATION_SERVICE_PRODUCT = "socket://localhost:9001",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "172.30.0.1",
    SQL_DATABASE = "app-db",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE public.product
            (
                id numeric(16) NOT NULL,
                name text NOT NULL,
                description text NOT NULL,
                type text NOT NULL,
                price numeric NOT NULL,
                PRIMARY KEY (id)
            );

            ALTER TABLE IF EXISTS public.product
                OWNER to postgres;

            COMMENT ON TABLE public.product
                IS 'Table that holds all the products of the application.';",
}