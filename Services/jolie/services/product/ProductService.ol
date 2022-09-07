include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./ProductInterface.iol"

execution { concurrent }

// deployment info
inputPort ProductPort {
    Location: LOCATION_SERVICE_PRODUCT
    Protocol: http { .format = "json" }
    Interfaces: ProductInterface
}

// prepare database connection (creates table if does not exist)
init
{
    with (connectionInfo) {
        .username = SQL_USERNAME;
        .password = SQL_PASSWORD;
        .host = SQL_HOST;
        .database = SQL_DATABASE; // "." for memory-only
        .driver = SQL_DRIVER
    };
    connect@Database(connectionInfo)();
    println@Console("Successfull connection to the PostgreSQL database")();

    // create table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Product table already there")());
        update@Database(SQL_CREATE_TABLE_PRODUCT)(ret)
    }
}

// behaviour info
main
{
    [ 
        all()(response) {
            query@Database(
                "select * from product"
            )(sqlResponse);
            response.values -> sqlResponse.row
        } 
    ]

    [ 
        product(request)(response) {
            query@Database(
                "select * from product where id=:id" {
                    .id = request.id
                }
            )(sqlResponse);
            
            if (#sqlResponse.row == 1) {
                response -> sqlResponse.row[0]
            }
        } 
    ]

    [ 
        create(request)(response) {
            messageSuccess = "The product was created with success!";

            update@Database(
                "insert into public.product(id, product, description, type, price) values (:id, :product, :description, :type, :price)" {
                    .id = request.id,
                    .product = request.product,
                    .description = request.description,
                    .type = request.type,
                    .price = request.price,
                }
            )(sqlResponse.status)

            if (#sqlResponse.status == 1) {
                response.message -> messageSuccess
            }
        } 
    ]  

    [ 
        update(request)(response) {
            update@Database(
                "UPDATE product SET product=:product WHERE id=:id" {
                    .product = request.product,
                    .id = request.id
                }
            )(response.status)
        } 
    ]
    
    [ 
        delete(request)(response) {
            update@Database(
                "DELETE FROM product WHERE id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]
}