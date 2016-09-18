package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.com.stohio.openshift.R;

/**
 * Created by emerson on 9/16/16.
 */
public class EventsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        TextView tv = (TextView) v.findViewById(R.id.eventsfrag);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static EventsFragment newInstance(String text) {
        EventsFragment f = new EventsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }
}
