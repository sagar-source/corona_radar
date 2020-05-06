package com.example.coronaradar.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronaradar.API.API;
import com.example.coronaradar.Adapter.CoronaAdapter;
import com.example.coronaradar.Model.Corona;
import com.example.coronaradar.R;
import com.example.coronaradar.NetworkClient.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    RecyclerView recyclerView;
    CoronaAdapter adapter;

    ProgressDialog prog_dialog;
    Dialog dialog;

    TextView total,recover,death;
    Disposable disposable;

    ArrayList list_case_type,list_val;

    List<Corona> list;

    Button btn_filter;

    static String CASE_TYPE,VAL;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linear_layout);

        list = new ArrayList<>();

        prog_dialog = new ProgressDialog(MainActivity.this);
        dialog = new Dialog(MainActivity.this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        total = findViewById(R.id.total);
        recover = findViewById(R.id.recover);
        death = findViewById(R.id.death);

        btn_filter = findViewById(R.id.btn_filter);

        list_case_type = new ArrayList();
        list_val = new ArrayList();

        list_case_type.add("Select Case Type");
        list_case_type.add("Total Cases");
        list_case_type.add("Recovered");
        list_case_type.add("Deaths");

        list_val.add("Select Check Type");
        list_val.add("Greater than equal");
        list_val.add("Less than equal");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        disposable = Observable.interval(500, 120000, //refresh data in every 2 minutes
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadData);


    }

    private void loadData(Long aLong) {

        prog_dialog.setTitle("Syncing Data");
        prog_dialog.setMessage("please wait...");
        prog_dialog.setCanceledOnTouchOutside(false);
        prog_dialog.show();

        Retrofit retrofit = RetrofitClient.getRetrofitClient();

        API api = retrofit.create(API.class);

        Observable<List<Corona>> call = api.getCorona();
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DisposableObserver<List<Corona>>() {

                    @Override
                    public void onNext(List<Corona> coronaList) {
                        int sum_total = 0,sum_recover = 0,sum_death = 0;

                        for (int i=0; i<coronaList.size(); i++) {

                            if (coronaList.get(i).getConfirmed() == 0) {
                                coronaList.remove(i);
                                i--;
                            }
                            Collections.sort(coronaList,Corona.confirmedcases);
                            Collections.reverse(coronaList);

                            btn_filter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    applyFilter(coronaList);

                                }
                            });

                            sum_total +=coronaList.get(i).getConfirmed();
                            sum_recover +=coronaList.get(i).getRecovered();
                            sum_death +=coronaList.get(i).getDeaths();
                        }

                        total.setText(String.valueOf(sum_total));
                        recover.setText(String.valueOf(sum_recover));
                        death.setText(String.valueOf(sum_death));

                        showData(coronaList);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void applyFilter(List<Corona> coronaList) {

        dialog.setContentView(R.layout.filter_layout);

        Spinner spin_case = dialog.findViewById(R.id.spin_case_type);
        Spinner spin_value = dialog.findViewById(R.id.spin_val);

        EditText edt_val = dialog.findViewById(R.id.edt_val);

        Button btn_apply = dialog.findViewById(R.id.btn_apply);
        Button btn_clear = dialog.findViewById(R.id.btn_clear);

        dialog.show();

        String c,v,n;

        c = sharedPreferences.getString("CASE_TYPE","");
        v = sharedPreferences.getString("VAL","");
        n = sharedPreferences.getString("EDT_VAL","");

        ArrayAdapter adapter_case = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,list_case_type);
        spin_case.setAdapter(adapter_case);

        if (!c.equals("")) {
            int spinnerPosition = adapter_case.getPosition(c);
            spin_case.setSelection(spinnerPosition);
        }

        spin_case.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position>0) {
                    CASE_TYPE  = parent.getItemAtPosition(position).toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("CASE_TYPE",CASE_TYPE);
                    editor.apply();
                }
                else  {
                    CASE_TYPE = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapter_value = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,list_val);
        spin_value.setAdapter(adapter_value);

        if (!v.equals("")) {
            int spinnerPosition = adapter_value.getPosition(v);
            spin_value.setSelection(spinnerPosition);
        }

        spin_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position>0) {
                    VAL = parent.getItemAtPosition(position).toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("VAL", VAL);
                    editor.apply();
                }
                else {
                    VAL = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!n.equals("")) {
            edt_val.setText(n);
        }


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EDT_VAL="";
                EDT_VAL = edt_val.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("EDT_VAL",EDT_VAL);
                editor.apply();

                if (!CASE_TYPE.equals("") && !VAL.equals("") && !EDT_VAL.equals("") ) {

                    if (CASE_TYPE.equals("Total Cases")) {

                        list = new ArrayList<>();

                        if (VAL.equals("Greater than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getConfirmed() >= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);

                        }
                        else if (VAL.equals("Less than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getConfirmed() <= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);
                        }
                    }
                    else if (CASE_TYPE.equals("Recovered")) {

                        list = new ArrayList<>();

                        if (VAL.equals("Greater than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getRecovered() >= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);

                        }
                        else if (VAL.equals("Less than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getRecovered() <= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);
                        }
                    }

                    else if (CASE_TYPE.equals("Deaths")) {

                        list = new ArrayList<>();

                        if (VAL.equals("Greater than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getDeaths() >= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);

                        }
                        else if (VAL.equals("Less than equal")) {
                            for (int i=0; i<coronaList.size(); i++){
                                if (coronaList.get(i).getDeaths() <= Integer.parseInt(EDT_VAL)) {

                                    list.add(coronaList.get(i));

                                }
                                else {
                                    noData();
                                }
                            }
                            showData(list);
                        }
                    }
                }
                else {
                    showData(list);
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("CASE_TYPE");
                editor.remove("VAL");
                editor.remove("EDT_VAL");
                editor.apply();

                edt_val.setText("");

                showData(coronaList);

            }
        });
    }

    private void noData() {
        Snackbar snackbar = Snackbar
                .make(linearLayout, "No Data Found!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showData(List<Corona> l) {
        adapter = new CoronaAdapter(MainActivity.this, l);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
        prog_dialog.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!sharedPreferences.getString("CASE_TYPE","").equals("") &&
                !sharedPreferences.getString("VAL","").equals("") &&
                  !sharedPreferences.getString("EDT_VAL","").equals("")) {

            showData(list);

        }
        else {

            if (disposable.isDisposed()) {
                disposable = Observable.interval(500, 120000,
                        TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::loadData);

                dialog.dismiss();

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        disposable.dispose();
    }
}
