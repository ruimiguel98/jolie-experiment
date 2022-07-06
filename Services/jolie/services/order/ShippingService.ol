include "console.iol"
include "database.iol"
include "string_utils.iol"

include "../interfaces/ShippingInterface.iol"

execution { concurrent }

// deployment info
inputPort ShippingPort {
    Location: "socket://localhost:8005/"
    Protocol: http { .format = "json" }
    Interfaces: ShippingInterface
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

    // create check table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Shipment table already exists")());
        update@Database(
            "CREATE TABLE `e-commerce-app-db`.`shipment` ( 
                `id` INT(16) NOT NULL , 
                `cart_id` INT(16) NOT NULL , 
                `name` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL , 
                `address` VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL , 
                `status` INT(10) NOT NULL ) 
                ENGINE = InnoDB;"
        )(ret)
    }
}

// behaviour info
main
{
    [ 
        sendShipment(request)(response) {
            response.values = "This is just a test"

            // update@Database(
            //     "insert into shipment(id, cart_id, name, address, status) 
            //      values (:id, :cart_id, :name, :address, :status)" {
            //         .id = request.id,
            //         .cart_id = request.cart_id,
            //         .name = request.name,
            //         .address = request.address,
            //         .status = request.status
            //     }
            // )(response.status)
            
            update@Database(
                "insert into shipment(id, cart_id, name, address, status) 
                 values (:id, :cart_id, :name, :address, :status)" {
                    .id = 1,
                    .cart_id = 123,
                    .name = "Shipment for HVG",
                    .address = "One Microsoft Way, Redmond, WA 98052, USA",
                    .status = 1
                }
            )(response.status)
        }
    ]

    [   
        getShipmentStatus(request)(response) {
            query@Database(
                "select * from shipment where id=:id" {
                    .id = request.id
                }
            )(sqlResponse);

            response.status = "sqlResponse.row[0].status"

            // if (#sqlResponse.row == 1) {
            //     response -> sqlResponse.row[0]
            // }
        }
    ]
}