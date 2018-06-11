

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class AssemblerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class AssemblerTest
{
   @Test
   public void testReader()
   {
       Assembler as = new Assembler(new String[] {"AssemblyCode.asm"});
       as.fillQueue();
       as.assemble();
       
       //check if the file is being read in correclty
       assertEquals(as.getRegisters().size(), as.getRegcnt()); //testing testing num registers
       assertEquals(as.getMaxmem(), Integer.parseInt("1000", 16)); //testing maxmem
       assertEquals(as.getWordSize(), 16);  //testing word size 
       assertEquals(as.getInstructions().size(), 5); //testing instruciton size 
       
   }
}
