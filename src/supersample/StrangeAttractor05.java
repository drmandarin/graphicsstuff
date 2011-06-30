package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static supersample.AntiAlias.downSample;
import static supersample.AntiAlias.gaussBlur;
import static supersample.Util.clearGrid;
import static supersample.Util.fmt;
import static supersample.Util.getLastCode;
import static supersample.Util.mapDensities;
import static supersample.Util.rotateGrid;
import static supersample.Util.untestedCode;
import static supersample.Util.writeImage;
import static supersample.Util.writeString;

public class StrangeAttractor05 extends JPanel implements Runnable{
  protected boolean rendering;
  private byte superFactor;
  private double xDelta, yDelta;
  private int numIterations, numPrev, height, width;
  private double[] A, lyapunov, max, min;
  private BufferedImage bufferedImage;
  private GaussKernel blurKernel, sampleKernel;
  private String codeString;
  private BufferedImage[] frames;
  private tuple[] dataPoints;
  private rgba[][] finalImage, superSample;
  
  public StrangeAttractor05(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
  }
  
  private double[] calcDerivative(double func[], double[] A){
    double dx, dy, dz, x, y, z, x2, x3, x4, y2, y3, y4;
    double xPrime, yPrime, zPrime;
    double[] funcPrime;
    
    x = func[0];
    xPrime = 0d;
    yPrime = 0d;
    zPrime = 0d;
    funcPrime = new double[func.length];
    for (int i = 0;i < funcPrime.length;i++)
      funcPrime[i] = 0d;
    switch(codeString.charAt(0)){
      case 'I':
        y = func[1];
        z = func[2];
        dx = A[1] + 2*A[2]*x + A[3]*y + A[4]*z;
        dy = A[3]*x + A[5] + 2*A[6]*y + A[7]*z;
        dz = A[4]*x + A[7]*y + A[8] + 2*A[9]*z;
        xPrime = sqrt(dx * dx + dy * dy + dz * dz);
        dx = A[11] + 2*A[12]*x + A[13]*y + A[14]*z;
        dy = A[13]*x + A[15] + 2*A[16]*y + A[17]*z;
        dz = A[14]*x + A[17]*y + A[18] + 2*A[19]*z;
        yPrime = sqrt(dx * dx + dy * dy + dz * dz);
        dx = A[21] + 2*A[22]*x + A[23]*y + A[24]*z;
        dy = A[23]*x + A[25] + 2*A[26]*y + A[27]*z;
        dz = A[24]*x + A[27]*y + A[28] + 2*A[29]*z;
        zPrime = sqrt(dx * dx + dy * dy + dz * dz);
        break;
      case 'H':
        x2 = pow(x,2);
        x3 = pow(x,3);
        x4 = pow(x,4);
        y = func[1];
        y2 = pow(y,2);
        y3 = pow(y,3);
        y4 = pow(y,4);
        dx = A[1] + 2*A[2]*x + 3*A[3]*x2 + 4*A[4]*x3 + 5*A[5]*x4 + 4*A[6]*x3*y
           + 3*A[7]*x2*y + 3*A[8]*x2*y2 + 2*A[9]*x*y + 2*A[10]*x*y2
           + 2*A[11]*x*y3 + A[12]*y + A[13]*y2 + A[14]*y3 + A[15]*y4;
        dy = A[6]*x4 + A[7]*x3 + 2*A[8]*x3*y + A[9]*x2 + 2*A[10]*x2*y
           + 3*A[11]*x2*y2 + A[12]*x + 2*A[13]*x*y + 3*A[14]*x*y2 + 4*A[15]*x*y3
           + A[16] + 2*A[17]*y + 3*A[18]*y2 + 4*A[19]*y3 + 5*A[20]*y4;
        xPrime = sqrt(dx * dx + dy * dy);
        dx = A[22] + 2*A[23]*x + 3*A[24]*x2 + 4*A[25]*x3 + 5*A[26]*x4 + 4*A[27]*x3*y
           + 3*A[28]*x2*y + 3*A[29]*x2*y2 + 2*A[30]*x*y + 2*A[31]*x*y2
           + 2*A[32]*x*y3 + A[33]*y + A[34]*y2 + A[35]*y3 + A[36]*y4;
        dy = A[27]*x4 + A[28]*x3 + 2*A[29]*x3*y + A[30]*x2 + 2*A[31]*x2*y
           + 3*A[32]*x2*y2 + A[33]*x + 2*A[34]*x*y + 3*A[35]*x*y2 + 4*A[36]*x*y3
           + A[37] + 2*A[38]*y + 3*A[39]*y2 + 4*A[40]*y3 + 5*A[41]*y4;
        yPrime = sqrt(dx * dx + dy * dy);
        break;
      case 'G':
        y = func[1];
        dx = A[1] + 2*A[2]*x + 3*A[3]*pow(x,2) + 4*A[4]*pow(x,3)
           + 3*A[5]*pow(x,2)*y + 2*A[6]*x*y + 2*A[7]*x*pow(y,2)
           + A[8]*y + A[9]*pow(y,2) + A[10]*pow(y,3);
        dy = A[5]*pow(x,3) + A[6]*pow(x,2) + 2*A[7]*pow(x,2)*y + A[8]*x
           + 2*A[9]*x*y + 3*A[10]*x*pow(y,2)
           + A[11] + 2*A[12]*y + 3*A[13]*pow(y,2) + 4*A[14]*pow(y,3);
        xPrime = sqrt(dx * dx + dy * dy);
        dx = A[16] + 2*A[17]*x + 3*A[18]*pow(x,2) + 4*A[19]*pow(x,3)
           + 3*A[20]*pow(x,2)*y + 2*A[21]*x*y + 2*A[22]*x*pow(y,2)
           + A[23]*y + A[24]*pow(y,2) + A[25]*pow(y,3);
        dy = A[20]*pow(x,3) + A[21]*pow(x,2) + 2*A[22]*pow(x,2)*y + A[23]*x
           + 2*A[24]*x*y + 3*A[25]*x*pow(y,2)
           + A[26] + 2*A[27]*y + 3*A[28]*pow(y,2) + 4*A[29]*pow(y,3);
        yPrime = sqrt(dx * dx + dy * dy);
        break;
      case 'F':
        y = func[1];
        dx = A[1] + 2*A[2]*x + 3*A[3]*x*x + 2*A[4]*x*y + A[5]*y + A[6]*y*y;
        dy = A[4]*x*x + A[5]*x + 2*A[6]*x*y + A[7] + 2*A[8]*y + 3*A[9]*y*y;
        xPrime = sqrt(dx * dx + dy * dy);
        dx = A[11] + 2*A[12]*x + 3*A[13]*x*x + 2*A[14]*x*y + A[15]*y + A[16]*y*y;
        dy = A[14]*x*x + A[15]*x + 2*A[16]*x*y + A[17] + 2*A[18]*y + 3*A[19]*y*y;
        yPrime = sqrt(dx * dx + dy * dy);
        break;
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
      case 3: funcPrime[2] = zPrime;
      case 2: funcPrime[1] = yPrime;
      case 1: funcPrime[0] = xPrime;
              break;
    }
    
    return funcPrime;
  }
  
  protected void displayImage(){
    
  }
  
  protected void findAttractors(String codeString){
    boolean resumeSearch;
    String lastCode, testString;
    
    resumeSearch = false;
    lastCode = getLastCode();
    switch(codeString.charAt(0)){
      case 'E':
        for (int a0 = 32;a0 < 128;a0++){
          System.out.println("a0: " + a0);
          for (int a1 = 32;a1 < 128;a1++){
            System.out.println("a1: " + a1);
            for (int a2 = 32;a2 < 128;a2++){
              System.out.println("a2: " + a2);
              for (int a3 = 32;a3 < 128;a3++){
                System.out.println("a3: " + a3);
                for (int a4 = 32;a4 < 128;a4++){
                  System.out.println("a4: " + a4);
                  for (int a5 = 32;a5 < 128;a5++){
                    System.out.println("a5: " + a5);
                    for (int a6 = 32;a6 < 128;a6++){
                      System.out.println("a6: " + a6);
                      for (int a7 = 32;a7 < 128;a7++){
                        System.out.println("a7: " + a7);
                        for (int a8 = 32;a8 < 128;a8++){
                          for (int a9 = 32;a9 < 128;a9++){
                            for (int a10 = 32;a10 < 128;a10++){
                              for (int a11 = 32;a11 < 128;a11++){
                                testString = "E";
                                testString += (char)a0;
                                testString += (char)a1;
                                testString += (char)a2;
                                testString += (char)a3;
                                testString += (char)a4;
                                testString += (char)a5;
                                testString += (char)a6;
                                testString += (char)a7;
                                testString += (char)a8;
                                testString += (char)a9;
                                testString += (char)a10;
                                testString += (char)a11;
                                if (resumeSearch){
                                  if (untestedCode(lastCode,testString)){
                                    testCode(testString);
                                    resumeSearch = false;
                                  }
                                }
                                else{
                                  testCode(testString);
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
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
    superSample = clearGrid(superSample);
    bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    rendering = true;
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
      case 'I':
        x += A[0];
        x += A[1] * xN;
        x += A[2] * pow(xN,2);
        x += A[3] * xN * yN;
        x += A[4] * xN * zN;
        x += A[5] * yN;
        x += A[6] * pow(yN,2);
        x += A[7] * yN * zN;
        x += A[8] * zN;
        x += A[9] * pow(zN,2);
        y += A[10];
        y += A[11] * xN;
        y += A[12] * pow(xN,2);
        y += A[13] * xN * yN;
        y += A[14] * xN * zN;
        y += A[15] * yN;
        y += A[16] * pow(yN,2);
        y += A[17] * yN * zN;
        y += A[18] * zN;
        y += A[19] * pow(zN,2);
        z += A[20];
        z += A[21] * xN;
        z += A[22] * pow(xN,2);
        z += A[23] * xN * yN;
        z += A[24] * xN * zN;
        z += A[25] * yN;
        z += A[26] * pow(yN,2);
        z += A[27] * yN * zN;
        z += A[28] * zN;
        z += A[29] * pow(zN,2);
        break;
      case 'H':
        x += A[0];
        x += A[1] * xN;
        x += A[2] * pow(xN,2);
        x += A[3] * pow(xN,3);
        x += A[4] * pow(xN,4);
        x += A[5] * pow(xN,5);
        x += A[6] * pow(xN,4) * yN;
        x += A[7] * pow(xN,3) * yN;
        x += A[8] * pow(xN,3) * pow(yN,2);
        x += A[9] * pow(xN,2) * yN;
        x += A[10] * pow(xN,2) * pow(yN,2);
        x += A[11] * pow(xN,2) * pow(yN,3);
        x += A[12] * xN * yN;
        x += A[13] * xN * pow(yN,2);
        x += A[14] * xN * pow(yN,3);
        x += A[15] * xN * pow(yN,4);
        x += A[16] * yN;
        x += A[17] * pow(yN,2);
        x += A[18] * pow(yN,3);
        x += A[19] * pow(yN,4);
        x += A[20] * pow(yN,5);
        y += A[21];
        y += A[22] * xN;
        y += A[23] * pow(xN,2);
        y += A[24] * pow(xN,3);
        y += A[25] * pow(xN,4);
        y += A[26] * pow(xN,5);
        y += A[27] * pow(xN,4) * yN;
        y += A[28] * pow(xN,3) * yN;
        y += A[29] * pow(xN,3) * pow(yN,2);
        y += A[30] * pow(xN,2) * yN;
        y += A[31] * pow(xN,2) * pow(yN,2);
        y += A[32] * pow(xN,2) * pow(yN,3);
        y += A[33] * xN * yN;
        y += A[34] * xN * pow(yN,2);
        y += A[35] * xN * pow(yN,3);
        y += A[36] * xN * pow(yN,4);
        y += A[37] * yN;
        y += A[38] * pow(yN,2);
        y += A[39] * pow(yN,3);
        y += A[40] * pow(yN,4);
        y += A[41] * pow(yN,5);
        break;
      case 'G':
        x += A[0];
        x += A[1] * xN;
        x += A[2] * pow(xN,2);
        x += A[3] * pow(xN,3);
        x += A[4] * pow(xN,4);
        x += A[5] * pow(xN,3) * yN;
        x += A[6] * pow(xN,2) * yN;
        x += A[7] * pow(xN,2) * pow(yN,2);
        x += A[8] * xN * yN;
        x += A[9] * xN * pow(yN,2);
        x += A[10] * xN * pow(yN,3);
        x += A[11] * yN;
        x += A[12] * pow(yN,2);
        x += A[13] * pow(yN,3);
        x += A[14] * pow(yN,4);
        y += A[15];
        y += A[16] * xN;
        y += A[17] * pow(xN,2);
        y += A[18] * pow(xN,3);
        y += A[19] * pow(xN,4);
        y += A[20] * pow(xN,3) * yN;
        y += A[21] * pow(xN,2) * yN;
        y += A[22] * pow(xN,2) * pow(yN,2);
        y += A[23] * xN * yN;
        y += A[24] * xN * pow(yN,2);
        y += A[25] * xN * pow(yN,3);
        y += A[26] * yN;
        y += A[27] * pow(yN,2);
        y += A[28] * pow(yN,3);
        y += A[29] * pow(yN,4);
        break;
      case 'F':
        x += A[0];
        x += A[1] * xN;
        x += A[2] * pow(xN,2);
        x += A[3] * pow(xN,3);
        x += A[4] * pow(xN,2) * yN;
        x += A[5] * xN * yN;
        x += A[6] * xN * pow(yN,2);
        x += A[7] * yN;
        x += A[8] * pow(yN,2);
        x += A[9] * pow(yN,3);
        y += A[10];
        y += A[11] * xN;
        y += A[12] * pow(xN,2);
        y += A[13] * pow(xN,3);
        y += A[14] * pow(xN,2) * yN;
        y += A[15] * xN * yN;
        y += A[16] * xN * pow(yN,2);
        y += A[17] * yN;
        y += A[18] * pow(yN,2);
        y += A[19] * pow(yN,3);
        break;
      case 'E': 
        x += A[0];
        x += A[1] * xN;
        x += A[2] * pow(xN,2);
        x += A[3] * xN * yN;
        x += A[4] * yN;
        x += A[5] * pow(yN,2);
        y += A[6];
        y += A[7] * xN;
        y += A[8] * pow(xN,2);
        y += A[9] * xN * yN;
        y += A[10] * yN;
        y += A[11] * pow(yN,2);
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
  
  private rgba[][] plot(rgba[][] sS, double xFunc, double yFunc, double zFunc, int[] colour){
    int xGrid, yGrid;
    
    xGrid = (int)((xFunc - min[0]) * xDelta);
    if (xGrid < sS.length && xGrid >= 0){
      yGrid = (int)(sS[xGrid].length - (yFunc - min[1]) * yDelta);
      if (yGrid < sS[xGrid].length && yGrid >= 0){
        if (zFunc < 0){
          sS[xGrid][yGrid].addColour(255,(int)((zFunc/min[2])*255),0);
        }
        else{
          sS[xGrid][yGrid].addColour(255,1,(int)((zFunc/max[2])*255));
        }
      }
    }
    return sS;
  }
  
  private rgba[][] plotPoints(tuple[] dataPoints, rgba[][] sS, int start, int end){
    int[][] colours;

    colours = new int[2][3];
    colours[0] = Util.red(255);
    colours[1] = Util.green(255);
    
    for (int j = start;j < end;j++){
      sS = plot(sS,dataPoints[j].tuple[0],dataPoints[j].tuple[1],dataPoints[j].tuple[2],colours[0]);
      if (j % 100 == 0){
        sS = plot(sS,-dataPoints[j].tuple[0],dataPoints[j].tuple[1],dataPoints[j].tuple[2],colours[0]);
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
      case 'I':
        lsum = new double[3];
        lyapunov = new double[3];
        lyapunov[0] = 0d;
        lyapunov[1] = 0d;
        lyapunov[2] = 0d;
        max = new double[3];
        min = new double[3];
        max[0] = -10000000000d;
        max[1] = max[0];
        max[2] = max[0];
        min[0] = -max[0];
        min[1] = -max[1];
        min[2] = -max[2];
        funcVals = new double[3];
        funcVals[0] = 0.5d;
        funcVals[1] = 0.5d;
        funcVals[2] = 0.5d;
        lsum[0] = log(calcDerivative(funcVals,A)[0])/ln2;
        lsum[1] = log(calcDerivative(funcVals,A)[1])/ln2;
        lsum[2] = log(calcDerivative(funcVals,A)[2])/ln2;
        for (int j = 0;j < numIterations;j++){
          funcVals = iterateFunc(funcVals,A);
          plot(sS,funcVals[0],funcVals[1],funcVals[2],colours[0]);
          lsum[0] += log(calcDerivative(funcVals,A)[0])/ln2;
          lsum[1] += log(calcDerivative(funcVals,A)[1])/ln2;
          lsum[2] += log(calcDerivative(funcVals,A)[2])/ln2;
          lyapunov[0] = lsum[0]/j;
          lyapunov[1] = lsum[1]/j;
          lyapunov[2] = lsum[2]/j;
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
          if (funcVals[2] > max[2]){
            max[2] = funcVals[2];
          }
          if (funcVals[2] < min[2]){
            min[2] = funcVals[2];
          }
        }
        break;
      case 'H':
      case 'G':
      case 'F':
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
        lsum[0] = log(calcDerivative(funcVals,A)[0])/ln2;
        lsum[1] = log(calcDerivative(funcVals,A)[1])/ln2;
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
  
  private tuple[] renderImage(tuple[] dataPoints){
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
      case 'I':
        lsum = new double[3];
        lyapunov = new double[3];
        lyapunov[0] = 0d;
        lyapunov[1] = 0d;
        lyapunov[2] = 0d;
        max = new double[3];
        min = new double[3];
        max[0] = -10000000000d;
        max[1] = max[0];
        max[2] = max[0];
        min[0] = -max[0];
        min[1] = -max[1];
        min[2] = -max[2];
        funcVals = new double[3];
        funcVals[0] = 0.5d;
        funcVals[1] = 0.5d;
        funcVals[2] = 0.5d;
        lsum[0] = log(calcDerivative(funcVals,A)[0])/ln2;
        lsum[1] = log(calcDerivative(funcVals,A)[1])/ln2;
        lsum[2] = log(calcDerivative(funcVals,A)[2])/ln2;
        for (int j = 0;j < numIterations;j++){
          dataPoints[j] = new tuple(funcVals);
          funcVals = iterateFunc(funcVals,A);
          lsum[0] += log(calcDerivative(funcVals,A)[0])/ln2;
          lsum[1] += log(calcDerivative(funcVals,A)[1])/ln2;
          lsum[2] += log(calcDerivative(funcVals,A)[2])/ln2;
          lyapunov[0] = lsum[0]/j;
          lyapunov[1] = lsum[1]/j;
          lyapunov[2] = lsum[2]/j;
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
          if (funcVals[2] > max[2]){
            max[2] = funcVals[2];
          }
          if (funcVals[2] < min[2]){
            min[2] = funcVals[2];
          }
        }
        break;
      case 'H':
      case 'G':
      case 'F':
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
        lsum[0] = log(calcDerivative(funcVals,A)[0])/ln2;
        lsum[1] = log(calcDerivative(funcVals,A)[1])/ln2;
        for (int j = 0;j < numIterations;j++){
          funcVals = iterateFunc(funcVals,A);
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
    
    return dataPoints;
  }

  @Override
  public void run(){
    boolean buffer;
    
    buffer = false;
    searchMaxMin();
    if(buffer){
      dataPoints = new tuple[numIterations];
      frames = new BufferedImage[numIterations/10000];
      dataPoints = renderImage(dataPoints);
      for (int k = 0;k < frames.length;k++){
        frames[k] = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        superSample = plotPoints(dataPoints,superSample,0,(k+1)*10000);
        superSample = mapDensities(superSample);
        superSample = gaussBlur(superSample,blurKernel);
        finalImage = downSample(superSample,sampleKernel,superFactor);
        writeImage(finalImage,"image" + k);
        frames[k] = convertImage(bufferedImage,finalImage);
        superSample = new rgba[width*superFactor][height*superFactor];
        superSample = clearGrid(superSample);
        finalImage = new rgba[width][height];
        bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      }
      rendering = false;
      for (int k = 0;k < frames.length;k++){
        bufferedImage = frames[k];
        writeImage(bufferedImage,"bfimg" + k);
        writeImage(frames[k],"frame" + k);
        repaint();
        try{
          Thread.sleep(50);
          System.out.println(k);
        }
        catch(Exception e){}
      }
    }
    else{
      dataPoints = new tuple[numIterations];
      dataPoints = renderImage(dataPoints);
      rendering = false;
      superSample = plotPoints(dataPoints,superSample,0,dataPoints.length);
      superSample = mapDensities(superSample);
      superSample = gaussBlur(superSample,blurKernel);
      finalImage = downSample(superSample,sampleKernel,superFactor);
      bufferedImage = convertImage(bufferedImage,finalImage);
      writeImage(bufferedImage,"bfimg01");
      /*
      int tracker = 2;
      for (int k = 5;k < 90;k = k+5){
        superSample = new rgba[width*superFactor][height*superFactor];
        superSample = clearGrid(superSample);
        finalImage = new rgba[width][height];
        bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        superSample = plotPoints(rotateGrid(dataPoints,k),superSample,0,dataPoints.length);
        superSample = mapDensities(superSample);
        superSample = gaussBlur(superSample,blurKernel);
        finalImage = downSample(superSample,sampleKernel,superFactor);
        bufferedImage = convertImage(bufferedImage,finalImage);
        writeImage(bufferedImage,"bfimg0" + tracker);
        tracker++;
      }
      /*
      superSample = renderImage(superSample);
      rendering = false;
      superSample = mapDensities(superSample);
      superSample = gaussBlur(superSample,blurKernel);
      finalImage = downSample(superSample,sampleKernel,superFactor);
      bufferedImage = convertImage(bufferedImage,finalImage);
       */
    }
  }
  
  public void searchMaxMin(){
    double[] funcVals, range;
    
    switch(codeString.charAt(0)){
      case 'A': ;
      case 'B': ;
      case 'C': ;
      case 'D': 
        funcVals = new double[1];
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
      case 'H': 
        funcVals = new double[2];
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
      case 'I': 
        funcVals = new double[3];
        range = new double[3];
        funcVals[0] = 0.5;
        funcVals[1] = 0.5;
        funcVals[2] = 0.5;
        max = new double[3];
        min = new double[3];
        max[0] = -10000000000d;
        max[1] = max[0];
        max[2] = max[0];
        min[0] = -max[0];
        min[1] = -max[1];
        min[2] = -max[2];
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
          if (funcVals[2] > max[2]){
            max[2] = funcVals[2];
          }
          if (funcVals[2] < min[2]){
            min[2] = funcVals[2];
          }
        }
        range[2] = max[2] - min[2];
        max[2] = max[2] + 0.05 * range[2];
        min[2] = min[2] - 0.05 * range[2];
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
    double ln2;
    int j;
    double[] funcVals, lsum;
    Double x, y;
    
    ln2 = log(2);
    switch(testString.charAt(0)){
      case 'E': 
        lsum = new double[2];
        lyapunov = new double[2];
        lyapunov[0] = 0d;
        lyapunov[1] = 0d;
        A = Util.decodeString(testString);
        funcVals = new double[2];
        funcVals[0] = 0.5d;
        funcVals[1] = 0.5d;
        x = new Double(funcVals[0]);
        y = new Double(funcVals[1]);
        lsum[0] = log(calcDerivative(funcVals,A)[0])/ln2;
        lsum[1] = log(calcDerivative(funcVals,A)[1])/ln2;
        j = 0;
        while((j < numIterations) && !(x.isInfinite()) && !(x.isNaN()) && !(y.isInfinite()) && !(y.isNaN())){
          funcVals = iterateFunc(funcVals,A);
          lsum[0] += log(abs(calcDerivative(funcVals,A)[0]))/ln2;
          lsum[1] += log(abs(calcDerivative(funcVals,A)[1]))/ln2;
          lyapunov[0] = lsum[0]/j;
          lyapunov[1] = lsum[1]/j;
          x = new Double(funcVals[0]);
          y = new Double(funcVals[1]);
          j++;
        }
        if ((j == numIterations) && ((lyapunov[0] > 0) || (lyapunov[1] > 0))){
          System.out.println("found");
          System.out.println(testString);
          writeString(testString,true);
        }
        break;
      case 'D': ;
      case 'C': ;
      case 'A':
        lsum = new double[1];
        lyapunov = new double[1];
        lyapunov[0] = 0d;
        A = Util.decodeString(testString);
        funcVals = new double[1];
        funcVals[0] = 0.5d;
        lsum[0] = log(abs(calcDerivative(funcVals,A)[0]))/ln2;
        x = new Double(funcVals[0]);
        j = 0;
        while((j < numIterations) && !(x.isInfinite()) && !(x.isNaN())){
          funcVals = iterateFunc(funcVals,A);
          lsum[0] += log(abs(calcDerivative(funcVals,A)[0]))/ln2;
          lyapunov[0] = lsum[0]/j;
          x = new Double(funcVals[0]);
          j++;
        }
        if (j == numIterations && lyapunov[0] > 0){
          System.out.println("found");
          System.out.println(testString);
          writeString(testString,true);
        }
        break;
      default:
        j = 0;
        funcVals = null;
        break;
    }
    writeString(testString);
  }
}