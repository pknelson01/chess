## Collections Overview:
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
  - FIFO -> "First In First Out"
  - LIFO -> "Last In First Out"
  - Priority -> "Priority rankings"


## Deque
- A queue that supports insertion from both ends of the queue
  - addFirst(), addLast()
  - peekFirst(), peekLast()
  - removeFirst(), removeLast()
- ArrayDeque
- LinkedList


## Stack
- Deprecated
- if you need a stack, use a deque


## Map
- A collection of key-value pairs
  - put(key, value) -> replaces the value at that given key
  - get(key)
  - contains(key)
  - remove(key)
  - keySet()
  - values()
  - entrySet()
- HashMap
- TreeMap
- LinkedHashMap