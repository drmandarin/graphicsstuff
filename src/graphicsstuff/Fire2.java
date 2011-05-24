package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.Random;
import javax.swing.JPanel;

public class Fire2 extends JPanel implements Runnable{
   boolean runTest;
   int pxHeight, pxWidth, scale, scHeight, scWidth, BLACK, RED, YELLOW;
   int[][] points;
   BufferedImage bi;
   Random rndm;
   Thread thread;

   public Fire2(int w, int h){
      runTest = true;
      pxHeight = h;
      pxWidth = w;
      scale = 2;
      scHeight = pxHeight/scale;
      scWidth = pxWidth/scale;
      points = new int[scWidth][scHeight];
      bi = new BufferedImage(pxWidth,pxHeight,BufferedImage.TYPE_INT_RGB);
      rndm = new Random();
      BLACK = (new Color(0,0,0)).getRGB();
      RED = (new Color(255,0,0)).getRGB();
      YELLOW = (new Color(255,255,0)).getRGB();
      thread = new Thread(this);
      thread.start();
   }

   private void drawSquare(int x, int y, int rgb){
      for (int i=0;i<scale;i++){
         for (int j=0;j<scale;j++){
            bi.setRGB((x*scale)+i,(y*scale)+j,rgb);
         }
      }
   }

   private int examinePoint(int i,int j){
      double factor;
      int red, green;
      Color tmpColor;

      red = 0;
      green = 0;
      factor = 4.0;

      tmpColor = new Color(points[i][j+1]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      tmpColor = new Color(points[i][j+2]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      tmpColor = new Color(points[(i+1)%(scWidth-1)][j+1]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      if (i == 0){
         tmpColor = new Color(points[scWidth-1][j+1]);
         red += tmpColor.getRed();
         green += tmpColor.getGreen();
      }
      else{
         tmpColor = new Color(points[i-1][j+1]);
         red += tmpColor.getRed();
         green += tmpColor.getGreen();
      }

      red = (int)((double)red/factor);
      green = (int)((double)green/factor);
      tmpColor = new Color(red,green,0);

      return tmpColor.getRGB();
   }

   public void paint(Graphics g){
      Graphics2D g2 = (Graphics2D)g;

      try{
         java.io.File outFile1 = new java.io.File("c:\\progs\\java\\test1.png");
         javax.imageio.ImageIO.write(bi,"png",outFile1);
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }

      g2.drawImage(bi,null,0,0);
      g2.dispose();
   }

   public void run(){
      while (runTest){
         tick();
         repaint();
         try{
            Thread.sleep(50);
         }
         catch(Exception e){
            System.err.println("Exception in run()");
            System.err.println(e.getMessage());
         }
      }
   }

   private void scaleImage(){
      for (int i=0;i<scWidth;i++){
         for (int j=0;j<scHeight;j++){
            drawSquare(i,j,points[i][j]);
         }
      }
   }

   public void tick(){
      Color pxlColor, tmpColor;

      //Set up randomised base of flame
      /*
      for (int i=0;i<scWidth;i++){
         for (int j=(scHeight-1);j>(scHeight-3);j--){
            pxlColor = new Color(255,rndm.nextInt(256),0);
            points[i][j] = pxlColor.getRGB();
         }
      }
       */

      for (int i=50;i<56;i++){
         for (int j=(scHeight-1);j>(scHeight-3);j--){
            points[i][j] = RED;
         }
      }

      for (int i=52;i<54;i++){
         for (int j=(scHeight-1);j>(scHeight-3);j--){
            points[i][j] = YELLOW;
         }
      }

      //Calculate flames
      for (int i=0;i<scWidth;i++){
         for (int j=scHeight/2;j<scHeight-2;j++){
            points[i][j] = examinePoint(i,j);
         }
      }
      scaleImage();
   }
}
