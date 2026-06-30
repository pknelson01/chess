import java.util.Random;


public class Basics {

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            String x = String.format("Arg %d is: %s (%d characters)", i, args[i], args[i].length());
            System.out.println(x);
        }

        Random generator = new Random();
        System.out.println(generator.nextInt());
        System.out.println(Math.pow(4,3));
    }
}