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

    private String url = "https://www.european-mammals.org/osm/EMMA2grid.php";
    private String storageFileFullName = "mammals/CGRSJSON.txt";
    private JSONObject cgrsJson;

    public MammalsMapExtractor()  {

        File file = new File(storageFileFullName);

        try {
            if(file.exists()) {
                cgrsJson = readJSONFromFile(file.getPath());

            }else {
                cgrsJson = readJSONFromUrl(url);
                writeJSONToFile(cgrsJson,file.getPath());
            }
        }catch(IOException e1) {
            System.out.println("IO Error");
        }catch(JSONException e2) {
            System.out.println("JSON Error");
        }
    }



    private JSONObject readJSONFromUrl(String url){
        JSONObject json = new JSONObject();
        try {
            InputStream is= new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.lines().collect(Collectors.joining());
            json = new JSONObject(jsonText);
            is.close();
        } catch(Exception e) {

        }
        return json;
    }

    private void writeJSONToFile(JSONObject obj, String path) {

        try (FileWriter file = new FileWriter(path)) {
            file.write(obj.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JSONObject readJSONFromFile(String url) throws FileNotFoundException {

        BufferedReader rd = new BufferedReader(new FileReader(url));
        String jsonText = rd.lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(jsonText);
        return json;
    }

    public  HashMap<String, double[]> getAreas() throws JSONException, IOException{

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