package com.example.chapter07.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chapter07.ProductListActivity;
import com.example.chapter07.R;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }
    private JSONObject searchInfo = new JSONObject();
    private RequestQueue requestQueue;

    String keyword = "";
    String category = "0";
    boolean condition_new = false;
    boolean condition_used = false;
    boolean condition_unspecified = false;
    boolean shipping_local_pickup = false;
    boolean shipping_free_shipping = false;
    float distance = 1000;
    String from = "currLocation";
    String zipcode = "90007";


    Spinner sp_category;
    CheckBox cb_new;
    CheckBox cb_used;
    CheckBox cb_unspecified;
    CheckBox cb_local_pickup;
    CheckBox cb_free_shipping;
    EditText et_distance;
    RadioGroup rg_location;
    EditText et_zipcode;
    RadioButton rb_currlocation;
    RadioButton rb_zipcode;
    CheckBox cb_enable_nearby_search;
    LinearLayout ll_location;
    Button btn_search;
    Button btn_clear;
    TextView tv_keyword_validation;
    TextView tv_zipcode_validation;


    private void getSearchInfoJSON(){
        try {
            this.searchInfo.put("keyword", this.keyword);
            this.searchInfo.put("category", this.category);

            JSONObject condition = new JSONObject();
            condition.put("new", this.condition_new);
            condition.put("used", this.condition_used);
            condition.put("unspecified", this.condition_unspecified);
            this.searchInfo.put("condition", condition);

            JSONObject shipping = new JSONObject();
            shipping.put("localPickUp", this.shipping_local_pickup);
            shipping.put("freeShipping", this.shipping_free_shipping);
            this.searchInfo.put("shipping", shipping);


            this.searchInfo.put("distance", this.distance);
            this.searchInfo.put("from", this.from);
            this.searchInfo.put("zipcode", this.zipcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Init searchInfo
        getSearchInfoJSON();

        // Keywords
        EditText et_keyword = view.findViewById(R.id.et_keyword);
        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(getContext(), "Keyword: " + keyword, Toast.LENGTH_SHORT).show();
                SearchFragment.this.keyword = s.toString();
            }
        });



        // Category
        sp_category = view.findViewById(R.id.sp_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( requireContext(), R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Set categoryId based on the selected item
                SearchFragment.this.category = Integer.toString(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });



        // Condition
        cb_new = view.findViewById(R.id.cb_new);
        cb_used = view.findViewById(R.id.cb_used);
        cb_unspecified = view.findViewById(R.id.cb_unspecified);

        cb_new.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SearchFragment.this.condition_new = isChecked;
        });

        cb_used.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SearchFragment.this.condition_used = isChecked;
        });

        cb_unspecified.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SearchFragment.this.condition_unspecified = isChecked;
        });



        // Shipping Options
        cb_local_pickup = view.findViewById(R.id.cb_local_pickup);
        cb_free_shipping = view.findViewById(R.id.cb_free_shipping);
        cb_local_pickup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SearchFragment.this.shipping_local_pickup = isChecked;
        });

        cb_free_shipping.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SearchFragment.this.shipping_free_shipping = isChecked;
        });





        // distance
        et_distance = view.findViewById(R.id.et_distance);
        et_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().replaceAll("^\\s+|\\s+$", "").length() == 0){
                    SearchFragment.this.distance = 0;
                }else{
                    try {
                        SearchFragment.this.distance = Float.parseFloat(s.toString());
                    }catch (NumberFormatException e){

                    }
                }
            }
        });


        rg_location = view.findViewById(R.id.rg_location);
        et_zipcode = view.findViewById(R.id.et_zipcode);
        rb_currlocation = view.findViewById(R.id.rb_currlocation);
        rb_zipcode = view.findViewById(R.id.rb_zipcode);
        cb_enable_nearby_search = view.findViewById(R.id.cb_enable_nearby_search);
        ll_location = view.findViewById(R.id.ll_location);

        // cb_enable_nearby_search
        cb_enable_nearby_search.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked == true){
                SearchFragment.this.zipcode = "90007";
                ll_location.setVisibility(View.VISIBLE);
                rb_currlocation.setChecked(true);
            }else if(isChecked == false){
                this.zipcode = "90007";
                ll_location.setVisibility(View.GONE);
//                rb_currlocation.setChecked(false);
//                rb_zipcode.setChecked(false);
            }
        });

        // Use: current Location
        rg_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == R.id.rb_currlocation) {
                    // If rb_currlocation is selected, disable the EditText
                    SearchFragment.this.zipcode = "90007";
                    et_zipcode.setEnabled(false);
                } else if (checkedId == R.id.rb_zipcode) {
                    SearchFragment.this.zipcode = "";
                    // If rb_zipcode is selected, enable the EditText
                    et_zipcode.setEnabled(true);
                }
            }
        });

        // Use: zipcode
        et_zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                SearchFragment.this.zipcode = s.toString();
            }
        });

        // Search
        btn_search = view.findViewById(R.id.btn_search);
        btn_clear = view.findViewById(R.id.btn_clear);
        tv_keyword_validation = view.findViewById(R.id.tv_keyword_validation);
        tv_zipcode_validation = view.findViewById(R.id.tv_zipcode_validation);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validation
                String strippedKeyword = SearchFragment.this.keyword.replaceAll("^\\s+|\\s+$", "");
                String strippedZipcode = SearchFragment.this.zipcode.replaceAll("^\\s+|\\s+$", "");

                System.out.println("--keyword="+strippedKeyword+",zipcode="+strippedZipcode);
                if(strippedKeyword.length() == 0){
                    tv_keyword_validation.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                }else{
                    tv_keyword_validation.setVisibility(View.GONE);
                }
                if(strippedZipcode.length() == 0){
                    tv_zipcode_validation.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                }else{
                    tv_zipcode_validation.setVisibility(View.GONE);
                }

                if(strippedKeyword.length() > 0 && strippedZipcode.length() > 0){
                    tv_keyword_validation.setVisibility(View.GONE);
                    tv_zipcode_validation.setVisibility(View.GONE);
                    // construct searchInfo JSON
                    getSearchInfoJSON();
                    String searchInfoStr = SearchFragment.this.searchInfo.toString();
                    System.out.println("--searchInfoString = " + searchInfoStr);

                    // do the http search
                    switchToSearchResult();
                }

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_keyword.setText("");
                sp_category.setSelection(0);
                cb_new.setChecked(false);
                cb_used.setChecked(false);
                cb_unspecified.setChecked(false);
                cb_local_pickup.setChecked(false);
                cb_free_shipping.setChecked(false);
                cb_enable_nearby_search.setChecked(false);
                et_distance.setText("");
                et_zipcode.setText("");

                SearchFragment.this.keyword = "";
                SearchFragment.this.category = "0";
                SearchFragment.this.condition_new = false;
                SearchFragment.this.condition_used = false;
                SearchFragment.this.condition_unspecified = false;
                SearchFragment.this.shipping_local_pickup = false;
                SearchFragment.this.shipping_free_shipping = false;
                SearchFragment.this.distance = 10;
                SearchFragment.this.from = "currLocation";
                SearchFragment.this.zipcode = "90007";
            }
        });

        return view;
    }


    private void switchToSearchResult() {
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        // If you need to pass data to the new activity, you can use intent.putExtra()
        intent.putExtra("searchInfoJSONStr", this.searchInfo.toString());
        if(this.keyword.startsWith("asd")){
            intent.putExtra("showNoResultStrFlag", "NORESULT");
        }else{
            intent.putExtra("showNoResultStrFlag", "SHOWRESULT");
        }
        startActivity(intent);
    }


}
