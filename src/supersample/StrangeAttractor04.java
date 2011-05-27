package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class StrangeAttractor04 extends JPanel implements Runnable{
  byte superFactor;
  double xDelta, xMax, xMin, yDelta, yMax, yMin;
  int height, width;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgba[][] finalImage, superSample;
  
  public StrangeAttractor04(int w, int h, int gW, int gH, int sF){
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
    xMax = 1.3;
    xMin = -2.0;
    yMax = 1.3;
    yMin = -2.0;
    xDelta = (superSample.length - 1)/(xMax - xMin);
    yDelta = (superSample[0].length - 1)/(yMax - yMin);
  }

  @Override
  public void paint(Graphics g){
    Graphics2D g2;

    g2 = (Graphics2D)g;
    g2.drawImage(bufferedImage,null,0,0);
  }
  
  private rgba[][] plot(rgba[][] sS, double xFunc, double yFunc, int[] colour){
    int xGrid, yGrid;
    xGrid = (int)((xFunc - xMin) * xDelta);
    if (xFunc < xMax &&
        xFunc > xMin &&
        yFunc < yMax &&
        yFunc > yMin   ){
      yGrid = (int)(sS[xGrid].length - (yFunc - yMin) * yDelta);
      sS[xGrid][yGrid].addColour(colour);
    }
    return sS;
  }

  private rgba[][] renderImage(rgba[][] sS){
    double ln2, lsum, lyapunov, xFunc;
    double[] A, funcValues;
    double maxx, minx, maxCo, minCo, incCo;
    int counter, modNum, numIterations, numPrev;
    int[][] colours;

    numIterations = 10;
    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    minCo = -5.0;
    maxCo = -minCo;
    incCo = 0.1;
    A = new double[3];
    numPrev = 5;
    modNum = numPrev + 1;
    funcValues = new double[numPrev+1];
    ln2 = Math.log(2);
    
    int rangeEnd = (int)((maxCo - minCo)/incCo + 1);
    for (int a = 0;a < rangeEnd;a++){
      A[0] = a * incCo + minCo;
      System.out.println("1: " + A[0]);
      for (double b = 0;b < rangeEnd;b++){
        A[1] = b * incCo + minCo;
        for (double c = 0;c < rangeEnd;c++){
          A[2] = c * incCo + minCo;
          counter = 0;
          funcValues[0] = 0.5;
          minx = 10000000000d;
          maxx = -minx;
          xFunc = funcValues[0];
          lsum = Math.log(Math.abs(2 * A[2] * xFunc + A[1]))/ln2;
          lyapunov = 0;
          
          for (int j=0;j<numIterations;j++){
            xFunc = A[0] + xFunc * (A[1] + A[2] * xFunc);
            lsum += Math.log(Math.abs(2 * A[2] * xFunc + A[1]))/ln2;
            lyapunov = lsum/j;
            //plot(sS,funcValues[(counter + 1) % modNum],xFunc,colours[0]);
            //plot(sS,xFunc,lyapunov,colours[1]);
            counter++;
            funcValues[counter % modNum] = xFunc;
            if (xFunc > maxx){
              maxx = xFunc;
            }
            if (xFunc < minx){
              minx = xFunc;
            }
          }
          if (lyapunov > 0 && maxx < 10000000d && minx > -10000000d){
            //System.out.println("lyapunov: " + lyapunov);
            //System.out.println("min :" + minx + " & max: " + maxx);
            //System.out.println(A[0] + " : " + A[1] + " : " + A[2]);
          }
        }
      }
    }
    return sS;
  }

  @Override
  public void run(){
  }
}