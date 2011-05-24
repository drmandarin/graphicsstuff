package graphicsstuff;

import java.awt.*;
import javax.swing.*;

class Fonts1 extends JPanel{
   Font myFont;
   
   public Fonts1(){
   }
   
   public void paint(Graphics g){
      char chars[];
      int j;
      Graphics2D g2;
      
      j = 300;
      myFont = new Font("Bitstream Cyberbit",Font.PLAIN,32);
      System.out.println(myFont.getNumGlyphs());
      chars = new char[j];
      for (int i=0;i<j;i++){
         chars[i] = (char)(i+12353);
      }
      g2 = (Graphics2D)g;
      g2.setFont(myFont);
      for (int i=0;i<j/20;i++){
         g2.drawChars(chars,i*20,20,10,(i*40)+40);
      }
   }
}