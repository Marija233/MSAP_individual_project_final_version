package com.example.msap_individual_project_final_version.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EarthquakeBean {

    @SerializedName("features")
    private List<FeatureBean> features;

    public List<FeatureBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureBean> features) {
        this.features = features;
    }
}
