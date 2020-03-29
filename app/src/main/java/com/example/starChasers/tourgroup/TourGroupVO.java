package com.example.starChasers.tourgroup;

public class TourGroupVO {
    private int logo;
    private String name;
    private int id;

    public TourGroupVO(){

    }
    public TourGroupVO(int id, int logo, String name){
        this.logo = logo;
        this.name = name;
        this.id = id;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
