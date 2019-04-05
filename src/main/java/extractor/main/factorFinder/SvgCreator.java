package extractor.main.factorFinder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SvgCreator {

    private final String pathOut;
    private final List<String> idsOfAreas;
    private final int conceptNumber;

    public  SvgCreator(String pathOutput, List<String> idsOfAreas, int conceptNumber){
        this.pathOut = pathOutput;
        this.idsOfAreas = idsOfAreas;
        this.conceptNumber = conceptNumber;
    }

    public void computeAndSave(){
        try {

            File outputFile = new File(pathOut);

            if(!outputFile.exists()){

                System.out.println(outputFile.getPath());
                File templateFile = new File("img/map/template/rendermap.svg");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(templateFile);
                System.out.println(idsOfAreas);
                addAreas(doc,idsOfAreas);
                setLegend(doc,"Concept " + conceptNumber);
                saveSVG(doc,outputFile);
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveSVG(Document doc, File outFile){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(outFile);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                    "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(domSource, streamResult);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void setLegend(Document document, String conceptName){
        // uprava legendy
        NodeList legendList = document.getElementById("legtext").getElementsByTagName("text");
        Node totalCellsNode = legendList.item(legendList.getLength() - 1);
        int countOfCells = document.getElementById("datapoints").getElementsByTagName("use").getLength();
        totalCellsNode.setTextContent("Total cells for this species = " + countOfCells);

        Node nameOfConcept = legendList.item(legendList.getLength() - 2);
        nameOfConcept.setTextContent(conceptName);
    }

    private void addAreas(Document document,List<String> idOfAreas){

        Element element = document.getElementById("utm");
        NodeList nList = element.getElementsByTagName("path");
        /*
        List<float[]> centers = idOfAreas.stream()
                .map(x -> getBy(nList,"id",x)
                        .ifPresent(p -> p.getAttributes().getNamedItem("d").getNodeValue()))
                .collect(Collectors.toList());
                //.map(y -> center(pathToCoors(y.toString()))).collect(Collectors.toList());

              */
        List<float[]> centers =  idOfAreas.stream()
                .map(x -> getBy(nList, "id", x))
                .collect(Collectors.toList()).stream()
                .filter(Optional::isPresent)
                .map(x -> x.get().getAttributes().getNamedItem("d").getNodeValue())
                .map(y -> center(pathToCoors(y))).collect(Collectors.toList());







        for(int i = 0; i < centers.size(); i++){
            addArea(document,centers.get(i),idOfAreas.get(i));
        }

    }

    private static void addArea(Document doc, float[] centerPoints,String id){
        Element areaToAdd = (Element) doc.getElementsByTagName("use").item(0).cloneNode(true);
        areaToAdd.setAttribute("id",id);
        areaToAdd.getAttributes().getNamedItem("x").setNodeValue(String.valueOf(centerPoints[0]));
        areaToAdd.getAttributes().getNamedItem("y").setNodeValue(String.valueOf(centerPoints[1]));
        doc.getElementById("datapoints").appendChild(areaToAdd);

    }

    private  List<Integer> pathToCoors(String strPath){
        String[] numbersInString = strPath.split("[M|l]");
        numbersInString[numbersInString.length-1] = "";

        List<Integer> numbers = new ArrayList<>();
        for(String s : numbersInString) {

            numbers.addAll(extractNumbers(s));
        }

        for(int i = 2; i < numbers.size();i += 2){
            numbers.set(i, numbers.get(i) + numbers.get(i-2));
            numbers.set(i+1, numbers.get(i+1) + numbers.get(i-1));
        }
        return numbers;
    }
    private Optional<Node> getBy(NodeList nList, String attribute, String value){
        for(int i = 0; i < nList.getLength();i++){
            Node attrValue = nList.item(i).getAttributes().getNamedItem(attribute);
            if(value.equals(attrValue.getNodeValue())){
                return Optional.of(nList.item(i));
            }
        }
        return Optional.empty();


    }
    private List<Integer> extractNumbers(String str){
        List<Integer> numbers = new ArrayList<>();
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(str);
        while(m.find()) {
            numbers.add(Integer.valueOf(m.group()));
        }
        return numbers;

    }

    private   float[] center(List<Integer> numbers){
        float totalX = 0;
        float totalY = 0;
        float count = numbers.size()/2f;
        for(int i = 0; i <numbers.size();i++){
            if(i % 2 == 0){
                totalX = totalX + numbers.get(i);
            }else{
                totalY = totalY + numbers.get(i);
            }
        }
        float[] center = new float[2];
        center[0] = totalX/(count);
        center[1] = totalY/(count);
        return center;


    }
}
