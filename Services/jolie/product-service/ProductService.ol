include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"

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

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
}

// behaviour info
main
{
    [ 
        all()(response) {
            println@Console( "[PRODUCT] - [" + currentDateTime + "] - [/all] -  fetch all products" )(  )

            query@Database(
                "select * from product"
            )(sqlResponse);

            response.products -> sqlResponse.row
        } 
    ]

    [ 
        product(request)(response) {
            query@Database(
                "select * from product where id = :id::uuid" { // type cast is important for POSTGRES
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
            println@Console( "[PRODUCT] - [" + currentDateTime + "] - [/create] - create product" )(  )

            getRandomUUID@StringUtils(  )( randomUUID )

            update@Database(
                "INSERT INTO product(id, product, description, type, price) VALUES (:id::uuid, :product, :description, :type, :price)" {
                    .id = randomUUID, // UUID auto generated
                    .product = request.product,
                    .description = request.description,
                    .type = request.type,
                    .price = request.price,
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1 ) {
                println@Console( "[PRODUCT] - [" + currentDateTime + "] - [/create] - product created with ID " + randomUUID )(  )
                customResponse.id = randomUUID // UUID auto generated
                customResponse.product = request.product
                customResponse.description = request.description
                customResponse.type = request.type
                customResponse.price = request.price
            }
            else {
                println@Console( "[PRODUCT] - [" + currentDateTime + "] - [/create] - ERROR creating a new product" )(  )
                customResponse.error = "Error creating the new product!"
            }

            response -> customResponse
        } 
    ]  

    [ 
        update(request)(response) {
            update@Database(
                "UPDATE product SET product = :product, description = :description, type = :type, price = :price::numeric WHERE id = :id::uuid" {
                    .id = request.id, // UUID auto generated
                    .product = request.product,
                    .description = request.description,
                    .type = request.type,
                    .price = request.price
                }
            )(sqlResponse.status)

            response.id = request.id // UUID auto generated
            response.product = request.product
            response.description = request.description
            response.type = request.type
            response.price = request.price
        } 
    ]
    
    [ 
        delete(request)(response) {
            update@Database(
                "DELETE FROM product WHERE id = :id::uuid" {
                    .id = request.id
                }
            )(response.status)

            message = "Product deleted with success"

            response -> message
        } 
    ]
}