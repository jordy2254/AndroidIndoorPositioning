package com.jordan.ips.model.data;

import android.content.Context;

import com.google.gson.Gson;
import com.jordan.ips.model.utils.PathFindingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileManager {

    private FileManager(){}

    private  static final String mapPath = "/maps";

    public static void saveWrapper(Context context, MapWrapper wrapper){
        if(!checkAndCreateSaveDir(context)){
            return;
        }
        PathFindingUtils.clearAllDynamicPathNodes();
        File file = new File(getPathname(context), getFileName(wrapper));
        if(file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        try {
            FileWriter fw = new FileWriter(file, true);
            new Gson().toJson(wrapper, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWrapper(Context context, MapWrapper wrapper){
        PathFindingUtils.clearAllDynamicPathNodes();
        if(!checkAndCreateSaveDir(context)){
            return;
        }

        File file = new File(getPathname(context), getFileName(wrapper));
        if(file.exists()) {
            file.delete();
        }
    }

    public static List<MapWrapper> loadMapWrappers(Context context) {
        PathFindingUtils.clearAllDynamicPathNodes();
        File dir = new File(getPathname(context));
        if(!dir.exists()){
            return new ArrayList<>();
        }

        File[] files = dir.listFiles();
        List<MapWrapper> wrappers = new ArrayList<>();

        for(File file : files){
            if(file.isDirectory()){
                continue;
            }
            if(!file.getName().contains(".json")){
                continue;
            }

            Gson gson = new Gson();
            FileReader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            MapWrapper wrapper = gson.fromJson(reader, MapWrapper.class);
            if(wrapper == null){
                continue;
            }
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private static String getFileName(MapWrapper wrapper) {
        return String.format("%d-%s.json", wrapper.getMap().getId(), wrapper.getMap().getName());
    }

    private static boolean checkAndCreateSaveDir(Context context) {
        File file = new File(getPathname(context));
        if(!file.exists()){
            return file.mkdirs();
        }
        return true;
    }

    private static String getPathname(Context context) {
        return context.getFilesDir() + mapPath;
    }
}
