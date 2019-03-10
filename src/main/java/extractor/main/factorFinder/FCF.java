package extractor.main.factorFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FCF {

    private final boolean[][] matrix;


    private boolean[][] univesumMatrix;
    List<List<Integer>> formalConcept = new ArrayList<>();
    HashMap<Integer,ArrayList<Integer>> universumTable;


    public FCF(boolean[][] matrix){

        this.matrix = matrix;
        this.univesumMatrix = copyMatrix(matrix);
        this.universumTable = matrixToUniversum(matrix);



    }

    public List<List<Integer>> getContext(){

        while (!universumTable.isEmpty()){
            ArrayList<Integer> d = new ArrayList<>();

            int v = 0;

            while (existApproColumn2(d,v,univesumMatrix)){

                int j;
                while ((j = chooseCollumn2(d,v)) != -1){
                    d.add(j);
                    d = arrowUp(arrowDown(d));

                    ArrayList<Integer> dDown = arrowDown(d);
                    int[][] cartesianResult = cartesian(dDown,d);
                    v = intersection(cartesianResult,univesumMatrix).size();
                }// select


            }

            ArrayList<Integer> c = arrowDown(d);
            formalConcept.add(c);
            formalConcept.add(d);


            removeFromUniversum(cartesian(c,d));
        }
        return formalConcept;

    }

    private boolean existApproColumn2(ArrayList<Integer> d, int v,boolean[][] universum){
        //long start = System.nanoTime();

        boolean condition = false;

        for(Integer col : universumTable.keySet()){
            if(!(d.contains(col)) && crossFunction(d,col, universum).size() > v) {
                condition = true;
                break;
            }
        }



        //long end = System.nanoTime();
        //System.out.println("Exist approColumn for:" + ((double)(end-start)/1000000));
        return condition ;
    }


    private int chooseCollumn2(ArrayList <Integer> d, int v){
        //long start = System.nanoTime();

        int size;
        int j = -1;
        int maxSize = v;

        for(Integer col : universumTable.keySet()){
            if (!d.contains(col)) {
                size = crossFunction(d,col,univesumMatrix).size();
                if(size > maxSize){
                    j = col;
                    maxSize = size;
                }
            }
        }

        //long end = System.nanoTime();
        //System.out.println("Collumn choosed for:" + ((double)(end-start)/1000000));
        return j;
    }

    private  ArrayList<int[]> crossFunction(ArrayList<Integer> d, int index,boolean[][] universum){


        ArrayList<Integer> indices = new ArrayList<>(d);
        indices.add(index);

        int[][] cartesianProduct = cartesian(arrowDown(indices),arrowUp(arrowDown(indices)));


        return intersection(cartesianProduct,universum);
    }


    private ArrayList<int[]> intersection(int[][] cartesianProduct, boolean[][] universum){
        ArrayList<int[]> intersection = new ArrayList<>();

        for(int i = 0; i < cartesianProduct.length; i++){
            if(universum[cartesianProduct[i][0]][cartesianProduct[i][1]]){
                intersection.add(cartesianProduct[i]);
            }
        }



        return intersection;

    }


    private int[][] cartesian(ArrayList<Integer> extent, ArrayList<Integer> intent){


        int[][] cartesianProduct = new int[extent.size() * intent.size()][2];
        int k = 0;
        for(int i = 0; i < extent.size();i++){
            for(int j = 0; j < intent.size(); j++){
                cartesianProduct[k][0] = extent.get(i);
                cartesianProduct[k][1] = intent.get(j);
                k++;
            }
        }


        return cartesianProduct;

    }

    //fine
    private ArrayList<Integer> arrowDown(ArrayList<Integer> intentIndices){

        boolean isSharing;
        ArrayList<Integer> extentList = new ArrayList<>();

        // loop over all rows
        for(int i = 0; i < matrix.length; i++){
            isSharing = true;

            // loop over given colls
            for(int j = 0; j < intentIndices.size(); j++){
                if(!matrix[i][intentIndices.get(j)]){
                    isSharing = false;
                    break;
                }
            }
            // if object sharing all given attributes
            if(isSharing) {

                extentList.add(i);
            }
        }

        return extentList;
    }

    //fine
    private ArrayList<Integer> arrowUp(ArrayList<Integer> extentIndices ){

        ArrayList<Integer> intentList = new ArrayList<>();
        boolean isSharing;

        // loop over all cols
        for(int i = 0; i < matrix[0].length; i++){

            isSharing = true;

            // loop over given rows
            for(int j = 0; j < extentIndices.size(); j++){
                if(!matrix[extentIndices.get(j)][i]){
                    isSharing = false;
                    break;
                }
            }

            if(isSharing) {
                intentList.add(i);
            }
        }

        return intentList;
    }

    /*
    private HashMap<Integer,ArrayList<Integer>> matrixToUniversum(boolean[][] matrix){
        HashMap<Integer,ArrayList<Integer>> universum = new LinkedHashMap<>();
        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int i = 0 ; i <cols; i++){
            ArrayList<Integer> rowsIndices = new ArrayList<>();
            for(int j = 0; j < rows; j++){

                if(matrix[j][i] == true){
                    rowsIndices.add(j);
                }

            }
            universum.put(i,rowsIndices);
        }


        return universum;

    }
    */
    public HashMap<Integer,ArrayList<Integer>> matrixToUniversum(boolean[][] matrix){
        HashMap<Integer,ArrayList<Integer>> universum = new LinkedHashMap<>();
        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int i = 0 ; i <cols; i++){
            ArrayList<Integer> rowsIndices = new ArrayList<>();
            for(int j = 0; j < rows; j++){

                if(matrix[j][i] == true){
                    rowsIndices.add(j);
                }

            }
            if(!rowsIndices.isEmpty()){
                universum.put(i,rowsIndices);
            }

        }


        return universum;

    }
    /*
    private void removeFromUniversum(int[][] cartesianProduct){
        //long start = System.nanoTime();

        for(int i = 0; i < cartesianProduct.length; i++){

            int row = cartesianProduct[i][0];
            int col = cartesianProduct[i][1];
            univesumMatrix[row][col] = false;


            if (universumTable.containsKey(col)) {
                if(universumTable.get(col).contains(row)){
                    universumTable.get(col).remove(universumTable.get(col).indexOf(row));

                    if(universumTable.get(col).isEmpty()){
                        universumTable.remove(col);
                    }
                }


            }


        }
        //long end = System.nanoTime();
        //System.out.println("Remove from matrix for:" + ((double)(end-start)/1000000));
    }
    */

    private void removeFromUniversum(int[][] cartesianProduct){
        long start = System.nanoTime();

        for(int i = 0; i < cartesianProduct.length; i++){

            int row = cartesianProduct[i][0];
            int col = cartesianProduct[i][1];
            univesumMatrix[row][col] = false;

            /*
            if (universumTable.containsKey(col)) {
                if(universumTable.get(col).contains(row)){
                    universumTable.get(col).remove(universumTable.get(col).indexOf(row));

                    if(universumTable.get(col).isEmpty()){
                        universumTable.remove(col);
                    }
                }


            }
            */


            if(universumTable.containsKey(col) && universumTable.get(col).contains(row)){
                universumTable.get(col).remove(universumTable.get(col).indexOf(row));
                if(universumTable.get(col).isEmpty()){
                    universumTable.remove(col);
                }
            }else if(universumTable.containsKey(col) && universumTable.get(col).isEmpty()){
                universumTable.remove(col);

            }




        }
        long end = System.nanoTime();
        //System.out.println("Remove from matrix for:" + ((double)(end-start)/1000000));
    }



    private boolean[][] copyMatrix (boolean[][] originalMatrix){
        boolean[][] result = new boolean[matrix.length][matrix[0].length];
        for (int i =0; i < matrix.length; i++){
            for(int j = 0 ; j < matrix[0].length; j++){
                result[i][j] = originalMatrix[i][j];

            }
        }
        return result;
    }
}
