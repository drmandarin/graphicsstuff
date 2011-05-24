package graphicsstuff;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.Random;

public class ThreeDee extends JPanel implements Runnable{
   boolean runFlag;
   int BLACK;
   int GREEN;
   int RED;
   int YELLOW;
   int angle;
   int height;
   int width;
   int origin_x;
   int origin_y;
   int[][] newStarLocations;
   int[][] starLocations;
   BufferedImage bi;
   Thread thread;
   
   public ThreeDee(int w, int h){
      super();
      this.setForeground(new Color(0,0,255,255));
      this.setBackground(new Color(0,0,255,255));
      height = h;
      width = w;
      origin_x = width/2;
      origin_y = height/2;
      runFlag = true;
      bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
      BLACK = (new Color(0,0,0,255)).getRGB();
      GREEN = (new Color(0,255,0,255)).getRGB();
      RED = (new Color(255,0,0,255)).getRGB();
      YELLOW = (new Color(255,255,0,255)).getRGB();
      clearImage();
      angle = 0;
      thread = new Thread(this);
      thread.start();
   }
   
   private void clearImage(){
      for (int i=0;i<width;i++){
         for (int j=0;j<height;j++){
            bi.setRGB(i,j,BLACK);
         }
      }
   }
   
   private void clearStar(int[] starCoords){
      int x;
      int y;
      
      x = origin_x+starCoords[1];
      y = origin_y-starCoords[2];
      bi.setRGB(x,y,BLACK);
      bi.setRGB(x-1,y,BLACK);
      bi.setRGB(x+1,y,BLACK);
      bi.setRGB(x,y-1,BLACK);
      bi.setRGB(x,y+1,BLACK);
   }
   
   private int[] getCartesianCoords(double rho,double phi,double theta){
      int[] xyzValues;
      
      xyzValues = new int[3];
      xyzValues[0] = (int)(rho * Math.cos(phi) * Math.sin(theta));
      xyzValues[1] = (int)(rho * Math.sin(phi) * Math.sin(theta));
      xyzValues[2] = (int)(rho * Math.cos(theta));
      
      return xyzValues;
   }
   
   public void paint(Graphics g){
      Graphics2D g2;
      
      g2 = (Graphics2D)g;
      g2.drawImage(bi,null,0,0);
   }
   
   private void plotStar(int[] starCoords){
      int x;
      int y;
      int rgb;
      
      x = origin_x+starCoords[1];
      y = origin_y-starCoords[2];
      rgb = (new Color(128+(starCoords[0]*127/500),128,128-(starCoords[0]*127/500),255)).getRGB();
      bi.setRGB(x,y,rgb);
      bi.setRGB(x-1,y,rgb);
      bi.setRGB(x+1,y,rgb);
      bi.setRGB(x,y-1,rgb);
      bi.setRGB(x,y+1,rgb);
   }
   
   private void plotVector(double[][] inVector, int vectorColour){
      
      for (int i=0;i<inVector[0].length;i++){
         bi.setRGB(origin_x+(int)inVector[1][i],origin_y-(int)inVector[2][i],vectorColour);
      }
   }
   
   private void populateStars(){
      double factor;
      double theta;
      double phi;
      double rho;
      int numStars;
      Random random;
      
      factor = Math.PI/180;
      numStars = 1800;
      newStarLocations = new int[numStars][3];
      starLocations = new int[numStars][3];
      rho = 400;
      random = new Random();
      
      for (int i=0;i<numStars;i++){
         theta = random.nextInt(180) * factor;
         phi = random.nextInt(360) * factor;
         //rho = random.nextInt(400);
         starLocations[i] = getCartesianCoords(rho,phi,theta);
         newStarLocations[i] = getCartesianCoords(rho,phi,theta);
         plotStar(starLocations[i]);
      }
   }
   
   public void run(){
      populateStars();
      while(runFlag){
         tick();
         repaint();
         try{
            Thread.sleep(20);
            //clearImage();
         }
         catch(Exception e){
            System.out.println("Exception in ThreeDee.run()");
            System.out.println(e.getMessage());
         }
      }
   }
   
   private void tick(){
      double rotAngle;
      double[][] zRotMatrix;
      
      zRotMatrix = new double[3][3];
      if (angle == 359){
         angle = 0;
      }
      else{
         angle++;
      }
      rotAngle = angle * Math.PI/180;
      zRotMatrix[0][0] = Math.cos(rotAngle);
      zRotMatrix[0][1] = -Math.sin(rotAngle);
      zRotMatrix[0][2] = 0;
      zRotMatrix[1][0] = Math.sin(rotAngle);
      zRotMatrix[1][1] = Math.cos(rotAngle);
      zRotMatrix[1][2] = 0;
      zRotMatrix[2][0] = 0;
      zRotMatrix[2][1] = 0;
      zRotMatrix[2][2] = 1;
      
      for (int i=0;i<newStarLocations.length;i++){
         for (int j=0;j<3;j++){
            clearStar(newStarLocations[i]);
            newStarLocations[i][j] = (int)(zRotMatrix[j][0] * starLocations[i][0] + zRotMatrix[j][1] * starLocations[i][1] + zRotMatrix[j][2] * starLocations[i][2]);
         }
         plotStar(newStarLocations[i]);
      }
   }
}

/*
 
 private void setAxes(){
      double factor;
      double phi;
      double theta;
      double[][] xAxis;
      double[][] yAxis;
      double[][] zAxis;
      int axisLength;
      
      factor = (Math.PI/180);
      axisLength = 101;
      xAxis = new double[3][axisLength];
      yAxis = new double[3][axisLength];
      zAxis = new double[3][axisLength];
      
      // x-axis
      phi = 0 * factor;
      theta = 90 * factor;
      for (int i=0;i<axisLength;i++){
         xAxis[0][i] = i * Math.cos(phi) * Math.sin(theta);
         xAxis[1][i] = i * Math.sin(phi) * Math.sin(theta);
         xAxis[2][i] = i * Math.cos(theta);
      }
      plotVector(xAxis,RED);
      
      // y-axis
      phi = 90 * factor;
      theta = 90 * factor;
      for (int i=0;i<axisLength;i++){
         yAxis[0][i] = i * Math.cos(phi) * Math.sin(theta);
         yAxis[1][i] = i * Math.sin(phi) * Math.sin(theta);
         yAxis[2][i] = i * Math.cos(theta);
      }
      plotVector(yAxis,RED);
      
      // z-axis
      phi = 0 * factor;
      theta = 0 * factor;
      for (int i=0;i<axisLength;i++){
         zAxis[0][i] = i * Math.cos(phi) * Math.sin(theta);
         zAxis[1][i] = i * Math.sin(phi) * Math.sin(theta);
         zAxis[2][i] = i * Math.cos(theta);
      }
      plotVector(zAxis,RED);
      
      double[][] newLine = new double[3][axisLength];
      phi = 90 * factor;
      theta = 45 * factor;
      for (int i=0;i<axisLength;i++){
         newLine[0][i] = i * Math.cos(phi) * Math.sin(theta);
         newLine[1][i] = i * Math.sin(phi) * Math.sin(theta);
         newLine[2][i] = i * Math.cos(theta);
      }
      plotVector(newLine,GREEN);
      
      double[][] circle = new double[3][3600];
      theta = 45 * factor;
      for (int i=0;i<3600;i++){
         phi = (double)i/10 * factor;
         circle[0][i] = 101 * Math.cos(phi) * Math.sin(theta);
         circle[1][i] = 101 * Math.sin(phi) * Math.sin(theta);
         circle[2][i] = 101 * Math.cos(theta);
      }
      
      double rotAngle = 90 * factor;
      double[][] rotMatrix = new double[3][3];
      rotMatrix[0][0] = Math.cos(rotAngle);
      rotMatrix[0][1] = 0;
      rotMatrix[0][2] = -Math.sin(rotAngle);
      rotMatrix[1][0] = 0;
      rotMatrix[1][1] = 1;
      rotMatrix[1][2] = 0;
      rotMatrix[2][0] = Math.sin(rotAngle);
      rotMatrix[2][1] = 0;
      rotMatrix[2][2] = Math.cos(rotAngle);
      
      for (int i=0;i<3600;i++){
         for (int j=0;j<3;j++){
            circle[j][i] = (rotMatrix[j][0] * circle[0][i]) + (rotMatrix[j][1] * circle[1][i]) + (rotMatrix[j][2] * circle[2][i]);
         }
      }
      
      plotVector(circle,YELLOW);
   }
 
 
 private void tick(){
      double phi;
      double theta;
      
      angle++;
      double factor = Math.PI/180;
      double[][] circle = new double[3][3600];
      theta = 45 * factor;
      for (int i=0;i<3600;i++){
         phi = (double)i/10 * factor;
         circle[0][i] = 101 * Math.cos(phi) * Math.sin(theta);
         circle[1][i] = 101 * Math.sin(phi) * Math.sin(theta);
         circle[2][i] = 101 * Math.cos(theta);
      }
      
      double rotAngle = angle * factor;
      double[][] rotMatrix = new double[3][3];
      rotMatrix[0][0] = Math.cos(rotAngle);
      rotMatrix[0][1] = 0;
      rotMatrix[0][2] = -Math.sin(rotAngle);
      rotMatrix[1][0] = 0;
      rotMatrix[1][1] = 1;
      rotMatrix[1][2] = 0;
      rotMatrix[2][0] = Math.sin(rotAngle);
      rotMatrix[2][1] = 0;
      rotMatrix[2][2] = Math.cos(rotAngle);
      
      for (int i=0;i<3600;i++){
         for (int j=0;j<3;j++){
            circle[j][i] = (rotMatrix[j][0] * circle[0][i]) + (rotMatrix[j][1] * circle[1][i]) + (rotMatrix[j][2] * circle[2][i]);
         }
      }
      
      plotVector(circle,YELLOW);
   }
 */