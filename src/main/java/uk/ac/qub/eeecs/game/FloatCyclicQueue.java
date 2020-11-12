package uk.ac.qub.eeecs.game;

/**
 * A derived Data structure class which
 * extends upon the super class to store
 * and interact with floats.
 *
 * This class is then used within GamePerformanceScreen
 * as a way of calculating the average Frames Per Second for the
 * player at the end
 *
 * @Author Robert Hawkes <40232279>
 */

public class FloatCyclicQueue extends CyclicQueue {

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public FloatCyclicQueue(int maxSize) {
        super(maxSize);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Calculates the average of the floats inside the data structure
     * @return Return an object castable to float of the average value of all values inside the data structure
     */
    @Override
    public Object getAverage() {
        float sum = 0.0f;
        for (int i = 0; i < this.currentSize; i++) {
            sum += (float)data[i];
        }

        return sum / this.currentSize;
    }
}
