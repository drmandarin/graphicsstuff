package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static supersample.AntiAlias.downSample;
import static supersample.AntiAlias.gaussBlur;
import static supersample.Util.fmt;
import static supersample.Util.mapDensities;

public class StrangeAttractor05 extends JPanel implements Runnable{
  private byte superFactor;
  private double xDelta, yDelta;
  private int numIterations, numPrev, height, width;
  private double[] A, lyapunov, max, min;
  private BufferedImage bufferedImage;
  private GaussKernel blurKernel, sampleKernel;
  private String codeString;
  private rgba[][] finalImage, superSample;
  
  public StrangeAttractor05(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
  }
  
  private double[] calcDerivative(double func[], double[] A){
    double dx, dy, x, y;
    double xPrime, yPrime;
    double[] funcPrime;
    
    x = func[0];
    xPrime = 0d;
    yPrime = 0d;
    funcPrime = new double[func.length];
    for (int i = 0;i < funcPrime.length;i++)
      funcPrime[i] = 0d;
    switch(codeString.charAt(0)){
      case 'E':
        y = func[1];
        dx = A[1] + 2 * A[2] * x + A[3] * y;
        dy = A[3] * x + A[4] + 2 * A[5] * y;
        xPrime = sqrt(dx * dx + dy * dy);
        dx = A[7] + 2 * A[8] * x + A[9] * y;
        dy = A[9] * x + A[10] + 2 * A[11] * y;
        yPrime = sqrt(dx * dx + dy * dy);
        break;
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
      case 2: funcPrime[1] = yPrime;
      case 1: funcPrime[0] = xPrime;
              break;
    }
    
    return funcPrime;
  }
  
  protected void findAttractors(String codeString){
    String testString;
    
    switch(codeString.charAt(0)){
      case 'E':
        for (int a0 = 32;a0 < 128;a0++){
          testString = "E";
          for (int k = 0;k < 12;k++){
            testString += (char)a0;
          }
          testCode(testString);
        }
        break;
      case 'D':
        for (int a0 = 32;a0 < 128;a0++){
          testString = "DOOYRI";
          //for (int k = 0;k < 6;k++){
            testString += (char)a0;
          //}
          testCode(testString);
        }
        break;
    }
    testCode(codeString);
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
  
  public double[] getLyapunov(){
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
                y += A[10] * yN;
                y += A[9] * xN * yN;
                y += A[8] * pow(xN,2);
                y += A[7] * xN;
                y += A[6];
                x += A[5] * pow(yN,2);
                x += A[4] * yN;
                x += A[3] * xN * yN;
                x += A[2] * pow(xN,2);
                x += A[1] * xN;
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
    if (xGrid < sS.length && xGrid >= 0){
      yGrid = (int)(sS[xGrid].length - (yFunc - min[1]) * yDelta);
      if (yGrid < sS[xGrid].length && yGrid >= 0){
        sS[xGrid][yGrid].addColour(colour);
      }
    }
    return sS;
  }

  private rgba[][] renderImage(rgba[][] sS){
    double ln2;
    double[] funcHistory, funcVals, lsum, max, min;
    int counter, modNum;
    int[][] colours;

    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    modNum = numPrev + 1;
    funcHistory = new double[numPrev+1];
    counter = 0;
    ln2 = log(2);
    funcHistory[0] = 0.5;
    counter = 0;
    
    switch(codeString.charAt(0)){
      case 'E':
        lsum = new double[2];
        lyapunov = new double[2];
        lyapunov[0] = 0d;
        lyapunov[1] = 0d;
        max = new double[2];
        min = new double[2];
        max[0] = -10000000000d;
        max[1] = max[0];
        min[0] = -max[0];
        min[1] = -max[1];
        funcVals = new double[2];
        funcVals[0] = 0.5d;
        funcVals[1] = 0.5d;
        lsum[0] = log(abs(calcDerivative(funcVals,A)[0]))/ln2;
        lsum[1] = log(abs(calcDerivative(funcVals,A)[1]))/ln2;
        for (int j = 0;j < numIterations;j++){
          funcVals = iterateFunc(funcVals,A);
          plot(sS,funcVals[0],funcVals[1],colours[0]);
          lsum[0] += log(calcDerivative(funcVals,A)[0])/ln2;
          lsum[1] += log(calcDerivative(funcVals,A)[1])/ln2;
          lyapunov[0] = lsum[0]/j;
          lyapunov[1] = lsum[1]/j;
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
      case 'D': ;
      case 'C': ;
      case 'B': ;
      case 'A':
        lsum = new double[1];
        lyapunov = new double[1];
        lyapunov[0] = 0;
        max = new double[2];
        min = new double[2];
        max[0] = -10000000000d;
        max[1] = max[0];
        min[0] = -max[0];
        min[1] = min[0];
        funcVals = new double[1];
        funcVals[0] = 0.5;
        lsum[0] = log(abs(calcDerivative(funcVals,A)[0]))/ln2;
        for (int j = 0;j < numIterations;j++){
          funcVals = iterateFunc(funcVals,A);
          lsum[0] += log(abs(calcDerivative(funcVals,A)[0]))/ln2;
          lyapunov[0] = lsum[0]/j;
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
    System.out.println("lyapunov:");
    for (int i = 0;i < lyapunov.length;i++){
      System.out.println(fmt(lyapunov[i]));
    }
    for (int i = 0;i < min.length;i++){
      System.out.println("min :" + fmt(min[i]) + " & max: " + fmt(max[i]));
    }
    
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
    double[] funcVals, range;
    
    switch(codeString.charAt(0)){
      case 'A': ;
      case 'B': ;
      case 'C': ;
      case 'D': funcVals = new double[1];
                range = new double[2];
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
                max[1] = max[0];
                min[1] = min[0];
                break;
      case 'E': ;
      case 'F': ;
      case 'G': ;
      case 'H': funcVals = new double[2];
                range = new double[2];
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
               range = new double[2];
               max = null;
               min = null;
               break;
    }
    range[0] = max[0] - min[0];
    range[1] = max[1] - min[1];
    max[0] = max[0] + 0.05 * range[0];
    min[0] = min[0] - 0.05 * range[0];
    max[1] = max[1] + 0.05 * range[1];
    min[1] = min[1] - 0.05 * range[1];
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
  
  private void testCode(String testString){
    double ln2, lsum;
    int j;
    double[] funcVals;
    Double x, y;
    
    switch(testString.charAt(0)){
      case 'E': 
        A = Util.decodeString(testString);
        funcVals = new double[2];
        funcVals[0] = 0.5d;
        funcVals[1] = 0.5d;
        x = new Double(funcVals[0]);
        y = new Double(funcVals[1]);
        j = 0;
        while((j < numIterations) && !(x.isInfinite()) && !(x.isNaN()) && !(y.isInfinite()) && !(y.isNaN())){
          funcVals = iterateFunc(funcVals,A);
          x = new Double(funcVals[0]);
          y = new Double(funcVals[1]);
          j++;
        }
        break;
      case 'D': ;
      case 'C': ;
      case 'A':
        A = Util.decodeString(testString);
        funcVals = new double[1];
        funcVals[0] = 0.5d;
        ln2 = log(2);
        lsum = log(abs(calcDerivative(funcVals,A)[0]))/ln2;
        x = new Double(funcVals[0]);
        j = 0;
        while((j < numIterations) && !(x.isInfinite()) && !(x.isNaN())){
          funcVals = iterateFunc(funcVals,A);
          x = new Double(funcVals[0]);
          lsum += log(abs(calcDerivative(funcVals,A)[0]))/ln2;
          lyapunov[0] = lsum/j;
          j++;
        }
        break;
      default:
        j = 0;
        funcVals = null;
        break;
    }
    if (j == numIterations && lyapunov[0] > 0){
      System.out.println("found");
      System.out.println(testString);
      //System.out.println(funcVals[0]);
      //System.out.println(funcVals[1]);
    }
  }
}