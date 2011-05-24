package supersample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class StrangeAttractor02 extends JPanel implements Runnable{
  byte superFactor;
  int height, width;
  BufferedImage bufferedImage;
  GaussKernel blurKernel, sampleKernel;
  rgba[][] finalImage, superSample;
  
  public StrangeAttractor02(int w, int h, int gW, int gH, int sF){
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
    double R;
    int xGrid, yGrid;
    int[] colour;

    xMax = 1;
    xMin = 0;
    yMax = 1;
    yMin = 0;
    xDelta = (sS.length - 1)/(xMax - xMin);
    yDelta = (sS[0].length - 1)/(yMax - yMin);
    colour = Util.red(255);
    R = 4;

    for (double j=xMin;j<=xMax;j+=0.001){
      xFunc = j;
      for (int k=0;k<1200;k++){
        yFunc = R * xFunc * (1 - xFunc);
        //System.out.println(xFunc +":" + yFunc);
        xGrid = (int)((xFunc - xMin) * xDelta);
        yGrid = (int)(sS[xGrid].length - (yFunc - yMin) * yDelta);
        if (xFunc < xMax &&
            xFunc > xMin &&
            yFunc < yMax &&
            yFunc > yMin   ){
          sS[xGrid][yGrid].addColour(colour);
        }
        xFunc = yFunc;
      }
    }

    return sS;
  }

  @Override
  public void run(){
  }
}