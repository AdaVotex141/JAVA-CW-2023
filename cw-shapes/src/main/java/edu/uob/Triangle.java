package edu.uob;

public class Triangle extends TwoDimensionalShape implements MultiVariantShape{
  protected long a;
  protected long b;
  protected long c;
  protected TriangleVariant Variant;
  static int countTri=0;
   // TODO implement me!
  public Triangle(long a, long b, long c) {
      this.a=a;
      this.b=b;
      this.c=c;
      Triangle.countTri++;
      //getVariant();
  }
  public long getLongestSide(){
    return Math.max(a, Math.max(b, c));
  }

  public String toString(){
    return (super.toString()+ " Triangle with sides of length "+a+","+b+","+c);
  }

  public TriangleVariant getVariant() {
    if(a<=0 || b<=0 || c<=0){
      Variant = TriangleVariant.ILLEGAL;
    }else if (isImpossible()) {
      Variant = TriangleVariant.IMPOSSIBLE;
    } else if (isRIGHT()) {
      Variant = TriangleVariant.RIGHT;
    } else if(a == b && b == c && a>0) {
      Variant = TriangleVariant.EQUILATERAL;
    } else if (isFLAT()) {
      Variant = TriangleVariant.FLAT;
    }else if (isISOSCELES()) {
      Variant = TriangleVariant.ISOSCELES;
    } else if (isSCALENE()) {
      Variant = TriangleVariant.SCALENE;
    }
    return Variant;
  }
  // Isosceles: any two equal
  private boolean isISOSCELES(){
    return ((a==b && c!=b) || (c==b && a!=b) || (a==c && b!=c));
  }
  // Scalene: all three different (but not special)
  private boolean isSCALENE(){
  return (a!=b && b!=c && c!=a);
  }

  private boolean isRIGHT(){
    return (isPyTriple(a,b,c) || isPyTriple(b,c,a) || isPyTriple(c,a,b));
  }

  private boolean isPyTriple(long side1, long side2, long side3){
    return side3 * side3 == side1 * side1 + side2 * side2;
  }

  private boolean isFLAT(){
    return a+b==c || a+c==b || b+c==a;
  }
/*  private boolean HelperFlat(int side1,int side2,int side3) {
    return Math.pow(side1, 2) + Math.pow(side2, 2) <= Math.pow(side3, 2);
  }
 */
  private boolean isImpossible(){
    return a + b < c || b + c < a || c + a < b;
  }
/*
  private boolean isIllegal(){
    return (a<=0 || b<=0 || c<=0);
  }
*/
  public static int getCount(){
    return countTri;
  }

  // TODO implement me!
  public double calculateArea() {
      long halfPeri=(a+b+c)/2;
      long area= (long) Math.sqrt(halfPeri*(halfPeri-a)*(halfPeri-b)*(halfPeri-c));
    return (double)area;
  }

  // TODO implement me!
  public int calculatePerimeterLength() {
    return (int)(a+b+c);
  }
}
