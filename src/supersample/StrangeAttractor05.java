package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class StrangeAttractor05 extends JPanel implements Runnable{
  byte superFactor;
  double xDelta, xMax, xMin, yDelta, yMax, yMin;
  int height, width;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgba[][] finalImage, superSample;
  
  public StrangeAttractor05(int w, int h, int gW, int gH, int sF){
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
  
  private double derivative(double x, double[] A){
    double xPrime;
    
    xPrime = A[1] + 2*A[2]*x + 3*A[3]*Math.pow(x,2) + 4*A[4]*Math.pow(x,3) + 5*A[5]*Math.pow(x,4);
    
    return xPrime;
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
    xMax = 2.1316423550985717 * 1.1;
    xMin = -1.7380224586026545 * 1.1;
    yMax = xMax;
    yMin = xMin;
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
    double maxx, minx;
    int counter, modNum, numIterations, numPrev;
    int[][] colours;

    numIterations = 100000;
    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    A = new double[6];
    A[0] = Util.mapCoefficient('O');
    A[1] = Util.mapCoefficient('O');
    A[2] = Util.mapCoefficient('Y');
    A[3] = Util.mapCoefficient('R');
    A[4] = Util.mapCoefficient('I');
    A[5] = Util.mapCoefficient('L');
    numPrev = 1;
    modNum = numPrev + 1;
    funcValues = new double[numPrev+1];
    counter = 0;
    ln2 = Math.log(2);
    funcValues[0] = 0.5;
    counter = 0;
    minx = 10000000000d;
    maxx = -minx;
    xFunc = funcValues[0];
    lsum = Math.log(Math.abs(derivative(xFunc,A)))/ln2;
    lyapunov = 0;
    
    for (int j=0;j<numIterations;j++){
      xFunc = A[0] + xFunc * (A[1] + xFunc * (A[2] + xFunc * (A[3] + xFunc * (A[4] + xFunc * (A[5])))));
      lsum += Math.log(Math.abs(derivative(xFunc,A)))/ln2;
      lyapunov = lsum/j;
      plot(sS,funcValues[(counter + 1) % modNum],xFunc,colours[0]);
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
    System.out.println("lyapunov: " + lyapunov);
    System.out.println("min :" + minx + " & max: " + maxx);
    
    return sS;
  }

  @Override
  public void run(){
  }
}