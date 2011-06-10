package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static java.lang.Math.pow;
import static supersample.AntiAlias.downSample;
import static supersample.AntiAlias.gaussBlur;
import static supersample.Util.mapDensities;

public class StrangeAttractor05 extends JPanel implements Runnable{
  private byte superFactor;
  private double lyapunov, xDelta, yDelta;
  private int numIterations, numPrev, height, width;
  private double[] A, max, min;
  private BufferedImage bufferedImage;
  private GaussKernel blurKernel, sampleKernel;
  private String codeString;
  private rgba[][] finalImage, superSample;
  
  public StrangeAttractor05(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
  }
  
  private double calcDerivative(double x, double[] A){
    double xPrime;
    
    xPrime = 0d;
    switch(codeString.charAt(0)){
      case 'D': xPrime += 5 * A[5] * pow(x,4);
      case 'C': xPrime += 4 * A[4] * pow(x,3);
      case 'B': xPrime += 3 * A[3] * pow(x,2);
      case 'A': xPrime += 2 * A[2] * x;
                xPrime += A[1];
                break;
      default:  break;
    }
    
    return xPrime;
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
  
  public double getLyapunov(){
    return lyapunov;
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
  }
  
  private double iterateFunc(double xN, double[] A){
    double x;
    
    x = 0d;
    switch(codeString.charAt(0)){
      case 'D': x += A[5] * pow(xN,5);
      case 'C': x += A[4] * pow(xN,4);
      case 'B': x += A[3] * pow(xN,3);
      case 'A': x += A[2] * pow(xN,2);
                x += A[1] * xN;
                x += A[0];
                break;
      default:  break;
    }
    return x;
  }

  @Override
  public void paint(Graphics g){
    Graphics2D g2;

    g2 = (Graphics2D)g;
    g2.drawImage(bufferedImage,null,0,0);
  }
  
  private rgba[][] plot(rgba[][] sS, double xFunc, double yFunc, int[] colour){
    int xGrid, yGrid;
    xGrid = (int)((xFunc - min[0]) * xDelta);
    if (xFunc < max[0] &&
        xFunc > min[0] &&
        yFunc < max[1] &&
        yFunc > min[1]   ){
      yGrid = (int)(sS[xGrid].length - (yFunc - min[1]) * yDelta);
      sS[xGrid][yGrid].addColour(colour);
    }
    return sS;
  }

  private rgba[][] renderImage(rgba[][] sS){
    double ln2, lsum, xFunc;
    double[] funcValues;
    double maxx, minx;
    int counter, modNum;
    int[][] colours;

    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    modNum = numPrev + 1;
    funcValues = new double[numPrev+1];
    counter = 0;
    ln2 = Math.log(2);
    funcValues[0] = 0.5;
    counter = 0;
    minx = 10000000000d;
    maxx = -minx;
    xFunc = funcValues[0];
    lsum = Math.log(Math.abs(calcDerivative(xFunc,A)))/ln2;
    lyapunov = 0;
    
    for (int j=0;j<numIterations;j++){
      xFunc = iterateFunc(xFunc,A);
      lsum += Math.log(Math.abs(calcDerivative(xFunc,A)))/ln2;
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
    searchMaxMin();
    superSample = renderImage(superSample);
    superSample = mapDensities(superSample);
    superSample = gaussBlur(superSample,blurKernel);
    finalImage = downSample(superSample,sampleKernel,superFactor);
    bufferedImage = convertImage(bufferedImage,finalImage);
  }
  
  public void searchMaxMin(){
    double xFunc;
    
    xFunc = 0.5;
    switch(codeString.charAt(0)){
      default: max = new double[2];
               min = new double[2];
               max[0] = -10000000000d;
               max[1] = max[0];
               min[0] = -max[0];
               min[1] = -max[1];
               break;
    }
    for (int j=0;j<numIterations/10;j++){
      xFunc = iterateFunc(xFunc,A);
      if (xFunc > max[0]){
        max[0] = xFunc;
      }
      if (xFunc < min[0]){
        min[0] = xFunc;
      }
    }
    max[0] = max[0] * 1.1;
    min[0] = min[0] * 1.1;
    max[1] = max[0];
    min[1] = min[0];
    xDelta = (superSample.length - 1)/(max[0] - min[0]);
    yDelta = (superSample[0].length - 1)/(max[1] - min[1]);
  }
  
  public void setCode(String codeString){
    this.codeString = new String(codeString);
    A = Util.decodeString(codeString.substring(1));
  }
  
  public void setIterations(int numIterations){
    this.numIterations = numIterations;
  }
  
  public void setPrev(int numPrev){
    this.numPrev = numPrev;
  }
}