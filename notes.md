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