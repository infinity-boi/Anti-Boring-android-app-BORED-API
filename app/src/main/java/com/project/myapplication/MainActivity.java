package com.project.myapplication;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private Button mButton2;
    private TextView mTextView;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView poph;
    private TextView pop;
    private static String completedText;
    private static String activity;
    private static View mProgressBar;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        final Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button2);
        mTextView = findViewById(R.id.textView);
        mTextView1 = findViewById(R.id.textView4);
        mTextView2 = findViewById(R.id.textView6);
        mTextView3 = findViewById(R.id.textView8);
        mTextView4 = findViewById(R.id.textView10);















        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BoredApiTask().execute();
                vib.vibrate(50);



            }
        });







    }




    public class BoredApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL apiUrl = new URL("https://www.boredapi.com/api/activity");
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                String responseBody = "";
                if (scanner.hasNext()) {
                    responseBody = scanner.next();
                }
                return responseBody;
            } catch (IOException e) {
                Log.e("MainActivity", "Error: " + e.getMessage());
            }
            return null;

        }






        @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject json = new JSONObject(result);
            activity = json.getString("activity");
            String typeo = json.getString("type");
            String parti = json.getString("participants");
            String price = json.getString("price");
            String acc = json.getString("accessibility");
            mTextView.setText(activity);
            mTextView1.setText(typeo);
            mTextView2.setText(parti);
            mTextView3.setText(price);
            mTextView4.setText(acc);
            howto.how();
            OpenAICompletionClient op = new OpenAICompletionClient(activity);

            mButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup,null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                    poph = popupView.findViewById(R.id.textView12);
                    pop=popupView.findViewById(R.id.popup_text);
                    mProgressBar = popupView.findViewById(R.id.progressBar);
                    boolean focusable = true;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    poph.setText(activity);
                    pop.setText(completedText);



                }
            });





        } catch (JSONException e) {
            Log.e("MainActivity", "Error parsing JSON: " + e.getMessage());
            mTextView.setText("Error");
        }
    }

}

    public static class howto{
        protected static void how(){
            OpenAICompletionClient.makeApiCall(results -> {
                mProgressBar.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                TextCompletionResponse textCompletionResponse = gson.fromJson(results, TextCompletionResponse.class);
                completedText = textCompletionResponse.getChoices().get(0).getText();
                mProgressBar.setVisibility(View.GONE);

            });
        }
    }
}
