import java.util.Arrays;
import java.util.HashMap;
/**
 * A class that represents an instruction 
 *
 * @author Cameron Zurmuhl
 * @10/1/2017
 */
public class Instruction
{

    //*********************************FIELDS*****************************//

    private String operator; //A string that stores operator of instruction i.e ADD, MUL, SUBI

    private Argument [] args;  //And array of arguments of the instructor

    int address; //the address of the instruction 

    //***************************CONSTRUCTORS**********************************//
    /**
     * Constructor for class Instruction
     * @param line the scanned input line from the .as file
     * The Instruction will be broken into tokens and processed
     */
    public Instruction(String operator, Argument [] args, int address)
    {
        this.operator = operator;
        this.args = args;
        this.address = address;

    }

    //****************************************METHODS*****************************//////////
    
    /**
     * Method buildSpecial builds hex code for speical instructions halt and nop
     * Instruction type: 32 bit opcode
     */
    public String buildSpecial()
    {
        if(operator.equals("NOP"))
        {
            return "0x00000000";
        } else if(operator.equals("HALT"))
        {
            return "0x11111111";
        }
        return null;
    }
    
    /**
     * Method buildStack builds hex code for stack operations push and pop
     * Instruction type size: 16 bit opcode, 16 bit Rd
     */
    public String buildStack()
    {
        String opcode = null;
        String hexRD = Integer.toHexString(args[0].getRegNum());
        while(hexRD.length() < 4)
        {
            hexRD = "0" + hexRD;
        }
        
        if(operator.equals("PUSH"))
        {
            opcode = "0x0222";
        } else if(operator.equals("POP"))
        {
            opcode = "0x0333";
        }
        return opcode + hexRD;
    }

    /**
     * Method buildRType builds hex code for a R-type instruction 
     * Instruction type size: 16 bit opcode, 4 bit Rm, 4 bit shamt, 4 bit Rn, 4 bit Rd
     * @return the hex form of the instruction
     */
    public String buildRType()
    {
        String opcode = null;
        String shamt = null;
        String hexRD = Integer.toHexString(args[0].getRegNum());
        String hexRN = Integer.toHexString(args[1].getRegNum());
        String hexRM = Integer.toHexString(args[2].getRegNum());
        if(operator.equals("ADD"))
        {
            opcode = "0x0458";
            shamt = "0";
        } else if (operator.equals("SUB"))
        {
            opcode = "0x0658";
            shamt = "0";
        } else if (operator.equals("AND"))
        {
            opcode = "0x0450";
            shamt = "0";
        } else if (operator.equals("ORR"))
        {
            opcode = "0x0550";
            shamt = "0";
        } else if (operator.equals("EOR"))
        {
            opcode = "0x0650";
            shamt = "0";
        } else if (operator.equals("LSL"))
        {
            opcode = "0x069B";
            shamt = hexRM;
            return opcode + shamt + hexRN;
        }
        else if (operator.equals("LSR"))
        {
            opcode = "0x069A";
            shamt = hexRM;
            return opcode + shamt + hexRN;
        }
        else if (operator.equals("SUBS"))
        {
            opcode = "0x0758";
            shamt = "0";
        } else if (operator.equals("ADDS"))
        {
            opcode = "0x0558";
            shamt = "0";
        } else if (operator.equals("BR"))
        {
            opcode = "0x06B0";
            shamt = "0";
        }
        return opcode + hexRM + shamt + hexRN + hexRD;
    }

    /**
     * Method buildDType builds hex code for D-type instruction
     * Instruction size: 12 bit opcode, 12 bit DT_address, 4 bit Rn, 4 bit Rt
     * @return the hex form of the instruction
     */
    public String buildDType()
    {
        String opcode = null;
        String hexDTAddress = Integer.toHexString(args[2].getImmediate());

        while(hexDTAddress.length() < 3)
        {
            hexDTAddress = "0" + hexDTAddress;
        }

        String hexRN = Integer.toHexString(args[1].getRegNum());
        String hexRT = Integer.toHexString(args[0].getRegNum());
        if(operator.equals("LDUR"))
        {
            opcode = "0x7C2";
        } else if(operator.equals("STUR"))
        {
            opcode = "0x7C0";
        } else if(operator.equals("LDURSW"))
        {
            opcode = "0x5C4";
        } else if(operator.equals("STURW"))
        {
            opcode = "0x5C0";
        } else if(operator.equals("LDURH"))
        {
            opcode = "0x3C2";
        } else if(operator.equals("STURH"))
        {
            opcode = "0x3C0";
        } else if(operator.equals("LDURB"))
        {
            opcode = "0x1C2";
        } else if(operator.equals("STURB"))
        {
            opcode = "0x1C0";
        }
        return opcode + hexDTAddress + hexRN + hexRT;

    }

    /**
     * Method moveZ handles the moveZ instruction (converts to a hex instruction)
     * Size of instruction is 12 bit opcode 4 bit rd 16 bit address
     * @param rd the register to load data to
     * @param address the address of the data 
     * @return the instruction for moveZ in hex
     */
    public String moveZ(String rd, int data)
    {
        String opcode = "0x694";
        String hexRD = rd.substring(1,2);
        String hexAddress = Integer.toHexString(data); //getting the address of data from the hashmap in the assembler
        while(hexAddress.length() < 4)
        {
            hexAddress = "0" + hexAddress;
        }
        return opcode + hexRD + hexAddress;
    }

    /**
     * Method buildIType builds hex code for I-type instruction
     * Instruction type size: 16 bit opcode, 8 bit immediate, 4 bit Rn, 4 bit Rd
     * @return the hex form of the instruction
     */
    public String buildIType()
    {
        String opcode = null;
        String hexRD = Integer.toHexString(args[0].getRegNum());
        String hexRN = Integer.toHexString(args[1].getRegNum());
        String immediate = Integer.toHexString(args[2].getImmediate());
        if(operator.equals("ADDI"))
        {
            opcode = "0x0488";
        } else if (operator.equals("SUBI"))
        {
            opcode = "0x0688";
        } else if (operator.equals("ADDIS"))
        {
            opcode = "0x0588";
        } else if (operator.equals("SUBIS"))
        {
            opcode = "0x0788";
        } else if (operator.equals("ANDI"))
        {
            opcode = "0x0490";
        } else if (operator.equals("ORRI"))
        {
            opcode = "0x0590";
        } else if (operator.equals("EORI"))
        {
            opcode = "0x0690";
        }
        return opcode + immediate + hexRN + hexRD;
    }

    /**
     * Method buildBType builds hex code for B-type instruction
     * Instruction type size: 16 bit opcode, 16 bit BR address
     * @return the hex form of the instruction 
     */
    public String buildBType()
    {
        String opcode = null;
        String branch = null;
        if(args[0].getArgumentType().equals("Register"))
        {
            branch = Integer.toHexString(args[0].getRegNum());
            while(branch.length() < 4)
            {
                branch = "0" + branch;
            }
        } else {
            branch = Integer.toHexString(args[0].getImmediate());
            while(branch.length() < 4)
            {
                branch = "0" + branch;
            }
        }

        if (operator.equals("B"))
        {
            opcode = "0x00A0";
        } else if (operator.equals("BL"))
        {
            opcode = "0x04A0";
        } 
        return opcode + branch;
    }

    /**
     * Method buildBType builds hex code for CB-type instruction
     * Instruction size: 12 bit opcode, 4 bit Rt, 16 bit Cond_BR_Address
     * @return the hex form of the instruction 
     */
    public String buildCBType()
    {
        String opcode = null;
        String hexRT = Integer.toHexString(args[0].getRegNum());
        String hex_Cond_BRA = Integer.toHexString(args[1].getImmediate());
        while(hex_Cond_BRA.length() < 4)
        {
            hex_Cond_BRA = "0" + hex_Cond_BRA;
        }

        if(operator.equals("CBZ"))
        {
            opcode = "0x5A0";
        } else if(operator.equals("CBNZ"))
        {
            opcode = "0x5A8";
        } else if(operator.equals("B.cond"))
        {
            opcode = "0x2A0";
        }
        return opcode + hex_Cond_BRA + hexRT;
    }

    /**
     * method getType() returns this intruction's type (R, D, I...)
     * @return the type of the instruction
     */
    public String getType()
    {
        String [] RType = new String [] {"ADD", "SUB", "AND", "ADDS", "SUBS", "BR", "ORR", "EOR", "LSL", "LSR"};
        String [] IType = new String [] {"ADDI", "SUBI", "ADDIS", "SUBIS", "ANDI", "ORRI", "EORI"};
        String [] DType = new String [] {"LDUR", "STUR", "LDRUSW", "STURW", "LDURH", "STURH", "LDURB", "STURB"};
        String [] BType = new String [] {"B", "BL"};
        String [] CBType = new String [] {"B.cond", "CBNZ", "CBZ"};
        String [] StackType = new String [] {"PUSH","POP"};
        if(Arrays.asList(RType).contains(operator))
        {
            return "RType";
        } else if(Arrays.asList(IType).contains(operator))
        {
            return "IType";
        } else if(Arrays.asList(DType).contains(operator))
        {
            return "DType";
        } else if(Arrays.asList(BType).contains(operator))
        {
            return "BType";
        } else if(Arrays.asList(CBType).contains(operator))
        {
            return "CBType";
        } else if(operator.equals("MOVEZ"))
        {
            return "IWType";
        } else if(Arrays.asList(StackType).contains(operator))
        {
            return "StackType";
        }
        return "SPECIAL";
    }

    /**
     * Method printInstruction prints the Argument and its operator
     */
    public void printInstruction()
    {
        System.out.print(operator);
        for(Argument a : args)
        {
            System.out.print(" " + a);
        }
        System.out.println();
    }

    /**
     * Method getOperator returns this.operator
     * @return this.operator
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * Method setOperator sets this.operator 
     * @param op the new operator
     */
    public void setOperator(String op)
    {
        operator = op;
    }

    /**
     * Method getArgument returns an argument at an index
     * @return an Argument
     */
    public Argument getArgument(int index )
    {
        return args[index];
    }

    /**
     * Method setArguments sets this.Arguments
     * @param ops the new Arguments
     */
    public void setArguments(Argument [] args)
    {
        this.args = args;
    }

    /**
     * Method getAddress returns the address of the instruction
     * @return this.address
     */
    public int getAddress()
    {
        return address;
    }
}
