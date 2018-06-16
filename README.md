# CPUSimulator
## Overal Description
This Project is composed of a three-program tool chain. This README will describe the order in which programs should be called and how to run the toolchain.

### Program 1: Assembler
Description: A program that interprets common ARM assembly instructions and generates an image file (.o file)<br /><br />
*Input: “AssemblyCode.as”* <br />
*Output: “AssemblyCode.o” in the working directory* <br /><br />
**Command line usage: <br />
java LaunchAssembler “AssemblyCode.as”** <br/>

After running the program, check the Assembler directory for the file.

### Program 2: CPU_Emulator
Descirption: A GUI that displays the processing of pre-written ARM assembly. The user can interact with the GUI using buttons. <br /><br />
*Input: “AssemblyCode.o” “true/false”* <br />
(Note: If “AssemblyCode.o” is somehow not in this directory, copy the file over from the Assembler directory.) <br />
*Output: A GUI allowing the user to control a simulation. Opon completion of the LEGv8 assembly, a "state of the machine" summary report will be printed to the CLI and there is an optional export to file button. This report summarizes the content of all registers and flags.* <br /><br />
**Command line usage: <br />
java LaunchCPU “AssemblyCode.o” Boolean <br />
Pass “true” to Boolean value for noisy mode (a debugging mode which shows print statements of the simulation in the CLI). Else, pass “false”.** <br/>

### Program 3: Viewer
Description: A GUI that displays the memory. The user can examine specific parts of the memory. The size of the memory is specified in the .as file for **Program 1**.<br /><br />

**Command line usage:
java LaunchVisualizer String, Boolean, int, int<br/>
**String** is the name of the image file you would like to view. Make sure this file is in the “Viewer” directory. **Boolean** is either “true” for viewing the memory in hexadecimal, or “false” for binary.<br />
The **first int** is the starting position in memory you’d like to view. This int *must be in hexadecimal and divisible by 4. This int must be at least 0 and no greater than the second int.*<br />
The **second int** is the ending position in memory you’d like to view. *This int must be in
hexadecimal and divisible by 4. This int must be at least the first int and no greater than the
maximum memory size (which is specified in the .as file).*<br /><br />

*Output: A GUI allowing the user to examine arbitrary memory.* <br />
**Example: java LaunchVisualizer “AssemblyCode.o” “true” 4 c**

## Distribution
The Jar_Distribution directory contains jar files that execute the programs with the same command line arguments as described. You will find project manuels in that folder that deliever the same information as the README.md. Project Documentation is available in the Documentation folder. Click on index.html

## Demo
<b>Running the simulation on "fast mode" with binary. ARM assembly code in lower right corner. Notice the viewer program also launches:</b>
<img src = "https://github.com/20zurmca/CPUSimulator/blob/master/Demonstrations/cpu_emulator.gif"> <br />
Note that the boxes labelled with "X_" are registers. PC is program counter. IR is instruction register. 

<b>After clicking "export to file:":</b>
<img src = "https://github.com/20zurmca/CPUSimulator/blob/master/Demonstrations/state_of_the_machine.PNG">

<b> Demonstrating the memory viewer:</b>
<ul>
  <li> hex: <br />
    <img src = "https://github.com/20zurmca/CPUSimulator/blob/master/Demonstrations/viewer_demo_hex.gif">
  </li>
  
  <li> bin: (Notice that the <em>Viewer Program</em> can specify the memory region that you can view) <br />
    <img src = "https://github.com/20zurmca/CPUSimulator/blob/master/Demonstrations/viewer_demo_binary.gif">
  </li>
</ul>
<b> Running Noisy Mode for debugging: </b>
<img src = "https://github.com/20zurmca/CPUSimulator/blob/master/Demonstrations/noisy_mode.gif">

## Future Expandability

