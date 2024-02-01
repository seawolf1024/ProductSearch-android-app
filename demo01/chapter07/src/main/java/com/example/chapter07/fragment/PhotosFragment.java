package com.example.chapter07.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private String itemTitle;
    private JSONObject titleJSON = new JSONObject();
    private RequestQueue requestQueue;
    private ArrayList<String> photoLinkList = new ArrayList<>();
    public PhotosFragment(String itemTitle) {
        // Required empty public constructor
        this.itemTitle = itemTitle;
        try {
            this.titleJSON.put("title", this.itemTitle);
        } catch (JSONException e) {throw new RuntimeException(e);}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        this.requestQueue = Volley.newRequestQueue(requireContext());

        getPhotos(view);
        return view;
    }

    public void getPhotos(View view){

        String url = "http://10.0.2.2:8080/searchphotos";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, this.titleJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        // Print or process the response as needed
                        System.out.println("--Response(Product): " + response.toString());
                        try {
                            System.out.println("--photos=" + response.toString());
                            parsePhotosUrl(response.toString());
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


    private void parsePhotosUrl(String response) throws JSONException {

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray photosArray = jsonResponse.getJSONArray("items");
        for(int i = 0; i < photosArray.length(); i++){
            String photoLink = photosArray.getJSONObject(i).getString("link");
            this.photoLinkList.add(photoLink);
        }
        System.out.println("--this.photoLinkList="+this.photoLinkList);
    }

    public void setViews(View view){
        // Progress Bar
        ProgressBar pb_progressbar = view.findViewById(R.id.pb_progressbar_3);
        pb_progressbar.setVisibility(View.GONE);
        TextView tv_loading_text = view.findViewById(R.id.tv_loading_text_3);
        tv_loading_text.setVisibility(View.GONE);
//        LinearLayout ll_layout = view.findViewById(R.id.ll_layout_3);
//        ll_layout.setVisibility(View.VISIBLE);


        ImageView iv_1 = view.findViewById(R.id.iv_1);
        ImageView iv_2 = view.findViewById(R.id.iv_2);
        ImageView iv_3 = view.findViewById(R.id.iv_3);
        ImageView iv_4 = view.findViewById(R.id.iv_4);
        ImageView iv_5 = view.findViewById(R.id.iv_5);
        ImageView iv_6 = view.findViewById(R.id.iv_6);
        ImageView iv_7 = view.findViewById(R.id.iv_7);
        ImageView iv_8 = view.findViewById(R.id.iv_8);


        if (photoLinkList.size() > 0) {
            String imageUrl = photoLinkList.get(0);
            Picasso.get().load(imageUrl).into(iv_1);
        }
        if (photoLinkList.size() > 1) {
            String imageUrl = photoLinkList.get(1);
            Picasso.get().load(imageUrl).into(iv_2);
        }
        if (photoLinkList.size() > 2) {
            String imageUrl = photoLinkList.get(2);
            Picasso.get().load(imageUrl).into(iv_3);
        }
        if (photoLinkList.size() > 4) {
            String imageUrl = photoLinkList.get(4);
            Picasso.get().load(imageUrl).into(iv_5);
        }
        if (photoLinkList.size() > 5) {
            String imageUrl = photoLinkList.get(5);
            Picasso.get().load(imageUrl).into(iv_6);
        }
        if (photoLinkList.size() > 6) {
            String imageUrl = photoLinkList.get(6);
            Picasso.get().load(imageUrl).into(iv_7);
        }
        if (photoLinkList.size() > 7) {
            String imageUrl = photoLinkList.get(7);
            Picasso.get().load(imageUrl).into(iv_8);
        }

    }

}