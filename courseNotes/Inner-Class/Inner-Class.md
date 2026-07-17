# Inner-Class
## A class within a class.
  - list -> ordered
  - set -> not ordered
- Iterator pattern:
    - returns an iterator over the elements in this list in proper sequence
    - hasNext(), next(), remove()
- The way to create an inner-class that has access to the variables of the containing class...
  - ... not making that inner-class static


## How to declare a class within a method (declare a class as close to where we need it) 
- 
```java
public Iterator iterator(int increment) {
    class DataStructureIterator implements Iterator {
        private int next = 0;
        
        @Override
        public boolean hasNext() {
            return (next <= arrayOfInts.length - 1);
        }
        
        @Override 
        public int next() {
            int retValue = arrayOfInts[next];
            next += increment;
            return retValue;
        }
    }
    
    return new DataStructureIterator;
}
```
- **There is no need to make a variable final if you don't have code that is changing it.** 
- 