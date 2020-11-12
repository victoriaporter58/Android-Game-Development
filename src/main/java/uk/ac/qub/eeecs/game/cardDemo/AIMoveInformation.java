package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.util.Vector2;


public class AIMoveInformation {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private Vector2 mStartPosition;
    private Vector2 mEndPosition;
    private int mCardIndex;



    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * small data store to hold move information to create a cardMove for use by the AI
     *  Created by &Justin johnston <40237507>
     *  @param startPosition the start position of the move
     *  @param endPosition the end position of the move
     *  @param cardIndex the position of the card in the List of cards
     * @version 1.0
     */
    public AIMoveInformation(Vector2 startPosition, Vector2 endPosition,int cardIndex){
       float x = startPosition.x;
       float y = startPosition.y;
       mStartPosition = new Vector2(x,y);
       x = endPosition.x;
       y = endPosition.y;
       mEndPosition = new Vector2(x,y);
       mCardIndex = cardIndex;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters // Setters
    // /////////////////////////////////////////////////////////////////////////

    public Vector2 getStartPosition() {
        return mStartPosition;
    }

    public Vector2 getEndPosition() {
        return mEndPosition;
    }

    public int getCardIndex() {
        return mCardIndex;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

}
