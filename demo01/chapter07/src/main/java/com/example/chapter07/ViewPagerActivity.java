package com.example.chapter07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager vp_content = findViewById(R.id.vp_content);
//        ArrayList<String> mGoodList = null;
//        ImagePagerAdapter adapter = new ImagePagerAdapter(this, mGoodList);
//        vp_content.setAdapter();

    }
}