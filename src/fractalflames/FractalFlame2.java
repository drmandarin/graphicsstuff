package fractalflames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class FractalFlame2 extends JPanel{
   byte averageFactor, superSample;
   int height, width, histoHeight, histoWidth, iterations, totalFuncWeight;
   int[] funcChooser, funcWeightings;
   //int[][] histo;
   rgba[][] histo;
   BufferedImage bi;
   Point origin;
   
   public FractalFlame2(int w, int h){
      height = h;
      width = w;
      init();
      renderImage();
      drawImage();
   }
   
   private void drawImage(){
      double hit, logHit;
      double[] sampledPoint;
      int r, g, b, colour, minHit, maxHit;
      
      sampledPoint = new double[4];
      /*
      minHit = iterations;
      maxHit = -1;
      for (int i=0;i<width;i++){
         for (int j=0;j<height;j++){
            minHit = (histo[i][j].a < minHit) ? histo[i][j].a : minHit;
            maxHit = (histo[i][j].a > maxHit) ? histo[i][j].a : maxHit;
         }
      }
      System.out.println("minHit: " + minHit);
      System.out.println("maxHit: " + maxHit);
       */
      for (int i=0;i<width;i++){
         for (int j=0;j<height;j++){
            sampledPoint = getSample(i,j);
            hit = sampledPoint[3];
            //hit = (double)histo[i][j].a;
            logHit = 1.0 - Math.log(hit)/hit;
            r = (int)(sampledPoint[0]*logHit);
            g = (int)(sampledPoint[1]*logHit);
            b = (int)(sampledPoint[2]*logHit);
            /*
            r = (int)((double)histo[i][j].r*logHit);
            g = (int)((double)histo[i][j].g*logHit);
            b = (int)((double)histo[i][j].b*logHit);
             */
            /*
            if (r > 255 || g > 255 || b > 255){
               System.out.println(r+":"+g+":"+b+":"+hit);
               for (int m=i;m<(i+superSample);m++){
                  for (int n=j;n<(j+superSample);j++){
                     System.out.print(histo[m][n].r+":");
                     System.out.print(histo[m][n].g+":");
                     System.out.print(histo[m][n].b+":");
                     System.out.print(histo[m][n].a);
                  }
                  System.out.println();
               }
            }
             */
            if (hit > 0){
               colour = (new Color(r,g,b)).getRGB();
               bi.setRGB(i,j,colour);
            }
         }
      }
   }
   
   private void renderImage(){
   }
   
   private void init(){
      superSample = 1;
      histoHeight = superSample * height;
      histoWidth = superSample * width;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      origin = new Point(histoHeight/2,histoWidth/2);
      //histo = new int[width][height];
      histo = new rgba[histoWidth][histoHeight];
      for (int i=0;i<histoWidth;i++){
         for (int j=0;j<histoHeight;j++){
            //histo[i][j] = 0;
            histo[i][j] = new rgba();
         }
      }
      funcWeightings = new int[19];
      funcWeightings[0] = 0;
      funcWeightings[1] = 0;
      funcWeightings[2] = 0;
      funcWeightings[3] = 0;
      funcWeightings[4] = 0;
      funcWeightings[5] = 20;
      funcWeightings[6] = 0;
      funcWeightings[7] = 20;
      funcWeightings[8] = 20;
      funcWeightings[9] = 0;
      funcWeightings[10] = 0;
      funcWeightings[11] = 0;
      funcWeightings[12] = 20;
      funcWeightings[13] = 20;
      funcWeightings[14] = 0;
      funcWeightings[15] = 100;
      funcWeightings[16] = 100;
      funcWeightings[17] = 100;
      funcWeightings[18] = 0;
      totalFuncWeight = 0;
      for (int i=0;i<funcWeightings.length;i++){
         totalFuncWeight += funcWeightings[i];
      }
      funcChooser = new int[totalFuncWeight];
      int pos = 0;
      for (int i=0;i<funcWeightings.length;i++){
         for (int j=pos;j<(pos + funcWeightings[i]);j++){
            funcChooser[j] = i;
         }
         pos += funcWeightings[i];
      }
   }

   @Override
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   private double[] translatePoint(double[] originalPoint){
      double x, y;
      double[] newPoint;
      
      newPoint = new double[2];
      x = originalPoint[0];
      y = originalPoint[1];
      x = x + 1.0;
      y = 1.0 - y;
      x *= (double)origin.x;
      y *= (double)origin.y;
      newPoint[0] = x;
      newPoint[1] = y;
      
      return newPoint;
   }
   
   private double[] getSample(int x, int y){
      double r, g, b, a;
      double[] retVal;
      int fromx, fromy;
      
      retVal = new double[4];
      r = 0.0;
      g = 0.0;
      b = 0.0;
      a = 0.0;
      
      fromx = x*superSample;
      fromy = y*superSample;
      for (int i = fromx; i < (fromx + superSample); i++){
         for (int j = fromy; j < (fromy + superSample); j++){
            r += (double)histo[i][j].r;
            g += (double)histo[i][j].g;
            b += (double)histo[i][j].b;
            //a += (double)histo[i][j].a;
            a = Math.max((double)histo[i][j].a,a);
         }
      }
      
      r /= averageFactor;
      g /= averageFactor;
      b /= averageFactor;
      //a /= averageFactor;
      
      retVal[0] = r;
      retVal[1] = g;
      retVal[2] = b;
      retVal[3] = a;
      return retVal;
   }
}