package com.example.raivai.currencyconverter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Spinner Fr,To;
    EditText input;
    TextView Res;
    Button Con;

    String from;
    String to;
    String Data[];
    //String listCurrency[]=new String[Data.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fr=(Spinner)findViewById(R.id.spinnerFrom);
        To=(Spinner)findViewById(R.id.spinnerTo);
        input=(EditText)findViewById(R.id.Cvalue);
        Con=(Button)findViewById(R.id.convert);
        Res=(TextView)findViewById(R.id.Result);
        //Data=new String[];
        Data=getResources().getStringArray(R.array.Cname);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Cname,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        Fr.setAdapter(adapter);
        Fr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from=Data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              //  from=Data[0];
            }
        });

        To.setAdapter(adapter);
        To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to=Data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // to=Data[0];
            }
        });

        Con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //main conversion
                Res.setText("Please Wait...");
                new MyTask().execute("https://free.currencyconverterapi.com/api/v6/convert?q="+from+"_"+to+"&compact=y");
            }
        });
    }



    private class MyTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPostExecute(String result) {
            StringBuilder Finalresult=new StringBuilder();
            try{
                JSONObject js=new JSONObject(result);
                JSONObject FromTo=js.getJSONObject(from+"_"+to);
                Double unitperVal=FromTo.getDouble("val");
                Finalresult.append("1 "+from+" = "+unitperVal+" "+to);
                String in=input.getText().toString();
                Double Value=Double.parseDouble(in);
                Double res=Value*unitperVal;
                Finalresult.append("\n"+in+" "+from+" = "+res+" "+to);
                Res.setText(Finalresult.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(),"Please Input any value",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result=new StringBuilder();
            HttpURLConnection urlConnection=null;
            try{
                URL url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=reader.readLine())!=null){
                    result.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }
    }
}
