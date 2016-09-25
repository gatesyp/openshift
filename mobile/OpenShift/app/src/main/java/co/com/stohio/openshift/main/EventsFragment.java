
package co.com.stohio.openshift.main;

import android.os.Bundle;
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
    ArrayList results = new ArrayList<DataObjectEvents>();
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
        String url = "http://e925d3c8.ngrok.io/events/get_last_few";

//        String url = "http://deploy-openshift1.0ec9.hackathon.openshiftapps.com/events/get_last_few";
//        String url = "http://myapppython27-openshift1.0ec9.hackathon.openshiftapps.com/events/get_last_few";
        String bogos = "sksddfajddfsadfdsasdfasdffskkjjkjkjlsadflkjdsalk";

        JSONObject jo = new JSONObject();
        try {
            jo.put("user_name","rwr21");
            jo.put("num", "8");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Myactivity", "JSON posted to " + url + " is: " + jo.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jo,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Here is REQUSTE", response.toString());
                        JSONArray responseArray = new JSONArray();
                        ArrayList<String> user_names = new ArrayList<String>();
                        ArrayList<String> pet_status = new ArrayList<String>();
                        ArrayList<String> current_xp = new ArrayList<String>();
                        ArrayList<String>  xp_change = new ArrayList<String>();
                        ArrayList<String>  categorys = new ArrayList<String>();
                        try {
                            responseArray = response.getJSONArray("get_last_events");
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject user = responseArray.getJSONObject(i);
                                user_names.add(user.getString("user_name"));
                                pet_status.add(user.getString("pet_status"));
                                categorys.add(user.getString("category"));
                                current_xp.add(user.getString("current_xp"));
                                xp_change.add(user.getString("xp_change"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int l = responseArray.length();
                        Log.d("DONGERDOGERDONGERDONGE", Integer.toString(l));
                        Log.d("DONGERDONGERDONGERDONGE", xp_change.get(0));

                        results.clear();
                        for (int index = 0; index < l; index++) {
                            int id = getResources().getIdentifier("co.com.stohio.openshift:drawable/" + pet_status.get(index), null, null);
//                            DataObject obj = new DataObject(user_names.get(index), current_xp.get(index), id);
                            DataObjectEvents obj = new DataObjectEvents(categorys.get(index), xp_change.get(index),  current_xp.get(index), id);

                            results.add(index, obj);
                        }
                        adapter = new MyEventsAdapter(results);
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
