package supersample;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

class Util{

  protected static int[] blue(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = 0;
    colour[1] = 0;
    colour[2] = level;

    return colour;
  }

  protected static int[] green(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = 0;
    colour[1] = level;
    colour[2] = 0;

    return colour;
  }

  protected static int[] red(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = 0;
    colour[2] = 0;

    return colour;
  }

  protected static int[] white(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = level;
    colour[2] = level;

    return colour;
  }

  protected static int[] yellow(int level){
    int[] colour;

    colour = new int[3];
    colour[0] = level;
    colour[1] = level;
    colour[2] = 0;

    return colour;
  }
  
  protected static void addComp(Container cont, Component comp, int x, int y){
    addComp(cont,comp,x,y,1,1);
  }
  
  protected static void addComp(Container cont, Component comp, int x, int y, String anchor){
    addComp(cont,comp,x,y,1,1,anchor);
  }
  
  protected static void addComp(Container cont, Component comp, int x, int y, int w, int h){
    GridBagConstraints c;
    
    c = new GridBagConstraints();
    c.gridx = x;
    c.gridy = y;
    c.gridwidth = w;
    c.gridheight = h;
    cont.add(comp,c);
  }
  
  protected static void addComp(Container cont, Component comp, int x, int y, int w, int h, String anchor){
    GridBagConstraints c;
    
    c = new GridBagConstraints();
    c.gridx = x;
    c.gridy = y;
    c.gridwidth = w;
    c.gridheight = h;
    if(anchor.matches("CENTER"))
      c.anchor = GridBagConstraints.CENTER;
    else if(anchor.matches("EAST"))
      c.anchor = GridBagConstraints.EAST;
    else if(anchor.matches("WEST"))
      c.anchor = GridBagConstraints.WEST;
    cont.add(comp,c);
  }
  
  protected static rgba[][] clearGrid(rgba[][] grid){
    for (int x = 0;x < grid.length;x++){
      for (int y = 0;y < grid[x].length;y++){
        grid[x][y] = new rgba();
      }
    }
    return grid;
  }
  
  protected static double[] decodeString(String inString){
    double[] coeffs;
    
    coeffs = new double[inString.length()-1];
    for (int i = 1;i < inString.length();i++){
      coeffs[i-1] = mapCoefficient(inString.charAt(i));
    }
    
    return coeffs;
  }
  
  protected static double fixDouble(double inVal){
    String valString;
    
    valString = String.valueOf(inVal);
    valString = valString.substring(0,valString.indexOf('.')+2);
    
    return Double.parseDouble(valString);
  }
  
  protected static String fmt(double num){
    DecimalFormat decFormat;
    
    decFormat = new DecimalFormat("+0.0000000000;-0.0000000000");
    return(decFormat.format(num));
  }
  
  protected static String getHTML(char x){
    return getHTML(x,1);
  }
  
  protected static String getHTML(char x, int xP){
    String html;
    
    html = "";
    html += "<i>" + x + "</i><sub>n</sub>";
    if (xP > 1)
      html += "<sup>" + xP + "</sup>";
    
    return html;
  }
  
  protected static String getHTML(char x, char y){
    return getHTML(x,1,y,1);
  }
  
  protected static String getHTML(char x, int xP, char y){
    return getHTML(x,xP,y,1);
  }
  
  protected static String getHTML(char x, char y, int yP){
    return getHTML(x,1,y,yP);
  }
  
  protected static String getHTML(char x, int xP, char y, int yP){
    String html;
    
    html = "";
    html += "<i>" + x + "</i><sub>n</sub>";
    if (xP > 1)
      html += "<sup>" + xP + "</sup>";
    html += "<i>" + y + "</i><sub>n</sub>";
    if (yP > 1)
      html += "<sup>" + yP + "</sup>";
    
    return html;
  }
  
  protected static String getLastCode(){
    BufferedReader file;
    String lastCode;
    
    lastCode = "";
    try{
      file = new BufferedReader(new FileReader("c:\\progs\\lastCode.txt"));
      lastCode = file.readLine();
      file.close();
    }
    catch(Exception e){
      System.out.println("Exception in getLastCode: " + e.getMessage());
    }
    return lastCode;
  }
  
  protected static char mapCoefficient(double coefficient){
    return (char)(coefficient * 10 + 77);
  }
  
  protected static double mapCoefficient(char coefficient){
    return fixDouble((double)((int)coefficient - 77) * 0.1d);
  }

  protected static rgba[][] mapDensities(rgba[][] histo){
    double maxAlpha, scale;
    int count;
    rgba[][] mappedImage;

    count = 0;
    maxAlpha = -1;
    mappedImage = new rgba[histo.length][histo[0].length];
    for (int x=0;x<histo.length;x++){
      for (int y=0;y<histo[x].length;y++){
        maxAlpha = Math.max(maxAlpha, histo[x][y].a);
        mappedImage[x][y] = new rgba();
      }
    }
    for (int x=0;x<histo.length;x++){
      for (int y=0;y<histo[x].length;y++){
        if (histo[x][y].a > 0){
          count++;
          scale = (maxAlpha == 1) ? 1 : Math.log((double)(histo[x][y].a))/Math.log(maxAlpha);
          mappedImage[x][y].r = (short)(histo[x][y].r * scale);
          mappedImage[x][y].g = (short)(histo[x][y].g * scale);
          mappedImage[x][y].b = (short)(histo[x][y].b * scale);
        }
      }
    }

    return mappedImage;
  }
  
  protected static tuple[] rotateGrid(tuple[] grid, int angle){
    double theta, x, y, z;
    double[][] R;
    tuple[] rotGrid;
    
    rotGrid = new tuple[grid.length];
    theta = (double)angle * PI/180;
    R = new double[3][3];
    R[0][0] = 1;
    R[0][1] = 0;
    R[0][2] = 0;
    R[1][0] = 0;
    R[1][1] = cos(theta);
    R[1][2] = -sin(theta);
    R[2][0] = 0;
    R[2][1] = sin(theta);
    R[2][2] = cos(theta);
    for (int k = 0;k < grid.length;k++){
      x = grid[k].tuple[0] * R[0][0] + grid[k].tuple[1] * R[0][1] + grid[k].tuple[2] * R[0][2];
      y = grid[k].tuple[0] * R[1][0] + grid[k].tuple[1] * R[1][1] + grid[k].tuple[2] * R[1][2];
      z = grid[k].tuple[0] * R[2][0] + grid[k].tuple[1] * R[2][1] + grid[k].tuple[2] * R[2][2];
      rotGrid[k] = new tuple(x,y,z);
    }
    return rotGrid;
  }
  
  protected static boolean untestedCode(String lastCode, String newCode){
    boolean untested;
    
    untested = true;
    assert lastCode.length() == newCode.length();
    for (int k = 0;k < lastCode.length();k++){
      untested = (lastCode.charAt(k) <= newCode.charAt(k)) && untested;
    }
    
    return untested;
  }
  
  protected static void writeImage(BufferedImage bI, String fN){
    File outFile;

    try{
      outFile = new java.io.File("C:\\TEMP\\sa\\" + fN + ".bmp");
      javax.imageio.ImageIO.write(bI,"bmp",outFile);
    }
    catch(Exception e){
      System.out.println("Exception in writeImage");
      System.out.println(e.getMessage());
    }
  }

  protected static void writeImage(rgba[][] wI, String fN){
    int colour;
    BufferedImage bI;
    File outFile;
    rgba pixel;

    bI = new BufferedImage(wI.length,wI[0].length,BufferedImage.TYPE_INT_RGB);
    for (int i=0;i<wI.length;i++){
      for (int j=0;j<wI[i].length;j++){
        pixel = wI[i][j];
        colour = (new Color(pixel.r,pixel.g,pixel.b)).getRGB();
        bI.setRGB(i,j,colour);
      }
    }

    try{
      outFile = new java.io.File("C:\\TEMP\\sa\\" + fN + ".bmp");
      javax.imageio.ImageIO.write(bI,"bmp",outFile);
    }
    catch(Exception e){
      System.out.println("Exception in writeImage");
      System.out.println(e.getMessage());
    }
  }
  
  protected static void writeString(String inString){
    BufferedWriter file;
    
    try{
      file = new BufferedWriter(new FileWriter("c:\\progs\\lastCode.txt"));
      file.write(inString);
      file.close();
    }
    catch(Exception e){
      System.out.println("Exception in writeString: " + e.getMessage());
    }
  }
  
  protected static void writeString(String inString, boolean attractor){
    BufferedWriter file;
    
    if (attractor){
      try{
        file = new BufferedWriter(new FileWriter("c:\\progs\\attractors.txt",true));
        file.write(inString);
        file.newLine();
        file.close();
      }
      catch(Exception e){
        System.out.println("Exception in writeString: " + e.getMessage());
      }
    }
  }
}