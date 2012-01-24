package supersample;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;

public class Main implements KeyListener, MouseListener, MouseWheelListener{
  int height, width;
  JFrame controlFrame, fullFrame;
  Point winFrameLocation;
  //StrangeAttractor01 jpanel;
  //StrangeAttractor02 jpanel;
  //StrangeAttractor03 jpanel;
  //StrangeAttractor04 jpanel;
  StrangeController strangeController;
  //SuperSample4 superSample4;
  String snapshotDir;
 
  public Main(){
    getProperties();
    UIManager.LookAndFeelInfo[] lfi = UIManager.getInstalledLookAndFeels();
    for (int i=0;i<lfi.length;i++){
      //System.out.println(lfi[i].getName());
      //System.out.println(lfi[i].getClassName());
    }
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }
    catch(Exception e){
      System.out.println(e.getMessage());
    }
    setupController();
    
    width = 400;
    height = 400;
  }

  public void addListeners(){
    /*
    winFrame.addKeyListener(this);
    winFrame.addMouseListener(this);
    winFrame.addMouseWheelListener(this);
     */
  }
  
  private void getProperties(){
    BufferedReader propFileReader;
    BufferedWriter propFileWriter;
    File propFile;
    String propFileName, propLine, propName, propVal;
    
    propFileName = System.getProperty("user.home");
    propFileName += System.getProperty("file.separator");
    propFileName += "sa.properties";
    
    propFile = new File(propFileName);
    if (propFile.exists()){
      try{
        propFileReader = new BufferedReader(new FileReader(propFile));
        propLine = propFileReader.readLine();
        while (propLine != null){
          propLine = propLine.replace(" ","");
          propName = propLine.substring(0,propLine.indexOf("="));
          propVal = propLine.substring(propLine.indexOf("=")+1);
          switch(propName){
            case "snapshotDir":
              snapshotDir = propVal;
              break;
          }
          propLine = propFileReader.readLine();
        }
        propFileReader.close();
      }
      catch(Exception e){
      }
    }
    else{
      try{
        propFile.createNewFile();
        propFileWriter = new BufferedWriter(new FileWriter(propFile));
        if ((System.getProperty("os.name")).toLowerCase().contains("windows")){
          propFileWriter.write("snapshotDir = c:\\tmp\\sa");
          snapshotDir = "c:\\tmp\\sa";
        }
        propFileWriter.flush();
        propFileWriter.close();
      }
      catch(Exception e){
      }
    }
  }
  
  private void setupController(){
    controlFrame = new JFrame("Control");
    strangeController = new StrangeController(controlFrame);
  }

  public static void main(String[] args){
    Main app = new Main();
    app.addListeners();
  }

  @Override
  public void keyPressed(KeyEvent kE){
  }

  @Override
  public void keyReleased(KeyEvent kE){
  }

  @Override
  public void keyTyped(KeyEvent kE){
  }

  @Override
  public void mouseClicked(MouseEvent mE){
    /*
    if (fullScreen){
      winFrame = new JFrame("SuperSample4");
      winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      fullFrame.getContentPane().removeAll();
      winFrame.setSize(width+hPad,height+vPad);
      winFrame.getContentPane().add(jpanel);
      fullFrame.dispose();
      winFrame.addMouseListener(this);
      winFrame.addMouseWheelListener(this);
      winFrame.setLocation(winFrameLocation);
      winFrame.setVisible(true);
      fullScreen = false;
    }
    else{
      winFrameLocation = winFrame.getLocation();
      fullFrame = new JFrame("SuperSample4");
      winFrame.getContentPane().removeAll();
      fullFrame.getContentPane().add(jpanel);
      winFrame.dispose();
      fullFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      fullFrame.setUndecorated(true);
      fullFrame.addMouseListener(this);
      fullFrame.addMouseWheelListener(this);
      fullFrame.setVisible(true);
      fullScreen = true;
    }
     */
  }

  @Override
  public void mouseEntered(MouseEvent mE){
  }

  @Override
  public void mouseExited(MouseEvent mE){
  }

  @Override
  public void mousePressed(MouseEvent mE){
  }

  @Override
  public void mouseReleased(MouseEvent mE){
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent mWE){
  }
}