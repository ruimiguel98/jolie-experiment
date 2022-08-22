include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./UserInterface.iol"

execution { concurrent }

// deployment info
inputPort UserPort {
    Location: LOCATION_SERVICE_USER
    Protocol: http { .format = "json" }
    Interfaces: UserInterface
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
        install (SQLException => println@Console("User table already there")());
        update@Database(SQL_CREATE_TABLE_USER)(ret)
    }
}

// behaviour info
main
{
    [ 
        all()(response) {
            query@Database(
                "SELECT * FROM users"
            )(sqlResponse);

            response.values -> sqlResponse.row
        } 
    ]

    [ 
        user(request)(response) {
            
            println@Console( "[USER] - Requesting the user with id " + request.id )(  )

            query@Database(
                "SELECT * FROM users WHERE id=:id::numeric" {
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
            messageSuccess = "The user was created with success!";

            update@Database(
                "INSERT INTO public.users(id, address, cart_products, credit_card, email, real_name, phone)
                 VALUES(:id::numeric, :address, :cartProducts, :creditCard, :email, :realName, :phone);" {
                    .id = request.id,
                    .address = request.address,
                    .cartProducts = request.cartProducts,
                    .creditCard = request.creditCard,
                    .email = request.email,
                    .realName = request.realName,
                    .phone = request.phone,
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
                "UPDATE public.users SET address=:address, cart_products=:cartProducts, credit_card=:creditCard, email=:email, real_name=:realName, phone=:phone
                WHERE id=:id::numeric;" {
                    .id = request.id,
                    .address = request.address,
                    .cartProducts = request.cartProducts,
                    .creditCard = request.creditCard,
                    .email = request.email,
                    .realName = request.realName,
                    .phone = request.phone
                }
            )(response.status)
        } 
    ]
    
    [ 
        delete(request)(response) {
            update@Database(
                "DELETE FROM users WHERE id=:id::numeric" {
                    .id = request.id
                }
            )(response.status)
        } 
    ]
}