package com.example.app.onboarding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app.databinding.ActivityOnboardingBinding;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new FirstScreen());
        fragments.add(new SecondScreen());
        fragments.add(new ThirdScreen());

        // Instantiate a ViewPager2 and a PagerAdapter.
        FragmentStateAdapter pagerAdapter = new ViewPagerAdapter(this,fragments);
        binding.pager.setAdapter(pagerAdapter);
    }
}