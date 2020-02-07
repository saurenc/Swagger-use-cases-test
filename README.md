About
This project uses Rest-Assured API and TESTNG to test the following API - https://petstore.swagger.io/#/pet

The below tests are created to demonstrate 
1. Create a new pet using POST method and verifying if the created pet is in line with the supplied details
2. Retrieve the pet details using petID through GET method
3. Update the pet name using POST methid
4. Delete the pet details using DELETE method

The Pet Id is maintained in the properties file and can be changed

TestNG tests are executed in the order of priority mentioned
Tests can be configured to be executed at class level or individual tests level
Configuration is attached in the project for reference - TESTNG_Config.jpg

The POSTMAN tests are added under the project


RestAssured
Rest-Assured is an open-source Java Domain-specific language (DSL) that makes testing REST service simple. It simplifies things by eliminating the need to use boiler-plate code to test and validate complex responses. It also supports XML and JSON Request/Responses.
You can find more information in this webpage: http://rest-assured.io/

TestNG
More information can be found here - https://testng.org/doc/

