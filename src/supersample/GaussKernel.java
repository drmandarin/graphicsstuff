package supersample;

class GaussKernel {
  byte height, width;
  double sum;
  double[][] array;
   
  protected GaussKernel(int inWidth, int inHeight){
    int counterX, counterY, radiusX, radiusY;
    double coefficient, denomX, denomY, sigmaX, sigmaY, val;
    float endX, endY, squareX, squareY, startX, startY;

    width = (byte)inWidth;
    height = (byte)inHeight;
    radiusX = width/2;
    radiusY = height/2;
    sigmaX = radiusX/3.0d;
    sigmaY = radiusY/3.0d;
    array = new double[width][height];
    coefficient = 1/(2.0d * Math.PI * sigmaX * sigmaY);
    denomX = 2 * sigmaX * sigmaX;
    denomY = 2 * sigmaY * sigmaY;
    sum = 0d;

    endX = (width % 2 == 1) ? radiusX : radiusX - 0.5f;
    endY = (height % 2 == 1) ? radiusY : radiusY - 0.5f;
    startX = -endX;
    startY = -endY;

    counterX = 0;
    for (float x=startX;x<=endX;x++){
      counterY = 0;
      squareX = x*x;
      for (float y=startY;y<=endY;y++){
        squareY = y*y;
        val = coefficient * Math.exp(- ((squareX/denomX)+(squareY/denomY)));
        array[counterX][counterY] = val;
        sum += val;
        counterY++;
      }
      counterX++;
    }
  }
}