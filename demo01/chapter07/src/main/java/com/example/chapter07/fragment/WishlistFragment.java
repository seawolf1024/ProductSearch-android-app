package com.example.chapter07.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.ProductListActivity;
import com.example.chapter07.R;
import com.example.chapter07.adapter.RecyclerViewAdapter;
import com.example.chapter07.adapter.SimilarProductAdapter;
import com.example.chapter07.adapter.WishListAdapter;
import com.example.chapter07.entity.ProductCard;
import com.example.chapter07.entity.SimilarProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class WishlistFragment extends Fragment {
    private RequestQueue requestQueue;
    private ArrayList<ProductCard> productCardList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Init wishList
        getWishList(view);

        // Inflate the layout for this fragment
        return view;
    }

    public void onResume() {
        super.onResume();

        // Refresh the product list every time the fragment is resumed
        getWishList(getView());
        // total items
        TextView tv_total_price = getView().findViewById(R.id.tv_total_price); // no such function
        tv_total_price.setText("WishList Total("+ productCardList.size() +" items)                $" +
                getTotalPrice(productCardList));

        TextView tv_no_results = getView().findViewById(R.id.tv_no_results);
        if(productCardList.size() == 0){
            tv_no_results.setVisibility(View.VISIBLE);
            tv_total_price.setVisibility(View.GONE);
        }else{
            tv_no_results.setVisibility(View.GONE);
            tv_total_price.setVisibility(View.VISIBLE);
        }
    }


    private void getWishList(View view) {
        String url = "http://10.0.2.2:8080/initialize";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("--Response(getWishList): " + response.toString());

                        try {
                            parseWishlistCard(response.toString());
                            setViews(view);
                        } catch (JSONException e) { throw new RuntimeException(e); }

                        // render to recyclerView
                        // render products card
                        RecyclerView rv_products = view.findViewById(R.id.rv_products);
                        // adapter
                        WishListAdapter adapter = new WishListAdapter(view.getContext(), productCardList);  // Why it says there's an error?
                        rv_products.setAdapter(adapter);

                        // Layout
                        rv_products.setLayoutManager(new GridLayoutManager(requireContext(), 2));  // Specify the number of columns


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("--Error(getWishList): " + error.toString());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private String getTotalPrice(ArrayList<ProductCard>productCardList){
        double res = 0.0;
        for(int i = 0; i < productCardList.size(); i++){
            res += productCardList.get(i).getItemPrice();
        }

        return String.format("%.2f", res);
    }
    private void parseWishlistCard(String searchResponseStr) throws JSONException {
        JSONArray wishlistCardArray = new JSONArray(searchResponseStr);
        this.productCardList.clear();

        for(int i = 0; i < wishlistCardArray.length(); i++){
            String itemId = wishlistCardArray.getJSONObject(i).getString("itemId");
            String image = wishlistCardArray.getJSONObject(i).getString("image");
            float price = Float.parseFloat(wishlistCardArray.getJSONObject(i).getString("price"));
            String title = wishlistCardArray.getJSONObject(i).getString("title");
            String conditionId = wishlistCardArray.getJSONObject(i).getString("conditionId");
            float shippingCost = Float.parseFloat(wishlistCardArray.getJSONObject(i).getString("shippingCost"));
            String zipcode = wishlistCardArray.getJSONObject(i).getString("zipcode");
            ProductCard productCard = new ProductCard(itemId, image, title, price, zipcode,shippingCost, conditionId);
            this.productCardList.add(productCard);
        }
    }



    public void setViews(View view){
        // render products card
        RecyclerView rv_products = view.findViewById(R.id.rv_products);
        // adapter
        WishListAdapter adapter = new WishListAdapter(view.getContext(), this.productCardList);
        rv_products.setAdapter(adapter);
        // Layout
        rv_products.setLayoutManager(new GridLayoutManager(view.getContext(),2,
                GridLayoutManager.VERTICAL,false));

    }
}