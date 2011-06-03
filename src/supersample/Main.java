package supersample;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.*;

public class Main implements KeyListener, MouseListener, MouseWheelListener{
  boolean fullScreen;
  int hPad, vPad, height, width;
  Controller01 controller;
  JFrame controlFrame, fullFrame, winFrame;
  JMenu lfMenu;
  JMenuBar controlMenuBar;
  Point winFrameLocation;
  //StrangeAttractor01 jpanel;
  //StrangeAttractor02 jpanel;
  //StrangeAttractor03 jpanel;
  //StrangeAttractor04 jpanel;
  StrangeAttractor05 jpanel;
  //SuperSample4 superSample4;
 
  public Main(){
    UIManager.LookAndFeelInfo[] lfi = UIManager.getInstalledLookAndFeels();
    for (int i=0;i<lfi.length;i++){
      System.out.println(lfi[i].getName());
      System.out.println(lfi[i].getClassName());
    }
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }
    catch(Exception e){
      System.out.println(e.getMessage());
    }
    controlMenuBar = new JMenuBar();
    
    width = 400;
    height = 400;
    controller = new Controller01();
    controlFrame = new JFrame("Control");
    controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    controlFrame.getContentPane().add(controller);
    controlFrame.add(controller);
    controlFrame.setAlwaysOnTop(true);
    controlFrame.setVisible(true);
    jpanel = new StrangeAttractor05(width,height,5,5,1);
    winFrame = new JFrame("StrangeAttractor");
    winFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    winFrame.getContentPane().add(jpanel);
    winFrame.setVisible(true);
    hPad = winFrame.getInsets().left + winFrame.getInsets().right;
    vPad = winFrame.getInsets().top + winFrame.getInsets().bottom;
    winFrame.setSize(width+hPad,height+vPad);
    fullScreen = false;
  }

  public void addListeners(){
    winFrame.addKeyListener(this);
    winFrame.addMouseListener(this);
    winFrame.addMouseWheelListener(this);
  }

  public static void main(String[] args){
    Main app = new Main();
    app.addListeners();
    //app.superSample4.run();
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