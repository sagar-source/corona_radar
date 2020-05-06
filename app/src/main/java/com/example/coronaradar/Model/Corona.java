package com.example.coronaradar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Corona {

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("confirmed")
    @Expose
    private int confirmed;

    @SerializedName("recovered")
    @Expose
    private int recovered;

    @SerializedName("deaths")
    @Expose
    private int deaths;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public static Comparator<Corona> confirmedcases = new Comparator<Corona>() {

        public int compare(Corona c1, Corona c2) {

            int cnf1 = c1.getConfirmed();
            int cnf2 = c2.getConfirmed();

            /*For ascending order*/
            return cnf1-cnf2;

            /*For descending order*/
            //rollno2-rollno1;
        }};
}
