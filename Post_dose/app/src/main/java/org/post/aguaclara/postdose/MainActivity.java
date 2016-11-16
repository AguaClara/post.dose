package org.post.aguaclara.postdose;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;


public class MainActivity extends Activity{
    private TextView mOutputText;
    private Button mCallApiButton;

    Intent incomingIntent;
    float rawWaterTurbidity;
    public ModelContainer modelContainer;
    UpdateModelReceiver myReceiver = null;
    Boolean myReceiverIsRegistered = false;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallApiButton = new Button(this);
        mCallApiButton.setText(getString(R.string.button));
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button pressed");
                mCallApiButton.setEnabled(false);
                getModel();
                mOutputText.setText("Waiting...");
                mCallApiButton.setEnabled(true);
            }
        });
        activityLayout.addView(mCallApiButton);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(getText(R.string.default_text));
        activityLayout.addView(mOutputText);

        setContentView(activityLayout);

        modelContainer = new ModelContainer();
        // Get the intent that started this activity
        Intent intent = getIntent();
        System.out.println(intent);
        String str = getString(R.string.from_collect_intent);
        if (!intent.getAction().equals("")) {
            System.out.println(intent.getAction());
            System.out.println("wanting: " + str);
        }
        if (intent.getAction().equals(getString(R.string.eval_regression_intent)))  {
            getModel();
            incomingIntent = intent;
            //establish model
            modelContainer.loadModel(getApplicationContext());

            Bundle b = incomingIntent.getExtras();

            String s = b.get("rawWaterTurbidity").toString();
            String plantName = b.get("plantName").toString();
            rawWaterTurbidity = 0.0f;
            try {
                rawWaterTurbidity = Float.valueOf(s);
            } catch (Exception e) {
                System.out.println("an exception was raised, worried? for " + s);
                e.printStackTrace();
            }

            sendAnswerBackToApp(modelContainer.getBestDosageRecommendation(rawWaterTurbidity));
        }
        if (intent.getAction().equals(str)) {
            getModel();
        }
        myReceiver = new UpdateModelReceiver();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public String getModel(){
        if (! isDeviceOnline()) {
            System.out.println("Device is offline");
            return "No network connection available.";
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =//"https://script.google.com/macros/s/AKfycbz4EsxZF_UQi5LmjU3NXY16V3wxB3mT_UMSuw2LsC4h2RXJxYg/exec";//aguaclara account
                String.format("https://script.google.com/macros/s/AKfycbwu8nLp3h1TKrOo2rqPRB1--kvZx5AWrEKBOhAT793VeEeUroA5/exec?param1=%1$s",
                        "Moroceli");//development account (Andrew's)
        HashMap mParams = new HashMap<String, String>();
        mParams.put("plantName", "Moroceli");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: "+ response);
                        modelContainer.setFromJSON(response);
                        modelContainer.saveModelCollection(getApplicationContext());
                        System.out.println("Model updated with " + response);
                        mOutputText.setText("Success. \n " + modelContainer.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println("That didn't work!");
                mOutputText.setText("No network connection available.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return modelContainer.toString();
    }

    //needed for the broadcast listener
    @Override
    protected void onResume() {
        super.onResume();
        if (!myReceiverIsRegistered) {
            registerReceiver(myReceiver, new IntentFilter(getString(R.string.from_collect_intent)));
            myReceiverIsRegistered = false;
        }
    }

    //needed for the broadcast listener
    @Override
    protected void onPause() {
        super.onPause();
        if (myReceiverIsRegistered) {
            unregisterReceiver(myReceiver);
            myReceiverIsRegistered = false;
        }
    }

    private void sendAnswerBackToApp(float response) {
        //If the returned bundle of values contains
        // values whose keys match the type and the
        // name of the sub-fields, then these values
        // overwrite the current values of those sub-fields.
        Intent intent = new Intent();
        intent.putExtra("value", response);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Listens for a broadcast from POST.collect
     */
    public class UpdateModelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            getModel();
        }
    }

}
