package uk.ac.qub.eeecs.game;

/**
 * An abstract data structure which can hold a generic Object
 * type up to a maximum size within heap before cycling round within
 * memory and over-writing the current values stored.
 *
 * @Author Robert Hawkes <40232279>
 */

public abstract class CyclicQueue {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final int DEFAULT_MAX_SIZE = 1;

    protected int maxSize;
    protected int currentSize;
    protected int currentArrayPointer;
    protected Object[] data;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public CyclicQueue(int maxSize){
        if(maxSize > 0) {
            this.maxSize = maxSize;
        }
        else {
            this.maxSize = DEFAULT_MAX_SIZE;
        }

        //Initialise all of the variables
        currentSize = 0;
        currentArrayPointer = 0;
        data = new Object[this.maxSize];
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public int getMaxSize() {
        return this.maxSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    /**
     * This methods adds i into data structure ,ensuring not to write out of bounds
     * and in a cyclic manner
     * @param i The object which you want to store in the data structure
     */
    public void add(Object i){
        //Algorithm
        //1. Test if the current array pointer is larger than max
        //1.1 If it is, we set to the start of heap memory (0)
        //2. Add the object into the data array
        //3. Increment the current position pointer
        //4. Test if we are below the maximum size of the data structure
        //4.1 If so, increment the current size

        //1. Test if the current array pointer is larger than max
        if(currentArrayPointer >= maxSize) {
            //1.1 If it is, we set to the start of heap memory (0)
            currentArrayPointer = 0;
        }

        //2. Add the object into the data array
        data[currentArrayPointer] = i;

        //3. Increment the current position pointer
        currentArrayPointer++;

        //4. Test if we are below the maximum size of the data structure
        if(currentSize < maxSize) {
            //4.1 If so, increment the current size
            currentSize++;
        }
    }

    //Get the index element in the array
    public Object get(int index) {
        //Check if the current size of data is less than the index requested
        if(currentSize <= index) {
            //We would have caused an out of bounds, return null (We can't get that memory)
            return null;
        }
        else {
            return data[index];
        }
    }

    //Get the average of Generic type from the data array
    public abstract <T> T getAverage();

}