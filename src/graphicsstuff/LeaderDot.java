package graphicsstuff;

class LeaderDot{
   private int rgb, x, y;
   private ColourPoint left, right, up, down;
   
   public LeaderDot(){
      
   }
   
   public LeaderDot(ColourPoint cp){
      rgb = cp.getRGB();
      x = cp.getX();
      y = cp.getY();
      left = null;
      right = null;
      up = null;
      down = null;
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
   
   public ColourPoint getLeft(){
      return left;
   }
   
   public ColourPoint getRight(){
      return right;
   }
   
   public ColourPoint getUp(){
      return up;
   }
   
   public ColourPoint getDown(){
      return down;
   }
   
   public void setLeft(int x, int y, int rgb){
      left = new ColourPoint(x,y,rgb);
   }
   
   public void setRight(int x, int y, int rgb){
      right = new ColourPoint(x,y,rgb);
   }
   
   public void setUp(int x, int y, int rgb){
      up = new ColourPoint(x,y,rgb);
   }
   
   public void setDown(int x, int y, int rgb){
      down = new ColourPoint(x,y,rgb);
   }
}