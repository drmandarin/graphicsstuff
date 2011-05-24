import java.applet.*;
import java.awt.*;
import javax.swing.JApplet;

public class GraphicsApplet1 extends JApplet implements Runnable{
   int x, y, y_init;
   Thread th;
   
   public void destroy(){
      
   }
   
   public void init(){
      th = new Thread(this);
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.setColor(new Color(255,0,0));
      g2.drawLine(x,y,x,y);
   }
   
   public void run(){
      Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
      while(true){
         repaint();
         if (x == 400){
            destroy();
            x = 0;
            y = y_init++;
         }
         else{
            x++;
            y++;
         }
         try{
            //Thread.sleep();
         }
         catch(Exception e){
            System.out.println(e.getMessage());
         }
         Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      }
   }
   
   public void start(){
      x = 0;
      y = 0;
      y_init = 0;
      th.start();
   }
   
   public void stop(){
      
   }
   
   public void update(Graphics g){
      paint(g);
   }
}