package extractor.main;


import java.io.IOException;

import java.util.*;
import java.net.URL;
import java.nio.channels.*;

import java.io.*;

import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;


public class Main {

    public static void main( String[] args ) throws IOException {



        // HashMap stores data from european mammals, cgrs cells with coors
        GeoTiff avgTempJan = new GeoTiff("world2/tavg/wc2.0_10m_tavg_01.tif");


        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor();
        MammalsExtractor mExtractor = new MammalsExtractor();



        HashMap<String, double[]> mammalsMap = mMapExtractor.getAreas();
        //List<Mammal> mammals = mExtractor.getMammalsPresence();



        WorldClimDownloader worldClimDownloader = new WorldClimDownloader();

        /*
        String template = "world2/TYPE/wc2.0_10m_TYPE_MONTH.tif";

        for(String bVar : bioVariables){
            String pathToTif = template.replace("TYPE", bVar);

            for (int i = 1; i <= 12; i++){
                String month = String.valueOf(i);
                if(i < 10)
                {
                    month = "0" + month;
                }
                pathToTif = pathToTif.replace("MONTH", month);

                GeoTiff climVarThMonth = new GeoTiff(pathToTif);
                ArrayList<Double> climVariables = new ArrayList<>();

                for(double[] d : mammalsMap.values())
                {
                    climVariables.add(climVarThMonth.getAvgFromNeighborhood(new Area(d),2));
                }


            }


        }
    */



        ArrayList<String> mammalsAreas = new ArrayList<>(mammalsMap.keySet());
        ArrayList<Mammal> mammals = new ArrayList<>(mExtractor.getMammalsPresence());
        ArrayList<String> bioVariables = new ArrayList<>(worldClimDownloader.getDownloadedVariables());
        DesirableMatrix desMatrix = new DesirableMatrix(mammals,mammalsAreas,bioVariables);

        System.out.println(mammals.get(0));
        //System.out.println(desMatrix.getRowsIndices().lastIndexOf(0));
















        //System.out.println("Coordinates of area 29UPR4 " + Arrays.toString(biggerPoly));
        //System.out.println(avgTempJan.getAvgFromNeighborhood(mArea, 2));
        //System.out.println(avgTempJan.getPxValue(1041, 240));




    }
}
