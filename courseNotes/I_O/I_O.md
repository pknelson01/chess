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