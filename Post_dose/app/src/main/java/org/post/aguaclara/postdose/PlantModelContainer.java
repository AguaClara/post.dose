package org.post.aguaclara.postdose;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds all of the models for all of the plants for which regression data was sent.
 * Created by asm278 on 11/16/16.
 */

public class PlantModelContainer extends Persistent{
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
        this();
        setFromJSON(json);
    }

    public void setFromJSON(String json){
        json = json.replace(" ", "");
        try {
            JSONObject jObject = new JSONObject(json);
            Iterator<?> keys = jObject.keys();
            while( keys.hasNext() ) {
                String key = (String) keys.next();
                if (jObject.get(key) instanceof JSONObject) {
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

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!PlantModelContainer.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final PlantModelContainer that = (PlantModelContainer) obj;
        Iterator<String> keys = models.keySet().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            if (!that.models.containsKey(key))
                return false;
            else if (!models.get(key).equals(that.models.get(key)))
                return false;
        }
        keys = that.models.keySet().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            if (!models.containsKey(key))
                return false;
            else if (!that.models.get(key).equals(models.get(key)))
                return false;
        }
        return true;
    }
}
