package org.post.aguaclara.postdose;

/**
 * Created by asm278 on 10/26/16.
 */

public class LinearModel extends Model {
    //p1 is slope of the line
    //p2 is the y-intercept

    //y=p1x+p2
    public float makeSuggestion(float rawWaterTurbidity) {
        return p1 * rawWaterTurbidity + p2;
    }
}
