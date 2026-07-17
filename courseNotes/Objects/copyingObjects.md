# Copying objects
- To anticipate what a certain move will do, you will need to copy the current state of the checkboard and then make that move to see what happens with each possible move
- Motivation
  - In programming, it is common to need to make a copy and existing object
  - two ways
    - shallow
      - copy the linked list but not all of the nodes
      - can still affect the original so not the most effective
    - deep
      - copies the linked list object as well as the nodes
    - Immutable Objects
      - if something is immutable, we don't need to deep copy those becase you cannot change those. (This is where a shallow copy is a good idea)
- Writing classes that support copying
- "clone" method on each class
  - "... implements Cloneable" will let you use the clone method that you inherit from Object
  - You will almost always want to @Overide the clone method to be public. Below is an example:
```java
public class Person implements Cloneable {
    private String fName;
    private String lName;


    @Override
    public Person clone() {
        tru {
            return (Person) super.clone(); // give you a shallow clone for free
        } catch(CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    } // shallow is ok in this situation because the class Person in this case only has immutable variables such as fName and lName being String(s)
}
```
- copy constructors
  - once we introduce instance variables such as birthday which is of Date datatype, we will need to handle this differently and use a DEEP copy. We will do everything the same until after we create the shallow copy `super.clone();`, after that we will need to create a deep copy of the birthday. See below:
```java
public class Person2 implements Cloneable {
    private String fName;
    private String lName;
    private Date BDay;


    @Override
    public Person2 clone() {
        tru {
            Person2 clone = (Person2) super.clone(); // give you a shallow clone for free
        
            Date clonedBDay = (Date) getBDate().clone();
            clone.setBirthdate(clonedBDay);
        
            return clone;
        } catch(CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }
}
```
- It gets even more complicated when you introduce a list:
  - `List.of(p)` is a shortcut way of creating a list of whatever parameter
  - 