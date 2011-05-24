package fractalflames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class FractalFlame1 extends JPanel{
   byte averageFactor, superSample;
   int height, width, histoHeight, histoWidth, iterations, totalFuncWeight;
   int[] funcChooser, funcWeightings;
   //int[][] histo;
   rgba[][] histo;
   BufferedImage bi;
   Point origin;
   
   public FractalFlame1(int w, int h){
      height = h;
      width = w;
      iterations = 10000000;
      init();
      renderImage();
      drawImage();
      saveImage();
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
      double omega, r, rsq, theta, cos120, cos240, sin120, sin240;
      double[] current, old, transCurrent;
      int x, y, function;
      int[] funcColour;
      
      funcColour = new int[3];
      current = new double[2];
      old = new double[2];
      current[0] = (Math.random()*2.0) - 1.0;
      current[1] = (Math.random()*2.0) - 1.0;
      transCurrent = translatePoint(current);
      cos120 = Math.cos(2*Math.PI/3);
      cos240 = Math.cos(4*Math.PI/3);
      sin120 = Math.sin(2*Math.PI/3);
      sin240 = Math.sin(4*Math.PI/3);
      /*
      for (int i=0;i<20;i++){
         function = (int)(Math.round(Math.random()*3.0));
         switch (function){
            case 1:
               current[0] = current[0]/2.0;
               current[1] = current[1]/2.0;
               break;
            case 2:
               current[0] = (current[0]+1.0)/2.0;
               current[1] = current[1]/2.0;
               break;
            case 3:
               current[0] = current[0]/2.0;
               current[1] = (current[1]+1.0)/2.0;
               break;
         }
      }
       */
      for (int i=0;i<iterations;i++){
         rsq = Math.pow(current[0],2.0) + Math.pow(current[1],2.0);
         r = Math.sqrt(rsq);
         theta = Math.atan2(current[1],current[0]);
         function = funcChooser[(int)(Math.floor(Math.random()*(double)totalFuncWeight))];
         switch (function){
            case 0:
               funcColour[0] = 100;
               funcColour[1] = 55;
               funcColour[2] = 0;
               break;
            case 1:
               funcColour[0] = 123;
               funcColour[1] = 84;
               funcColour[2] = 98;
               current[0] = Math.sin(current[0]);
               current[1] = Math.sin(current[1]);
               break;
            case 2:
               funcColour[0] = 205;
               funcColour[1] = 79;
               funcColour[2] = 139;
               current[0] = current[0]/rsq;
               current[1] = current[1]/rsq;
               break;
            case 3:
               funcColour[0] = 80;
               funcColour[1] = 153;
               funcColour[2] = 112;
               current[0] = r * (Math.cos(theta + r));
               current[1] = r * (Math.sin(theta + r));
               break;
            case 4:
               funcColour[0] = 56;
               funcColour[1] = 115;
               funcColour[2] = 132;
               current[0] = r * (Math.cos(2 * theta));
               current[1] = r * (Math.sin(2 * theta));
               break;
            case 5:
               funcColour[0] = 172;
               funcColour[1] = 168;
               funcColour[2] = 70;
               current[0] = theta / Math.PI;
               current[1] = r - 1;
               break;
            case 6:
               funcColour[0] = 83;
               funcColour[1] = 68;
               funcColour[2] = 71;
               current[0] = r * (Math.sin(theta + r));
               current[1] = r * (Math.cos(theta - r));
               break;
            case 7:
               funcColour[0] = 51;
               funcColour[1] = 255;
               funcColour[2] = 0;
               current[0] = r * (Math.sin(theta * r));
               current[1] = -(r * (Math.cos(theta * r)));
               break;
            case 8:
               funcColour[0] = 71;
               funcColour[1] = 30;
               funcColour[2] = 170;
               current[0] = (theta * (Math.sin(Math.PI * r)))/Math.PI;
               current[1] = (theta * (Math.cos(Math.PI * r)))/Math.PI;
               break;
            case 9:
               funcColour[0] = 255;
               funcColour[1] = 102;
               funcColour[2] = 0;
               current[0] = (Math.cos(theta) + Math.sin(r))/r;
               current[1] = (Math.sin(theta) - Math.cos(r))/r;
               break;
            case 10:
               funcColour[0] = 155;
               funcColour[1] = 102;
               funcColour[2] = 100;
               current[0] = Math.sin(theta)/r;
               current[1] = Math.cos(theta)*r;
               break;
            case 11:
               funcColour[0] = 51;
               funcColour[1] = 153;
               funcColour[2] = 0;
               current[0] = Math.sin(theta)*Math.cos(r);
               current[1] = Math.cos(theta)*Math.sin(r);
               break;
            case 12:
               funcColour[0] = 120;
               funcColour[1] = 172;
               funcColour[2] = 174;
               current[0] = r*Math.pow((Math.sin(theta + r)),3);
               current[1] = r*Math.pow((Math.cos(theta - r)),3);
               break;
            case 13:
               funcColour[0] = 166;
               funcColour[1] = 83;
               funcColour[2] = 130;
               omega = Math.round(Math.random());
               current[0] = Math.sqrt(r) * Math.cos(theta/2.0 + omega);
               current[1] = Math.sqrt(r) * Math.sin(theta/2.0 + omega);
               break;
            case 14:
               current[0] = -current[0];
               current[1] = -current[1];
               break;
            case 15:
               old[0] = current[0];
               old[1] = current[1];
               current[0] = (old[0]*cos120) - (old[1]*sin120);
               current[1] = (old[1]*cos120) + (old[0]*sin120);
               break;
            case 16:
               old[0] = current[0];
               old[1] = current[1];
               current[0] = (old[0]*cos240) - (old[1]*sin240);
               current[1] = (old[1]*cos240) + (old[0]*sin240);
               break;
            case 17:
               current[0] = -current[0];
               break;
            case 18:
               funcColour[0] = 255;
               funcColour[1] = 255;
               funcColour[2] = 255;
               current[0] = 0.75*current[0];
               current[1] = 0.75*current[1];
               break;
            case 20:
               current[0] = current[0]/2.0;
               current[1] = current[1]/2.0;
               break;
            case 21:
               current[0] = (current[0]+1.0)/2.0;
               current[1] = current[1]/2.0;
               break;
            case 22:
               current[0] = current[0]/2.0;
               current[1] = (current[1]+1.0)/2.0;
               break;
         }
         transCurrent = translatePoint(current);
         //System.out.println(i + " - " + current[0] + ":" + current[1]);
         //System.out.println(i + " - " + transCurrent[0] + ":" + transCurrent[1]);
         //System.out.println(i + " - " + (int)Math.floor(transCurrent[0]) + ":" + (int)Math.floor(transCurrent[1]));
         x = (int)transCurrent[0];
         y = (int)transCurrent[1];
         if (x < histoWidth && 0 <= x && y < histoHeight && 0 <= y){
            histo[x][y].addColour(funcColour);
            //histo[x][y]++;
            //bi.setRGB((int)transCurrent[0],(int)transCurrent[1],white);
         }
      }
   }
   
   private void init(){
      int pos;
      
      superSample = 1;
      averageFactor = (byte)Math.pow((double)(superSample),2);
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
      pos = 0;
      for (int i=0;i<funcWeightings.length;i++){
         for (int j=pos;j<(pos + funcWeightings[i]);j++){
            funcChooser[j] = i;
         }
         pos += funcWeightings[i];
      }
   }
   
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
   
   private void saveImage(){
      BufferedImage bi2;
      String fileFormat, filePath;
      String[] fileNames;
      rgba point;
      
      /*
      bi2 = new BufferedImage(histoWidth,histoHeight,BufferedImage.TYPE_INT_RGB);
      for (int i=0;i<histoWidth;i++){
         for (int j=0;j<histoWidth;j++){
            point = histo[i][j];
            bi2.setRGB(i,j,(new Color(point.r,point.g,point.b).getRGB()));
         }
      }
       */
      fileNames = new String[2];
      filePath = "c:\\progs\\java\\FractalFlames\\test\\fflame_" + System.currentTimeMillis();
      fileNames[0] = filePath + ".png";
      fileNames[1] = filePath + ".bmp";
      //fileNames[2] = filePath + "_full.png";
      //fileNames[3] = filePath + "_full.bmp";
      try{
         for (int i=0;i<fileNames.length;i++){
            fileFormat = fileNames[i].substring(fileNames[i].lastIndexOf(".")+1,fileNames[i].length());
            java.io.File outFile = new java.io.File(fileNames[i]);
            javax.imageio.ImageIO.write(bi,fileFormat,outFile);
         }
         /*
         for (int i=fileNames.length/2;i<fileNames.length;i++){
            fileFormat = fileNames[i].substring(fileNames[i].lastIndexOf(".")+1,fileNames[i].length());
            java.io.File outFile = new java.io.File(fileNames[i]);
            javax.imageio.ImageIO.write(bi2,fileFormat,outFile);
         }
          */
      }
      catch(Exception e){
         System.out.println("Exception in FractalFlame1.saveImage");
         System.out.println(e.getMessage());
      }
   }
}