package extractor.main.factorFinder;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FactorAnalyser {

    private String path;
    private File rowsHeaderFile = new File("matrix/rowsHeader.txt");
    private File colsHeaderFile = new File("matrix/colsHeader.txt");
    private File dataFile = new File("matrix/data.txt");
    private List<String> colHeaders;
    private List<String> rowHeaders;
    private List<List<Integer>> areasMammals;


    public FactorAnalyser(String path){

        this.path = path;
        this.colHeaders = loadHeader(colsHeaderFile);
        this.rowHeaders = loadHeader(rowsHeaderFile);
        this.areasMammals = loadMatrixFromFile(dataFile);
    }



    public void transForm() throws IOException {
        // pass the path to the file as a parameter

        List<List<String>> factors = readFromFile(path);
        List<String> extentsIndices = factors.get(0);
        List<String> intentsIndices = factors.get(1);
        System.out.println(areasMammals.get(0));

        StringBuilder output = new StringBuilder();
        StringBuilder outputAverages = new StringBuilder();
        StringBuilder outputDifferences = new StringBuilder();

        int factorCounter = 0;

        for( int j = 0; j < extentsIndices.size(); j++){

            if(extractNumbers(intentsIndices.get(j)).size() == 1){
                continue;
            }

            visualiseFactors(extentsIndices.get(j),factorCounter);

            output.append("-------------------CONCEPT NUMBER:").append(factorCounter).append(" -------------------");
            output.append(System.getProperty("line.separator"));
            output.append("<").append(trueValues(intentsIndices.get(j),rowHeaders));
            output.append(System.getProperty("line.separator"));

            output.append("[").append(formatCols(extentsIndices.get(j),colHeaders)).append("]>");
            output.append(System.getProperty("line.separator"));
            output.append(System.getProperty("line.separator"));


            outputAverages.append("-------------------CONCEPT NUMBER:").append(factorCounter).append(" -------------------");
            outputAverages.append(System.getProperty("line.separator"));
            outputAverages.append("<").append(trueValues(intentsIndices.get(j),rowHeaders));
            outputAverages.append(System.getProperty("line.separator"));
            outputAverages.append(averagesOverMammals(intentsIndices.get(j),rowHeaders));
            //outputAverages.append(differencesOverMammals(intentsIndices.get(j),extentsIndices.get(j),rowHeaders));
            outputAverages.append(System.getProperty("line.separator"));
            outputAverages.append(averages(extentsIndices.get(j),colHeaders));
            outputAverages.append(System.getProperty("line.separator"));

            outputDifferences.append("-------------------CONCEPT NUMBER:").append(factorCounter).append(" -------------------");
            outputDifferences.append(System.getProperty("line.separator"));
            outputDifferences.append("<").append(trueValues(intentsIndices.get(j),rowHeaders));
            outputDifferences.append(System.getProperty("line.separator"));
            outputDifferences.append(differencesOverMammals(intentsIndices.get(j),extentsIndices.get(j),rowHeaders));
            outputDifferences.append(System.getProperty("line.separator"));
            outputDifferences.append(averages(extentsIndices.get(j),colHeaders));
            outputDifferences.append(System.getProperty("line.separator"));
            factorCounter++;
        }



        BufferedWriter writer = new BufferedWriter(new FileWriter("factorsDetailP.txt"));
        writer.write(output.toString());
        writer.close();
        writer = new BufferedWriter(new FileWriter("averagesP.txt"));
        writer.write(outputAverages.toString());
        writer.close();
        writer = new BufferedWriter(new FileWriter("differencesP.txt"));
        writer.write(outputDifferences.toString());
        writer.close();


    }

    private List<List<Integer>> loadMatrixFromFile(File dataFile){
        List<Integer> colsIndices = new ArrayList<>();
        List<List<Integer>> areasMammals = new ArrayList<>();
        try {

            DataInputStream dataInputStreamCols = new DataInputStream(new FileInputStream(dataFile));
            int i;


            while (dataInputStreamCols.available() > 0){
                i = dataInputStreamCols.readInt();
                if(i < 4656){
                    colsIndices.add(i);
                }

                if(i == Integer.MAX_VALUE){
                    areasMammals.add(colsIndices);
                    colsIndices = new ArrayList<>();
                }


            }



            dataInputStreamCols.close();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return areasMammals;
    }

    private void visualiseFactors(String extentsIndices, int factorCounter){
        String pathToSvg = "img/map/factors/concept" + factorCounter + ".svg";
        List<String> idsOfAreas = extractNumbers(extentsIndices)
                .stream()
                .filter(x -> x <4656)
                .map(x -> colHeaders.get(x))
                .collect(Collectors.toList());
        SvgCreator svgCreator = new SvgCreator(pathToSvg,idsOfAreas,factorCounter);
        svgCreator.computeAndSave();
    }

    private List<List<String>> readFromFile(String path) throws IOException {
        List<String> extentsIndices = new ArrayList<>();
        List<String> intentsIndices = new ArrayList<>();
        FileReader fr = new FileReader(path);
        boolean start = false;
        int count = 0;
        int i;
        StringBuilder extentIntent = new StringBuilder("");
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
                extentIntent = new StringBuilder();

            }

            if(start){
                extentIntent.append((char) i);
            }
        }
        List<List<String>> returnList = new ArrayList<>();
        returnList.add(extentsIndices);
        returnList.add(intentsIndices);
        return  returnList;
    }

    private  List<Integer> extractNumbers(String str){
        List<Integer> numbers = new ArrayList<>();
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(str);
        while(m.find()) {
            numbers.add(Integer.valueOf(m.group()));
        }
        return numbers;

    }
    private List<Integer> getAreas(String string){
        return extractNumbers(string).stream().filter(x -> x <4656).collect(Collectors.toList());
    }

    private String trueValues(String stringArray, List<String> headers){
        return extractNumbers(stringArray).stream()
                .map(headers::get)
                .collect(Collectors.toList())
                .toString();

    }
    private String formatCols(String stringArray, List<String> headers){

        List<Integer> integers = getAreas(stringArray);
        StringBuilder stringAfter = new StringBuilder("");


        String[] climVariables = {"tmin: ","tmax: ","tavg: ","prec: ","srad: ","wind: ","vapr: "};
        int countVariables = 0;


        for( int i: integers){
            stringAfter.append(headers.get(i)).append(", ");
            stringAfter.append(climVariables[countVariables]);
            for(int j = 1; j <=7*12;j++){


                DecimalFormat df2 = new DecimalFormat("#.##");
                df2.setRoundingMode(RoundingMode.CEILING);
                stringAfter.append(df2.format(Float.valueOf(headers.get(i + (j*4656)))));
                stringAfter.append(", ");
                if(j % 12 ==0 && j != 7*12){
                    countVariables++;
                    stringAfter.append(climVariables[countVariables]);
                }


            }
            countVariables = 0;
            stringAfter.append(System.getProperty("line.separator"));

        }


        return stringAfter.toString();
    }

    private String averagesOverMammals(String mammals, List<String> rowHeaders){

        List<Integer> integers = extractNumbers(mammals);
        StringBuilder output = new StringBuilder();
        for(int mammal : integers){
            output.append(rowHeaders.get(mammal));
            output.append(System.getProperty("line.separator"));
            output.append(averages(areasMammals.get(mammal).toString(),colHeaders));
            output.append(System.getProperty("line.separator"));
        }

        return output.toString();
    }

    private String differencesOverMammals(String mammals, String extentAreas, List<String> rowHeaders){

        List<Integer> mammalsIndices = extractNumbers(mammals);
        List<Integer> areasOnFactor = extractNumbers(extentAreas).stream().filter(x -> x <4656).collect(Collectors.toList());
        float[] averagesOverFactor = averages(areasOnFactor,colHeaders);


        StringBuilder output = new StringBuilder();
        for(int mammal : mammalsIndices){
            output.append(rowHeaders.get(mammal));
            output.append(System.getProperty("line.separator"));
            float[] averagesOverMammal = averages(areasMammals.get(mammal),colHeaders);
            for(int i = 0; i <averagesOverMammal.length;i++){
                averagesOverMammal[i] =  averagesOverMammal[i] - averagesOverFactor[i];
            }

            output.append(strByMonth(averagesOverMammal));
            output.append(System.getProperty("line.separator"));
        }

        return output.toString();
    }

    private float[] averages(List<Integer> indices, List<String> headers){
        float[] averagesMonth = new float[7 * 12];
        for(int i = 0; i < 7 * 12; i++){
            averagesMonth[i] = 0f;
        }
        for( int i: indices){
            for(int j = 1; j <=7*12;j++){
                if(Float.isNaN(Float.valueOf(headers.get(i + (j*4656))))){
                    continue;
                }
                averagesMonth[j-1] = (averagesMonth[j-1] + Float.valueOf(headers.get(i + (j*4656))));
            }

        }
        for(int i = 0; i < averagesMonth.length;i++){
            averagesMonth[i] = averagesMonth[i] / indices.size();
        }
        return averagesMonth;

    }

    private String strByMonth(float[] values){

        String[] climVariables = {"tmin: "," tmax: "," tavg: "," prec: "," srad: "," wind: "," vapr: "};
        int countVariables = 0;
        StringBuilder stringAfter = new StringBuilder("");
        stringAfter.append("By month");
        int monthsCount = 0;
        float average = 0;
        for(int i = 0; i < climVariables.length*12;i++){

            if(i % 12 ==0){
                stringAfter.append(System.getProperty("line.separator"));
                stringAfter.append(climVariables[countVariables]);
                countVariables++;
            }
            DecimalFormat df2 = new DecimalFormat("#.##");
            df2.setRoundingMode(RoundingMode.CEILING);
            stringAfter.append(df2.format(values[i]));
            stringAfter.append(" ");
            monthsCount++;

            if(monthsCount == 12){
                for(int j = i-11; j < i; j++){
                    average += values[j];
                }
                stringAfter.append("avg: " + df2.format(average/12));
                average = 0;
                monthsCount = 0;
            }
        }
        stringAfter.append(System.getProperty("line.separator"));

        return stringAfter.toString();
    }
    private String averages(String extentAreas, List<String> headers){

        List<Integer> integers = extractNumbers(extentAreas).stream().filter(x -> x <4656).collect(Collectors.toList());
        float[] averagesMonth = averages(integers,headers);
        return  strByMonth(averagesMonth);
        /*
        StringBuilder stringAfter = new StringBuilder("");

        String[] climVariables = {"tmin: "," tmax: "," tavg: "," prec: "," srad: "," wind: "," vapr: "};

        int countVariables = 0;

        stringAfter.append("By month");


        for(int i = 0; i < climVariables.length*12;i++){
            if(i % 12 ==0){
                stringAfter.append(System.getProperty("line.separator"));
                stringAfter.append(climVariables[countVariables]);
                countVariables++;
            }
            DecimalFormat df2 = new DecimalFormat("#.##");
            df2.setRoundingMode(RoundingMode.CEILING);
            stringAfter.append(df2.format(averagesMonth[i]));
            stringAfter.append(" ");
        }
        stringAfter.append(System.getProperty("line.separator"));

        return stringAfter.toString();
        */
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
/*
        Float[] minsMonth = new Float[climVariables.length * 12];
        Float[] maxsMonth = new Float[climVariables.length * 12];

        for(int i = 0; i < climVariables.length*12; i++){
            averagesMonth[i] = 0f;
            minsMonth[i] = Float.MAX_VALUE;
            maxsMonth[i] = Float.MIN_VALUE;
        }
        */
        /*
        for( int i: integers){
            for(int j = 1; j <=7*12;j++){
                if(Float.isNaN(Float.valueOf(headers.get(i + (j*4656))))){
                    continue;
                }

                averagesMonth[j-1] = (averagesMonth[j-1] + Float.valueOf(headers.get(i + (j*4656))));
                minsMonth[j-1] = Float.min(minsMonth[j-1], Float.valueOf(headers.get(i + (j*4656))));
                maxsMonth[j-1] = Float.max(maxsMonth[j-1], Float.valueOf(headers.get(i + (j*4656))));
                //averagesOver[countVariables] = averagesOver[countVariables] + Float.valueOf(headers.get(i + (j*4656)));
                //minsOver[countVariables] = Float.min(minsOver[countVariables], Float.valueOf(headers.get(i + (j*4656))));
                //maxsOver[countVariables] = Float.max(maxsOver[countVariables], Float.valueOf(headers.get(i + (j*4656))));
                if(j % 12 ==0 && j != 7*12){
                    countVariables++;

                }


            }
            countVariables = 0;
        }
        /*
        for(int i = 0; i < averagesMonth.length;i++){
            averagesMonth[i] = averagesMonth[i] / integers.size();
        }
        /*
        stringAfter.append("Averages ");
        for(int i = 0; i < averagesOver.length; i++){
            averagesOver[i] = averagesOver[i] / (integers.size()*12);
            stringAfter.append(climVariables[i]).append(averagesOver[i]);
        }
        stringAfter.append(System.getProperty("line.separator"));
        stringAfter.append("Mins ");
        for(int i = 0; i < maxsOver.length; i++){
            stringAfter.append(climVariables[i]).append(minsOver[i]);
        }
        stringAfter.append(System.getProperty("line.separator"));

        stringAfter.append("Maxs ");
        for(int i = 0; i < minsOver.length; i++){
            stringAfter.append(climVariables[i]).append(maxsOver[i]);
        }
        stringAfter.append(System.getProperty("line.separator"));

        */
