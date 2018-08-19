package extractor.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MammalsExtractor {

    private final String urlToMap;
    private final String urlToSpecies;



    public MammalsExtractor() {
        this.urlToMap = "https://www.european-mammals.org/php/rendermap.php?latname=";
        this.urlToSpecies = "https://www.european-mammals.org/php/mapmaker.php";
        //System.out.println(downloadWebPage(this.urlToMap + getSpecies().get(0)));
        System.out.println(getMammalsPresence());
        //;

    }

    private String downloadWebPage(String targetURL) {
        StringBuilder pageBuilder = new StringBuilder();
        try {
            URL url = new URL(targetURL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            //BufferedWriter writer = new BufferedWriter(new FileWriter("data.html"));
            String line;

            while ((line = reader.readLine()) != null) {
                pageBuilder.append(line);
            }
            reader.close();

        }catch(Exception e1) {
            System.out.println("nepovedlo se");
        }

        return pageBuilder.toString();
    }

    public List<String> getSpecies() {
        StringBuilder strBuilder = new StringBuilder(downloadWebPage(this.urlToSpecies));
        String startTag = "<OPTION >";
        String endTag = "</OPTION>";
        int startIndex;
        int endIndex;
        List<String> species = new ArrayList<>();

        while(strBuilder.indexOf(startTag) != -1) {
            startIndex = strBuilder.indexOf(startTag) + 9;
            endIndex = strBuilder.indexOf(endTag);
            species.add(strBuilder.substring(startIndex, endIndex).replace(" ", "+"));
            strBuilder.delete(0,endIndex + 9);

        }
        return species;
    }

    private Mammal extractMammal(String mammalName) {
        // extraction datapoints from HTML
        StringBuilder strBuilder = new StringBuilder(downloadWebPage(this.urlToMap + mammalName));
        List<String> postLocations = new ArrayList<>();
        List<String> preLocations = new ArrayList<>();
        strBuilder.delete(0, strBuilder.indexOf("datapoints"));
        strBuilder.delete(0, strBuilder.indexOf("<use"));
        strBuilder.delete(strBuilder.indexOf("</g>")-1, strBuilder.length()-1);

        //parse dataPoints
        while(strBuilder.indexOf("id") != -1) {

            String id;
            String phase;
            id = strBuilder.substring(strBuilder.indexOf("id")+6,strBuilder.indexOf("x")-2);
            phase = strBuilder.substring(strBuilder.indexOf("#"),strBuilder.indexOf("/>"));
            strBuilder.delete(0, strBuilder.indexOf("/>")+2);

            if(phase.contains("post")) {
                postLocations.add(id);
            }else {
                preLocations.add(id);
            }

        }

        return new Mammal(mammalName, preLocations, postLocations);
    }

    public List<Mammal> getMammalsPresence() {
        return getSpecies().stream()
                .map(s -> extractMammal(s))
                .limit(1)
                .collect(Collectors.toList());
    }


}
