package com.example.chapter07.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.ProductListActivity;
import com.example.chapter07.R;
import com.example.chapter07.adapter.PicturesPagerAdapter;
import com.example.chapter07.entity.ProductCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductFragment extends Fragment {
    private String itemId;
    private JSONObject itemIdJSON = new JSONObject();

    private RequestQueue requestQueue;
    private ArrayList<String> productPictureUrls;
    private String productTitle;
    private double productPrice;
    private double productShippingPrice;
    private String productBrand;
    private Map<String, String> specificMap;



    public ProductFragment(String itemId, double shippingCost) {
        // Required empty public constructor
        this.itemId = itemId;
        try {
            this.itemIdJSON.put("itemId", this.itemId);
        } catch (JSONException e) {throw new RuntimeException(e);}

        this.productShippingPrice = shippingCost;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        this.requestQueue = Volley.newRequestQueue(requireContext());

        //construct JSON = {"itemId": "123456"}

        try {
            getProductDetail(view);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void getProductDetail(View view) throws JSONException {
        String url = "http://10.0.2.2:8080/productinfo";
        System.out.println("--beforesendProductInfo:"+this.itemIdJSON.getString("itemId"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, this.itemIdJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        // Print or process the response as needed
                        System.out.println("--Response(Product): " + response.toString());
                        try {
                            parseProductDetail(response.toString());
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

    private void parseProductDetail(String productDetailStr) throws JSONException {
        JSONObject jsonResponse = new JSONObject(productDetailStr);

        // parse all the variables from JSON
        // pictureURLs
        JSONArray pictureUrlJSONArray = jsonResponse.getJSONObject("Item")
                    .getJSONArray("PictureURL");
        this.productPictureUrls = new ArrayList<String>();
        for(int i = 0; i < pictureUrlJSONArray.length(); i++){
            productPictureUrls.add(pictureUrlJSONArray.getString(i));
        }

        // title
        this.productTitle = jsonResponse.getJSONObject("Item").getString("Title");
        System.out.println("--title=" + productTitle);

        // currentPrice
        this.productPrice = jsonResponse.getJSONObject("Item").
                getJSONObject("CurrentPrice").getDouble("Value");
        System.out.println("--price="+productPrice);

        // shippingPrice
        // already got

        // brand & specifics
        this.productBrand = "Not Specified";
        JSONArray nameValueArray = jsonResponse.getJSONObject("Item").getJSONObject("ItemSpecifics")
                .getJSONArray("NameValueList");
        this.specificMap = new HashMap<>();
        for(int i = 0; i < nameValueArray.length(); i++){
            JSONObject nvElement = nameValueArray.getJSONObject(i);
            String name = nvElement.getString("Name");
            String value = nvElement.getJSONArray("Value").getString(0);
            System.out.println("--specific name="+name);
            this.specificMap.put(name, value);
            if(name.toLowerCase().equals("brand")){
                this.productBrand = value;
            }
        }
    }

    public void setViews(View view){
        // Progress Bar
        ProgressBar pb_progressbar = view.findViewById(R.id.pb_progressbar);
        pb_progressbar.setVisibility(View.GONE);
        TextView tv_loading_text = view.findViewById(R.id.tv_loading_text);
        tv_loading_text.setVisibility(View.GONE);




        // ViewPager
        ViewPager vp_pictures = view.findViewById(R.id.vp_pictures);
        PicturesPagerAdapter adapter = new PicturesPagerAdapter(view.getContext(), this.productPictureUrls, vp_pictures);
        vp_pictures.setAdapter(adapter);
        adapter.startAutoScroll(); // Start auto-scrolling
        vp_pictures.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Other elements
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_price = view.findViewById(R.id.tv_price);
//        TextView tv_shipping_cost = view.findViewById(R.id.tv_shipping_cost);
        TextView tv_brand = view.findViewById(R.id.tv_brand);
        TextView tv_specifics = view.findViewById(R.id.tv_specifics);
        TextView tv_price_shippingcost = view.findViewById(R.id.tv_price_shippingcost);


        tv_title.setText(this.productTitle);

//        tv_shipping_cost.setText(String.valueOf(this.productShippingPrice));

        if(this.productShippingPrice == 0.0){
            tv_price_shippingcost.setText("$"+this.productPrice+" with " + "Free Shipping");
        }else{
            String shippingCostStr = String.format("%.2f", this.productShippingPrice);
            tv_price_shippingcost.setText("$"+this.productPrice+" with " +  shippingCostStr
                    +" Shipping");
        }

        // Highlights
        tv_price.setText("Price     $"+String.valueOf(this.productPrice));
        tv_brand.setText("Brand     " + this.productBrand);


        String specificsStr = "";
        for (Map.Entry<String,String> mapElement : this.specificMap.entrySet()) {
            String key = mapElement.getKey();
            String value = capitalizeFirstLetter(mapElement.getValue());
            specificsStr += "·" + value + "\n";
        }
        tv_specifics.setText(specificsStr);
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return the input unchanged if it's null or empty
        }

        // Convert the first character to uppercase and concatenate the rest of the string
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }


}