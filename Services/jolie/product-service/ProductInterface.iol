type Product {
    .id: string
    .description: string
    .product: string
    .type: string
    .price: double
}

type Products {
    .products[1, *]: Product // an array of Products
}

type CreateRequest {
    .description: string
    .product: string
    .type: string
    .price: double
}

type DeleteRequest {
    .id: string
}


interface ProductInterface {
    RequestResponse:
        all(void)(Products),
        product(undefined)(Product),
        create(CreateRequest)(Product),
        update(Product)(Product),
        delete(DeleteRequest)(string)
}

constants {
    LOCATION_SERVICE_PRODUCT = "socket://host.docker.internal:9051",

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",

    SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE product (
                                    id UUID,
                                    description VARCHAR(50),
                                    product VARCHAR(50),
                                    price DECIMAL(20,2),
                                    type VARCHAR(50),
                                    CONSTRAINT product_pkey PRIMARY KEY (id)
                                );

                                ALTER TABLE IF EXISTS public.product
                                    OWNER to postgres;

                                COMMENT ON TABLE public.product IS 'Table that holds all the products of the application.';",
}