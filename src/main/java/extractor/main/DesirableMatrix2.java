package extractor.main;

import java.io.*;
import java.util.ArrayList;

public class DesirableMatrix2 {


    private ArrayList<Integer> colsIndices;
    private ArrayList<Integer> rowsSwitchs;
    private ArrayList<Mammal> mammals;
    private ArrayList<String> mammalAreas;
    private ArrayList<String> bioVariables;

    private File colsFile = new File("matrix/data.txt");

    public DesirableMatrix2(ArrayList<Mammal> listMammals, ArrayList<String> mammalAreas, ArrayList<String> bioVariables){
        this.mammals = listMammals;
        this.mammalAreas = mammalAreas;
        this.bioVariables = bioVariables;
        this.rowsSwitchs = new ArrayList<>();
        this.colsIndices = new ArrayList<>();

        if( colsFile.exists()){
            loadMatrixFromFile();
            System.out.println("yep");
        }else{
            System.out.println("nope");
            createMatrix();
            saveMatrixAsSparse();
        }
    }

    /*
    Create  boolean matrix
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


                colsIndices.add(index);
                index += numberOfAreas;
                for(int j = 0; j < bioVariables.size(); j++){
                    for(int k = 0; k < 12; k++){

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


    /*
    Loads Matrix from File
     */
    private void loadMatrixFromFile(){
        try {
            DataInputStream dataInputStreamCols = new DataInputStream(new FileInputStream(colsFile));


            int i;
            int count = 0;

            while ((i = dataInputStreamCols.readInt()) != -1) {

                colsIndices.add(i);
                System.out.println(i);
                if(i == Integer.MAX_VALUE){
                    rowsSwitchs.add(count);
                }
                count++;
            }

            dataInputStreamCols.close();
            System.out.println(rowsSwitchs);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }



    public ArrayList<Integer> getColsIndices() {
        return colsIndices;
    }
}
