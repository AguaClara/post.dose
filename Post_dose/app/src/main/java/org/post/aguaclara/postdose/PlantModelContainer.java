package org.post.aguaclara.postdose;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds all of the models for all of the plants for which regression data was sent.
 * Created by asm278 on 11/16/16.
 */

public class PlantModelContainer {
    static final String filename = "myModelfile";
    private HashMap<String, ModelContainer> models;

    public PlantModelContainer(){
        models = new HashMap<String,ModelContainer>();
    }

    /**
     * This constructs thte PlantModelContainer from a json string of :
     * {plantname: {model:{p1:v1,...},...},...,"general": {model:{p1:v1,...}}}
     * @param json
     */
    public PlantModelContainer(String json){
        setFromJSON(json);
    }

    public void setFromJSON(String json){
        try {
            JSONObject jObject = new JSONObject(json);
            Iterator<?> keys = jObject.keys();
            while( keys.hasNext() ) {
                String key = (String) keys.next();
                if (jObject.get(key) instanceof JSONObject) {
                    System.out.println(key);
                    models.put(key, new ModelContainer(jObject.get(key).toString()));
                }
            }
        } catch (JSONException e){
            System.err.println("PlantModelContainer: BAD JSON RECVD!");
            e.printStackTrace();
        }
        if (! models.containsKey("general"))
            System.err.println("No general model!!!");
    }

    public float getBestDosageRecommendation(float rawWaterTurb, String plantName){
        if (models.containsKey(plantName))
            return models.get(plantName).getBestDosageRecommendation(rawWaterTurb);
        else if (models.containsKey("general"))
            return models.get("general").getBestDosageRecommendation(rawWaterTurb);
        else
            return -1f;
    }
    @Override
    public String toString(){
        JSONObject j = new JSONObject();
        try {
            Iterator<String> keys = models.keySet().iterator();
            while (keys.hasNext()){
                String key = keys.next();
                j.put(key,models.get(key).toJSON());
            }
        } catch (JSONException e) {
            System.out.println("JSON-ifying all the plants models failed");
            e.printStackTrace();
        }
        return j.toString();
    }

    private void load(FileInputStream inputStream){
        //LOADS THIS MODEL
        String old_model = "";
        try {

            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            old_model = sb.toString();
        } catch (Exception e) {
            System.out.println("No Model File Found?");
            e.printStackTrace();
        }

        setFromJSON(old_model);

    }

    //outputstream needs to be opened.
    protected void save(FileOutputStream outputStream){
        //saves this model collection as a JSON
        String str = this.toString();
        //https://developer.android.com/training/basics/data-storage/files.html
        try {
            outputStream.write(str.getBytes());
            outputStream.close();
        } catch (Exception e) {
            System.out.println("No Model File Found in save");
            e.printStackTrace();
        }
    }


    protected void saveModelCollection(Context context){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            this.save(outputStream);
        } catch (Exception e) {
            System.err.println("No Model File Found in saveModel?");
            e.printStackTrace();
        }
    }
    protected void loadModel(Context context){
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(filename);
            this.load(inputStream);
        } catch (Exception e) {
            System.out.println("No Model File Found loading in main");
            e.printStackTrace();
        }
    }
}
