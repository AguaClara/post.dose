package org.post.aguaclara.postdose;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by asm278 on 10/26/16.
 */

public class ModelContainer {
    static final String filename = "myModelfile";

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

    public float getBestDosageRecommendation(float rawWaterTurb){
        return bestModel.makeSuggestion(rawWaterTurb);
    }

    private void setBestModel(){
        float bestRSq = Math.max(Math.max(exp.rSquared,
                                        pow.rSquared),
                                Math.max(log.rSquared,
                                        lin.rSquared));
        if (lin.rSquared >= bestRSq - 0.001)
            bestModel = lin;
        if (log.rSquared >= bestRSq - 0.001)
            bestModel = log;
        if (pow.rSquared >= bestRSq - 0.001)
            bestModel = pow;
        if (exp.rSquared >= bestRSq - 0.001)
            bestModel = exp;
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
