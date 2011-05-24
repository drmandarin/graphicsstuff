package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class SuperSample4 extends JPanel implements Runnable{
  byte superFactor;
  int height, width;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgba[][] finalImage, superSample;

  public SuperSample4(int w, int h, int gW, int gH, int sF){
    init(w,h,gW,gH,sF);
    superSample = renderImage(superSample);
    Util.writeImage(superSample,"image01");
    superSample = AntiAlias.gaussBlur(superSample,blurKernel);
    Util.writeImage(superSample,"image02");
    finalImage = AntiAlias.downSample(superSample,sampleKernel,superFactor);
    Util.writeImage(finalImage,"image03");
    bufferedImage = drawImage(bufferedImage,finalImage);
  }

  private void init(int w, int h, int gW, int gH, int sF){
    width = w;
    height = h;
    superFactor = (byte)sF;
    blurKernel = new GaussKernel(gW,gH);
    sampleKernel = new GaussKernel(superFactor,superFactor);
    finalImage = new rgba[width][height];
    superSample = new rgba[width*superFactor][height*superFactor];
    for (int x=0;x<superSample.length;x++){
      for (int y=0;y<superSample[x].length;y++){
        superSample[x][y] = new rgba(255,255,255);
      }
    }
    bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
  }

  private void drawBlock(rgba[][] sS, int x, int y, int w, int[] colour){
    int xL, xR, yL, yR;

    xL = Math.max(x-w,0);
    xR = Math.min(x+w,sS.length-1);
    yL = Math.max(y-w,0);
    yR = Math.min(y+w,sS[x].length-1);

    for (int i=xL;i<=xR;i++){
      for (int j=yL;j<=yR;j++){
        sS[i][j].addColour(colour);
      }
    }
  }

  private void drawDiamond(rgba[][] sS, int x, int y, int w, int[] colour){
    if (x-w > 0)
      sS[x-w][y].addColour(colour);

    if (x+w < sS.length)
      sS[x+w][y].addColour(colour);

    if (y-w > 0)
      sS[x][y-w].addColour(colour);

    if (y+w < sS[x].length)
      sS[x][y+w].addColour(colour);

    drawBlock(sS,x,y,w-1,colour);
  }
  
  private void drawGrid(rgba[][] sS, double xMin, double xMax, double yMin, double yMax, double xSpace, double ySpace){
    double x1, x2, xDelta, xFunc;
    double y1, y2, yDelta, yFunc;
    int xGrid, xSteps;
    int yGrid, ySteps;
    int[] white;
    Point xGridMax, xGridMin, yGridMax, yGridMin;
    //x1 = left
    //x2 = right
    //y1 = bottom
    //y2 = top

    xGridMax = new Point();
    xGridMin = new Point();
    yGridMax = new Point();
    yGridMin = new Point();
    white = new int[3];
    white[0] = 150;
    white[1] = 150;
    white[2] = 150;
    xDelta = (sS.length - 1)/(xMax - xMin);
    x1 = (int)(xMin/xSpace)*xSpace;
    x2 = (int)(xMax/xSpace)*xSpace;
    xSteps = (int)((x2 - x1)/xSpace);
    yDelta = (sS[0].length - 1)/(yMax - yMin);
    y1 = (int)(yMin/ySpace)*ySpace;
    y2 = (int)(yMax/ySpace)*ySpace;
    ySteps = (int)((y2 - y1)/ySpace);
    xGridMax.x = (int)((x2 - xMin) * xDelta);
    xGridMin.x = (int)((x1 - xMin) * xDelta);
    yGridMax.x = (int)((0d - xMin) * xDelta);
    yGridMin.x = (int)((0d - xMin) * xDelta);
    xGridMax.y = (int)((0d - yMin) * yDelta);
    xGridMin.y = (int)((0d - yMin) * yDelta);
    yGridMax.y = sS[0].length - (int)((y2 - yMin) * yDelta);
    yGridMin.y = sS[0].length - (int)((y1 - yMin) * yDelta);
    System.out.println(xGridMin.x + ":" + xGridMin.y);
    System.out.println(xGridMax.x + ":" + xGridMax.y);
    System.out.println(yGridMin.x + ":" + yGridMin.y);
    System.out.println(yGridMax.x + ":" + yGridMax.y);
    for (int i=0;i<=xSteps;i++){
      xFunc = x1 + xSpace*i;
      xGrid = (int)((xFunc - xMin) * xDelta);
      drawBlock(sS,xGrid,yGridMin.y,5,white);
      drawLine(sS,new Point(xGrid,0),new Point(xGrid,sS[xGrid].length),1,white);
    }
    for (int i=0;i<=ySteps;i++){
      yFunc = y1 + ySpace*i;
      yGrid = sS[0].length - (int)((yFunc - yMin) * yDelta);
      drawLine(sS,new Point(0,yGrid),new Point(sS.length,yGrid),1,white);
    }
  }

  private void drawLine(rgba[][] sS, Point startPoint, Point endPoint, int w, int[] colour){
    double incX, incY;
    int numSteps, x, y;
    Point delta;

    delta = new Point(endPoint.x - startPoint.x,endPoint.y - startPoint.y);
    numSteps = (Math.abs(delta.x) < Math.abs(delta.y)) ? delta.y : delta.x;
    incX = (double)delta.x/(double)numSteps;
    incY = (double)delta.y/(double)numSteps;
    for (int i=0;i<numSteps;i++){
      x = (int)(i * incX + startPoint.x);
      y = (int)(i * incY + startPoint.y);
      drawBlock(sS,x,y,w,colour);
    }
  }

  private void drawStroke(rgba[][] sS, int x, int y, int length, int[] colour){
    int startX, endX;
    
    startX = Math.max(0,x - length/2);
    endX = Math.min(x + length/2,sS.length-1);
    
    for (int i=startX;i<=endX;i++){
      sS[i][y].addColour(colour);
    }
  }

  private BufferedImage drawImage(BufferedImage bI, rgba[][] sS){
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
  
  @Override
  public void paint(Graphics g){
    Graphics2D g2;
    
    g2 = (Graphics2D)g;
    g2.drawImage(bufferedImage,null,0,0);
  }

  private rgba[][] renderImage(rgba[][] sS){
    double xDelta, xFunc, xMax, xMin;
    double yDelta, yFunc, yMax, yMin, y;
    int[] blue, green, red;

    blue = new int[3];
    blue[0] = 0;
    blue[1] = 0;
    blue[2] = 255;
    green = new int[3];
    green[0] = 0;
    green[1] = 255;
    green[2] = 0;
    red = new int[3];
    red[0] = 255;
    red[1] = 0;
    red[2] = 0;
    xMax = Math.PI;
    xMin = -Math.PI;
    xDelta = (xMax - xMin)/(sS.length - 1);
    yMax = 1.2;
    yMin = -1.2;
    yDelta = (sS[0].length - 1)/(yMax - yMin);
    drawGrid(sS,xMin,xMax,yMin,yMax,0.2d,0.2d);
    for (int x=0;x<sS.length;x++){
      xFunc = x * xDelta + xMin;
      yFunc = Math.sin(xFunc);
      y = sS[x].length - (yFunc - yMin) * yDelta;
      if (y > 0 && y < sS[x].length)
        drawDiamond(sS,x,(int)y,3,green);
        //drawStroke(sS,x,(int)y,7,green);
    }
    for (int x=0;x<sS.length;x++){
      xFunc = x * xDelta + xMin;
      yFunc = Math.tan(xFunc);
      y = sS[x].length - (yFunc - yMin) * yDelta;
      if (y > 0 && y < sS[x].length)
        drawDiamond(sS,x,(int)y,3,red);
    }
    for (int x=0;x<sS.length;x++){
      xFunc = x * xDelta + xMin;
      yFunc = Math.cos(xFunc) * Math.sin(xFunc) * Math.sin(xFunc);
      y = sS[x].length - (yFunc - yMin) * yDelta;
      //if (y > 0 && y < sS[x].length)
        //drawDiamond(sS,x,(int)y,3,blue);
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
}