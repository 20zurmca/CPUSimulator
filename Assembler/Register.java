
/**
 * A class that represents a register
 * for the assembler's purpose, this class acts as a placeholder
 */
public class Register
{
    private int name; //name of register
    private int data; //data in register
    private int size; //wordsize 
    
    /**
     * Constructor for class Register
     * @param name this.name
     * @param data this.data
     * @param size this.size
     */
    public Register (int name, int data, int size)
    {
        this.name = name;
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
    public int getData()
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
    public void setData(int data)
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
