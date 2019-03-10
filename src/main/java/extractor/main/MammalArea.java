package extractor.main;

import java.util.Arrays;

public class MammalArea extends Area {

    private final String CGRSID;

    public MammalArea(String cgrsId, double[] coors) {
        super(coors);
        this.CGRSID = cgrsId;
    }

    @Override
    public String toString() {
        return "MammalArea [CGRSID=" + CGRSID + ", getGPSCoordinates()=" + Arrays.toString(getGPSCoordinates()) + "]";
    }

    public String getCGRSID() {
        return CGRSID;
    }


}
