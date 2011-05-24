package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Graph1 extends JPanel{
   private int height, width;
   
   public Graph1(int w, int h){
      super();
      height = h;
      width = w;
   }
   
   public void drawLine(BufferedImage bi,int x1,int y1,int x2,int y2){
      int RED, r, g, b;
      Color pixel1, pixel2;
      
      RED = (new Color(255,0,0)).getRGB();
      for (int i=y1;i<y2;i++){
         pixel1 = new Color(bi.getRGB(x1,y1));
         pixel2 = new Color(bi.getRGB(x2,y2));
         r = (pixel1.getRed() + pixel2.getRed())/2;
         g = (pixel1.getGreen() + pixel2.getGreen())/2;
         b = (pixel1.getBlue() + pixel2.getBlue())/2;
         /*
         bi.setRGB((x1+x2)/2,i,(new Color(r,g,b).getRGB()));
         bi.setRGB((x1+x2)/2,i-1,(new Color(r,g,b).getRGB()));
         bi.setRGB((x1+x2)/2,i+1,(new Color(r,g,b).getRGB()));
          */
         bi.setRGB((x1+x2)/2,i,RED);
         bi.setRGB((x1+x2)/2,i-1,RED);
         bi.setRGB((x1+x2)/2,i+1,RED);
      }
   }
   
   public void paint(Graphics g){
      double x_val[], y_val[], z_val[];
      int BLUE, GREEN, RED;
      int i;
      int x_pos[], y_pos[];
      BufferedImage bi;
      Color pixel;
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
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
      
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      for (i=0;i<width;i++){
         bi.setRGB(i,height/2,RED);
      }
      for (i=0;i<height;i++){
         bi.setRGB(width/2,i,RED);
      }
      for (i=0;i<width;i++){
         x_pos[i] = i;
         x_val[i] = (double)(i - (width/2))/144;
         y_val[i] = -Math.cos(x_val[i]);
         y_pos[i] = (int)(y_val[i]*216) + (height/2);
         if ((y_pos[i] < 0) || (y_pos[i] > (height-1))){
            y_pos[i] = 0;
         }
         z_val[i] = (1.3 - Math.abs(y_val[i]))/1.3;
         pixel = new Color(0,(int)(z_val[i]*255),0,0);
         bi.setRGB(x_pos[i],y_pos[i],pixel.getRGB());
      }
      /*
      g2.setColor(new Color(255,0,0));
      for (i=1;i<width;i++){
         g2.drawLine(x_pos[i-1],y_pos[i-1],x_pos[i],y_pos[i]);
      }
       */
      for (i=0;i<width-1;i++){
         drawLine(bi,x_pos[i],y_pos[i],x_pos[i+1],y_pos[i+1]);
      }
      g2.drawImage(bi,null,0,0);
   }
}