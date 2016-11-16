package org.post.aguaclara.postdose;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * This class contains Exponential, Lonear, Power, and Logarithmic Models
 * Created by asm278 on 10/26/16.
 */

public class ModelContainer {

    private ExponentialModel exp;
    private PowerModel pow;
    private LogarithmicModel log;
    private LinearModel lin;

    public Model bestModel;

    public ModelContainer(){
        exp = new ExponentialModel();
        lin = new LinearModel();
        pow = new PowerModel();
        log = new LogarithmicModel();
        setBestModel();
    }

    /**
     * This constructs the ModelContainer from a json string of:
     * {linear:{p1:v1,...},exponential:{p1:v1,...},power:{p1:v1,...},logarithmic:{p1:v1,...},}
     * @param json
     */
    public ModelContainer(String json){
        exp = new ExponentialModel();
        lin = new LinearModel();
        pow = new PowerModel();
        log = new LogarithmicModel();
        setFromJSON(json);
        setBestModel();
    }

    public float getBestDosageRecommendation(float rawWaterTurb){
        return bestModel.makeSuggestion(rawWaterTurb);
    }

    private void setBestModel(){
        float bestRSq = Math.max(Math.max(exp.rSquared,
                                        pow.rSquared),
                                Math.max(log.rSquared,
                                        lin.rSquared));
        if (log.rSquared >= bestRSq - 0.001)
            bestModel = log;
        if (pow.rSquared >= bestRSq - 0.001)
            bestModel = pow;
        if (exp.rSquared >= bestRSq - 0.001)
            bestModel = exp;
        if (lin.rSquared >= bestRSq - 0.001)
            bestModel = lin;
    }

    public void setFromJSON(String json){
        try {
            JSONObject res = new JSONObject(json);
            try {
                String str = res.getString("exponential");
                exp.setFromJSON(str);
            } catch (JSONException e){
                e.printStackTrace();
            }
            try {
                String str = res.getString("linear");
                lin.setFromJSON(str);
            } catch (JSONException e){
                e.printStackTrace();
            }
            try {
                String str = res.getString("power");
                pow.setFromJSON(str);
            } catch (JSONException e){
                e.printStackTrace();
            }
            try {
                String str = res.getString("logarithmic");
                log.setFromJSON(str);
            } catch (JSONException e){
                e.printStackTrace();
            }

        } catch (JSONException e) {
            System.err.println("BAD JSON RECVD!");
            e.printStackTrace();
        }
        setBestModel();
    }

    @Override
    public String toString(){
        return this.toJSON();
    }

    public String toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put("exponential",exp.toJSON());
            j.put("linear",lin.toJSON());
            j.put("power",pow.toJSON());
            j.put("logarithmic",log.toJSON());
        } catch (JSONException e) {
            System.out.println("JSON-ifying the model failed");
            e.printStackTrace();
        }
        return j.toString();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!ModelContainer.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final ModelContainer that = (ModelContainer) obj;
        if (((Model)this.exp).equals(that.exp)
                && this.lin.equals(that.lin)
                && this.log.equals(that.log)
                && this.pow.equals(that.pow))
            return true;
        return false;
    }

}
