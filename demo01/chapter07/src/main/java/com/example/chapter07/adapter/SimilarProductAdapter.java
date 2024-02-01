package com.example.chapter07.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter07.R;
import com.example.chapter07.entity.SimilarProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.ViewHolder> {
    private ArrayList<SimilarProduct> similarProducts;
    private Context context;

    public SimilarProductAdapter(Context context, ArrayList<SimilarProduct> similarProducts) {
        this.context = context;
        this.similarProducts = similarProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_product,
                parent, false);
        return new ViewHolder(view);
    }

    // bind data: this class --> holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimilarProduct similarProduct = similarProducts.get(position);

        // Load image using Picasso library (you can use your preferred image loading library)
        Picasso.get().load(similarProduct.getImageUrl()).into(holder.imageView);

        holder.titleTextView.setText(similarProduct.getTitle());
        holder.shippingCostTextView.setText(String.format("Shipping Cost: $%.2f", similarProduct.getShippingCost()));
        holder.daysLeftTextView.setText(String.format("Days Left: %d", similarProduct.getDaysLeft()));
        holder.priceTextView.setText(String.format("Price: $%.2f", similarProduct.getPrice()));
    }

    @Override
    public int getItemCount() {
        return this.similarProducts.size();
    }

    // holder, get elements from XML
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView shippingCostTextView;
        TextView daysLeftTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            shippingCostTextView = itemView.findViewById(R.id.shipping_cost_text_view);
            daysLeftTextView = itemView.findViewById(R.id.days_left_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
        }
    }
}
