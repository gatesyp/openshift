package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.com.stohio.openshift.R;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.Highlight;
//import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;

import co.com.stohio.openshift.lib.BottomSheetBehaviorGoogleMapsLike;
import co.com.stohio.openshift.lib.MergedAppBarLayoutBehavior;

/**
 * Created by emerson on 9/16/16.
 */
public class HomeFragment extends Fragment {
    int[] mDrawables = {
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3,
            R.drawable.cheese_3
    };
    private View v;

    TextView bottomSheetTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle(" ");
//        }

        ImageView imageView = (ImageView) v.findViewById(R.id.puppy);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.happy_b1).into(imageViewTarget);

        /**
         * If we want to listen for states callback
         */
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehaviorGoogleMapsLike behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        AppBarLayout mergedAppBarLayout = (AppBarLayout) v.findViewById(R.id.merged_appbarlayout);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle("Data View");
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

        bottomSheetTextView = (TextView) bottomSheet.findViewById(R.id.bottom_sheet_title);
        ItemPagerAdapter adapter = new ItemPagerAdapter(this.getActivity(),mDrawables);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        setCharts();

        return v;
    }

    private void setCharts(){
        // grab data from server

        // populate charts
        lineChart();
        pieChart();
        radarChart();



    }
    private void lineChart(){
        LineChart lineChart = (LineChart) v.findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(1f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(13f, 5));

        LineDataSet dataset = new LineDataSet(entries, "XP Over Time");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
//        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        LineData data = new LineData(dataset);

        lineChart.setData(data);
        lineChart.setDescription("XP Change Over Time");
        lineChart.animateY(5000);
    }
    private void pieChart(){
        PieChart pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(4f, 0));
        pieEntries.add(new PieEntry(3f, 1));
        pieEntries.add(new PieEntry(9f, 2));
        pieEntries.add(new PieEntry(12f, 3));
        pieEntries.add(new PieEntry(18f, 4));
        pieEntries.add(new PieEntry(19f, 5));

        PieDataSet pieDataset = new PieDataSet(pieEntries, "Most Events");
        pieDataset.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animate();

        PieData pieData = new PieData(pieDataset);
        pieChart.setData(pieData);
        pieChart.setDescription("Most Events");
    }
    private void radarChart(){
        RadarChart radarChart = (RadarChart) v.findViewById(R.id.radar_chart);
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        radarEntries.add(new RadarEntry(4f, 0));
        radarEntries.add(new RadarEntry(3f, 1));
        radarEntries.add(new RadarEntry(9f, 2));
        radarEntries.add(new RadarEntry(12f, 3));
        radarEntries.add(new RadarEntry(18f, 4));
        radarEntries.add(new RadarEntry(19f, 5));

        RadarDataSet radarDataset = new RadarDataSet(radarEntries, "Most Events");
        radarDataset.setColors(ColorTemplate.COLORFUL_COLORS);
        radarChart.animate();

        RadarData radarData = new RadarData(radarDataset);
        radarChart.setData(radarData);
        radarChart.setDescription("Relative to Friends");

    }
    public static HomeFragment newInstance(String text) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }
}