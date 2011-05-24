package graphicsstuff;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class MapReader{
   
   public MapReader(){
      BufferedImage bi;
      //File inFile;
      
      try{
         //inFile = new File("C:\\Documents and Settings\\kunderhay\\My Documents\\My Pictures\\world_map1.gif");
         bi = ImageIO.read(new File("C:\\Documents and Settings\\kunderhay\\My Documents\\My Pictures\\world_map1.gif"));
         for (int i=0;i<bi.getWidth();i++){
            System.out.print(bi.getRGB(i,270)+",");
         }
         System.out.println();
      }
      catch(Exception e){
         System.out.println("Exception in graphicsstuff.MapReader.MapReader()");
         System.out.println(e.getMessage());
      }
      
   }
   
   public static void main(String[] args){
      MapReader mapReader = new MapReader();
   }
}