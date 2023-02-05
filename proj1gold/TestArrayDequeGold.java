import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class TestArrayDequeGold {
    static int testSize = 1000;

    @Test
    public void test() {

        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();

        String prev = null, cur = null;
        for (int i = 0; i < testSize; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.25) {
                sad1.addLast(i);
                ads1.addLast(i);
                prev = cur;
                cur = "addLast(" + i + ")\n";
            } else if (numberBetweenZeroAndOne < 0.5) {
                sad1.addFirst(i);
                ads1.addFirst(i);
                prev = cur;
                cur = "addFirst(" + i + ")\n";
            } else if (!sad1.isEmpty()) {
                if (numberBetweenZeroAndOne < 0.75) {
                    String nxt = prev + cur + "removeFirst()\n";
                    Assert.assertEquals(nxt, ads1.removeFirst(), sad1.removeFirst());
                    prev = cur;
                    cur = "removeFirst()\n";
                } else {
                    String nxt = prev + cur + "removeLast()\n";
                    Assert.assertEquals(nxt, ads1.removeLast(), sad1.removeLast());
                    prev = cur;
                    cur = "removeLast()\n";
                }
            }
        }

        sad1.printDeque();
    }
}
