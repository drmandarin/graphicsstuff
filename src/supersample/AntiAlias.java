package supersample;

class AntiAlias{

  protected static rgba[][] downSample(rgba[][] sS, GaussKernel sK, int superFactor){
    int width, height;
    rgba[][] dS;

    if (superFactor == 1){
      return sS;
    }
    else{
      width = sS.length / superFactor;
      height = sS[0].length / superFactor;
      dS = new rgba[width][height];
      for (int x=0;x<width;x++){
        for (int y=0;y<height;y++){
          dS[x][y] = getPixelSample(sS,sK,superFactor,x,y);
        }
      }
      return dS;
    }
  }

  protected static rgba[][] gaussBlur(rgba[][] sS, GaussKernel bK){
    rgba pixelSample;
    rgba[][] gB;

    gB = new rgba[sS.length][sS[0].length];
    for (int x=0;x<gB.length;x++){
      for (int y=0;y<gB[x].length;y++){
        pixelSample = getSample(x,y,sS,bK);
        gB[x][y] = pixelSample;
      }
    }
    return gB;
  }

  private static rgba getPixelSample(rgba[][] sS, GaussKernel sK, int superFactor, int a, int b){
    int offsetX, offsetY;
    double[] sampler;
    rgba pixelSample;

    offsetX = a*superFactor;
    offsetY = b*superFactor;
    pixelSample = new rgba();
    sampler = new double[3];
    sampler[0] = 0d;
    sampler[1] = 0d;
    sampler[2] = 0d;
    for (int x=0;x<superFactor;x++){
      for (int y=0;y<superFactor;y++){
        sampler[0] += sS[x+offsetX][y+offsetY].r * sK.array[x][y];
        sampler[1] += sS[x+offsetX][y+offsetY].g * sK.array[x][y];
        sampler[2] += sS[x+offsetX][y+offsetY].b * sK.array[x][y];
      }
    }
    pixelSample.r = (short)(Math.round(sampler[0]/sK.sum));
    pixelSample.g = (short)(Math.round(sampler[1]/sK.sum));
    pixelSample.b = (short)(Math.round(sampler[2]/sK.sum));

    return pixelSample;
  }

  private static rgba getSample(int a, int b, rgba[][] sS, GaussKernel gK){
    double samplerSum;
    int endX, endY, gX, gY, iniGY, radiusX, radiusY, startX, startY;
    double[] sampler;
    rgba pixelSample;

    pixelSample = new rgba();
    radiusX = gK.width/2;
    radiusY = gK.height/2;
    startX = (gK.width %2 == 0) ? (a-radiusX+1) : (a-radiusX);
    startY = (gK.height %2 == 0) ? (b-radiusY+1) : (b-radiusY);
    endX = a+radiusX;
    endY = b+radiusY;
    sampler = new double[3];
    sampler[0] = 0;
    sampler[1] = 0;
    sampler[2] = 0;
    if (a < radiusX              ||
        a >= sS.length - radiusX ||
        b < radiusY              ||
        b >= sS[a].length - radiusY   ){
      samplerSum = 0d;
      gX = (startX < 0) ? -startX : 0;
      iniGY = (b < radiusY) ? -startY : 0;
      gY = iniGY;
      startX = Math.max(0,startX);
      startY = Math.max(0,startY);
      endX = Math.min(sS.length-1,endX);
      endY = Math.min(sS[a].length-1,endY);
      for (int x=startX;x<=endX;x++){
        for (int y=startY;y<=endY;y++){
          sampler[0] += sS[x][y].r * gK.array[gX][gY];
          sampler[1] += sS[x][y].g * gK.array[gX][gY];
          sampler[2] += sS[x][y].b * gK.array[gX][gY];
          samplerSum += gK.array[gX][gY];
          gY++;
        }
        gY = iniGY;
        gX++;
      }
    }
    else{
      samplerSum = gK.sum;
      gX = 0;
      gY = 0;
      for (int x=startX;x<=endX;x++){
        for (int y=startY;y<=endY;y++){
          sampler[0] += sS[x][y].r * gK.array[gX][gY];
          sampler[1] += sS[x][y].g * gK.array[gX][gY];
          sampler[2] += sS[x][y].b * gK.array[gX][gY];
          gY++;
        }
        gY = 0;
        gX++;
      }
    }
    pixelSample.r = (short)(Math.round(sampler[0]/samplerSum));
    pixelSample.g = (short)(Math.round(sampler[1]/samplerSum));
    pixelSample.b = (short)(Math.round(sampler[2]/samplerSum));
    return pixelSample;
  }
}