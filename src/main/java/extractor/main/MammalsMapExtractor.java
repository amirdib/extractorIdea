package extractor.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MammalsMapExtractor {



    private JSONObject cgrsJson;

    public MammalsMapExtractor()  {

        String url = "https://www.european-mammals.org/osm/EMMA2grid.php";
        File file = new File("mammals/CGRSJSON.txt");


            if(file.exists()) {
                cgrsJson = readJSONFromFile(file.getPath());
            }else {
                cgrsJson = readJSONFromUrl(url);
                writeJSONToFile(cgrsJson, file.getPath());
            }
    }

    /**
     * Read storaged JSON from EMMA website
     * @param url - url to EMMA2grid where coordinates to polygons are stored
     * @return - JSON Object  with ids and coordinates of polygons in the map
     */
    private JSONObject readJSONFromUrl(String url){

        try {
            InputStream is= new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.lines().collect(Collectors.joining());
            is.close();
            return new JSONObject(jsonText);
        } catch(Exception e) {
            e.printStackTrace();
            return new JSONObject("");
        }
    }

    /**
     * Saves JSON to disk
     * @param obj - JSONObject to storage persist on disk
     * @param path - write path
     */
    private void writeJSONToFile(JSONObject obj, String path) {

        try (FileWriter file = new FileWriter(path)) {
            file.write(obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File saving error");
        }
    }

    /**
     * Read storaged JSON from file
     * @param path - path to JSON File
     * @return ids and coordinates of polygons in the map
     */
    private JSONObject readJSONFromFile(String path) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(path));
            String jsonText = rd.lines().collect(Collectors.joining());
            return new JSONObject(jsonText);
        }catch(FileNotFoundException fnf){
            fnf.printStackTrace();
            return new JSONObject("");
        }
    }

    /**
     * Transfer JSON with areas to HashMap
     * key - CGRSName of area(polygon)
     * value - Coordinates
     * @return HashMap mentioned above
     */
    public HashMap<String, double[]> getAreas() {

        JSONArray jsonArray = new JSONArray(cgrsJson.get("features").toString());
        HashMap<String,double[]> cgrsCoordinates = new HashMap<>();

        for(int i = 0; i < jsonArray.length(); i++) {

            JSONObject cgrsCellJson = ((JSONObject) jsonArray.get(i));
            JSONObject geometry = ((JSONObject) cgrsCellJson.get("geometry"));
            JSONObject properties = (JSONObject) cgrsCellJson.get("properties");

            String[] coorsList = geometry.get("coordinates").toString()
                    .replace("[", "").replace("]", "").split(",");

            double[] doubleCoors = new double[coorsList.length];

            for(int j =0; j < coorsList.length; j++) {
                doubleCoors[j] = Double.parseDouble(coorsList[j]);
            }

            cgrsCoordinates.put(properties.getString("CGRSName"), doubleCoors);

        }
        return cgrsCoordinates;
    }
}