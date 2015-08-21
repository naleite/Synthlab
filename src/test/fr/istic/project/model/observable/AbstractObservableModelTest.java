package fr.istic.project.model.observable;

import fr.istic.project.model.module.filter.FilterAttenuation;
import fr.istic.project.model.module.mix.JSynMix;
import fr.istic.project.model.module.mix.Mix;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Observer;

import static org.junit.Assert.*;

public class AbstractObservableModelTest {

    private AbstractObservableModel abstractObservableModel;

    private FilterAttenuation filter1;
    private Mix mix;
    private Observer obs;
    @Before
    public void setUp() throws Exception {
        abstractObservableModel = new AbstractObservableModel();
        mix = new JSynMix();
        mix.setGainIn1(5);
        filter1 = new FilterAttenuation(mix.getGainIn1());
        obs = (o, arg) -> filter1.setAttenuationDB(mix.getGainIn1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetObserversNull() throws Exception {
        abstractObservableModel.getObservers(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObserverExist() throws Exception{
        abstractObservableModel.addObserver(PropertyType.AMF_CHANGED, obs);
        abstractObservableModel.addObserver(PropertyType.AMF_CHANGED, obs);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testAddObserverNull() throws Exception {
        abstractObservableModel.addObserver(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObserverObserverNull() throws Exception {
        PropertyType p = PropertyType.AMF_CHANGED;
        abstractObservableModel.addObserver(p, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverNotExist() throws Exception {
        abstractObservableModel.removeObserver(PropertyType.AMF_CHANGED, obs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverNull() throws Exception {
        abstractObservableModel.removeObserver(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverONull() throws Exception {
        PropertyType p = PropertyType.AMF_CHANGED;
        abstractObservableModel.removeObserver(p, null);
    }

    @Test
    public void testRemoveObserver() throws Exception {
        abstractObservableModel.addObserver(PropertyType.IN1_CHANGED, obs);
        abstractObservableModel.removeObserver(PropertyType.IN1_CHANGED, obs);
        assertEquals(abstractObservableModel.getObservers(PropertyType.IN1_CHANGED), new HashSet<Observer>());
    }

    @Test
    public void testFirePropertyChange() throws Exception {
        abstractObservableModel.firePropertyChange(PropertyType.AMF_CHANGED, 5, 2);
        System.out.println(PropertyType.AMF_CHANGED.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFirePropertyChangeNull() throws Exception {
        abstractObservableModel.firePropertyChange(null, 5, 2);
    }

    @Test
    public void testFirePropertyChangeValueEquals() throws Exception {
        abstractObservableModel.firePropertyChange(PropertyType.AMF_CHANGED, 2 , 2);
    }

    @Test
    public void testFirePropertyChangeValueNull() throws Exception {
        abstractObservableModel.firePropertyChange(PropertyType.AMF_CHANGED, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFirePropertyChangeNull2() throws Exception {
        abstractObservableModel.firePropertyChange(null, 5);
    }
}