package com.tripplanner.previous_trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.PreviousTripFragmentBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviousTripFragment extends Fragment  {

    private PreviousTripViewModel previousTripViewModel;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PreviousTripFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.previous_trip_fragment, container, false);
        View view = binding.getRoot();

    //    Toolbar toolbar =  binding.toolbar;
    //    ((AppCompatActivity)getActivity()).setActionBar(toolbar);
        viewPager =binding.viewpager;
        addTabs(viewPager);
        tabLayout =  binding.tabs;
        tabLayout.setupWithViewPager(viewPager);
        disableswipe();
        return view;
    }
    private void disableswipe()
    {
        int PAGE_0 = 0;
        int PAGE_1 = 1;
        int PAGE_2 = 2;
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPager.getCurrentItem() == PAGE_0) {
                    viewPager.setCurrentItem(PAGE_0 - 1, false);
                    viewPager.setCurrentItem(PAGE_0, false);
                    return true;
                }
                else if (viewPager.getCurrentItem() == PAGE_1) {
                    viewPager.setCurrentItem(PAGE_1 - 1, false);
                    viewPager.setCurrentItem(PAGE_1, false);
                    return true;
                }
                else if (viewPager.getCurrentItem() == PAGE_2) {
                    viewPager.setCurrentItem(PAGE_2 - 1, false);
                    viewPager.setCurrentItem(PAGE_2, false);
                    return true;
                }
                return false;
            }
        });
    }

    private void addTabs(ViewPager viewPager) {
        PreviousTripAdapter adapter = new PreviousTripAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new DoneTripFragment(), "Done Trips");
        adapter.addFrag(new CancledTripFragment(), "Canceled Trips");
          adapter.addFrag(new MapContinerFragment(), "Map");
        //   adapter.addFrag(new BananaFragment(), "Banana");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        previousTripViewModel = ViewModelProviders.of(this).get(PreviousTripViewModel.class);
        // TODO: Use the ViewModel
      //  viewPager.beginFakeDrag();

    }


}
