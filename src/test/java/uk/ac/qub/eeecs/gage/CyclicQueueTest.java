package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.game.CyclicQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CyclicQueueTest {

    /**
     * Cyclic Queue test using generics
     *
     * @Author Robert Hawkes <40232279>
     */
    @Mock
    CyclicQueue cyclicQueue;

    public void setUp(int maxSize) {
        cyclicQueue = new CyclicQueue(maxSize) {
            @Override
            public <T> T getAverage() {
                return null;
            }
        };
    }

    @Test
    public void CyclicQueue_constructorCorrectData() {
        int maxSize = 5;
        setUp(maxSize);
        assertEquals(maxSize, cyclicQueue.getMaxSize());
    }

    @Test
    public void CyclicQueue_constructorIncorrectData() {
        int maxSize = -1;
        setUp(maxSize);

        assertEquals(cyclicQueue.getMaxSize(), cyclicQueue.DEFAULT_MAX_SIZE);
    }

    @Test
    public void CyclicQueue_canAddItem() {
        int maxSize = 1;
        setUp(maxSize);
        Object value = new Object();
        cyclicQueue.add(value);

        assertEquals(value, cyclicQueue.get(0));
    }

    @Test
    public void CyclicQueue_canAddItem_maxSizeReached() {
        int maxSize = 1;
        setUp(maxSize);
        Object value = new Object();
        cyclicQueue.add(value);
        Object value2 = new Object();
        cyclicQueue.add(value2);
        assertEquals(value2, cyclicQueue.get(0));
    }

    @Test
    public void CyclicQueue_getOldestValue() {
        int maxSize = 2;
        setUp(maxSize);
        Object value = new Object();
        cyclicQueue.add(value);
        Object value2 = new Object();
        cyclicQueue.add(value2);
        assertEquals(value, cyclicQueue.get(0));
    }

    @Test
    public void CyclicQueue_getYoungestValue() {
        int maxSize = 2;
        setUp(maxSize);
        Object value = new Object();
        cyclicQueue.add(value);
        Object value2 = new Object();
        cyclicQueue.add(value2);
        assertEquals(value2, cyclicQueue.get(cyclicQueue.getCurrentSize()-1));
    }

    @Test
    public void CyclicQueue_getValueOutsideBounds() {
        int maxSize = 1;
        int indexToGet = 2;
        setUp(maxSize);
        Object value = new Object();
        cyclicQueue.add(value);
        assertEquals(null, cyclicQueue.get(indexToGet));
        assertNotEquals(value, cyclicQueue.get(indexToGet));
    }
}

