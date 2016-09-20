
package co.com.stohio.openshift.main;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.stohio.openshift.R;

/**
 * Created by emerson on 9/16/16.
 */

public class EventsFragment extends Fragment {
    public static RecyclerView.Adapter adapter;
    ArrayList results = new ArrayList<DataObject>();
    public static RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        rv = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv .setLayoutManager(llm);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://f6cd1422.ngrok.io/events/get_last_few";
        String bogos = "sksddfajddfsadfdsasdfasdffskkjlsadflkjdsalk";

        JSONObject jo = new JSONObject();
        try {
            jo.put("user_name","rwr21");
            jo.put("num", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Myactivity", "JSON posted to " + url + " is: " + jo.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jo,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MyActivity", response.toString());
                        JSONArray responseArray = new JSONArray();
                        ArrayList<String> user_names = new ArrayList<String>();
                        ArrayList<String> pet_status = new ArrayList<String>();
                        ArrayList<String> current_xp = new ArrayList<String>();
                        ArrayList<String>  xp_change = new ArrayList<String>();
                        ArrayList<String>  categorys = new ArrayList<String>();
                        try {
                            responseArray = response.getJSONArray("get_last_events");
                            for (int i = 0; i < responseArray.length(); i++) {
                                user_names.add(responseArray.getJSONObject(i).getString("user_name"));
                                pet_status.add(responseArray.getJSONObject(i).getString("pet_status"));
                                categorys.add(responseArray.getJSONObject(i).getString("category"));
                                current_xp.add(responseArray.getJSONObject(i).getString("current_xp"));
                                xp_change.add(responseArray.getJSONObject(i).getString("xp_change"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int l = responseArray.length();
                        Log.d("DONGERDONGERDONGERDONGE", Integer.toString(l));
                        Log.d("DONGERDONGERDONGERDONGE", xp_change.get(0));

                        results.clear();
                        for (int index = 0; index < 2; index++) {
                            int id = getResources().getIdentifier("co.com.stohio.openshift:drawable/" + user_names.get(index), null, null);
//                            DataObject obj = new DataObject(user_names.get(index), current_xp.get(index), id);
                            DataObject obj = new DataObject("Primary", "Secondary ",  id, "Pet level", "pet state");

                            results.add(index, obj);
                        }
                        adapter = new MyCardViewAdapter(results);
                        rv.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {//
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MyActivity", "That didnt work! ");
                    }
                });

        queue.add(jsObjRequest);
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
