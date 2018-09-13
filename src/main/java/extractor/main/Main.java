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

        WorldClimDownloader worldClimDownloader = new WorldClimDownloader();

        // HashMap stores data from european mammals, cgrs cells with coors
        GeoTiff avgTempJan = new GeoTiff("world2/tavg/wc2.0_10m_tavg_01.tif");


        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor();
        MammalsExtractor mExtractor = new MammalsExtractor();



        HashMap<String, double[]> mammalsMap = mMapExtractor.getAreas();
        List<Mammal> mammals = mExtractor.getMammalsPresence();

        ArrayList<String> bioVariables = worldClimDownloader.getDownloadedVariables();

        /*
        File folder = new File("world2");
        for (File e : folder.listFiles()) {
            bioVariables.add(e.getName());
        }
    */


        String template = "world2/TYPE/wc2.0_10m_TYPE_MONTH.tif";
        int size = 0;
        int sizeDistinct = 0;
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
                for(double[] d : mammalsMap.values()){
                    climVariables.add(climVarThMonth.getAvgFromNeighborhood(new Area(d),2));
                }
                size += climVariables.size();
                sizeDistinct += new HashSet<Double>(climVariables).size();

            }

            System.out.println("Biovariable " + bVar + " was processed");
        }
        System.out.println(size);
        System.out.println(sizeDistinct);

        /*
        int numberOfAreas = mammalsMap.size();
        int numberOfSpecies = mammals.size();
        int numberOfCols = numberOfAreas * bioVariables.size() * 12 + numberOfAreas;
        System.out.println(numberOfCols);
        System.out.println(numberOfAreas);
        byte[][] finalMatrix = new byte[numberOfSpecies][numberOfCols];
        Set<String> missinAreas = new HashSet<>();



        ArrayList<String> mammalAreas = new ArrayList<>(mammalsMap.keySet());
        long starttime = System.nanoTime();


        System.out.println(mammals.size());

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


        File finalMatrixFile = new File("world2/matrix.txt");
        finalMatrixFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(finalMatrixFile);

        for (int i = 0; i < finalMatrix.length; i++) {
            for (int j = 0; j < finalMatrix[i].length; j++) {

                fout.write(String.valueOf(finalMatrix[i][j]).getBytes());
            }
            fout.write(System.getProperty("line.separator").getBytes());
            System.out.println(i + " row writed");
        }

        fout.close();
        System.out.println((endtime- starttime) / 1000000);

*/
        //System.out.println("Coordinates of area 29UPR4 " + Arrays.toString(biggerPoly));
        //System.out.println(avgTempJan.getAvgFromNeighborhood(mArea, 2));
        //System.out.println(avgTempJan.getPxValue(1041, 240));




    }
}
