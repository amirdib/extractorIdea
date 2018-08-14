package extractor.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Area {


    private final double[] GPSCoordinates;

    public Area(double[] coors) {

        this.GPSCoordinates = coors;
    }

    public Location getMiddleCoordinate() {

        double height = GPSCoordinates[1] + GPSCoordinates[3];
        double width = GPSCoordinates[0] + GPSCoordinates[4];

        return new Location(width/2,height/2);

    }

    public double getArea() {
        double area = 0;

        for(int i = 0; i < GPSCoordinates.length-2; i += 2) {
            area += (GPSCoordinates[i] * GPSCoordinates[i + 3])
                    - (GPSCoordinates[i + 1] * GPSCoordinates[i + 2]);

        }

        return Math.abs(area/2);
    }

    public Area intersection(Area incident) {

        List<double[]> linesReference = getLines(getGPSCoordinates());
        List<double[]> linesIncident = getLines(incident.getGPSCoordinates());

        double[] point1 = new double[2];
        double[] point2 = new double[2];
        List<double[]> result = new ArrayList<>();

        for(int i = 0; i < linesReference.size();i++) {

            for(int j = 0; j < linesIncident.size();j++) {

                point1[0] = linesIncident.get(j)[0];
                point1[1]  = linesIncident.get(j)[1];
                point2[0]  = linesIncident.get(j)[2];
                point2[1]  = linesIncident.get(j)[3];
                if(isFront(linesReference.get(i),point1) && isFront(linesReference.get(i),point2)) {
                    result.add(new double[] {point2[0], point2[1]});
                }else if(isFront(linesReference.get(i),point1) && !isFront(linesReference.get(i),point2)) {
                    result.add(intersection(linesReference.get(i), point1, point2));

                }else if(!isFront(linesReference.get(i),point1) && isFront(linesReference.get(i),point2)) {
                    result.add(intersection(linesReference.get(i), point1, point2));
                    result.add(new double[] {point2[0], point2[1]});
                }

            }

            Collections.rotate(result, 1);
            linesIncident = new ArrayList<double[]>(getLines(result));
            result.clear();
        }

        double[] polygon = new double[linesIncident.size() * 2 + 2];
        int k = 0;

        for(double[] e : linesIncident) {

            polygon[k] = e[0];
            k++;
            polygon[k] = e[1];
            k++;
        }

        if(!linesIncident.isEmpty()) {
            polygon[k] = linesIncident.get(0)[0];
            k++;
            polygon[k] = linesIncident.get(0)[1];
        }

        return new Area(polygon);
    }


    private List<double[]> getLines(double[] polygon) {
        List<double[]> lines = new ArrayList<>();
        for(int i = 0; i < polygon.length-2;i+=2) {
            lines.add(new double[] {polygon[i], polygon[i+1], polygon[i+2], polygon[i+3]});
        }
        return lines;
    }

    private List<double[]> getLines(List<double[]> pointList) {

        double[] polygon = new double[pointList.size() *2 + 2];
        int k = 0;

        for(double[] p : pointList) {
            polygon[k] = p[0];
            k++;
            polygon[k] = p[1];
            k++;
        }

        if(!pointList.isEmpty()) {
            polygon[k] = pointList.get(0)[0];
            k++;
            polygon[k] = pointList.get(0)[1];
        }

        return getLines(polygon);
    }

    private boolean isFront(double[] line, double[] point) {

        double result = ((line[3] - line[1]) * point[0] - (line[2] - line[0]) * point[1] + line[2] * line[1] - line[3]*line[0])/
                Math.sqrt(Math.pow((line[3] - line[1]), 2) + Math.pow((line[2] - line[0]), 2));

        return result >= 0;
    }

    private double[] intersection(double[] line, double[] p, double[] q) {
        double[] a = new double[] {line[0], line[1]};
        double[] b = new double[] {line[2], line[3]};
        double A1 = b[1] - a[1];
        double B1 = a[0] - b[0];
        double C1 = A1 * a[0] + B1 * a[1];

        double A2 = q[1] - p[1];
        double B2 = p[0] - q[0];
        double C2 = A2 * p[0] + B2 * p[1];

        double det = A1 * B2 - A2 * B1;
        double x = (B2 * C1 - B1 * C2) / det;
        double y = (A1 * C2 - A2 * C1) / det;

        return new double[]{x, y};
    }

    public double[] getGPSCoordinates() {
        return GPSCoordinates;
    }

    @Override
    public String toString() {
        return "Area [GPSCoordinates=" + Arrays.toString(GPSCoordinates) + "]";
    }
}