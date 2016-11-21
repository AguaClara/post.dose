package org.post.aguaclara.postdose;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by asm278 on 11/16/16.
 */

public abstract class Persistent {

    static final String filename = "myModelfile";


    abstract void setFromJSON(String json);

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
            str = str.replace("\\","").replace("\"","");
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
