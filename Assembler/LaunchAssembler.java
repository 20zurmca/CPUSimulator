
/**
 * A class that launches the assembler
 * @author Cameron Zurmuhl
 * @version 10/22/2017 3:30 P.m
 */
public class LaunchAssembler
{
    public static void main(String [] args)
    {
        if(!(args.length == 1) || !args[0].equals("AssemblyCode.as"))
        {
            System.out.println("USAGE: java LaunchAssembler String");
            System.out.println("String = AssemblyCode.as");
            System.exit(0);
        }
        Assembler as = new Assembler(args[0]);
        as.fillQueue();
        as.assemble();
        as.generateImageFile();
        as.generateStatistics();
    }
}
