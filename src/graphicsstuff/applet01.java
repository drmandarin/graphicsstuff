package graphicsstuff;

import java.applet.Applet;
import java.awt.Graphics;
//import javax.swing.JApplet;

public class applet01 extends Applet {
   StringBuffer buffer;

   @Override
   public void init() {
      buffer = new StringBuffer();
      addItem("initialising ... ");
   }

   @Override
   public void start(){
      addItem("starting ...");
   }

   @Override
   public void stop(){
      addItem("stopping ...");
   }

   @Override
   public void destroy(){
      addItem("destroying ...");
   }

   public void addItem(String newWord){
      System.out.println(newWord);
      buffer.append(newWord);
      repaint();
   }

   public void paint(Graphics g){
      g.drawRect(0,0,getWidth()-1,getHeight()-1);
      g.drawString(buffer.toString(),5,15);
   }
}