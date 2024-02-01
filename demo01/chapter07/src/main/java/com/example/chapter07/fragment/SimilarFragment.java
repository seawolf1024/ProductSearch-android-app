package com.example.chapter07.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.R;
import com.example.chapter07.adapter.RecyclerViewAdapter;
import com.example.chapter07.adapter.SimilarProductAdapter;
import com.example.chapter07.entity.SimilarProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class SimilarFragment extends Fragment {
    private String itemId;
    private RequestQueue requestQueue;
    private JSONObject itemIdJSON = new JSONObject();

    // RecyclerView
    private ArrayList<SimilarProduct> similarProducts;
    private RecyclerView rv_similar_product;
    private SimilarProductAdapter adapter;

    // Spinner
    private Spinner sp_sortBy;
    private String sp_orderBy;
    private String sortBy = "Default";
    private String orderBy = "Ascending";

    public SimilarFragment(String itemId) {
        this.itemId = itemId;
        try {
            this.itemIdJSON.put("itemId", this.itemId);
        } catch (JSONException e) {throw new RuntimeException(e);}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set view
        View view = inflater.inflate(R.layout.fragment_similar, container, false);
        // set HTTP requestQueue
        this.requestQueue = Volley.newRequestQueue(requireContext());

        // set similarProducts
        this.similarProducts = new ArrayList<>();
        try {
            getSimilarProducts(view);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


//        // ** set recyclerView
//        // get recyclerView from xml
//        this.rv_similar_product = view.findViewById(R.id.rv_similar_product);
//        adapter = new SimilarProductAdapter(view.getContext(), this.similarProducts);
//        rv_similar_product.setLayoutManager(new LinearLayoutManager(requireContext()));
//        rv_similar_product.setAdapter(adapter);

        return view;
    }

    private void getSimilarProducts(View view) throws JSONException {
        String url = "http://10.0.2.2:8080/searchsimilar";
        System.out.println("--beforesendSimilar:"+this.itemIdJSON.getString("itemId"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, this.itemIdJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("--similar: " + response.toString());
                        try {
                            parseSimilarProducts(response.toString());
                            setViews(view);
                        } catch (JSONException e) {throw new RuntimeException(e);}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("--Error(Product): " + error.toString());
                    }
                });

        // Add the request to the RequestQueue
        this.requestQueue.add(jsonObjectRequest);
    }

    private void parseSimilarProducts(String productDetailStr) throws JSONException {
        JSONObject jsonResponse = new JSONObject(productDetailStr);
        // image
        JSONArray items = jsonResponse.getJSONObject("getSimilarItemsResponse")
                .getJSONObject("itemRecommendations")
                .getJSONArray("item");
        for(int i = 0; i < items.length(); i++){
            String imageUrl = items.getJSONObject(i).getString("imageURL");
            String title = items.getJSONObject(i).getString("title");
            double shippingCost = Float.parseFloat(items.getJSONObject(i)
                    .getJSONObject("shippingCost")
                    .getString("__value__"));
            int daysLeft = Integer.parseInt(getDaysLeft(items.getJSONObject(i).
                    getString("timeLeft")));
            float price = Float.parseFloat(items.getJSONObject(i).getJSONObject("buyItNowPrice")
                    .getString("__value__"));

            SimilarProduct similarProduct = new SimilarProduct(imageUrl, title, shippingCost, daysLeft, price);
            System.out.println("--simiproduct"+similarProduct.toString());
            similarProducts.add(similarProduct);
        }
    }

    private String getDaysLeft(String s){ //"P23DT11H33M39S"
        int left = s.indexOf('P');
        int right = s.indexOf('D');
        return s.substring(left + 1, right);
    }

    public void setViews(View view){
        // Progress Bar
        ProgressBar pb_progressbar = view.findViewById(R.id.pb_progressbar_4);
        pb_progressbar.setVisibility(View.GONE);
        TextView tv_loading_text = view.findViewById(R.id.tv_loading_text_4);
        tv_loading_text.setVisibility(View.GONE);


        // set RecyclerView for similarProducts
        rv_similar_product = view.findViewById(R.id.rv_similar_product);
        adapter = new SimilarProductAdapter(view.getContext(), this.similarProducts);
        rv_similar_product.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_similar_product.setAdapter(adapter);

        // set Spinners
        Spinner spSortBy = view.findViewById(R.id.sp_sortBy);
        Spinner spOrderBy = view.findViewById(R.id.sp_orderBy);

        // Set Spinner item selected listeners
        spSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update sortBy variable based on selected item
                sortBy = parentView.getSelectedItem().toString();
                updateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        spOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update orderBy variable based on selected item
                orderBy = parentView.getSelectedItem().toString();
                updateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

    }

    private void updateRecyclerView() {
        // Implement sorting and ordering logic here
        Collections.sort(similarProducts, new Comparator<SimilarProduct>() {
            @Override
            public int compare(SimilarProduct product1, SimilarProduct product2) {
                // Implement sorting logic based on sortBy variable
                switch (sortBy) {
                    case "Name":
                        return product1.getTitle().compareTo(product2.getTitle());
                    case "Price":
                        return Float.compare(product1.getPrice(), product2.getPrice());
                    case "DaysLeft":
                        return Integer.compare(product1.getDaysLeft(), product2.getDaysLeft());
                    default:
                        return 0; // Default sorting
                }
            }
        });

        // Reverse the order if orderBy is "desc"
        if (orderBy.equals("Descending")) {
            Collections.reverse(similarProducts);
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}