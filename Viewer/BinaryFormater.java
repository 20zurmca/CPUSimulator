
/**
 * Class BinaryFormater formats a binary string to appropriate length 
 *
 * @author Cameron Zurmuhl
 * @version 10/20/17
 */
public class BinaryFormater
{
  /**
    * Method format formats a Binary string to the length of the wordsize
    * @param s the stirng in binary
    * @param wordSize the size to format the string to
    * @return the formated String 
    */
   public static String format(String s, int wordSize)
   {
       while(s.length() < wordSize)
       {
           s = "0" + s;
        }
       if(s.length() > wordSize)
       {
           s = s.substring(s.length()-wordSize, s.length());
        }
       return s;
   }
}
