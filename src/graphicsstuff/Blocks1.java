package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Blocks1 extends JPanel implements Runnable{
   private boolean runTest;
   private int r,g,b,rgb,x,y, height, width;
   private BufferedImage bi;
   
   public Blocks1(int w, int h){
      height = h;
      width = w;
      runTest = true;
      r = 0;
      g = 0;
      b = 0;
      x = 0;
      y = 0;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      bi.setAccelerationPriority(1);
      Thread th = new Thread(this);
      th.start();
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
   
   public void tick(){
      rgb = (new Color(r,g,b)).getRGB();
      bi.setRGB(x,y,rgb);
      if (x == 254){
         x = 0;
         y++;
         b = 0;
         r++;
         g++;
      }
      else{
         b++;
         x++;
      }
   }
   
   public void update(Graphics g){
      
   }
}