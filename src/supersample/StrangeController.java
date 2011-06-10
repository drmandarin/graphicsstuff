package supersample;

import java.awt.event.*;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static supersample.Util.addComp;

public class StrangeController implements ActionListener, FocusListener{
  boolean fullScreen;
  int hPad, vPad, height, width;
  Font boldFont, courFont, normFont;
  JButton renderButton;
  JComboBox prevBox;
  JFrame winFrame;
  JLabel formString, lyaValLabel;
  JTextField codeField, gkHeightField, gkWidthField, iterField, ssfField, winHeightField, winWidthField;
  StrangeAttractor05 jpanel;
  
  public StrangeController(JFrame controlFrame){
    init();
    addPanels(controlFrame);
    populateAttractorPanel(controlFrame);
    populateDisplayPanel(controlFrame);
    populateButtonPanel(controlFrame);
    populateResultPanel(controlFrame);
    controlFrame.setAlwaysOnTop(true);
    controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    controlFrame.setSize(200,200);
    controlFrame.pack();
    controlFrame.setVisible(true);
  }
  
  private void init(){
    width = 400;
    height = 400;
    boldFont = new Font("Tahoma",Font.BOLD,11);
    courFont = new Font("Courier",Font.PLAIN,11);
    normFont = new Font("Tahoma",Font.PLAIN,11);
  }
  
  private void addPanels(JFrame frame){
    JMenu lfMenu;
    JMenuBar menuBar;
    JMenuItem textItem;
    JPanel attrPanel, buttPanel, dispPanel, rsltPanel;
    
    frame.setLayout(new GridBagLayout());
    attrPanel = new JPanel();
    attrPanel.setBorder(createTitledBorder(createEtchedBorder(),"Attractor properties",TitledBorder.LEFT,TitledBorder.TOP,boldFont));
    attrPanel.setName("attrPanel");
    addComp(frame,attrPanel,0,0);
    dispPanel = new JPanel();
    dispPanel.setBorder(createTitledBorder(createEtchedBorder(),"Display properties",TitledBorder.LEFT,TitledBorder.TOP,boldFont));
    dispPanel.setName("dispPanel");
    addComp(frame,dispPanel,1,0);
    buttPanel = new JPanel();
    buttPanel.setName("buttPanel");
    addComp(frame,buttPanel,0,1,2,1);
    rsltPanel = new JPanel();
    rsltPanel.setBorder(createTitledBorder(createEtchedBorder(),"Results",TitledBorder.LEFT,TitledBorder.TOP,boldFont));
    rsltPanel.setName("rsltPanel");
    addComp(frame,rsltPanel,0,2,2,1);
    menuBar = new JMenuBar();
    lfMenu = new JMenu("Look and Feel");
    lfMenu.setFont(normFont);
    textItem = new JMenuItem("text item");
    textItem.setFont(normFont);
    lfMenu.add(textItem);
    menuBar.add(lfMenu);
    frame.setJMenuBar(menuBar);
    System.out.println(attrPanel.getWidth());
  }
  
  private String deriveFormula(String codeString){
    //A: x = a0 + a1x + a2x^2
    //B: x = a0 + a1x + a2x^2 + a3x^3
    //C: x = a0 + a1x + a2x^2 + a3x^3 + a4x^4
    //D: x = a0 + a1x + a2x^2 + a3x^3 + a4x^4 + a5x^5
    //E: x = a0 + a1x + a2x^2 + a3xy + a4y + a5y^2
    //   y = b0 + b1x + b2x^2 + b3xy + b4y + b5y^2
    double[][] coeffs;
    String formula;
    String[] terms;
    
    formula = "<html><i>x</i><sub>n+1</sub> = ";
    coeffs = Util.decodeString(codeString);
    switch(codeString.charAt(0)){
      case 'A': terms = new String[3];
                break;
      case 'B': terms = new String[4];
                break;
      case 'C': terms = new String[5];
                break;
      case 'D': terms = new String[6];
                break;
      case 'E': terms = new String[6];
                break;
      default:  terms = new String[coeffs.length];
                break;
    }
    switch(codeString.charAt(0)){
      case 'E': terms[5] = "<i>y</i><sub>n</sub><sup>2</sup>";
                terms[4] = "<i>y</i><sub>n</sub>";
                terms[3] = "<i>x</i><sub>n</sub><i>y</i><sub>n</sub>";
                terms[2] = "<i>x</i><sub>n</sub><sup>2</sup>";
                terms[1] = "<i>x</i><sub>n</sub>";
                terms[0] = "";
                break;
      case 'D': terms[5] = "<i>x</i><sub>n</sub><sup>5</sup>";
      case 'C': terms[4] = "<i>x</i><sub>n</sub><sup>4</sup>";
      case 'B': terms[3] = "<i>x</i><sub>n</sub><sup>3</sup>";
      case 'A': terms[2] = "<i>x</i><sub>n</sub><sup>2</sup>";
                terms[1] = "<i>x</i><sub>n</sub>";
                terms[0] = "";
                break;
      default:  break;
    }
    
    formula += coeffs[0];
    for (int i = 1;i < coeffs.length;i++){
      if (coeffs[i] < 0)
        formula += " - ";
      else
        formula += " + ";
      formula += abs(coeffs[i]);
      formula += terms[i];
    }
    formula += "</html>";
    
    return formula;
  }
  
  private void populateAttractorPanel(JFrame frame){
    JLabel codeLabel, formLabel, iterLabel, prevLabel;
    JPanel attrPanel;
    String[] prevList;
    
    attrPanel = (JPanel)(frame.getContentPane().getComponents()[0]);
    attrPanel.setLayout(new GridBagLayout());
    prevList = new String[10];
    for (int i = 0;i < 10;i++){
      prevList[i] = String.valueOf(i);
    }
    codeLabel = new JLabel(" Attractor string ");
    codeLabel.setFont(boldFont);
    addComp(attrPanel,codeLabel,0,0,"EAST");
    codeField = new JTextField("DOOYRIL");
    codeField.addFocusListener(this);
    codeField.setColumns(40);
    codeField.setFont(courFont);
    addComp(attrPanel,codeField,1,0,"WEST");
    formLabel = new JLabel(" Attractor formula ");
    formLabel.setFont(boldFont);
    addComp(attrPanel,formLabel,0,1,"EAST");
    formString = new JLabel();
    formString.setFont(courFont);
    formString.setText(deriveFormula(codeField.getText()));
    addComp(attrPanel,formString,1,1,"WEST");
    prevLabel = new JLabel(" Map to previous iteration ");
    prevLabel.setFont(boldFont);
    addComp(attrPanel,prevLabel,0,2,"EAST");
    prevBox = new JComboBox(prevList);
    prevBox.setFont(normFont);
    addComp(attrPanel,prevBox,1,2,"WEST");
    iterLabel = new JLabel(" Number of iterations ");
    iterLabel.setFont(boldFont);
    addComp(attrPanel,iterLabel,0,3,"EAST");
    iterField = new JTextField("10000");
    iterField.setColumns(10);
    iterField.setFont(courFont);
    addComp(attrPanel,iterField,1,3,"WEST");
  }
  
  private void populateButtonPanel(JFrame frame){
    JPanel buttPanel;
    
    buttPanel = (JPanel)(frame.getContentPane().getComponents()[2]);
    renderButton = new JButton("Render ...");
    renderButton.addActionListener(this);
    renderButton.setFont(boldFont);
    addComp(buttPanel,renderButton,0,0,"CENTER");
  }
  
  private void populateDisplayPanel(JFrame frame){
    JLabel gkLabel, ssfLabel, winLabel, xLabel;
    JPanel dispPanel;
    
    dispPanel = (JPanel)(frame.getContentPane().getComponents()[1]);
    dispPanel.setLayout(new GridBagLayout());
    winLabel = new JLabel(" Window size ");
    winLabel.setFont(boldFont);
    addComp(dispPanel,winLabel,0,0,"EAST");
    winWidthField = new JTextField(String.valueOf(width));
    winWidthField.setColumns(5);
    winWidthField.setFont(courFont);
    addComp(dispPanel,winWidthField,1,0,"WEST");
    xLabel = new JLabel("x");
    xLabel.setFont(courFont);
    addComp(dispPanel,xLabel,2,0,"CENTER");
    winHeightField = new JTextField(String.valueOf(height));
    winHeightField.setColumns(5);
    winHeightField.setFont(courFont);
    addComp(dispPanel,winHeightField,3,0,"EAST");
    gkLabel = new JLabel(" Gauss kernel size ");
    gkLabel.setFont(boldFont);
    addComp(dispPanel,gkLabel,0,1,"EAST");
    gkWidthField = new JTextField("5");
    gkWidthField.setColumns(2);
    gkWidthField.setFont(courFont);
    addComp(dispPanel,gkWidthField,1,1,"WEST");
    xLabel = new JLabel("x");
    xLabel.setFont(courFont);
    addComp(dispPanel,xLabel,2,1,"CENTER");
    gkHeightField = new JTextField("5");
    gkHeightField.setColumns(2);
    gkHeightField.setFont(courFont);
    addComp(dispPanel,gkHeightField,3,1,"WEST");
    ssfLabel = new JLabel(" Supersample factor ");
    ssfLabel.setFont(boldFont);
    addComp(dispPanel,ssfLabel,0,2,"EAST");
    ssfField = new JTextField("1");
    ssfField.setColumns(2);
    ssfField.setFont(courFont);
    addComp(dispPanel,ssfField,1,2,"WEST");
  }
  
  private void populateResultPanel(JFrame frame){
    JLabel lyaLabel, maxLabel, minLabel;
    JPanel rsltPanel;
    
    rsltPanel = (JPanel)(frame.getContentPane().getComponents()[3]);
    rsltPanel.setLayout(new GridBagLayout());
    maxLabel = new JLabel(" Maximum ");
    maxLabel.setFont(boldFont);
    addComp(rsltPanel,maxLabel,0,0,"EAST");
    minLabel = new JLabel(" Minimum ");
    minLabel.setFont(boldFont);
    addComp(rsltPanel,minLabel,0,1,"EAST");
    lyaLabel = new JLabel(" Lyapunov ");
    lyaLabel.setFont(boldFont);
    addComp(rsltPanel,lyaLabel,0,2,"EAST");
    lyaValLabel = new JLabel("                    ");
    lyaValLabel.setFont(normFont);
    addComp(rsltPanel,lyaValLabel,1,2,"WEST");
  }
  
  @Override
  public void actionPerformed(ActionEvent e){
    if (e.getSource() == renderButton){
      int gkHeight, gkWidth, ssFactor, winHeight, winWidth;
      
      gkHeight = parseInt(gkHeightField.getText());
      gkWidth = parseInt(gkWidthField.getText());
      ssFactor = parseInt(ssfField.getText());
      winHeight = parseInt(winHeightField.getText());
      winWidth = parseInt(winWidthField.getText());
      jpanel = new StrangeAttractor05(winWidth,winHeight,gkWidth,gkHeight,ssFactor);
      winFrame = new JFrame("StrangeAttractor");
      winFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      winFrame.getContentPane().add(jpanel);
      winFrame.setVisible(true);
      hPad = winFrame.getInsets().left + winFrame.getInsets().right;
      vPad = winFrame.getInsets().top + winFrame.getInsets().bottom;
      winFrame.setSize(winWidth+hPad,winHeight+vPad);
      fullScreen = false;
      jpanel.setCode(codeField.getText());
      jpanel.setIterations(Integer.parseInt(iterField.getText()));
      jpanel.setPrev(prevBox.getSelectedIndex());
      jpanel.run();
      lyaValLabel.setText(String.valueOf(jpanel.getLyapunov()));
    }
  }
  
  @Override
  public void focusGained(FocusEvent e){
  }
  
  @Override
  public void focusLost(FocusEvent e){
    if (e.getSource() == codeField){
      formString.setText(deriveFormula(codeField.getText()));
    }
  }
}