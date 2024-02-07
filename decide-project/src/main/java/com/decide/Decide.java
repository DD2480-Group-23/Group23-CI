package com.decide;

import java.lang.Math;
import java.lang.Exception;

/**
 * Class for DECIDE function.
 * DD2480 Assignment 1.
 * 
 * @author Benjamin Jansson Mbonyimana
 * @author Isadora Fukiat Winter
 * @author Felix Sjögren
 * @author Jonatan Stagge
 */

public class Decide {

    // --------- Constants ---------
    double PI = 3.1415926535;

    // --------- Type declarations ---------
    public enum CONNECTORS {
        NOTUSED,
        ORR,
        ANDD
    }

    enum COMPTYPE {
        LT,
        EQ,
        GT
    }

    // --------- Global Variables ---------
    // Inputs to the decide function
    Parameters PARAMETERS;

    // Number of data points.
    int NUMPOINTS;

    // Array of 100 doubles. X coordinates of data points.
    double[] X_COORDINATES;

    // Array of 100 doubles. Y coordinates of data points.
    double[] Y_COORDINATES;

    // 2D array of [15, 15] connnectors. Logical Connector Matrix.
    CONNECTORS[][] LCM;

    // 2D array of [15, 15] booleans. Preliminary Unlocking Matrix.
    public boolean[][] PUM;

    // Number of LICs
    int NUMLICS = 15;

    // Array of 15 booleans. Preliminary Unlocking Vector.
    public boolean[] PUV;

    // Array of 15 booleans. Conditions Met Vector.
    public boolean[] CMV;

    // Array of 15 booleans. Final Unlocking Vector.
    public boolean[] FUV;

    // Decision variable.
    public boolean LAUNCH;

    // --------- Constructor ---------
    public Decide(Parameters PARAMETERS, double[] X_COORDINATES, double[] Y_COORDINATES, int NUMPOINTS,
            CONNECTORS[][] LCM, boolean[] PUV) {
        if (NUMPOINTS != X_COORDINATES.length || X_COORDINATES.length != Y_COORDINATES.length) {

            throw new IllegalArgumentException("invalid input");
        }
        this.PARAMETERS = PARAMETERS;
        this.X_COORDINATES = X_COORDINATES;
        this.Y_COORDINATES = Y_COORDINATES;
        this.NUMPOINTS = NUMPOINTS;
        this.LCM = LCM;
        this.PUV = PUV;

        PUM = new boolean[15][15];
        CMV = new boolean[15];
        FUV = new boolean[15];
    }

    // --------- Main Function ---------

    /**
     * DECIDE - Main function to evaluate Launch Interceptor Conditions and make a
     * decision.
     * Preconditions: PARAMETERS, X_COORDINATES, Y_COORDINATES, NUMPOINTS, LCM, and
     * PUV are valid.
     * Postconditions: LAUNCH is set based on the evaluation of Launch Interceptor
     * Conditions.
     */
    public void DECIDE() {
        createCMV();
        createPUM();
        createFUV();
        createLAUNCH();
        if (LAUNCH)
            System.out.println("YES");
        else
            System.out.println("NO");
    }

    /**
     * createLAUNCH - Sets the LAUNCH variable based on the FUV vector.
     * Preconditions: FUV is a valid boolean array.
     * Postconditions: LAUNCH is set to true if all elements in FUV are true;
     * otherwise, it is set to false.
     */
    public void createLAUNCH() {
        for (boolean FuvElem : FUV) {
            if (!FuvElem) {
                LAUNCH = false;
                return;
            }
        }
        LAUNCH = true;
    }

    /**
     * createFUV - Creates the Final Unlocking Vector (FUV) based on PUV and PUM.
     * Preconditions: PUV and PUM are valid input arrays.
     * Postconditions: FUV is set based on the evaluation of PUV and PUM arrays.
     */
    public void createFUV() {
        boolean breakflag;
        for (int i = 0; i < NUMLICS; i++) {
            breakflag = false;
            if (!PUV[i]) {
                FUV[i] = true;
            } else {
                for (int j = 0; j < NUMLICS; j++) {
                    if (!PUM[i][j] && j != i) {
                        FUV[i] = false;
                        breakflag = true;
                        break;
                    }
                }
                if (!breakflag) {
                    FUV[i] = true;
                }
            }
        }
    }

    /**
     * createPUM - Creates the Preliminary Unlocking Matrix (PUM) based on LCM and
     * CMV.
     * Preconditions: LCM and CMV are valid input arrays.
     * Postconditions: PUM is set based on the evaluation of LCM and CMV arrays.
     */
    public void createPUM() {
        CONNECTORS logVar;
        for (int i = 0; i < NUMLICS; i++) {
            for (int j = i; j < NUMLICS; j++) {
                logVar = LCM[i][j];
                switch (logVar) {
                    case NOTUSED:
                        PUM[i][j] = true;
                        PUM[j][i] = true;
                        break;

                    case ORR:
                        if (CMV[i] || CMV[j]) {
                            PUM[i][j] = true;
                            PUM[j][i] = true;
                        } else {
                            PUM[i][j] = false;
                            PUM[j][i] = false;
                        }
                        break;

                    case ANDD:
                        if (CMV[i] && CMV[j]) {
                            PUM[i][j] = true;
                            PUM[j][i] = true;
                        } else {
                            PUM[i][j] = false;
                            PUM[j][i] = false;
                        }
                        break;

                    default: // shouldn't get here.
                        PUM[i][j] = false;
                        PUM[j][i] = false;
                        break;
                }
            }
        }
    }

    /**
     * createCMV - Creates the Conditions Met Vector (CMV) based on the evaluation
     * of Launch Interceptor Conditions.
     * Preconditions: LIC0(), LIC1(), ..., LIC14() are valid and represent the
     * evaluation of specific Launch Interceptor Conditions.
     * Postconditions: CMV is set based on the evaluation of Launch Interceptor
     * Conditions.
     */
    public void createCMV() {
        CMV[0] = LIC0();
        CMV[1] = LIC1();
        CMV[2] = LIC2();
        CMV[3] = LIC3();
        CMV[4] = LIC4();
        CMV[5] = LIC5();
        CMV[6] = LIC6();
        CMV[7] = LIC7();
        CMV[8] = LIC8();
        CMV[9] = LIC9();
        CMV[10] = LIC10();
        CMV[11] = LIC11();
        CMV[12] = LIC12();
        CMV[13] = LIC13();
        CMV[14] = LIC14();
    }

    // --------- Help Functions ---------

    /**
     * LIC0 - Determines if at least one pair of consecutive data points has a
     * distance greater than LENGTH1.
     * Preconditions: PARAMETERS, X_COORDINATES, Y_COORDINATES, and NUMPOINTS are
     * valid.
     * Postconditions: Returns true if such a pair exists; false otherwise.
     */
    public boolean LIC0() {
        double length1 = PARAMETERS.LENGTH1;

        for (int i = 0; i < NUMPOINTS - 1; i++) {
            double distance = calculateDistance(X_COORDINATES[i], Y_COORDINATES[i], X_COORDINATES[i + 1],
                    Y_COORDINATES[i + 1]);
            if (distance > length1) {
                return true;
            }
        }
        return false;
    }

    /**
     * calculateDistance - Helper function to compute the distance between two
     * points.
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * LIC1 - Checks for a circle with radius RADIUS1 among three consecutive data
     * points.
     * Preconditions: RADIUS1, X_COORDINATES, and Y_COORDINATES are valid.
     * Postconditions: Returns true if such a circle exists; false otherwise.
     */
    public boolean LIC1() {
        double radius = PARAMETERS.RADIUS1;
        for (int i = 0; i < NUMPOINTS - 2; i++) {
            if (!isContainedInCircle(radius, X_COORDINATES[i], Y_COORDINATES[i], X_COORDINATES[i + 1],
                    Y_COORDINATES[i + 1], X_COORDINATES[i + 2], Y_COORDINATES[i + 2])) {
                return true;
            }
        }
        return false;
    }

    /**
     * isContainedInCircle - Checks if a circle with a given radius contains three
     * specified points.
     */
    private boolean isContainedInCircle(double radius, double x1, double y1, double x2, double y2, double x3,
            double y3) {
        if ((Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= (radius * 2))
                && (Math.sqrt(Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2)) <= (radius * 2))
                && (Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2)) <= (radius * 2))) {
            return true;
        }
        return false;
    }

    /**
     * LIC2 - Checks if there exists an angle between consecutive vectors formed by
     * three points (p1, p2, p3) that is not within the specified range.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES) and EPSILON are
     * valid.
     * Postconditions: Returns true if the angle condition is met; false otherwise.
     */
    public boolean LIC2() {
        double x1, x2, x3, y1, y2, y3;
        for (int i = 0; i < NUMPOINTS - 2; i++) {
            x1 = X_COORDINATES[i];
            y1 = Y_COORDINATES[i];
            x2 = X_COORDINATES[i + 1];
            y2 = Y_COORDINATES[i + 1];
            x3 = X_COORDINATES[i + 2];
            y3 = Y_COORDINATES[i + 2];
            if (!(x1 == x2 && y1 == y2) && !(x3 == x2 && y3 == y2)) { // Neither p1 nor p3 coincide with vertex (p2).
                // Calculate angle by creating vectors and using dot product
                double dot_product = (x1 - x2) * (x3 - x2) + (y1 - y2) * (y3 - y2);
                double magnitude_v1 = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                double magnitude_v2 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
                double cos_angle = dot_product / (magnitude_v1 * magnitude_v2);
                double angle = Math.acos(cos_angle);
                // Check if angle holds any of the two conditions
                if (angle < (PI - PARAMETERS.EPSILON) || angle > (PI + PARAMETERS.EPSILON)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * LIC3 - Checks if there exists a triangle formed by three consecutive points
     * (p1, p2, p3) with an area greater than the specified value.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES) and AREA1 are
     * valid.
     * Postconditions: Returns true if the area condition is met; false otherwise.
     */
    public boolean LIC3() {
        double x1, x2, x3, y1, y2, y3, area;
        for (int i = 0; i < NUMPOINTS - 2; i++) {
            x1 = X_COORDINATES[i];
            y1 = Y_COORDINATES[i];
            x2 = X_COORDINATES[i + 1];
            y2 = Y_COORDINATES[i + 1];
            x3 = X_COORDINATES[i + 2];
            y3 = Y_COORDINATES[i + 2];
            area = 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
            if (area > PARAMETERS.AREA1)
                return true;
        }
        return false;
    }

    /**
     * LIC4 - Checks if there exist consecutive points that together visit more than
     * the specified number of quadrants.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), QPTS, and QUADS
     * are valid.
     * Postconditions: Returns true if the condition is met; false otherwise.
     */
    public boolean LIC4() {
        int qPts = PARAMETERS.QPTS;
        int quads = PARAMETERS.QUADS;
        if (NUMPOINTS < qPts + quads - 1) {
            return false;
        }
        for (int i = 0; i <= NUMPOINTS - qPts; i++) {
            int[] quadrants = new int[4];
            for (int j = i; j < i + qPts; j++) {
                int quadrant = 0;
                if (X_COORDINATES[j] > 0 && Y_COORDINATES[j] > 0) {
                    quadrant = 1;
                } else if (X_COORDINATES[j] < 0 && Y_COORDINATES[j] > 0) {
                    quadrant = 2;
                } else if (X_COORDINATES[j] < 0 && Y_COORDINATES[j] < 0) {
                    quadrant = 3;
                } else if (X_COORDINATES[j] > 0 && Y_COORDINATES[j] < 0) {
                    quadrant = 4;
                } else {
                    if (X_COORDINATES[j] == 0 && Y_COORDINATES[j] == 0) {
                        quadrant = 1;
                    } else if (X_COORDINATES[j] == 0) {
                        quadrant = (Y_COORDINATES[j] > 0) ? 2 : 4;
                    } else {
                        quadrant = (X_COORDINATES[j] > 0) ? 1 : 3;
                    }
                }
                quadrants[quadrant - 1] = 1;
            }
            int count = 0;
            for (int value : quadrants) {
                count += value;
            }
            if (count > quads) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC5 - Checks if there exists at least one set of two consecutive data points
     * such that the first point is to the right of (in terms of increasing
     * x-coordinate values) the second point.
     * Preconditions: X_COORDINATES and NUMPOINTS are valid.
     * Postconditions: Returns true if the condition is met; false otherwise.
     */
    public boolean LIC5() {
        for (int i = 0; i < NUMPOINTS - 1; i++) {
            if (X_COORDINATES[i + 1] - X_COORDINATES[i] < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC6 - Checks if there exists at least one set of three consecutive data
     * points that are collinear (lie on the same straight line) and have a distance
     * greater than the specified value from each other.
     * Preconditions: X_COORDINATES, Y_COORDINATES, NUMPOINTS, NPTS, and DIST are
     * valid.
     * Postconditions: Returns true if the condition is met; false otherwise.
     */
    public boolean LIC6() {
        int N_POINTS = PARAMETERS.NPTS;
        double DIST = PARAMETERS.DIST;
        if (NUMPOINTS < 3 || N_POINTS < 3 || DIST < 0 || (NUMPOINTS - N_POINTS < 0))
            return false;
        for (int i = 0; i < NUMPOINTS - (N_POINTS - 1); i++) {
            double x1 = X_COORDINATES[i];
            double y1 = Y_COORDINATES[i];
            double xN = X_COORDINATES[i + N_POINTS - 1];
            double yN = Y_COORDINATES[i + N_POINTS - 1];
            if ((x1 == xN) && (y1 == yN)) {
                for (int k = 0; k < NUMPOINTS - N_POINTS; k++) {
                    if (Math.sqrt(Math.pow(x1 - X_COORDINATES[k], 2) + Math.pow(y1 - Y_COORDINATES[k], 2)) > DIST) {
                        return true;
                    }
                }
            } else {
                for (int j = i; j < i + N_POINTS; j++) {
                    if (calculateDistanceToLine(X_COORDINATES[i], Y_COORDINATES[i], x1, y1, xN, yN) > DIST) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * calculateDistanceToLine - Calculates the perpendicular distance from a point
     * (xPoint, yPoint) to a line defined by two points (xStartPoint, yStartPoint)
     * and (xEndPoint, yEndPoint).
     */
    private double calculateDistanceToLine(double xPoint, double yPoint, double xStartPoint, double yStartPoint,
            double xEndPoint, double yEndPoint) {
        double A = yStartPoint - yEndPoint;
        double B = xEndPoint - xStartPoint;
        double C = (xStartPoint * yEndPoint) - (xEndPoint * yStartPoint);

        double distance = Math.abs((A * xPoint) + (B * yPoint) + C) / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
        return distance;
    }

    /**
     * LIC7 - Checks if there exist K consecutive data points that form a polygon
     * with perimeter greater than the specified value.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), KPTS, LENGTH1, and
     * NUMPOINTS are valid.
     * Postconditions: Returns true if the condition is met; false otherwise.
     */
    public boolean LIC7() {
        int kPts = PARAMETERS.KPTS;

        for (int i = 0; i <= NUMPOINTS - kPts - 2; i++) {
            double distance = calculateDistance(X_COORDINATES[i], Y_COORDINATES[i], X_COORDINATES[i + kPts + 1],
                    Y_COORDINATES[i + kPts + 1]);
            if (distance > PARAMETERS.LENGTH1) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC8 - Checks if there exist three consecutive points (p1, p2, p3) that are
     * not contained within a circle with the specified radius.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), APTS, BPTS,
     * RADIUS1, and NUMPOINTS are valid.
     * Postconditions: Returns true if the condition is met; false otherwise.
     */
    public boolean LIC8() {
        int A_PTS = PARAMETERS.APTS;
        int B_PTS = PARAMETERS.BPTS;
        double radius1 = PARAMETERS.RADIUS1;
        if (A_PTS < 0 || B_PTS < 0 || NUMPOINTS < 5)
            return false;
        if (A_PTS + B_PTS > NUMPOINTS - 3)
            return false;
        for (int i = 0; i + A_PTS + B_PTS + 2 < NUMPOINTS; i++) {
            double x1 = X_COORDINATES[i];
            double y1 = Y_COORDINATES[i];
            double x2 = X_COORDINATES[i + A_PTS + 1];
            double y2 = Y_COORDINATES[i + A_PTS + 1];
            double x3 = X_COORDINATES[i + A_PTS + B_PTS + 2];
            double y3 = Y_COORDINATES[i + A_PTS + B_PTS + 2];
            if (!isContainedInCircle(radius1, x1, y1, x2, y2, x3, y3)) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC9 - Checks if there exists a pair of consecutive points (p1, p2) and (p2,
     * p3) forming an angle greater than specified.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), CPTS, DPTS,
     * EPSILON, and NUMPOINTS are valid.
     * Postconditions: Returns true if the angle condition is met; false otherwise.
     */
    public boolean LIC9() {
        if (NUMPOINTS < 5)
            return false;
        double x1, x2, x3, y1, y2, y3;
        int p2offset = PARAMETERS.CPTS + 1;
        int p3offset = p2offset + PARAMETERS.DPTS + 1;
        for (int i = 0; i + p3offset < NUMPOINTS; i++) {
            x1 = X_COORDINATES[i];
            y1 = Y_COORDINATES[i];
            x2 = X_COORDINATES[i + p2offset];
            y2 = Y_COORDINATES[i + p2offset];
            x3 = X_COORDINATES[i + p3offset];
            y3 = Y_COORDINATES[i + p3offset];
            if (!(x1 == x2 && y1 == y2) && !(x3 == x2 && y3 == y2)) { // Neither p1 nor p3 coincide with vertex (p2).
                // Calculate angle by creating vectors and using dot product
                double dot_product = (x1 - x2) * (x3 - x2) + (y1 - y2) * (y3 - y2);
                double magnitude_v1 = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                double magnitude_v2 = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
                double cos_angle = dot_product / (magnitude_v1 * magnitude_v2);
                double angle = Math.acos(cos_angle);
                // Check if angle holds any of the two conditions
                if (angle < (PI - PARAMETERS.EPSILON) || angle > (PI + PARAMETERS.EPSILON)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * LIC10 - Checks if there exists a pair of consecutive points (p1, p2) and (p2,
     * p3) forming a triangle with an area greater than specified.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), EPTS, FPTS, AREA1,
     * and NUMPOINTS are valid.
     * Postconditions: Returns true if the area condition is met; false otherwise.
     */
    public boolean LIC10() {
        if (NUMPOINTS < 5)
            return false;
        double x1, x2, x3, y1, y2, y3, area;
        int p2offset = PARAMETERS.EPTS + 1;
        int p3offset = p2offset + PARAMETERS.FPTS + 1;
        for (int i = 0; i + p3offset < NUMPOINTS; i++) {
            x1 = X_COORDINATES[i];
            y1 = Y_COORDINATES[i];
            x2 = X_COORDINATES[i + p2offset];
            y2 = Y_COORDINATES[i + p2offset];
            x3 = X_COORDINATES[i + p3offset];
            y3 = Y_COORDINATES[i + p3offset];
            area = 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
            if (area > PARAMETERS.AREA1)
                return true;
        }
        return false;
    }

    /**
     * LIC 11 - Checks for consecutive points violating the condition:
     * X_COORDINATES[p2] - X_COORDINATES[p1] < 0.
     * Preconditions: Coordinates (X_COORDINATES), GPTS, and NUMPOINTS are valid.
     * Postconditions: Returns true if the specified condition is met; false
     * otherwise.
     */
    public boolean LIC11() {
        int G_PTS = PARAMETERS.GPTS;
        if (NUMPOINTS < 3 || G_PTS < 1 || G_PTS > (NUMPOINTS - 2))
            return false;
        for (int i = 0; i < NUMPOINTS - G_PTS - 1; i++) {
            double x1 = X_COORDINATES[i];
            double x2 = X_COORDINATES[i + G_PTS + 1];
            if (x2 - x1 < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC12 - Checks for consecutive points violating distance conditions:
     * distance(p1, p2) > LENGTH1 and distance(p2, p3) < LENGTH2.
     * Preconditions: Coordinates, LENGTH1, LENGTH2, and NUMPOINTS are valid.
     * Postconditions: Returns true if specified conditions are met; false
     * otherwise.
     */
    public boolean LIC12() {
        double length1 = PARAMETERS.LENGTH1;
        double length2 = PARAMETERS.LENGTH2;

        for (int i = 0; i <= NUMPOINTS - 3; i++) {
            double distance1 = calculateDistance(X_COORDINATES[i], Y_COORDINATES[i], X_COORDINATES[i + 1],
                    Y_COORDINATES[i + 1]);
            double distance2 = calculateDistance(X_COORDINATES[i + 1], Y_COORDINATES[i + 1], X_COORDINATES[i + 2],
                    Y_COORDINATES[i + 2]);

            if (distance1 > length1 && distance2 < length2) {
                return true;
            }
        }
        return false;
    }

    /**
     * LIC 13 - Checks if there exist two sets of consecutive points (p1, p2, p3)
     * that satisfy different circle conditions.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), APTS, BPTS,
     * RADIUS1, and RADIUS2 are valid.
     * Postconditions: Returns true if both specified conditions are met; false
     * otherwise.
     */
    public boolean LIC13() {
        boolean condition1 = false;
        boolean condition2 = false;
        int A_PTS = PARAMETERS.APTS;
        int B_PTS = PARAMETERS.BPTS;
        double radius1 = PARAMETERS.RADIUS1;
        double radius2 = PARAMETERS.RADIUS2;
        if (NUMPOINTS < 5 || radius2 < 0)
            return false;
        for (int i = 0; i + A_PTS + B_PTS + 2 < NUMPOINTS; i++) {
            double x1 = X_COORDINATES[i];
            double y1 = Y_COORDINATES[i];
            double x2 = X_COORDINATES[i + A_PTS + 1];
            double y2 = Y_COORDINATES[i + A_PTS + 1];
            double x3 = X_COORDINATES[i + A_PTS + B_PTS + 2];
            double y3 = Y_COORDINATES[i + A_PTS + B_PTS + 2];
            if (!isContainedInCircle(radius1, x1, y1, x2, y2, x3, y3)) {
                condition1 = true;
                break;
            }
        }
        for (int i = 0; i + A_PTS + B_PTS + 2 < NUMPOINTS; i++) {
            double x1 = X_COORDINATES[i];
            double y1 = Y_COORDINATES[i];
            double x2 = X_COORDINATES[i + A_PTS + 1];
            double y2 = Y_COORDINATES[i + A_PTS + 1];
            double x3 = X_COORDINATES[i + A_PTS + B_PTS + 2];
            double y3 = Y_COORDINATES[i + A_PTS + B_PTS + 2];
            if (isContainedInCircle(radius2, x1, y1, x2, y2, x3, y3)) {
                condition2 = true;
                break;
            }
        }
        if (condition1 && condition2)
            return true;
        return false;
    }

    /**
     * LIC 14 - Checks if there exists a smaller triangle (p1, p2, p3) inside a
     * larger triangle that satisfies specified area conditions.
     * Preconditions: Coordinates (X_COORDINATES, Y_COORDINATES), EPTS, FPTS, and
     * AREA2 are valid. The first triangle (LIC10) exists, and NUMPOINTS is greater
     * than or equal to 5.
     * Postconditions: Returns true if the second triangle exists and meets the
     * specified area condition; false otherwise.
     */
    public boolean LIC14() {
        if (this.LIC10() && NUMPOINTS >= 5) { // The first triangle exists
            double x1, x2, x3, y1, y2, y3, area;
            int p2offset = PARAMETERS.EPTS + 1;
            int p3offset = p2offset + PARAMETERS.FPTS + 1;
            for (int i = 0; i + p3offset < NUMPOINTS; i++) {
                x1 = X_COORDINATES[i];
                y1 = Y_COORDINATES[i];
                x2 = X_COORDINATES[i + p2offset];
                y2 = Y_COORDINATES[i + p2offset];
                x3 = X_COORDINATES[i + p3offset];
                y3 = Y_COORDINATES[i + p3offset];
                area = 0.5 * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
                if (area < PARAMETERS.AREA2) // The second triangle exists
                    return true;
            }
        }
        return false;
    }
}
