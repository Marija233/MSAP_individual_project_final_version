package com.example.msap_individual_project_final_version.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeatureBean implements Serializable {

    @SerializedName("type")
    private String type;
    @SerializedName("properties")
    private PropertiesBean propertiesBean;
    @SerializedName("geometry")
    private GeometryBean geometryBean;

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public PropertiesBean getPropertiesBean() {
        return propertiesBean;
    }

    public void setPropertiesBean(PropertiesBean propertiesBean) {
        this.propertiesBean = propertiesBean;
    }

    public GeometryBean getGeometryBean() {
        return geometryBean;
    }

    public void setGeometryBean(GeometryBean geometryBean) {
        this.geometryBean = geometryBean;
    }
}