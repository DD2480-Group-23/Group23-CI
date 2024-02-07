package src.test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.lang.reflect.Parameter;

import org.junit.Rule;
import org.junit.rules.Timeout;
import org.w3c.dom.views.DocumentView;

import src.main.Decide;
import src.main.Parameters;

import java.util.*;


/**
 * Test Class for DECIDE function.
 * 
 * @author Benjamin Jansson Mbonyimana
 * @author Isadora Fukiat Winter
 * @author Felix Sjögren
 * @author Jonatan Stagge
 */
public class DecideTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);
    Decide trueForAllDecide;
    Decide falseDecide;
    Parameters trueForAllParam;
    Parameters falseParam;
    Decide.CONNECTORS[][] orrForAllLCM;
    Decide.CONNECTORS[][] anddForAllLCM;
    boolean[] truePUV;
    double PI = 3.1415926535;

    @Before
    public void setUp() {
        // If something "global" is needed, ex, set up parameters or something

        // Set up for trueForAllDecide, A Decide object where all LICs should be true

        trueForAllParam = new Parameters(0, 0, 0, 0, 2,
                1, 0, 3, 1, 1, 1,
                1, 1, 1, 1, 1, 1000000,
                1000000, 1000000);
        falseParam = new Parameters(1000000, 1000000, 0, 0, 2,
                1, 0, 6, 1, 1, 1,
                1, 1, 1, 1, 1, 1000000,
                100000, 0);
        double[] trueForAllXCoords = { 0, 1, 1, -1, 0, 0 };
        double[] trueForAllYCoords = { 0, 0, 1, -1, 0, 0 };
        Decide.CONNECTORS[] oneOrrRow = { Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR,
                Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR,
                Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR,
                Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR, Decide.CONNECTORS.ORR };
        Decide.CONNECTORS[][] trueForAllLCM = { oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow,
                oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow, oneOrrRow };
        orrForAllLCM = trueForAllLCM;
        boolean[] trueForAllPUV = { true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                true };
        truePUV = trueForAllPUV;
        trueForAllDecide = new Decide(trueForAllParam, trueForAllXCoords, trueForAllYCoords, 6, trueForAllLCM,
                trueForAllPUV);
        falseDecide = new Decide(falseParam, trueForAllXCoords, trueForAllYCoords, 6, trueForAllLCM,
                trueForAllPUV);

        Decide.CONNECTORS[] anddRow = { Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD,
                Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD,
                Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD,
                Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD, Decide.CONNECTORS.ANDD };
        Decide.CONNECTORS[][] anddMatrix = { anddRow, anddRow, anddRow, anddRow, anddRow, anddRow,
                anddRow, anddRow, anddRow, anddRow, anddRow, anddRow, anddRow, anddRow, anddRow };
        anddForAllLCM = anddMatrix;
    }

    /**
     * Tests the full DECIDE method, ensuring that LAUNCH is true when all conditions are satisfied.
     * Pre-conditions: trueForAllDecide is set up with valid parameters.
     * Post-conditions: LAUNCH should be true after calling DECIDE. All LICs should be true.
    */
    @Test
    public void testFullDeviceMethodLauchTrue() {
        trueForAllDecide.DECIDE();
        assertTrue(trueForAllDecide.LAUNCH);
    }

    /**
     * Tests the full DECIDE method, ensuring that LAUNCH is false when not all conditions are satisfied.
     * Pre-conditions: falseDecide is set up with conditions that are not satisfied.
     * Post-conditions: LAUNCH should be false after calling DECIDE.
    */
    @Test
    public void testFullDeviceMethodLauchFalse() {
        falseDecide.DECIDE();
        assertFalse(trueForAllDecide.LAUNCH);
    }

    /**
     * Test to try invalid input.
     * precondition: None, tests the constructors capability
     * postCondition: Throws illegal argument exception since NUMPOINTS is set to 0 but there are 6 points.
     */
    @Test 
    public void testThirdmaintestInvalidInputs() {
        assertThrows(IllegalArgumentException.class,() -> {
            Decide testDevide = new Decide(falseParam, new double[]{0, 1, 1, -1, 0, 0 }, new double[]{0, 1, 1, -1, 0, 0 }, 0, anddForAllLCM, truePUV);
        });
    }

    /**
     * Tests that LAUNCH is true when all elements in FUV are true.
     * Pre-conditions: Three points with the first two coinciding. FUV is initialized with all true values.
     * Post-conditions: LAUNCH should be true.
    */
    @Test
    public void testLaunchTrue() {
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
        testDecide.FUV = tempBoolArr;

        //test
        testDecide.createLAUNCH();
        assertTrue(testDecide.LAUNCH);
    }

    /**
     * Tests that LAUNCH is false when at least one element in FUV is false.
     * Pre-conditions: Three points with the first two coinciding. FUV is initialized with at least one false value.
     * Post-conditions: LAUNCH should be false.
    */
    @Test
    public void testLaunchFalse() {
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {true, true, true, true, false, true, true, true, true, true, true, true, true, true, true};
        testDecide.FUV = tempBoolArr;

        //temp
        testDecide.createLAUNCH();
        assertFalse(testDecide.LAUNCH);
    }

    /**
     * Tests that FUV is true when all elements in PUM and PUV are true.
     * Pre-conditions: Three points with the first two coinciding. PUM and PUV are initialized with all true values.
     * Post-conditions: All elements in FUV should be true.
    */
    @Test
    public void testFuvIfAllPumAndPuvAreTrue() {
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
        testDecide.PUV=tempBoolArr;
        boolean[][] tempBoolMat = {tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, 
                                    tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, 
                                    tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr};
        testDecide.PUM=tempBoolMat;

        //Test
        testDecide.createFUV();
        for(boolean val : testDecide.FUV) {
            assertTrue(val);
        }
    }

   /**
    * Tests that FUV is false when all elements in PUM are false or false and PUV are true.
    * Pre-conditions: Three points with the first two coinciding. PUM is initialized with all false values, and some elements are set to true in PUV.
    * Post-conditions: All elements in FUV should be false.
   */
    @Test
    public void testFuvIfAllPumAreFalseOrFalseAndPuvAreTrue() {
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
        boolean[] tempBoolArrFalse = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        testDecide.PUV=tempBoolArr;
        boolean[][] tempBoolMat = {tempBoolArrFalse, 
                                    {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
                                    tempBoolArrFalse, tempBoolArrFalse, tempBoolArrFalse, 
                                     {true, true, true, true, true, true, false, true, true, true, true, true, true, true, true},
                                    tempBoolArrFalse, tempBoolArrFalse, tempBoolArrFalse, tempBoolArrFalse, 
                                    tempBoolArrFalse, tempBoolArrFalse, tempBoolArrFalse, 
                                    {true, true, true, true, true, true, true, true, true, true, true, true, true, true, false}, 
                                    tempBoolArrFalse};
        testDecide.PUM=tempBoolMat;

        //Test
        testDecide.createFUV();
        for(boolean val : testDecide.FUV) {
            assertFalse(val);
        }
    }

    /**
     * Tests that FUV is true when all elements in PUV are false.
     * Pre-conditions: Three points with the first two coinciding. PUV is initialized with all false values.
     * Post-conditions: All elements in FUV should be true.
    */
    @Test
    public void testFuvIfAllPuvAreFalse(){
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        testDecide.PUV=tempBoolArr;

        //Test
        testDecide.createFUV();
        for(boolean val : testDecide.FUV) {
            assertTrue(val);
        }
    }

    /**
     * Tests that FUV is true when all elements in PUM are true and all elements in PUV are false.
     * Pre-conditions: Three points with the first two coinciding. PUV is initialized with all false values, and PUM is initialized with all true values.
     * Post-conditions: All elements in FUV should be true.
    */
    @Test
    public void testFivIfAllPumAreTrueAndPuvAreFalse() {
        //setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
        boolean[] tempBoolArrFalse = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        testDecide.PUV=tempBoolArrFalse;
        boolean[][] tempBoolMat = {tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, 
                                    tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, 
                                    tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr, tempBoolArr};
        testDecide.PUM=tempBoolMat;

        //Test
        testDecide.createFUV();
        for(boolean val : testDecide.FUV) {
            assertTrue(val);
        }
    }

    /**
     * Tests that all elements in the PUM matrix are true when CMV is true for all conditions and using the ORR logic.
     * Pre-conditions: Three points with the first two coinciding. CMV is initialized with all true values.
     * Post-conditions: All elements in the PUM matrix should be true.
    */
    @Test
    public void testPumUsingAllTrueAndOrr() {
        // setUp
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = { true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                true };
        testDecide.CMV = tempBoolArr;

        // test
        testDecide.createPUM();
        for (boolean[] row : testDecide.PUM) {
            for (boolean val : row) {
                assertTrue(val);
            }
        }
    }

    /**
     * Tests that all elements in the PUM matrix are false when CMV is false and using the ORR logic.
     * Pre-conditions: Three points with the first two coinciding. CMV is initialized with all false values.
     * Post-conditions: All elements in the PUM matrix should be false.
    */
    @Test
    public void testPumCellsAreIfCmvFalseAndORR() {
        // setup
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                anddForAllLCM, truePUV);
        boolean[] tempBoolArr = { false, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false };
        testDecide.CMV = tempBoolArr;
        testDecide.createPUM();

        // test
        testDecide.createPUM();
        for (boolean[] row : testDecide.PUM) {
            for (boolean val : row) {
                assertFalse(val);
            }
        }
    }

    /**
     * Tests LIC0 when less than three points are provided.
     * Pre-conditions: Two points with coordinates (0, 0) and (1, 1).
     * Post-conditions: The LIC0 evaluation should be true.
    */
    @Test
    public void testLIC0TrueIfLessThanThreePoints() {
        double[] xCoordsTwoPoints = { 0, 1 };
        double[] yCoordsTwoPoints = { 0, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsTwoPoints, yCoordsTwoPoints, 2,
                orrForAllLCM, truePUV);
        assertTrue(testDecide.LIC0());
    }
    
    /**
     * Tests LIC1 when 3 consecutive points are not in a circle.
     * Pre-conditions: Three points with coordinates (1, 1), (4, 4), and (6, 6). Radius set to 1.
     * Post-conditions: The LIC1 evaluation should be true.
    */
    @Test
    public void testLIC1TrueIf3ConsecutivePointsIsNotInCircle(){
        double[] xCoordsThreePoints = {1, 4, 6};
        double[] yCoordsThreePoints = {1, 4, 6};
        trueForAllParam.setRADIUS1(1);
        Decide testDecide = new Decide(trueForAllParam, xCoordsThreePoints, yCoordsThreePoints, 3,
                orrForAllLCM, truePUV);
        assertTrue(testDecide.LIC1());
    }

    /**
     * Tests LIC1 when all 4 consecutive points are inside the circle.
     * Pre-conditions: Four points with coordinates (1, 1), (1, 1), (1.5, 1.5), and (1.75, 1.75). Radius set to 1.
     * Post-conditions: The LIC1 evaluation should be false.
    */
    @Test
    public void testLICFalseIfAllConsecutivePointInCircle(){
        double[] xCoords = {1,1,1.5,1.75};
        double[] yCoords = {1,1,1.5,1.75};
        trueForAllParam.setRADIUS1(1);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 4, orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC1());
    }

    /**
     * Tests LIC2 when points coincide with vertices.
     * Pre-conditions:
     * - First point coincides with a vertex: Coordinates (1, 1), (1, 1), (0, 1).
     * - Third point coincides with a vertex: Coordinates (0, 1), (1, 1), (1, 1).
     * Post-conditions: The LIC2 evaluation should be false in both cases.
    */
    @Test
    public void testLIC2FalseIfPointsCoincide() {
        // First point coincide with vertex
        double[] xCoordsP1andP2coincide = { 1, 1, 0 };
        double[] yCoordsP1andP2coincide = { 1, 1, 1 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 3,
                orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC2());

        // Third point coincide with vertex
        double[] xCoordsP3andP2coincide = { 0, 1, 1 };
        double[] yCoordsP3andP2coincide = { 1, 1, 1 };
        testDecide = new Decide(trueForAllParam, xCoordsP3andP2coincide, yCoordsP3andP2coincide, 3, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC2());
    }

    /**
     * Tests LIC2 when points form a valid right angle.
     * Pre-conditions: Three points forming a right angle: Coordinates (0, 0), (1, 1), (2, 0). Epsilon set to 0.
     * Post-conditions: The LIC2 evaluation should be true.
    */
    @Test
    public void testLIC2TrueIfValidAngle() {
        // A right angle should pass the test if Epsilon is 0
        double[] xCoordsRightAngle = { 0, 1, 2 };
        double[] yCoordsRightAngle = { 0, 1, 0 };
        trueForAllParam.setEPSILON(0);
        Decide testDecide = new Decide(trueForAllParam, xCoordsRightAngle, yCoordsRightAngle, 3, orrForAllLCM,
                truePUV);
        assertTrue(testDecide.LIC2());
    }

    /**
     * Tests LIC2 when points form an invalid right angle.
     * Pre-conditions: Three points forming a right angle: Coordinates (0, 0), (1, 1), (2, 0). Epsilon set close to PI.
     * Post-conditions: The LIC2 evaluation should be false.
    */
    @Test
    public void testLIC2FalseIfInvalidAngle() {
        // A right angle should not pass the test IF epsilon≈PI.
        double[] xCoordsRightAngle = { 0, 1, 2 };
        double[] yCoordsRightAngle = { 0, 1, 0 };
        trueForAllParam.setEPSILON(PI - 0.1);
        Decide testDecide = new Decide(trueForAllParam, xCoordsRightAngle, yCoordsRightAngle, 3, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC2());
    }

    /**
     * Tests LIC3 with a valid triangle that has an area of 30 units.
     * Pre-conditions: Three points forming a triangle with coordinates (0, 0), (3, 10), and (6, 0). Comparison value set to 10.
     * Post-conditions: The LIC3 evaluation should be true.
    */
    @Test
    public void testLIC3TrueIfValidTriangle() {
        // A triangle with an area of 30 units.
        double[] xCoordsTriangle = { 0, 3, 6 };
        double[] yCoordsTriangle = { 0, 10, 0 };
        // Set comparison value to less than 30
        trueForAllParam.setAREA1(10);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 3, orrForAllLCM,
                truePUV);
        assertTrue(testDecide.LIC3());
    }

    /**
     * Tests LIC3 with an invalid triangle that has an area of 30 units.
     * Pre-conditions: Three points forming a triangle with coordinates (0, 0), (3, 10), and (6, 0). Comparison value set to 40.
     * Post-conditions: The LIC3 evaluation should be false.
    */
    @Test
    public void testLIC3FalseIfInvalidTriangle() {
        // A triangle with an area of 30 units.
        double[] xCoordsTriangle = { 0, 3, 6 };
        double[] yCoordsTriangle = { 0, 10, 0 };
        // Set comparison value to more than 30
        trueForAllParam.setAREA1(40);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 3, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC3());
    }

    /**
     * Tests LIC4 with a set of points forming a valid quadrilateral in all four quadrants.
     * Pre-conditions: Four points forming a quadrilateral with coordinates (1, 1), (-1, 1), (-1, -1), and (1, -1).
     * Post-conditions: The LIC4 evaluation should be true.
    */
    @Test
    public void testLIC4TrueIfValidQuadrants() {
        double[] xCoords = {1, -1, -1, 1};
        double[] yCoords = {1, 1, -1, -1};
        
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 4, orrForAllLCM, truePUV);
        
        // Perform the LIC4 check
        assertTrue(testDecide.LIC4());
    }

    /**
     * Tests LIC5 with a set of points forming a convex quadrilateral.
     * Pre-conditions: Four points forming a convex quadrilateral with coordinates (1, 0), (0, 1), (-1, 0), and (0, -1).
     * Post-conditions: The LIC5 evaluation should be true.
    */
    @Test
    public void testLIC5TrueIfValidQuadrants() {
        double[] xCoords = { 1, 0, -1, 0 };
        double[] yCoords = { 0, 1, 0, -1 };
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 4,
                orrForAllLCM, truePUV);
        assertTrue(testDecide.LIC5());
    }

    /**
     * Tests LIC6 with coinciding points and greater distances.
     * Pre-conditions: Eight coinciding points with coordinates (0,0) and one point with coordinates (2,0). Distance parameter set to 2, and NPTS parameter set to 3.
     * Post-conditions: The LIC6 evaluation should be true.
    */
    @Test
    public void testLIC6TrueIfDistanceGreaterForCoincidingPoints(){
        double[] xCoords = {0,0,0,2,0,0,0,0};
        double[] yCoords = {0,0,0,2,0,0,0,0};
        trueForAllParam.setNPTS(3);
        trueForAllParam.setDIST(2);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 8, anddForAllLCM, truePUV);
        assertTrue(testDecide.LIC6());
    }

    /**
     * Tests LIC6 with coinciding points and smaller distances.
     * Pre-conditions: Four coinciding points with coordinates (0,0). Distance parameter set to 2, and NPTS parameter set to 3.
     * Post-conditions: The LIC6 evaluation should be false.
    */
    @Test
    public void testLIC6FalseIfDistanceSmallerForCoincidingPoints(){
        double[] xCoords = {0,0,0,0};
        double[] yCoords = {0,0,0,0};
        trueForAllParam.setNPTS(3);
        trueForAllParam.setDIST(2);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 4, anddForAllLCM, truePUV);
        assertFalse(testDecide.LIC6());
    }

    /**
     * Tests LIC7 with valid triplets.
     * Pre-conditions: Three non-collinear points forming a triangle with coordinates (0,0), (1,1), and (2,0).
     * Post-conditions: The LIC7 evaluation should be true.
    */
    @Test
    public void testLIC7TrueIfValidTriplets() {
        double[] xCoords = { 0, 1, 2 };
        double[] yCoords = { 0, 1, 0 };
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 3,
                orrForAllLCM, truePUV);
        assertTrue(testDecide.LIC7());
    }

    /**
     * Tests LIC8 when all points are contained within a circle.
     * Pre-conditions: Seven points forming a shape contained within a circle. Radius set to 1.
     * Post-conditions: The LIC8 evaluation should be false.
    */
    @Test
    public void testLIC8FalseIfAllContainedInCircle(){
        double[] xCoords = {0,2,1,3,1,3,2};
        double[] yCoords = {0,2,1,3,1,3,2};
        trueForAllParam.setRADIUS1(1);
        trueForAllParam.setAPTS(1);
        trueForAllParam.setBPTS(1);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 7, anddForAllLCM, truePUV);
        assertFalse(testDecide.LIC8());
    }

    /**
     * Tests LIC8 when points are not all contained within a circle.
     * Pre-conditions: Seven points forming a shape not completely contained within a circle. Radius set to 1.
     * Post-conditions: The LIC8 evaluation should be true.
    */
    @Test
    public void testLIC8TrueIfNotContainedInCircle(){
        double[] xCoords = {0,2,1,5,1,7,2};
        double[] yCoords = {0,2,1,5,1,7,2};
        trueForAllParam.setRADIUS1(1);
        trueForAllParam.setAPTS(1);
        trueForAllParam.setBPTS(1);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 7, anddForAllLCM, truePUV);
        assertTrue(testDecide.LIC8());
    }

    /**
     * Tests LIC9 when less than five points are provided.
     * Pre-conditions: Three points forming a triangle.
     * Post-conditions: The LIC9 evaluation should be false.
    */
    @Test
    public void testLIC9FalseIfLessThanFivePoints() {
        double[] xCoordsThreePoints = { 0, 3, 6 };
        double[] yCoordsThreePoints = { 0, 10, 0 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsThreePoints, yCoordsThreePoints, 3,
                orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC9());
    }

    /**
     * Tests LIC9 when points coincide.
     * Pre-conditions: Eight points with coinciding vertices, violating the angle condition.
     * Post-conditions: The LIC9 evaluation should be false.
    */
    @Test
    public void testLIC9FalseIfPointsCoincide() {
        // First point coincide with vertex
        trueForAllParam.setCPTS(2);
        trueForAllParam.setDPTS(3);
        double[] xCoordsP1andP2coincide = { 9, 0, 0, 9, 0, 0, 0, 8 };
        double[] yCoordsP1andP2coincide = { 9, 0, 0, 9, 0, 0, 0, 8 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsP1andP2coincide, yCoordsP1andP2coincide, 8,
                orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC9());

        // Third point coincide with vertex
        double[] xCoordsP3andP2coincide = { 8, 0, 0, 9, 0, 0, 0, 9 };
        double[] yCoordsP3andP2coincide = { 8, 0, 0, 9, 0, 0, 0, 9 };
        testDecide = new Decide(trueForAllParam, xCoordsP3andP2coincide, yCoordsP3andP2coincide, 8, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC9());
    }

    /**
     * Tests LIC9 with a valid angle.
     * Pre-conditions: Eight points forming a valid angle (e.g., a right angle).
     * Post-conditions: The LIC9 evaluation should be true.
    */
    @Test
    public void testLIC9TrueIfValidAngle() {
        // A right angle should pass the test if Epsilon is 0
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setCPTS(2);
        trueForAllParam.setDPTS(3);
        double[] xCoordsRightAngle = { 0, 9, 9, 1, 9, 9, 9, 2 };
        double[] yCoordsRightAngle = { 0, 9, 9, 1, 9, 9, 9, 0 };
        trueForAllParam.setEPSILON(0);
        Decide testDecide = new Decide(trueForAllParam, xCoordsRightAngle, yCoordsRightAngle, 8, orrForAllLCM,
                truePUV);
        assertTrue(testDecide.LIC9());
    }

    /**
     * Tests LIC9 with an invalid angle.
     * Pre-conditions: Eight points forming an invalid angle (e.g., a right angle with epsilon≈PI).
     * Post-conditions: The LIC9 evaluation should be false.
    */
    @Test
    public void testLIC9FalseIfInvalidAngle() {
        // A right angle should not pass the test IF epsilon≈PI.
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setCPTS(2);
        trueForAllParam.setDPTS(3);
        double[] xCoordsRightAngle = { 0, 9, 9, 1, 9, 9, 9, 2 };
        double[] yCoordsRightAngle = { 0, 9, 9, 1, 9, 9, 9, 0 };
        trueForAllParam.setEPSILON(PI - 0.1);
        Decide testDecide = new Decide(trueForAllParam, xCoordsRightAngle, yCoordsRightAngle, 8, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC9());
    }

    /**
     * Tests LIC10 when less than five points are provided.
     * Pre-conditions: Three points provided, which is less than the required five points.
     * Post-conditions: The LIC10 evaluation should be false.
    */
    @Test
    public void testLIC10FalseIfLessThanFivePoints() {
        double[] xCoordsThreePoints = { 0, 3, 6 };
        double[] yCoordsThreePoints = { 0, 10, 0 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsThreePoints, yCoordsThreePoints, 3,
                orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC10());
    }

    /**
     * Tests LIC10 for a set of eight points forming a closed shape with an area less than the threshold (10 units).
     * Pre-conditions: Eight points forming a closed shape, meeting point count requirements for P1, P2, and P3.
     * Post-conditions: LIC10 should be true.
    */
    @Test
    public void testLIC10TrueIfValidTriangle() {
        // A triangle with an area of 30 units.
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setEPTS(2);
        trueForAllParam.setFPTS(3);
        double[] xCoordsTriangle = { 0, 1, 1, 3, 1, 1, 1, 6 };
        double[] yCoordsTriangle = { 0, 1, 1, 10, 1, 1, 1, 0 };
        // Set comparison value to less than 30
        trueForAllParam.setAREA1(10);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 8, orrForAllLCM,
                truePUV);
        assertTrue(testDecide.LIC10());
    }

    /**
     * Tests LIC10 for a set of eight points forming a closed shape with an area less than the threshold (30 units).
     * Pre-conditions: Eight points forming a closed shape, meeting point count requirements for P1, P2, and P3.
     * Post-conditions: LIC10 should be true.
    */
    @Test
    public void testLIC10FalseIfInvalidTriangle() {
        // A triangle with an area of 30 units.
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setEPTS(2);
        trueForAllParam.setFPTS(3);
        double[] xCoordsTriangle = { 0, 1, 1, 3, 1, 1, 1, 6 };
        double[] yCoordsTriangle = { 0, 1, 1, 10, 1, 1, 1, 0 };
        // Set comparison value to more than 30
        trueForAllParam.setAREA1(40);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 8, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC10());
    }

    /**
     * Tests LIC11 for a quadrilateral with a negative difference in consecutive areas.
     * Pre-conditions: Quadrilateral with seven points, meeting P1 and P2 requirements.
     * Post-conditions: LIC11 should be true.
    */
    @Test
    public void testLIC11TrueIfDifferenceNegative(){
        double [] xCoords = {0,1,2,-1,4,5,6};
        double [] yCoords = {0,1,2,-1,4,5,6};
        trueForAllParam.setGPTS(2);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 7, anddForAllLCM, truePUV);
        assertTrue(testDecide.LIC11());
    }

    /**
     * Tests LIC11 for a quadrilateral with a positive difference in consecutive areas.
     * Pre-conditions: Quadrilateral with seven points, meeting P1 and P2 requirements.
     * Post-conditions: LIC11 should be false.
    */
    @Test
    public void testLIC11FalseIfDifferencePositive(){
        double [] xCoords = {0,1,2,3,4,5,6};
        double [] yCoords = {0,1,2,3,4,5,6};
        trueForAllParam.setGPTS(2);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 7, anddForAllLCM, truePUV);
        assertFalse(testDecide.LIC11());
    }

    /**
     * Tests LIC12 for a set of three points forming valid triplets.
     * Pre-conditions: Three points provided.
     * Post-conditions: LIC12 should be true.
    */
    @Test
    public void testLIC12TrueIfValidTriplets() {
        double[] xCoords = { 0, 1, 2 };
        double[] yCoords = { 0, 1, 0 };
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 3,
                orrForAllLCM, truePUV);
        assertTrue(testDecide.LIC12());
    }

    /**
     * Tests LIC13 when points are not contained in a circle, but one triangle is.
     * Pre-conditions: Nine points provided.
     * Post-conditions: LIC13 should be true.
    */
    @Test
    public void testLIC13TrueIfPointsNotContainedInCircleButOneTriangleIs(){
        double[] xCoords = {0,2,4,6,8,10,12,1,2};
        double[] yCoords = {0,2,4,6,8,10,12,1,1};
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 9, anddForAllLCM, truePUV);
        assertTrue(testDecide.LIC13());
    }

    /**
     * Tests LIC13 when points are contained in a circle.
     * Pre-conditions: Seven points forming a convex quadrilateral, meeting point count requirements for P1 and P2.
     * Post-conditions: LIC13 should be false.
    */
    @Test
    public void testLIC13FalseIfPointsContainedInCircle(){
        double[] xCoords = {0,2,2,2,2,0,0};
        double[] yCoords = {0,2,2,2,2,2,2};
        trueForAllParam.setRADIUS1(2);
        trueForAllParam.setRADIUS2(2);
        trueForAllParam.setAPTS(1);
        trueForAllParam.setBPTS(1);
        Decide testDecide = new Decide(trueForAllParam, xCoords, yCoords, 7, anddForAllLCM, truePUV);
        assertFalse(testDecide.LIC13());
    }
    
    /**
     * Tests LIC14 with less than five points.
     * Pre-conditions: Three points provided, which is less than the required five points.
     * Post-conditions: LIC14 should be false.
    */
    @Test
    public void testLIC14FalseIfLessThanFivePoints() {
        double[] xCoordsThreePoints = { 0, 3, 6 };
        double[] yCoordsThreePoints = { 0, 10, 0 };
        Decide testDecide = new Decide(trueForAllParam, xCoordsThreePoints, yCoordsThreePoints, 3,
                orrForAllLCM, truePUV);
        assertFalse(testDecide.LIC14());
    }

    /**
     * Tests LIC14 with valid triangles.
     * Pre-conditions: Eight points forming two valid triangles, meeting point count requirements for P1, P2, and P3.
     * Post-conditions: LIC14 should be true.
    */
    @Test
    public void testLIC14TrueIfValidTriangles() {
        // A triangle with an area of 30 units.
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setEPTS(2);
        trueForAllParam.setFPTS(3);
        double[] xCoordsTriangle = { 0, 1, 1, 3, 1, 1, 1, 6 };
        double[] yCoordsTriangle = { 0, 1, 1, 10, 1, 1, 1, 0 };
        // Set first triangle's comparison value to less than 30
        trueForAllParam.setAREA1(10);
        // Set second triangle's comparison value to more than 30
        trueForAllParam.setAREA2(40);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 8, orrForAllLCM,
                truePUV);
        assertTrue(testDecide.LIC14());
    }

    /**
     * Tests LIC14 with an invalid triangle.
     * Pre-conditions: Eight points forming two triangles, meeting point count requirements for P1, P2, and P3.
     * Post-conditions: LIC14 should be false.
    */
    @Test
    public void testLIC14FalseIfInvalidTriangle() {
        // A triangle with an area of 30 units.
        // P1, then 2 points, then P2, then 3 points, then P3
        trueForAllParam.setEPTS(2);
        trueForAllParam.setFPTS(3);
        double[] xCoordsTriangle = { 0, 1, 1, 3, 1, 1, 1, 6 };
        double[] yCoordsTriangle = { 0, 1, 1, 10, 1, 1, 1, 0 };
        // Set comparison value to less than 30 (OK)
        trueForAllParam.setAREA1(10);
        // Also set second triangle's comparison value to less than 30 (Not OK)
        trueForAllParam.setAREA2(20);
        Decide testDecide = new Decide(trueForAllParam, xCoordsTriangle, yCoordsTriangle, 8, orrForAllLCM,
                truePUV);
        assertFalse(testDecide.LIC14());
    }
}