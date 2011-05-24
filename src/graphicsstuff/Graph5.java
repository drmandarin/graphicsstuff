package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.Random;

class Graph5 extends JPanel implements Runnable{
   boolean runTest;
   int height, width;
   BufferedImage bi;
   Color tmpClr;
   ColourPoint curr;
   Random rndm;
   
   public Graph5(int w, int h){
      super();
      rndm = new Random();
      height = h;
      width = w;
      runTest = true;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      tmpClr = new Color(rndm.nextInt(255),rndm.nextInt(255),rndm.nextInt(255));
      curr = new ColourPoint(rndm.nextInt(width),rndm.nextInt(height),tmpClr.getRGB());
      Thread th = new Thread(this);
      th.start();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   public void run(){
      while(runTest){
         tick();
         repaint();
      }
   }
   
   public void tick(){
      int a, b, g, r, x, y, rgb, dir;
      Color tmpClr;
      
      //fade rest of screen first
      //for (int i=0;i<100;i++){
         for (int j=0;j<100;j++){
            tmpClr = new Color(bi.getRGB(j,j));
            r = tmpClr.getRed();
            g = tmpClr.getGreen();
            b = tmpClr.getBlue();
            if (r > 0){
               r = r-1;
            }
            if (g > 0){
               g = g-1;
            }
            if (b > 0){
               b = b-1;
            }
            bi.setRGB(j,j,(new Color(r,g,b)).getRGB());
         }
      //}
      //end fading
      
      dir = 0;
      x = curr.getX();
      y = curr.getY();
      rgb = curr.getRGB();
      b = (new Color(rgb)).getBlue();
      g = (new Color(rgb)).getGreen();
      r = (new Color(rgb)).getRed();
      //paint block
      bi.setRGB(x,y,rgb);
      if (x > 0){
         bi.setRGB(x-1,y,rgb);
         if (y > 0){
            bi.setRGB(x-1,y-1,rgb);
            bi.setRGB(x,y-1,rgb);
         }
         if (y < (height-1)){
            bi.setRGB(x,y+1,rgb);
            bi.setRGB(x-1,y+1,rgb);
         }
      }
      if (x < (width-1)){
         bi.setRGB(x+1,y,rgb);
         if (y > 0){
            bi.setRGB(x+1,y-1,rgb);
         }
         if (y < (height-1)){
            bi.setRGB(x+1,y+1,rgb);
         }
      }
      //move point
      a = rndm.nextInt(3);
      if (a == 0){
         if (x > 0){
            dir = -1;
         }
         else{
            dir = 0;
         }
      }
      else if (a == 1){
         dir = 0;
      }
      else{
         if (x < (width-1)){
            dir = 1;
         }
         else{
            dir = 0;
         }
      }
      curr.setX(x + dir);
      a = rndm.nextInt(3);
      if (a == 0){
         if (y > 0){
            dir = -1;
         }
         else{
            dir = 0;
         }
      }
      else if (a == 1){
         dir = 0;
      }
      else{
         if (y < (height-1)){
            dir = 1;
         }
         else{
            dir = 0;
         }
      }
      curr.setY(y + dir);
      a = rndm.nextInt(3);
      if (a == 0){
         if (r == 255){
            curr.setRGB((new Color(0,g,b)).getRGB());
         }
         else{
            curr.setRGB((new Color(r+1,g,b)).getRGB());
         }
      }
      else if (a == 1){
         if (g == 255){
            curr.setRGB((new Color(r,0,b)).getRGB());
         }
         else{
            curr.setRGB((new Color(r,g+1,b)).getRGB());
         }
      }
      else{
         if (b == 255){
            curr.setRGB((new Color(r,g,0)).getRGB());
         }
         else{
            curr.setRGB((new Color(r,g,b+1)).getRGB());
         }
      }
   }
}