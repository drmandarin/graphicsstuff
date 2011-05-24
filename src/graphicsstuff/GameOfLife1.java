package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;

class GameOfLife1 extends JPanel implements Runnable{
   boolean runTest;
   int black, green, height, width;
   int[][] newUniverse, oldUniverse;
   BufferedImage bi;
   Thread th;
   Vector changed;
   
   public GameOfLife1(int w, int h){
      height = h;
      width = w;
      newUniverse = new int[height][width];
      oldUniverse = new int[height][width];
      changed = new Vector();
      runTest = true;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      initValues();
      th = new Thread(this);
      th.start();
   }
   
   private void cloneUniv(){
      Point tmpPoint;
      
      //System.out.println("cloning universe...");
      for (int i=0;i<changed.size();i++){
         tmpPoint = (Point)changed.get(i);
         oldUniverse[tmpPoint.y][tmpPoint.x] = newUniverse[tmpPoint.y][tmpPoint.x];
      }
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
      for (int i=1;i<height-1;i++){
         for (int j=1;j<width-1;j++){
            if (rndm.nextFloat() < 0.02){
               newUniverse[i][j] = 1;
               changed.add(new Point(j,i));
               bi.setRGB(j,i,green);
            }
            else{
               bi.setRGB(j,i,black);
            }
         }
      }
      System.out.println(changed.size()*100/(height*width)+"%");
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   public void run(){
      System.out.println("running...");
      while(runTest){
         repaint();
         tick();
      }
   }
   
   private void tick(){
      int newVal, size;
      Point tmpPoint;
      
      System.out.println("ticking...");
      cloneUniv();
      size = changed.size();
      for (int i=0;i<size;i++){
         System.out.println("Size of changed: "+size);
         tmpPoint = (Point)changed.get(0);
         changed.remove(0);
         newVal = examinePoint(tmpPoint);
         if (oldUniverse[tmpPoint.y][tmpPoint.x] != newVal){
            newUniverse[tmpPoint.y][tmpPoint.x] = newVal;
            changed.add(new Point(tmpPoint.x,tmpPoint.y));
            if (newVal == 0){
               bi.setRGB(tmpPoint.x,tmpPoint.y,black);
            }
            else{
               bi.setRGB(tmpPoint.x,tmpPoint.y,green);
            }
         }
      }
   }
}