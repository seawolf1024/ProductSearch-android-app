package com.example.chapter07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chapter07.adapter.MyPagerAdapter;
import com.example.chapter07.adapter.ProductDetailPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;

public class ProductDetailActivity extends AppCompatActivity {
    String productId;
    String productTitle;
    String shippingInfoJSONArrayStr;
    double shippingCost = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        this.productId = getIntent().getExtras().getString("itemId");
        this.productTitle = getIntent().getExtras().getString("title");
        this.shippingInfoJSONArrayStr = getIntent().getExtras().getString("shippingInfoJSONArrayStr");
        try {
            parseShippingCost(this.shippingInfoJSONArrayStr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // set tabBar
        TextView tv_title_bar = findViewById(R.id.tv_title_bar);
        tv_title_bar.setText(this.productTitle);
        ImageButton ib_return = findViewById(R.id.ib_return);
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // set productId

        TextView textView = findViewById(R.id.tv_itemId);
//        textView.setText(this.productId + this.productTitle);

        // set Tab and ViewPager
        TabLayout tabLayout = findViewById(R.id.tl_tabLayout);
        ViewPager2 viewPager = findViewById(R.id.vp_viewPager);

        ProductDetailPagerAdapter adapter = new ProductDetailPagerAdapter(this, this.productId, this.productTitle, this.shippingInfoJSONArrayStr, this.shippingCost);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.information_variant_box);
                            tab.setText("Product");

                            break;
                        case 1:
                            tab.setIcon(R.drawable.truck_delivery);
                            tab.setText("Shipping");

                            break;
                        case 2:
                            tab.setIcon(R.drawable.google);
                            tab.setText("Photos");
                            break;
                        case 3:
                            tab.setIcon(R.drawable.equal);
                            tab.setText("Similar");
                    }
                }
        ).attach();
        tabLayout.setBackgroundColor(getResources().getColor(R.color.purple));
        tabLayout.setTabTextColors(Color.parseColor("#939191"),Color.parseColor("#FFFFFF"));

    }

    private void getProductDetail(String itemId){

    }

    private void parseShippingCost(String shippingInfoJSONArrayStr) throws JSONException {
        JSONArray shippingInfoJSONArray = new JSONArray(shippingInfoJSONArrayStr);
        for(int i = 0; i < shippingInfoJSONArray.length(); i++){
            String currItemId = shippingInfoJSONArray.getJSONObject(i).getString("itemId");
            if(currItemId.equals(this.productId)){
                this.shippingCost = Float.parseFloat(shippingInfoJSONArray.getJSONObject(i).getString("itemShippingCost"));
                break;
            }
        }
    }
}