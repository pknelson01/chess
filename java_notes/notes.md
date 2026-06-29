# My Notes:

### History and Overview of Java:

- Syntax is very similar to C++
  - Built in Garbage Collection (deletes when no longer using)
  - References instead of pointers 
  - all data-types in Java are the same size
  - Classes are dynamically linked at runtime (no separate link step)

### Java Architecture

- What does it mean to be compiled?
  - Source Code -> compiler -> executable code -> device
  - The compiler is designated for the specific device (mac, pc, android,...). The idea is that the source code does not need to be changed, the compiler is what makes it work with various devices. 
  - Advantage: Compiled code is very fast
  - Disadvantage: Not portable
- What does it mean to be interpreted?
  - Source Code -> Interpreter 
  - You run the source code on the interpreter for the specific device
  - Advantage: Very portable
  - Disadvantage: Slower than compiled code
- Hybrid (Java):
  - Source Code -> Compiler -> Java Byte Code -> JVM (for each specific device(Mac, PC, Android,...))
  - JVM = Java Virtual Machine
  - The JVM will take the Java Byte Code and interpret it for whatever device the JVM is configured for
- JIT = Just in Time Compilation
- Hotspot VM = Dynamically recompilation at runtime
- Uses a generational garbage collector:
  - checks newer memory and frees it up more often and that allows it to run faster. 

### Writing Java Code
- when you write a class, you place that code in a .java file. The file name needs to be the same name as the class name. 

### Javadoc
- Multi-line comment with an extra asterisk /** */

### Primitive Data Types:
- Byte - Integral - Smallest size (Half the size of a short)
- Short - Integral - Half the size of Int
- Int - Integral - Standard (32bit)
- Long - Integral - Twice the size of Int
- Float - Decimal #'s
- Double - Decimal #'s - Twice the size of a Float
- Chat - 16bit
- Boolean - True/False

### Strings:
- Strings are classes, in C++ they are character arrays but that is not the case with java
- They are immutable, meaning they cannot change
- **String Methods**
  - int length()
  - char charAt()
  - String trim()
  - boolean startsWith(String)
  - int indexOf(int)
  - int indexOf(String)
  - String substring(int)
  - String substring(int, int)
- **Special Characters**
  - \n (newline)
  - \t (tab)
  - \" (double quote)
  - \' (single quote)
  - \\ (backslash)
  - \b (backspace)
  - \uXXXX (insert the Unicode character represented by XXXX)
  - \r (carriage return—return to the beginning of the current line—obsolete)
  - \f (form feed—advance to the next line—obsolete)

### Packages:
- Provide a way to organize classes into logical groups
- Can have sub-packages (separated by .(dots))
- Specify the package for a class with a 'package' statement at the top of the .java file
- Files must be in a directory structure that matches the path structure
- The package name becomes part of the class name. Example: Java has two date classes: 
  - java.util.Date
  - java.sql.Date
- Code Example:
``` java
package edu.byu.cs;

public class Student{}
```

### Imports:
- It is a shorthand for the fully-qualified package name
- They do not increase the size of your compiled .class file
- If used, they appear at the top of the file
- The wildcard * imports all classes in the package but not the sub-packages 
- You do not need to import in the following cases:
  - The class you are using is in the java.lang.package
  - the class you would import is in the same package as the class that needs to use it

### Classpath:
- An environment variable that contains a list of directories that contain.class files, package base directories, or other resources your application needs to access
-  .(current directory) is implicitly on the CLASSPATH if you don't set a CLASSPATH 
-  Can use -classpath command line param
-  Colon separate on MacOS and Linus
-  Semicolon on Windows
 
### 
- Classes are a template for your object (Classes are compile time things, objects are runtime things)
- All code with exception for packages and imports will be inside the class
- To create an object you will need to use the new keyword
  - Strings and arrays are a slight exception to this

### Object References:
- Reference will be on the Stack or Heap
- Heap is where the memory is allocated for objects
```Java
Date dt;
dt = new Date();
```
- Creation of a reference does not create an object
- Multiple references can relate to the same object

### Static Variabls and Methods
- Instance Variables
  - Each object gets its own copy of all of the instance variables
  - Most variables should be instance variables
  - Example:
    - Allows two date objects to represent different dates
- Static Variables
  - Associated with the class not with the instances
  - Use in special cases where you won't create instances of a class, or all instances should share the same values
  - Example:
    - If the variable of a Date class where static, all dates in a program would represent the same date

### Accessor & Mutators
- AKA Getters / Setters
- Allow you to control access to instance variables
  - Make variables private and only allow access through getters and setters

### Constructor Methods
- The name of the constructor must be the same as the class that contains it
- Like a method without a return type
- All classes have at least one constructor
- Classes can have multiple constructors (w/ different param types)
- Constructors invoke each other with 'this(...)'
- Constructors invoke parent constructor with 'super(...)'
- this(...) or super(...) is always the fist statement

### Inheritance
- Inherit members of a parent (super) class without explicitly writing them in the child class
- Use the 'extends' keyword
```java
public class Employee
extends Person {...}
```
- Use the "is-a" rule
- Every class extends Object
- What is inherited?
  - All instance variables (even private)
  - all non-private, non-static methods
- What is not inherited?
  - Constructors
  - Static Methods
  - Private methods

### Method Overriding
- A subclass replaces an inherited method by redefining it
  - Argument list must be the same
  - Return type must be the same (or a subclass)
  - Must not make access modifier more restrictive
  - Must not throw new or broader checked exceptions
- Can call the overridden version of the method by using super
  - Examples: Person.java, Employee.java (see toString() methods)
- Use @Override annotation to prevent typos
  - Examples:
    - In previous example, replace toString with tostring (lowercase 'S') and see what happens
    - Remove @Override and replace toString with tostring
```Java
// Person.java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }
}

// Employee.java
public class Employee extends Person {
    private String company;

    public Employee(String name, String company) {
        super(name);
        this.company = company;
    }

    @Override
    public String toString() {
        return super.toString() + ", Company: " + company;
    }
}
```
### Implementing a Hashcode Method
#### The general contract of hashCode:
- Whenever it is invoked on the same object more than once during an execution of a Java application, **the hashCode method must consistently return the same integer**, provided no information used in equals comparisons on the object is modified. This integer need not remain consistent from one execution of an application to another execution of the same application.
- If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of the two objects **must produce the same integer result**.
- It is **not required** that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, **the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hash tables**.

- Hash each value in the object that you want included in the hash
  - For integral numbers, use the number as the hash for that value
  - For Strings (or other objects), call the object's hashCode() method
  - For Objects that may be null, can use the Objects.hashCode() method
  - For arrays, either hash each element in the array, or call Arrays.hashCode()
- While computing a hash, accumulate the hashed values, multiplying the accumulated value by an odd prime number (not 2) before adding the next hashed value
  - We usually multiply by 31
- Resources for learning more:
  - API documentation for hashCode() method in Object class
  - [https://www.baeldung.com/java-hashcode](https://www.baeldung.com/java-hashcode)
#### hashCode() Method Example
```java
public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (int) id;
    hash = 31 * hash + (name == null ? 0 : name.hashCode());
    hash = 31 * hash + (email == null ? 0 : email.hashCode());
    return hash;
}
```

### Method Overloading
- Reuse a method name with a different argument list
- Example: The PrintWriter class:

```java
public class PrintWriter extends Writer {
    public void print(boolean)
    public void print(char)
    public void print(char[])
    public void print(double)
    public void print(float)
    public void print(int)
    public void print(long)
    public void print(Object)
    public void print(String)

    public void println(boolean)
    public void println(char)
    ...
}
```

### The Final Keyword
- Final Variables
  - Can't be changed after a value is assigned
  - For instance variables, the last chance to assign is in a constructor
  - `public final int myVariable = 10;`
- Final Reference Variables
```Java
public final ArrayList list = new ArrayList();
```
  - What can't change? What can change?
- Final Methods
```Java 
public final void myMethod() {...}
```
  - What can you not do to a final method?
  - **Hint:** In Java, all non-final instance methods are virtual
    - Applying FINAL to a method means that method is non-virtual and that means you cannot override it

### The 'this' Reference

- **What is it?**
  - `this` is a reference to the current object — the object that is running the method

- **When do I have to use it?**
  - When a parameter name is the same as an instance variable name, you need `this` to tell Java you mean the instance variable and not the parameter:
  
```java
  public void setFirstName(String firstName) {
      this.firstName = firstName; // this.firstName = instance variable, firstName = parameter
  }
```

- **When can it be inferred by the compiler?**
  - When there is no naming conflict, `this` is optional — the compiler assumes you mean the instance variable:
  
```java
  public void printName() {
      System.out.println(firstName); // compiler knows you mean this.firstName
  }
```

### Enums
- Like a class
- User where you otherwise have unrestricted String with only a few values being valid
```java
public enum Gender {
    Male, Female;

    @Override
    public String toString() {
        return this == Male ? "m" : "f";
    }
}
```

### Object-Oriented Design Overview
- Decompose a program into classes
- Use separate classes to represent each concept in the application domain
- Identify relationships between classes
  - Represent Is-A relationships with inheritance
  - Represent Has-A and Uses-A relationships with references
- Keep data private
- Not all fields need individual getters and setters (think about who needs access)
- Use a standard structure for class definitions
- Break up classes that have too many responsibilities
- Make the names of your classes and methods reflect their responsibilities
- Classes have noun names, methods usually have verb names
- Use static methods as an exception, not a general rule
```java
public class MyClass {
    // Static Variables

    // Instance Variables

    // Main Method (if it exists)

    // Constructors

    // Methods
    // (grouped by functionality, not by accessibility, not by static vs
    //  instance, etc)
}
```

### Java Records
- Very common to have Java classes that only exist to represent data
- These are called POJO's (plain old java object)
- Java Records make it so that we don't have to write POJO's
```java
public record Pet(int id, String name, String type){}
```
- Immutability 
- Simplified constructor syntax
  - Pet p = new Pet(1, "Obi", "Dog");
- Automatic getters
  - Access by using field name (without get): p.name();
- Automatic equals/hashcode/toString that compares all fields
- Since you can't change the record, you can create a method that "changes" the record by taking in the record and then just returning a new instance. 
```java
public record Pet(int id, String name, String type) {
    Pet rename(String newName) {
        return new Pet(id, newName, type);
    }
}
```





