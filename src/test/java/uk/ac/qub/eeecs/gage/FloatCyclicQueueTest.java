package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.game.FloatCyclicQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FloatCyclicQueueTest {

    /**
     * Float Cyclic Queue test using derived class
     *
     * @Author Robert Hawkes <40232279>
     */
    @Mock
    FloatCyclicQueue floatCyclicQueue;

    public void setUp(int maxSize) {
        floatCyclicQueue = new FloatCyclicQueue(maxSize);
    }

    @Test
    public void floatCyclicQueue_constructorCorrectData() throws Exception {
        int maxSize = 5;
        setUp(maxSize);
        assertEquals(maxSize, floatCyclicQueue.getMaxSize());
    }

    @Test
    public void floatCyclicQueue_constructorIncorrectData() throws Exception {
        int maxSize = -1;
        setUp(maxSize);

        assertEquals(floatCyclicQueue.getMaxSize(), floatCyclicQueue.DEFAULT_MAX_SIZE);
    }

    @Test
    public void floatCyclicQueue_canAddItem() throws Exception {
        int maxSize = 1;
        setUp(maxSize);
        Long value = 1000L;
        floatCyclicQueue.add(value);

        assertEquals(value, floatCyclicQueue.get(0));
    }

    @Test
    public void floatCyclicQueue_canAddItem_maxSizeReached() throws Exception {
        int maxSize = 1;
        setUp(maxSize);
        Long value = 1000L;
        floatCyclicQueue.add(value);
        Long value2 = 5000L;
        floatCyclicQueue.add(value2);
        assertEquals(value2, floatCyclicQueue.get(0));
    }

    @Test
    public void floatCyclicQueue_getOldestValue() throws Exception {
        int maxSize = 2;
        setUp(maxSize);
        Long value = 1000L;
        floatCyclicQueue.add(value);
        Long value2 = 5000L;
        floatCyclicQueue.add(value2);
        assertEquals(value, floatCyclicQueue.get(0));
    }

    @Test
    public void floatCyclicQueue_getYoungestValue() throws Exception {
        int maxSize = 2;
        setUp(maxSize);
        Long value = 1000L;
        floatCyclicQueue.add(value);
        Long value2 = 5000L;
        floatCyclicQueue.add(value2);
        assertEquals(value2, floatCyclicQueue.get(floatCyclicQueue.getCurrentSize()-1));
    }

    @Test
    public void CyclicQueue_getValueOutsideBounds() {
        int maxSize = 1;
        int indexToGet = 2;
        setUp(maxSize);
        Long value = 100L;
        floatCyclicQueue.add(value);
        assertEquals(null, floatCyclicQueue.get(indexToGet));
        assertNotEquals(value, floatCyclicQueue.get(indexToGet));
    }

    @Test
    public void floatCyclicQueue_getAverage_Success() {
        int maxSizeInput = 2;
        float expectedAverage = 0.15f;
        float floatToAdd = 0.1f;
        float floatToAdd2 = 0.2f;

        setUp(maxSizeInput);

        floatCyclicQueue.add(floatToAdd);
        floatCyclicQueue.add(floatToAdd2);

        assertEquals(floatCyclicQueue.getAverage(), expectedAverage);
    }
}
