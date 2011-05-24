package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.Random;
import javax.swing.JPanel;

public class Gaussian extends JPanel implements Runnable{
   boolean runTest;
   int BLACK;
   int BLUE;
   int GREY;
   int baseLine;
   int height;
   int width;
   int[] oldValues;
   int[] values;
   BufferedImage bi;
   Random random;
   Thread thread;
   
   public Gaussian(int w, int h){
      runTest = true;
      height = h;
      width = w;
      baseLine = height-20;
      oldValues = new int[width];
      values = new int[width];
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      random = new Random();
      BLACK = (new Color(0,0,0)).getRGB();
      BLUE = (new Color(100,0,255)).getRGB();
      GREY = (new Color(128,128,128)).getRGB();
      for (int i=0;i<height;i++){
         bi.setRGB(639,i,GREY);
      }
      thread = new Thread(this);
      thread.start();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      for (int i=0;i<oldValues.length;i++){
         bi.setRGB(i,(baseLine-oldValues[i]),BLACK);
      }
      for (int i=0;i<values.length;i++){
         bi.setRGB(i,(baseLine-values[i]/100),BLUE);
         oldValues[i] = (values[i]/100);
      }
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   public void run(){
      int j = 0;
      
      while(runTest){
         tick();
         if (j%200 == 0){
            repaint();
         }
         if (j > 2147483640){
            j = 0;
         }
         j++;
      }
   }
   
   public void tick(){
      double randomValue;
      int index;
      
      randomValue = random.nextGaussian();
      if ((Math.abs(randomValue)) < 4){
         index = (int)((randomValue+4.0)*(1279D/8D));
         values[index]++;
      }
      /*
      for (int i=0;i<oldValues.length;i++){
         bi.setRGB(i,(baseLine-oldValues[i]),BLACK);
      }
      for (int i=0;i<values.length;i++){
         bi.setRGB(i,(baseLine-values[i]/10),BLUE);
         oldValues[i] = (values[i]/10);
      }
       */
   }
}