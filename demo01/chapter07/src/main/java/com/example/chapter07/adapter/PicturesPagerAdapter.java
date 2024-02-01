package com.example.chapter07.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.chapter07.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PicturesPagerAdapter extends PagerAdapter {

    private final Context context;
    private final ArrayList<String> productPictureUrls;

    public PicturesPagerAdapter(Context context, ArrayList<String> productPictureUrls, ViewPager vp_pictures){
        this.context = context;
        this.productPictureUrls = productPictureUrls;
        this.vp_pictures = vp_pictures;
        this.handler = new Handler();
        System.out.println("--adapter,urls="+this.productPictureUrls);
    }


    // Scroll
    private ViewPager vp_pictures;
    private static final long AUTO_SCROLL_DELAY = 5000; // Auto-scroll delay in milliseconds
    private Handler handler;
    private boolean autoScrollEnabled = false;
    public void startAutoScroll() {
        if (!autoScrollEnabled) {
            autoScrollEnabled = true;
            handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        }
    }

    public void stopAutoScroll() {
        if (autoScrollEnabled) {
            autoScrollEnabled = false;
            handler.removeCallbacks(autoScrollRunnable);
        }
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = vp_pictures.getCurrentItem();
            int nextItem = (currentItem + 1) % getCount();
            vp_pictures.setCurrentItem(nextItem, true);
            handler.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };
    //



    @Override
    public int getCount() {
//        System.out.println("--getCount(PicturesPagerAdapter)=" + this.productPictureUrls.size());
        return this.productPictureUrls.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product_picture, container, false);

        ImageView iv_picture = view.findViewById(R.id.iv_picture);
        Picasso.get().load(productPictureUrls.get(position)).into(iv_picture);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
