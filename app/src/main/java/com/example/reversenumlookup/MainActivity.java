package com.example.reversenumlookup;

import com.example.reversenumlookup.BuildConfig;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private EditText phoneInput;
    private Button lookupButton;
    private TextView resultHeaderTextView;
    private TableLayout resultTableLayout;
    private TextView tableNumber, tableValid, tableCountry, tableLocation, tableCarrier, tableLineType;

    private RequestQueue requestQueue;
    private final String API_KEY = BuildConfig.MY_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI elements from activity_main.xml
        phoneInput = findViewById(R.id.phoneTextbox);
        lookupButton = findViewById(R.id.searchButton);
        resultHeaderTextView = findViewById(R.id.resultHeaderTextView); // Link to the header TextView
        resultTableLayout = findViewById(R.id.resultTableLayout); // Link to the TableLayout

        // Link TextViews within the TableLayout
        tableNumber = findViewById(R.id.tableNumber);
        tableValid = findViewById(R.id.tableValid);
        tableCountry = findViewById(R.id.tableCountry);
        tableLocation = findViewById(R.id.tableLocation);
        tableCarrier = findViewById(R.id.tableCarrier);
        tableLineType = findViewById(R.id.tableLineType);

        // Initialize the Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set a click listener for the search button
        lookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawPhoneNumber = phoneInput.getText().toString().trim();
                // Strip non-digit characters before validation and API call
                String cleanedPhoneNumber = rawPhoneNumber.replaceAll("[^\\d]", "");

                if (validatePhoneNumber(cleanedPhoneNumber)) {
                    lookupPhoneNumber(cleanedPhoneNumber);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid 10-digit phone number.", Toast.LENGTH_SHORT).show();
                    // Hide results and clear table on invalid input
                    resultHeaderTextView.setVisibility(View.INVISIBLE);
                    resultTableLayout.setVisibility(View.INVISIBLE);
                    clearTable();
                }
            }
        });
    }

    // Method to validate the phone number (now validates the cleaned number)
    private boolean validatePhoneNumber(String phoneNumber) {
        // Check if it's exactly 10 digits
        return phoneNumber.length() == 10;
    }

    // Method to perform the phone number lookup using the API
    private void lookupPhoneNumber(String phoneNumber) {
        // **IMPORTANT:** Prepend a country code (assuming US for 10-digit numbers)
        // You might need to adjust this logic based on the expected input and API requirements
        String phoneNumberWithCountryCode = "1" + phoneNumber; // Assuming US country code '1'

        // Construct the API URL with the phone number including country code
        String url = "https://api.numlookupapi.com/v1/validate/" + phoneNumberWithCountryCode + "?apikey=" + API_KEY;

        // JsonObjectRequest to fetch data from the API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean valid = response.getBoolean("valid");

                            if (valid) {
                                // Populate the table with data
                                tableNumber.setText(response.getString("number"));
                                tableValid.setText(String.valueOf(valid)); // Convert boolean to String
                                tableCountry.setText(response.getString("country_name"));
                                tableLocation.setText(response.getString("location"));
                                tableCarrier.setText(response.getString("carrier"));
                                tableLineType.setText(response.getString("line_type"));

                                // Make header and table visible
                                resultHeaderTextView.setVisibility(View.VISIBLE);
                                resultTableLayout.setVisibility(View.VISIBLE);

                            } else {
                                // Handle cases where the API indicates the number is invalid
                                Toast.makeText(MainActivity.this, "Invalid phone number according to API.", Toast.LENGTH_SHORT).show();
                                // Hide results and clear table
                                resultHeaderTextView.setVisibility(View.INVISIBLE);
                                resultTableLayout.setVisibility(View.INVISIBLE);
                                clearTable();
                            }

                        } catch (JSONException e) {
                            // Handle JSON parsing errors
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing API response.", Toast.LENGTH_SHORT).show();
                            // Hide results and clear table on error
                            resultHeaderTextView.setVisibility(View.INVISIBLE);
                            resultTableLayout.setVisibility(View.INVISIBLE);
                            clearTable();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle network or API errors
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error fetching data from API.", Toast.LENGTH_SHORT).show();
                // Hide results and clear table on error
                resultHeaderTextView.setVisibility(View.INVISIBLE);
                resultTableLayout.setVisibility(View.INVISIBLE);
                clearTable();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    // Method to clear the text in the table TextViews
    private void clearTable() {
        tableNumber.setText("");
        tableValid.setText("");
        tableCountry.setText("");
        tableLocation.setText("");
        tableCarrier.setText("");
        tableLineType.setText("");
    }
}