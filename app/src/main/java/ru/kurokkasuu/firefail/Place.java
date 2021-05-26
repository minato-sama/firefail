package ru.kurokkasuu.firefail;

public class Place {
    public String name;
    public double lat, lon;

    public Place(){
        // конструктор по умолчанию для FireBase
    }
    public Place(String name, double lat, double lon){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "name:" + name + ", lat=" + lat + ", lon=" + lon;
    }
}
