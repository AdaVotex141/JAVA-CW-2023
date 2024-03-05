package edu.uob;

public class Shapes {

  // TODO use this class as then entry point; play around with your shapes, etc
  public static void main(String[] args) {
    //Triangle testTriangle = new Triangle(5, 7, 9);
//    long longestSide = testTriangle.getLongestSide();
//    System.out.println("The longest side of the triangle is " + longestSide);

    TwoDimensionalShape shape;
    shape=new Circle(2);
    shape.setColour(Colour.GREEN);
    System.out.println(shape);

    shape=new Rectangle(2,3);
    shape.setColour(Colour.BLUE);
    System.out.println(shape);

//    shape=new Triangle(4,5,6);
//    shape.setColour(Colour.RED);
//    System.out.println(shape);

    TwoDimensionalShape[] array=new TwoDimensionalShape[100];
    for(int i=0;i<100;i++){
      double randomValue=Math.random();
      if (randomValue<0.33){
          shape=new Circle((int)(randomValue*10));
          array[i]=shape;
      }else if (randomValue<0.66 && randomValue>0.33){
          shape=new Rectangle((int)(randomValue*10),(int)(randomValue*5));
          array[i]=shape;
      }else if (randomValue>0.66){
        shape=new Triangle((int)(randomValue*30),(int)(randomValue*50),(int)(randomValue*40));
        array[i]=shape;
      }
    }
    //Task 4: print
    int count=0;
    for(int i=0;i<100;i++){
      if(array[i] instanceof Triangle){
        count+=1;
      }
    }

    System.out.print("There are " + count +" Triangles.\n");
    //Task 5:Class Variables
    System.out.print("There are " + Triangle.getCount() +" Triangles");
    //Task 6:Casting



  }
  }
