package fractalflames;

class rgba{
   short r;
   short g;
   short b;
   short a;
   
   protected rgba(){
      r = 0;
      g = 0;
      b = 0;
      a = 0;
   }
   
   protected rgba(int in_r, int in_g, int in_b, int in_a){
      r = (short)in_r;
      g = (short)in_g;
      b = (short)in_b;
      a = (short)in_a;
   }
   
   protected rgba(short in_r, short in_g, short in_b, short in_a){
      r = in_r;
      g = in_g;
      b = in_b;
      a = in_a;
   }
   
   protected void addColour(int[] rgb){
      r = (short)((r + rgb[0])/2);
      g = (short)((g + rgb[1])/2);
      b = (short)((b + rgb[2])/2);
      a += 1;
   }
   
   protected void addColour(short[] rgb){
      r = (short)((r + rgb[0])/2);
      g = (short)((g + rgb[1])/2);
      b = (short)((b + rgb[2])/2);
      a += 1;
   }
}