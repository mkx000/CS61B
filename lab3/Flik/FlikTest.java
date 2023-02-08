import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    public void testFlik(){
//        assertTrue(!Flik.isSameNumber(128, 128));

        for (int i = 0; i < 500; i++){
            assertTrue(Flik.isSameNumber(i, i));
        }

    }
}
