
package co.com.stohio.openshift.main;

import android.os.Bundle;
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

public class SocialFragment extends Fragment {
    public static RecyclerView.Adapter adapter;
    ArrayList results = new ArrayList<DataObject>();
    public static RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
        rv = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv .setLayoutManager(llm);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://f6cd1422.ngrok.io/friends/find";
        String bogos = "sksddfajddfsadfdsasdfasdffskkjlsadflkjdsalk";

        JSONObject jo = new JSONObject();
        try {
            jo.put("user_name","rwr21");
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
                        try {
                            responseArray = response.getJSONArray("friends");
                            for (int i = 0; i < responseArray.length(); i++) {
                                user_names.add(responseArray.getJSONObject(i).getString("user_name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int l = responseArray.length();

                        results.clear();
                        for (int index = 0; index < l; index++) {
                            DataObject obj = new DataObject(user_names.get(index), "Secondary " + index);

                            results.add(index, obj);
                        }
                        adapter = new MyRA(results);
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

    public static SocialFragment newInstance(String text) {
        SocialFragment f = new SocialFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}