
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

public class SocialFragment extends Fragment {
    public static RecyclerView.Adapter adapter;
    ArrayList results = new ArrayList<DataObjectSocial>();
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
//        String url = "http://f6cd1422.ngrok.io/friends/find_advanced";
//        String url = "http://e925d3c8.ngrok.io/friends/find_advanced";
        String url = "http://deploy-openshift1.0ec9.hackathon.openshiftapps.com/friends/find_advanced";
//        String url = "http://myapppython27-openshift1.0ec9.hackathon.openshiftapps.com/friends/find_advanced";
        String bogos = "sksddfadsdfjddfsadjdkdfdsasdfasdffskkjlsadflkjdsalk";

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
                        Log.d("DONGERACTIVITERF", response.toString());
                        JSONArray responseArray = new JSONArray();
                        ArrayList<JSONObject> users = new ArrayList<JSONObject>();
                        ArrayList<String> usernames = new ArrayList<String>();
                        ArrayList<String> pet_states = new ArrayList<String>();
                        ArrayList<String> pet_levels = new ArrayList<String>();
                        String username = new String();
                        String pet_state = new String();

//                        ArrayList<JSONObject> users = new ArrayList<JSONObject>();
                        try {
                            responseArray = response.getJSONArray("get_advanced_friend_data");
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject users_object = responseArray.getJSONObject(i);
                                JSONObject data = users_object.getJSONObject("data");
                                username = data.getString("username");
                                usernames.add(username);

                                JSONObject last_week_data = data.getJSONObject("last_week_data");
                                pet_state = last_week_data.getString("current_pet_state");
                                pet_states.add(pet_state);

                                int level = data.getInt("pet_level");
                                pet_levels.add(Integer.toString(level));

                                Log.d("JSONOBJECT_USER", users_object.toString());
                                Log.d("JSONOBJECT_data", data.toString());
                                Log.d("JSONOBJECT_username", username);
                                Log.d("JSONOBJECT_last_week", last_week_data.toString());
                                Log.d("JSONOBJECT_pet_state", pet_state);
                                Log.d("JSONOBJECT_level", Integer.toString(level));
                                users.add(responseArray.getJSONObject(0));
//                                user_names.add(responseArray.getJSONObject(i).getString("username"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int l = responseArray.length();

                        results.clear();
                        for (int index = 0; index < l; index++) {
                            int id = getResources().getIdentifier("co.com.stohio.openshift:drawable/" + usernames.get(index), null, null);
                            DataObjectSocial obj = new DataObjectSocial(usernames.get(index), pet_levels.get(index), pet_states.get(index), id );
                            results.add(index, obj);
                        }
                        adapter = new MySocialAdapter(results);
                        rv.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {//
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MyActivity", "That didnt work! ");
                        for (int index = 0; index < 2; index++) {
                            int id = getResources().getIdentifier("co.com.stohio.openshift:drawable/" + "emc67", null, null);
                            DataObjectSocial obj = new DataObjectSocial("Name", "Level",  "State", id);

                            results.add(index, obj);
                        }
                        adapter = new MySocialAdapter(results);
                        rv.setAdapter(adapter);
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