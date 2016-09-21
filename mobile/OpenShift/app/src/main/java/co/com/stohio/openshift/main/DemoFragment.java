package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import co.com.stohio.openshift.R;

public class DemoFragment extends Fragment {

    private int[] GalImages = new int[] {
            R.raw.happy, R.raw.bloated, R.raw.homestate_1, R.raw.homestate_2, R.raw.sick, R.raw.sick_1, R.raw.tired_pt1, R.raw.tired_pt2, R.drawable.home

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demo, container, false);

        ItemPagerAdapter adapter = new ItemPagerAdapter(this.getActivity(),GalImages);
        adapter.setActivity(getActivity());
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.mvieww);
        viewPager.setAdapter(adapter);
//        ImageView imageView1 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(imageView1);
//        Glide.with(this).load(R.raw.homestate_1).into(imageViewTarget1);
//        ImageView imageView2 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget2 = new GlideDrawableImageViewTarget(imageView2);
//        Glide.with(this).load(R.raw.happy).into(imageViewTarget2);
//        ImageView imageView3 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget3 = new GlideDrawableImageViewTarget(imageView3);
//        Glide.with(this).load(R.raw.tired_pt1).into(imageViewTarget3);
//        ImageView imageView4 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget4 = new GlideDrawableImageViewTarget(imageView4);
//        Glide.with(this).load(R.raw.sick).into(imageViewTarget4);
//        ImageView imageView5 = (ImageView) v.findViewById(R.id.demo1);
//        GlideDrawableImageViewTarget imageViewTarget5 = new GlideDrawableImageViewTarget(imageView5);
//        Glide.with(this).load(R.raw.bloated).into(imageViewTarget5);

        return v;
    }

    public static DemoFragment newInstance(String text) {
        DemoFragment f = new DemoFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }
}

