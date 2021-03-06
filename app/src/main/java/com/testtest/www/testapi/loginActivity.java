package com.testtest.www.testapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class loginActivity extends AppCompatActivity {

    private EditText eUsername,ePassword;
    private Button btnLogin;
    private Button btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       eUsername = (EditText) findViewById(R.id.apiTxtName);
       ePassword = (EditText) findViewById(R.id.apiTxtPassword);
       btnLogin = (Button) findViewById(R.id.apiBtnLogin);
       btnList = (Button) findViewById(R.id.btnList);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = eUsername.getText().toString();
                final String password = ePassword.getText().toString();
               // Toast.makeText(loginActivity.this,ePassword.getText(),Toast.LENGTH_LONG).show();
                // new AsyncLogin().execute();
                new AsyncLogin().execute(username,password);

            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentList = new Intent(loginActivity.this,ListDatActivity.class);
                startActivity(intentList);
                loginActivity.this.finish();
            }
        });

    }


    private class AsyncLogin extends AsyncTask<String, String, String>
    {

        ProgressDialog pdLoading = new ProgressDialog(loginActivity.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                //Toast.makeText(loginActivity.this,"hello",Toast.LENGTH_LONG).show();
                //url = new URL("http://192.168.64.2/Rithy/TestAndroidWebService/login.php");
                url = new URL("http://mobile.rithydev.com/test/login.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";

            }

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open  connectionfor sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            //Toast.makeText(loginActivity.this,query.toString(),Toast.LENGTH_LONG).show();

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method

                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(loginActivity.this,result.toString(),Toast.LENGTH_LONG).show();
            //this method will be running on UI thread

           // Toast.makeText(loginActivity.this,result.toString(),Toast.LENGTH_LONG).show();

            pdLoading.dismiss();


            if(result.equalsIgnoreCase("true"))
            {

                Intent intent = new Intent(loginActivity.this,MainActivity.class);
                startActivity(intent);
                loginActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(loginActivity.this, "Invalid userename or password", Toast.LENGTH_LONG);

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(loginActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG);

            }

        }

    }

}










