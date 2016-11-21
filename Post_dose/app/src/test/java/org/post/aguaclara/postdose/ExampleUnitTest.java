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
    public void modelsToFromString() {
        ModelContainer m = new ModelContainer();
        String m_gt = "{\"exponential\":{\"p1\":0,\"rsq\":0.5,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":1,\"p2\":1},\"power\":{\"p1\":1,\"rsq\":0,\"p2\":10},\"logarithmic\":{\"p1\":4,\"rsq\":0.5,\"p2\":0}}";
        m.setFromJSON(m_gt);
        assertEquals(m_gt, m.toString());
    }

    @Test
    public void bestModel() {
        ModelContainer m = new ModelContainer();
        String m_gt = "{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}";
        m.setFromJSON(m_gt);
        assertEquals(m.bestModel.rSquared, 0.75, eps);
        assertEquals(m.bestModel.p1, 10, eps);
        assertEquals(m.bestModel.p2, 2, eps);
    }

    @Test
    public void testEquality() {
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
    public void testPlantModelContainer() {
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

    @Test
    public void testPlantModelContainerWithRealBadResult() {
        String result = "{\"Other\":\"{\\\"logarithmic\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"exponential\\\":{\\\"p2\\\":0,\\\"p1\\\":7.389056205749512,\\\"rsq\\\":0},\\\"power\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"linear\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1}}\",\"Nicolas\":\"{\\\"logarithmic\\\":{\\\"p2\\\":2.6023318767547607,\\\"p1\\\":-2.5319900512695313,\\\"rsq\\\":0.7337256073951721},\\\"exponential\\\":{\\\"p2\\\":0.011426271870732307,\\\"p1\\\":4.13126277923584,\\\"rsq\\\":0.7666018605232239},\\\"power\\\":{\\\"p2\\\":0.4010891318321228,\\\"p1\\\":1.5575275421142578,\\\"rsq\\\":0.7796077728271484},\\\"linear\\\":{\\\"p2\\\":3.54314923286438,\\\"p1\\\":0.0799010694026947,\\\"rsq\\\":0.8057294487953186}}\",\"moroceli\":\"{\\\"logarithmic\\\":{\\\"p2\\\":4.511453628540039,\\\"p1\\\":0.8422552347183228,\\\"rsq\\\":0.02740848809480667},\\\"exponential\\\":{\\\"p2\\\":0.002712170360609889,\\\"p1\\\":15.742383003234863,\\\"rsq\\\":-0.08050931245088577},\\\"power\\\":{\\\"p2\\\":0.4277983605861664,\\\"p1\\\":3.078500986099243,\\\"rsq\\\":0.029451290145516396},\\\"linear\\\":{\\\"p2\\\":8.530984878540039,\\\"p1\\\":0.07372988015413284,\\\"rsq\\\":0.01655460335314274}}\",\"San\":\"{\\\"logarithmic\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"exponential\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"power\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"linear\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1}}\",\"Otoro\":\"{\\\"logarithmic\\\":{\\\"p2\\\":2.6666646003723145,\\\"p1\\\":5.78447961807251,\\\"rsq\\\":0.0027328478172421455},\\\"exponential\\\":{\\\"p2\\\":-0.0038481582887470722,\\\"p1\\\":17.44751739501953,\\\"rsq\\\":-0.0364011712372303},\\\"power\\\":{\\\"p2\\\":0.32295337319374084,\\\"p1\\\":5.046838760375977,\\\"rsq\\\":9.510256350040436E-4},\\\"linear\\\":{\\\"p2\\\":9.139235496520996,\\\"p1\\\":0.18717898428440094,\\\"rsq\\\":0.0022642253898084164}}\",\"general\":\"{\\\"logarithmic\\\":{\\\"p2\\\":4.293959140777588,\\\"p1\\\":1.3604501485824585,\\\"rsq\\\":0.02324685826897621},\\\"exponential\\\":{\\\"p2\\\":0.0027001940179616213,\\\"p1\\\":15.680042266845703,\\\"rsq\\\":-0.07221385836601257},\\\"power\\\":{\\\"p2\\\":0.4091074764728546,\\\"p1\\\":3.3046634197235107,\\\"rsq\\\":0.024765409529209137},\\\"linear\\\":{\\\"p2\\\":8.687776565551758,\\\"p1\\\":0.07299977540969849,\\\"rsq\\\":0.01448921300470829}}\",\"Atima\":\"{\\\"logarithmic\\\":{\\\"p2\\\":0,\\\"p1\\\":2,\\\"rsq\\\":0},\\\"exponential\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1},\\\"power\\\":{\\\"p2\\\":1,\\\"p1\\\":1,\\\"rsq\\\":0},\\\"linear\\\":{\\\"p2\\\":0,\\\"p1\\\":0,\\\"rsq\\\":-1}}\"}";
        result = result.replace("\\","").replace("\"","");
        PlantModelContainer pmc = new PlantModelContainer();
        pmc.setFromJSON(result);

        PlantModelContainer pmc2 = new PlantModelContainer(result);
        assertTrue(pmc.equals(pmc2));
        assertTrue(pmc2.equals(pmc));
        assertTrue(pmc2.equals(pmc2));
        System.out.println("pmc:" + pmc);
        assertTrue(pmc.getBestDosageRecommendation(15f,"general") > 0);
    }
    @Test
    public void testGetResult() {
        PlantModelContainer pmc = new PlantModelContainer();
        String m_gt = "{\"general\":{\"exponential\":{\"p1\":10,\"rsq\":0.75,\"p2\":2},\"linear\":{\"p1\":10,\"rsq\":0.74,\"p2\":11},\"power\":{\"p1\":3,\"rsq\":0.1,\"p2\":4},\"logarithmic\":{\"p1\":1,\"rsq\":0.0001,\"p2\":3}}}";
        pmc.setFromJSON(m_gt);
        float turb = 0.0f;
        float rec = pmc.getBestDosageRecommendation(turb,"general");
        float result = MainActivity.getResult(""+turb,"general",pmc);
        assertEquals(rec,result);

        rec = pmc.getBestDosageRecommendation(turb,"general");
        result = MainActivity.getResult(""+turb,"ithici",pmc);
        assertEquals(rec,result);
    }

}