package com.jordan.ips.model.api;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jordan.ips.model.data.MapWrapper;
import com.jordan.ips.model.data.map.persisted.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static com.android.volley.Request.Method.*;

public class MapSyncronisationUtil {

    //Manifest allows this to happen with cleartexttrafficpermitted- remove this for final release,
    //secure with ssl
    private static final String apiAddress = "http://10.0.2.2:3500/maps/sync";

    public static void syncroniseMap(int mapId, String mapPass, Context context, MapSyncronsiedCallBack callBack){
        JSONObject obj = createJsonObject(mapId, mapPass);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(GET, apiAddress, null, response -> {
            Log.i("Rest Response", response.toString());
            Gson gson = new Gson();
            Map map = gson.fromJson(response.toString(), Map.class);
            callBack.syncronisationComplete(map);
        }, response -> {
            callBack.syncronisationFailed();
        }){
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() {
                return obj.toString().getBytes();
            }

        };
        MyRequestQueue.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    private static void x(){



    }
    private static JSONObject createJsonObject(int mapId, String mapPass){
        JSONObject object = new JSONObject();
        try {
            object.put("mapKey", mapId);
            object.put("password", mapPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}
