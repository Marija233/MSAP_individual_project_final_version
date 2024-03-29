package com.example.msap_individual_project_final_version.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeometryBean implements Serializable {

    @SerializedName("type")
    private String type;
    @SerializedName("coordinates")
    private String[] coordinates;

    public String[] getCoordinates() { return coordinates; }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){ this.type = type; }

}
