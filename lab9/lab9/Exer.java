package lab9;

public class Exer {
    public static void main(String[] args) {
        Integer[] A = new Integer[3];
        if (A[0] == null && A[1] == null && A[2] == null){
            System.out.println("yes");
        }

        if (A[0].equals(A[1])){
            System.out.println("yes");
        }
    }
}
