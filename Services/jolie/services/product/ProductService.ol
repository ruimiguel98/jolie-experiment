include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./ProductInterface.iol"
// include "../locations.iol"
// include "../../sql/sql_scripts.iol" // import constant SQL scripts to be used instead of hardcoded text

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
        getProducts()(response) {
            query@Database(
                "select * from product"
            )(sqlResponse);
            response.values -> sqlResponse.row
        } 
    ]

    [ 
        getProduct(request)(response) {
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
        createProduct(request)(response) {
            update@Database(
                "insert into public.product(id, name, description, type, price) values (:id, :name, :description, :type, :price)" {
                    .id = request.id,
                    .name = request.name,
                    .description = request.description,
                    .type = request.type,
                    .price = request.price,
                }
            )(response.status)
        } 
    ]  

    [ 
        updateProduct(request)(response) {
            update@Database(
                "update product set name=:name where id=:id" {
                    .name = request.name,
                    .id = request.id
                }
            )(response.status)
        } 
    ]
    
    [ 
        deleteProduct(request)(response) {
            update@Database(
                "delete from product where id=:id" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]
}