package sau.phpexampe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

// test.php located inside -> go to the project view and find the file.

public class MainActivity extends AppCompatActivity {

    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.enableDefaults(); //STRICT MODE ENABLED

        resultView = (TextView) findViewById(R.id.resultView);
        try {
            new MainAsync().execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    protected class MainAsync extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try {

                JSONArray jsonArray = null;

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", "Saurabh");
                    jsonObject.put("email", "sau@gmail.com");
                    URL url = new URL("http://t3teach.com/test/test.php");


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    String output = jsonObject.toString();
                    writer.write(output);
                    writer.flush();
                    writer.close();

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();


                    String temp;
                    while ((temp = streamReader.readLine()) != null)
                        responseStrBuilder.append(temp);


                    jsonArray = new JSONArray(responseStrBuilder.toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject json = jsonArray.getJSONObject(i);

                    s = s + " Name : " + json.getString("name") + " email " + json.getString("email");
                }


            } catch (Exception e) {

                Log.e("log_tag", "Error  converting result " + e.toString());

            }

            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            resultView.setText(s);
        }
    }
}

