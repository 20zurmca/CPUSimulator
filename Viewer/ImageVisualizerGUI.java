/**
 * Class ImageVisrualizerGUI allows the user to exam arbitrary image files at a specified range, determined by the command line
 * The class allows both binary and hex support
 */
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.border.Border;
import javax.swing.*;

public class ImageVisualizerGUI extends JFrame {
    private JMenuBar menuBar;
    private JButton button1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private TextArea textarea1;
    private JTextField textfield1;
    private JTextField textfield2;
    private JTextField textfield3;
    private ImageVisualizer v;
    private boolean isHex;
    private int start;
    private int end;
    String oFile;

    /**
     * Constructor for class ImageVisualizerGui
     * @param oFile the file to read the memory from
     * @param hex whether to report in hex or binary
     * @param start the start of the memory range
     * @param end the end of the memory range
     */
    public ImageVisualizerGUI(String oFile, boolean hex, int start, int end){
        this.oFile = oFile;
        isHex = hex;
        this.start = start;
        this.end = end;
        v=new ImageVisualizer(oFile, hex, start, end);
        v.processHeader();
        v.processMemory();
        this.setTitle("ImageVisualizerGUI");
        this.setSize(993,767);
        //menu generate method
        generateMenu();
        this.setJMenuBar(menuBar);

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(993,767));
        contentPane.setBackground(new Color(102,255,255));

        button1 = new JButton();
        button1.setBounds(630,80,90,35);
        button1.setBackground(new Color(214,217,223));
        button1.setForeground(new Color(0,0,0));
        button1.setEnabled(true);
        button1.setFont(new Font("SansSerif",1,18));
        button1.setText("GO");
        button1.setVisible(true);
        //Set action for button click
        //Call defined method
        button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    memLookUp(evt);
                }
            });

        label1 = new JLabel();
        label1.setBounds(420,11,648,35);
        label1.setBackground(new Color(214,217,223));
        label1.setForeground(new Color(0,0,0));
        label1.setEnabled(true);
        label1.setFont(new Font("SansSerif",1,20));
        label1.setText("Entry Memory Location in Hex.  (Enter a four-divisible digit).");
        label1.setVisible(true);

        label2 = new JLabel();
        label2.setBounds(528,83,43,32);
        label2.setBackground(new Color(214,217,223));
        label2.setForeground(new Color(0,0,0));
        label2.setEnabled(true);
        label2.setFont(new Font("SansSerif",1,20));
        label2.setText("0x");
        label2.setVisible(true);

        label3 = new JLabel();
        label3.setBounds(440,236,90,35);
        label3.setBackground(new Color(214,217,223));
        label3.setForeground(new Color(0,0,0));
        label3.setEnabled(true);
        label3.setFont(new Font("SansSerif",1,25));
        label3.setText("LEGv8");
        label3.setVisible(true);
        
        label4 = new JLabel();
        label4.setBounds(430,150,90,35);
        label4.setBackground(new Color(214,217,223));
        label4.setForeground(new Color(0,0,0));
        label4.setEnabled(true);
        label4.setFont(new Font("SansSerif",1,18));
        label4.setText("ERRORS");
        label4.setVisible(true);

        textarea1 = new TextArea();
        textarea1.setBounds(5,5,410,772);
        textarea1.setBackground(new Color(255,255,255));
        textarea1.setForeground(new Color(0,0,0));
        textarea1.setEnabled(true);
        textarea1.setFont(new Font("sansserif",0,18));
        if(hex){
            textarea1.setText(v.memoryDisplayHex());
        } else {
            textarea1.setText(v.memoryDisplayBin());
        }
        // textarea1.setBorder(BorderFactory.createBevelBorder(1));
        textarea1.setVisible(true);

        textfield1 = new JTextField();
        textfield1.setBounds(550,81,79,37);
        textfield1.setBackground(new Color(255,255,255));
        textfield1.setForeground(new Color(0,0,0));
        textfield1.setEnabled(true);
        textfield1.setFont(new Font("SansSerif",0,18));
        textfield1.setText("");
        textfield1.setVisible(true);

        textfield2 = new JTextField();
        textfield2.setBounds(550,234,272,43);
        textfield2.setBackground(new Color(255,255,255));
        textfield2.setForeground(new Color(0,0,0));
        textfield2.setEnabled(true);
        textfield2.setFont(new Font("SansSerif",0,18));
        textfield2.setText("");
        textfield2.setVisible(true);
        
        textfield3 = new JTextField();
        textfield3.setBounds(420,175,540,43);
        textfield3.setBackground(new Color(255,255,255));
        textfield3.setForeground(new Color(0,0,0));
        textfield3.setEnabled(true);
        textfield3.setFont(new Font("SansSerif",0,12));
        textfield3.setText("");
        textfield3.setVisible(true);

        //adding components to contentPane panel
        contentPane.add(button1);
        contentPane.add(label1);
        contentPane.add(label2);
        contentPane.add(label3);
        contentPane.add(label4);
        contentPane.add(textarea1);
        contentPane.add(textfield1);
        contentPane.add(textfield2);
        contentPane.add(textfield3);

        //adding panel to JFrame and seting of window position and close operation
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    //Method actionPerformed for button1
    /**
     * Method memLoopUp allows examination of memory at a specified range
     */
    private void memLookUp (ActionEvent evt) {
        int pos = Integer.parseInt(textfield1.getText(),16); //get user input where to start 
        if(!(pos % 4 ==0) || pos < -1 || pos > end || pos<start) //error messages 
        {
            textfield3.setText("Invalid input.  Memory location must be divisible by four, positive, and in bounds (< end, > start)");
            textfield2.setText("");
            return;
        } else {
                        textfield3.setText("");
        }
        String instruction = v.getMem().getFourBytes(pos); //get the memory and parse 
        String binInstruction = BinaryFormater.format(Integer.toBinaryString(Integer.parseInt(instruction, 16)),32);
        if(isHex){
            textfield2.setText(v.convertToAssemblyHex(instruction)); //if hex convert to hex instruction 
        } else {
            textfield2.setText(v.convertToAssemblyBin(binInstruction));
        }
    }

    //method for generate menu
    /**
     * Method generateMenu generates the menu interface for this GUI
     */
    public void generateMenu(){
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu tools = new JMenu("Tools");
        JMenu help = new JMenu("Help");

        JMenuItem open = new JMenuItem("Open   ");
        JMenuItem save = new JMenuItem("Save   ");
        JMenuItem exit = new JMenuItem("Exit   ");
        JMenuItem preferences = new JMenuItem("Preferences   ");
        JMenuItem about = new JMenuItem("About   ");

        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(exit);
        tools.add(preferences);
        help.add(about);

        menuBar.add(file);
        menuBar.add(tools);
        menuBar.add(help);
    }

    public static void main(String[] args){
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ImageVisualizerGUI(args[0], Boolean.parseBoolean(args[1]), Integer.parseInt(args[2],16), Integer.parseInt(args[3],16));
                }
            });
    }

}