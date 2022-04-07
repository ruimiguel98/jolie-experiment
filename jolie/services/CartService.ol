include "console.iol"
include "database.iol"
include "string_utils.iol"

include "../interfaces/CartInterface.iol"

execution { concurrent }

// deployment info
inputPort Server {
    Location: "socket://localhost:8000/"
    Protocol: http { .format = "json" }
    Interfaces: Cart
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

    // create cart table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Cart table already exists")());
        update@Database(
            "CREATE TABLE `cart` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT,
            `product_id` varchar(255) DEFAULT NULL,
            PRIMARY KEY (`id`),
            ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8"
        )(ret)
    }
}

// behaviour info
main
{
    [ cartRetrieve()(response) {
        query@Database(
            "select * from cart"
        )(sqlResponse);
        response.values -> sqlResponse.row
    } ]
    [ cartAdd(request)(response) {
        update@Database(
            "insert into cart(id, name, price, availability) values (:id, :name, :price, :availability)" {
                .id = request.id,
                .name = request.name,
                .price = request.price,
                .availability = request.availability,
            }
        )(response.status)
    } ]
    [ cartDelete(request)(response) {
        update@Database(
            "delete from cart where id=:id" {
                .id = request.id
            }
        )(response.status)
    } ]
}