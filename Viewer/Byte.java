
/**
 * A class that represents a byte of data
 *
 * @author Cameron Zurmuhl 
 * @version 10/18/2017
 */
public class Byte
{
    //fields///
    
    private int data; //the data to store
    
    /**
     * Constructor for class Byte
     * @param data this byte's data
     */
    public Byte (int data)
    {
        this.data = data;
    }
    
    /**
     * Constructor for class Byte
     */
    public Byte ()
    {}
    
    /**
     * Method getData returns this.data
     * @return this.data
     */
    public int getData()
    {
        return data;
    }
    
    /**
     * Method setData sets the data
     * @param data the new data to set to
     */
    public void setData(int data)
    {
        this.data = data;
    }
    
    @Override
    public String toString()
    {
        return "This byte's data: " + data;
    }
}
