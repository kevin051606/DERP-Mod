import java.util.Objects;

public class example{

    public static void main(String[] args){
        String b= new String("fire");
        String c= new String("fire");
        System.out.println(Objects.equals(c, b));
    }
}