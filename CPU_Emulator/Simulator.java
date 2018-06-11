import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
/**
 * A class that represents a CPU
 *
 * @author Cameron Zurmuhl
 * @version 10/18/2017
 */
public class Simulator
{
    private BufferedReader oReader; //buffered reader for the .o file

    private File oFile;  //the file 

    private ArrayList<Register> registers; //list of general purpose registers

    private MainMemory mem;  //memory from the .o file

    private int wordSize = 0; //wordsize 

    private int regcnt = 0;  //register count

    private int maxmem = 0;  //maxmem 

    private Register IR = new Register("IR", "00000000000000000000000000000000", 32);  //IR register

    private Register SP = new Register("SP", "0000000000000000", 16);  //SP register

    private Register FP = new Register("FP", "0000000000000000", 16); //FP regsiter

    private Register PC = new Register ("PC", "0000000000000000", 16); //PC register

    private Register SPSR = new Register("SPSR", "0000", 4); //Register that contains flags.  Order of flags in the register: NZCV

    private static boolean noisy; //noisy mode

    /**
     * Constructor for class Simulator 
     */
    public Simulator(String fileName, boolean noisy)
    {
        this.noisy = noisy;
        try{
            oReader = new BufferedReader(new FileReader(fileName));
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        registers = new ArrayList<>();
    }

    public static void main (String [] args)
    {
        Simulator s = new Simulator(args[0], Boolean.parseBoolean(args[1]));
        s.processHeader();
        s.processMemory();
        s.run();
    }

    /**
     * Method run loops through the main: directive in the memory in the image file
     */
    public void run()
    {
        for(int j = 0; j<13; j++)
        {
            System.out.println("Current PC: " + Integer.parseInt(PC.getData(),2));
            String i = fetchInstruction();
            System.out.println("Current IR: " + IR.getData());
            String type = decodeInstruction(i);
            executeInstruction(type, i);
            updateFlags();
            SPSR.setData("0000");
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
            this.wordSize = Integer.parseInt(regrex.substring(0,2));
            this.regcnt = Integer.parseInt(regrex.substring(2,3));
            this.maxmem = Integer.parseInt(regrex.substring(4, 8), 16);
            mem = new MainMemory(maxmem);
            String fp = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(regrex.substring(9, 12),16)),wordSize);
            String sp = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(regrex.substring(12, regrex.length()), 16)),wordSize);

            SP.setData(sp);
            FP.setData(fp);
            //System.out.println(wordSize + " " + regcnt + " " + maxmem + " " + FP.getData()+ " " + SP.getData());
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        //initialize registers 
        for(int i = 0; i<regcnt; i++)
        {
            registers.add(new Register(i, "0000000000000000", wordSize));
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
                String hexbyte = memory.substring(i, i+2);
                mem.addEntry("0x"+hexbyte, index, 1);
                index++;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method writeNewMemory() writes updated memory to a new image file
     */
    public void writeNewMemory()
    {
        File newImage = new File("newImage.o"); //create a new file to write to
        PrintWriter pw = null;
        try{
            if(!newImage.exists()){ //if the file does not exist, create a new one
                newImage.createNewFile();
                pw = new PrintWriter(new FileWriter(newImage));
                pw.println("#hex:WS-"+wordSize+":RC-"+regcnt+":MM-0x"+Integer.toHexString(maxmem)+":FP-0x" +Integer.toHexString(Integer.parseInt(FP.getData(),2))+":SP-0x"+Integer.toHexString(Integer.parseInt(SP.getData(),2))); //write code here
                pw.println(mem);
                pw.flush();
                pw.close(); //close file 
            } 
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method writeRegisterFile() writes register information to a .o file
     */
    public void writeRegisterFile()
    {
        File RegisterInfo = new File("Registers.o");
        PrintWriter pw = null;
        try{
            if(!RegisterInfo.exists()){ //if the file does not exist, create a new one
                RegisterInfo.createNewFile();
                pw = new PrintWriter(new FileWriter(RegisterInfo));
                pw.println("REGISTER\tDATA");
                for(int i = 0; i<regcnt; i++)
                {
                    pw.println("X"+registers.get(i).getName() + "\t" + registers.get(i).getData());
                }

                pw.println("IR\t"+IR.getData());
                pw.println("FP\t"+FP.getData());
                pw.println("SP\t"+SP.getData());
                pw.println("PC\t"+PC.getData());
                pw.println("\n----------------------------FLAGS----------------------------");
                pw.println("N\t" + SPSR.getData().substring(0,1));
                pw.println("Z\t" + SPSR.getData().substring(1,2));
                pw.println("C\t" + SPSR.getData().substring(2,3));
                pw.println("V\t" + SPSR.getData().substring(3,4));
                pw.flush();
                pw.close(); //close file 
            } 
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method fetchInstruction() gets the next instruction
     * @return the instruction in hex
     */
    public String fetchInstruction()
    {

        String instruction = mem.getFourBytes(Integer.parseInt(PC.getData(),2)); //get the instruction by processing four bytes
        if(noisy){
            System.out.println("Instruction: 0x" + instruction);
        }
        int iToInt = Integer.parseInt(instruction, 16); //parsing instruction to integer
        String iToB = BinaryFormater.format(Integer.toBinaryString(iToInt), 32); //parsing to binary 

        IR.setData(iToB); //setting instruction register in binary

        //update ProgramCounter
        PC.setData(addStrings(PC.getData(), BinaryFormater.format(Integer.toBinaryString(4),wordSize),false));
        return instruction;

    }

    /**
     * Method decodeInstruction updates the PC and returns the instruction type
     * @param the instruction to decode
     * @return the instruction's type
     */
    public String decodeInstruction(String i)
    {

        //return instruction type
        String opcode = null;
        if(i.charAt(0) == '0') //some instruction types have a leading 0
        {
            opcode = i.substring(0,4);
        } else 
        {
            opcode = i.substring(0,3);
        }

        String [] ROpcodes = new String [] {"0458", "0558", "0658", "0758", "0450", "0550", "0650", "06b0", "069b", "069b"};
        String [] IOpcodes = new String [] {"0488", "0588", "0688", "0788", "0490", "0590", "0690"};
        String [] DOpcodes = new String [] {"7c2", "7c0", "5c4", "5c0", "3c2", "3c0", "1c2", "1c0"};
        String [] BOpcodes = new String [] {"00a0", "04a0"};
        String [] CBOpcodes = new String []{"2a0", "5a8", "5a0"};
        String [] stackOpcodes = new String [] {"0222","0333"};
        String [] specialTypes = new String [] {"1111", "0000"};

        if(Arrays.asList(IOpcodes).contains(opcode))
        {
            return  "I";
        } else if(opcode.equals("694"))
        {
            return "IW";
        } else if(Arrays.asList(DOpcodes).contains(opcode))
        {
            return "D";
        } else if(Arrays.asList(ROpcodes).contains(opcode))
        {
            return "R";
        } else if(Arrays.asList(CBOpcodes).contains(opcode))
        {
            return "CB";
        } else if(Arrays.asList(BOpcodes).contains(opcode))
        {
            return "B";
        } else if(Arrays.asList(stackOpcodes).contains(opcode))
        {
            return "STACK";
        }
        return "SPECIAL"; //halt or NOP
    }

    /**
     * Method executeInstruction processes an instruction given its type
     * @param i the instruction passed
     * @param type the type of the instruction
     */
    public void executeInstruction(String type, String i)
    {
        if(type.equals("I"))
        {
            interpretIType(i);
        } else if(type.equals("IW"))
        {
            interpretIWType(i);
        } else if(type.equals("D"))
        {
            interpretDType(i);
        } else if(type.equals("R"))
        {
            interpretRType(i);
        } else if(type.equals("CB"))
        {
            interpretCBType(i);
        } else if(type.equals("B"))
        {
            interpretBType(i);
        } else if (type.equals("STACK"))
        {
            interpretStackType(i);
        } else if(type.equals("SPECIAL"))
        {
            interpretSpecialType(i);
        }
    }

    /**
     * Method push pushes a register onto the data stack
     * @param r the register to push
     */
    private void push(Register r)
    {
        if(noisy){
            System.out.println("Frame and Stack pointer position before push:\n0x" + Integer.toHexString(Integer.parseInt(FP.getData(), 2)) + ", " + Integer.toHexString(Integer.parseInt(SP.getData(),2)));
        }
        String binData = r.getData();
        String binToHex = Integer.toHexString(Integer.parseInt(binData,2));
        binToHex = "0x" + binToHex; //get the data to store to memory
        int sp = Integer.parseInt(SP.getData(),2);
        int sp2 = sp + 2; //advance the stack pointer 
        String newSP = BinaryFormater.format(Integer.toBinaryString(sp2),wordSize); //binary 
        SP.setData(newSP);
        mem.addEntry(binToHex,sp,2); //add in the data to the stack in a two byte boundary
        FP.setData(BinaryFormater.format(Integer.toBinaryString(sp), wordSize)); //put the frame pointer where the original stack pointer was

        if(noisy){
            System.out.println("Memory After pushing register X" + r.getName() + ":");
            System.out.println("Data at Location: 0x" +Integer.toHexString(sp) + ": " + Integer.toHexString(mem.getEntry(sp).getData())); 
            System.out.println("Data at Location: 0x" +Integer.toHexString(sp+1) + ": " + Integer.toHexString(mem.getEntry(sp+1).getData()));
            System.out.println("Frame and Stack pointer position after push:\n0x" + Integer.toHexString(Integer.parseInt(FP.getData(), 2)) + ", " + Integer.toHexString(Integer.parseInt(SP.getData(),2)));
        }
    }

    /**
     * Method pop pops data from the stack
     * @param r the register to pop to 
     */
    private void pop(Register r)
    {
        //if SP == FP, stack is empty
        if(Integer.parseInt(SP.getData(),2) == Integer.parseInt(FP.getData(), 2))
        {
            throw new NoSuchElementException();
        }

        if(noisy){
            System.out.println("Frame and Stack pointer position before pop:\n0x" + Integer.toHexString(Integer.parseInt(FP.getData(), 2)) + ", " + Integer.toHexString(Integer.parseInt(SP.getData(),2)));
        }
        int start = Integer.parseInt(FP.getData(),2); //starting address to pop from
        String data = mem.getTwoBytes(start); //popping data
        for(int i = start; i<start + 2; i++) //freeing memory 
        {
            mem.getEntry(i).setData(0);
        }
        SP.setData(FP.getData()); //setting SP to FP

        if(mem.getEntry(Integer.parseInt(FP.getData(),2)-1).getData() == 239)
        {
            FP.setData(BinaryFormater.format(Integer.toBinaryString(start-8),wordSize)); //keeping in mind the .double that is already stored onto the stack to begin with 
        } else if(Integer.parseInt(FP.getData(), 2) > 512) {
            FP.setData(BinaryFormater.format(Integer.toBinaryString(start-2),wordSize)); //can't go lower than the start of the stack
        }
        r.setData(BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(data, 16)), wordSize)); //storing data to register  

        if(noisy){
            System.out.println("Memory After poping register X" + r.getName() + ":");
            System.out.println("Data at Location: 0x" +Integer.toHexString(start) + ": " + Integer.toHexString(mem.getEntry(start).getData())); 
            System.out.println("Data at Location: 0x" +Integer.toHexString(start+1) + ": " + Integer.toHexString(mem.getEntry(start+1).getData()));
            System.out.println("Frame and Stack pointer position after pop:\n0x" + Integer.toHexString(Integer.parseInt(FP.getData(), 2)) + ", " + Integer.toHexString(Integer.parseInt(SP.getData(), 2)));
            System.out.println("Data in register X" + r.getName() +": " + r.getData());
        }
    }

    /**
     * Method updateFlags displaces flags after an instruction is processed (NCZV)
     */
    public void updateFlags()
    {
        String Flags = SPSR.getData();
        if(noisy){
            System.out.println("N: " + Flags.charAt(0) + "\nZ: " + Flags.charAt(1) + "\nC: " + Flags.charAt(2) + "\nV: " + Flags.charAt(3));
        }
    }

    /**
     * Method getCurrentFlags() returns the current flag states
     * @return the current flag states
     */
    public String getCurrentFlags()
    {
        return SPSR.getData();
    }

    /**
     * Method displayState() displays the state of the machine to the command line 
     */

    private void displayState()
    {
        System.out.println("\n----------------------------STATE OF THE MACHINE----------------------------");    
        System.out.println("REGISTER\tDATA");
        for(int i = 0; i<regcnt; i++)
        {
            System.out.println("X"+registers.get(i).getName() + "\t" + registers.get(i).getData());
        }

        System.out.println("IR\t"+IR.getData());
        System.out.println("FP\t"+FP.getData());
        System.out.println("SP\t"+SP.getData());
        System.out.println("PC\t"+PC.getData());
        System.out.println("\n----------------------------FLAGS----------------------------\n"); //NZCV
        String Flags = SPSR.getData();
        System.out.println("N: " + Flags.charAt(0) + "\nZ: " + Flags.charAt(1) + "\nC: " + Flags.charAt(2) + "\nV: " + Flags.charAt(3));
    }

    /**
     * Method interpretSpecialType interprets an instruction with a special opcode
     * @param instruction the instruction to interpet
     */
    public void interpretSpecialType(String instruction)
    {
        String opcode = instruction.substring(0,4);
        if(opcode.equals("1111")) //HALT
        {
            System.out.println("Reached Halt");
            displayState();
        } else if(opcode.equals("0000")) //NOP
        {
            if(noisy){
                System.out.println("NOP-moving on");
            }
        }
    }

    /**
     * Method interpretStackType interprets an instruction with a Stack-type opcode
     * @param instruction the instruction to interpret 
     */
    private void interpretStackType(String instruction)
    {
        String opcode = instruction.substring(0,4);
        String rd = instruction.substring(6,8);
        Register Rd = getRegister(Integer.parseInt(rd,16));
        if(opcode.equals("0222")) //PUSH
        {
            if(noisy){
                System.out.println("Performing PUSH X" + Rd.getName());
            }
            push(Rd);
        } else { //POP
            if(noisy){
                System.out.println("Performing POP X" + Rd.getName());
            }

            try{
                pop(Rd);
            } catch(Exception e)
            {e.printStackTrace();}
        }
    }

    /**
     * Method interpretBType interprets an instruction with a B-type opcode
     * @param instruction the instruction to interpret
     */
    private void interpretBType(String instruction)
    {
        //16 bit opcode, 16 bit BR address
        String opcode = instruction.substring(0,4);
        String brAddress = instruction.substring(4,8); //branch address
        String binAddress = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(brAddress, 16)), wordSize); //binary address

        if(opcode.equals("00a0")) //B
        {
            if(noisy){
                System.out.println("Program Counter Before Branch: " + Integer.parseInt(PC.getData(), 2));
                System.out.println("Performing B " + Integer.parseInt(brAddress,16));
            }
            PC.setData(binAddress);
            if(noisy){
                System.out.println("Program Counter After Branch: " + Integer.parseInt(PC.getData(), 2));
            }
        }
    }

    /**
     * Method interpretCBType interprets an instruction with a CB-Type opcode
     * @param instruction the instruction to interpret
     */
    private void interpretCBType(String instruction)
    {
        //12 bit opcode, 4 bit Rt, 16 bit Cond_BR_Address
        String opcode = instruction.substring(0,3);
        String rt = instruction.substring(7,8);
        String Cond_BR_Address = instruction.substring(3,7);
        String binAddress = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(Cond_BR_Address, 16)),wordSize);
        Register Rt = getRegister(Integer.parseInt(rt));
        if (opcode.equals("5a0")) //CBZ
        {
            if(noisy){
                System.out.println("Performing CBZ X" + Rt.getName() + " " + Integer.parseInt(Cond_BR_Address,16));
            }
            if(Integer.parseInt(Rt.getData(), 2) == 0) //RT.data = 0-->BRANCH
            {
                if(noisy){
                    System.out.println("PC Before: " + Integer.parseInt(PC.getData(), 2));
                }
                PC.setData(addStrings(PC.getData(), binAddress,false));
                if(noisy){
                    System.out.println("PC After: " + Integer.parseInt(PC.getData(),2));
                }
            }
            if(noisy){
                System.out.println("Value in Register X" + Rt.getName() + " : " + Rt.getData());
            }
        }
    }

    /**
     * Method interpretRType interprets an instruction with a R-Type opcode
     * @param instruction the instruction to interpret
     */
    private void interpretRType(String instruction)
    {
        //16 bit opcode, 4 bit Rm, 4 bit shamt, 4 bit Rn, 4 bit Rd
        //obtain the registers form the strings 
        String opcode = instruction.substring(0,4);
        String rm = instruction.substring(4,5);
        String shamt = instruction.substring(5,6);
        String rn = instruction.substring(6,7);
        String rd = instruction.substring(7,8);
        Register Rm = getRegister(Integer.parseInt(rm, 16));
        Register Rn = getRegister(Integer.parseInt(rn, 16));
        Register Rd = getRegister(Integer.parseInt(rd, 16));
        if(opcode.equals("0458")) //ADD
        {
            if(noisy){
                System.out.println("Performing ADD X" + Rd.getName() + " X" + Rm.getName() + " X" + Rn.getName());
            }
            String addition = addStrings(Rn.getData(),Rm.getData(),true);
            Rd.setData(addition);
            if(noisy){
                System.out.println("Data in register X" + Rd.getName() + " : " + Rd.getData());
            }
        } else if(opcode.equals("0450"))
        {
            if(noisy){
                System.out.println("Performing AND X" + Rd.getName() + " X" + Rm.getName() + " X" + Rn.getName());
            }
            String and = andStrings(Rm.getData(), Rn.getData());
            Rd.setData(and);
            if(noisy){
                System.out.println("Data in regsiter X" + Rd.getName() + " : " + Rd.getData());
            }
        } else if(opcode.equals("0650"))
        {
            if(noisy){
                System.out.println("Performing EOR X" + Rd.getName() + " X" + Rm.getName() + " X" + Rn.getName());
            }
            String xor = XorStrings(Rm.getData(), Rn.getData());
            Rd.setData(xor);
            if(noisy){
                System.out.println("Data in regsiter X" + Rd.getName() + " : " + Rd.getData());
            }
        } else if (opcode.equals("0658")) //SUB
        {
            if(noisy){
                System.out.println("Performing SUB X" + Rd.getName() + " X" + Rm.getName() + " X" + Rn.getName());
            }
            int decimalRn = Integer.parseInt(Rn.getData(),2);
            int negRn = -decimalRn; //flip to negative, convert to binary 
            String negativeRn = BinaryFormater.format(Integer.toBinaryString(negRn), wordSize);
            String sub = addStrings(Rm.getData(), negativeRn, true);
            Rd.setData(sub);
            if(noisy){
                System.out.println("Data in regsiter X" + Rd.getName() + " : " + Rd.getData());
            }
        }
    }

    /**
     * Method interpretDType interpts an instruction with a D-Type opcode
     * @param instruction the instruction to interpret
     */
    private void interpretDType(String instruction)
    {
        //12 bit opcode, 12 bit address, 4 bit Rn, 4 bit Rt
        String opcode = instruction.substring(0,3);
        String hexImmediate = instruction.substring(3,6);
        String rn = instruction.substring(6,7);
        String rt = instruction.substring(7,8);
        Register Rt = getRegister(Integer.parseInt(rt,16));
        Register Rn = getRegister(Integer.parseInt(rn,16));
        String binImmediate = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(hexImmediate, 16)), wordSize);

        String location = addStrings(Rn.getData(), binImmediate,false); //calculate correct memory location scalled by wordsize

        if(opcode.equals("7c2")) //LDUR --loads 16 bits of data 
        {
            String loadedBytes = mem.getTwoBytes(Integer.parseInt(location, 2)); //get the bytes from memory 
            String binLoadedBytes = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(loadedBytes, 16)), wordSize); //turn those bytes into binary 

            Rt.setData(binLoadedBytes); //load the binary into Rt
            if(noisy){
                System.out.println("Performing LDUR X" + Rt.getName() + " X" + Rn.getName() + " #" + Integer.parseInt(hexImmediate,2));
                System.out.println("Data in register X" + Rt.getName() + ": " + Rt.getData());
            }
        } else if(opcode.equals("7c0")) //STUR --stores 16 bits of memory 
        {
            String binaryToStore = Rt.getData();
            int memPos = Integer.parseInt(Rn.getData(), 2);
            String hexToStore = "0x" + Integer.toHexString(Integer.parseInt(binaryToStore, 2));
            if(noisy){
                System.out.println("Performing STUR X" + Rt.getName() + " X" + Rn.getName() + " #" + Integer.parseInt(hexImmediate,2));
            }
            mem.addEntry(hexToStore,memPos,2); //Add entry in a 2 byte boudary
            if(noisy){
                System.out.println("Location: 0x" +Integer.toHexString(memPos) + ": " + "0"+Integer.toHexString(mem.getEntry(memPos).getData())); 
                System.out.println("Location: 0x" + Integer.toHexString(memPos+1) + ": " + Integer.toHexString(mem.getEntry(memPos+1).getData())); 
            }
        }
    }

    /**
     * Method interpretIWType interprets an instruction with an IW-Type opcode (MOVEZ)
     * @param instruction the instruction to interpret
     */
    private void interpretIWType(String instruction)
    {
        String rd = instruction.substring(3,5); 
        String binAddress = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(instruction.substring(5,8), 16)), wordSize); //obtain address pointer in binary

        Register Rd = getRegister(Integer.parseInt(rd, 16)); //get the register to load to
        if(noisy){ //MOVEZ
            System.out.println("Performing MOVEZ. Register Rd: X" + Rd.getName() + " Address: " + binAddress);
        }
        Rd.setData(binAddress);
        if(noisy){
            System.out.println("Value in Rd register: (X" + Rd.getName() + "): " + Rd.getData());
        }
    }

    /**
     * Method interpretIType interprets an instruction with an I-Type opcode
     * @param instruction the instruction to interpret 
     */
    private void interpretIType(String instruction)
    {
        String opcode = instruction.substring(0,4);
        String immediate = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(instruction.substring(4,6), 16)),wordSize);
        String rn = instruction.substring(6,7);
        String rd = instruction.substring(7,8);
        Register Rn = getRegister(Integer.parseInt(rn, 16));
        Register Rd = getRegister(Integer.parseInt(rd, 16));

        if(opcode.equals("0488")) //ADDI
        {
            if(noisy){
                System.out.println("Performing ADDI X" + Rd.getName() + " X" + Rn.getName() + " #" + Integer.parseInt(immediate,2));
            }
            Rd.setData(addStrings(Rn.getData(),immediate,true));
        }
        if(noisy){
            System.out.println("Value in Rd register: (X" + Rd.getName() + "): " + Rd.getData());
        }
    }

    /**
     * Method addStrings adds two strings together and keeps track of flags
     * @param string1 the first string
     * @param string2 the second string
     * @param isInstruction whether the operation is with an instruction or not
     * @return string1 + string2 as a string
     */
    public String addStrings(String string1, String string2, boolean isInstruction)
    { 

        char [] answer = new char[16]; //keeps track of final answer
        char [] carry = new char[17]; //keeps track of carries
        String ret = ""; //return string
        for(int i = 0; i<16; i++) //fill the arrays with 0s to starr
        {
            answer[i] = '0';
            carry[i] = '0';
        }
        carry[16] = '0';

        char [] s1 = string1.toCharArray();
        char [] s2 = string2.toCharArray();
        int carryIndex = 16;

        for(int j = 15; j>=0; j--) //work backwards from the arrays
        {
            int result = Character.getNumericValue(s1[j]) + Character.getNumericValue(s2[j]) + Character.getNumericValue(carry[carryIndex]);
            //System.out.println("s1[j]: " + Character.getNumericValue(s1[j]) + " s2[j]: " + Character.getNumericValue(s2[j]) + " carry[carryIndex]: " + Character.getNumericValue(carry[carryIndex]));
            if(result == 1)
            {
                answer[j] = '1';
            } else if(result == 2)
            {
                carry[carryIndex-1] = '1';
            } else if(result == 3)
            {
                answer[j] = '1';
                carry[carryIndex-1] = '1';
            } 
            carryIndex--;
        }

        for(int i = 0; i<answer.length; i++) //build the return string
        {
            ret += answer[i];
        }

        //set flags

        if(isInstruction)
        {

            //check for overflow and carry
            //remember order of flags is NZCV
            String changedFlags = null;
            if(carry[0] == '1')
            {
                if(carry[0] == carry[1]){
                    changedFlags = SPSR.getData().substring(0,2) + "1" + SPSR.getData().substring(2,3);
                    SPSR.setData(changedFlags); //set carry, no overflow 
                } else if(carry[0] != carry[1])
                {
                    changedFlags = SPSR.getData().substring(0,2) + "11";
                    SPSR.setData(changedFlags); //set carry AND overflow
                }
            } else if(carry[0]=='0' && carry[1]=='1')
            {
                changedFlags = SPSR.getData().substring(0,3) + "1";
                SPSR.setData(changedFlags); //set overflow, no carry
            }

            //check for Z flag
            if(Integer.parseInt(ret,2) == 0)
            {
                String currentFlags = SPSR.getData();
                changedFlags = currentFlags.substring(0,1) + "1" + currentFlags.substring(2, currentFlags.length()); //change the Z flag to 1
                System.out.println("Changing Negative flags to: " + changedFlags);
                SPSR.setData(changedFlags);
            }

            //check fo N flag 
            if(ret.substring(0,1).equals("1"))
            {
                String currentFlags = SPSR.getData();
                changedFlags = "1"+currentFlags.substring(1,currentFlags.length()); //change the N flag to 1
                SPSR.setData(changedFlags);
            }
        }
        return ret;
    }

    /**
     * Method andStrings berforms a bitwise & on two 16 bit strings
     * @param string1 the first string
     * @param string2 the second string
     * @return string1 & string2
     */
    public String andStrings(String string1, String string2)
    {
        char [] str1 = string1.toCharArray();
        char [] str2 = string2.toCharArray();
        char [] answer = new char [16];
        //& is 1 if both bits are 1
        for(int i = 0; i<16; i++)
        {
            if(str1[i] == '1')
            {
                if(str2[i] == '1')
                {
                    answer[i] = '1';
                } else {
                    answer[i] = '0';
                }
            } else 
            {
                answer[i] = '0';
            }
        }

        //set zero flag if necessary 
        String ret = new String(answer);
        if(ret.equals("0000000000000000"))
        {
            SPSR.setData(SPSR.getData().substring(0,1) + "1" + SPSR.getData().substring(2,4));
        }
        return ret;
    }

    /**
     * Method XorStrings berforms a bitwise xor on two 16 bit strings
     * @param string1 the first string
     * @param string2 the second string
     * @return string1 | string2
     */
    public String XorStrings(String string1, String string2)
    {
        char [] str1 = string1.toCharArray();
        char [] str2 = string2.toCharArray();
        char [] answer = new char [16];
        //| is 1 if at least one bit is 1
        for(int i = 0; i<16; i++)
        {
            if(str1[i] == '1')
            {
                if(str2[i] == '1')
                {
                    answer[i] = '0';
                } else {
                    answer[i] = '1';
                }
            } else if(str1[i] == '0')
            {
                if(str2[i] == '0')
                {
                    answer[i] = '0';
                } else {
                    answer[i] = '1';
                }
            }
        }

        //Set Zero Flag if necessary 
        String ret = new String(answer);
        if(ret.equals("0000000000000000"))
        {
            SPSR.setData(SPSR.getData().substring(0,1) + "1" + SPSR.getData().substring(2,4));
        }
        return ret;
    }

    /**
     * Method convertToAssembly() converts a binary instruction to LEGv8 assembly
     * @param bin the binary instruction
     * @return the LEGv8 code
     */
    public String convertToAssembly(String bin)
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
        }else if(bin.equals("00000000000000000000000000000000") && !PC.getData().equals("0000000000000000"))
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
        }
        return "";
    }

    /**
     * Method getSPSR gets the SPSR register
     * @return register SPSR
     */
    public Register getSPSR()
    {
        return SPSR;
    }

    /**
     * Method getSPSR gets the SPSR register
     * @return register SPSR
     */
    public Register getPC()
    {
        return PC;
    }

    /**
     * Method getIR gets the IR register
     * @return register IR
     */
    public Register getIR()
    {
        return IR;
    }

    /**
     * Method getFP gets the FP register
     * @return register FP
     */
    public Register getFP()
    {
        return FP;
    }

    /**
     * Method obtainRegister returns a register in the given  general register arrayList 
     * @param reg the register ID
     * @return the correct register
     */
    public Register getRegister(int reg)
    {
        for(Register r: registers)
        {
            if( r.getName() == reg)
            {
                return r;
            }
        }
        return null;
    }
}
