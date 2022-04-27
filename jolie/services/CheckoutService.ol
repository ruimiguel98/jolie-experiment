include "console.iol"
include "database.iol"
include "string_utils.iol"

include "../interfaces/CheckoutInterface.iol"
include "../interfaces/PaymentInterface.iol"

execution { concurrent }

// deployment info
inputPort CheckoutPort {
    Location: "socket://localhost:8002/"
    Protocol: http { .format = "json" }
    Interfaces: CheckoutInterface
}

outputPort PaymentPort {
    Location: "socket://localhost:8003/"
    Protocol: http { .format = "json" }
    Interfaces: PaymentInterface
}

// prepare database connection (creates table if does not exist)
// init
// {
//     with (connectionInfo) {
//         .username = "jolie";
//         .password = "Ia@bNf-9NAd!t(@z";
//         .host = "localhost";
//         .database = "e-commerce-app-db"; // "." for memory-only
//         .driver = "mysql"
//     };

//     connect@Database(connectionInfo)();
//     println@Console("Successfull connection to the MySQL database")();

//     // create check table if it does not exist
//     scope (createTable) {
//         install (SQLException => println@Console("Cart table already exists")());
//         update@Database(
//             "CREATE TABLE `e-commerce-app-db`.`cart` ( 
//                 `id` INT(16) NOT NULL , 
//                 `name` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL , 
//                 `product` INT(16) NOT NULL , 
//                 `total` INT(10) NOT NULL ) 
//                 ENGINE = InnoDB;"
//         )(ret);

//         update@Database(
//             "CREATE TABLE IF NOT EXISTS `e-commerce-app-db`.`cart_procuct` ( 
//                 `id` INT(16) NOT NULL , 
//                 `product_id` INT(16) NOT NULL , 
//                 `cart_id` INT(16) NOT NULL ) 
//                 ENGINE = InnoDB;"
//         )(ret)
//     }
// }

// behaviour info
main
{
    [ 
        checkoutPay()(response) {   
            processPayment@PaymentPort( "mytest" )()
            response.values = "This is just a test"
        } 
    ]
}