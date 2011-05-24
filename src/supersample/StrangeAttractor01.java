package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class StrangeAttractor01 extends JPanel implements Runnable{
  byte superFactor;
  int height, width;
  int[] black, green, red;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgba[][] finalImage, superSample;
  
  public StrangeAttractor01(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
    superSample = renderImage(superSample);
    superSample = Util.mapDensities(superSample);
    superSample = AntiAlias.gaussBlur(superSample,blurKernel);
    finalImage = AntiAlias.downSample(superSample,sampleKernel,superFactor);
    bufferedImage = convertImage(bufferedImage,finalImage);
  }

  private BufferedImage convertImage(BufferedImage bI, rgba[][] sS){
    int colour;
    rgba pixel;

    for (int x=0;x<bI.getWidth();x++){
      for (int y=0;y<bI.getHeight();y++){
        pixel = sS[x][y];
        colour = (new Color(pixel.r,pixel.g,pixel.b)).getRGB();
        bI.setRGB(x,y,colour);
      }
    }
    return bI;
  }
  
  private void init(int w, int h, int gW, int gH, int sF){
    height = h;
    width = w;
    superFactor = (byte)sF;
    blurKernel = new GaussKernel(gW,gH);
    sampleKernel = new GaussKernel(superFactor,superFactor);
    finalImage = new rgba[width][height];
    superSample = new rgba[width*superFactor][height*superFactor];
    for (int x=0;x<superSample.length;x++){
      for (int y=0;y<superSample[x].length;y++){
        superSample[x][y] = new rgba();
      }
    }
    bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    black = Util.white(0);
    green = Util.green(255);
    red = Util.red(255);
  }

  @Override
  public void paint(Graphics g){
    Graphics2D g2;

    g2 = (Graphics2D)g;
    g2.drawImage(bufferedImage,null,0,0);
  }

  private rgba[][] renderImage(rgba[][] sS){
    double xDelta, xFunc, xMax, xMin;
    double yDelta, yFunc, yMax, yMin;
    double average, chooser, delX, lyapunov;
    int xGrid, yGrid;

    xMax = 1;
    xMin = 0;
    yMax = 4;
    yMin = 2;
    xDelta = (sS.length - 1)/(xMax - xMin);
    yDelta = (sS[0].length - 1)/(yMax - yMin);
    xGrid = 0;
    yGrid = 0;

    for (double j=yMin;j<=yMax;j+=0.001){
      xFunc = 0.25;
      yFunc = j;
      lyapunov = 0;
      average = 0;
      for (int k=0;k<100;k++){
        xFunc = yFunc * xFunc * (1 - xFunc);
        xGrid = (int)((xFunc - xMin) * xDelta);
        if (xFunc < xMax &&
            xFunc > xMin &&
            yFunc < yMax &&
            yFunc > yMin   ){
          yGrid = (int)(sS[xGrid].length - (yFunc - yMin) * yDelta);
          sS[xGrid][yGrid].addColour(red);
        }
        delX = j * (1 - 2 * xFunc);
        if(k == 0){
          average = Math.abs(delX);
        }
        else{
          average = (average * (j-1) + Math.abs(delX))/j;
        }
        chooser = Math.random();
        if (chooser < 0.75){
          xFunc = Math.sin(xFunc + Math.PI);
          xGrid = (int)((xFunc - xMin) * xDelta);
          if (xFunc < xMax &&
              xFunc > xMin &&
              yFunc < yMax &&
              yFunc > yMin   ){
            yGrid = (int)(sS[xGrid].length - (yFunc - yMin) * yDelta);
            sS[xGrid][yGrid].addColour(green);
          }
        }
      }
      //lyapunov = lyapunov/(Math.log(12000)/Math.log(2));
      lyapunov = Math.log(average);
      xGrid = (int)((lyapunov - xMin) * xDelta);
      if (lyapunov < xMax &&
          lyapunov > xMin &&
          yFunc    < yMax &&
          yFunc    > yMin   ){
        sS[xGrid][yGrid].addColour(green);
      }
    }

    return sS;
  }

  @Override
  public void run(){
  }
}