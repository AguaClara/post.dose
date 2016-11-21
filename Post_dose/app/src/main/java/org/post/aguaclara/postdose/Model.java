package org.post.aguaclara.postdose;

/**
 * Created by asm278 on 10/6/16.
 */

import org.json.JSONException;
import org.json.JSONObject;


public abstract class Model{
    public static final float eps = 0.0001f;
    float p1;
    float p2;
    float rSquared;


    public Model(){
        p1 = 0.0f;
        p2 = 0.0f;
        rSquared = -1f;
    }
    public Model(float a, float b,float rSq){
        p1 = a;
        p2 = b;
        rSquared = rSq;
    }

    abstract float makeSuggestion(float rawWaterTurbidity);



    public void setFromJSON(String json) throws JSONException {
        try {
            JSONObject res = new JSONObject(json);
            String str = res.getString("p1");
            p1 = Float.valueOf(str);
            str = res.getString("p2");
            p2 = Float.valueOf(str);
            str = res.getString("rsq");
            rSquared = Float.valueOf(str);
        } catch (NumberFormatException e){
            //turns a Float(#NUM) error into a JSON error (really a JSON error too)
            throw new JSONException(e.getMessage());
        }

    }

    @Override
    public String toString(){
        JSONObject j = new JSONObject();
        try {
            j.put("p1",p1);
            j.put("p2",p2);
            j.put("rsq",rSquared);
        } catch (JSONException e) {
            System.out.println("JSON-ifying the model failed");
            e.printStackTrace();
        }
        return j.toString();
    }

    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put("p1",p1);
            j.put("p2",p2);
            j.put("rsq",rSquared);
        } catch (JSONException e) {
            System.out.println("JSON-ifying the model failed");
            e.printStackTrace();
        }
        return j;
    }

    //this means that models can be equal if they have same params but different model, i.e
    // a log model can be equal to a linear model.
    // TODO: fix this to prevent cross-class equality
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Model.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Model that = (Model) obj;
        if (Math.abs(that.rSquared-this.rSquared) < eps
                && Math.abs(that.p1-this.p1) < eps
                && Math.abs(that.p2-this.p2) < eps)
            return true;
        return false;
    }

}

