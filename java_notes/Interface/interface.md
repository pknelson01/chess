# Interface

## Polymorphism:
- Poly = many
- Morph = change
- Polymorphism = many forms
- Objects can take on many forms in an object-orients program

```java
// Examples:

Employee emp = new Employee(); // good
Person emp = new Employee(); // good
Object emp = new Employee(); // good
Dog emp = new Employee(); // bad - fails the is-a test
```

## Result in Memory
```java
Person emp = new Employee(); // would give a restriction
```

## Reasons for Polymorphism
- Heterogeneous Collections
  - Collections (such as arrays or ArrayLists) of a parent type that contain children of different types
- Polymorphic Parameters
  - Parameters in a method call that expect a parent reference or object but receive a child of the expected type
- **_REMEMBER_** Arrays in Java are objects that contain references

## Polymorphism Example Part I:
- Create a simulation of a city, using an inheritance hierarchy of vehicles
- Will have different kinds of vehicles in the city that can all 'go'
- Will start the simulation by placing vehicles in an array and calling a go() method
- This is a Heterogeneous Collection because it is going to store a collection of various vehicles
- The go() method would make most sense to be in the parent class (vehicle)
```java
public class CitySimulation {
    public void run() {
        Vehicle [] vehicles = new Vehicle[6];
        vehicles[0] = new Car();
        vehicles[1] = new Car();
        vehicles[2] = new Truck();
        vehicles[3] = new Truck();
        vehicles[4] = new Boat();
        vehicles[5] = new Airplane();

        for(int i = 0; i < vehicles.length; i++) {
            vehicles[i].go();
        }
    }
}
```

# Abstract Classes

## Abstract Vehicle Class

```java
public abstract class Vehicle{
    public abstract void go();
}
```

```java
// example:
Vehicle fourRunner = new Car();
fourRunner.go();
```

- Cannot be instantiated
- Can be a reference type
- Can be an array type
- May have non-abstract methods
- Don't need to have abstract methods
- Provide a guarantee:
  - If you have a non-null reference of an abstract type, it refers to an object that is not abstract and therefore has non-abstract implementations for all methods
```java
public class CitySimulation {
    public void run() {
        Vehicle [] vehicles = new Vehicle[6];
        vehicles[1] = new Car();
        vehicles[2] = new Truck();
        vehicles[3] = new Truck();
        vehicles[4] = new Boat();
        vehicles[5] = new Airplane();

        for(int i = 0; i < vehicles.length; i++) {
            vehicles[i].go();
        }
    }
}
```

## Simulation must also include people... and dogs:
- How would we do this?
  - **With an _Interface_**
```java
 public interface Moveable {
    void go();
 }
 
 public void run() {
    Moveable[] moveables = new Moveable[6];
    moveables[0] = new Car();
    moveables[1] = new Truck();
    moveables[2] = new Plane();
    moveables[3] = new Train();
    moveables[4] = new Person();
    moveables[5] = new Dog();
    
    for (int i = 0; i < moveables.length; i++) {
        moveables[i].go();
    }
}

```
- Cannot be instantiated
- Can be used as a reference type
- Can be used a an array 
- May **NOT** have non-abstract methods
  - all methods are abstract
- All methods are public
  - with one exception being in newer versions of Java
- Provides same guarantee as abstract classes:
  - If you have a non-null reference of an abstract type, it refers to an object that is not abstract and therefore has non-abstract implementations for all methods
- Can have constant variables
  - all variables are public, static, and final
- in Java 8 and later
  - can have instance methods with bodies as long as they are declared with default
    - default void myDefaultMethod() {...}
  - can have static methods with bodies
- In Java 9 and later
  - can have private methods with bodies (not inherited)

## Creating an Interface:
- An interface can extend another interface:
```java
public interface MyInterface extends Moveable, Comparable {
    void myMethod();
    void myOtherMethod();
} // this means MyInterface inherits the abstract methods from Moveable and Comparable and can also use its own
```
- No single-inheritance limit with interfaces
- Implementing classes must implement all methods inside that interface and each parent interface or be declared abstract

## Implementing an Interface
- Use the **_Implements_** keyword
```java
public class Person implements Moveable {
    public void go() {
        // code to make person go
    }
}
```
## Implementing an interface with an Abstract Class in Java
```java
public abstract class Vehicle implements Moveable {
    // all subclasses of Vehicle will need to implement methods from the Moveable Interface
}
```

## Extending a Class and Implementing Multiple Interfaces
```java
public class Employee extends Person implements Moveable, Comparable {
    public void go() {
        // code to make person go
    }
    public int compareTo(Object obj) {
        // code to compare two employees
    }
}
```
