
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
/**
 * A class that emulates an Assembler
 * Project 1 CS203: Computer Organization
 * Lafayette College, Fall 2017
 * Professor Pfaffmann
 * @author Cameron Zurmuhl
 * @version 9/30/17
 */
public class Assembler
{
    //*******************FIELDS***********************/////

    private BufferedReader asFile; //the file reader of the input file (.as)

    private String [] directives; //directives that show up in the .as

    private ArrayList<Instruction> instructions; //list of Instructions that are parsed

    private Queue<String> assemblyLines;   //queue to process the strings

    private HashMap<String, Integer> keywords;  //keywords that appear in the .as file. Key is the keyword, Value is the position in memory the keyword appears

    private MainMemory memory;  //stores parsed data words

    private ArrayList <Register> registers = new ArrayList<>(); //storing the registers 

    private int currentLine = 0;  //marks where the parser is 

    private int pos = 0; //The current data memory position 

    private int align = 0; //alignment 

    private int wordSize = 0; //wordsize 

    private int regcnt = 0;  //register count

    private int maxmem = 0;  //maxmem 
    
    private long runTime; //running time for the assembler

    //******************************CONSTRUCTORS**************************//

    /**
     * Constructor for class Assembler
     */
    public Assembler() {} 

    /**
     * Constructor for class Assembler
     * @param file the file that needs to be parsed 
     */
    public Assembler(String file)
    {
        //initialzing I/O
        try{
            asFile = new BufferedReader(new FileReader(new File(file)));
        } catch(Exception e)
        {
            System.out.println("Exception found!  file input must by AssemblyCode.o\n");
            e.printStackTrace();
        }
        this.directives = new String[] 
        {".wordsize","main:","stack:","heap:",".regcnt",
            ".maxmem",
            ".pos",".align",".double", 
            ".single", ".half",".byte","data:"};
        this.instructions = new ArrayList<>();
        this.assemblyLines = new LinkedList<>();
        this.keywords = new HashMap<>();
    }

    ///********************************METHODS*****************************////


    /**
     * Method fillQueue reads in the .asm file fills each line into a queue.  Any comments will be ignored.
     */
    public void fillQueue()
    {
        String s = null;
        try{
            while((s = asFile.readLine()) != null)
            {
                String [] splitLine = s.split(";"); //splitting by semi colon
                String wantedLine = splitLine[0].trim();
                if(wantedLine.length() > 0){
                    String noExtraSpaces = wantedLine.replaceAll(" +", " ");
                    assemblyLines.add(noExtraSpaces.replaceAll("[^a-zA-Z0-9.:\\s\\#]+","")); //regrex
                    currentLine++;
                }
            }
            asFile.close();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Function assemble assembles the assembly code 
     */
    public void assemble()
    {
        long currentTime = System.currentTimeMillis();
        if(assemblyLines.isEmpty())
        {
            throwError("Error: No lines to parse!");
            return;
        }
        readFirstThree();
        while(!assemblyLines.isEmpty())
        {
            parseLine(assemblyLines.poll());
        }
        long endTime = System.currentTimeMillis();
        runTime = endTime - currentTime;
    }

    /**
     * Function generateImageFile generates the image file for the CPU emulator 
     */
    public void generateImageFile()
    {
        File file = new File("AssemblyCode.o"); //create a new file to write to
        PrintWriter pw = null;
        try{
            if(!file.exists()){ //if the file does not exist, create a new one
                file.createNewFile();
                pw = new PrintWriter(new FileWriter(file));
                pw.println("#hex:WS-"+wordSize+":RC-"+regcnt+":MM-0x"+Integer.toHexString(maxmem)+":FP-0x" + Integer.toHexString(keywords.get("stack:"))+":SP-0x"+208); //write code here
                pw.println(memory);
                pw.flush();
                pw.close(); //close file 
            } 
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Function generateStatistics prints statistics about the assembly process
     */
    public void generateStatistics()
    {
        System.out.println("-------------------------------SUMMARY STATISTICS-------------------------------");
        System.out.println("Number of Lines parsed: " + currentLine);
        System.out.println("Time to assemble: " + (int)runTime + "ms");
        System.out.println("Number of instructions: " + instructions.size());
        System.out.println("Check directory CPU_Emulator for the image file: AssemblyCode.o\n");
    }

    /**
     * Function parseLine parses a line of assembly code 
     * @param line the line of assembly 
     */
    public void parseLine(String line)
    {
        String [] splitLine = line.split(" ");
        if(hasDirective(line)) //case where there is a directive
        {
            String dir = extractDirective(line); //obtain directive 
            if(dir.equals(".pos")) //set memory position 
            {
                pos = Integer.parseInt(extractHex(line), 16);
            } else if(dir.equals("main:")) //put keyword in map, process the first instruction 
            {
                keywords.put(dir, pos);
                String instructionName = splitLine[1];
                if(instructionName.equals("MOVEZ") && !keywords.containsKey("data")) //case for MOVEZ
                {
                    //if data is not yet defined, enqueue the line again
                    assemblyLines.add(line.substring(6,line.length()));
                    keywords.put("MOVEZ", pos);
                    pos+=4;
                    return;
                }
                Argument [] args = null;
                ArrayList <Argument> argList = new ArrayList<>();
                for(int i = 2; i < splitLine.length; i++){ //add args to argument set
                    Argument a = new Argument(splitLine[i]);
                    argList.add(a);
                }
                Instruction i = new Instruction(instructionName, argList.toArray(new Argument[argList.size()]),pos);
                memory.addEntry(generateMachineCode(i), pos, 4);
                pos += 4;
                instructions.add(i);
            } else if(dir.equals(".align")) //set alignment 
            {
                keywords.put(dir, pos);
                align = Integer.parseInt(splitLine[1]);
            } else if(dir.substring(0,4).equals("data")) //store in hashmap
            {
                keywords.put(dir.substring(0,4), pos);
            } else if(dir.equals(".double")) //load into memory
            {
                memory.addEntry(splitLine[1], pos, 8); 
                pos += 8;
            } else if(dir.equals(".single")) //load into memory
            {
                memory.addEntry(splitLine[1], pos, 4);
                pos += 4;
            } else if(dir.equals(".half")) //load into memory 
            {
                if(splitLine[1].length()-2 > 4 )
                {
                    throwError("Error: Not enough byte space to represent full hex value in given data space.  Must trunkate");
                }
                memory.addEntry(splitLine[1], pos, 2); 
                pos += 2;
            } else if(dir.equals(".byte")) //load into memory 
            {
                if(splitLine[1].length()-2 > 2 ) //trying to fit data into a memory space that is less bytes than the amount of data
                {
                    throwError("\nTechnical Error: Not enough byte space to represent full hex value of " + splitLine[1] + ".  Must trunkate");
                }
                memory.addEntry(splitLine[1], pos, 1);
                pos++;
            } else if (dir.equals("stack:")) //put to hashmap, load into mmeory 
            {
                keywords.put(dir, pos);
                memory.addEntry(splitLine[2], pos, 8);
            } 
        } else if (splitLine[0].equals(splitLine[0].toUpperCase()))
        {
            String instructionName = splitLine[0];
            if(instructionName.equals("MOVEZ") && !keywords.containsKey("data")) //case for MOVEZ if data is not yet defined, enqueue the line again
            {
                
                keywords.put("MOVEZ", pos);
                assemblyLines.add(line);
                pos+=4; 
                return;
            }

            Argument [] args = null;
            ArrayList <Argument> argList = new ArrayList<>();
            for(int i = 1; i < splitLine.length; i++){ //fetching instruction arguments
                Argument a = new Argument(splitLine[i]);
                argList.add(a);
            }

            if(instructionName.equals("MOVEZ")) //catching MOVEZ the second time
            {
                pos = keywords.get("MOVEZ");
                Instruction i = new Instruction(instructionName, argList.toArray(new Argument[argList.size()]), pos);
                instructions.add(i);
                memory.addEntry(generateMachineCode(i), 0, 4);
            } else { //all other instructions
                Instruction i = new Instruction(instructionName, argList.toArray(new Argument[argList.size()]), pos);
                instructions.add(i);
                memory.addEntry(generateMachineCode(i), pos, 4);
                pos += 4;
            }
        }
    }

    /**
     * Function readFirstThree reads the first three lines because those three lines are distinct
     */
    public void readFirstThree()
    {
        String lineOne = getNextLine();
        String [] lineOneSplit = lineOne.split(" ");
        this.wordSize = Integer.parseInt(lineOneSplit[1]); //obaining word size

        String lineTwo = getNextLine();  //obtaining regcnt
        String [] lineTwoSplit = lineTwo.split(" ");
        this.regcnt = Integer.parseInt(lineTwoSplit[1]);
        for(int i = 0; i<regcnt; i++)
        {
            registers.add(new Register(i, 0, wordSize));
        }

        String lineThree = getNextLine(); //obtaining maxmem
        String [] lineThreeSplit = lineThree.split(" ");

        this.maxmem = Integer.parseInt(extractHex(lineThree), 16);

        this.memory = new MainMemory(maxmem); //creating memory array
    }

    /**
     * Method getNextLine() gets the next line 
     * returns null if end of file is reached
     * @return the next line of the file as a string
     */
    public String getNextLine()
    {
        return assemblyLines.poll();
    }

    /**
     * Method isEmpty() returns if the queue is empty or not
     * @return whether this.assemblyLines is empty
     */
    public boolean isEmpty()
    {
        return assemblyLines.isEmpty();
    }

    /**
     * method generateMachineCode generates the machine code for an Instruction
     * @param instruction the instruction to translate
     * @return machine code in a string format
     */
    public String generateMachineCode(Instruction instruction)
    {
        if(instruction.getType().equals("RType"))
        {
            return instruction.buildRType();
        } else if(instruction.getType().equals("IType"))
        {
            return instruction.buildIType();
        } else if(instruction.getType().equals("DType"))
        {
            return instruction.buildDType();
        } else if(instruction.getType().equals("BType"))
        {
            return instruction.buildBType();
        } else if(instruction.getType().equals("CBType"))
        {
            return instruction.buildCBType();
        } else if(instruction.getType().equals("IWType"))
        {
            return instruction.moveZ(instruction.getArgument(0).toString(), keywords.get("data"));
        } else if(instruction.getType().equals("StackType"))
        {
            return instruction.buildStack();
        } else if(instruction.getType().equals("SPECIAL"))
        {
            return instruction.buildSpecial();
        }
        return "Error durring machine code generation";
    }

    /**
     * Method hasKeyword examines if the next input line contains a directive
     * @param line the line of assembly code from the .as file
     * @return true if there is a directive, false if otherwise
     */
    public boolean hasDirective(String line)
    {
        //splitting the line by space
        String [] splitLine = line.split(" ");
        for(int i = 0; i<directives.length; i++)
        {
            for(int j = 0; j<splitLine.length; j++){
                if(directives[i].equalsIgnoreCase(splitLine[j])) //searching across two arrays
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method extractDirective obtains the directive in a line of assembly
     * @param line the line of assembly code form the .as file
     * @return the directive in the assembly line 
     */
    public String extractDirective(String line)
    {
        String [] splitLine = line.split(" ");
        for(int i = 0; i<directives.length; i++)
        {
            for(int j = 0; j<splitLine.length; j++){
                if(directives[i].equalsIgnoreCase(splitLine[j])) //check for directive 
                {
                    return directives[i];
                }
            }
        }
        return "No Directive";
    }

    /**
     * Method extractHex returns the label in a line of assembly i.e 0x... or an integer
     * @return a String that represents the label in the line of assembly
     */
    public String extractHex(String line)
    {
        String [] splitLine = line.split(" ");
        for(int i = 0; i<splitLine.length; i++){
            if(splitLine[i].substring(0,2).equals("0x"))
            {
                return splitLine[i].substring(2, splitLine[i].length()); 
            }
        }
        return "No label";
    }

    /**
     * Method getArgumentType returns if an argument is a register, or an immediate
     * @param line an input string
     * @return a string that tells what the operand is (register, or immediate)
     */
    public String getOperandType(String line)
    {
        if(line.substring(0,1).equals("X"))
        {
            return "register";
        } else if(line.substring(0,1).equals("#"))
        {
            return "immediate";
        }
        return null;
    }

    /**
     * Method throwError prints and error
     * @param error the description of an error
     */
    public void throwError(String e)
    {
        System.out.println(e);
    }


    /**
     * Method getMemory returns this.memory
     * @return this.memory
     */
    public MainMemory getMemory()
    {
        return memory;
    }

    /**
     * Method getCurrentCodeSection returns this.currentCodeSection
     * @return this.currentCodeSection
     */
    public int getCurrentLine()
    {
        return currentLine;
    }

    /**
     * Method set currentCodeSection sets this.currentCodeSection
     * @param codeSection the new code section
     */
    public void setCurrentCodeSection(int codeSection)
    {
        this.currentLine = codeSection;
    }

    /**
     * Method getDataMemoryAddress returns this.dataMemoryAddress
     * @return this.dataMemoryAddress
     */
    public int getPos()
    {
        return pos;
    }

    /**
     * Method setDataMemoryAddress sets this.dataMemoryAddress
     * @param a the memory address
     */
    public void setPos(int a)
    {
        pos = a;
    }

    /**
     * Method getasReader() returns this.asFile
     * @returns this.asFile
     */

    public BufferedReader getasReader()
    {
        return asFile;
    }

    /**
     * Method getQueue() returns this.assemblyLines
     * @return this.assemblyLines
     */
    public Queue getQueue()
    {
        return this.assemblyLines;
    }

    /**
     * Method getWordSize() returns this.wordSize
     */
    public int getWordSize()
    {
        return this.wordSize;
    }

    /**
     * Method getRegcnt returns this.regnct
     */
    public int getRegcnt()
    {
        return this.regcnt;
    }

    /**
     * Method getMaxmem() returns this.maxmem
     */
    public int getMaxmem()
    {
        return this.maxmem;
    }

    /**
     * Method getResgisters returns the register arrayList
     */
    public ArrayList<Register> getRegisters ()
    {
        return registers;
    }

    /**
     * Get HashMap returns the hashmap
     */
    public HashMap getHashMap()
    {
        return keywords;
    }

    /**
     * Get instructions returns this.instructions
     * @return this.instructions
     */
    public ArrayList<Instruction> getInstructions()
    {
        return this.instructions;
    }
}

