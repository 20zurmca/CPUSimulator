import java.io.*;
/**
 * A class that can view arbitrary image files in hex or binary 
 *
 * @author Cameron Zurmuhl 
 * @version October 22, 2017, 12:23 A.M
 */
public class ImageVisualizer
{
    private MainMemory mem; //main memory 

    int maxmem;  //max memory 

    private BufferedReader oReader; //buffered reader for the .o file

    private File oFile;  //the file 

    private boolean hex; //whether to report in hex or binary 
    
    private int startingPos; //starting position for the visualizer
    
    private int endingPos; //ending positon for the visualizer


    /**
     * Constructor for objects of class Viewer
     * @param fileName the name of the image file to visualize
     * @param hex whether the viewer is to report hex or binary 
     * @param start the starting position in memory to visualize
     * @param end the ending position in memory to visualize
     */
    public ImageVisualizer(String fileName, boolean hex, int start, int end)
    {
        oFile = new File(fileName);
        this.hex = hex;
        this.startingPos = start;
        this.endingPos = end;
        try{
            oReader = new BufferedReader(new FileReader(oFile));
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method processHeader() processes the first line in the image file
     */
    public void processHeader()
    {
        try{
            String line = oReader.readLine();
            String regrex = line.replaceAll("[^0-9]+",""); //only looking at the numbers
            this.maxmem = Integer.parseInt(regrex.substring(4, 8), 16);
            mem = new MainMemory(maxmem);

        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method processMemory() processes the memory in the image file
     */
    public void processMemory()
    {
        try{
            String memory = oReader.readLine();
            int index = 0;
            //adding mememory to and array
            for(int i = 0; i<memory.length()-2; i+=2)
            {
                String hexbyte = memory.substring(i, i+2); //get the byt ein hex
                mem.addEntry("0x"+hexbyte, index, 1);
                index++;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Function memoryDisplayHex() returns a string formated to display memory from 0x0 down in hex
     * Binary or hex depends on the input 
     * @return the memory in hex
     */
    public String memoryDisplayHex()
    {
        int pos = startingPos;
        String ret = "";
        while(pos <= endingPos)
        {
            String instruction = mem.getFourBytes(pos);
            ret += "0x"+Integer.toHexString(pos) + ": " + instruction.substring(0,4) + " " + instruction.substring(4,8)+"\n";
            pos += 4;
        }
        return ret;
    }
    
    /**
     * Function memoryDisplayBin() returns a string formated to display memory from 0x0 down in binary
     * @return the memory in binary 
     */
    public String memoryDisplayBin()
    {
        int pos = startingPos;
        String ret = "";
        while(pos <= endingPos)
        {
            String instruction = mem.getFourBytes(pos);
            String bin = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(instruction, 16)),32);
            ret += "0x"+Integer.toHexString(pos) + ": " + bin.substring(0,16) + " " + bin.substring(16,32)+"\n";
            pos += 4;
        }
        return ret;
    }

    /**
     * Method convertToAssemblyHex() converts a hexadecimal instruction to LEGv8 assembly
     * @param bin the binary instruction
     * @return the LEGv8 code
     */
    public String convertToAssemblyHex(String hex)
    {
        if(hex.equals("69400100"))
        {
            return "MOVEZ X0 data";
        } else if(hex.equals("7c200001"))
        {
            return "LDUR X1, X0, #0";
        } else if(hex.equals("5a000202"))
        {
            return "CBZ X2, #32";
        } else if(hex.equals("00a0000c"))
        {
            return "B #12";
        } else if(hex.equals("04881422"))
        {
            return "ADDI X2, X2, #20";
        }else if(hex.equals("06581022"))
        {
            return "SUB X2, X1, X2";
        }else if(hex.equals("02220002"))
        {
            return "PUSH X2";
        }else if(hex.equals("00000000"))
        {
            return "NOP";
        }else if(hex.equals("03330003"))
        {
            return "POP X3";
        }else if(hex.equals("7c000003"))
        {
            return "STUR X3, X0, #0";
        }else if(hex.equals("06502033"))
        {
            return "EOR X3, X2, X3";
        }else if(hex.equals("5a000043"))
        {
            return "CBZ X3, #4";
        }else if(hex.equals("11111111"))
        {
            return "HALT";
        } else if(hex.equals("00000def"))
        {
            return "STACK VALUE: " + Integer.parseInt("def", 16);
        } else if(hex.equals("00abab00") || hex.equals("0000ab00"))
        {
            return "DATA VALUE: " + Integer.parseInt("ab", 16);
        }
        return "";
    }

    /**
     * Method convertToAssemblyBin() converts a binary instruction to LEGv8 assembly
     * @param bin the binary instruction
     * @return the LEGv8 code
     */
    public String convertToAssemblyBin(String bin)
    {
        if(bin.equals("01101001010000000000000100000000"))
        {
            return "MOVEZ X0 data";
        } else if(bin.equals("01111100001000000000000000000001"))
        {
            return "LDUR X1, X0, #0";
        } else if(bin.equals("01011010000000000000001000000010"))
        {
            return "CBZ X2, #32";
        } else if(bin.equals("00000000101000000000000000001100"))
        {
            return "B #12";
        } else if(bin.equals("00000100100010000001010000100010"))
        {
            return "ADDI X2, X2, #20";
        }else if(bin.equals("00000110010110000001000000100010"))
        {
            return "SUB X2, X1, X2";
        }else if(bin.equals("00000010001000100000000000000010"))
        {
            return "PUSH X2";
        }else if(bin.equals("00000000000000000000000000000000"))
        {
            return "NOP";
        }else if(bin.equals("00000011001100110000000000000011"))
        {
            return "POP X3";
        }else if(bin.equals("01111100000000000000000000000011"))
        {
            return "STUR X3, X0, #0";
        }else if(bin.equals("00000110010100000010000000110011"))
        {
            return "EOR X3, X2, X3";
        }else if(bin.equals("01011010000000000000000001000011"))
        {
            return "CBZ X3, #4";
        }else if(bin.equals("00010001000100010001000100010001"))
        {
            return "HALT";
        } else if(bin.equals("00000000101010111010101100000000") || bin.equals("00000000000000001010101100000000"))
        {
            return "DATA VALUE: " + Integer.parseInt("ab", 16);
        } else if(bin.equals("00000000000000000000110111101111"))
        {
            return "STACK VALUE: " + Integer.parseInt("def", 16);
        }
        return "";
    }
    
    /**
     * Method getMem() returns the main memory
     * @return this.mem
     */
    public MainMemory getMem()
    {
        return mem;
    }
    
    /**
     * Method isHex() returns whether the memory should be displayed in hex or binary
     */
    public boolean isHex()
    {
        return hex;
    }
}
