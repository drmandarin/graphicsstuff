package graphicsstuff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Alias2 extends JPanel{
   int WHITE, height, width;
   BufferedImage bi;
   
   public Alias2(int w, int h){
      height = h;
      width = w;
      init();
      drawLine(10,10,350,30);
      drawLine(10,10,350,300);
      drawLine(10,10,350,10);
      drawLine(10,10,250,360);
      drawLine(10,10,30,350);
      /*
      int x2, y2;
      double theta;
      for (int i = 0;i<36;i++){
         theta = ((double)i * 10.0) * (Math.PI/180.0);
         x2 = (int)(199.0 * Math.cos(theta));
         y2 = (int)(199.0 * Math.sin(theta));
         drawLine(200,200,x2+200,y2+200);
      }
      //x2 = (int)(199.0 * Math.cos(Math.PI/6));
      //y2 = (int)(199.0 * Math.sin(Math.PI/6));
      //drawLine(200,200,x2+200,y2+200);
       */
   }
   
   private void drawLine(int ix1, int iy1, int ix2, int iy2){
      double dx, dy, gradient, interx, intery, xend, yend, xgap, ygap, x1, y1, x2, y2;
      float colVal;
      int xpxl1, xpxl2, ypxl1, ypxl2;
      Color pxlColour;
      
      x1 = (double)ix1;
      x2 = (double)ix2;
      y1 = (double)iy1;
      y2 = (double)iy2;
      if (x2 < x1){
         double temp = x1;
         x1 = x2;
         x2 = temp;
         temp = y1;
         y1 = y2;
         y2 = temp;
      }
      dx = x2 - x1;
      dy = y2 - y1;
      
      if (dx > dy){
         gradient = dy/dx;
         
         //first endpoint
         xend = round(x1);
         yend = y1 + gradient * (xend - x1);
         xgap = rfpart(x1 + 0.5);
         xpxl1 = (int)xend;
         ypxl1 = (int)ipart(yend);
         colVal = (float)(rfpart(yend) * xgap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl1,ypxl1,pxlColour.getRGB());
         colVal = (float)(fpart(yend) * xgap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl1,ypxl1+1,pxlColour.getRGB());
         intery = yend + gradient;

         //second endpoint
         xend = round(x2);
         yend = y2 + gradient * (xend - x2);
         xgap = rfpart(x2 + 0.5);
         xpxl2 = (int)xend;
         ypxl2 = (int)ipart(yend);
         colVal = (float)(rfpart(yend) * xgap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl2,ypxl2,pxlColour.getRGB());
         colVal = (float)(fpart(yend) * xgap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl2,ypxl2+1,pxlColour.getRGB());

         for (int i=(xpxl1+1);i<xpxl2;i++){
            colVal = (float)(fpart(intery));
            pxlColour = new Color(colVal,0,0);
            bi.setRGB(i,(int)ipart(intery),pxlColour.getRGB());
            colVal = (float)(rfpart(intery));
            pxlColour = new Color(colVal,0,0);
            bi.setRGB(i,(int)ipart(intery)+1,pxlColour.getRGB());
            //bi.setRGB(i,(int)ipart(intery)-1,pxlColour.getRGB());
            intery += gradient;
         }
      }
      else{
         gradient = dx/dy;
         
         //first endpoint
         yend = round(y1);
         xend = x1 + gradient * (yend - y1);
         ygap = rfpart(y1 + 0.5);
         xpxl1 = (int)ipart(xend);
         ypxl1 = (int)yend;
         colVal = (float)(rfpart(xend) * ygap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl1,ypxl1,pxlColour.getRGB());
         colVal = (float)(fpart(xend) * ygap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl1+1,ypxl1,pxlColour.getRGB());
         interx = xend + gradient;

         //second endpoint
         yend = round(y2);
         xend = x2 + gradient * (yend - y2);
         ygap = rfpart(y2 + 0.5);
         xpxl2 = (int)ipart(xend);
         ypxl2 = (int)yend;
         colVal = (float)(rfpart(xend) * ygap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl2,ypxl2,pxlColour.getRGB());
         colVal = (float)(fpart(xend) * ygap);
         pxlColour = new Color(colVal,colVal,colVal);
         bi.setRGB(xpxl2+1,ypxl2,pxlColour.getRGB());

         for (int i=(ypxl1+1);i<ypxl2;i++){
            colVal = (float)(fpart(interx));
            pxlColour = new Color(colVal,0,0);
            bi.setRGB((int)ipart(interx),i,pxlColour.getRGB());
            colVal = (float)(rfpart(interx));
            pxlColour = new Color(colVal,0,0);
            bi.setRGB((int)ipart(interx)+1,i,pxlColour.getRGB());
            interx += gradient;
         }
      }
   }
   
   private void init(){
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      WHITE = (Color.WHITE).getRGB();
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   //===========================================================================
   //Utility functions
   //===========================================================================
   
   private double fpart(double x){
      return (Math.ceil(x) - x);
   }
   
   private double ipart(double x){
      return Math.floor(x);
   }
   
   private double rfpart(double x){
      return 1.0 - fpart(x);
   }
   
   private double round(double x){
      return ipart(x + 0.5);
   }
}