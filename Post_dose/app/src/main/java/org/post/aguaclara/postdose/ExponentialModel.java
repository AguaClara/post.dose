package org.post.aguaclara.postdose;

/**
 * Created by asm278 on 10/26/16.
 */

public class ExponentialModel extends Model {
    //p1 is multiplicative term
    //p2 is exponential trm

    //y=p1*e^(xp2)
    public float makeSuggestion(float rawWaterTurbidity){
        return p1 * (float)Math.pow(rawWaterTurbidity * p2, Math.E);
    }
}
