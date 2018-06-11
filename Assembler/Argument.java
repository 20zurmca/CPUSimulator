
/**
 * A class that represents an Argument in the .as file (register, immediate, branch, etc.)
 *
 * @author Cameron Zurmuhl
 * @version 10/1/2017
 */
public class Argument
{
    private String name;      //name of the Argument
        
    //************CONSTRUCTORS****************//
   
    /**
     * Constructor for class Argument
     * @param name the name of the Argument 
     */
    public Argument(String name)
    {
        this.name = name;
    }
    
    
    //******************************METHODS***************************//
    
    /**
     * Method getArgumentType returns if the Argument is a register or immediate
     * @return the type of the Argument in String format
     */
    public String getArgumentType()
    {
        if(name.charAt(0) == 'X' || name.charAt(0) == 'x')
        {
            return "Register";
        } else if(name.charAt(0) == '#')
        {
            return "Immediate";
        }
        return "Pointer";
    }
    
    /**
     * Method getRegNum gets the register number of the Argument
     * @return the register number as an integer
     */
    public int getRegNum()
    {
        int ArgumentLength = name.length();
        String RegNum = name.substring(1,ArgumentLength);
        return Integer.parseInt(RegNum);
    }
    
    /**
     * Method getImmediate gets the immediate value of the Argument
     * @return the immediate value of the Argument
     */
    public int getImmediate()
    {
        int ArgumentLength = name.length();
        String immediate = name.substring(1,ArgumentLength);
        return Integer.parseInt(immediate);
    }
    
    public String toString()
    {
        return name;
    }
}
