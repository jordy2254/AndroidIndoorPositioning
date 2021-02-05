package com.jordan.ips.model.api;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.jordan.ips.model.data.FileManager;
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
    private static final String apiAddress = "http://192.168.0.28:3500/maps/sync";

    public static void syncroniseMap(int mapId, String mapPass, Context context, MapSyncronsiedCallBack callBack){
        JSONObject obj = createJsonObject(mapId, mapPass);

        StringRequest sr = new StringRequest(POST, apiAddress, resp ->{
            Log.i("Rest Response", resp);
            Gson gson = new Gson();
            Map map = gson.fromJson(resp, Map.class);
            callBack.syncronisationComplete(map);
        }, err->{
            callBack.syncronisationFailed();
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }


            @Override
            public byte[] getBody() {
                return obj.toString().getBytes();
            }

        };


        MyRequestQueue.getInstance(context).addToRequestQueue(sr);
    }

    public static void syncroniseMapAndUpdateWrapper(MapWrapper mapWrapper, int mapId, String mapPass, Context context, MapSyncronsiedCallBack callBack){
        syncroniseMap(mapId, mapPass, context, new MapSyncronsiedCallBack() {
            @Override
            public void syncronisationComplete(Map map) {
                mapWrapper.setMap(map);
                mapWrapper.setLastSyncedDate(new Date());
                mapWrapper.setSyncing(false);
                FileManager.saveWrapper(context, mapWrapper);

                callBack.syncronisationComplete(map);
            }

            @Override
            public void syncronisationFailed() {
                Toast.makeText(context, "Failed to syncronize map", Toast.LENGTH_SHORT).show();
                mapWrapper.setSyncing(false);

                callBack.syncronisationFailed();
            }
        });
    }

    private static JSONObject createJsonObject(int mapId, String mapPass){
        JSONObject object = new JSONObject();
        try {
            object.put("mapKey", String.valueOf(mapId));
            object.put("password", mapPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}
