public class HorribleSteve {
    public static void main(String[] args) {
//        int i = 0;
        for (int j = 0, i = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, i)) {
                System.out.println("i is " + i);
                break; // break exits the for loop!
            }
        }
    }
}
