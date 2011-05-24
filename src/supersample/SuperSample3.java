package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class SuperSample3 extends JPanel implements Runnable{
  byte superFactor;
  int height, width;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgb[][] finalImage, superSample;

  public SuperSample3(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
    superSample = renderImage(superSample);
    writeImage(superSample,"image01");
    superSample = gaussBlur(superSample,blurKernel);
    writeImage(superSample,"image02");
    finalImage = downSample(superSample,sampleKernel);
    writeImage(finalImage,"image03");
    bufferedImage = drawImage(bufferedImage,finalImage);
  }

  private void init(int w, int h, int gW, int gH, int sF){
    width = w;
    height = h;
    superFactor = (byte)sF;
    blurKernel = new GaussKernel(gW,gH);
    sampleKernel = new GaussKernel(superFactor,superFactor);
    finalImage = new rgb[width][height];
    superSample = new rgb[width*superFactor][height*superFactor];
    for (int x=0;x<superSample.length;x++){
      for (int y=0;y<superSample[x].length;y++){
        superSample[x][y] = new rgb();
      }
    }
    bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
  }
  
  private rgb[][] downSample(rgb[][] sS, GaussKernel sK){
    rgb[][] dS;

    dS = new rgb[width][height];
    for (int x=0;x<width;x++){
      for (int y=0;y<height;y++){
        dS[x][y] = getPixelSample(x,y,sS,sK);
      }
    }
    return dS;
  }
  
  private BufferedImage drawImage(BufferedImage bI, rgb[][] sS){
    int colour;
    rgb pixel;

    for (int x=0;x<bI.getWidth();x++){
      for (int y=0;y<bI.getHeight();y++){
        pixel = sS[x][y];
        colour = (new Color(pixel.r,pixel.g,pixel.b)).getRGB();
        bI.setRGB(x,y,colour);
      }
    }
    return bI;
  }
  
  private rgb[][] gaussBlur(rgb[][] sS, GaussKernel bK){
    rgb pixelSample;
    rgb[][] gB;

    gB = new rgb[sS.length][sS[0].length];
    for (int x=0;x<gB.length;x++){
      for (int y=0;y<gB[x].length;y++){
        pixelSample = getSample(x,y,sS,bK);
        gB[x][y] = pixelSample;
      }
    }
    return gB;
  }

  private rgb getPixelSample(int a, int b, rgb[][] sS, GaussKernel sK){
    int offsetX, offsetY;
    double[] sampler;
    rgb pixelSample;

    offsetX = a*superFactor;
    offsetY = b*superFactor;
    pixelSample = new rgb();
    sampler = new double[3];
    sampler[0] = 0d;
    sampler[1] = 0d;
    sampler[2] = 0d;
    for (int x=0;x<superFactor;x++){
      for (int y=0;y<superFactor;y++){
        sampler[0] += sS[x+offsetX][y+offsetY].r * sK.array[x][y];
        sampler[1] += sS[x+offsetX][y+offsetY].g * sK.array[x][y];
        sampler[2] += sS[x+offsetX][y+offsetY].b * sK.array[x][y];
      }
    }
    pixelSample.r = (short)(Math.round(sampler[0]/sK.sum));
    pixelSample.g = (short)(Math.round(sampler[1]/sK.sum));
    pixelSample.b = (short)(Math.round(sampler[2]/sK.sum));

    return pixelSample;
  }

  private rgb getSample(int a, int b, rgb[][] sS, GaussKernel gK){
    double samplerSum;
    int endX, endY, gX, gY, iniGY, radiusX, radiusY, startX, startY;
    double[] sampler;
    rgb pixelSample;

    pixelSample = new rgb();
    radiusX = gK.width/2;
    radiusY = gK.height/2;
    startX = (gK.width %2 == 0) ? (a-radiusX+1) : (a-radiusX);
    startY = (gK.height %2 == 0) ? (b-radiusY+1) : (b-radiusY);
    endX = a+radiusX;
    endY = b+radiusY;
    sampler = new double[3];
    sampler[0] = 0;
    sampler[1] = 0;
    sampler[2] = 0;
    if (a < radiusX              ||
        a >= sS.length - radiusX ||
        b < radiusY              ||
        b >= sS[a].length - radiusY   ){
      samplerSum = 0d;
      gX = (startX < 0) ? -startX : 0;
      iniGY = (b < radiusY) ? -startY : 0;
      gY = iniGY;
      startX = Math.max(0,startX);
      startY = Math.max(0,startY);
      endX = Math.min(sS.length-1,endX);
      endY = Math.min(sS[a].length-1,endY);
      for (int x=startX;x<=endX;x++){
        for (int y=startY;y<=endY;y++){
          sampler[0] += sS[x][y].r * gK.array[gX][gY];
          sampler[1] += sS[x][y].g * gK.array[gX][gY];
          sampler[2] += sS[x][y].b * gK.array[gX][gY];
          samplerSum += gK.array[gX][gY];
          gY++;
        }
        gY = iniGY;
        gX++;
      }
    }
    else{
      samplerSum = gK.sum;
      gX = 0;
      gY = 0;
      for (int x=startX;x<=endX;x++){
        for (int y=startY;y<=endY;y++){
          sampler[0] += sS[x][y].r * gK.array[gX][gY];
          sampler[1] += sS[x][y].g * gK.array[gX][gY];
          sampler[2] += sS[x][y].b * gK.array[gX][gY];
          gY++;
        }
        gY = 0;
        gX++;
      }
    }
    pixelSample.r = (short)(Math.round(sampler[0]/samplerSum));
    pixelSample.g = (short)(Math.round(sampler[1]/samplerSum));
    pixelSample.b = (short)(Math.round(sampler[2]/samplerSum));
    return pixelSample;
  }
  
  @Override
  public void paint(Graphics g){
    Graphics2D g2;
    
    g2 = (Graphics2D)g;
    g2.drawImage(bufferedImage,null,0,0);
  }

  private rgb[][] renderImage(rgb[][] sS){
    for (int x=0;x<sS.length;x++){
      for (int y=0;y<sS[x].length;y++){
        sS[x][y] = new rgb((x+y)%256,Math.abs((x-y)%256),(x^y)%256);
      }
    }
    return sS;
  }

  @Override
  public void run(){
    int counter = 0;
    while (counter < 100){
      try{
        Thread.sleep(500);
        System.out.println(counter);
      }
      catch(Exception e){
      }
      counter++;
    }
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