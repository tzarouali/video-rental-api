
Solution
--------
The REST API contained in this repo is a Play Framework Java application. Play uses Akka-Http as an embedded server so there's no need to setup an external one.


Requirements
------------
- Java 8
- Postgres


Setting up the Postgres database
--------------------------------
This application makes use of a Postgres database which is accessed using the URL jdbc:postgresql://localhost/video_rental_db via the user and password rest_api_user/rest_api_user

In order to setup this database, follow these steps:

1- Open a console and go to the project base directory
2- Execute as a Postgres administrator user (e.g. postgres): psql -U postgres -f sql/db_init.sql

After this command, you will have a Postgres DB with name "video_rental_db" ready to be used by the database user "rest_api_user".


Building and running the REST API
---------------------------------
To build the REST API project follow these steps. I'm assuming a Linux environment but this can be done from Windows as well:

1- Open a console and navigate to the project base directory
2- Execute: ./sbt stage (this will download all the required dependencies and build the application)
3- Navigate to the folder target/universal/stage/bin/
4- Execute: ./video-rental-api -Dplay.crypto.secret=test
5- You will see how the application starts (in port 9000) and logs to stdout

After executing the previous commands, the application will start listening to requests

To stop the application just press "Ctrl+C".


Executing the unit tests
------------------------
The application includes 29 unit tests that make use of JUnit as the testing framework and an in-memory H2 database.
To execute the unit tests included the only steps to follow are:

1- Open a console and navigate to the project base directory
2- Execute: ./sbt test


Appication design
-----------------
For the exercise I decided to use Play Framework because of its async-by-default nature. Embracing a paradigm like this can make an application better scalable and maintanable.

The libraries I'm using are more or less well known except for "Jooq". Jooq is a database-mapping library written in Java that implements the active record pattern. It offers a
very good SQL Java DSL with which to interact with the database in a safely way to prevent the typical errors that arise from executing stringly SQL queries from Java. It's well
equiped with a lot of helper methods to do almost everything you can when querying a database.

The application is structured in a very standard way:
- app/controllers: contains the different controllers serving the responses to the requests
- app/exceptions: contains a few exceptions used throughout the application
- app/jooq: contains the Jooq objects created from the initial SQL script and that map objects in the database
- app/model: contains the application business model
- app/repositories: contains the repositories used to access the database
- app/services: contains the services implementing the business logic
- app/utils: contains some utility classes to help implementing the business logic and some classes configuring aspects like general error handling and how the dependency injection
         library used, which is Guice, has to work
- logs: contains the application log
- conf: contains the configuration files for Play Framework and Jooq. One very important file in this directory is "routes". It contains the list of endpoints that the REST API
        is exposing and how are they mapped to the controllers
- project: contains configuration files for Play Framework
- sql: contains the SQL script with which to initialize the database
- test: contains the unit tests

One decision I took regarding the database design was to have a table containing the default prices and bonus per film type. This way, in the application when we need to compute the rental
price when the user makes a renting request, we just need to call the method FilmPriceCalculator.calculateRentPrice(...) without having to pass how much one film cost. Instead, just
using the Chain-of-responsibility pattern to implement the simple film price calculator we know how much does any of the films cost and how many bonus points are gained renting them.
This means that we can change the cost of the films and the bonus points without needing to make changes in the code.
Also, the customer bonus points per rental can be tracked by querying the table "customer_film_rent" and the total bonus points can be read from the table "customer".


Examples of GET requests (assuming localhost and default port 9000)
-------------------------------------------------------------------
The GET requests are pretty straightforward just by looking at the path parameters (if any) described in the "routes" file. For example, to retrieve the list of films by type we will need to use as request URI something like "http://localhost:9000/api/films/type/New Release" assuming that we're asking for films of type "New Release".


Examples of POST requests (assuming localhost and default port 9000)
--------------------------------------------------------------------
1- To create a new film we will need to POST to "http://localhost:9000/api/films/name/:name/filmType/:filmTypeId". Where the film type ID parameter has to be one of the available ones: New Release (1), Regular film (2) and Old film (3). One such example could be "http://localhost:9000/api/films/name/My new movie/filmType/1"

2- To create a customer we will need to POST to "http://localhost:9000/api/customers/name/:customerName/balance/:amount" passing in the URI the name and initial customer funds.

3- To rent films we need to POST to "http://localhost:9000/api/customerRents" passing in the request body a JSON object like:
    {
        "customerCode" : "6d72c220-d2bc-43d4-a36b-dda070120031",
        "filmIds" : [1,2,3],
        "numberRentDays" : "10"
    }

    The customer code can be read using the GET endpoint to retrieve the customers ("http://localhost:9000/api/customers"). The "filmIds" field contains the array of film
    identifiers to rent. These films can be read from the GET endpoint to retrieve films ("http://localhost:9000/api/films").

4- To return films we need to POST to "http://localhost:9000/api/customerReturns" passing in the request body a JSON object like:
    {
        "customerCode" : "f6aa2161-c7ec-472d-8c4b-6515e6613419",
        "filmIds" : [3]
    }
