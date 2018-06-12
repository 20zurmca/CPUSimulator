# CPUSimulator
## Overal Description
This Project is composed of a three-program tool chain. This README will describe the order in which programs should be called and how to run the toolchain.

### Program 1: Assembler
Description: A program that interprets main ARM assembly instructions and generates an image file (.o file)
*Input: “AssemblyCode.as”*
*Output: “AssemblyCode.o” in the working directory*
**Command line usage:
java LaunchAssembler “AssemblyCode.as”**

After running the program, check the Assembler directory for the file.

### Program 2: CPU_Emulator
Descirption: A GUI that displays the processing of pre-written ARM assembly. The user can interact with the GUI using buttons.
*Input: “AssemblyCode.o” “true/false”*
(Note: If “AssemblyCode.o” is somehow not in this directory, copy the file over from the Assembler directory.)
*Output: A GUI allowing the user to control a simulation*
**Command line usage:
java LaunchCPU “AssemblyCode.o” Boolean
Pass “true” to Boolean value for noisy mode (a debugging mode which shows print statements of the simulation in the CLI). Else, pass “false”.**

### Program 3: Viewer
Description: A GUI that displays the memory. The user can examine specific parts of the memory. The size of the memory is specified in the .as file for **Program 1**.

**Command line usage:
java LaunchVisualizer String, Boolean, int, int
**String** is the name of the image file you would like to view. Make sure this file is in the “Viewer” directory. **Boolean** is either “true” for viewing the memory in hexadecimal, or “false” for binary.
The **first int** is the starting position in memory you’d like to view. This int *must be in hexadecimal and divisible by 4. This int must be at least 0 and no greater than the second int.*
The **second int** is the ending position in memory you’d like to view. *This int must be in
hexadecimal and divisible by 4. This int must be at least the first int and no greater than the
maximum memory size (which is specified in the .as file).*

*Output: A GUI allowing the user to examine arbitrary memory.*
**Example: java LaunchVisualizer “AssemblyCode.o” “true” 4 c**
