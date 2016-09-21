package co.com.stohio.openshift.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import az.plainpie.PieView;
import co.com.stohio.openshift.R;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.com.stohio.openshift.lib.BottomSheetBehaviorGoogleMapsLike;
import co.com.stohio.openshift.lib.MergedAppBarLayoutBehavior;

import static java.lang.Math.abs;

/**
 * Created by emerson on 9/16/16.
 */
public class HomeFragment extends Fragment {
//    ArrayList results = new ArrayList<DataObject>();
    int[] mDrawables = {
            R.raw.sick_1
    };
    private View v;
    public static int nut_num;
    public static int social_num;
    public static int sleep_num;
    public static int fitness_num;
    public static float perc;


    TextView bottomSheetTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        String url = "http://f6cd1422.ngrok.io/events/get_last_few";
//        String url = "http://e925d3c8.ngrok.io/events/get_data";

        String url = "http://deploy-openshift1.0ec9.hackathon.openshiftapps.com/events/get_data";
        String bogos = "sksddfajddfsadfdsasdfasdffskkjlsadflkjdsalk";

        JSONObject jo = new JSONObject();
        try {
            jo.put("user_name","rwr21");
            jo.put("num", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Myactivity", "JSON posted to " + url + " is: " + jo.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jo,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("THEACTIVITY", response.toString());
                        JSONArray responseArray = new JSONArray();
//                        ArrayList<String> user_names = new ArrayList<String>();
//                        ArrayList<String> pet_status = new ArrayList<String>();
//                        ArrayList<String> current_xp = new ArrayList<String>();
//                        ArrayList<String>  xp_change = new ArrayList<String>();
//                        ArrayList<String>  categorys = new ArrayList<String>();
                        try {
                            JSONObject person = response.getJSONObject("data");
                            Log.d("PERSONPERSOPESON", person.toString());

                            int pet_level = person.getInt("pet_level");
                            String pet_name = person.getString("pet_name");
                            String percent_to_lvl = person.getString("percent_to_lvl");
                            perc = Float.parseFloat(percent_to_lvl);
                            Log.d("Here is the perc", percent_to_lvl);
                            JSONObject last_week_data = person.getJSONObject("last_week_data");
                            String current_pet_state = last_week_data.getString("current_pet_state");
                            String future_pet_statse = last_week_data.getString("future_pet_state");
                            JSONObject last_week_xp = last_week_data.getJSONObject("last_week_xp");

                            String nutrition = last_week_xp.getString("nutrition");
                            nut_num = Integer.parseInt(nutrition);
                            String social = last_week_xp.getString("social");
                            social_num = Integer.parseInt(social);
                            String sleep = last_week_xp.getString("sleep");
                            sleep_num = Integer.parseInt(sleep);
                            String fitness = last_week_xp.getString("fitness");
                            fitness_num = Integer.parseInt(fitness);

//                            for (int i = 0; i < responseArray.length(); i++) {
//                                user_names.add(responseArray.getJSONObject(i).getString("user_name"));
//                                pet_status.add(responseArray.getJSONObject(i).getString("pet_status"));
//                                categorys.add(responseArray.getJSONObject(i).getString("category"));
//                                current_xp.add(responseArray.getJSONObject(i).getString("current_xp"));
//                                xp_change.add(responseArray.getJSONObject(i).getString("xp_change"));
//                            }

//                            ImageView imageView = (ImageView) v.findViewById(R.id.puppy);
//                            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//                            Glide.with(getActivity()).load(R.raw.homestate_1).into(imageViewTarget);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
//                        int l = responseArray.length();
//                        Log.d("DONGERDONGERDONGERDONGE", Integer.toString(l));
//                        Log.d("DONGERDONGERDONGERDONGE", xp_change.get(0));
//
////                        results.clear();
//
////                        for (int index = 0; index < 2; index++) {
////                            int id = getResources().getIdentifier("co.com.stohio.openshift:drawable/" + user_names.get(index), null, null);
//
////                            DataObject obj = new DataObject(user_names.get(index), current_xp.get(index), id);
////                            DataObject obj = new DataObject("Primary", "Secondary ",  id, "Pet level", "pet state");
//
////                            results.add(index, obj);
////                        }
                    }
                },
                new Response.ErrorListener() {//
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        ImageView imageView = (ImageView) v.findViewById(R.id.puppy);
//                        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//                        Glide.with(getActivity()).load(R.raw.happy).into(imageViewTarget);
                        Log.d("MyActivity", "That didnt work! ");
                    }
                });

        queue.add(jsObjRequest);
        ImageView imageView = (ImageView) v.findViewById(R.id.puppy);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.happy).into(imageViewTarget);

        TextView message = (TextView) v.findViewById(R.id.HoverText);
//        message.setText("Fido is sleepy.\nGet moving to perk him up!");
        message.setText("Nice job!\nFido is feeling frisky");
        message.setTextColor(Color.parseColor("#000000"));
//        message.setTypeface();
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
        adapter.setActivity(getActivity());
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        setCharts();

        return v;
    }
    // placeholder code to run selected code on a thread
//    private void runThread(){
//
//        final int interval = 2000; // 1 Second
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable(){
//            public void run() {
//                Toast.makeText(getActivity(), "C'Mom no hands!", Toast.LENGTH_SHORT).show();
//                ImageView imageView = (ImageView) v.findViewById(R.id.puppy);
//                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//                Glide.with(getActivity()).load(R.raw.homestate_2).into(imageViewTarget);
//            }
//        };
//        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
//        handler.postDelayed(runnable, interval);
//    }

    private void setCharts(){
        // grab data from server

        // populate charts
        lineChart();
        pieChart();
        //radarChart();

    }
    private void lineChart(){
        PieView pie = (PieView) v.findViewById(R.id.pieView);
        pie.setmPercentage(perc);
//        Log.d("PERCENTSGEEE", perc);
        LineChart lineChart = (LineChart) v.findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(1f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(8f, 4));

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
        lineChart.setDescription("");
        lineChart.animateY(5000);
    }
    private void pieChart(){
        PieChart pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(abs(nut_num), "Nutrition"));
        pieEntries.add(new PieEntry(abs(social_num), "Social"));
        pieEntries.add(new PieEntry(abs(sleep_num), "Sleep"));
        pieEntries.add(new PieEntry(abs(fitness_num), "Fitness"));

        PieDataSet pieDataset = new PieDataSet(pieEntries, "Most Events");
        pieDataset.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animate();

        PieData pieData = new PieData(pieDataset);
        pieChart.setData(pieData);
        pieChart.setDescription("");
    }



    //    private void radarChart(){
//        RadarChart radarChart = (RadarChart) v.findViewById(R.id.radar_chart);
//        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
//        radarEntries.add(new RadarEntry(4f, 0));
//        radarEntries.add(new RadarEntry(3f, 1));
//        radarEntries.add(new RadarEntry(9f, 2));
//        radarEntries.add(new RadarEntry(12f, 3));
//        radarEntries.add(new RadarEntry(18f, 4));
//        radarEntries.add(new RadarEntry(19f, 5));
//
//        RadarDataSet radarDataset = new RadarDataSet(radarEntries, "Most Events");
//        radarDataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        radarChart.animate();
//
//        RadarData radarData = new RadarData(radarDataset);
//        radarChart.setData(radarData);
//        radarChart.setDescription("Relative to Friends");
//
//    }
    public static HomeFragment newInstance(String text) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }
}
