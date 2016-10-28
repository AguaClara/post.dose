package org.post.aguaclara.postdose;

import org.json.JSONException;
import org.json.JSONObject;
import org.post.aguaclara.postdose.Model;

/**
 * Created by asm278 on 10/26/16.
 */

public class LogarithmicModel extends Model {
        //p1 is constant term
        //p2 is multiplicative term

        //y=p1+p2lnx
        public float makeSuggestion(float rawWaterTurbidity){
            return p1 + p2 * (float)Math.log(rawWaterTurbidity);
        }


}
