package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.RenderingHints;
import java.lang.Thread;
import java.util.Random;
import javax.swing.JPanel;

public class Lines1 extends JPanel implements Runnable{
   boolean runTest;
   double incX, incY;
   int height, width, numSteps, stepsDone, white;
   int[] startColour, endColour, deltaColour, tmpColour;
   BufferedImage bi;
   Point current, startPoint, endPoint, delta;
   Random rndm;
   Thread thread;
   
   public Lines1(int w, int h){
      height = h;
      width = w;
      runTest = true;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      rndm = new Random();
      white = (new Color(255,255,255)).getRGB();
      init();
      
      thread = new Thread(this);
      thread.start();
   }
   
   public void init(){
      int tmpRGB;
      startPoint = new Point(rndm.nextInt(width),rndm.nextInt(height));
      endPoint = new Point(rndm.nextInt(width),rndm.nextInt(height));
      delta = new Point(endPoint.x - startPoint.x,endPoint.y - startPoint.y);
      numSteps = (Math.abs(delta.x) < Math.abs(delta.y)) ? Math.abs(delta.y) : Math.abs(delta.x);
      incX = (double)delta.x/(double)numSteps;
      incY = (double)delta.y/(double)numSteps;
      current = new Point(startPoint.x,startPoint.y);
      stepsDone = 0;
      startColour = new int[3];
      endColour = new int[3];
      deltaColour = new int[3];
      tmpColour = new int[3];
      for (int i=0;i<3; i++){
         startColour[i] = rndm.nextInt(255);
         endColour[i] = rndm.nextInt(255);
         deltaColour[i] = endColour[i] - startColour[i];
      }
      
      //System.out.println("Start:   " + startPoint.x + ":" + startPoint.y);
      //System.out.println("End:     " + endPoint.x + ":" + endPoint.y);
      //System.out.println("Delta:   " + delta.x + ":" + delta.y);
      //System.out.println("Inc:     " + incX + ":" + incY);
      //System.out.println("SColour: [" + startColour[0] + "," + startColour[1] + "," + startColour[2] + "]");
      //System.out.println("EColour: [" + endColour[0] + "," + endColour[1] + "," + endColour[2] + "]");
      tmpRGB = (new Color(startColour[0],startColour[1],startColour[2])).getRGB();
      bi.setRGB(startPoint.x,startPoint.y,tmpRGB);
      tmpRGB = (new Color(endColour[0],endColour[1],endColour[2])).getRGB();
      bi.setRGB(endPoint.x,endPoint.y,tmpRGB);
   }
   
   public void paint(Graphics g){
      Graphics2D g2 = (Graphics2D)g;
      
      /*
      g2.setBackground((Color.BLACK));
      g2.clearRect(0,0,width,height);
      g2.setColor(new Color(255,255,255));
      g2.drawLine(startPoint.x,startPoint.y,endPoint.x,endPoint.y);
       */
      //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      //g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.drawImage(bi,null,0,0);
      g2.dispose();
   }
   
   protected void paintComponent(Graphics g){
      Graphics2D g2 = (Graphics2D)g;
      //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      //g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      //g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      super.paintComponent(g2);
   }
   
   public void run(){
      while(runTest){
         try{
            Thread.sleep(1);
         }
         catch(Exception e){
            System.out.println("Exception in Lines1.run()");
            System.out.println(e.getMessage());
         }
         tick();
         repaint();
      }
   }
   
   public void tick(){
      double lambda;
      int tmpRGB;
      
      if (stepsDone < numSteps){
         current.x = (int)((double)stepsDone * incX) + startPoint.x;
         current.y = (int)((double)stepsDone * incY) + startPoint.y;
         //System.out.println(current.x + ":" + current.y);
         lambda = (double)1.0 - ((double)numSteps - (double)stepsDone)/(double)numSteps;
         for (int i=0;i<3;i++){
            tmpColour[i] = (int)((double)deltaColour[i] * lambda + (double)startColour[i]);
         }
         //System.out.println("TColour: [" + tmpColour[0] + "," + tmpColour[1] + "," + tmpColour[2] + "]");
         tmpRGB = (new Color(tmpColour[0],tmpColour[1],tmpColour[2])).getRGB();
         stepsDone++;
         bi.setRGB(current.x,current.y,tmpRGB);
      }
      else{
         init();
      }
   }
   
}