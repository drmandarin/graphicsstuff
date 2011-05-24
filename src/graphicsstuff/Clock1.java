package graphicsstuff;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import javax.swing.*;

class Clock1 extends JPanel implements Runnable{
   boolean firstRun, runTest;
   double angle;
   double[][] rotMatrix;
   int height, width, currentSecs;
   int[] x_points, y_points;
   int[][] arrow_base, arrow_points;
   Color black, red;
   GregorianCalendar myCal;
   final Point origin;
   Point endPoint;
   Random rndm;
   
   public Clock1(int w, int h){
      super();
      rndm = new Random();
      height = h;
      width = w;
      firstRun = true;
      runTest = true;
      arrow_base = new int[4][2];
      arrow_points = new int[4][2];
      x_points = new int[4];
      y_points = new int[4];
      origin = new Point(width/2, height/2);
      arrow_base[0][0] = origin.x;
      arrow_base[0][1] = origin.y;
      arrow_base[1][0] = arrow_base[0][0] - 4;
      arrow_base[1][1] = arrow_base[0][1] + 4;
      arrow_base[2][0] = arrow_base[0][0];
      arrow_base[2][1] = arrow_base[0][1] - 40;
      arrow_base[3][0] = arrow_base[0][0] + 4;
      arrow_base[3][1] = arrow_base[0][1] + 4;
      arrow_points[0][0] = arrow_base[0][0];
      arrow_points[0][1] = arrow_base[0][1];
      x_points[0] = arrow_base[0][0];
      y_points[0] = arrow_base[0][1];
      rotMatrix = new double[2][2];
      endPoint = new Point();
      Thread th = new Thread(this);
      th.start();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      rotMatrix[0][0] = Math.cos(angle);
      rotMatrix[0][1] = Math.sin(angle);
      rotMatrix[1][0] = -rotMatrix[0][1];
      rotMatrix[1][1] = rotMatrix[0][0];
      for (int i=0;i<4;i++){
         arrow_points[i][0] = (int)(arrow_base[i][0]*rotMatrix[0][0] + arrow_base[i][1]*rotMatrix[1][0]);
         arrow_points[i][1] = (int)(arrow_base[i][0]*rotMatrix[0][1] + arrow_base[i][1]*rotMatrix[1][1]);
         x_points[i] = arrow_points[i][0];
         y_points[i] = arrow_points[i][1];
      }
      g2.setBackground(new Color(0,0,0));
      g2.clearRect(0,0,width,height);
      g2.setColor(new Color(255,0,0));
      //g2.drawString(Long.toString(System.currentTimeMillis()),curr_x,curr_y);
      g2.drawLine(origin.x,origin.y,endPoint.x,endPoint.y);
      g2.fillPolygon(x_points,y_points,4);
   }

   /*
   public void repaint(){
      firstRun = true;
      super.repaint();
   }
    */

   public void run(){
      while(runTest){
         tick();
         repaint();
         /*
         try{
            Thread.sleep(1);
         }
         catch(Exception e){
            System.out.println("Exception in Clock1.run()");
            System.out.println(e.getMessage());
         }
          */
      }
   }
   
   private void tick(){
      myCal = new GregorianCalendar();
      currentSecs = myCal.get(Calendar.SECOND);
      angle = ((currentSecs/60.0)*2*Math.PI);
      //System.out.print(currentSecs+"\t"+(float)currentSecs/60+"\t"+angle);
      endPoint.x = origin.x + (int)Math.round(Math.sin(angle)*40);
      endPoint.y = origin.y - (int)Math.round(Math.cos(angle)*40);
      //System.out.println("\t"+Math.sin((currentSecs/60)*2*Math.PI));
   }
}