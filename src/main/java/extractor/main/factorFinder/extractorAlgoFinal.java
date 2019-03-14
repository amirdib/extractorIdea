package extractor.main.factorFinder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class extractorAlgoFinal {



    /*

        boolean[][] matrixForFirst = matrixGenerator(8000,119);
        boolean[][] matrixForSecond = matrixGenerator(350000,470);

        FCF fcfSuperFast = new FCF(matrix1);
        long start2 = System.nanoTime();
        System.out.println(fcfSuperFast.getContext());
        writeToFile(fcfSuperFast.getContext());
        long end2 = System.nanoTime();



        System.out.println("Second:" + (end2 - start2));
        */



    private static String createReadAbleString(List<List<Integer>> listOfConcepts){


        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < listOfConcepts.size(); i+=2){
            stringBuilder.append("<").append(listOfConcepts.get(i) + ",")
                    .append(listOfConcepts.get(i+1)).append(">");

        }
        return stringBuilder.toString();
    }
    private static String createSuperReadbleString(List<List<Integer>> listOfConcepts){
        StringBuilder stringBuilder = new StringBuilder();
        List<String> colsHeaders = loadHeader(new File("matrix/colsHeader.txt"));
        List<String> rowHeaders = loadHeader(new File("matrix/rowsHeader.txt"));

        for(int i = 0; i < listOfConcepts.size(); i+=2){
            stringBuilder.append("<").append(intsToStr(listOfConcepts.get(i),colsHeaders) + ",")
                    .append(intsToStr(listOfConcepts.get(i+1),rowHeaders)).append(">");

        }
        return stringBuilder.toString();
    }

    private static List<String> intsToStr( List<Integer> ints, List<String> strings){
        List<String> returnString = new ArrayList<>();
        for(int i : ints){
            returnString.add(strings.get(i));
        }
       return returnString;
    }



    public static void writeToFile(List<List<Integer>> listOfConcepts) throws URISyntaxException, IOException {

        String str = createReadAbleString(listOfConcepts);

        Path path = Paths.get("newfile.txt");
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);



    }

    private static List<String> loadHeader(File filePath) {
        List<String> headerValues = new ArrayList<>();
        try {


            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath));

            String value;
            while (dataInputStream.available() > 0) {
                value = dataInputStream.readUTF();
                headerValues.add(value);

            }

            dataInputStream.close();
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
        return headerValues;
    }
    public static boolean[][] transposeMatrix(boolean [][] m){
        boolean[][] temp = new boolean[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static boolean[][] matrixGenerator(int rows,int cols){
        boolean[][] resultMatrix = new boolean[rows][cols];
        Random r = new Random();
        int up = 100;
        int down = 0;
        for(int i = 0; i < rows;i++){
            for(int j = 0; j < cols; j++){
                if((r.nextInt(up-down) + down) < 6)
                    resultMatrix[i][j] = true;

            }
        }
        return resultMatrix;

    }





}

