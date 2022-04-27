include "console.iol"
include "database.iol"
include "string_utils.iol"

include "../interfaces/ProductInterface.iol"

execution { concurrent }

// deployment info
inputPort Server {
    Location: "socket://localhost:8000"
    Protocol: http { .format = "json" }
    Interfaces: ProductInterface
}

// prepare database connection (creates table if does not exist)
init
{
    with (connectionInfo) {
        .username = "jolie";
        .password = "Ia@bNf-9NAd!t(@z";
        .host = "localhost";
        .database = "e-commerce-app-db"; // "." for memory-only
        .driver = "mysql"
    };
    connect@Database(connectionInfo)();
    println@Console("Successfull connection to the MySQL database")();

    // create table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Product table already there")());
        update@Database(
            "CREATE TABLE `e-commerce-app-db`.`product` ( 
                `id` INT(16) NOT NULL , 
                `name` VARCHAR(100) NOT NULL , 
                `description` VARCHAR(500) NOT NULL , 
                `type` INT(16) NOT NULL , 
                `price` FLOAT(16) NOT NULL , 
                PRIMARY KEY (`id`)
            ) ENGINE = InnoDB;"
        )(ret)

        update@Database(
            "CREATE TABLE IF NOT EXISTS `e-commerce-app-db`.`cart_procuct` ( 
                `id` INT(16) NOT NULL , 
                `product_id` INT(16) NOT NULL , 
                `cart_id` INT(16) NOT NULL ) 
                ENGINE = InnoDB;"
        )(ret)
    }
}

// behaviour info
main
{
    [ retrieveAll()(response) {
        query@Database(
            "select * from product"
        )(sqlResponse);
        response.values -> sqlResponse.row
    } ]
    [ create(request)(response) {
        update@Database(
            "insert into product(id, name, description, type, price) values (:id, :name, :description, :type, :price)" {
                .id = request.id,
                .name = request.name,
                .description = request.description,
                .type = request.type,
                .price = request.price,
            }
        )(response.status)
    } ]
    [ retrieve(request)(response) {
        query@Database(
            "select * from product where id=:id" {
                .id = request.id
            }
        )(sqlResponse);
        if (#sqlResponse.row == 1) {
            response -> sqlResponse.row[0]
        }
    } ]    
    [ update(request)(response) {
        update@Database(
            "update product set name=:name where id=:id" {
                .name = request.name,
                .id = request.id
            }
        )(response.status)
    } ]
    [ delete(request)(response) {
        update@Database(
            "delete from product where id=:id" {
                .id = request.id
            }
        )(response.status)
    } ]
}