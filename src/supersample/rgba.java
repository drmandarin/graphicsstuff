package supersample;

public class rgba{
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
   
  protected rgba(int inR, int inG, int inB){
    r = (short)inR;
    g = (short)inG;
    b = (short)inB;
    a = 0;
  }
   
  protected rgba(short inR, short inG, short inB){
    r = inR;
    g = inG;
    b = inB;
    a = 0;
  }

  protected rgba(int inR, int inG, int inB, int inA){
    r = (short)inR;
    g = (short)inG;
    b = (short)inB;
    a = (short)inA;
  }

  protected rgba(short inR, short inG, short inB, short inA){
    r = inR;
    g = inG;
    b = inB;
    a = inA;
  }

  protected void addColour(int addR, int addG, int addB){
    r = (short)((r + addR)/2);
    g = (short)((g + addG)/2);
    b = (short)((b + addB)/2);
    a += 1;
  }

  protected void addColour(short addR, short addG, short addB){
    r = (short)((r + addR)/2);
    g = (short)((g + addG)/2);
    b = (short)((b + addB)/2);
    a += 1;
  }

  protected void addColour(int[] colour){
    r = (short)((r + colour[0])/2);
    g = (short)((g + colour[1])/2);
    b = (short)((b + colour[2])/2);
    a += 1;
  }

  protected void addColour(short[] colour){
    r = (short)((r + colour[0])/2);
    g = (short)((g + colour[1])/2);
    b = (short)((b + colour[2])/2);
    a += 1;
  }
}