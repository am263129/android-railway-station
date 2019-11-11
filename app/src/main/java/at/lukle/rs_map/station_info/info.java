package at.lukle.rs_map.station_info;

public class info {
    public String type;
    public String station_name;
    public String Info_pic;
    public String Info_name;
    public String Info_distance;
    public String Info_description;
    public Double lat;
    public Double lng;
    public info(){

    }



    public info(String type,String  info_name, String info_pic_url, String info_distance, String info_description , String station_name, Double lat, Double lng ){
        this.Info_name = info_name;
        this.Info_pic = info_pic_url;
        this.Info_distance = info_distance;
        this.Info_description = info_description;
        this.station_name = station_name;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
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

    public String getInfo_pic() {
        return Info_pic;
    }

    public String getStation_name() {
        return station_name;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    public String getType() {
        return type;
    }
}
