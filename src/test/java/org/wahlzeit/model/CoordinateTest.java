package org.wahlzeit.model;

import org.junit.*;

public class CoordinateTest {

     private Coordinate noWhere;
     private Coordinate coordinateA;
     private Coordinate coordinateB;
     private Coordinate coordinateC;

    @Before
    public  void initTest(){
        noWhere=new NoWhereCoordinate();
        coordinateA=new Coordinate(1.0,2.0,3.0);
        coordinateB=new Coordinate(1.0,2.0,3.0);
        coordinateC=new Coordinate(-1.0,-2.0,-3.0);
    }

    @Test
    public void createCoordinateThroughSetter(){
        Coordinate foo = new Coordinate();
        foo.setX(0.0);
        foo.setY(0.0);
        foo.setZ(-1.0);
        Assert.assertNotNull(foo);
    }

    @Test
    public void createCoordinateThroughConstructor(){
        Coordinate bar = new Coordinate(0.0,0.0,-1.0);
        Assert.assertNotNull(bar);
    }

    @Test
    public void coordinateB_andA_areEqual(){
        Assert.assertEquals(coordinateA,coordinateB);
    }

    @Test
    public void coordinateC_andA_areNotEqual(){
        Assert.assertNotEquals(coordinateA,coordinateC);
    }

    @Test
    public void SomeString_andA_areNotEqual(){
        Assert.assertFalse(coordinateA.equals("fooLocation"));
    }

    @Test
    public void coordinateA_andNull_areNotEqual(){
        Assert.assertFalse(coordinateA.equals(null));
    }

    @Test
    public void coordinateNoWhere_andNoWhere_areNotEqual(){
        Assert.assertFalse(noWhere.equals(noWhere));
    }

    @Test
    public void someCoordinate_andNoWhere_areNotEqual(){
        Assert.assertFalse(coordinateA.equals(noWhere));
        Assert.assertFalse(coordinateB.equals(noWhere));
        Assert.assertFalse(coordinateC.equals(noWhere));
    }

    @Test
    public void coordinateA_multipliedByMinusOne_isC(){
        coordinateA.setX(coordinateA.getX()*(-1));
        coordinateA.setY(coordinateA.getY()*(-1));
        coordinateA.setZ(coordinateA.getZ()*(-1));

        Assert.assertTrue(coordinateA.equals(coordinateC));
    }

    @Test
    public void distanceOfCoordinateA_andB_is0(){
        CheckDistance(coordinateA,coordinateB,0,0);
    }

    @Test
    public void distanceOfCoordinateB_andC_is7_5(){
        CheckDistance(coordinateB,coordinateC,7.5,0.02);
    }

    @Test
    public void distanceOfAnyCoord_andNoWhere_isMinus1(){
        CheckDistance(coordinateA,noWhere,-1,0);
        CheckDistance(noWhere,coordinateB,-1,0);
    }

    private void CheckDistance(Coordinate first, Coordinate second, double expectedDistance, double tollerance){
        double distance= first.getDistance(second);
        Assert.assertEquals(expectedDistance,distance,tollerance);
    }
}
