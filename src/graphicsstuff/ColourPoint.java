package graphicsstuff;

class ColourPoint{
   private int rgb, x, y;
   
   public ColourPoint(int con_x, int con_y, int con_rgb){
      x = con_x;
      y = con_y;
      rgb = con_rgb;
   }
   
   public int getRGB(){
      return rgb;
   }
   
   public int getX(){
      return x;
   }
   
   public int getY(){
      return y;
   }
   
   public void setRGB(int new_rgb){
      rgb = new_rgb;
   }
   
   public void setX(int new_x){
      x = new_x;
   }
   
   public void setY(int new_y){
      y = new_y;
   }
}