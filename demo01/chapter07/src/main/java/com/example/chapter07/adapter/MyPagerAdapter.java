package com.example.chapter07.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chapter07.fragment.SearchFragment;
import com.example.chapter07.fragment.WishlistFragment;

public class MyPagerAdapter extends FragmentStateAdapter {

    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new WishlistFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
