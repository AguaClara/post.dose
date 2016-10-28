package org.post.aguaclara.postdose;

/**
 * Created by asm278 on 10/26/16.
 */

public class PowerModel extends Model {
    //p1 is constant term
    //p2 is power term

    //y=p1+x^p2
    public float makeSuggestion(float rawWaterTurbidity){
        return p1 + (float)Math.pow(rawWaterTurbidity, p2);
    }
}
