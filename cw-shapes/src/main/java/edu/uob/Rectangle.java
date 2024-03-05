package edu.uob;

public class Rectangle extends TwoDimensionalShape {
  protected int width;
  protected int height;

  public Rectangle(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public double calculateArea() {
    return width * height;
  }

  public int calculatePerimeterLength() {
    return 2 * (width + height);
  }

  public String toString() {
    return super.toString()+" Rectangle of dimensions " + width + " x " + height;
  }
}
