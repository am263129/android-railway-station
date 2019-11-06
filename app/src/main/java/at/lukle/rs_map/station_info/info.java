package at.lukle.rs_map.station_info;

public class info {
    public Integer Info_pic;
    public String Info_name;
    public String Info_distance;
    public String Info_description;

    public info(){

    }



    public info(String  info_name, Integer info_pic, String info_distance, String info_description ){
        this.Info_name = info_name;
        this.Info_pic = info_pic;
        this.Info_distance = info_distance;
        this.Info_description = info_description;
    }

    public String getInfo_description() {
        return Info_description;
    }

    public String getInfo_distance() {
        return Info_distance;
    }

    public String getInfo_name() {
        return Info_name;
    }

    public Integer getInfo_pic() {
        return Info_pic;
    }
}
