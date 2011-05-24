package graphicsstuff;

import java.awt.*;
import javax.swing.*;

public class Graph2 extends JPanel{
   private int height, width;
   
   public Graph2(int w, int h){
      super();
      height = h;
      width = w;
   }
   
   public void paint(Graphics g){
      double x_val[], y_val[], z_val[];
      int BLUE, GREEN, RED;
      int i;
      int x_pos[], y_pos[];
      Color pixel;
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.setBackground(new Color(0,0,0));
      g2.clearRect(0,0,width,height);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      BLUE = (new Color(0,0,255)).getRGB();
      GREEN = (new Color(0,255,0)).getRGB();
      RED = (new Color(255,0,0)).getRGB();
      x_val = new double[width];
      y_val = new double[width];
      z_val = new double[width];
      x_pos = new int[width];
      y_pos = new int[width];
      
      for (i=0;i<width;i++){
         putPixel(g2,i,height/2,RED);
      }
      for (i=0;i<height;i++){
         putPixel(g2,width/2,i,RED);
      }
      for (i=0;i<width;i++){
         x_pos[i] = i;
         x_val[i] = (double)(i - (width/2))/144;
         y_val[i] = Math.pow(x_val[i],Math.sin(x_val[i]));
         //y_val[i] = Math.cos(x_val[i]);
         y_pos[i] = (int)(y_val[i]*216) + (height/2);
         if ((y_pos[i] < 0) || (y_pos[i] > (height-1))){
            y_pos[i] = 0;
         }
         //z_val[i] = (1.3 - Math.abs(y_val[i]))/1.3;
         z_val[i] = 1;
         pixel = new Color(0,(int)(z_val[i]*255),0,0);
         putPixel(g2,x_pos[i],y_pos[i],pixel.getRGB());
         putPixel(g2,x_pos[i],y_pos[i]+1,pixel.getRGB());
         putPixel(g2,x_pos[i],y_pos[i]-1,pixel.getRGB());
      }
   }
   
   private void putPixel(Graphics2D g2,int x,int y,int rgb){
      g2.setColor(new Color(rgb));
      g2.drawLine(x,y,x,y);
   }
}