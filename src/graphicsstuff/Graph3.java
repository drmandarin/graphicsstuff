package graphicsstuff;

import java.awt.*;
import javax.swing.*;

public class Graph3 extends JPanel implements Runnable{
   int height, width, x, y, y_init;
   
   public Graph3(int w, int h){
      x = 0;
      y = 0;
      y_init = 0;
      Thread th = new Thread(this);
      th.start();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.setColor(new Color(255,0,0));
      g2.drawLine(0,0,x,y);
   }
   
   public void run(){
      Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
      while(true){
         repaint();
         if(x == 500){
            x = 0;
            y = y_init;
            y_init++;
         }
         else{
            x++;
            y++;
         }
         try{
            Thread.sleep(0,10);
         }
         catch(Exception e){
            System.out.println(e.getMessage());
         }
      }
   }
}