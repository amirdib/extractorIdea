package extractor.main.factorFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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



    public static String createReadAbleString(List<List<Integer>> listOfConcepts){


        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < listOfConcepts.size(); i+=2){
            stringBuilder.append("<").append(listOfConcepts.get(i) + ",")
                    .append(listOfConcepts.get(i+1)).append(">");

        }
        return stringBuilder.toString();
    }

    public static void writeToFile(List<List<Integer>> listOfConcepts) throws URISyntaxException, IOException {

        String str = createReadAbleString(listOfConcepts);

        Path path = Paths.get("newfile.txt");
        byte[] strToBytes = str.getBytes();

        Files.write(path, strToBytes);



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

