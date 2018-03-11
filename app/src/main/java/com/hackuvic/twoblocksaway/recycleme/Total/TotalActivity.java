package com.hackuvic.twoblocksaway.recycleme.Total;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hackuvic.twoblocksaway.recycleme.R;
import com.hackuvic.twoblocksaway.recycleme.dao.TypeDao;
import com.hackuvic.twoblocksaway.recycleme.model.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TotalActivity extends AppCompatActivity {
    private String TAG = TotalActivity.class.getSimpleName();
    private static final String SERVICE_URL = "http://recycleme.azurewebsites.net/api/RecyclingItemsApi";
    private ProgressDialog progressDialog;
    private int total = 0;
    private List<Type> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        //TextView refundTextView = findViewById(R.id.txtTotalRefund);

        TypeDao typeDao = new TypeDao(this);
        types = typeDao.findAllTypes();
        new Contacts().execute();
    }

    private class Contacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler http = new HttpHandler();
            for (Type type : types) {
                String jsonStr = http.makeServiceCall(SERVICE_URL + "/" + type.getName());
                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr == null) {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                    return null;
                }

                try {
                    JSONObject object = new JSONObject(jsonStr);
                    System.out.println(type.getCount());
                    System.out.println(object.getInt("recyclingFee"));
                    total += object.getInt("recyclingFee") * type.getCount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TotalActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                TextView refundTextView = findViewById(R.id.txtTotalRefund);
                double amount = total / 100.0;
                refundTextView.setText(String.format("$ %.2f", amount));
                progressDialog.dismiss();
            }
        }
    }
}
