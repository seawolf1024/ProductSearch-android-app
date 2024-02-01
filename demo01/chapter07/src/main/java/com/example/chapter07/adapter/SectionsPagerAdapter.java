package com.example.chapter07.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chapter07.fragment.SearchFragment;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment for each tab
        switch (position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new SearchFragment();
            default:
                return new Fragment(); // Return a default fragment or null if needed
        }
    }

    @Override
    public int getItemCount() {
        // Number of tabs
        return 2;
    }
}
