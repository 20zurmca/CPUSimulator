
/**
 * A class that represents a register
 */
public class Register
{
    private int name; //name of register
    
    private String specialName; //name for special registers that don't have numerical labels
    
    private String data; //data in register
    
    private int size; //wordsize 
    
    /**
     * Constructor for class Register
     * @param name this.name
     * @param data this.data
     * @param size this.size
     */
    public Register (int name, String data, int size)
    {
        this.name = name;
        this.data = data; 
        this.size = size;
    }
    
    /**
     * Constructor for class Register
     * @param specialName the name as a string
     * @param data this.data
     * @param size this.size
     */
    public Register(String name, String data, int size)
    {
        this.specialName = name;
        this.data = data;
        this.size = size;
        
    }
    
    /**
     * Method getName returns this.name
     * @return this.name
     */
    public int getName()
    {
        return name;
    }
    
    /**
     * Method getData returns this.data
     * @return this.data
     */
    public String getData()
    {
        return data; 
    }
    
    /**
     * Method setName sets this.name
     * @param name the new name to set this.name to
     */
    public void setName(int name)
    {
        this.name = name;
    }
    
    /**
     * Method setData sets this.data
     * @param data the data to set this.data to
     */
    public void setData(String data)
    {
        this.data = data;
    }
    
    /**
     * method getSize returns this.size
     * @return this.size
     */
    public int getSize()
    {
        return this.size;
    }
}
