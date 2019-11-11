package at.lukle.rs_map;

/**
 * Created by Lukas on 10/24/2015.
 */
public class State {

    private String Name;
    private Integer area_X;
    private Integer area_Y;
    private Integer center_X;
    private Integer center_Y;
    private Double lat;
    private Double lng;
    private String trainName;

    public State(){

    }

    public State(String name,Integer area_x, Integer area_y, Integer center_x, Integer center_y, Double lat, Double lng,String trainname) {
        this.Name = name;
        this.area_X = area_x;
        this.area_Y = area_y;
        this.center_X = center_x;
        this.center_Y = center_y;
        this.lat = lat;
        this.lng = lng;
        this.trainName = trainname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Integer getArea_X() {
        return area_X;
    }

    public Integer getArea_Y() {
        return area_Y;
    }

    public Integer getCenter_X() {
        return center_X;
    }

    public Integer getCenter_Y() {
        return center_Y;
    }

    public String getTrainName() {
        return trainName;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
