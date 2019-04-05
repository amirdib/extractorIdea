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



    public static void main( String[] args ) throws IOException, URISyntaxException {



        // HashMap stores data from european mammals, cgrs cells with coors
        //GeoTiff avgTempJan = new GeoTiff("world2/tavg/wc2.0_10m_tavg_01.tif");
        FactorAnalyser fa = new FactorAnalyser("newfile.txt");
        //System.out.println(fa.countFactors());
        fa.transForm();
        ;
        /*
        int count = 0;
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;


        GeoTiff climVarThMonth = new GeoTiff("world2/wind/wc2.0_10m_wind_12.tif");
        for(int i = 0; i < climVarThMonth.getWidth(); i++){
            for(int j = 0; j < climVarThMonth.getHeight();j++){
               max = Float.max(max,climVarThMonth.getPxValue(i,j));
               min = Float.min(min,climVarThMonth.getPxValue(i,j));
               //System.out.println(climVarThMonth.getPxValue(i,j));

            }
        }
        // prec -32768.0
        // srad 65535
        System.out.println(max);
        System.out.println(min);
        */


/*
        Instant start = Instant.now();
         //Pro tvorbu matice odkomentovat
        //Stahne a ulozi udaje o mapě na které se nachází savci
        MammalsMapExtractor mMapExtractor = new MammalsMapExtractor(); // optimalized

        MammalsExtractor mExtractor = new MammalsExtractor(); // optimalized
        WorldClimDownloader worldClimDownloader = new WorldClimDownloader(); // optimalized
        worldClimDownloader.downLoadAll();



        //ArrayList<String> mammalsAreas = new ArrayList<>(mammalsMap.keySet());
        ArrayList<Mammal> mammals = new ArrayList<>(mExtractor.getMammalsPresence());// List of objects of mammals 245


        ArrayList<String> bioVariables = new ArrayList<>(worldClimDownloader.getDownloadedVariables()); // list of biovariables //7
        HashMap<String, double[]> mammalsMap = mMapExtractor.getAreas(); // Keys = id for location, double[] = coordinates // 4656

        DesirableMatrix2 desMatrix = new DesirableMatrix2(mammals,mammalsMap,bioVariables);

        /*
        boolean[][] denseMatrix = desMatrix.getDenseMatrix();


        System.gc();
        System.out.println("dONE");


        FCF conceptFinder = new FCF(transposeMatrix(denseMatrix));
        writeToFile(conceptFinder.getContext());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Time " + timeElapsed);


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
