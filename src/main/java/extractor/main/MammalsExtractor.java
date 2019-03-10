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
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class MammalsExtractor {

    private final String urlToMap;
    private final String urlToSpecies;



    public MammalsExtractor() {
        this.urlToMap = "https://www.european-mammals.org/php/rendermap.php?latname=";
        this.urlToSpecies = "https://www.european-mammals.org/php/mapmaker.php";

        //System.out.println(getMammalsPresence());
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
        //System.out.println(mammalName);
        return new Mammal(mammalName, preLocations, postLocations);
    }

    public List<Mammal> getMammalsPresence() {
        String path = "mammals/mammals.xml";
        File mammalsXmlStorage = new File(path);

        if(mammalsXmlStorage.exists()){
            return getMammalsFromXml(path);
        }else{
            List<Mammal> mammals = getSpecies().stream()
                    .map(this::extractMammal)
                    .collect(Collectors.toList());
            mammalsToXml(mammals, path);
            return mammals;
        }
    }

    private List<Mammal> getMammalsFromXml(String path){
        List<Mammal> mammalList = new ArrayList<>();
        try{
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList mammalNodeList = doc.getElementsByTagName("Mammal");

            

            for(int i = 0; i < mammalNodeList.getLength(); i++){

                NodeList mammalAtritubes = mammalNodeList.item(i).getChildNodes();
               //
                NodeList preLocations = mammalAtritubes.item(1).getChildNodes();


                NodeList posLocations = mammalAtritubes.item(2).getChildNodes();

                String mammalName = mammalAtritubes.item(0).getTextContent();
                List<String> preLocationsFinal = new ArrayList<>();
                List<String> posLocationsFinal = new ArrayList<>();

                for (int k = 0; k < preLocations.getLength(); k++) {
                    Node childNode = preLocations.item(k);

                    if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    preLocationsFinal.add(childNode.getTextContent());
                }
                for (int j = 0; j < posLocations.getLength(); j++) {
                    Node childNode = posLocations.item(j);

                    if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    posLocationsFinal.add(childNode.getTextContent());
                }
                mammalList.add(new Mammal(mammalName,preLocationsFinal,posLocationsFinal));

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return mammalList;


    }

    private void mammalsToXml(List<Mammal> mammals, String path) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Create root element mammals
            Element mammalsRootElement = doc.createElement( "Mammals" );
            doc.appendChild( mammalsRootElement );

            // iterate over mammals, adding them into xml doc
            for( Mammal m : mammals ){
                Element mammalElement = doc.createElement( "Mammal" );
                Element nameElement = doc.createElement( "name");
                nameElement.appendChild(doc.createTextNode(m.getName()));
                mammalElement.appendChild(nameElement);

                // add prelocations to XML
                Element preLocationsRootElement = doc.createElement("preLocations");
                for(String pre: m.getPreLocations()) {
                        Element preLocation = doc.createElement("preLocation");
                        preLocation.appendChild(doc.createTextNode(pre));
                        preLocationsRootElement.appendChild(preLocation);
                }
                mammalElement.appendChild(preLocationsRootElement);

                // add postLocations to XML
                Element posLocationsRootElement = doc.createElement("posLocations");
                for(String post: m.getPostLocations()) {
                    Element posLocation = doc.createElement("posLocation");
                    posLocation.appendChild(doc.createTextNode(post));
                    posLocationsRootElement.appendChild(posLocation);
                }
                mammalElement.appendChild(posLocationsRootElement);
                mammalsRootElement.appendChild( mammalElement );


            }

            // transforms xml doc to string
            TransformerFactory tf = TransformerFactory.newInstance();
            StringWriter writer = new StringWriter();
            Transformer transformer = tf.newTransformer();
            transformer.transform( new DOMSource( doc ), new StreamResult( writer ) );
            //personXMLStringValue = writer.getBuffer().toString();
            Files.write(Paths.get(path), writer.getBuffer().toString().getBytes());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
