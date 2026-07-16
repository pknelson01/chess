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