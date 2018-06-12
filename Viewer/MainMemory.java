
/**
 * A class that represents the main memory in a computer 
 *
 * @author Cameron Zurmuhl
 * @version 10/1/2017
 */
public class MainMemory
{
    //*************************FIELDS***********************//////
    int size;  //size of the memory 
    private Byte [] entries; //array of data

    //***************CONSTRUCTORS****************///

    /**
     * Constructor for class memory
     * @param size initializes the memory to the size passed
     */
    public MainMemory(int size)
    {
        this.entries = new Byte[size]; 
        this.size = size;
    }

    ///*******************METHODS*********************///

    /**
     * Method getEntry returns a memory entry
     * @param address the location of the entry
     * @return a memory entry
     */
    public Byte getEntry(int address)
    {
        return entries[address];
    }
    
    /**
     * Method getFourBytes returns four bytes of memory as a string
     * @param start the starting position to get the bytes from 
     * @return four bytes of memory as a string
     */
    public String getFourBytes(int start)
    {
        String ret = ""; //return string
        int count = 0;
        while(count < 4 )
        {
            String hexbyte = Integer.toString(entries[start++].getData(), 16); //get a byte of data
            while(hexbyte.length()<2)
            {
                hexbyte = "0" + hexbyte; //add 0 if necessary 
            }
            ret += hexbyte;
            count++;
        }
        return ret;
    }
    
    
    /**
     * Method getTwoBytes returns two bytes of memory as a string
     * @param start the starting position to get the bytes from 
     * @return two bytes of memory as a string
     */
    public String getTwoBytes(int start)
    {
        String ret = ""; //return string
        int count = 0;
        while(count < 2 )
        {
            String hexbyte = Integer.toString(entries[start++].getData(), 16); //get a byte of data
            while(hexbyte.length()<2)
            {
                hexbyte = "0" + hexbyte; //add 0 if necessary 
            }
            ret += hexbyte;
            count++;
        }
        return ret;
    }

    /**
     * Method addEntry adds data to memory in a Big-Endian form 
     * @param data the data of the entry
     * @param address location for the entry
     * @param offset how many bytes to store the data in  
     */
    public void addEntry(String data, int pos, int offset)
    {
        String [] dataSplit = data.split("0x"); //splitting the string by the label
        String hex = dataSplit[1];
        int tmpPos = pos;
        if(hex.length() > 2*offset)
        {
            hex = hex.substring(hex.length()-2*offset, hex.length()); //only storing what we are able to store 
        }
        int hexLength = hex.length();
        int cnt = hexLength; //counter 

        //filling the memory space
        for(int i = 0; i<offset; i++)
        {
            entries[tmpPos++] = new Byte(0);
        }
        tmpPos--;
        //filling in the data back to front 
        String hexByte = null;
        while(cnt > 0 )
        {
            if(cnt == 1)
            {
                hexByte = hex.substring(0,1); //take into consideration odd amount of digits
            }
            else {
                hexByte = hex.substring(cnt-2, cnt);
            }
            entries[tmpPos].setData(Integer.parseInt(hexByte, 16));
            tmpPos--;
            cnt -= 2;
        }
        
        //Printing out the entry
        /*
        tmpPos = pos;
        for(int i = 0; i<offset; i++)
        {
            String hxByte = Integer.toHexString(entries[tmpPos].getData());
            if(Integer.toHexString(entries[tmpPos].getData()).length() < 2) //formatting 
            {
                hxByte = "0" + hxByte;
            }
            System.out.println("Pos: 0x" + Integer.toHexString(tmpPos) + " Byte: " + hxByte);
            tmpPos++;
        }
        */
        
       
    }

    /**
     * Method printMemory prints the memory array
     */
    public void printMemory()
    {
        int count = 0;
        for(Byte m : entries)
        {
            System.out.println("Entry " + count++ + " " + m);
        }
    }

    /**
     * Method getLength returns the number of entries in the main memory
     * @return the length of entries
     */
    public int getLength()
    {
        return size;
    }

    @Override
    public String toString()
    {
        String ret = "";
        String hexByte = null;
        for(int i = 0; i < size; i++)
        {
            if(entries[i] == null)
            {
                hexByte = "00"; //empty bytes means 0s
            } else {
                hexByte = Integer.toHexString(entries[i].getData());
                if(hexByte.length() < 2) //formatting
                {
                    hexByte = "0" + hexByte;
                }
            }
            ret += hexByte;
        }
        return ret;
    }
}
