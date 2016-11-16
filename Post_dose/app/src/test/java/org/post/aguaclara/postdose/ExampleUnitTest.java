package org.post.aguaclara.postdose;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.ServiceTestCase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
//@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    public final static float eps = (float) 0.005;


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void toJSONIsRight(){
        ModelContainer m = new ModelContainer();
        assertTrue(m.toString().equals("{\"exponential\":{\"p1\":0,\"rsq\":0,\"p2\":0},\"linear\":{\"p1\":0,\"rsq\":0,\"p2\":0},\"power\":{\"p1\":0,\"rsq\":0,\"p2\":0},\"logarithmic\":{\"p1\":0,\"rsq\":0,\"p2\":0}}"));
    }

    @Test
    public void modelsToFromString(){
        ModelContainer m = new ModelContainer();
        String m_gt = "{\"exponential\":{\"p1\":0,\"rsq\":0.5,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":1,\"p2\":1},\"power\":{\"p1\":1,\"rsq\":0,\"p2\":10},\"logarithmic\":{\"p1\":4,\"rsq\":0.5,\"p2\":0}}";
        m.setFromJSON(m_gt);
        assertEquals(m_gt,m.toString());
    }

    @Test
    public void bestModel(){
        ModelContainer m = new ModelContainer();
        String m_gt = "{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}";
        m.setFromJSON(m_gt);
        assertEquals(m.bestModel.rSquared, 0.75, eps);
        assertEquals(m.bestModel.p1, 10, eps);
        assertEquals(m.bestModel.p2, 2, eps);
    }

    @Test
    public void testEquality(){
        ModelContainer m = new ModelContainer();
        String m_gt = "{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}";
        m.setFromJSON(m_gt);
        assertTrue(m.equals(m));
        ModelContainer n = new ModelContainer();
        n.setFromJSON(m_gt);
        assertTrue(m.equals(n));
        String n_gt_diff = "{\"exponential\":{\"p1\":1,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}";
        n.setFromJSON(n_gt_diff);
        assertFalse(m.equals(n));
    }

    @Test
    public void testPlantModelContainer(){
        PlantModelContainer pmc = new PlantModelContainer();
        String m_gt = "{\"general\":{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}}";
        pmc.setFromJSON(m_gt);
        PlantModelContainer pmc2 = new PlantModelContainer(m_gt);
        assertTrue(pmc.equals(pmc2));
        assertTrue(pmc2.equals(pmc));
        assertTrue(pmc2.equals(pmc2));
        String m_gt_two = "{\"moroceli\":{\"exponential\":{\"p1\":100,\"rsq\":1.75,\"p2\":1},\"linear\":{\"p1\":1,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}," +
                "\"general\":{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}}";
        pmc2.setFromJSON(m_gt_two);
        System.out.println(pmc);
        System.out.println(pmc2);
        assertFalse(pmc.equals(pmc2));
    }
}