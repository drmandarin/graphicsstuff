package graphicsstuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;

class GameOfLife2 extends JPanel implements Runnable{
   boolean runTest;
   int black, green, height, width, scaleFactor, scaledHeight, scaledWidth;
   int[][] newUniverse, oldUniverse;
   BufferedImage bi;
   Thread th;
   
   public GameOfLife2(int w, int h){
      height = h;
      width = w;
      scaleFactor = 4;
      scaledHeight = height/scaleFactor;
      scaledWidth = width/scaleFactor;
      newUniverse = new int[scaledHeight][scaledWidth];
      oldUniverse = new int[scaledHeight][scaledWidth];
      runTest = true;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      initValues();
      th = new Thread(this);
      th.start();
   }
   
   private void cloneUniv(){
      
      //System.out.println("cloning universe...");
      for (int i=0;i<scaledHeight;i++){
         for (int j=0;j<scaledWidth;j++){
            oldUniverse[i][j] = newUniverse[i][j];
         }
      }
   }
   
   private int examinePoint(int y, int x){
      int ptVal, numNghbrs, x_pos, y_pos;
      
      numNghbrs = 0;
      for (int i=-1;i<2;i++){
         if (i+y < 0){
            y_pos = scaledHeight + i;
         }
         else if (i+y >= scaledHeight){
            y_pos = (i+y) - scaledHeight;
         }
         else{
            y_pos = i+y;
         }
         for (int j=-1;j<2;j++){
            if (j+x < 0){
               x_pos = scaledWidth + j;
            }
            else if (j+x >= scaledWidth){
               x_pos = (j+x) - scaledWidth;
            }
            else{
               x_pos = j+x;
            }
            numNghbrs += oldUniverse[y_pos][x_pos];
         }
      }
      numNghbrs -= oldUniverse[y][x];
      
      if ((oldUniverse[y][x] == 1) && ((numNghbrs < 2) || (numNghbrs > 3))){
         ptVal = 0;
      }
      else if ((oldUniverse[y][x] == 0) && (numNghbrs ==3)){
         ptVal = 1;
      }
      else{
         ptVal = oldUniverse[y][x];
      }
      
      return ptVal;
   }
   
   private int examinePoint(Point pt){
      int ptVal, numNghbrs;
      
      //System.out.println("examining point... "+pt.y+";"+pt.x);
      numNghbrs = 0;
      for (int i=-1;i<2;i++){
         for (int j=-1;j<2;j++){
            numNghbrs += oldUniverse[pt.y + j][pt.x + i];
         }
      }
      
      if ((oldUniverse[pt.y][pt.x] == 1) && ((numNghbrs < 2) || (numNghbrs > 3))){
         ptVal = 0;
      }
      else if ((oldUniverse[pt.y][pt.x] == 0) && (numNghbrs == 3)){
         ptVal = 1;
      }
      else{
         ptVal = oldUniverse[pt.y][pt.x];
      }
      
      return ptVal;
   }
   
   private void initValues(){
      Random rndm;
      
      black = (new Color(0,0,0)).getRGB();
      green = (new Color(0,255,0)).getRGB();
      rndm = new Random();
      for (int i=0;i<scaledHeight;i++){
         for (int j=0;j<scaledWidth;j++){
            if (rndm.nextFloat() < 0.35){
               newUniverse[i][j] = 1;
            }
         }
      }
      scaleImage();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      try{
        //Thread.sleep(50);
      }
      catch(Exception e){
          System.out.println("Exception in paint()");
          System.out.println(e.getMessage());
      }
      g2.drawImage(bi,null,0,0);
   }
   
   public void run(){
      System.out.println("running...");
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      /*
      Font[] fonts = ge.getAllFonts();
      for (int i=0;i<fonts.length;i++){
         System.out.println(fonts[i].getFontName());
      }
       */
      
      while(runTest){
         repaint();
         tick();
      }
   }
   
   private void scaleImage(){
      int cellVal, x1, x2, y1, y2, x_pos, y_pos;
      //loop through universe array
      for (int i=0;i<scaledHeight;i++){
         y1 = (i * scaleFactor);
         y2 = y1 + scaleFactor;
         for (int j=0;j<scaledWidth;j++){
            x1 = (j * scaleFactor);
            x2 = x1 + scaleFactor;
            cellVal = newUniverse[i][j];
            //loop through pixels to make block
            for (int k=x1;k<x2;k++){
               if (k >= width){
                  x_pos = k - width;
               }
               else{
                  x_pos = k;
               }
               for (int m=y1;m<y2;m++){
                  if (m >= height){
                     y_pos = m - height;
                  }
                  else{
                     y_pos = m;
                  }
                  
                  if (cellVal == 0){
                     bi.setRGB(x_pos,y_pos,black);
                  }
                  else{
                     bi.setRGB(x_pos,y_pos,green);
                  }
               }
            }
         }
      }
   }
   
   private void tick(){
      int newVal;
      
      //System.out.println("ticking...");
      cloneUniv();
      for (int i=0;i<scaledHeight;i++){
         for (int j=0;j<scaledWidth;j++){
            newUniverse[i][j] = examinePoint(i,j);
         }
      }
      scaleImage();
   }
}