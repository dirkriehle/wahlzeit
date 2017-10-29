package org.wahlzeit.model;

import org.junit.*;

public class CoordinateTest {

     static final Double VALUE_EXCEEDING_COORD_MAXVALUE= Double.MAX_VALUE - 1E291;

     private Coordinate noWhere;
     private Coordinate octantIa;
     private Coordinate octantIb;
     private Coordinate octantVII;
     private Coordinate layerXYa;
     private Coordinate layerXYb;

    @Before
    public  void initTest(){
        noWhere=new NoWhereCoordinate();
        octantIa =new Coordinate(1.0,2.0,3.0);
        octantIb =new Coordinate(1.0,2.0,3.0);
        octantVII =new Coordinate(-1.0,-2.0,-3.0);
        layerXYa= new Coordinate(0,1,0);
        layerXYb= new Coordinate(5,1,0);
    }

    /*CreationTests*/
    @Test
    public void createCoordinate_initThroughSetter_notNull(){
        Coordinate foo = new Coordinate();
        foo.setX(0.0);
        foo.setY(0.0);
        foo.setZ(-1.0);
        Assert.assertNotNull(foo);
    }

    @Test
    public void createCoordinate_initThroughConstructor_notNull(){
        Coordinate bar = new Coordinate(0.0,0.0,-1.0);
        Assert.assertNotNull(bar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInZ_shouldThroughException(){
        new Coordinate(1,1,VALUE_EXCEEDING_COORD_MAXVALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInX_shouldThroughException(){
        new Coordinate(1,VALUE_EXCEEDING_COORD_MAXVALUE,1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_withExceedingMaxValueInY_shouldThroughIllegalArgumentException(){
        new Coordinate(VALUE_EXCEEDING_COORD_MAXVALUE,1,1);
    }

    @Test
    public void createCoordinate_belowMaxValue_shouldNOTThroughExceptions(){
        Double valueBelowCoordMaxValue =Double.MAX_VALUE - 1E292;
        try{
            new Coordinate(1,1,valueBelowCoordMaxValue);
            new Coordinate(1,valueBelowCoordMaxValue,1);
            new Coordinate(valueBelowCoordMaxValue,1,1);
        }catch (Exception ex){
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }

    @Test
    public void createCoordinate_withCoordMaxValue_shouldNOTThroughExceptions(){
        try{
            new Coordinate(1,1,Coordinate.MAX_VALUE);
            new Coordinate(1,Coordinate.MAX_VALUE,1);
            new Coordinate(Coordinate.MAX_VALUE,1,1);
        }catch (Exception ex){
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCoordinate_belowCoordMinValues_shouldThroughIllegalArgumentException(){
        new Coordinate(1,1,-VALUE_EXCEEDING_COORD_MAXVALUE);
    }

    @Test
    public void createCoordinate_aboveCoordMinValue_shouldNOTThroughExceptions(){
        try{
            new Coordinate(1,1,-Coordinate.MAX_VALUE);
            new Coordinate(1,-Coordinate.MAX_VALUE,1);
            new Coordinate(-Coordinate.MAX_VALUE,1,1);
        }catch (Exception ex){
            Assert.fail("Exception was thrown but shouldn't.");
        }
    }

    /*EqualityTests*/
    @Test
    public void octantIa_andB_areEqual(){
        Assert.assertEquals(octantIa, octantIb);
    }

    @Test
    public void octantVII_andOctantIa_areNotEqual(){
        Assert.assertNotEquals(octantIa, octantVII);
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void someString_andOctantIa_areNotEqual(){
        Assert.assertFalse(octantIa.equals("fooLocation"));
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void octantIa_andNull_areNotEqual(){
        Assert.assertFalse(octantIa.equals(null));
    }

    @Test
    public void coordinateNoWhere_andNoWhere_areNotEqual(){
        Assert.assertFalse(noWhere.equals(new NoWhereCoordinate()));
    }

    @Test
    public void someCoordinate_andNoWhere_areNotEqual(){
        Assert.assertFalse(octantIa.equals(noWhere));
        Assert.assertFalse(octantIb.equals(noWhere));
        Assert.assertFalse(octantVII.equals(noWhere));
    }

    @Test
    public void octantIa_multipliedByMinusOne_isOctantVII(){
        octantIa.setX(octantIa.getX()*(-1));
        octantIa.setY(octantIa.getY()*(-1));
        octantIa.setZ(octantIa.getZ()*(-1));

        Assert.assertTrue(octantIa.equals(octantVII));
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateX_areNotEqual(){
        octantIb.setX(octantIb.getX()+1);
        Assert.assertNotEquals(octantIa,octantIb);
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateY_areNotEqual(){
        octantIb.setY(octantIb.getY()+1);
        Assert.assertNotEquals(octantIa,octantIb);
    }

    @Test
    public void octantIa_andB_withDifferentOrdinateZ_areNotEqual(){
        octantIb.setZ(octantIb.getZ()+1);
        Assert.assertNotEquals(octantIa,octantIb);
    }

    /*DistanceTests*/
    @Test
    public void distanceOfCoordinateA_andB_is0(){
        CheckDistance(octantIa, octantIb,0,0);
    }

    @Test
    public void distanceOfLayerXYa_andXYb_is5(){ CheckDistance(layerXYa,layerXYb,5,0); }

    @Test
    public void distanceOfCoordinateB_andC_is7_5(){
        CheckDistance(octantIb, octantVII,7.5,0.02);
    }

    @Test
    public void distanceOfAnyCoord_andNoWhere_isMinus1(){
        CheckDistance(octantIa,noWhere,-1,0);
        CheckDistance(noWhere, octantIb,-1,0);
    }

    @Test
    public void distanceOfAnyCoord_andNull_isMinus1(){
        CheckDistance(octantIa,null,-1,0);
        CheckDistance(noWhere, null,-1,0);
    }

    private void CheckDistance(Coordinate first, Coordinate second, double expectedDistance, double tolerance){
        double distance= first.getDistance(second);
        Assert.assertEquals(expectedDistance,distance,tolerance);
    }
}
