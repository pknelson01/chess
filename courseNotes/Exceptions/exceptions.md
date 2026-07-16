# Exceptions and Exception Handling
---

## Overview:
- Abnormal conditions that can occur in Java classes
- May be but are not necessarily errors
- Allow you to separate normal processing logic from abnormal processing logic
- Represented by classes and objects in Java

## Throwable:
- Exception:
  - RuntimeException:
    - NullPointerException
    - IndexOutOfBoundsException
    - ...
  - IOException:
  - ...
- Error:
  - VirtualMachineError:
    - OutOfMemoryError
  - LinkageError:
    - NoClassDefFoundError
  - ...

## Code Example:

```Java
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Exceptions {
  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);
    processFile(file);
  }

  private static void processFile(File file) {
    Scanner scan = null;

    try (Scanner scan = new Scanner(file)) {
      // ... use Scanner for something useful
      scan.clos();
    }
    catch (FileNotFoundException ex){
      System.out.printf("Could not find this file: %s", file);
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    finally {
      if (scan != null) {
        scan.close();
      }
    }
  }
}
```

- If you don't do anything when you catch an error that is called "Swallowing"
- You can create your own Exception by creating a class with the exception name and then saying "... extends Exception"
```java
public class MyException extends Exception {}
```

- You can catch multiple types of exceptions by placing an or bar ( | ) between each exception.

## Checked Exceptions:
- Java forces you to handle "Check Exceptions (i.e. IOException)"
- Unchecked:
  - NullPointerException, IndexOutOfBoundsException, ...
  - Errors

