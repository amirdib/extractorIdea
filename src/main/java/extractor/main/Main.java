package extractor.main;


import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.net.URL;
import java.nio.channels.*;

import java.io.*;
import org.apache.commons.io.FileUtils;


public class Main {

    public static void main( String[] args ) throws IOException
    {
        URL url = new URL("http://biogeo.ucdavis.edu/data/worldclim/v2.0/tif/base/wc2.0_10m_tmin.zip");
        File file = new File("world/min/minimum.zip");
        FileUtils.copyURLToFile(url, file);


        // HashMap stores data from european mammals, cgrs cells with coors
        GeoTiff avgTempJan = new GeoTiff("world/wc2.0_10m_tavg_01.tif");
        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor();
        MammalsExtractor mExtractor = new MammalsExtractor();

        HashMap<String,double[]> mammalsMap = mMapExtractor.getAreas();

        double[] biggerPoly = mammalsMap.get("29UPR4");
        MammalArea mArea = new MammalArea("29UPR4",biggerPoly);

        System.out.println("Coordinates of area 29UPR4 " + Arrays.toString(biggerPoly));

        System.out.println(avgTempJan.getAvgFromNeighborhood(mArea, 2));
        System.out.println(avgTempJan.getPxValue(1041, 240));

    }
}
