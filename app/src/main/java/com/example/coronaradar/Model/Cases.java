package com.example.coronaradar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Cases {

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("TotalConfirmed")
    @Expose
    private int TotalConfirmed;

    @SerializedName("TotalRecovered")
    @Expose
    private int TotalRecovered;

    @SerializedName("TotalDeaths")
    @Expose
    private int TotalDeaths;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public int getTotalConfirmed() {
        return TotalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        TotalConfirmed = totalConfirmed;
    }

    public int getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        TotalRecovered = totalRecovered;
    }

    public int getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public static Comparator<Cases> confirmedcases = new Comparator<Cases>() {

        public int compare(Cases c1, Cases c2) {

            int cnf1 = c1.getTotalConfirmed();
            int cnf2 = c2.getTotalConfirmed();

            /*For ascending order*/
            return cnf1-cnf2;

        }};
}
