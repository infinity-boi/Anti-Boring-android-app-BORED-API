package com.project.myapplication;


import java.io.IOException;
import java.io.InputStream;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.AsyncTask;


public class OpenAICompletionClient {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/engines/text-davinci-003/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void makeApiCall(final OpenAICallback callback) {
        new OpenAITask(callback).execute();
    }

    public interface OpenAICallback {
        void onResult(String results);
    }

    private static class OpenAITask extends AsyncTask<Void, Void, String> {
        private OpenAICallback callback;
        private String activity;

        OpenAITask(OpenAICallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();


            String a = "How to be a good human being";
            String requestBody = "{\"prompt\": \"" + a + "\",\"max_tokens\":100}";

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(RequestBody.create(JSON, requestBody))
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String responseJson = response.body().string();
                    throw new IOException("API call failed with code: " + response.code());

                }
                return response.body().string();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String results) {
            callback.onResult(results);
        }
    }
}
