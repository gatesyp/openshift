package com.example.emerson.toolbartest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerson on 9/16/16.
 */

public class SocialFragment extends Fragment {
    private RecyclerView.Adapter adapter;
    public static JSONObject returner = new JSONObject();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social, container, false);
//        TextView tv = (TextView) v.findViewById(R.id.socialfrag);
//        tv.setText(getArguments().getString("msg"));

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        rv .setLayoutManager(llm);
        adapter = new MyRA(getDataSet());
        rv.setAdapter(adapter);
        return v;
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://f6cd1422.ngrok.io/friends/find";

        String bogos = "ajddfsadfdsafskkjlsadflkjdsalk";

        JSONObject returned = new JSONObject();
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
                        // Display the first 500 characters of the response string.
                        // create the model
                        returner = response;

                        Log.d("MyActivity", "eeDONGER");
                        Log.d("MyActivity", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyActivity", "That didnt work! ");
            }
        });

        queue.add(jsObjRequest);


        int l = returner.length();


        String lenght = Integer.toString(l);

        Log.d("MyActyivity", "Here is the length: " +lenght);

        for (int index = 0; index < l; index++) {
            DataObject obj = new DataObject("Some Primary Text " + index, "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }

    public static SocialFragment newInstance(String text) {
        SocialFragment f = new SocialFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;

    }

//    private void getEvents(){
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        String url ="http://www.google.com";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        // create the model
//                        eventText.setText(response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                String msg = "That didnt work! ";
//                eventText.setText(msg);
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
}
