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
import static supersample.Util.decodeString;
import static supersample.Util.fmt;
import static supersample.Util.getHTML;

public class StrangeController implements ActionListener, FocusListener{
  boolean fullScreen;
  int hPad, vPad, height, width;
  Font boldFont, courFont, normFont;
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
  }
  
  private String deriveFormula(String codeString){
    //A: x = a00 + a01x + a02x^2
    //B: x = a00 + a01x + a02x^2 + a03x^3
    //C: x = a00 + a01x + a02x^2 + a03x^3 + a04x^4
    //D: x = a00 + a01x + a02x^2 + a03x^3 + a04x^4 + a05x^5
    //E: x = a00 + a01x + a02x^2 + a03xy + a04y + a05y^2
    //   y = a06 + a07x + a08x^2 + a09xy + a10y + a11y^2
    //F: x = a0 + a1x + a2x^2 + a3x^3
    //     + a4x^2y + a5xy + a6xy^2
    //     + a7y + a8y^2 + a9y^3
    //   y = a10 + a11x + a12x^2 + a13x^3
    //     + a14x^2y + a15xy + a16xy^2
    //     + a17y + a18y^2 + a19y^3
    //G: x = a0 + a1x + a2x^2 + a3x^3 + a4x^4
    //     + a5x^3y + a6x^2y + a7x^2y^2 + a8xy + a9xy^2 + a10xy^3
    //     + a11y + a12y^2 + a13y^3 + a14y^4
    //   y = a15 + a16x + a17x^2 + a18x^3 + a19x^4
    //     + a20x^3y + a21x^2y + a22x^2y^2 + a23xy + a24xy^2 + a25xy^3
    //     + a26y + a27y^2 + a28y^3 + a29y^4
    //H: x = a0 + a1x + a2x^2 + a3x^3 + a4x^4 + a5x^5 + a6x^4y + a7x^3y
    //     + a8x^3y^2 + a9x^2y + a10x^2y^2 + a11x^2y^3 + a12xy + a13xy^2 
    //     + a14xy^3  + a15xy^4 + a16y + a17y^2 + a18y^3 + a19y^4 + a20y^5
    //   y = a21 + a22x + a23x^2 + a24x^3 + a25x^4 + a26x^5 + a27x^4y + a28x^3y
    //     + a29x^3y^2 + a30x^2y + a31x^2y^2 + a32x^2y^3 + a33xy + a34xy^2 
    //     + a35xy^3  + a36xy^4 + a37y + a38y^2 + a39y^3 + a40y^4 + a41y^5
    //I: x = a0 + a1x + a2x^2 + a3xy + a4xz + a5y + a6y^2 + a7yz + a8z + a9z^2
    //   y = a10 + a11x + a12x^2 + a13xy + a14xz + a15y + a16y^2 + a17yz + a18z + a19z^2
    //   z = a20 + a21x + a22x^2 + a23xy + a24xz + a25y + a26y^2 + a27yz + a28z + a29z^2
    double[] coeffs;
    String formula;
    String[] terms;
    
    formula = "<html><i>x</i><sub>n+1</sub> = ";
    coeffs = decodeString(codeString);
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
      case 'F': terms = new String[10];
                break;
      case 'G': terms = new String[15];
                break;
      case 'H': terms = new String[21];
                break;
      case 'I': terms = new String[10];
                break;
      default:  terms = new String[coeffs.length];
                break;
    }
    switch(codeString.charAt(0)){
      case 'I':
        terms[0] = "";
        terms[1] = getHTML('x');
        terms[2] = getHTML('x',2);
        terms[3] = getHTML('x','y');
        terms[4] = getHTML('x','z');
        terms[5] = getHTML('y');
        terms[6] = getHTML('y',2);
        terms[7] = getHTML('y','z');
        terms[8] = getHTML('z');
        terms[9] = getHTML('z',2);
        break;
      case 'H':
        terms[0] = "";
        terms[1] = getHTML('x');
        terms[2] = getHTML('x',2);
        terms[3] = getHTML('x',3);
        terms[4] = getHTML('x',4);
        terms[5] = getHTML('x',5);
        terms[6] = getHTML('x',4,'y');
        terms[7] = getHTML('x',3,'y');
        terms[8] = getHTML('x',3,'y',2);
        terms[9] = getHTML('x',2,'y');
        terms[10] = getHTML('x',2,'y',2);
        terms[11] = getHTML('x',2,'y',3);
        terms[12] = getHTML('x','y');
        terms[13] = getHTML('x','y',2);
        terms[14] = getHTML('x','y',3);
        terms[15] = getHTML('x','y',4);
        terms[16] = getHTML('y');
        terms[17] = getHTML('y',2);
        terms[18] = getHTML('y',3);
        terms[19] = getHTML('y',4);
        terms[20] = getHTML('y',5);
      case 'G':
        terms[0] = "";
        terms[1] = getHTML('x');
        terms[2] = getHTML('x',2);
        terms[3] = getHTML('x',3);
        terms[4] = getHTML('x',4);
        terms[5] = getHTML('x',3,'y');
        terms[6] = getHTML('x',2,'y');
        terms[7] = getHTML('x',2,'y',2);
        terms[8] = getHTML('x','y');
        terms[9] = getHTML('x','y',2);
        terms[10] = getHTML('x','y',3);
        terms[11] = getHTML('y');
        terms[12] = getHTML('y',2);
        terms[13] = getHTML('y',3);
        terms[14] = getHTML('y',4);
        break;
      case 'F':
        terms[0] = "";
        terms[1] = "<i>x</i><sub>n</sub>";
        terms[2] = "<i>x</i><sub>n</sub><sup>2</sup>";
        terms[3] = "<i>x</i><sub>n</sub><sup>3</sup>";
        terms[4] = "<i>x</i><sub>n</sub><sup>2</sup><i>y</i><sub>n</sub>";
        terms[5] = "<i>x</i><sub>n</sub><i>y</i><sub>n</sub>";
        terms[6] = "<i>x</i><sub>n</sub><i>y</i><sub>n</sub><sup>2</sup>";
        terms[7] = "<i>y</i><sub>n</sub>";
        terms[8] = "<i>y</i><sub>n</sub><sup>2</sup>";
        terms[9] = "<i>y</i><sub>n</sub><sup>3</sup>";
        break;
      case 'E':
        terms[0] = "";
        terms[1] = "<i>x</i><sub>n</sub>";
        terms[2] = "<i>x</i><sub>n</sub><sup>2</sup>";
        terms[3] = "<i>x</i><sub>n</sub><i>y</i><sub>n</sub>";
        terms[4] = "<i>y</i><sub>n</sub>";
        terms[5] = "<i>y</i><sub>n</sub><sup>2</sup>";
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
    switch(codeString.charAt(0)){
      case 'A': ;
      case 'B': ;
      case 'C': ;
      case 'D': 
        for (int i = 1;i < coeffs.length;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i];
        }
        formula += "</html>";
        break;
      case 'E': ;
      case 'F': ;
      case 'G': 
        for (int i = 1;i < coeffs.length/2;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i];
        }
        formula += "<br>";
        formula += "<html><i>y</i><sub>n+1</sub> = ";
        formula += coeffs[coeffs.length/2];
        for (int i = coeffs.length/2 + 1;i < coeffs.length;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i % terms.length];
        }
        formula += "</html>";
        break;
      case 'H':
        for (int i = 1;i < coeffs.length/2;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i];
          if (i == 7 || i == 13)
            formula += "    <br>";
        }
        formula += "<br>";
        formula += "<html><i>y</i><sub>n+1</sub> = ";
        formula += coeffs[coeffs.length/2];
        for (int i = coeffs.length/2 + 1;i < coeffs.length;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i % terms.length];
          if (i == 28 || i == 34)
            formula += "<br>";
        }
        formula += "</html>";
        break;
      case 'I':
        for (int i = 1;i < coeffs.length/3;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i];
        }
        formula += "<br>";
        formula += "<html><i>y</i><sub>n+1</sub> = ";
        formula += coeffs[coeffs.length/3];
        for (int i = coeffs.length/3 + 1;i < 2*coeffs.length/3;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i % terms.length];
        }
        formula += "<br>";
        formula += "<html><i>z</i><sub>n+1</sub> = ";
        formula += coeffs[2*coeffs.length/3];
        for (int i = 2*coeffs.length/3 + 1;i < coeffs.length;i++){
          if (coeffs[i] < 0)
            formula += " - ";
          else
            formula += " + ";
          formula += abs(coeffs[i]);
          formula += terms[i % terms.length];
        }
        formula += "</html>";
        break;
    }
    
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
    codeField = new JTextField("IOHGWFIHJPSGWTOJBXWJKPBLKFRUKKQ");
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
    JButton renderButton, searchButton;
    JPanel buttPanel;
    
    buttPanel = (JPanel)(frame.getContentPane().getComponents()[2]);
    renderButton = new JButton("Render ...");
    renderButton.addActionListener(this);
    renderButton.setFont(boldFont);
    renderButton.setName("renderButton");
    addComp(buttPanel,renderButton,0,0,"CENTER");
    searchButton = new JButton("Search ...");
    searchButton.addActionListener(this);
    searchButton.setFont(boldFont);
    searchButton.setName("searchButton");
    addComp(buttPanel,searchButton,1,0,"CENTER");
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
    String sourceName;
    Thread thread;
    
    sourceName = ((JButton)e.getSource()).getName();
    if (sourceName.equalsIgnoreCase("renderButton")){
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
      thread = new Thread(jpanel);
      thread.start();
      while (jpanel.rendering){
        try{
          Thread.sleep(500);
        }
        catch(Exception ex){
          System.out.println("Exception in actionPerformed: " + ex.getMessage());
        }
      }
      lyaValLabel.setText(fmt(jpanel.getLyapunov()[0]));
    }
    else if (sourceName.equalsIgnoreCase("searchButton")){
      int gkHeight, gkWidth, ssFactor, winHeight, winWidth;
      
      gkHeight = parseInt(gkHeightField.getText());
      gkWidth = parseInt(gkWidthField.getText());
      ssFactor = parseInt(ssfField.getText());
      winHeight = parseInt(winHeightField.getText());
      winWidth = parseInt(winWidthField.getText());
      jpanel = new StrangeAttractor05(winWidth,winHeight,gkWidth,gkHeight,ssFactor);
      jpanel.setCode(codeField.getText());
      jpanel.setIterations(Integer.parseInt(iterField.getText()));
      jpanel.findAttractors(codeField.getText());
      jpanel = null;
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