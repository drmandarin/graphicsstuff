package graphicsstuff;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import javax.swing.*;

class Clock2 extends JPanel implements Runnable{
   boolean runTest;
   int height, width;
   final Color black, red;
   Point origin;
   Point[] arrowBase;
   Point[][] arrowPoint;
   
   public Clock2(int w, int h){
      super();
      height = h;
      width = w;
      runTest = true;
      black = new Color(0,0,0);
      red = new Color(255,0,0);
      initVals();
      Thread th = new Thread(this);
      th.start();
   }
   
   private int[][] convertCoords(Point[] points){
      int[][] newPoints;
      Point offset;
      
      newPoints = new int[2][4];
      offset = new Point(width/2,height/2);
      for(int i=0;i<4;i++){
         newPoints[0][i] = points[i].x+offset.x;
         newPoints[1][i] = points[i].y+offset.y;
      }
      
      return newPoints;
   }
   
   public void initVals(){
      double theta;
      double[][] rotMatrix;
      int x, y;
      
      rotMatrix = new double[2][2];
      origin = new Point(0,0);
      arrowBase = new Point[4];
      arrowBase[0] = new Point(origin.x,origin.y);
      arrowBase[1] = new Point(arrowBase[0].x-40,arrowBase[0].y+40);
      arrowBase[2] = new Point(arrowBase[0].x,arrowBase[0].y-100);
      arrowBase[3] = new Point(arrowBase[0].x+40,arrowBase[0].y+40);
      arrowPoint = new Point[60000][4];
      for (int i=0;i<60000;i++){
         theta = (i/30000.0)*Math.PI;
         rotMatrix[0][0] = Math.cos(theta);
         rotMatrix[0][1] = -Math.sin(theta);
         rotMatrix[1][0] = -rotMatrix[0][1];
         rotMatrix[1][1] = rotMatrix[0][0];
         for (int j=0;j<4;j++){
            x = (int)(arrowBase[j].x*rotMatrix[0][0]+arrowBase[j].y*rotMatrix[0][1]);
            y = (int)(arrowBase[j].x*rotMatrix[1][0]+arrowBase[j].y*rotMatrix[1][1]);
            arrowPoint[i][j] = new Point(x,y);
         }
      }
   }
   
   public void paint(Graphics g){
      int[][] points;
      Graphics2D g2;
      GregorianCalendar myCal;
      
      g2 = (Graphics2D)g;
      myCal = new GregorianCalendar();
      points = convertCoords(arrowPoint[myCal.get(Calendar.SECOND)*1000+myCal.get(Calendar.MILLISECOND)]);
      g2.setBackground(black);
      g2.clearRect(0,0,width,height);
      g2.setColor(red);
      g2.drawPolygon(points[0],points[1],4);
   }
   
   public void run(){
      while(runTest){
         repaint();
      }
   }
}