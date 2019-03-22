package extractor.main.factorFinder;

import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactorAnalyser {

    private String path;
    private File rowsHeaderFile = new File("matrix/rowsHeader.txt");
    private File colsHeaderFile = new File("matrix/colsHeader.txt");
    private List<String> colHeaders;
    private List<String> rowHeaders;


    public FactorAnalyser(String path){

        this.path = path;
        this.colHeaders = loadHeader(colsHeaderFile);
        this.rowHeaders = loadHeader(rowsHeaderFile);
    }

    public int countFactors() throws IOException {
        int countLeft = 0;
        int countRight = 0;
        BufferedReader b = new BufferedReader(new FileReader(path));
        String readLine = "";
        while ((readLine = b.readLine()) != null) {

            for ( char ch : readLine.toCharArray()){
                if(ch == '<'){
                    countLeft++;
                }
                else if(ch == '>'){
                    countRight++;
                }

            }
        }
        System.out.println(countLeft);
        System.out.println(countRight);
        return countLeft ;
    }

    public void transForm() throws IOException {
        // pass the path to the file as a parameter
        FileReader fr =
                new FileReader(path);
        boolean start = false;
        int count = 0;
        StringBuilder extentIntent = new StringBuilder("");
        List<String> extentsIndices = new ArrayList<>();
        List<String> intentsIndices = new ArrayList<>();
        System.out.println(rowHeaders.size());

        int i;
        while ((i=fr.read()) != -1) {
            if( (char) i == '['){
                start = true;
                count++;
            }else if( (char) i == ']'){
                start = false;
                extentIntent.append(']');
                if(count % 2 == 0){
                    intentsIndices.add(extentIntent.toString());
                }else{
                    extentsIndices.add(extentIntent.toString());
                }
                extentIntent  = new StringBuilder("");

            }

            if(start){
                extentIntent.append((char) i);
            }
        }
        extentIntent  = new StringBuilder("");
        colHeaders = loadHeader(colsHeaderFile);
        rowHeaders = loadHeader(rowsHeaderFile);

        for( int j = 0; j < extentsIndices.size(); j++){
            System.out.println(extentsIndices.get(j).length());
            extentIntent.append("-------------------CONCEPT NUMBER:" + j + " -------------------");
            extentIntent.append(System.getProperty("line.separator"));
            extentIntent.append("<[").append(trueValues(intentsIndices.get(j),rowHeaders)).append("],");
            extentIntent.append(System.getProperty("line.separator"));
            extentIntent.append("[").append(formatCols(extentsIndices.get(j),colHeaders)).append("]>");
            extentIntent.append(System.getProperty("line.separator"));
            extentIntent.append(System.getProperty("line.separator"));
        }

        //4656

        Path path = Paths.get("newfile2.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter("newfile2.txt"));
        writer.write(extentIntent.toString());

        writer.close();




        //Files.write(path, extentIntent.toString().getBytes());

    }

    private String trueValues(String stringArray, List<String> headers){
        String[] stringsBefore = stringArray.replaceAll("[^\\d]", " " ).split(" ");
        List<Integer> integers = new ArrayList<>();
        StringBuilder stringAfter = new StringBuilder("");
        for(String s : stringsBefore){
            if(!s.contains(" ")){
                if(!s.equals("")){
                    integers.add(Integer.valueOf(s));
                }
            }
        }

        for( int i: integers){
            stringAfter.append(headers.get(i)).append(", ");
        }
        return stringAfter.toString();

    }
    private String formatCols(String stringArray, List<String> headers){
        String[] stringsBefore = stringArray.replaceAll("[^\\d]", " " ).split(" ");
        List<Integer> integers = new ArrayList<>();
        StringBuilder stringAfter = new StringBuilder("");
        for(String s : stringsBefore){
            if(!s.contains(" ")){
                if(!s.equals("") && (Integer.valueOf(s) < 4656)){
                    integers.add(Integer.valueOf(s));
                }
            }
        }
        String[] climVariables = {"tmin: ","tmax: ","tavg: ","prec: ","srad: ","wind: ","vapr: "};
        int countVariables = 0;

        for( int i: integers){
            stringAfter.append(headers.get(i)).append(", ");
            stringAfter.append(climVariables[countVariables]);
            for(int j = 1; j <=7*12;j++){
                if(j % 13 ==0){
                    countVariables++;
                    stringAfter.append(climVariables[countVariables]);
                }

                DecimalFormat df2 = new DecimalFormat("#.##");
                df2.setRoundingMode(RoundingMode.CEILING);
                stringAfter.append(df2.format(Float.valueOf(headers.get(i + (j*4656)))));
                stringAfter.append(", ");

            }
            countVariables = 0;
            stringAfter.append(System.getProperty("line.separator"));

        }


        return stringAfter.toString();
    }

 // stejna metoda na dvou mistech dat do jedne tridy
    private List<String> loadHeader(File filePath){
        List<String> headerValues = new ArrayList<>();
        try {


            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filePath));

            String value;
            while (dataInputStream.available() > 0){
                value = dataInputStream.readUTF();
                headerValues.add(value);

            }

            dataInputStream.close();
        }catch (IOException fnfe){
            fnfe.printStackTrace();
        }
        return headerValues;
    }
}
