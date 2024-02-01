package com.example.chapter07.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chapter07.fragment.PhotosFragment;
import com.example.chapter07.fragment.ProductFragment;
import com.example.chapter07.fragment.SearchFragment;
import com.example.chapter07.fragment.ShippingFragment;
import com.example.chapter07.fragment.SimilarFragment;
import com.example.chapter07.fragment.WishlistFragment;

public class ProductDetailPagerAdapter extends FragmentStateAdapter {
    private String itemId;
    private String itemTitle;
    private String shippingInfoJSONArrayStr;
    private double shippingCost;
    public ProductDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, String itemId, String itemTitle, String shippingInfoJSONArrayStr, double shippingCost) {
        super(fragmentActivity);
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.shippingInfoJSONArrayStr = shippingInfoJSONArrayStr;
        this.shippingCost = shippingCost;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position) {
            case 0:
                return new ProductFragment(this.itemId, this.shippingCost);
            case 1:
                return new ShippingFragment(this.itemId, this.shippingInfoJSONArrayStr);
            case 2:
                return new PhotosFragment(this.itemTitle);
            case 3:
                return new SimilarFragment(this.itemId);
            default:
                return new ProductFragment(this.itemId, this.shippingCost);
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Number of tabs
    }


}
