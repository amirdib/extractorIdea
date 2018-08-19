package extractor.main;

import java.util.List;

public class Mammal {

    private final String name;
    private final List<String> preLocations;
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