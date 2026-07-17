# Design Principles

---

## Goals of Software Design
- Create systems that:
  - work and satisfy customer requirements
  - are as easy possible to understand, debug, and maintain
  - hold up well under changes

    
1. Design is inherently iterative
   1. Design, implement, test, design, implement, test, ...
   2. Feedback loop from implementation back into design provides valuable knowledge
   3. Designing everything before beginning implementation doesn't work
   4. Beginning implementation without doing any design also does not work
   5. there is a balance between the two:
      1. interleaving design and implementation activities in relatively short iterations

2. Abstraction
    - **Definition:** *In computing, abstraction means hiding complex implementation details behind a simpler interface, so you can work with something based on what it does rather than how it does it.*
   1. Primary tool for coping with **COMPLEXITY**
   2. In OOP, abstractions are represented by classes
   3. Programming languages provide classes that model low-level concepts such as strings and file I/O
   4. Programs written solely in terms of these low-level classes are very difficult to understand
   5. Software designers must create higher-level, domain-specific classes, and write their software in terms of those
      1. High-level classes are implemented in terms of low-level classes
   6. Each class has a carefully designed public interface that defines how the rest of the system interacts with it
   7. A client can invoke operations on an obj without understanding how it works internally 
   8. This is a powerful technique for reducing the cognitive burden of building complex systems
   9. Classes often model complex, real-world objects 
   10. You generally can't fully represent the thing you are abstracting, so you make domain appropriate decisions about what to represent in methods and variables
       1. For example if creating a person class for a social media application, you would take in their name, birthday, interests, etc. But you would not need their social security #, medical history, ancestry, etc. The variables should be domain specific

3. Good Naming
   1. A central part of abstraction is giving things names
   2. Selecting good names for things is critical
   3. Class, method, and variable names should clearly convey their function or purpose
   4. Class and variable names are usually nouns
   5. Methods are usually verbs because they are **DOING** something

4. Single Responsibility
    1. Each class and method should have a single responsibility
   2. Each class should represent one, well-define concept
   3. Each method should perform one, well-define task
   4. If I have a method that **needs** to perform multiple tasks should have a sub-method for each of those tasks that way the main method can just call those sub-methods when it needs to in its main task.

5. Decomposition
   1. For managing **COMPLEXITY**
   2. Large problems subdivided into smaller sub-problems
   3. subdivision continues until leaf-level problems are simple enough to solve directly 
   4. Solutions to sub-problems are recombined into solutions to larger problems
   5. Strongly related to Abstraction
   6. The solution to each sub-problem is encapsulated in its own abstraction 
   7. Solutions to larger problems are expressed in terms of sub-problem solutions, which leads to concise readable code
   8. The decomposition process helps us discover the abstractions 
   9. Levels of Decomposition:
      1. System
      2. Subsystem
      3. Packages (folder with classes that relate to each other)
      4. Classes
      5. Methods
   
6. Good Algorithm & Data Structure Selection
    1. No amount of decomposition or abstraction will hide a fundamentally flawed selection of algorithm or data structure

7. Low Coupling
   1. Classes should not interact much with each other. The more they do the larger the ripple effect when you have to update one class. You should write `shy code`
   2. A class should hide or "encapsulate", its internal implementation that are not essential for its users to know about
   3. Encapsulation is essential in achieving low-coupling
   4. All internal implementations should be `private` unless there is a good reason to make it `protected` or `public`
   5. A classes public interface should be as simple and minimal as possible
   6. Don't let internal details "leak out" of a class
        1. `ClassRoll` instead of `StudentLinkedList`
   7. Some classes or methods are inherently tied to a particular implementation. For these it is ok to use an implementation-specific name
      1. HashTable
      2. TreeSet

8. Avoid Code Duplication
   1. If you have duplicate code and you encounter a bug in the code. You don't fix it once, you correct it n-times