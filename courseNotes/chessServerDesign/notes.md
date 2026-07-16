# Chess Server Design

**Phase 4 is where the server will actually be complete**

## Phase 2 is just the design process for the implementation of Phase 3
- Server:
  - the backend
- Client:
  - the frontend
- Web: 
  - the component between the Server and Client (communication)
  - Client makes HTTP Requests to the Server and the Server returns HTTP Responses to the Client


## Components for each:
- Server:
  - Website
    - home-page for the server
  - Web API (set of classes that you can use in the program - server will publish to the world a set of functions that a user can use to play chess)
    - register a new user
    - login
    - logout
    - join a game
    - listing the games available to play
    - ...
- Client:
  - Web Browser (Test Page) - a way of calling the various functions of out Web API
  - Console Client
  - Test Driver (JUnit tests) - the way we are going to test our server

## Sing Responsibility Principle (SRP)
- Every class represents one well-defined concept, all its functionality relates to that one concept, and it has a good name that described what it represents (should be a noun)
- Every method does a singular thing and has a good name

## Avoid Code Duplication
- If there is, create a method that you can use instead

## Encapsulation
- Classes and methods should hide their internal implementation details
- Class members should be private when possible

## Web API
- Will have 7 functions:
  - login
    - logs a user in with an existing account
    - returns an auth token
  - logout
    - Logs a user out with an existing account
  - 
  - clear application
    - will delete all data from the database (used for testing purposes)
    - url: /db
  - register
    - register a new user
    - url: /user
  - 
- Endpoints <=> one of those 7 functions
- Body & Success/failure field(s) will contain information in JSON
- Failure Messages have an "Error:" attribute