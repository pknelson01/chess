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

### Classpath
- An environment variable that contains a list of directories that contain.class files, package base directories, or other resources your application needs to access
-  .(current directory) is implicitly on the CLASSPATH if you don't set a CLASSPATH 
-  Can use -classpath command line param
-  Colon separate on MacOS and Linus
-  Semicolon on Windows