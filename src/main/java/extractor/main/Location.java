package extractor.main;

public class Location {

    private double longitude;
    private double latitude;


    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;


    }

    public double distanceTo(Location location) {

        double r = 6371e3; // radius of Earth in meters
        double phi1 = Math.toRadians(this.latitude);
        double phi2 = Math.toRadians(location.latitude);
        double deltaPhi = Math.toRadians(location.latitude - this.latitude);
        double deltaLambda = Math.toRadians(location.longitude - this.longitude);

        double a = Math.pow(Math.sin(deltaPhi/2),2) +
                Math.cos(phi1/2) * Math.cos(phi2/2) *
                        Math.pow(Math.sin(deltaLambda/2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 -a ));

        return r * c;


    }

    @Override
    public String toString() {
        return "Location [longitude=" + longitude + ", latitude=" + latitude + "]";
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
