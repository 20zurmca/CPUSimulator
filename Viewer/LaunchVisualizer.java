
/**
 * Class LaunchVisualizer allows the user to start the Viewer program
 *
 * @author Cameron Zurmuhl
 * @version 10/22/2017 6:50 P.M
 */
public class LaunchVisualizer
{
    public static void main(String [] args)
    {
       if(!(args.length == 4) ||!(args[1].equals("true") || args[1].equals("false")) || !(args[0].equals("AssemblyCode.o")) || !(Integer.parseInt(args[2],16) % 4 == 0)|| !(Integer.parseInt(args[3],16) % 4 == 0) || (Integer.parseInt(args[2],16) < 0) || Integer.parseInt(args[3],16) > 4096 || Integer.parseInt(args[2],16) > Integer.parseInt(args[3],16))
        {
            System.out.println("USAGE: java LaunchVisualizer String boolean int int");
            System.out.println("\nString is name of the .o file (AssemblyCode.o) boolean is either 'true' for hexadecimal visualization, false for binary");
            System.out.println("Both ints must be in hexadecimal.  Both ints must be divisible by 4.  int 1 must be less than int 2.\nBoth ints must be positive. Int 2 canot be greater than 0x1000");
            System.exit(0);
        }
        ImageVisualizerGUI v =new ImageVisualizerGUI(args[0], Boolean.parseBoolean(args[1]), Integer.parseInt(args[2],16), Integer.parseInt(args[3],16));
    }
}
