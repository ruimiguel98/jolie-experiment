include "console.iol"
include "database.iol"
include "string_utils.iol"

include "./AddressInterface.iol"

execution { concurrent }

// deployment info
inputPort AddressPort {
    Location: LOCATION_SERVICE_ADDRESS
    Protocol: http { .format = "json" }
    Interfaces: AddressInterface
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

    // create check table if it does not exist
    scope (createTable) {
        install (SQLException => println@Console("Address table already exists")());
        update@Database(SQL_CREATE_ADDRESS_INFO)(ret)
    }
}

// behaviour info
main
{
    [ 
        addAddressInfo(request)(response) {

            println@Console( "Adding address information for user with ID " + request.user_owner )(  )

            update@Database(
                "INSERT INTO address(id, user_owner, address, postal_code, city) 
                  VALUES (:id::numeric, :user_owner::numeric, :address, :postal_code, :city);" {
                    .id = request.id,
                    .user_owner = request.user_owner,
                    .address = request.address,
                    .postal_code = request.postal_code,
                    .city = request.city
                }
            )(response.status)
        }
    ]

    [ 
        deleteAddressInfo(request)(response) {

            println@Console( "Deleting address information" )(  )

            update@Database(
                "DELETE FROM address WHERE id=:id::numeric;" {
                    .id = request.id,
                }
            )(response.status)
            
        }
    ]

    [ 
        getSavedAddressInfoList(request)(response) {

            USER_DOES_NOT_EXIST_MESSAGE = "The provided user id " + request.user_owner + " is not in the system"

            println@Console( "Fetching list of saved addresss information for user " + request.user_owner )(  )

            query@Database(
                "SELECT * FROM address WHERE user_owner=:user_owner::numeric" {
                    .user_owner = request.user_owner
                }
            )(sqlResponse)

            if (#sqlResponse.row >= 1) {
                response -> sqlResponse
            }
            else {
                response.message -> USER_DOES_NOT_EXIST_MESSAGE
            }
        } 
    ]
}