package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static java.lang.Math.abs;
import static java.lang.Math.log;
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
  
  private double[] calcDerivative(double func[], double[] A){
    double x;
    double xPrime;
    double[] funcPrime;
    
    x = func[0];
    xPrime = 0d;
    funcPrime = new double[func.length];
    for (int i = 0;i < funcPrime.length;i++)
      funcPrime[i] = 0d;
    switch(codeString.charAt(0)){
      case 'D': xPrime += 5 * A[5] * pow(x,4);
      case 'C': xPrime += 4 * A[4] * pow(x,3);
      case 'B': xPrime += 3 * A[3] * pow(x,2);
      case 'A': xPrime += 2 * A[2] * x;
                xPrime += A[1];
                break;
      default:  break;
    }
    switch(funcPrime.length){
      case 3: ;//funcPrime[2] = zPrime;
      case 2: ;//funcPrime[1] = yPrime;
      case 1: funcPrime[0] = xPrime;
              break;
    }
    
    return funcPrime;
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
  
  private double[] iterateFunc(double[] funcN, double[] A){
    double x, y, z;
    double xN, yN, zN;
    double[] func;
    
    x = 0d; xN = 0d;
    y = 0d; yN = 0d;
    z = 0d; zN = 0d;
    func = new double[funcN.length];
    switch(func.length){
      case 3: zN = funcN[2];
      case 2: yN = funcN[1];
      case 1: xN = funcN[0];
              break;
      default: break;
    }
    switch(codeString.charAt(0)){
      case 'E': y += A[11] * pow(yN,2);
                y += A[10] * y;
                y += A[9] * x * y;
                y += A[8] * pow(xN,2);
                y += A[7] * x;
                y += A[6];
                x += A[5] * pow(yN,2);
                x += A[4] * y;
                x += A[3] * x * y;
                x += A[2] * pow(xN,2);
                x += A[1] * x;
                x += A[0];
                break;
      case 'D': x += A[5] * pow(xN,5);
      case 'C': x += A[4] * pow(xN,4);
      case 'B': x += A[3] * pow(xN,3);
      case 'A': x += A[2] * pow(xN,2);
                x += A[1] * xN;
                x += A[0];
                break;
      default:  break;
    }
    switch(func.length){
      case 3: func[2] = z;
      case 2: func[1] = y;
      case 1: func[0] = x;
              break;
    }
    return func;
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
    yGrid = (int)(sS[xGrid].length - (yFunc - min[1]) * yDelta);
    if (xGrid < sS.length    &&
        xGrid > 0            &&
        yFunc < sS[0].length &&
        yFunc > 0 ){
      sS[xGrid][yGrid].addColour(colour);
      System.out.println("here");
    }
    return sS;
  }

  private rgba[][] renderImage(rgba[][] sS){
    double ln2, lsum, maxx, maxy, minx, miny;
    double[] funcHistory, funcVals;//, max, min;
    int counter, modNum;
    int[][] colours;

    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    modNum = numPrev + 1;
    funcHistory = new double[numPrev+1];
    counter = 0;
    ln2 = Math.log(2);
    funcHistory[0] = 0.5;
    counter = 0;
    lyapunov = 0;
    
    maxx = 0d;
    minx = 0d;
    
    switch(codeString.charAt(0)){
      case 'E': //max = new double[2];
                //min = new double[2];
                //max[0] = -10000000000d;
                //max[1] = max[0];
                //min[0] = -max[0];
                //min[1] = -max[1];
                maxx = -10000000000d;
                maxy = -10000000000d;
                minx = 10000000000d;
                miny = 10000000000d;
                funcVals = new double[2];
                funcVals[0] = 0.5d;
                funcVals[1] = 0.5d;
                for (int j = 0;j < numIterations;j++){
                  funcVals = iterateFunc(funcVals,A);
                  plot(sS,funcVals[0],funcVals[1],colours[0]);
                  if (funcVals[0] > maxx){
                    maxx = funcVals[0];
                  }
                  if (funcVals[0] < minx){
                    minx = funcVals[0];
                  }
                  if (funcVals[1] > maxy){
                    maxy = funcVals[1];
                  }
                  if (funcVals[1] < miny){
                    miny = funcVals[1];
                  }
                }
                break;
      case 'D': ;
      case 'C': ;
      case 'B': ;
      case 'A': max = new double[2];
                min = new double[2];
                max[0] = -10000000000d;
                max[1] = max[0];
                min[0] = -max[0];
                min[1] = min[0];
                funcVals = new double[1];
                funcVals[0] = funcHistory[0];
                lsum = log(abs(calcDerivative(funcVals,A)[0]))/ln2;
                for (int j = 0;j < numIterations;j++){
                  funcVals = iterateFunc(funcVals,A);
                  lsum += log(abs(calcDerivative(funcVals,A)[0]))/ln2;
                  lyapunov = lsum/j;
                  plot(sS,funcHistory[(counter + 1) % modNum],funcVals[0],colours[0]);
                  counter++;
                  funcHistory[counter % modNum] = funcVals[0];
                  if (funcVals[0] > max[0]){
                    max[0] = funcVals[0];
                  }
                  if (funcVals[0] < min[0]){
                    min[0] = funcVals[0];
                  }
                }
                break;
      default: max = null;
               min = null;
               break;
    }
    System.out.println("lyapunov: " + lyapunov);
    System.out.println("min :" + minx + " & max: " + maxx);
    
    return sS;
  }

  @Override
  public void run(){
    searchMaxMin();
    System.out.println("min: " + min[0] + ":" + min[1]);
    System.out.println("max: " + max[0] + ":" + max[1]);
    superSample = renderImage(superSample);
    superSample = mapDensities(superSample);
    superSample = gaussBlur(superSample,blurKernel);
    finalImage = downSample(superSample,sampleKernel,superFactor);
    bufferedImage = convertImage(bufferedImage,finalImage);
  }
  
  public void searchMaxMin(){
    double[] funcVals;
    
    switch(codeString.charAt(0)){
      case 'A': ;
      case 'B': ;
      case 'C': ;
      case 'D': funcVals = new double[1];
                funcVals[0] = 0.5;
                max = new double[2];
                min = new double[2];
                max[0] = -10000000000d;
                max[1] = max[0];
                min[0] = -max[0];
                min[1] = -max[1];
                for (int j=0;j<numIterations;j++){
                  funcVals = iterateFunc(funcVals,A);
                  if (funcVals[0] > max[0]){
                    max[0] = funcVals[0];
                  }
                  if (funcVals[0] < min[0]){
                    min[0] = funcVals[0];
                  }
                }
                break;
      case 'E': ;
      case 'F': ;
      case 'G': ;
      case 'H': funcVals = new double[2];
                funcVals[0] = 0.5;
                funcVals[1] = 0.5;
                max = new double[2];
                min = new double[2];
                max[0] = -10000000000d;
                max[1] = max[0];
                min[0] = -max[0];
                min[1] = -max[1];
                for (int j = 0;j < numIterations;j++){
                  funcVals = iterateFunc(funcVals,A);
                  if (funcVals[0] > max[0]){
                    max[0] = funcVals[0];
                  }
                  if (funcVals[0] < min[0]){
                    min[0] = funcVals[0];
                  }
                  if (funcVals[1] > max[1]){
                    max[1] = funcVals[1];
                  }
                  if (funcVals[1] < min[1]){
                    min[1] = funcVals[1];
                  }
                }
                break;
      default: funcVals = null;
               max = null;
               min = null;
               break;
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
    A = Util.decodeString(codeString);
  }
  
  public void setIterations(int numIterations){
    this.numIterations = numIterations;
  }
  
  public void setPrev(int numPrev){
    this.numPrev = numPrev;
  }
}