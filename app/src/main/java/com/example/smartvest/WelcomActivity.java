package com.example.smartvest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        TextView IdText = (TextView) findViewById(R.id.IdText);
        TextView PassWordText = (TextView) findViewById(R.id.PassWordText);
        Button managementButton =(Button) findViewById(R.id.managementButton);
        Button safetyButton = (Button) findViewById(R.id.safetyButton);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");


        IdText.setText(userID);
        PassWordText.setText(userPassword);


        if(!userID.equals("admin"))
        {
            managementButton.setVisibility(View.GONE);
        }
        else
        {
            safetyButton.setVisibility(View.GONE);
        }

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });

        safetyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(WelcomActivity.this, Safety.class);
                startActivity(intent);
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> implements com.example.smartvest.BackgroundTask {
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://smartvest.dothome.co.kr/List.php";
        }

        @Override
        protected String doInBackground(Void...voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }


        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(WelcomActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            WelcomActivity.this.startActivity(intent);
        }

    }

}