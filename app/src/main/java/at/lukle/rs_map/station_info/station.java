package at.lukle.rs_map.station_info;

import java.util.ArrayList;

public class station {

    public String Name;
    public String Id;
    public ArrayList<String> Tubes;
    public ArrayList<String> Directions;
    public ArrayList<String> Time_A;
    public ArrayList<String> Time_B;
    public station(){

    }

    public void station(String name, String id, ArrayList<String> tubes, ArrayList<String> directions, ArrayList<String> time_A, ArrayList<String> time_B){
        this.Name = name;
        this.Directions = directions;
        this.Id = id;
        this.Tubes = tubes;
        this.Time_A = time_A;
        this.Time_B = time_B;
    }

    public String getName() {
        return Name;
    }

    public ArrayList<String> getDirections() {
        return Directions;
    }

    public ArrayList<String> getTime_A() {
        return Time_A;
    }

    public ArrayList<String> getTime_B() {
        return Time_B;
    }

    public ArrayList<String> getTubes() {
        return Tubes;
    }

    public String getId() {
        return Id;
    }

}
