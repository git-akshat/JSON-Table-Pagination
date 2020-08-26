package com.example.jsontable;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.jsontable.model.Airport;
import com.example.jsontable.utils.SortUtil;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button buttonLoad;
    private TableLayout tableLayout;
    private RequestQueue mQueue;
    private ProgressDialog mProgressBar;
    private int PAGE_SIZE = 50;
    private LinearLayout buttonLayout;
    private Button[] buttons;
    private int no_of_pages;
    private ScrollView scrollView;
    private static TableRow tableRowHeader;
    private List<Airport> airports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLoad = findViewById(R.id.button_parse);
        tableLayout = findViewById(R.id.table);
        tableLayout.setStretchAllColumns(true);
        mProgressBar = new ProgressDialog(this);

        buttonLayout = findViewById(R.id.btnLay);

        scrollView = findViewById(R.id.scroll_view);

        mQueue = Volley.newRequestQueue(this);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {
        mProgressBar.setCancelable(true);
        mProgressBar.setMessage("Fetching Data...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();

        String url = "https://gist.githubusercontent.com/tdreyno/4278655/raw/7b0762c09b519f40397e4c3e100b097d861f5588/airports.json";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            getSupportActionBar().setTitle("Airport (" + String.valueOf(response.length() + ")"));
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject employee = response.getJSONObject(i);
                                String city = employee.getString("city");
                                String name = employee.getString("name");
                                String code = employee.getString("code");
                                String country = employee.getString("country");
                                airports.add(new Airport(city, name, code, country));
                            }
                            createTable(airports, 0);
                            paginate(buttonLayout, response.length(), PAGE_SIZE, airports);
                            checkBtnBackGroud(0);
                            sortData();
                            mProgressBar.hide();
                            buttonLoad.setVisibility(View.GONE);

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

    private void paginate(final LinearLayout buttonLayout, int data_size, int page_size, final List<Airport> airports) {
        no_of_pages = (data_size + page_size - 1) / page_size;
        buttons = new Button[no_of_pages];
        Snackbar.make(buttonLayout, "Page 1 of " + no_of_pages, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.transparent)).show();

        for (int i = 0; i < no_of_pages; i++) {
            buttons[i] = new Button(this);
            buttons[i].setBackgroundColor(getResources().getColor(android.R.color.white));
            buttons[i].setText(String.valueOf(i + 1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayout.addView(buttons[i], lp);

            final int j = i;
            buttons[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    createTable(airports, j);
                    checkBtnBackGroud(j);
                    Snackbar.make(buttonLayout, "Page " + (j + 1) + " of " + no_of_pages, Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.transparent)).show();
                    sortData();
                }
            });
        }
    }

    private void checkBtnBackGroud(int index) {
        for (int i = 0; i < no_of_pages; i++) {
            if (i == index) {
                buttons[index].setBackgroundResource(R.drawable.cell_shape_square_blue);
            } else {
                buttons[i].setBackground(null);
            }
        }
    }

    private void sortData() {
       final TextView tvCity = (TextView) tableRowHeader.getChildAt(1);
       final TextView tvAirport = (TextView) tableRowHeader.getChildAt(2);
       final TextView tvCode = (TextView) tableRowHeader.getChildAt(3);
       final TextView tvCountry = (TextView) tableRowHeader.getChildAt(4);
       tvCity.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, tvCity.getText(), Toast.LENGTH_LONG).show();
               SortUtil.sortByCity(airports);
               createTable(airports, 0);
           }
       });

       tvAirport.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, tvAirport.getText(), Toast.LENGTH_LONG).show();
               SortUtil.sortByName(airports);
               createTable(airports, 0);
           }
       });

        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, tvCode.getText(), Toast.LENGTH_LONG).show();
                SortUtil.sortByCode(airports);
                createTable(airports, 0);
            }
        });

        tvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, tvCountry.getText(), Toast.LENGTH_LONG).show();
                SortUtil.sortByCountry(airports);
                createTable(airports, 0);
            }
        });
    }


    private void createTableRow(String serial_number, String city, String airport, String code, String country, int index) {
        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(lp);

        TextView textViewSN = new TextView(this);
        TextView textViewCity = new TextView(this);
        TextView textViewAirport = new TextView(this);
        TextView textViewCode = new TextView(this);
        TextView textViewCountry = new TextView(this);

        textViewSN.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0));
        textViewCity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.3f));
        textViewAirport.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1));
        textViewCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0));
        textViewCountry.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.3f));

        textViewSN.setGravity(Gravity.CENTER);
        textViewCity.setGravity(Gravity.CENTER);
        textViewAirport.setGravity(Gravity.CENTER);
        textViewCode.setGravity(Gravity.CENTER);
        textViewCountry.setGravity(Gravity.CENTER);

        textViewAirport.setMaxLines(3);
        textViewCity.setMaxLines(2);
        textViewCountry.setMaxLines(2);

        textViewSN.setPadding(5, 15, 5, 15);
        textViewCity.setPadding(5, 15, 5, 15);
        textViewAirport.setPadding(5, 15, 5, 15);
        textViewCode.setPadding(5, 15, 5, 15);
        textViewCountry.setPadding(5, 15, 5, 15);

        textViewSN.setText(serial_number);
        textViewCity.setText(city);
        textViewAirport.setText(airport);
        textViewCode.setText(code);
        textViewCountry.setText(country);

        textViewSN.setBackgroundResource(R.drawable.cell_shape_white);
        textViewCity.setBackgroundResource(R.drawable.cell_shape_grey);
        textViewAirport.setBackgroundResource(R.drawable.cell_shape_white);
        textViewCode.setBackgroundResource(R.drawable.cell_shape_grey);
        textViewCountry.setBackgroundResource(R.drawable.cell_shape_white);

        tableRow.addView(textViewSN);
        tableRow.addView(textViewCity);
        tableRow.addView(textViewAirport);
        tableRow.addView(textViewCode);
        tableRow.addView(textViewCountry);

        if (index == -1) {
            tableRowHeader = tableRow;
            textViewSN.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_small));
            textViewCity.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_small));
            textViewAirport.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_small));
            textViewCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_small));
            textViewCountry.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.font_size_small));
            textViewSN.setBackgroundResource(R.drawable.cell_shape_blue);
            textViewCity.setBackgroundResource(R.drawable.cell_shape_blue);
            textViewAirport.setBackgroundResource(R.drawable.cell_shape_blue);
            textViewCode.setBackgroundResource(R.drawable.cell_shape_blue);
            textViewCountry.setBackgroundResource(R.drawable.cell_shape_blue);
        }
        tableLayout.addView(tableRow, index + 1);
    }

    private void createTable(List<Airport> airports, int page) {
        tableLayout.removeAllViews();

        // header row
        createTableRow("S.N", "City", "Airport", "Code", "Country", -1);

        // data rows
        for (int i = 0, j = page * 50; j < airports.size() && i < 50; i++, j++) {
            createTableRow(
                    String.valueOf(i + 1),
                    airports.get(j).getCity(),
                    airports.get(j).getAirport(),
                    airports.get(j).getCode(),
                    airports.get(j).getCountry(),
                    i
            );
        }

    }
}