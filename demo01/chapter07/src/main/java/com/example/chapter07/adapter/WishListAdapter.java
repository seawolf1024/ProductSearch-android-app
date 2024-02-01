package com.example.chapter07.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chapter07.ProductDetailActivity;
import com.example.chapter07.ProductListActivity;
import com.example.chapter07.R;
import com.example.chapter07.entity.ProductCard;
import com.example.chapter07.fragment.WishlistFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductCard> productList;

    // http request
    private RequestQueue requestQueue;



    public WishListAdapter(Context context, ArrayList<ProductCard> productList){
        this.context = context;
        this.productList = productList;
        requestQueue = Volley.newRequestQueue(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_recyclerview,null);
        return new ViewHolder(itemView);
    }

    // bind data: this class --> holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = this.productList.get(position).getItemId();
        String productTitle = this.productList.get(position).getItemTitle();
        String productZipcode = this.productList.get(position).getItemZipcode();
        float productShippingCost = this.productList.get(position).getItemShippingCost();
        String productCondition = this.productList.get(position).getConditionId();
        float productPrice = this.productList.get(position).getItemPrice();
        String productImage = this.productList.get(position).getItemImage();

        holder.tv_product_title.setText(productTitle);
//        holder.tv_product_zipcode.setText(productZipcode);
        holder.tv_product_zipcode.setText("Zip: " + (productZipcode.length() == 0? "N/A": productZipcode));

        holder.tv_product_shipping_cost.setText(productShippingCost == 0? "Free":"$"+String.valueOf(productShippingCost));
        holder.tv_product_condition.setText(productCondition);
        holder.tv_product_price.setText(String.valueOf(productPrice));
        // Use Picasso to load and display the image
        Picasso.get().load(productImage).into(holder.iv_product_image);



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // holder, get elements from XML
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_product_image;
        private TextView tv_product_title;
        private TextView tv_product_zipcode;
        private TextView tv_product_shipping_cost;
        private TextView tv_product_condition;
        private  TextView tv_product_price;
        private ImageButton ib_wishlist;
        private LinearLayout ll_product_card;
        public ViewHolder(View itemView){
            super(itemView);
            ll_product_card = itemView.findViewById(R.id.ll_product_card);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_title = itemView.findViewById(R.id.tv_product_title);
            tv_product_zipcode = itemView.findViewById(R.id.tv_product_zipcode);
            tv_product_shipping_cost = itemView.findViewById(R.id.tv_product_shipping_cost);
            tv_product_condition = itemView.findViewById(R.id.tv_product_condition);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            ib_wishlist = itemView.findViewById(R.id.ib_wishlist);
            ib_wishlist.setSelected(true);

            ib_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    // Perform actions based on the selected state
                    if (v.isSelected()) {
                        // do nothing
                        Toast.makeText(itemView.getContext(), "Added to wishList", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject itemIdJSON = new JSONObject();
                        try {
                            itemIdJSON.put("itemId", productList.get(getAdapterPosition()).getItemId());
                        } catch (JSONException e) { throw new RuntimeException(e); }
                        removeItemFromWishlist(itemIdJSON);
                        ll_product_card.setVisibility(View.GONE);
                        Toast.makeText(itemView.getContext(), "Removed from wishList", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        private void removeItemLocally(int deletePos){
            productList.remove(deletePos);
            notifyDataSetChanged();
            // Notify the adapter that the data has changed
            // Cannot use notifyDataSetChanged() here!
        }
        private void removeItemFromWishlist(JSONObject itemJSON) {
            String url = "http://10.0.2.2:8080/removeitem";

            // Send HTTP POST request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, itemJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response if needed
                            System.out.println("--response(removeItemFromWishlist):"+response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error if needed
                            System.out.println("--error(removeItemFromWishlist)="+error.toString());
                        }
                    });
            requestQueue.add(request);
        }

    }
}


