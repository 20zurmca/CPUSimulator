
/**
 * Class LaunchCPU allows the user to start the CPU simulator 
 *
 * @author Cameron Zurmuhl
 * @version 10/22/2017
 */
public class LaunchCPU
{
   public static void main(String [] args)
   {
       if(!(args.length == 2) || !args[0].equals("AssemblyCode.o") || !(args[1].equals("true") || args[1].equals("false")))
       {
           System.out.println("USAGE: java CPULauncher String boolean");
           System.out.println("String must be fileName 'AssemblyCode.o'");
           System.out.println("boolean is 'true' or 'false' for noisy mode");
           System.exit(0);
        }
       ProcessorWindow p = new ProcessorWindow(args[0], Boolean.parseBoolean(args[1]));
       ImageVisualizerGUI g= new ImageVisualizerGUI(true); //display memory only in hex
    }
}
