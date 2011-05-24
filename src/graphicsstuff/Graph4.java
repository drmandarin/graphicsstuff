package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.*;
import javax.swing.*;

public class Graph4 extends JPanel implements Runnable{
   boolean runTest;
   int BLACK, GREEN, RED, WHITE;
   int height, tickCounter, width;
   BufferedImage bi;
   LeaderDot leader;
   Raster blankRaster;
   Vector points;
   
   public Graph4(int w, int h){
      height = h;
      width = w;
      tickCounter = 0;
      leader = new LeaderDot();
      BLACK = (new Color(0,0,0)).getRGB();
      GREEN = (new Color(0,255,0)).getRGB();
      RED = (new Color(255,0,0)).getRGB();
      WHITE = (new Color(255,255,255)).getRGB();
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      bi.setAccelerationPriority(1);
      blankRaster = bi.getData();
      points = new Vector(width,width);
      genPoints();
      runTest = true;
      Thread th = new Thread(this);
      th.start();
   }
   
   public void genPoints(){
      double x_val, y_val;
      int y_pos;
      
      for (int i=0;i<width;i++){
         points.addElement(new ColourPoint(i,height/2,RED));
      }
      for (int i=0;i<height;i++){
         points.addElement(new ColourPoint(width/2,i,RED));
      }
      for (int i=0;i<width;i++){
         x_val = (double)(i - (width/2))/144;
         y_val = Math.sin(x_val);
         y_pos = (int)(y_val*216) + (height/2);
         points.addElement(new ColourPoint(i,y_pos,GREEN));
      }
      for (int i=0;i<width;i++){
         x_val = (double)(i - (width/2))/144;
         y_val = Math.cos(x_val);
         y_pos = (int)(y_val*216) + (height/2);
         points.addElement(new ColourPoint(i,y_pos,GREEN));
      }
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   public void run(){
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      while(runTest){
         tick();
         repaint();
         try{
            Thread.sleep(1);
         }
         catch(Exception e){
            System.out.println(e.getMessage());
         }
      }
   }
   
   private void tick(){
      int prev_x, prev_y, x, y;
      ColourPoint curr, prev;
      
      //bi.setData(blankRaster);
      System.out.println(tickCounter);
      if (tickCounter < points.size()){
         curr = (ColourPoint)points.elementAt(tickCounter);
         x = curr.getX();
         y = curr.getY();
         if (tickCounter != 0){
            prev = (ColourPoint)points.elementAt(tickCounter);
            prev_x = prev.getX();
            prev_y = prev.getY();
            bi.setRGB(leader.getX(),leader.getY(),leader.getRGB());
            if (prev_x > 1){
               prev = leader.getLeft();
               bi.setRGB(prev.getX(),prev.getY(),prev.getRGB());
            }
            if (prev_x < width-2){
               prev = leader.getRight();
               bi.setRGB(prev.getX(),prev.getY(),prev.getRGB());
            }
            if (prev_y > 1){
               prev = leader.getUp();
               bi.setRGB(prev.getX(),prev.getY(),prev.getRGB());
            }
            if (prev_y < height-2){
               prev = leader.getDown();
               bi.setRGB(prev.getX(),prev.getY(),prev.getRGB());
            }
         }
         /*
         bi.setRGB(curr.getX(),curr.getY(),WHITE);
         if (curr.getX() > 0 && curr.getY() > 0){
            bi.setRGB(curr.getX()-1,curr.getY()-1,WHITE);
         }
         if (curr.getX() > 0 && curr.getY() < (height-1)){
            bi.setRGB(curr.getX()-1,curr.getY()+1,WHITE);
         }
         if (curr.getX() < (width-1) && curr.getY() > 0){
            bi.setRGB(curr.getX()+1,curr.getY()-1,WHITE);
         }
         if (curr.getX() < (width-1) && curr.getY() < (height-1)){
            bi.setRGB(curr.getX()+1,curr.getY()+1,WHITE);
         }
         if (tickCounter != 0){
            prev = (ColourPoint)points.elementAt(tickCounter - 1);
            bi.setRGB(prev.getX(),prev.getY(),prev.getRGB());
         }
          */
         leader = new LeaderDot(curr);
         if (x > 0){
            leader.setLeft(x-1,y,bi.getRGB(x-1,y));
            bi.setRGB(x-1,y,WHITE);
         }
         if (x < width-1){
            leader.setRight(x+1,y,bi.getRGB(x+1,y));
            bi.setRGB(x+1,y,WHITE);
         }
         if (y > 0){
            leader.setUp(x,y-1,bi.getRGB(x,y-1));
            bi.setRGB(x,y-1,WHITE);
         }
         if (y < height-1){
            leader.setDown(x,y+1,bi.getRGB(x,y+1));
            bi.setRGB(x,y+1,WHITE);
         }
         bi.setRGB(curr.getX(),curr.getY(),WHITE);
      }
      tickCounter++;
   }
   
   public void update(Graphics g){
      
   }
}