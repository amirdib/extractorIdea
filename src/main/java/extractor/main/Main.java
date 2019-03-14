package extractor.main;


import java.io.IOException;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.net.URL;
import java.nio.channels.*;

import java.io.*;

import extractor.main.factorFinder.FCF;
import extractor.main.factorFinder.FactorAnalyser;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;

import static extractor.main.factorFinder.extractorAlgoFinal.*;



public class Main {

    public static byte[][] matrix = {
            {1,0,1,0,1,1},
            {0,0,1,0,0,0},
            {1,1,0,1,1,1},
            {0,0,1,0,0,1},
            {0,1,1,1,0,1}};

    public static void main( String[] args ) throws IOException, URISyntaxException {



        // HashMap stores data from european mammals, cgrs cells with coors
        //GeoTiff avgTempJan = new GeoTiff("world2/tavg/wc2.0_10m_tavg_01.tif");
        FactorAnalyser fa = new FactorAnalyser("newfile.txt");
        //System.out.println(fa.countFactors());
        fa.transForm();

        /* //Pro tvorbu matice odkomentovat
        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor();
        MammalsExtractor mExtractor = new MammalsExtractor();
        WorldClimDownloader worldClimDownloader = new WorldClimDownloader();

        Instant start = Instant.now();

        //ArrayList<String> mammalsAreas = new ArrayList<>(mammalsMap.keySet());
        ArrayList<Mammal> mammals = new ArrayList<>(mExtractor.getMammalsPresence());// List of objects of mammals 489
        ArrayList<String> bioVariables = new ArrayList<>(worldClimDownloader.getDownloadedVariables()); // list of biovariables //7
        HashMap<String, double[]> mammalsMap = mMapExtractor.getAreas(); // Keys = id for location, double[] = coordinates // 4656

        DesirableMatrix2 desMatrix = new DesirableMatrix2(mammals,mammalsMap,bioVariables);


        boolean[][] denseMatrix = desMatrix.getDenseMatrix();


        System.gc();
        System.out.println("dONE");

        /*
        FCF conceptFinder = new FCF(transposeMatrix(denseMatrix));
        writeToFile(conceptFinder.getContext());
        */

        /* Uz nepotrbne
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time " + timeElapsed);

        System.out.println("Rows " + denseMatrix.length);
        System.out.println("Cols " + denseMatrix[1].length);

        boolean[][] transposedMatrix = transposeMatrix(denseMatrix);

        System.out.println("Rows " + transposedMatrix.length);
        System.out.println("Cols " + transposedMatrix[1].length);
        */





    }
}
