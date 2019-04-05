package extractor.main;

import javax.xml.crypto.Data;
import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class DesirableMatrix2 {


    private List<Integer> colsIndices;
    private ArrayList<Integer> rowsSwitchs;
    private ArrayList<Mammal> mammals;
    private ArrayList<String> mammalAreas;
    private ArrayList<String> bioVariables;
    private List<String> colHeaders;
    private List<String> rowHeaders;
    HashMap<String, double[]> mammalsMap;

    private File colsFile = new File("matrix/data.txt");
    private File rowsHeaderFile = new File("matrix/rowsHeader.txt");
    private File colsHeaderFile = new File("matrix/colsHeader.txt");

    public DesirableMatrix2(ArrayList<Mammal> listMammals, HashMap<String, double[]> mammalsMap, ArrayList<String> bioVariables){
        this.mammals = listMammals;
        this.mammalAreas = new ArrayList<>(mammalsMap.keySet());
        this.bioVariables = bioVariables;
        this.mammalsMap = mammalsMap;
        this.rowsSwitchs = new ArrayList<>();
        this.colsIndices = new ArrayList<>();

        if( colsFile.exists()){
            System.out.println("Loaded");
            loadMatrixFromFile();

        }else{
            System.out.println("Created");
            createMatrix();
            saveMatrixAsSparse();
        }

        if(rowsHeaderFile.exists()){
            this.rowHeaders = loadHeader(rowsHeaderFile);

        }else{
            List<String> mammalNames;
            mammalNames = listMammals.stream()
                    .map(e -> e.getName())
                    .collect(Collectors.toList());
            writeHeader(mammalNames,rowsHeaderFile);
            this.rowHeaders = mammalNames;

        }

        if(!colsHeaderFile.exists()){
            this.colHeaders = processColsHeader();
        }else{
            this.colHeaders = loadHeader(colsHeaderFile);
        }
        //getDenseMatrix();


    }

    /**
     *
     * @return matrix in dense format
     */
    public boolean[][] getDenseMatrix(){
        int numberOfRows = rowHeaders.size();

        int numberOfCols = mammalAreas.size() * Month.values().length + mammalAreas.size();

        boolean[][] finalMatrix = new boolean[rowHeaders.size()][colHeaders.size()];
        int to;
        int count = 0;

        for (int i = 0; i < numberOfRows; i++){
            to = colsIndices.indexOf(Integer.MAX_VALUE);
            /*
            Set<Integer> uniqueGas = new HashSet<Integer>(colsIndices.subList(0,to));
            if(colsIndices.subList(0,to).size() != uniqueGas.size()){
                System.out.println("Retard alert");
            }
            */
            for(int d : colsIndices.subList(0,to)){

                finalMatrix[i][d] = true;
                count++;
            }


            colsIndices = colsIndices.subList(to + 1,colsIndices.size());
        }
        System.out.println("CountInGet" + count);
        return finalMatrix;
    }
    //finally correct
    private List<String> processColsHeader(){
        double max = Double.MIN_VALUE;
        ArrayList<String> climVariables = new ArrayList<>();
        try {
            String template = "world2/TYPE/wc2.0_10m_TYPE_MONTH.tif";

            climVariables.addAll(mammalAreas);


            for(String bVar : bioVariables){

                System.out.println(bVar);
                for (int i = 1; i <= 12; i++){
                    String month = "";
                    if(i < 10)
                    {
                        month = "0" + i;
                    }
                    else{
                        month = String.valueOf(i);
                    }
                    String pathToTif = template.replace("TYPE", bVar).replace("MONTH", month);

                    GeoTiff climVarThMonth = new GeoTiff(pathToTif);



                    for(String mammalArea : mammalAreas)
                    {

                        /*
                        if(bVar.equalsIgnoreCase("tavg") && !Double.isNaN(climVarThMonth.getAvgFromNeighborhood(new Area(d),2))){
                           max = Double.max(max,climVarThMonth.getAvgFromNeighborhood(new Area(d),2));
                           System.out.println("joo");
                        }
                        */
                        double[] d = mammalsMap.get(mammalArea);
                        climVariables.add(String.valueOf(climVarThMonth.getAvgFromNeighborhood(new Area(d),2)));
                    }
                }
            }
            //System.out.println(max);
            System.out.println("ClimVariables " + climVariables.size());
            writeHeader(climVariables,colsHeaderFile);

        }catch (IOException e){
            e.printStackTrace();
        }
        return climVariables;

    }

    /**
     * This method creates matrix in sparse reprezentation
     * In list column indices are stored
     * After MAX_VALUE we switch to the next row
     */
    /*oblast - odsazzeni o pocet oblasti dostane se na prvni variable zaznam po 12x po sobe pro mesic odsazeni na dalsi promenou*/
    private void createMatrix(){

        int numberOfAreas = mammalAreas.size();
        int numberOfSpecies = mammals.size();
        int numberOfCols = numberOfAreas * bioVariables.size() * 12 + numberOfAreas;


        for (Mammal mammal : mammals) {

            for (String l : mammal.getPostLocations()) {

                // obcas nejake oblasti chybi, chyba na strane mammals db
                int index = mammalAreas.indexOf(l);
                if (index == -1) {

                    continue;
                }


                colsIndices.add(index);
                index += numberOfAreas;
                for (int j = 0; j < bioVariables.size(); j++) {
                    for (int k = 0; k < 12; k++) {

                        colsIndices.add(index);
                        index += numberOfAreas;

                    }

                }

            }

            colsIndices.add(Integer.MAX_VALUE);

        }
    }

    /**
     * This method write matrix on the disk
     *
     */
    private void saveMatrixAsSparse(){
        try {

            File directory = new File("matrix");

            if(!directory.exists()){
                directory.mkdir();
            }

            colsFile.createNewFile();

            DataOutputStream dataOutputStreamCols = new DataOutputStream(new FileOutputStream(colsFile));

            for(int i = 0; i < colsIndices.size(); i++){

                dataOutputStreamCols.writeInt(colsIndices.get(i));
            }


            dataOutputStreamCols.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method loads matrix from disk sparse representation
     * Integer.MAX_VALUE indicates move to the next row
     *
     */
    private void loadMatrixFromFile(){
        try {
            DataInputStream dataInputStreamCols = new DataInputStream(new FileInputStream(colsFile));
            int i;
            int count = 0;

            while (dataInputStreamCols.available() > 0){
                i = dataInputStreamCols.readInt();
                colsIndices.add(i);
                if(i == Integer.MAX_VALUE){
                    rowsSwitchs.add(count);

                }
                count++;


            }

            System.out.println("ColsIndices "  + colsIndices.size());
            System.out.println("ColsIndices "  + count);
            dataInputStreamCols.close();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is for storage values from row/col header
     * @param values - values of row or col header
     * @param filePath - defines storage of this data
     */
    private void writeHeader(List<String> values, File filePath){
        try {

            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filePath));

            for (String s : values){
                dataOutputStream.writeUTF(s);
            }

            dataOutputStream.close();

        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * For load headers data from disk
     * @param filePath Path to the file
     * @return List with values of row/col header
     */
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
