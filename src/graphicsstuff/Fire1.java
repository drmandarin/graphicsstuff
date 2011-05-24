package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.Random;
import javax.swing.JPanel;

public class Fire1 extends JPanel implements Runnable{
   boolean runTest;
   int scale, height, width, BLACK, RED;
   int[][] points;
   BufferedImage bi;
   Random rndm;
   Thread thread;

   public Fire1(int w, int h){
      runTest = true;
      height = h;
      width = w;
      scale = 2;
      points = new int[width/scale][height/scale];
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      rndm = new Random();
      BLACK = (new Color(0,0,0)).getRGB();
      RED = (new Color(255,0,0)).getRGB();
      thread = new Thread(this);
      thread.start();
   }
   
   private void drawSquare(int x,int y, int rgb){
      for (int i=0;i<scale;i++){
         for (int j=0;j<scale;j++){
            bi.setRGB(x+i,y+j,rgb);
         }
      }
   }
   
   private int examinePoint(int i,int j){
      double factor;
      int red, green;
      Color tmpColor;
      
      red = 0;
      green = 0;
      factor = 4.3;
      
      tmpColor = new Color(points[i][j+1]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      tmpColor = new Color(points[i][j+2]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      tmpColor = new Color(points[(i+1)%(width-1)][j+1]);
      red += tmpColor.getRed();
      green += tmpColor.getGreen();
      if (i == 0){
         tmpColor = new Color(points[width-1][j+1]);
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
      tmpColor = new Color((int)red,(int)green,0);
      
      return tmpColor.getRGB();
   }
   
   public void paint(Graphics g){
      Graphics2D g2 = (Graphics2D)g;
      
      g2.drawImage(bi,null,0,0);
      
      /*
      try{
         java.io.File outFile = new java.io.File("c:\\progs\\java\\test.png");
         javax.imageio.ImageIO.write(bi,"png",outFile);
      }
      catch(Exception e){
         
      }
       */
      
      g2.dispose();
   }
   
   public void run(){
      while(runTest){
         try{
            Thread.sleep(50);
         }
         catch(Exception e){
            System.out.println("Exception in run()");
            System.out.println(e.getMessage());
         }
         tick();
         repaint();
      }
   }
   
   private void scaleImage(){
      for (int i=0;i<width/scale;i++){
         for (int j=0;j<height/scale;j++){
            drawSquare(i,j,points[i][j]);
         }
      }
   }
   
   public void tick(){
      Color tmpColor, pxlColor;
      
      for (int i=0;i<width/scale;i++){
         for (int j=(height/scale)-1;j>(height/scale)-3;j--){
            pxlColor = new Color(255,rndm.nextInt(256),0);
            
            //This creates a yellow-gold flame.
            //int k;
            //k = rndm.nextInt(256);
            //pxlColor = new Color(k,k,255);
            
            //This creates a very deep red flame.
            //pxlColor = new Color(rndm.nextInt(256),0,255);
            points[i][j] = pxlColor.getRGB();
         }
      }
      
      //place embers
      /*
      for (int i=1;i<8;i++){
         for (int j=i*50;j<(i*50)+5;j++){
            for (int k=height-1;k<height-6;k--){
               points[j][k] = BLACK;
            }
         }
      }
       */

      for (int i=0;i<width;i++){
         for (int j=height/2;j<height-2;j++){
            points[i][j] = examinePoint(i,j);
            //bi.setRGB(i,j,points[i][j]);
         }
      }
      
      scaleImage();
      
      //???? ??????
      
      
      for (int i=0;i<width;i++){
         for (int j=0;j<height;j++){
            tmpColor = new Color(i^j);
            pxlColor = new Color(tmpColor.getBlue(),tmpColor.getBlue(),tmpColor.getBlue());
            bi.setRGB(i,j,pxlColor.getRGB());
            //bi.setRGB(i,j,i^j);
         }
      }
       /*
      for (int k=0;k<255;k++){
         for (int i=0;i<255;i++){
            pxlColor = new Color(i,k,0);
            for (int j=0;j<3;j++){
               bi.setRGB((i*3)+j+10,20+(k*3),pxlColor.getRGB());
               bi.setRGB((i*3)+j+10,21+(k*3),pxlColor.getRGB());
               bi.setRGB((i*3)+j+10,22+(k*3),pxlColor.getRGB());
            }
         }
      }
       */
   }
}