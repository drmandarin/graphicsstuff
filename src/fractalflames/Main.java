package fractalflames;

import java.awt.*;
import javax.swing.*;

public class Main{
   DisplayMode dm;
   //FractalFlame1 fractalflame1;
   FractalFlame2 fractalflame2;
   GraphicsDevice gd;
   GraphicsEnvironment ge;
   JFrame frame;
   JWindow window;
   
   public Main(){
      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      gd = ge.getDefaultScreenDevice();
      dm = gd.getDisplayMode();
      //fractalflame1 = new FractalFlame1(800,800);
      fractalflame2 = new FractalFlame2(800,800);
      /*
      window = new JWindow();
      window.setSize(dm.getWidth(),dm.getHeight());
      window.getContentPane().add(myJPanel);
      gd.setFullScreenWindow(window);
      window.setVisible(true);
       */
      frame = new JFrame("main");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //frame.setSize(dm.getWidth(),dm.getHeight());
      frame.setSize(808,828);
      //frame.setSize(408,428);
      //frame.getContentPane().add(myJPanel);
      frame.getContentPane().add(fractalflame2);
      //System.out.println(gd.isFullScreenSupported());
      //gd.setFullScreenWindow(frame);
      frame.setVisible(true);
      //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
   }

   public static void main(String[] args){
      Main app = new Main();
   }
}