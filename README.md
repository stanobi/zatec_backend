# Technical Challenge (Backend)
This is an application which displays all jokes categories, all star wars people and search result from jokes and star wars people

##Online version
Application is successfully deploy to heroku and you can visit via https://zatec-backend.herokuapp.com/swagger-ui.html

## Content
View Project Structure description and Instruction.

```
.
├── /src/
├──── /main/
├────── /java/
├──────── /com/zatec/technical_challenge/
├────────── /configuration                          -> contains configuration classes
├────────── /controller                             -> contains all restful api controller
├────────── /dto                                    -> contains all generated data transfer objects
├────────── /api                                    -> contains all generated api classes
├────────── /exception                              -> contain exception handling class
├────────── /rest                                   -> contains feignclient interface used to consume api
├────────── /service                                -> contains classes containing the app business logic
├────────── /util                                   -> contains classes containing common operations and values
├────────── TechnicalChallengeApplication           -> Main Class
├────── /resources
├──────── /openapi/api-specification.yml            -> open-api configuration yml file
├──────── /static
├──────── /templates
├──────── application.properties                    -> contains all external configurations
├──── /test                                         -> contains all test classes
├── /target                                         -> contains auto generated file
├──── /generated-sources
├──────── /openapi/src/main/java                    -> contains auto generated java codes from openapi yml file
├── .gitignore                                      -> contains all files and folder that shouldn't be pushed to git
├── pom.xml                                         -> file used to manage dependencies 
└── README.md                                       -> application structure description and startup/setup guide
    
Instructions 

    1. To setup the application,  
        a. Ensure that jdk8 is installed
        b. Ensure that maven is installed 
        c. You will have to clone the project.
        d. Go to the project root directory on the terminal 
        e. Run the maven command on the terminal "mvn clean install" to generate the openapi java classes
        f. Mark /target/generated-sources/openapi/src/main/java as generated source root.
        g. Ensure that port 8080 is available to be used
    
    2. Start up the application by running the command on the terminal 'mvn spring-boot:run' in the root directory
    
    3. Considering that we have the swagger-ui configured for the service, View api detail via browser by visiting "http://localhost:8080/swagger-ui.html"

    4. To Test: we can test via swagger-ui by visiting any of the links provided in the step 3 and providing the parameter as required. 

    5. To run unit test, you can run the command on the terminal 'mvn test'

```