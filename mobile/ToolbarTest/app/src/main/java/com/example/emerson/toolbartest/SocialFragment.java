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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by emerson on 9/16/16.
 */

public class SocialFragment extends Fragment {
    private RecyclerView.Adapter adapter;
    public static JSONObject capture_response = new JSONObject();
    ArrayList results = new ArrayList<DataObject>();

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




        ArrayList<DataObject> myVar = new ArrayList<DataObject>();
        myVar = getDataSet(new VolleyCallback() {
            @Override
            public ArrayList<DataObject> onSuccess(String result) {
                return results;
            }
        });

        adapter = new MyRA(myVar);



        rv.setAdapter(adapter);
        return v;
    }

    private ArrayList<DataObject> getDataSet(final VolleyCallback callback) {
//    private void getDataSet(final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://f6cd1422.ngrok.io/friends/find";

        String bogos = "sksddfajddfsadfdsasdfasdffskkjlsadflkjdsalk";

        JSONObject returned = new JSONObject();
        JSONObject jo = new JSONObject();
        try {
            jo.put("user_name","rwr21");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Myactivity", "JSON posted to " + url + " is: " + jo.toString());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jo,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        // create the model
                        capture_response = response;
                        int l = capture_response.length();
                        String lenght = Integer.toString(l);
                        Log.d("MyActyivity", "Here is the length: " +lenght);
                        Log.d("MyActivity", "eeDONGER");
                        Log.d("MyActivity", response.toString());

                        for (int index = 0; index < l; index++) {
                            DataObject obj = new DataObject("Some Primary Text " + index, "Secondary " + index);
                            results.add(index, obj);
                        }
                        callback.onSuccess("Test");
                    }
                },
                new Response.ErrorListener() {//
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyActivity", "That didnt work! ");
            }
        });

        queue.add(jsObjRequest);
//        capture_response = future.get(30, TimeUnit.SECONDS);
        try {
            JSONObject response =  future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (TimeoutException e) {
            // exception handling
        }
        return ;
    }

    public interface VolleyCallback {
      ArrayList<DataObject> onSuccess(String result);
    }

    public static SocialFragment newInstance(String text) {
        SocialFragment f = new SocialFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
