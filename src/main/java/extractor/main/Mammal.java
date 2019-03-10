package extractor.main;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "mammal")
public class Mammal {

    @XmlElement(name = "name")
    private final String name;
    @XmlElement(name = "prelocations")
    private final List<String> preLocations;
            @XmlElement(name = "postlocations")
    private final List<String> postLocations;


    public Mammal(String name, List<String> pre,List<String> post) {
        this.name = name;
        this.preLocations = pre;
        this.postLocations = post;

    }

    public String getName() {
        return name;
    }

    public List<String> getPreLocations() {
        return preLocations;
    }

    public List<String> getPostLocations() {
        return postLocations;
    }


    @Override
    public String toString() {
        return "Mammal [name=" + name + ", preLocations=" + preLocations + ", postLocations=" + postLocations + "]";
    }


}