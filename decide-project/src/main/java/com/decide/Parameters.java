package com.decide;

public class Parameters {
    double LENGTH1; // Length in LICs 0, 7, 12
    double RADIUS1; // Radius in LICs 1, 8, 13
    double EPSILON; // Deviation from PI in LICs 2, 9
    double AREA1; // Area in LICs 3, 10, 14
    int QPTS; // No. of consecutive points in LIC 4
    int QUADS; // No. of quadrants in LIC 4
    double DIST; // Distance in LIC 6
    int NPTS; // No. of consecutive pts. in LIC 6
    int KPTS; // No. of int. pts. in LICs 7, 12
    int APTS; // No. of int. pts. in LICs 8, 13
    int BPTS; // No. of int. pts. in LICs 8, 13
    int CPTS; // No. of int. pts. in LIC 9
    int DPTS; // No. of int. pts. in LIC 9
    int EPTS; // No. of int. pts. in LICs 10, 14
    int FPTS; // No. of int. pts. in LICs 10, 14
    int GPTS; // No. of int. pts. in LIC 11
    double LENGTH2; // Maximum length in LIC 12
    double RADIUS2; // Maximum radius in LIC 13
    double AREA2; // Maximum area in LIC 14

    public Parameters(double LENGTH1, double RADIUS1, double EPSILON, double AREA1, int QPTS, int QUADS, double DIST,
            int NPTS, int KPTS, int APTS, int BPTS, int CPTS, int DPTS, int EPTS, int FPTS, int GPTS, double LENGTH2,
            double RADIUS2, double AREA2) {

        this.LENGTH1 = LENGTH1;
        this.RADIUS1 = RADIUS1;
        this.EPSILON = EPSILON;
        this.AREA1 = AREA1;
        this.QPTS = QPTS;
        this.QUADS = QUADS;
        this.DIST = DIST;
        this.NPTS = NPTS;
        this.KPTS = KPTS;
        this.APTS = APTS;
        this.BPTS = BPTS;
        this.CPTS = CPTS;
        this.DPTS = DPTS;
        this.EPTS = EPTS;
        this.FPTS = FPTS;
        this.GPTS = GPTS;
        this.LENGTH2 = LENGTH2;
        this.RADIUS2 = RADIUS2;
        this.AREA2 = AREA2;
    }

    // Setters
    public void setLENGTH1(double LENGTH1) {
        this.LENGTH1 = LENGTH1;
    }

    public void setRADIUS1(double RADIUS1) {
        this.RADIUS1 = RADIUS1;
    }

    public void setEPSILON(double EPSILON) {
        this.EPSILON = EPSILON;
    }

    public void setAREA1(double AREA1) {
        this.AREA1 = AREA1;
    }

    public void setQPTS(int QPTS) {
        this.QPTS = QPTS;
    }

    public void setQUADS(int QUADS) {
        this.QUADS = QUADS;
    }

    public void setDIST(double DIST) {
        this.DIST = DIST;
    }

    public void setNPTS(int NPTS) {
        this.NPTS = NPTS;
    }

    public void setKPTS(int KPTS) {
        this.KPTS = KPTS;
    }

    public void setAPTS(int APTS) {
        this.APTS = APTS;
    }

    public void setBPTS(int BPTS) {
        this.BPTS = BPTS;
    }

    public void setCPTS(int CPTS) {
        this.CPTS = CPTS;
    }

    public void setDPTS(int DPTS) {
        this.DPTS = DPTS;
    }

    public void setEPTS(int EPTS) {
        this.EPTS = EPTS;
    }

    public void setFPTS(int FPTS) {
        this.FPTS = FPTS;
    }

    public void setGPTS(int GPTS) {
        this.GPTS = GPTS;
    }

    public void setLENGTH2(double LENGTH2) {
        this.LENGTH2 = LENGTH2;
    }

    public void setRADIUS2(double RADIUS2) {
        this.RADIUS2 = RADIUS2;
    }

    public void setAREA2(double AREA2) {
        this.AREA2 = AREA2;
    }
}
