package com.example.chapter07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.chapter07.adapter.RecyclerViewAdapter;
import com.example.chapter07.entity.ProductCard;
import com.example.chapter07.fragment.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    ArrayList<ProductCard> productList = new ArrayList<>();
    String shippingInfoJSONArrayStr;
    private RequestQueue requestQueue;
    JSONObject searchInfo;

    private String showNoResultStrFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        requestQueue = Volley.newRequestQueue(this);

        // get searchInfoJSON
        String searchInfoJSONStr = getIntent().getExtras().getString("searchInfoJSONStr");
        this.showNoResultStrFlag = getIntent().getExtras().getString("showNoResultStrFlag");

        try {
            this.searchInfo = new JSONObject(searchInfoJSONStr);
            searchProducts();
        } catch (JSONException e) { throw new RuntimeException(e); }

    }

    private void searchProducts(){
        String url = "http://10.0.2.2:8080/search";
        System.out.println("beforesend:"+this.searchInfo.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, this.searchInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        // Print or process the response as needed
                        System.out.println("--Response: " + response.toString());
                        System.out.println("--length1 = " + response.toString().length());
                        try {
                            parseProductCard(response.toString());
                            parseShippingInfo(response.toString());

                        } catch (JSONException e) {
                            System.out.println("--Parsing productCard or shippingInfo error!");
                            throw new RuntimeException(e); }
                        setViews();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        // Print or process the error as needed
                        System.out.println("--Error: " + error.toString());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void parseProductCard(String searchResponseStr) throws JSONException {
        productList = new ArrayList<>();
        // Parse the JSON string
        JSONObject jsonResponse = new JSONObject(searchResponseStr);

        JSONArray items = jsonResponse.getJSONArray("findItemsAdvancedResponse")
                .getJSONObject(0).getJSONArray("searchResult").getJSONObject(0)
                .getJSONArray("item");
        System.out.println("--items=" + items);

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);

            // parse all the variables from JSON
            String itemId = item.getJSONArray("itemId").getString(0);
            String itemImage = item.getJSONArray("galleryURL").getString(0);
            String itemTitle = item.getJSONArray("title").getString(0);
            float itemPrice = Float.parseFloat(item.getJSONArray("sellingStatus").getJSONObject(0)
                    .getJSONArray("currentPrice").getJSONObject(0)
                    .getString("__value__"));
            String itemZipcode = item.getJSONArray("postalCode").getString(0);
            float itemShippingCost = Float.parseFloat(item.getJSONArray("shippingInfo").getJSONObject(0)
                            .getJSONArray("shippingServiceCost").getJSONObject(0)
                            .getString("__value__"));

            String conditionId = null;
            try {
                conditionId = item.getJSONArray("condition").getJSONObject(0).
                        getJSONArray("conditionDisplayName").getString(0);
            }catch (JSONException e){System.out.println("--condition parse error");}


            // set value to a class
            ProductCard productCard = new ProductCard(itemId, itemImage, itemTitle, itemPrice,
                    itemZipcode, itemShippingCost, conditionId);
            productList.add(productCard);
        }
    }

    private void parseShippingInfo(String searchResponseStr) throws JSONException {
        System.out.println("--FLAG="+showNoResultStrFlag);
        TextView tv_no_results = findViewById(R.id.tv_no_results);

        tv_no_results.setVisibility(View.GONE);
        if(this.showNoResultStrFlag.equals("NORESULT")){

            tv_no_results.setVisibility(View.VISIBLE);
            return;
        }

        // Parse the JSON string
        JSONObject jsonResponse = new JSONObject(searchResponseStr);

        JSONArray items = jsonResponse.getJSONArray("findItemsAdvancedResponse")
                .getJSONObject(0).getJSONArray("searchResult").getJSONObject(0)
                .getJSONArray("item");
        System.out.println("--items=" + items);

        JSONArray shippingInfoJSONArray = new JSONArray();
        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);

            // ** itemId
            String itemId = item.getJSONArray("itemId").getString(0);

            // *** Sold by
            // store name
            String storeName = "Unspecified";
            try {
                storeName = item.getJSONArray("storeInfo").getJSONObject(0)
                        .getJSONArray("storeName").getString(0);
            }catch (JSONException e){System.out.println("--storeName parse error");}


            String storeUrl = "http://stores.ebay.com/signaturelab";
            try {
                storeUrl = item.getJSONArray("storeInfo").getJSONObject(0)
                    .getJSONArray("storeURL").getString(0);
            }catch (JSONException e){System.out.println("--storeName parse error");}

            // feedback score
            String feedbackScore = item.getJSONArray("sellerInfo")
                    .getJSONObject(0).getJSONArray("feedbackScore")
                    .getString(0);
            // popularity
            String popularity = item.getJSONArray("sellerInfo")
                    .getJSONObject(0).getJSONArray("positiveFeedbackPercent")
                    .getString(0);
            // feedback star
            String feedbackStar = item.getJSONArray("sellerInfo")
                    .getJSONObject(0).getJSONArray("feedbackRatingStar")
                    .getString(0);

            // ** Shipping info
            // Shipping Cost
            float itemShippingCost = Float.parseFloat(item.getJSONArray("shippingInfo").getJSONObject(0)
                    .getJSONArray("shippingServiceCost").getJSONObject(0)
                    .getString("__value__"));
            // Global Shipping
            String globalShipping = item.getJSONArray("shippingInfo")
                    .getJSONObject(0).getJSONArray("shipToLocations")
                    .getString(0) == "Worldwide" ? "Yes" : "No";
            // Handling time
            int handlingTimeInt = Integer.parseInt(item.getJSONArray("shippingInfo").getJSONObject(0)
                    .getJSONArray("handlingTime").getString(0));
            String handlingTime = String.valueOf(handlingTimeInt) + (handlingTimeInt == 0 ? " day":" days");

            // Condition
            String conditionDescription = "Unspecified";
            try {
                conditionDescription = item.getJSONArray("condition").getJSONObject(0).
                        getJSONArray("conditionDisplayName").getString(0);
            }catch (JSONException e){System.out.println("--condition parse error");}

//            System.out.println("-- storeName =" + storeName);
//            System.out.println("-- storeUrl =" + storeUrl);
//            System.out.println("-- feedbackScore =" + feedbackScore);
//            System.out.println("-- popularity =" + popularity);
//            System.out.println("-- feedbackStar =" + feedbackStar);
//            System.out.println("-- itemShippingCost =" + itemShippingCost);
//            System.out.println("-- globalShipping =" + globalShipping);
//            System.out.println("-- handlingTime =" + handlingTime);
//            System.out.println("-- conditionDescription =" + conditionDescription);

            JSONObject singleShippingInfoJSON = new JSONObject();
            singleShippingInfoJSON.put("itemId", itemId);
            singleShippingInfoJSON.put("storeName", storeName);
            singleShippingInfoJSON.put("storeUrl", storeUrl);
            singleShippingInfoJSON.put("feedbackScore", feedbackScore);
            singleShippingInfoJSON.put("popularity", popularity);
            singleShippingInfoJSON.put("feedbackStar", feedbackStar);
            singleShippingInfoJSON.put("itemShippingCost", itemShippingCost);
            singleShippingInfoJSON.put("globalShipping", globalShipping);
            singleShippingInfoJSON.put("handlingTime", handlingTime);
            singleShippingInfoJSON.put("conditionDescription", conditionDescription);

            shippingInfoJSONArray.put(singleShippingInfoJSON);
        }
        this.shippingInfoJSONArrayStr = shippingInfoJSONArray.toString();
    }


    private void setViews(){


        System.out.println("--setViews, productList = "+productList);
        //title bar
        ImageButton ib_return = findViewById(R.id.ib_return);
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // render products card
        RecyclerView rv_products = findViewById(R.id.rv_products);
        // adapter
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, productList, this.shippingInfoJSONArrayStr);
        rv_products.setAdapter(adapter);
        // Layout
//        rv_products.setLayoutManager(new LinearLayoutManager(ProductListActivity.this,
//                LinearLayoutManager.VERTICAL, false));
        rv_products.setLayoutManager(new GridLayoutManager(ProductListActivity.this,2,
                GridLayoutManager.VERTICAL,false));



        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        TextView loadingText = findViewById(R.id.loadingText);
        loadingText.setVisibility(View.GONE);
    }

}


