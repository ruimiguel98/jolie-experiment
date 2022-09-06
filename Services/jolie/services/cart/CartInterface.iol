type CreateCartRequest {
    .userId: string // to accept UUID values
}

type UpdateCartRequest {
    .id: string // to accept UUID values
    .userId: string // to accept UUID values
}

type AddProductRequest {
    .cartId: string // to accept UUID values
    .productId: string // to accept UUID values
}

type RemoveProductRequest {
    .cartId: string // to accept UUID values
    .productId: string // to accept UUID values
}

interface CartInterface {
    RequestResponse:
        // CRUD operations
        all(undefined)(undefined),
        cart(undefined)(undefined),
        create(CreateCartRequest)(undefined),
        update(UpdateCartRequest)(undefined),
        delete(undefined)(undefined),

        // OTHER operations
        addProduct(AddProductRequest)(undefined),
        removeProduct(RemoveProductRequest)(undefined)
}


constants {
    LOCATION_SERVICE_CART = "socket://localhost:9057",
    LOCATION_SERVICE_PRODUCT = "socket://host.docker.internal:9051", // used by cart orchestrator

    SQL_USERNAME = "postgres",
    SQL_PASSWORD = "welcome1",
    SQL_HOST = "host.docker.internal",
    SQL_DATABASE = "postgres",
    SQL_DRIVER = "postgresql",


    SQL_CREATE_CART_INFO = "CREATE TABLE public.cart (
                                id varchar(128) NOT NULL,
                                user_id varchar(128) null,
                                CONSTRAINT cart_pkey PRIMARY KEY (id)
                            );
                            CREATE TABLE public.cart_products (
                                cart_id varchar(128) NOT NULL,
                                product_id varchar(128) NOT NULL,
                                quantity numeric NOT NULL DEFAULT 1
                            );
                            COMMENT ON TABLE public.cart IS 'Table that holds the cart information.';
                            COMMENT ON TABLE public.cart_products IS 'Table that holds the cart products information.';"
}