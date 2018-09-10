package extractor.main;


import java.io.IOException;

import java.util.*;
import java.net.URL;
import java.nio.channels.*;

import java.io.*;

import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;


public class Main {

    public static void main( String[] args ) throws IOException, JAXBException {

        WorldClimDownloader worldClimDownloader = new WorldClimDownloader();

        // HashMap stores data from european mammals, cgrs cells with coors
        GeoTiff avgTempJan = new GeoTiff("world2/tavg/wc2.0_10m_tavg_01.tif");
        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor();
        MammalsExtractor mExtractor = new MammalsExtractor();
        //var neco = new ArrayList<String>();


        HashMap<String, double[]> mammalsMap = mMapExtractor.getAreas();


        ArrayList<String> bioVariables = new ArrayList<>();
        File folder = new File("world2");
        for (File e : folder.listFiles()) {
            bioVariables.add(e.getName());
        }
        int numberOfAreas = mammalsMap.size();
        int numberOfSpecies = mExtractor.getSpecies().size();
        int numberOfCols = numberOfAreas * bioVariables.size() * 12;
        byte[][] finalMatrix = new byte[numberOfSpecies][numberOfCols];
        Set<String> missinAreas = new HashSet<>();



        ArrayList<String> mammalAreas = new ArrayList<>(mammalsMap.keySet());
        long starttime = System.nanoTime();
        System.out.println("List of Mammals startDownload");
        List<Mammal> mammals = mExtractor.getMammalsPresence();
        /*
        System.out.println("List of Mammals downloaded");
        for (int i = 0; i < mammals.size(); i++) {

            for (String l : mammals.get(i).getPostLocations()) {
                int index = mammalAreas.indexOf(l);
                if(index == -1){
                    //System.out.println(l);
                    //System.out.println(mammals.get(i));
                    //missinAreas.add(l);
                    continue;
                }
                finalMatrix[i][index] = 1;
                index += numberOfAreas;
                for(int j = 0; j < bioVariables.size(); j++){
                    for(int k = 0; k < 12; k++){
                       finalMatrix[i][index + k] = 1;

                    }
                    index += numberOfAreas;
                }

            }
            System.out.println( i +" mammal processed");

        }
        long endtime = System.nanoTime();
        System.out.println((endtime- starttime) / 1000000);
/*
        File finalMatrixFile = new File("world2/matrix.txt");
        finalMatrixFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(finalMatrixFile);

        for (int i = 0; i < finalMatrix.length; i++) {
            for (int j = 0; j < finalMatrix[i].length; j++) {

                fout.write(String.valueOf(finalMatrix[i][j]).getBytes());
            }
            fout.write(System.getProperty("line.separator").getBytes());
        }

        fout.close();
*/


        //System.out.println("Coordinates of area 29UPR4 " + Arrays.toString(biggerPoly));
        //System.out.println(avgTempJan.getAvgFromNeighborhood(mArea, 2));
        System.out.println(avgTempJan.getPxValue(1041, 240));




    }
}
