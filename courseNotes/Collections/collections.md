# Collections Overview:
- Collection
  - List
  - Set
    - SortedSet
      - NavigableSet
  - Queue
    - Deque (double-ended-queue)
- Map
  - SortedMap
    - NavigableMap
- Iterator
  - ListIterator


## Java.util
- Where you will find the data structures
- Interface Collection\<E\>
- Interface Iterator\<E\>
  - hasNext()
    - returns true if the iteration has more elements
  - iterator()
    - returns an iterator over the elements in this collection


## List
- List Interface
- A sequence of elements accessed by index
- get(index), set(index, value)
- ArrayList (resizable array implementation)
- LinkedList (doubly-linked)
- Lists support a more powerful iterator called a ListIterator
  - hasNext() & hasPrevious() 


## Set
- A collection that contains no duplicates
  - add(), contains(), remove()
- HashSet (fast with extra memory)
- TreeSet (slower but less memory)
- LinkedHashSet


## Queue
- a collection designed for holding elements prior to processing
  - add(), peek(), remove()
- ArrayDeque (fifo)
- LinkedList (fifo)
- PriorityQueue (priority queue)
- Queue types:
  - FIFO → "First In First Out"
  - LIFO → "Last In First Out"
  - Priority -> "Priority rankings"


## Deque
- A queue that supports insertion from both ends of the queue
  - addFirst(), addLast()
  - peekFirst(), peekLast()
  - removeFirst(), removeLast()
- ArrayDeque
- LinkedList


## Stack
- ~~Deprecated~~
- if you need a stack, use a deque


## Map
- A collection of key-value pairs
  - put(key, value) → replaces the value at that given key
  - get(key)
  - contains(key)
  - remove(key)
  - keySet()
  - values()
  - entrySet()
- HashMap
- TreeMap
- LinkedHashMap


# Considerations:

## equals() → Equality Checking
- When using collections, we put objects into a collection, and then later come back and search the collection for particular values. For this to work we need to think hard about what it means for two objects to be "equal".
- As we have said before, by default the Object.equals method compares objects by identity. If you want objects to only be equal to themselves, do nothing. However, if you want equality checks to be based on value rather than identity, override the equals method on objects that will be placed in collections.


## Hashing-Based Collections
- If you use hash-based collections and want objects compared by value rather than by memory address, you need to override hashCode(), since the default Object.hashCode() just returns something based on the object's identity/address.
- The rule to follow: equals and hashCode must stay consistent—if equals compares identity, hashCode should too, and if equals compares values, hashCode should use those same fields.


## Sorted Collections
- TreeSet, TreeMap, PriorityQueue → must all be sortable
- We must be able to compare any two objects and determine their relationship
- Comparable and Comparator for tree-based collections
- compareTo(T o) returns an integer
  -   0 → equal
  - \>0 → Current object greater than parameter object
  - \<0 → Current object less than parameter object
- If using TreeSet, TreeMap, PriorityQueue → must implement the comparable interface
- Can't change information in objects (must remove and reinsert)


```java
import java.sql.Time;

public class TimeOfDay implements Comparable<TimeOfDay> {

    private int hour;
    private int minute;

    public TimeOfDay() {
        setHour(0);
        setMinute(0);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException();
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException();
        this.minute = minute;
    }

    @Override
    public int compareTo(TimeOfDay timeOfDay) {

        int result = Integer.compare(hour, timeOfDay.hour);
        if (result == 0)
            result = Integer.compare(minute, timeOfDay.minute);
        return result;
    }
}
```