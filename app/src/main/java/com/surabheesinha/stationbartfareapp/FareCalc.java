package com.surabheesinha.stationbartfareapp;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Created by surabheesinha on 9/4/17.
 */

public class FareCalc extends AppCompatActivity {
    Model model;
    Object sourceobj;
    Object destobj;
    String destobjstring;
    String sourceobjstring;
    List<Model> stationlist = new ArrayList<>();
    List<String> stnlist = new ArrayList<>();
    Spinner spinnersp;
    Spinner destinationsp;
    TextView farestext1,farestext,Sourcename,DestinationName;
    Button button;
    LinearLayout faresvisible;
    String urlsrcdest="http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V&json=y";
    //String urlfares=;
    String Tag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farecalc);
        farestext1 = (TextView)findViewById(R.id.farestext1);
        farestext = (TextView)findViewById(R.id.farestext);
        button = (Button)findViewById(R.id.buttonfare);
        faresvisible = (LinearLayout)findViewById(R.id.faresvisible);

        new DownloadJson().execute();

    }
    /*public String onClick(View view) {
        try {

            URL url = new URL("http://api.bart.gov/api/sched.aspx?cmd=fare&orig=" + source + "&dest=" + dest + "&date=today&key=MW9S-E7SL-26DU-VV8V&json=y");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                String resultfares = stringBuilder.toString();
                //return result;
                JSONObject jsonObject = new JSONObject(resultfares);
                ;
                JSONArray jsonArray;
                String nameStation = "";
                String abbrStation = "";
                String address = "";
                JSONObject rootobj = jsonObject.getJSONObject("root");
                JSONObject stationobj = rootobj.getJSONObject("fares");
                JSONArray fareArray = stationobj.getJSONArray("fare");
                JSONObject newobj = fareArray.getJSONObject(0);
                String amount = newobj.getString("@amount");
              return amount;

            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }*/


    private class DownloadJson extends AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{

                URL url = new URL("http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V&json=y");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    String result = stringBuilder.toString();
                    return result;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //tv.setText(result);
            final String sourcsp ;
            final String destsp ;
            try {
                JSONObject jsonObject = new JSONObject(result);;
                JSONArray jsonArray;
                String nameStation ="";
                String abbrStation="";
                String address ="";
                JSONObject rootobj = jsonObject.getJSONObject("root");
                JSONObject stationobj = rootobj.getJSONObject("stations");
                JSONArray stationArray = stationobj.getJSONArray("station");

                for(int i = 0;i < stationArray.length();i++)
                {
                    JSONObject nwobj = stationArray.getJSONObject(i);
                    //address = nwobj.getString("address");
                    abbrStation = nwobj.getString("abbr");
                    nameStation = nwobj.getString("name");
                    // Model model= new Model(nameStation,abbrStation);
                    // tv.setText(nameStation);
                    //tv.setText(result);
                    model = new Model();
                    //model.setName(nwobj.getString("name"));
                    //stationlist.add(model);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    stnlist.add(nwobj.getString("abbr"));
                    //stnlist.add(nwobj.getString("abbr"));
                    spinnersp = (Spinner)findViewById(R.id.sourcesp);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FareCalc.this,android.R.layout.simple_list_item_1,stnlist);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnersp.setAdapter(arrayAdapter);
                    //CustomOnItemSelectedListener csn = new CustomOnItemSelectedListener();
                    //spinnersp.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                    //Toast.makeText(MainActivity.this,
                    //      "source selected: " +
                    //              "\n" + String.valueOf(spinnersp.getSelectedItem()) ,
                    //      Toast.LENGTH_LONG).show();

                    spinnersp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            sourceobj = parent.getItemAtPosition(position);
                            //Toast.makeText(parent.getContext(), "OnItemSelectedListener source : " + sourceobj.toString(), Toast.LENGTH_SHORT).show();
                            //selected name
                            sourceobjstring =sourceobj.toString();


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    final String source = String.valueOf(spinnersp.getSelectedItem());
                    //spinnersp.getSelectedItem().toString();

                    //Toast.makeText(getApplicationContext(),"the name"+source,Toast.LENGTH_LONG);
                    destinationsp = (Spinner)findViewById(R.id.destinationsp);
                    //destinationsp.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                    destinationsp.setAdapter(arrayAdapter);
                    //final String dest = destinationsp.getSelectedItem().toString();
                    //Log.d(Tag,dest);
                    //Toast.makeText(getApplicationContext(),"the name"+dest,Toast.LENGTH_LONG);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>()
                    //spinnersp.setAdapter(new ArrayAdapter<Model>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,stationlist));
                    //tv.setText(nameStation);
                    // String fareamounttext =  onClick(source,dest);
                    // tv.setText(fareamounttext);
                    destinationsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            destobj = parent.getItemAtPosition(position);
                            //Toast.makeText(parent.getContext(), "OnItemSelectedListener source : " + destobj.toString(), Toast.LENGTH_SHORT).show();
                            destobjstring =destobj.toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            class Onclickbutton extends AsyncTask<Void,Void,String> {
                                @Override
                                protected String doInBackground(Void... params) {
                                    try {
                                        //Log.d("Source", sourceobjstring+"");
                                        //Log.d("Destination", destobjstring);

                                        URL url = new URL("http://api.bart.gov/api/sched.aspx?cmd=fare&orig=" + sourceobjstring + "&dest=" + destobjstring + "&date=today&key=MW9S-E7SL-26DU-VV8V&json=y");
//                                         Log.d("URL", url.toString());
                                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                                        try {
                                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                            StringBuilder stringBuilder = new StringBuilder();
                                            String line;
                                            while ((line = bufferedReader.readLine()) != null) {
                                                stringBuilder.append(line).append("\n");
                                            }
                                            bufferedReader.close();

                                            String resultfares = stringBuilder.toString();
                                            Log.d("resultfares", resultfares.toString());




                                            //faretext.setText(amount);
                                            return resultfares;
                                        } finally {
                                            urlConnection.disconnect();
                                        }
                                    } catch (Exception e) {
                                        Log.e("ERROR", e.getMessage(), e);
                                        return null;

                                    }

                                }

                                @Override
                                protected void onPostExecute(String resultfares) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(resultfares);
                                        Log.d("jsonObject", jsonObject.toString());
                                        //faretext.setText(resultfares);
                                        Log.d("jsonObject", jsonObject.toString());
                                        JSONObject rootobj = jsonObject.getJSONObject("root");
                                        Log.d("rootobj", rootobj.toString());
                                        JSONObject stationobj = rootobj.getJSONObject("fares");
                                        Log.d("stationobj", stationobj.toString());
                                        JSONArray fareArray = stationobj.getJSONArray("fare");
                                        Log.d("fareArray", fareArray.toString());
                                        JSONObject newobj = fareArray.getJSONObject(0);
                                        Log.d("NEW", newobj.toString());
                                        String amount = newobj.getString("@amount");
                                        Log.d("amount", amount.toString());
                                        //Toast.makeText(getApplicationContext(),"the amount is " +amount,Toast.LENGTH_LONG);
                                        faresvisible.setVisibility(View.VISIBLE);
                                        farestext.setText(amount);


                                        //super.onPostExecute(s);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            new Onclickbutton().execute();
                        }
                    });


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }


}
