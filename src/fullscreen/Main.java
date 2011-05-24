package fullscreen;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

public class Main{

  public Main(){
    GraphicsDevice gd;
    GraphicsEnvironment ge;
    JFrame jframe;

    jframe = new JFrame("main");
    ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    gd = ge.getDefaultScreenDevice();
    System.out.print(gd.getDisplayMode().getWidth());
    System.out.print("x");
    System.out.print(gd.getDisplayMode().getHeight());
    System.out.print("x");
    System.out.print(gd.getDisplayMode().getBitDepth());
    System.out.print("@");
    System.out.println(gd.getDisplayMode().getRefreshRate());
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setUndecorated(true);
    jframe.setVisible(true);
    gd.setFullScreenWindow(jframe);
  }

  public static void main(String[] args){
    Main app = new Main();
  }
}