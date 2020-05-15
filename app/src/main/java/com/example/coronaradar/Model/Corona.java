package com.example.coronaradar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class Corona {

    @SerializedName("Countries")
    public List<Cases> Countries = null;

    public List<Cases> getCountries() {
        return Countries;
    }

    public void setCountries(List<Cases> countries) {
        Countries = countries;
    }
}
