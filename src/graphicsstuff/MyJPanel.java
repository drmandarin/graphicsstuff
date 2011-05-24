package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.Random;

public class MyJPanel extends JPanel{
   private int height, width;
   
   public MyJPanel(int w, int h){
      super();
      height = h;
      width = w;
   }
   
   public void paint(Graphics g){
      double colourchoice;
      int RED;
      BufferedImage bi;
      Color pixel, refpixel;
      Color pixels[][];
      Graphics2D g2;
      Random rndm;
      
      g2 = (Graphics2D)g;
      rndm = new Random();
      pixels = new Color[width][height];
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      pixel = new Color(255,0,0);
      RED = pixel.getRGB();
      for (int i=0;i<width;i++){
         for (int j=0;j<height;j++){
            if ((rndm.nextDouble() < 0.0) || (i == 0)){
               pixel = new Color(rndm.nextInt(256),rndm.nextInt(256),rndm.nextInt(256));
            }
            else{
               if (j%10 == 0){
                  colourchoice = rndm.nextDouble() * 3;
                  refpixel = pixels[i-1][j];
                  if ((colourchoice < 1)){
                     pixel = new Color((refpixel.getRed()%255)+1,refpixel.getGreen(),refpixel.getBlue());
                  }
                  else if ((colourchoice >= 1) && (colourchoice < 2)){
                     pixel = new Color(refpixel.getRed(),(refpixel.getGreen()%255)+1,refpixel.getBlue());
                  }
                  else{
                     pixel = new Color(refpixel.getRed(),refpixel.getGreen(),(refpixel.getBlue()%255)+1);
                  }
               }
               else{
                  refpixel = pixels[i][j-1];
                  pixel = new Color(refpixel.getRGB());
               }
            }
            pixels[i][j] = new Color(pixel.getRGB());
            bi.setRGB(i,j,pixel.getRGB());
         }
      }
      g2.drawImage(bi,null,0,0);
   }
}