package uk.ac.qub.eeecs.game.cardDemo;


public class GameTurnSystem {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////
    private static int MAXMANA = 10;
    private int mStartingMana = 1;
    private int mManaThisturn;
    private int mTurnNumber;
    private int mTurnPart;
    private Board mBoard;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * the game turn system
     * Created by &Justin johnston <40237507>
     * @version 1.1
     */

    public GameTurnSystem(Board board){
       mTurnNumber = 1;
       mBoard = board;
       mTurnPart = 0;
       mManaThisturn = mStartingMana;

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters // Setters
    // /////////////////////////////////////////////////////////////////////////


    public int getManaThisturn() {
        return mManaThisturn;
    }

    public int getTurnNumber() {
        return mTurnNumber;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    /**
     * resets cards on the board to their correct state at the start of a players turn
     * draws a card if possible for that player
     * Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public boolean switchTurn(Deck deck,PlayerTurn player,GameLogic gameLogic){
        int handPlacementLocation = 0;
        int playerDeckSpace = mBoard.PLAYER1BOARDDECKSPACE ;
        if(player == PlayerTurn.OPPONENT){
            handPlacementLocation += mBoard.PLAYER2HANDSPACE;
            playerDeckSpace = mBoard.PLAYER2BOARDDECKSPACE;
        }
        int i = 0;
        while(i < deck.getActiveCards().size()){
            deck.getActiveCards().get(i).setMovable(true);
            if(deck.getActiveCards().get(i).isInHand()){
                deck.getActiveCards().get(i).setPosition(mBoard.getBoardComponents().get(handPlacementLocation).position.x,mBoard.getBoardComponents().get(handPlacementLocation).position.y);
                handPlacementLocation++;
            }
            i++;
        }

        boolean cardReturned = deck.drawCard(mBoard.getBoardComponents().get(playerDeckSpace).position.x,mBoard.getBoardComponents().get(playerDeckSpace).position.y);
        if(cardReturned){
            new CardMove(deck.getActiveCards().get(i).position,mBoard.getBoardComponents().get(handPlacementLocation).position,mBoard,i,gameLogic,false,player);
        }else{
            increaseTurnCounter();
            return false;
        }
        increaseTurnCounter();
        return true;
    }

    /**
     * updates the turn counter after both players have taken their turn
     * Not used
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    private void increaseTurnCounter() {
        if(mTurnPart == 1){
            mTurnNumber++;
            mTurnPart = 0;
            if(mManaThisturn < MAXMANA){
                mManaThisturn++;
            }
        }else{
            mTurnPart++;
        }
    }

    /**
     * removes cards which have been killed and adds them
     * to the discard pile
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    public void cleanBoard(Deck deck,int Player){
        int discardSpace;
        if(Player == 1){
            discardSpace = mBoard.PLAYER1DISCARDSPACE;
        }else{
            discardSpace = mBoard.PLAYER2DISCARDSPACE;
        }
        for(Card card:deck.getActiveCards()){
            if(card.getHealth() == 0){
                deck.addToDiscardPile(card);
                card.setPosition(mBoard.getBoardComponents().get(discardSpace).position.x,mBoard.getBoardComponents().get(discardSpace).position.y);
            }
        }
        deck.getActiveCards().removeAll(deck.getDiscardPile());
        deck.getActiveDefenderCards().removeAll(deck.getDiscardPile());
    }

}
