package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.com.stohio.openshift.R;

/**
 * Created by emerson on 9/15/16.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mPage == 1) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            return view;
        }
        if(mPage == 2) {
            View view = inflater.inflate(R.layout.fragment_events, container, false);
            return view;
        }
        if(mPage == 3) {
            View view = inflater.inflate(R.layout.fragment_social, container, false);
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.fragment_social, container, false);
            TextView textView = (TextView) view;
            textView.setText("Fragment #" + mPage);
            return view;
        }

    }

}
