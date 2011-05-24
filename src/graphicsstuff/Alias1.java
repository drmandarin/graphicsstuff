package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Alias1 extends JPanel{
   int height, width;
   BufferedImage bi;
   
   public Alias1(int w, int h){
      height = h;
      width = w;
      init();
      drawLine(new Point(10,10),new Point(350,30));
   }
   
   private void computeColours(double x, double y, Point curr, Point prev){
      int val;
      Color tmpColour;
      Point aa1, aa2;
      
      aa1 = new Point(prev.x, curr.y);
      aa2 = new Point(curr.x, prev.y);
      //val = (int)();
      tmpColour = new Color(127,127,127);
      val = tmpColour.getRGB();
      bi.setRGB(aa1.x,aa1.y,val);
      bi.setRGB(aa2.x,aa2.y,val);
   }
   
   private void drawLine(Point a, Point b){
      double incX, incY, newX, newY;
      int numSteps, stepsDone, WHITE;
      Color tempColour;
      Point current, delta, previous;
      
      current = new Point();
      current.x = a.x;
      current.y = a.y;
      delta = new Point();
      previous = new Point();
      WHITE = (Color.WHITE).getRGB();
      stepsDone = 0;
      delta.x = b.x - a.x;
      delta.y = b.y - a.y;
      numSteps = Math.abs(delta.x) < Math.abs(delta.y) ? Math.abs(delta.y) : Math.abs(delta.x);
      incX = delta.x/(double)numSteps;
      incY = delta.y/(double)numSteps;
      
      while (stepsDone < numSteps){
         newX = (double)stepsDone * incX + (double)a.x;
         newY = (double)stepsDone * incY + (double)a.y;
         previous.x = current.x;
         previous.y = current.y;
         current.x = (int)newX;
         current.y = (int)newY;
         if ((previous.x != current.x) && (previous.y != current.y)){
            computeColours(newX,newY,current,previous);
         }
         System.out.println("newX    : " + newX);
         System.out.println("floor   : " + Math.floor(newX) + ":" + (int)Math.floor(newX));
         System.out.println("ceil    : " + Math.ceil(newX) + ":" + (int)Math.ceil(newX));
         System.out.println("newY    : " + newY);
         System.out.println("floor   : " + Math.floor(newY) + ":" + (int)Math.floor(newY));
         System.out.println("ceil    : " + Math.ceil(newY) + ":" + (int)Math.ceil(newY));
         System.out.println("currentX: " + current.x);
         System.out.println("currentY: " + current.y);
         //computeColours(newX,newY);
         bi.setRGB(current.x,current.y,WHITE);
         //tempColour;
         stepsDone++;
      }
   }
   
   private void init(){
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
}