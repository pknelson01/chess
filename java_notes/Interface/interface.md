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