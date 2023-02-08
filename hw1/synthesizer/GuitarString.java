// TODO: Make sure to make this class a part of the synthesizer package
//package <package name>;
package synthesizer;

import java.util.HashSet;

//Make sure this class is public
public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this divsion operation into an int. For better
        //       accuracy, use the Math.round() function before casting.A
        //       Your buffer should be initially filled with zeros.
        buffer = new ArrayRingBuffer((int) Math.round(SR / frequency));

    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // TODO: Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each other.
        boolean flag = true;
        while (flag) {
            flag = false;
            while (!buffer.isEmpty()) {
                buffer.dequeue();
            }
            while (!buffer.isFull()) {
                double noise = Math.random() - 0.5;
                buffer.enqueue(noise);
            }
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().
        double n1 = buffer.dequeue();
        double n2 = buffer.peek();
        n1 = (n1 + n2) / 2 * GuitarString.DECAY;
        buffer.enqueue(n1);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // TODO: Return the correct thing.
        return buffer.peek();
    }
}
