# trivago-test
Case Study for Trivago

Swagger documentation is available at http://localhost:8080/api/doc


### Just for the demo purposes

* Location information should reference tables like cities, countries, etc., but locations are stored as a row and are immutable. 
When updating accommodation and supplying location id != 0, then it considered that location hasn't changed. If id == 0, then previous location deleted and new created.
* Some constants are used in place as strings, but should be organized in different classes.
* Hotelier id is just passed as a variable, but should be a data from JWT supplied by authentication service like KeyCloak. Also, there is no hotelier information, just an abstract id.