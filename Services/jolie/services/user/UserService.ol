include "console.iol"
include "database.iol"
include "string_utils.iol"
include "time.iol"


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

    getCurrentDateTime@Time( )( currentDateTime ) // call this API to create a GLOBAL VARIABLE for current datetime to be present in logs
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
                "SELECT * FROM users WHERE id = :id" {
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
            println@Console( "[USER] - [" + currentDateTime + "] - [/create] - create user" )(  )

            getRandomUUID@StringUtils(  )( randomUUID )

            update@Database(
                "INSERT INTO users(id, real_name, email, phone, address, gender)
                 VALUES(:id, :realName, :email, :phone, :address, :gender);" {
                    .id = randomUUID, // UUID auto generated
                    .realName = request.realName,
                    .email = request.email,
                    .phone = request.phone,
                    .address = request.address,
                    .gender = request.gender
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1 ) {
                println@Console( "[USER] - [" + currentDateTime + "] - [/create] - user created with ID " + randomUUID )(  )
                customResponse.message = "User created with success"
                customResponse.user.id = randomUUID
                customResponse.user.realName = request.realName
                customResponse.user.email = request.email
                customResponse.user.phone = request.phone
                customResponse.user.address = request.address
                customResponse.user.gender = request.gender
            }
            else {
                println@Console( "[USER] - [" + currentDateTime + "] - [/create] - ERROR creating a new user" )(  )
                customResponse.error = "Error while creating the new user"
            }

            response -> customResponse
        } 
    ]  

    [ 
        update(request)(response) {
            println@Console( "[USER] - [" + currentDateTime + "] - [/update] - update user" )(  )

            update@Database(
                "UPDATE users SET real_name=:realName, email=:email, phone=:phone, address=:address, gender=:gender
                WHERE id=:id;" {
                    .id = request.id,
                    .realName = request.realName,
                    .email = request.email,
                    .phone = request.phone,
                    .address = request.address,
                    .gender = request.gender
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1 ) {
                println@Console( "[USER] - [" + currentDateTime + "] - [/update] - succes update on user with id " + request.id )(  )
                customResponse.message = "User updated with success"
            }
            else {
                println@Console( "[USER] - [" + currentDateTime + "] - [/update] - ERROR updating user with id " + request.id )(  )
                customResponse.error = "Error while creating the new user"
            }

            response -> customResponse
        } 
    ]
    
    [ 
        delete(request)(response) {
            println@Console( "[USER] - [" + currentDateTime + "] - [/delete] - delete user with ID " + request.id )(  )

            update@Database(
                "DELETE FROM users WHERE id = :id" {
                    .id = request.id
                }
            )(sqlResponse.status)

            // verify if the request was successfull
            if ( #sqlResponse.status == 1 ) {
                println@Console( "[USER] - [" + currentDateTime + "] - [/delete] - deleted user with ID " + request.id )(  )
                customResponse.message = "User deleted with success"
            }
            else {
                println@Console( "[USER] - [" + currentDateTime + "] - [/delete] - ERROR deleting the user" )(  )
                customResponse.error = "Error deleting the user"
                customResponse.tip = "Make sure that the user exists in the database"
            }

            response -> customResponse
        } 
    ]
}