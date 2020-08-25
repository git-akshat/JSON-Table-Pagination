package com.example.jsontable;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private RequestQueue mQueue;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonParse = findViewById(R.id.button_parse);
        tableLayout = findViewById(R.id.table);
        tableLayout.setStretchAllColumns(true);
        mProgressBar = new ProgressDialog(this);

        mQueue = Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Data...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();

        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        final List<Airport> airports = new ArrayList<>();

        String url = "https://gist.githubusercontent.com/tdreyno/4278655/raw/7b0762c09b519f40397e4c3e100b097d861f5588/airports.json";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            getSupportActionBar().setTitle("Airport (" + String.valueOf(response.length() + ")"));
                            tableLayout.removeAllViews();
                            // set table header
                            createTableRow("City", "Airport", "Code", "Country", -1);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject employee = response.getJSONObject(i);
                                String city = employee.getString("city");
                                String name = employee.getString("name");
                                String code = employee.getString("code");
                                String country = employee.getString("country");
                                airports.add(new Airport(city, name, code, country));
                                createTableRow(city, name, code, country, i);
                            }
                            mProgressBar.hide();
//                            SortUtil.sortByCity(airports);
                        } catch (JSONException e) {
                            mProgressBar.hide();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void createTableRow(String city, String airport, String code, String country, int i) {
        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(lp);

        TextView textViewCity = new TextView(this);
        TextView textViewAirport = new TextView(this);
        TextView textViewCode = new TextView(this);
        TextView textViewCountry = new TextView(this);

        textViewCity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
        textViewAirport.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
        textViewCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0));
        textViewCountry.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));

        textViewAirport.setGravity(Gravity.CENTER);
        textViewCity.setGravity(Gravity.CENTER);
        textViewCode.setGravity(Gravity.CENTER);
        textViewCountry.setGravity(Gravity.CENTER);

        textViewCity.setMaxLines(4);
        textViewAirport.setMaxLines(4);
        textViewCountry.setMaxLines(3);

        textViewCity.setPadding(5, 15, 5, 15);
        textViewAirport.setPadding(5, 15, 5, 15);
        textViewCode.setPadding(5, 15, 5, 15);
        textViewCountry.setPadding(5, 15, 5, 15);

        textViewCity.setText(city);
        textViewAirport.setText(airport);
        textViewCode.setText(code);
        textViewCountry.setText(country);

        textViewCity.setBackgroundResource(R.drawable.cell_shape_grey);
        textViewAirport.setBackgroundResource(R.drawable.cell_shape_white);
        textViewCode.setBackgroundResource(R.drawable.cell_shape_grey);
        textViewCountry.setBackgroundResource(R.drawable.cell_shape_white);

        tableRow.addView(textViewCity);
        tableRow.addView(textViewAirport);
        tableRow.addView(textViewCode);
        tableRow.addView(textViewCountry);

        if (i == -1) {
            textViewCity.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_medium));
            textViewAirport.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_medium));
            textViewCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_medium));
            textViewCountry.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_medium));
            textViewAirport.setBackgroundResource(R.drawable.cell_shape_grey_dark);
            textViewCountry.setBackgroundResource(R.drawable.cell_shape_grey_dark);
        }
        tableLayout.addView(tableRow, i+1);
    }
}