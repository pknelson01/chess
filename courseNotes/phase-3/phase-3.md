# Chess Server Implementation Tips

## Web API Handlers
- If the web API requires an auth token, the handler can validate the auth token
  - Might put this logic in the handler base class so it can be shared by multiple handlers or in a Service class that can be shared by multiple services
    - create a base class that stores the logic for validating the auth token and then handlers can subclass that base class and inherit that logic
- Deserialize JSON request body to Java request object
  - translate from JSON to a Java request object
    - What is a request Object
- Call service class to perform the requested function, passing it the Java request object
- Receive the Java response object from service
- Serialize Java response object to JSON
- Send HTTP response back to the client with appropriate status code and response body
```java
LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);

LoginService service = new LoginService();
LoginResult result = service.login(request);

return gson.toJson(result);

// Best to create a separate class with fromJson and toJson methods instead of calling gson directly from handlers
```


## Tip - Avoid Code Duplication
- Areas for potential CD
  - HTTP Handler Classes
  - Service classes
  - Request/Result classes
  - DAO Classes
- User inheritance to avoid this kind of code duplication
  - put common code in base classes that can be inherited


## JSON Tips
- Make sure the field (variable) names in your request and response classes match exactly the names used in the specification (including capitalization)
  - Bugs will occur if not
- GSON does not serialize null fields (they will be missing in the JSON)
```java
class Response {
    String message;
}

class LoginResponse extends Response {
    String authtoken;
    String username;
}
```


## Generating an Auth Token
- There are multiple ways of doing this, here is an example:
```java
import java.util.UUID;
UUID.randomUUID().toString();
```


## Server Implementation Approach
1. Review class structure diagram
2. Create Java packages in your project to contain your server classes
3. Create your Server class and get static file handling to work so you can see the test page (see the Server Web API lecture for details)
4. Pick one Web API and get it working end-to-end, and test it with the test Web page(e.g., login)
5. As you go, write JUnit test cases for Service classes you create
6. Repeat for the other Web APIs until you have written all 7 and are passing all of the passoff tests