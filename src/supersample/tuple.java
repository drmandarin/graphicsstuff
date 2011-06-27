package supersample;

class tuple{
  double[] tuple;
  
  protected tuple(int length){
    tuple = new double[length];
  }
  
  protected tuple(double[] vals){
    tuple = new double[vals.length];
    for (int i = 0;i < vals.length;i++){
      tuple[i] = vals[i];
    }
  }
}