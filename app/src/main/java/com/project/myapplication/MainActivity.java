package com.project.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.concurrent.TimeUnit;

import com.theokanning.openai.*;
import com.theokanning.openai.completion.CompletionRequest;

import okio.AsyncTimeout;


public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private static Button mButton2;
    private TextView mTextView;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private static TextView poph;
    private static TextView pop;
    private static String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button2);
        mTextView = findViewById(R.id.textView);
        mTextView1 = findViewById(R.id.textView4);
        mTextView2 = findViewById(R.id.textView6);
        mTextView3 = findViewById(R.id.textView8);
        mTextView4 = findViewById(R.id.textView10);


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        poph = popupView.findViewById(R.id.textView12);
        pop = popupView.findViewById(R.id.popup_text);
        //mProgressBar = popupView.findViewById(R.id.progressBar);
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BoredApiTask().execute();
                vib.vibrate(80);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                new howto().execute();

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


        } catch (JSONException e) {
            Log.e("MainActivity", "Error parsing JSON: " + e.getMessage());
            mTextView.setText("Error");
        }

        }

    }

    private static class howto extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OpenAiService service = new OpenAiService("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",100);
            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt("How to"+activity+"in 5 steps")
                    .model("text-davinci-003")
                    .temperature(0.3)
                    .maxTokens(400)
                    .topP(1.0)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.0)
                    .echo(false)
                    .build();

            return service.createCompletion(completionRequest).getChoices().get(0).getText();
        }

        @Override
        protected void onPostExecute(String completedTexts) {

            poph.setText(activity);
            pop.setText(completedTexts);
        }
    }

}


