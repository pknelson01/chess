public class notes {
  public static void main(String[] args) {
    System.out.println("Hello World");
    x();
    y();
  }

  public static void x() {
    String intro = "Hello There...";
    String middle = "GeNeRaL kEnObI!";
    int end = 11;
    float aFloat = 2.5F;
    char char1 = '\u03A3'; 
    String s1 = "You were my ";
    String s2 = "brother, Anakin!";
    
    System.out.printf("%s\n%s\n%d\n%f\n%c\n%s%s\b", intro, middle, end, aFloat, char1, s1, s2);
  }

  public static void y() {
    StringBuilder builder = new StringBuilder();
    builder.append("!");        
    builder.append("!");      
    builder.append("!");      
    String str = builder.toString();
    System.out.println(str);
  }
}