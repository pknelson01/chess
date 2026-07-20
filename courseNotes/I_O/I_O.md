# Streams & Files

---

### Ways to Read / Write Files:
1. Streams
   - Sequential
2. Scanner Class
   - Tokenize stream input
3. Files Class
   - Read, copy, etc... whole files
4. RandomAccessFile Class
   - Use a file pointer to read from / write to any location in a file

- The File Class
  - Used to represent a file but can also be used to create and delete a file
  - examples:

```java
// Check if it exists
File file = new File("/user/MyFile.txt");
if(file.exists()) {
    // do something
}

// Create
File file = new File("/user/MyFile.txt");
file.createNewFile();

// Delete
File file = new File("/user/MyFile.txt");
file.delete();
```

### Streams (Most important / common method):
- Two Choices:
  1. Binary Formatted: 00 00 04 D2
  2. Text Formatted: 1234
- InputStream and OutputStream
  - bytes
  - binary-formatted
- Reader and Writer
  - characters
  - text-formatted

- The InputStream interface is used to read bytes sequentially from a data source
- Filter Input Streams:
  - Decompress data
  - Decrypt data
  - Compute a "digest" of the stream
  - Byte counting
  - Line counting
  - Buffering
- Open the InputStream on the data source and then wrap it in one or more filter (decompression, decryption, ...) that provides the features you want
- OutputStream:
  - Writing bytes work the same way but in reverse
  - You can filter Output Streams as well
    - same filters as input
- Examples:
```java
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GZIPOutputStream;

public class Compress {
    private static final int CHUNK_SIZE = 512;
    
    public static void main(String[] args) {
        Compress compress = new Compress();
        
        if (args.length == 2) {
            try {
                compress.compressFile(args[0], args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            compress.usage();
        }
    }
    
    public void compressFile(String inputFilePath, String outputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        
        try(FileInputStream fis = new FileInputStream(inputFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            GZIPOutputStream zipos = new GZIPOutputStream(bos)) {

            byte [] chunk = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(chunk)) > 0) {
                zipos.write(chunk, 0, bytesRead);
            }
        }
    }

    private void usage() { System.out.println("\nUSAGE: java Compress <input-file> <output-file>"); }
    }
```

## Reading and Writing Binary-Formatted Data
- The DataOutputStream (or DataInputStream) class lets you write binary-formatted data values
- The `DataOutputStream(OutputStream out` constructor wraps a DataOutputStream around any OutputStream


# Readers and Writers 

---

- The `reader` interface is used to read characters sequentially from a data source
- The `Writer`interface is used to write characters sequentially to a data destination

Example:

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CopyFileExample {

    public static void main(String [] args) throws IOException {
        if(args.length != 2) {
            printUsage();
        } else {
            CopyFileExample fileCopier = new CopyFileExample();
            fileCopier.copy(args[0], args[1]);
        }
    }

    private static void printUsage() {
        System.out.println("USAGE: java CopyFileExample fromFile toFile");
    }

    private void copy(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);

        try(FileReader fr = new FileReader(fromFile);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(toFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw)) {

            String line;
            while((line = br.readLine()) != null) {
                pw.println(line);
            }
        }
    }
}
```

- The `PrintWriter` class lets you write text-formatted data values 
- The `Scanner` class lets you read text-formatted data values



# Scanner Class

---

- **Good for when you need to tokenize the input**
- Read from a `File`, `InputStream`, or `Reader`
- Can specify the delimit character(s) as a regular expression
  - Defaults to delimiting white space: \s+

## Example 1:
```java
// Example 1:

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerExample1 {
  public static void main(String[] args) throws FileNotFoundException {
    ScannerExample1 scannerExample = new ScannerExample1();

    if(args.length == 1) {
      scannerExample.processFile(args[0]);
    } else {
      scannerExample.usage();
    }
  }

  public void processFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);

    try(Scanner scanner = new Scanner(file)) {
      while (scanner.hasNext()) {
        String str = scanner.next();
        System.out.println(str);
      }
    }
  }

  private void usage() {
    System.out.println("\nUSAGE: java ScannerExample2 <input-file>");
  }
}
```
## Example 2:
```java
// Example 2:

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerExample2 {
  public static void main(String[] args) throws FileNotFoundException {
    ScannerExample2 scannerExample = new ScannerExample2();

    if(args.length == 1) {
      scannerExample.processFile(args[0]);
    } else {
      scannerExample.usage();
    }
  }

  public void processFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);

    try(Scanner scanner = new Scanner(file)) {
      scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

      while (scanner.hasNext()) {
        String str = scanner.next();
        System.out.println(str);
      }
    }
  }

  private void usage() {
    System.out.println("\nUSAGE: java ScannerExample2 <input-file>");
  }
}
```


## Example 3:
```java
// Example 3:

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ScannerExample3 {
  public static void main(String[] args) throws IOException {
    ScannerExample3 scannerExample = new ScannerExample3();

    if(args.length == 1) {
      scannerExample.processFile(args[0]);
    } else {
      scannerExample.usage();
    }
  }

  public void processFile(String filePath) throws IOException {
    File file = new File(filePath);

    try(FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        Scanner scanner = new Scanner(br)) {
      scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

      while (scanner.hasNext()) {
        String str = scanner.next();
        System.out.println(str);
      }
    }
  }

  private void usage() {
    System.out.println("\nUSAGE: java ScannerExample3 <input-file>");
  }
}
```

