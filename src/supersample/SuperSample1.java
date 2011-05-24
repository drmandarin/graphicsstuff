package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class SuperSample1 extends JPanel{
  byte radiusX, radiusY, scaleX, scaleY;
  int height, width;
  BufferedImage bI;
  GaussKernel gaussKernel;
  rgb[][] superSample;

  public SuperSample1(int w, int h, byte x, byte y){
    height = h;
    width = w;
    radiusX = x;
    radiusY = y;
    scaleX = (byte)(2*radiusX + 1);
    scaleY = (byte)(2*radiusY + 1);
    init();
    superSample = createImage(superSample);
    drawImage(bI,downSample(gaussKernel, superSample));
    //drawImage(bI,superSample);
  }

  private void init(){
    gaussKernel = new GaussKernel(radiusX,radiusY);
    superSample = new rgb[width*scaleX][height*scaleY];
    for (int x=0;x<superSample.length;x++){
      for (int y=0;y<superSample[x].length;y++){
        superSample[x][y] = new rgb();
      }
    }
    bI = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    //bI = new BufferedImage(width*scaleX,height*scaleY,BufferedImage.TYPE_INT_RGB);
  }

  private rgb[][] createImage(rgb[][] sS){
    /*
    for (int x=0;x<sS.length;x++){
      for (int y=0;y<sS[x].length;y++){
        if (Math.abs(x-y)<1){
          sS[x][y].r = 255;
        }
        if (x > 380 && x < 400){
          sS[x][y].g = 255;
        }
        if (y > 400 && y < 420){
          sS[x][y].g = 255;
        }
        //sS[x][y].r = (short)((x+y)%256);
        //sS[x][y].g = (short)((x&y)%256);
        //sS[x][y].b = (short)((x^y)%256);
      }
    }
    */
    for (int x=0;x<(sS.length/3);x++){
      for (int y=0;y<400;y++){
        sS[x][y].r = 255;
      }
    }
    for (int x=(sS.length/3);x<(2*sS.length/3);x++){
      for (int y=0;y<400;y++){
        sS[x][y].g = 255;
      }
    }
    for (int x=(2*sS.length/3);x<sS.length;x++){
      for (int y=0;y<400;y++){
        sS[x][y].b = 255;
      }
    }
    for (int x=0;x<sS.length;x++){
      for (int y=400;y<sS[x].length;y++){
        sS[x][y].r = (short)(x % 256);
        sS[x][y].b = (short)((x + 23) % 256);
        sS[x][y].b = (short)((x + 67) % 256);
      }
    }
    writeImage(sS,"image01");
    return sS;
  }

  private rgb[][] downSample(GaussKernel gK, rgb[][] sS){
    rgb[][] scaledSample;

    scaledSample = new rgb[width][height];

    for (int x=0;x<scaledSample.length;x++){
      for (int y=0;y<scaledSample[x].length;y++){
        scaledSample[x][y] = getSample(x, y, gK, sS);
      }
    }
    return scaledSample;
  }

  private void drawImage(BufferedImage bI, rgb[][] dS){
    int colour;
    rgb pixel;

    for (int x=0;x<dS.length;x++){
      for (int y=0;y<dS[x].length;y++){
        pixel = dS[x][y];
        colour = (new Color(pixel.r,pixel.g,pixel.b)).getRGB();
        bI.setRGB(x,y,colour);
      }
    }
  }

  private rgb getSample(int x, int y, GaussKernel gK, rgb[][] sS){
    int gkX, gkY, startX, startY, endX, endY;
    double[] sampler;
    rgb pixelSample;

    startX = x * scaleX;
    startY = y * scaleY;
    endX = startX + scaleX;
    endY = startY + scaleY;
    pixelSample = new rgb();
    sampler = new double[3];
    sampler[0] = 0;
    sampler[1] = 0;
    sampler[2] = 0;

    for (int i=startX;i<endX;i++){
      for (int j=startY;j<endY;j++){
        gkX = i % scaleX;
        gkY = j % scaleY;
        sampler[0] += sS[i][j].r * gK.array[gkX][gkY];
        sampler[1] += sS[i][j].g * gK.array[gkX][gkY];
        sampler[2] += sS[i][j].b * gK.array[gkX][gkY];
      }
    }
    pixelSample.r = (short)(sampler[0]/gK.sum);
    pixelSample.g = (short)(sampler[1]/gK.sum);
    pixelSample.b = (short)(sampler[2]/gK.sum);
    return pixelSample;
  }

  public void paint(Graphics g){
    Graphics2D g2;

    g2 = (Graphics2D)g;
    g2.drawImage(bI,null,0,0);
  }

    public void writeImage(rgb[][] wI, String fN){
    int colour;
    BufferedImage bI;
    java.io.File outFile;
    rgb pixel;

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