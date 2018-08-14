package extractor.main;
import java.awt.geom.Area;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.FileDirectoryEntry;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;

public class Main {

    public static void main( String[] args ) throws IOException
    {
        // 1041,240 - px pidi ostrova u GB 29UPR4 jeho ID

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
