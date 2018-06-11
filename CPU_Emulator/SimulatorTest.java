

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class SimulatorTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SimulatorTest
{
    @Test
    public void testAddStrings()
    {
        Simulator cpu = new Simulator ();
        String zero = cpu.addStrings("0011000000000000","1101000000000000");
        assertEquals(Integer.parseInt(zero, 2), 0);
        assertEquals(cpu.getSPSR().getData(), "0110");
    }
}
