include "console.iol"
include "database.iol"
include "string_utils.iol"

include "../interfaces/ProductInterface.iol"

execution { concurrent }

// deployment info
inputPort Server {
    Location: "socket://localhost:8000/product/"
    Protocol: http { .format = "json" }
    Interfaces: Product
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
            "create table Product(id integer generated always as identity, " +
            "text varchar(255) not null, primary key(id))"
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
            "insert into product(id, name, price, availability) values (:id, :name, :price, :availability)" {
                .id = request.id,
                .name = request.name,
                .price = request.price,
                .availability = request.availability,
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