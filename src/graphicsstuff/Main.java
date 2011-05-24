package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Main{
   //Alias1 alias1;
   //Alias2 blob;
   //Blocks1 blocks1;
   //Clock1 clock1;
   //Clock2 clock2;
   DisplayMode dm;
   //Fire1 fire1;
   //Fire2 fire2;
   //Fonts1 fonts1;
   //FractalFlame1 fractalflame1;
   //GameOfLife1 life1;
   //GameOfLife2 life2;
   //Gaussian gaussian;
   //Graph1 graph1;
   //Graph2 graph2;
   //Graph3 graph3;
   //Graph4 graph4;
   //Graph5 graph5;
   //Graphics2D g2;
   GraphicsDevice gd;
   GraphicsEnvironment ge;
   //Lines1 lines1;
   ThreeDee threeDee;
   JFrame frame;
   JWindow window;
   MyJPanel myJPanel;

   public Main(){
      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      gd = ge.getDefaultScreenDevice();
      dm = gd.getDisplayMode();
      //myJPanel = new MyJPanel(dm.getWidth(),dm.getHeight());
      //graph1 = new Graph1(dm.getWidth(),dm.getHeight());
      //graph2 = new Graph2(dm.getWidth(),dm.getHeight());
      //graph3 = new Graph3(dm.getWidth(),dm.getHeight());
      //graph4 = new Graph4(dm.getWidth(),dm.getHeight());
      //graph5 = new Graph5(dm.getWidth(),dm.getHeight());
      //blocks1 = new Blocks1(dm.getWidth(),dm.getHeight());
      //fonts1 = new Fonts1();
      //clock1 = new Clock1(dm.getWidth(),dm.getHeight());
      //clock2 = new Clock2(dm.getWidth(),dm.getHeight());
      //life1 = new GameOfLife1(dm.getWidth(),dm.getHeight());
      //life2 = new GameOfLife2(dm.getWidth(),dm.getHeight());
      //fire1 = new Fire1(400,400);
      //fire2 = new Fire2(400,400);
      //gaussian = new Gaussian(dm.getWidth(),dm.getHeight());
      threeDee = new ThreeDee(dm.getWidth(),dm.getHeight());
      //lines1 = new Lines1(800,800);
      //alias1 = new Alias1(400,400);
      //blob = new Alias2(400,400);
      //fractalflame1 = new FractalFlame1(800,800);
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
      frame.getContentPane().add(threeDee);
      //System.out.println(gd.isFullScreenSupported());
      //gd.setFullScreenWindow(frame);
      frame.setVisible(true);
      //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
   }

   public static void main(String[] args){
      Main app = new Main();
   }
}