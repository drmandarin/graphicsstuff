package supersample;

import java.awt.Color;
import java.awt.image.BufferedImage;

class Util{

  protected static int[] blue(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = 0;
    colour[1] = 0;
    colour[2] = level;

    return colour;
  }

  protected static int[] green(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = 0;
    colour[1] = level;
    colour[2] = 0;

    return colour;
  }

  protected static int[] red(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = 0;
    colour[2] = 0;

    return colour;
  }

  protected static int[] white(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = level;
    colour[2] = level;

    return colour;
  }

  protected static int[] yellow(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = level;
    colour[2] = 0;

    return colour;
  }
  
  protected static char mapCoefficient(double coefficient){
    return (char)(coefficient * 10 + 77);
  }
  
  protected static double mapCoefficient(char coefficient){
    return (double)(((int)coefficient - 77) * 0.1);
  }

  protected static rgba[][] mapDensities(rgba[][] histo){
    double maxAlpha, scale;
    int count;
    rgba[][] mappedImage;

    count = 0;
    maxAlpha = -1;
    mappedImage = new rgba[histo.length][histo[0].length];
    for (int x=0;x<histo.length;x++){
      for (int y=0;y<histo[x].length;y++){
        maxAlpha = Math.max(maxAlpha, histo[x][y].a);
        mappedImage[x][y] = new rgba();
      }
    }
    for (int x=0;x<histo.length;x++){
      for (int y=0;y<histo[x].length;y++){
        if (histo[x][y].a > 0){
          count++;
          scale = (maxAlpha == 1) ? 1 : Math.log((double)(histo[x][y].a))/Math.log(maxAlpha);
          mappedImage[x][y].r = (short)(histo[x][y].r * scale);
          mappedImage[x][y].g = (short)(histo[x][y].g * scale);
          mappedImage[x][y].b = (short)(histo[x][y].b * scale);
        }
      }
    }

    return mappedImage;
  }

  protected static void writeImage(rgba[][] wI, String fN){
    int colour;
    BufferedImage bI;
    java.io.File outFile;
    rgba pixel;

    bI = new BufferedImage(wI.length,wI[0].length,BufferedImage.TYPE_INT_RGB);
    for (int i=0;i<wI.length;i++){
      for (int j=0;j<wI[i].length;j++){
        pixel = wI[i][j];
        colour = (new Color(pixel.r,pixel.g,pixel.b)).getRGB();
        bI.setRGB(i,j,colour);
      }
    }

    try{
      outFile = new java.io.File("C:\\TEMP\\" + fN + ".bmp");
      javax.imageio.ImageIO.write(bI,"bmp",outFile);
    }
    catch(Exception e){
      System.out.println("Exception in SuperSample2.writeImage");
      System.out.println(e.getMessage());
    }
  }
}