package synthesizer;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
    }

    @Test
    public void testPeek(){
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(2);
        Assert.assertEquals(arb.peek().intValue(), 1);
        arb.dequeue();
        Assert.assertEquals(arb.peek().intValue(), 2);
        arb.dequeue();
        Assert.assertEquals(arb.peek().intValue(), 2);
        arb.dequeue();

        arb.peek();
//        Assert.assertTrue(arb.peek() instanceof RuntimeException);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
