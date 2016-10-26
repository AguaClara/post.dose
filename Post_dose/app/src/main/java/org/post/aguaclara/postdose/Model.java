package org.post.aguaclara.postdose;

/**
 * Created by asm278 on 10/6/16.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class Model{
    //TODO make work for more regressions
    private float m;
    private float b;
    static final String filename = "myModelfile";


    public Model(){
        m = 0.0f;
        b = 0.0f;
    }

    public float makeSuggestion(float rawWaterTurbidity){
        return m * rawWaterTurbidity + b;
    }

    public void setFromJSON(String j){
        try {
            JSONObject res = new JSONObject(j);
            try {
                String strm = res.getString("m");
                m = Float.valueOf(strm);
            } catch (JSONException e){}
            try {
                String strb = res.getString("b");
                b = Float.valueOf(strb);
            } catch (JSONException e){}

        } catch (JSONException e) {
            System.err.println("BAD JSON RECVD!");
            e.printStackTrace();
        }
    }

    public void setM(float m){
        this.m = m;
    }

    public float getM() {
        return m;
    }

    public void setB(float b){
        this.b = b;
    }

    public float getB() {
        return b;
    }

    @Override
    public String toString(){
        JSONObject j = new JSONObject();
        try {
            j.put("m",m);
            j.put("b",b);
        } catch (JSONException e) {
            System.out.println("JSON-ifying the model failed");
            e.printStackTrace();
        }
        return j.toString();
    }


    //TODO: Make private
    public void load(FileInputStream inputStream){
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
    //TODO: Make private
    public void save(FileOutputStream outputStream){
        //SAVES THIS MODEL as a JSON
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



    private void saveModel(Model m, Context context){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(Model.filename, Context.MODE_PRIVATE);
            m.save(outputStream);
        } catch (Exception e) {
            System.err.println("No Model File Found in saveModel?");
            e.printStackTrace();
        }
    }
    private Model loadModel(Context context){
        FileInputStream inputStream;
        Model m = new Model();
        try {
            inputStream = context.openFileInput(Model.filename);
            m.load(inputStream);
        } catch (Exception e) {
            System.out.println("No Model File Found loading in main");
            e.printStackTrace();
        }
        return m;
    }

}

