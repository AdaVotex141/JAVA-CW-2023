package edu.uob;

public abstract class TwoDimensionalShape {
  protected Colour shapeColour;
  public TwoDimensionalShape() {

  }
  public void setColour(Colour shapeColour){
    this.shapeColour=shapeColour;
  }
  public Colour getColour(){
    return shapeColour;
  }
  public String toString(){
    return "This is a " + getColour();
  }
  public abstract double calculateArea();

  public abstract int calculatePerimeterLength();
}
