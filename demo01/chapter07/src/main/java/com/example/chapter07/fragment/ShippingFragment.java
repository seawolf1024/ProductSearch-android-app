package com.example.chapter07.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShippingFragment extends Fragment {
    private String itemId;

    // shipping info
    private String shippingInfoJSONArrayStr;
    private String storeName;
    private String storeUrl;
    private int feedbackScore;
    private float popularity;
    private String feedbackStar;
    private String itemShippingCost;
    private String globalShipping;
    private String handlingTime;
    private String conditionDescription;

    // return policy
    private String policy;
    private String returnsWithin;
    private String refundMode;
    private String shippedBy;

    // network
    private JSONObject itemIdJSON = new JSONObject();
    private RequestQueue requestQueue;
    public ShippingFragment(String itemId, String shippingInfoJSONArrayStr) {
        // Required empty public constructor
        this.itemId = itemId;
        this.shippingInfoJSONArrayStr = shippingInfoJSONArrayStr;
        System.out.println("--shippingInfo="+this.shippingInfoJSONArrayStr);
        try {
            this.itemIdJSON.put("itemId", this.itemId);
        } catch (JSONException e) {throw new RuntimeException(e);}
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        this.requestQueue = Volley.newRequestQueue(requireContext());

        getReturnPolicy(view);

        return view;
    }

    public void getReturnPolicy(View view){
        String url = "http://10.0.2.2:8080/productinfo";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, this.itemIdJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        // Print or process the response as needed
                        System.out.println("--Response(Product): " + response.toString());
                        try {
                            parseReturnPolicy(response.toString());
                            parseShippingInfo(shippingInfoJSONArrayStr);
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


    private void parseReturnPolicy(String productDetailStr) throws JSONException {
        JSONObject jsonResponse = new JSONObject(productDetailStr);

        // parse all the variables from JSON
        this.refundMode = jsonResponse.getJSONObject("Item")
                .getJSONObject("ReturnPolicy").getString("Refund");
        this.policy = jsonResponse.getJSONObject("Item")
                .getJSONObject("ReturnPolicy").getString("ReturnsAccepted");
        this.returnsWithin = jsonResponse.getJSONObject("Item")
                .getJSONObject("ReturnPolicy").getString("ReturnsWithin");
        this.shippedBy = jsonResponse.getJSONObject("Item")
                .getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy");
//        System.out.println("-- refundMode =" + refundMode);
//        System.out.println("-- policy =" + policy);
//        System.out.println("-- returnsWithin =" + returnsWithin);
//        System.out.println("-- shippedBy =" + shippedBy);
    }

    private void parseShippingInfo(String shippingInfoJSONArrayStr) throws JSONException {
        JSONArray shippingInfoJSONArray = new JSONArray(shippingInfoJSONArrayStr);
        for(int i = 0; i < shippingInfoJSONArray.length(); i++){
            String currItemId = shippingInfoJSONArray.getJSONObject(i).getString("itemId");
//            System.out.println("--currentItemId = " + currItemId + ", this.itemId =" + this.itemId);
            if(currItemId.equals(this.itemId)){
                this.storeName = shippingInfoJSONArray.getJSONObject(i).getString("storeName");
                this.storeUrl = shippingInfoJSONArray.getJSONObject(i).getString("storeUrl");
                this.feedbackScore = Integer.parseInt(shippingInfoJSONArray.getJSONObject(i).getString("feedbackScore"));
                this.popularity = Float.parseFloat(shippingInfoJSONArray.getJSONObject(i).getString("popularity"));
                this.feedbackStar = shippingInfoJSONArray.getJSONObject(i).getString("feedbackStar");
                this.itemShippingCost = shippingInfoJSONArray.getJSONObject(i).getString("itemShippingCost");
                this.globalShipping = shippingInfoJSONArray.getJSONObject(i).getString("globalShipping");
                this.handlingTime = shippingInfoJSONArray.getJSONObject(i).getString("handlingTime");
                this.conditionDescription = shippingInfoJSONArray.getJSONObject(i).getString("conditionDescription");
                break;
            }
        }
    }
    public void setViews(View view){
        // Progress Bar
        ProgressBar pb_progressbar = view.findViewById(R.id.pb_progressbar);
        pb_progressbar.setVisibility(View.GONE);
        TextView tv_loading_text = view.findViewById(R.id.tv_loading_text);
        tv_loading_text.setVisibility(View.GONE);
        LinearLayout ll_layout = view.findViewById(R.id.ll_layout);
        ll_layout.setVisibility(View.VISIBLE);


        TextView tv_storename = view.findViewById(R.id.tv_storename);
        TextView tv_store_link = view.findViewById(R.id.tv_store_link);

//        Click storename to redirect to the store
//        tv_storename.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse(storeUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });



        TextView tv_feedback_score = view.findViewById(R.id.tv_feedback_score);
        TextView tv_popularity = view.findViewById(R.id.tv_popularity);
//        TextView tv_feedback_star = view.findViewById(R.id.tv_feedback_star);//5
        ImageView iv_feedback_star = view.findViewById(R.id.iv_feedback_star);
        TextView tv_shipping_cost = view.findViewById(R.id.tv_shipping_cost);
        TextView tv_global_shipping = view.findViewById(R.id.tv_global_shipping);
        TextView tv_handling_time = view.findViewById(R.id.tv_handling_time);
        TextView tv_policy = view.findViewById(R.id.tv_policy);
        TextView tv_returns_within = view.findViewById(R.id.tv_returns_within);//10
        TextView tv_refund_mode = view.findViewById(R.id.tv_refund_mode);
        TextView tv_shipped_by = view.findViewById(R.id.tv_shipped_by);


        tv_storename.setText(this.storeName);
        tv_store_link.setText(this.storeUrl);
        tv_feedback_score.setText(String.valueOf(this.feedbackScore));
        tv_popularity.setText(String.valueOf(this.popularity));

        if(this.feedbackScore < 10){
            // none
            iv_feedback_star.setImageResource(R.drawable.star_circle);
        }else if(this.feedbackScore < 50) {
            // yellow
            iv_feedback_star.setImageResource(R.drawable.star_circle_yellow);
        }else if(this.feedbackScore < 99){
            //blue
            iv_feedback_star.setImageResource(R.drawable.star_circle_blue);
        }else if(this.feedbackScore < 499){
            // Turquoise
            iv_feedback_star.setImageResource(R.drawable.star_circle_turquoise);
        }else if(this.feedbackScore < 999){
            // purple
            iv_feedback_star.setImageResource(R.drawable.star_circle_purple);
        }else if(this.feedbackScore < 4999){
            // red
            iv_feedback_star.setImageResource(R.drawable.star_circle_red);
        }else if(this.feedbackScore < 9999){
            // green
            iv_feedback_star.setImageResource(R.drawable.star_circle_green);
        }else if(this.feedbackScore < 24999){
            // yellow sh
            iv_feedback_star.setImageResource(R.drawable.star_circle_outline_yellow);
        }else if(this.feedbackScore < 49999){
            // turquoise sh
            iv_feedback_star.setImageResource(R.drawable.star_circle_outline_turquoise);
        }else if(this.feedbackScore < 99999){
            // purple sh
            iv_feedback_star.setImageResource(R.drawable.star_circle_outline_purple);
        }else if(this.feedbackScore < 499999){
            // red sh
            iv_feedback_star.setImageResource(R.drawable.star_circle_outline_red);
        }else{
            // green sh
            iv_feedback_star.setImageResource(R.drawable.star_circle_outline_turquoise);
        }

//        tv_feedback_star.setText(this.feedbackStar);//5
        tv_shipping_cost.setText(this.itemShippingCost);
        tv_global_shipping.setText(this.globalShipping);
        tv_handling_time.setText(this.handlingTime);
        tv_policy.setText(this.policy);
        tv_returns_within.setText(this.returnsWithin);//10
        tv_refund_mode.setText(this.refundMode);
        tv_shipped_by.setText(this.shippedBy);
    }

}