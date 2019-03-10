package extractor.main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class DesirableMatrix {

    private ArrayList<Integer> rowsIndices;
    private ArrayList<Integer> colsIndices;
    private ArrayList<Mammal> mammals;
    private ArrayList<String> mammalAreas;
    private ArrayList<String> bioVariables;
    private File rowsFile = new File("matrix/rowsIndices.txt");
    private File colsFile = new File("matrix/colsIndices.txt");

    public DesirableMatrix(ArrayList<Mammal> listMammals, ArrayList<String> mammalAreas, ArrayList<String> bioVariables){
        this.mammals = listMammals;
        this.mammalAreas = mammalAreas;
        this.bioVariables = bioVariables;
        this.rowsIndices = new ArrayList<>();
        this.colsIndices = new ArrayList<>();

        if(rowsFile.exists() && colsFile.exists()){
            loadMatrixFromFile();
        }else{
            createMatrix();
            saveMatrixAsSparse();
        }
    }

    /*
    Create dense boolean matrix
     */
    private void createMatrix(){

        int numberOfAreas = mammalAreas.size();
        int numberOfSpecies = mammals.size();
        int numberOfCols = numberOfAreas * bioVariables.size() * 12 + numberOfAreas;

        byte[][] finalMatrix = new byte[numberOfSpecies][numberOfCols];


        for (int i = 0; i < numberOfSpecies; i++) {

            for (String l : mammals.get(i).getPostLocations()) {
                int index = mammalAreas.indexOf(l);
                if(index == -1){

                    continue;
                }

                rowsIndices.add(i);
                colsIndices.add(index);
                index += numberOfAreas;
                for(int j = 0; j < bioVariables.size(); j++){
                    for(int k = 0; k < 12; k++){
                        rowsIndices.add(i);
                        colsIndices.add(index + k);


                    }
                    index += numberOfAreas;
                }

            }
        }

    }

    /*
    Save matrix as sparse to textFile
     */
    private void saveMatrixAsSparse(){
        try {

            File directory = new File("matrix");

            if(!directory.exists()){
                directory.mkdir();
            }
            rowsFile.createNewFile();
            colsFile.createNewFile();
            DataOutputStream dataOutputStreamRows = new DataOutputStream(new FileOutputStream(rowsFile));
            DataOutputStream dataOutputStreamCols = new DataOutputStream(new FileOutputStream(colsFile));

            for(int i = 0; i < rowsIndices.size(); i++){
                dataOutputStreamRows.writeInt(rowsIndices.get(i));
                dataOutputStreamCols.writeInt(colsIndices.get(i));
            }

            dataOutputStreamRows.close();
            dataOutputStreamCols.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    Loads Matrix from File
     */
    private void loadMatrixFromFile(){
        try {
            File rowsFile = new File("matrix/rowsIndices.txt");
            File colsFile = new File("matrix/colsIndices.txt");


            DataInputStream dataInputStreamRows = new DataInputStream(new FileInputStream(rowsFile));
            DataInputStream dataInputStreamCols = new DataInputStream(new FileInputStream(colsFile));


            int i;

            while ((i = dataInputStreamRows.readInt()) != -1) {
                rowsIndices.add(i);

                i = dataInputStreamCols.readInt();
                colsIndices.add(i);
            }
            dataInputStreamRows.close();
            dataInputStreamCols.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getRowsIndices() {
        return rowsIndices;
    }

    public ArrayList<Integer> getColsIndices() {
        return colsIndices;
    }
}
