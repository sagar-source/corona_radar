package com.example.coronaradar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaradar.Model.Corona;
import com.example.coronaradar.R;

import java.util.List;

public class CoronaAdapter extends RecyclerView.Adapter<CoronaAdapter.IssueViewHolder> {

    Context mCtx;
    List<Corona> coronaList;

    public CoronaAdapter(Context mCtx, List<Corona> coronaList) {
        this.mCtx = mCtx;
        this.coronaList = coronaList;
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.corona_list_layout, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, final int position) {
     holder.txt_country.setText(coronaList.get(position).getCountry());
     holder.txt_total.setText(String.valueOf(coronaList.get(position).getConfirmed()));
     holder.txt_recovered.setText(String.valueOf(coronaList.get(position).getRecovered()));
     holder.txt_deaths.setText(String.valueOf(coronaList.get(position).getDeaths()));
    }

    @Override
    public int getItemCount() {
        return coronaList.size();
    }

    class IssueViewHolder extends RecyclerView.ViewHolder {

        TextView txt_country,txt_total,txt_recovered,txt_deaths;

        public IssueViewHolder(View itemView) {
            super(itemView);
            txt_country = itemView.findViewById(R.id.txt_country_name);
            txt_total = itemView.findViewById(R.id.txt_total);
            txt_recovered = itemView.findViewById(R.id.txt_recovered);
            txt_deaths = itemView.findViewById(R.id.txt_deaths);
        }
    }
}

