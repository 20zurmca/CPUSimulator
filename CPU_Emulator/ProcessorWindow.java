/**
 *A class that wraps the CPU simulation in a GUI
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
import java.io.*;

public class ProcessorWindow extends JFrame {
    private JMenuBar menuBar;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JLabel label1;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JPanel panel1;
    private JPanel panel2;
    private JTextArea textarea10;
    private JTextArea textarea11;
    private JTextArea textarea4;
    private JTextArea textarea5;
    private JTextArea textarea6;
    private JTextArea textarea7;
    private JTextArea textarea8;
    private JTextArea textarea9;
    private JTextField textfield2;
    private JTextField textfield3;
    private JTextField textfield4;
    private JTextField textfield5;
    private JTextField textfield6;
    private JTextField textfield7;
    private JTextField textfield8;
    private JTextField textfield9;

    private Simulator sim; //simulator 
    private Integer clock = 0; //counter
    private Timer stepTimer; //swing timer
    private String file; //image file
    private boolean noisy; //noisy mode 
    //Constructor 
    /**
     * Constructor for class ProcessorWindow
     * @param file the image file to work with
     * @param noisy whether to activate noisy mode or not (prints to terminal)
     */
    public ProcessorWindow(String file, boolean noisy){
        this.file = file;
        this.noisy = noisy;
        sim = new Simulator(file, noisy);
        sim.processHeader();
        sim.processMemory(); //load memory 
        this.setTitle("GUI_project");
        this.setSize(1479,719);
        //menu generate method
        generateMenu();
        this.setJMenuBar(menuBar);

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(1479,719));
        contentPane.setBackground(new Color(192,192,192));

        button1 = new JButton();
        button1.setBounds(260,107,179,55);
        button1.setBackground(new Color(214,217,223));
        button1.setForeground(new Color(0,0,0));
        button1.setEnabled(true);
        button1.setFont(new Font("SansSerif",1,18));
        button1.setText("Step Instruction");
        button1.setVisible(true);
        //Set action for button click
        //Call defined method
        button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    stepInstruction(evt);
                }
            });

        button2 = new JButton();
        button2.setBounds(261,177,181,49);
        button2.setBackground(new Color(214,217,223));
        button2.setForeground(new Color(0,0,0));
        button2.setEnabled(true);
        button2.setFont(new Font("SansSerif",1,18));
        button2.setText("Reset");
        button2.setVisible(true);
        //Set action for button click
        //Call defined method
        button2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    reset(evt);
                }
            });

        button3 = new JButton();
        button3.setBounds(264,249,179,61);
        button3.setBackground(new Color(214,217,223));
        button3.setForeground(new Color(0,0,0));
        button3.setEnabled(true);
        button3.setFont(new Font("SansSerif",1,18));
        button3.setText("Exit");
        button3.setVisible(true);
        //Set action for button click
        //Call defined method
        button3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    exit(evt);
                }
            });

        button4 = new JButton();
        button4.setBounds(267,330,182,48);
        button4.setBackground(new Color(214,217,223));
        button4.setForeground(new Color(0,0,0));
        button4.setEnabled(true);
        button4.setFont(new Font("SansSerif",1,18));
        button4.setText("Export To File");
        button4.setVisible(true);
        //Set action for button click
        //Call defined method
        button4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    exportToFile(evt);
                }
            });

        button5 = new JButton();
        button5.setBounds(33,103,151,49);
        button5.setBackground(new Color(214,217,223));
        button5.setForeground(new Color(0,0,0));
        button5.setEnabled(true);
        button5.setFont(new Font("SansSerif",1,14));
        button5.setText("Process Slow");
        button5.setVisible(true);
        //Set action for button click
        //Call defined method

        button5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    processSlow(evt);
                }
            });

        button6 = new JButton();
        button6.setBounds(25,178,190,44);
        button6.setBackground(new Color(214,217,223));
        button6.setForeground(new 
            Color(0,0,0));
        button6.setEnabled(true);
        button6.setFont(new Font("SansSerif",1,18));
        button6.setText("Process Medium");
        button6.setVisible(true);
        //Set action for button click
        //Call defined method
        button6.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    processMedium(evt);
                }
            });

        button7 = new JButton();
        button7.setBounds(38,250,148,51);
        button7.setBackground(new Color(214,217,223));
        button7.setForeground(new Color(0,0,0));
        button7.setEnabled(true);
        button7.setFont(new Font("SansSerif",1,18));
        button7.setText("Process Fast");
        button7.setVisible(true);
        //Set action for button click
        //Call defined method
        button7.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    processFast(evt);
                }
            });

        label1 = new JLabel();
        label1.setBounds(42,27,76,44);
        label1.setBackground(new Color(214,217,223));
        label1.setForeground(new Color(255,255,0));
        label1.setEnabled(true);
        label1.setFont(new Font("SansSerif",1,25));
        label1.setText("Time:");
        label1.setVisible(true);

        label10 = new JLabel();
        label10.setBounds(43,567,46,25);
        label10.setBackground(new Color(214,217,223));
        label10.setForeground(new Color(255,255,0));
        label10.setEnabled(true);
        label10.setFont(new Font("SansSerif",1,30));
        label10.setText("PC");
        label10.setVisible(true);

        label11 = new JLabel();
        label11.setBounds(44,622,60,28);
        label11.setBackground(new Color(214,217,223));
        label11.setForeground(new Color(255,255,0));
        label11.setEnabled(true);
        label11.setFont(new Font("SansSerif",1,30));
        label11.setText("IR");
        label11.setVisible(true);

        label12 = new JLabel();
        label12.setBounds(26,273,206,30);
        label12.setBackground(new Color(0,0,255));
        label12.setForeground(new Color(255,255,0));
        label12.setEnabled(true);
        label12.setFont(new Font("SansSerif",1,30));
        label12.setText("Instruction 0x");
        label12.setVisible(true);

        label13 = new JLabel();
        label13.setBounds(20,670,109,32);
        label13.setBackground(new Color(214,217,223));
        label13.setForeground(new Color(255,255,0));
        label13.setEnabled(true);
        label13.setFont(new Font("SansSerif",1,30));
        label13.setText("LEGv8");
        label13.setVisible(true);

        label2 = new JLabel();
        label2.setBounds(66,131,29,29);
        label2.setBackground(new Color(214,217,223));
        label2.setForeground(new Color(255,255,0));
        label2.setEnabled(true);
        label2.setFont(new Font("SansSerif",1,30));
        label2.setText("N");
        label2.setVisible(true);

        label3 = new JLabel();
        label3.setBounds(168,135,39,27);
        label3.setBackground(new Color(214,217,223));
        label3.setForeground(new Color(255,255,0));
        label3.setEnabled(true);
        label3.setFont(new Font("SansSerif",1,30));
        label3.setText("Z");
        label3.setVisible(true);

        label4 = new JLabel();
        label4.setBounds(268,135,37,31);
        label4.setBackground(new Color(214,217,223));
        label4.setForeground(new Color(255,255,0));
        label4.setEnabled(true);
        label4.setFont(new Font("SansSerif",1,30));
        label4.setText("C");
        label4.setVisible(true);

        label5 = new JLabel();
        label5.setBounds(370,132,31,35);
        label5.setBackground(new Color(255,0,0));
        label5.setForeground(new Color(255,255,51));
        label5.setEnabled(true);
        label5.setFont(new Font("SansSerif",1,30));
        label5.setText("V");
        label5.setVisible(true);

        label6 = new JLabel();
        label6.setBounds(45,337,72,33);
        label6.setBackground(new Color(214,217,223));
        label6.setForeground(new Color(255,255,0));
        label6.setEnabled(true);
        label6.setFont(new Font("SansSerif",1,30));
        label6.setText("X0");
        label6.setVisible(true);

        label7 = new JLabel();
        label7.setBounds(44,389,44,35);
        label7.setBackground(new Color(255,0,0));
        label7.setForeground(new Color(255,255,0));
        label7.setEnabled(true);
        label7.setFont(new Font("SansSerif",1,30));
        label7.setText("X1");
        label7.setVisible(true);

        label8 = new JLabel();
        label8.setBounds(44,447,58,33);
        label8.setBackground(new Color(214,217,223));
        label8.setForeground(new Color(255,255,0));
        label8.setEnabled(true);
        label8.setFont(new Font("SansSerif",1,30));
        label8.setText("X2");
        label8.setVisible(true);

        label9 = new JLabel();
        label9.setBounds(43,510,57,27);
        label9.setBackground(new Color(214,217,223));
        label9.setForeground(new Color(255,255,51));
        label9.setEnabled(true);
        label9.setFont(new Font("SansSerif",1,30));
        label9.setText("X3");
        label9.setVisible(true);

        panel1 = new JPanel(null);
        panel1.setBorder(BorderFactory.createEtchedBorder(1));
        panel1.setBounds(5,2,775,712);
        panel1.setBackground(new Color(0,0,255));
        panel1.setForeground(new Color(0,0,0));
        panel1.setEnabled(false);
        panel1.setFont(new Font("sansserif",0,12));
        panel1.setVisible(true);

        panel2 = new JPanel(null);
        panel2.setBorder(BorderFactory.createEtchedBorder(1));
        panel2.setBounds(775,2,700,712);
        panel2.setBackground(new Color(0,0,255));
        panel2.setForeground(new Color(0,0,0));
        panel2.setEnabled(false);
        panel2.setFont(new Font("sansserif",0,12));
        panel2.setVisible(true);

        textarea10 = new JTextArea();
        textarea10.setBounds(46,417,539,292);
        textarea10.setBackground(new Color(255,255,255));
        textarea10.setForeground(new Color(0,0,0));
        textarea10.setEnabled(true);
        textarea10.setFont(new Font("SansSerif",0,14));
        textarea10.setText("ASSEMBLY:\nMOVEZ x0, data\nLDUR x1, [x0, #0]\nCBZ  x2, #32\nADDI x2, x2, #20\nSUB x2, x2, x1\nPUSH x2\nNOP\nPOP  x3\nSTUR x3, [x0, #0]\nEOR  x3, x3, x2\nCBZ x3, #4\nB  #12\nHALT");
        textarea10.setBorder(BorderFactory.createBevelBorder(1));
        textarea10.setVisible(true);

        textarea11 = new JTextArea();
        textarea11.setBounds(127,671,386,31);
        textarea11.setBackground(new Color(255,255,255));
        textarea11.setForeground(new Color(0,0,0));
        textarea11.setEnabled(true);
        textarea11.setFont(new Font("SansSerif",0,18));
        textarea11.setText(sim.convertToAssembly(sim.getIR().getData()));
        textarea11.setBorder(BorderFactory.createBevelBorder(1));
        textarea11.setVisible(true);

        textarea4 = new JTextArea();
        textarea4.setBounds(100,343,313,29);
        textarea4.setBackground(new Color(255,255,255));
        textarea4.setForeground(new Color(0,0,0));
        textarea4.setEnabled(true);
        textarea4.setFont(new Font("SansSerif",0,18));
        textarea4.setText(sim.getRegister(0).getData());
        textarea4.setBorder(BorderFactory.createBevelBorder(1));
        textarea4.setVisible(true);

        textarea5 = new JTextArea();
        textarea5.setBounds(101,399,317,27);
        textarea5.setBackground(new Color(255,255,255));
        textarea5.setForeground(new Color(0,0,0));
        textarea5.setEnabled(true);
        textarea5.setFont(new Font("SansSerif",0,18));
        textarea5.setText(sim.getRegister(1).getData());
        textarea5.setBorder(BorderFactory.createBevelBorder(1));
        textarea5.setVisible(true);

        textarea6 = new JTextArea();
        textarea6.setBounds(102,455,316,25);
        textarea6.setBackground(new Color(255,255,255));
        textarea6.setForeground(new Color(0,0,0));
        textarea6.setEnabled(true);
        textarea6.setFont(new Font("SansSerif",0,18));
        textarea6.setText(sim.getRegister(2).getData());
        textarea6.setBorder(BorderFactory.createBevelBorder(1));
        textarea6.setVisible(true);

        textarea7 = new JTextArea();
        textarea7.setBounds(104,513,311,28);
        textarea7.setBackground(new Color(255,255,255));
        textarea7.setForeground(new Color(0,0,0));
        textarea7.setEnabled(true);
        textarea7.setFont(new Font("SansSerif",0,18));
        textarea7.setText(sim.getRegister(3).getData());
        textarea7.setBorder(BorderFactory.createBevelBorder(1));
        textarea7.setVisible(true);

        textarea8 = new JTextArea();
        textarea8.setBounds(104,563,415,30);
        textarea8.setBackground(new Color(255,255,255));
        textarea8.setForeground(new Color(0,0,0));
        textarea8.setEnabled(true);
        textarea8.setFont(new Font("SansSerif",0,18));
        textarea8.setText(sim.getPC().getData());
        textarea8.setBorder(BorderFactory.createBevelBorder(1));
        textarea8.setVisible(true);

        textarea9 = new JTextArea();
        textarea9.setBounds(103,624,420,30);
        textarea9.setBackground(new Color(255,255,255));
        textarea9.setForeground(new Color(0,0,0));
        textarea9.setEnabled(true);
        textarea9.setFont(new Font("SansSerif",0,18));
        textarea9.setText(sim.getIR().getData());
        textarea9.setBorder(BorderFactory.createBevelBorder(1));
        textarea9.setVisible(true);

        textfield2 = new JTextField();
        textfield2.setBounds(112,35,94,35);
        textfield2.setBackground(new Color(255,255,255));
        textfield2.setForeground(new Color(0,0,0));
        textfield2.setEnabled(false);
        textfield2.setFont(new Font("SansSerif",0,20));
        textfield2.setText(clock.toString());
        textfield2.setVisible(true);

        textfield3 = new JTextField();
        textfield3.setBounds(49,172,70,51);
        textfield3.setBackground(new Color(255,255,255));
        textfield3.setForeground(new Color(0,0,0));
        textfield3.setEnabled(true);
        textfield3.setFont(new Font("SansSerif",1,30));
        textfield3.setText(sim.getSPSR().getData().substring(0,1));
        textfield3.setVisible(true);

        textfield4 = new JTextField();
        textfield4.setBounds(138,171,78,51);
        textfield4.setBackground(new Color(255,255,255));
        textfield4.setForeground(new Color(0,0,0));
        textfield4.setEnabled(true);
        textfield4.setFont(new Font("SansSerif",1,30));
        textfield4.setText(sim.getSPSR().getData().substring(1,2));
        textfield4.setVisible(true);

        textfield5 = new JTextField();
        textfield5.setBounds(240,173,74,51);
        textfield5.setBackground(new Color(255,255,255));
        textfield5.setForeground(new Color(0,0,0));
        textfield5.setEnabled(true);
        textfield5.setFont(new Font("SansSerif",1,30));
        textfield5.setText(sim.getSPSR().getData().substring(2,3));
        textfield5.setVisible(true);

        textfield6 = new JTextField();
        textfield6.setBounds(345,174,78,52);
        textfield6.setBackground(new Color(255,255,255));
        textfield6.setForeground(new Color(0,0,0));
        textfield6.setEnabled(true);
        textfield6.setFont(new Font("SansSerif",1,30));
        textfield6.setText(sim.getSPSR().getData().substring(3,4));
        textfield6.setVisible(true);

        textfield7 = new JTextField();
        textfield7.setBounds(433,32,219,52);
        textfield7.setBackground(new Color(255,255,255));
        textfield7.setForeground(new Color(0,0,0));
        textfield7.setEnabled(false);
        textfield7.setFont(new Font("SansSerif",0,30));
        textfield7.setText("Register Panel");
        textfield7.setVisible(true);

        textfield8 = new JTextField();
        textfield8.setBounds(243,28,213,58);
        textfield8.setBackground(new Color(255,255,255));
        textfield8.setForeground(new Color(0,0,0));
        textfield8.setEnabled(false);
        textfield8.setFont(new Font("SansSerif",0,30));
        textfield8.setText("Control Panel");
        textfield8.setVisible(true);

        textfield9 = new JTextField();
        textfield9.setBounds(225,271,260,37);
        textfield9.setBackground(new Color(255,255,255));
        textfield9.setForeground(new Color(0,0,0));
        textfield9.setEnabled(true);
        textfield9.setFont(new Font("SansSerif",0,18));
        textfield9.setText(BinaryFormater.format(Integer.toHexString(Integer.parseInt(sim.getIR().getData(),2)),8));
        textfield9.setVisible(true);

        //adding components to contentPane panel
        panel2.add(button1);
        panel2.add(button2);
        panel2.add(button3);
        panel2.add(button4);
        panel2.add(button5);
        panel2.add(button6);
        panel2.add(button7);
        panel1.add(label1);
        panel1.add(label10);
        panel1.add(label11);
        panel1.add(label12);
        panel1.add(label13);
        panel1.add(label2);
        panel1.add(label3);
        panel1.add(label4);
        panel1.add(label5);
        panel1.add(label6);
        panel1.add(label7);
        panel1.add(label8);
        panel1.add(label9);
        contentPane.add(panel1);
        contentPane.add(panel2);
        panel2.add(textarea10);
        panel1.add(textarea11);
        panel1.add(textarea4);
        panel1.add(textarea5);
        panel1.add(textarea6);
        panel1.add(textarea7);
        panel1.add(textarea8);
        panel1.add(textarea9);
        panel1.add(textfield2);
        panel1.add(textfield3);
        panel1.add(textfield4);
        panel1.add(textfield5);
        panel1.add(textfield6);
        panel1.add(textfield7);
        panel2.add(textfield8);
        panel1.add(textfield9);

        //adding panel to JFrame and seting of window position and close operation
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    //Method actionPerformed for button1
    /**
     * Method stepInstruction steps one instruction in the simulation
     * @param evt an ActionEvent
     */
    private void stepInstruction (ActionEvent evt) {
        if(clock < 13){
            //Fetch -- Execute -- Set Text Areas --Update clock --Reset flags
            String i = sim.fetchInstruction();
            String type = sim.decodeInstruction(i);
            sim.executeInstruction(type, i);
            textarea11.setText(sim.convertToAssembly(sim.getIR().getData()));
            textarea4.setText(sim.getRegister(0).getData());
            textarea5.setText(sim.getRegister(1).getData());
            textarea6.setText(sim.getRegister(2).getData());
            textarea7.setText(sim.getRegister(3).getData());
            textarea8.setText(sim.getPC().getData());
            textarea9.setText(sim.getIR().getData());
            clock++;
            textfield2.setText(clock.toString());
            textfield3.setText(sim.getSPSR().getData().substring(0,1));
            textfield4.setText(sim.getSPSR().getData().substring(1,2));
            textfield5.setText(sim.getSPSR().getData().substring(2,3));
            textfield6.setText(sim.getSPSR().getData().substring(3,4));
            textfield9.setText(BinaryFormater.format(Integer.toHexString(Integer.parseInt(sim.getIR().getData(),2)),8));
            sim.getSPSR().setData("0000");
        }
    }

    //Method actionPerformed for button2
    /**
     * Method Reset resets the simulation to the beginning
     * @param evt an action event
     */
    private void reset (ActionEvent evt) {
        if(stepTimer != null) //if swing timer is initialized, stop it
        {stepTimer.stop();}
        sim = new Simulator(file, noisy);
        sim.processHeader(); 
        sim.processMemory();
        textarea11.setText(sim.convertToAssembly(sim.getIR().getData()));
        textarea4.setText(sim.getRegister(0).getData());
        textarea5.setText(sim.getRegister(1).getData());
        textarea6.setText(sim.getRegister(2).getData());
        textarea7.setText(sim.getRegister(3).getData());
        textarea8.setText(sim.getPC().getData());
        textarea9.setText(sim.getIR().getData());
        clock = 0;
        textfield2.setText(clock.toString());
        textfield3.setText(sim.getSPSR().getData().substring(0,1));
        textfield4.setText(sim.getSPSR().getData().substring(1,2));
        textfield5.setText(sim.getSPSR().getData().substring(2,3));
        textfield6.setText(sim.getSPSR().getData().substring(3,4));
        textfield9.setText(BinaryFormater.format(Integer.toHexString(Integer.parseInt(sim.getIR().getData(),2)),8));
    }

    //Method actionPerformed for button3
    /**
     * Method exit exits the program 
     * @param evt and action event
     */
    private void exit (ActionEvent evt) {
        System.exit(0);
    }

    //Method actionPerformed for button4
    /**
     * Method exportToFile exports the current memory and register state to separate .o files
     * @param evt and action event
     */
    private void exportToFile (ActionEvent evt) {
        sim.writeRegisterFile();
        sim.writeNewMemory();
        System.out.println("Check CPU_Emulator directory for files 'newImage.o' and 'Registers.o'");
    }

    //Method actionPerformed for button5
    /**
     * Method processSlow processes the simulation at a slow step speed
     * @param evt and action event
     */
    private void processSlow (ActionEvent evt) {
        resetTimer(); //reset the swing timer

        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    stepInstruction(evt);
                }
            };
        stepTimer = new Timer(2000,al);
        stepTimer.start();
    }

    //Method actionPerformed for button6
    /**
     * Method processMedium processes the simulation at a slow step speed
     * @param evt and action event
     */
    private void processMedium (ActionEvent evt) {
        resetTimer(); //reset the swing timer
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    stepInstruction(evt);
                }
            };
        stepTimer = new Timer(1000,al);
        stepTimer.start();
    }

    //Method actionPerformed for button7
    /**
     * Method processFast processes the simulation at a slow step speed
     * @param evt and action event
     */
    private void processFast (ActionEvent evt) {
        resetTimer(); //reset the swing timer
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent evt){
                    stepInstruction(evt);
                }
            };
        stepTimer = new Timer(500,al);
        stepTimer.start();
    }

    public void resetTimer()
    {
        if(stepTimer != null){
            stepTimer.stop();
        }
    }

    //method for generate menu
    /**
     * Method generateMenu generates the GUI menu interface
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
                    new ProcessorWindow(args[0], Boolean.parseBoolean(args[1]));
                }
            });
    }

}