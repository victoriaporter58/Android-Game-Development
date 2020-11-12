package uk.ac.qub.eeecs.game.cardDemo;

public class AI {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////
    private int mBoardstrengththreshold =1;
    private int mManaththreshold =8;
    private boolean mTurnComplete;
    // storage for the game objects
    private Board mBoard;
    private Deck mAIDeck;
    private Deck mOpponentDeck;
    private  Player mAIPlayer;
    private  Player mOpponent;
    //storage for the AI needs calculations
    private int mHealthDifference;
    private int mBoardStrengthRemaining;
    private int mBoardStrength;
    private int mOpponentBoardStrength;
    private int mRelativeBoardStrength;
    private Boolean mHandMovesComplete;
    private Boolean mBoardMovesComplete;
    private Boolean mCanWinThisTurn;
    private Boolean mDefendersOnOpponentsBoard;
    private Boolean mFirstTurn;
    private Boolean mBoardFull;
    private boolean mConserveMana;
    private int mBoardSpaceIndex;
    private int mOpponentsAvatarSpace;

    private AIMode mAIMode;
    private int mLowestCardCost;
    private int mLowestDefendCardCost;
    private int mLowestRushCardCost;
    private int mLowestManaCardCost;
    private boolean mHandContainsPlayableDefendCard;
    private boolean mHandContainsPlayableRushCard;
    private boolean mHandContainsPlayableManaCard;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * a basic AI, designed to gather information about the game state and make
     * decisions based on those findings
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public AI(Board board,Deck AIDeck,Deck opponentDeck,Player AIPlayer, Player opponent, int player){
        mTurnComplete = false;
        mBoard = board;
        mAIDeck = AIDeck;
        mOpponentDeck = opponentDeck;
        mOpponent = opponent;
        mAIPlayer = AIPlayer;
        mBoardMovesComplete = false;
        mHandMovesComplete = false;
        mTurnComplete = false;
        mCanWinThisTurn = false;
        mDefendersOnOpponentsBoard = false;
        mFirstTurn = true;
        mHealthDifference = 0;
        mConserveMana = false;
        mAIMode = AIMode.DEFENSIVE;
        if(player == 1){
            mBoardSpaceIndex = Board.PLAYER1BOARDSPACE;
            mOpponentsAvatarSpace = Board.PLAYER2AVATARSPACE;
        }else{
            mBoardSpaceIndex = Board.PLAYER2BOARDSPACE;
            mOpponentsAvatarSpace = Board.PLAYER1AVATARSPACE;
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters // Setters
    // /////////////////////////////////////////////////////////////////////////
    public boolean isTurnComplete() {
        return mTurnComplete;
    }

    public void setTurnComplete(boolean mTurnComplete) {
        this.mTurnComplete = mTurnComplete;
    }

    public int getHealthDifference() {
        return mHealthDifference;
    }

    public AIMode getAIMode() {
        return mAIMode;
    }

    public int getLowestDefendCardCost() {
        return mLowestDefendCardCost;
    }

    public int getLowestRushCardCost() {
        return mLowestRushCardCost;
    }

    public int getLowestManaCardCost() {
        return mLowestManaCardCost;
    }

    public boolean doesHandContainPlayableDefendCard() {
        return mHandContainsPlayableDefendCard;
    }

    public boolean doesHandContainPlayableRushCard() {
        return mHandContainsPlayableRushCard;
    }

    public boolean doesHandContainPlayableManaCard() {
        return mHandContainsPlayableManaCard;
    }

    public Boolean getHandMovesComplete() {
        return mHandMovesComplete;
    }

    public Boolean getBoardMovesComplete() {
        return mBoardMovesComplete;
    }


    public Boolean isFirstTurn() {
        return mFirstTurn;
    }

    public void setFirstTurn(Boolean mFirstTurn) {
        this.mFirstTurn = mFirstTurn;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Take Turn gathers information about the board and then selects the most
     * relevant move from the returned information
     * it returns a store of coordinates which can be converted to a cardMove
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public AIMoveInformation takeTurn() {
        gatherInformation();
        AIMoveInformation move = null;
        if (!mTurnComplete) {
            switch (mAIMode){
                case DEFENSIVE:move =takeDefensiveTurn();break;
                case AGGRESSIVE:move =takeAggressiveTurn();break;
            }
        }
        return move;
    }

    /**
     * makes a more defensive approach to playing cards
     * preferring to play defend cards
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public AIMoveInformation takeDefensiveTurn() {
        AIMoveInformation move = null;
        if(!mHandMovesComplete){
            if(mHandContainsPlayableDefendCard){
                move = playCard(AbilityType.DEFEND);
            }else if(mHandContainsPlayableManaCard){
                move = playCard(AbilityType.MANA);
            }else if(mHandContainsPlayableRushCard){
                move = playCard(AbilityType.RUSH);
            }else{
                move = playCard(AbilityType.NONE);
            }
        }else if(!mBoardMovesComplete){
            if(mCanWinThisTurn &&!mDefendersOnOpponentsBoard){
                move = attackOpponent();
            }else if(mDefendersOnOpponentsBoard){
                move = attackOpponentCard();
            }else{
                move = attackOpponent();
            }
        }
        return move;
    }

    /**
     * makes a more aggressive approach to playing cards
     * preferring to play rush cards,will conserve mana if
     * dominating board.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public AIMoveInformation takeAggressiveTurn() {
        AIMoveInformation move = null;
        if(!mHandMovesComplete){
            if(mConserveMana){
                mHandMovesComplete = true;
            }else if(mHandContainsPlayableRushCard){
                move = playCard(AbilityType.RUSH);
            }else if(mHandContainsPlayableManaCard){
                move = playCard(AbilityType.MANA);
            }else if(mHandContainsPlayableDefendCard&&(mLowestCardCost==mLowestDefendCardCost)){
                move = playCard(AbilityType.DEFEND);
            }else{
                move = playCard(AbilityType.NONE);
            }
        }else if(!mBoardMovesComplete){
            if(mCanWinThisTurn &&!mDefendersOnOpponentsBoard){
                move = attackOpponent();
            }else if((mBoardStrength <= mOpponentBoardStrength)||mDefendersOnOpponentsBoard){
                move = attackOpponentCard();
            }else{
                move = attackOpponent();
            }
        }
        return move;
    }
    /**
     * Start turn resets the booleans which control
     * the end of the turn to their default state
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void startTurn(){
        mBoardMovesComplete = false;
        mHandMovesComplete = false;
        mTurnComplete = false;
    }

    /**
     * Information is gathered from the board to facilitate decisions about the next move
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void gatherInformation(){
        mHealthDifference = mAIPlayer.getHealth() - mOpponent.getHealth();
        mLowestCardCost = calculateLowestCardCost(mAIDeck);
        mBoardStrengthRemaining = calculateBoardStrength(mAIDeck,true);
        mBoardStrength= calculateBoardStrength(mAIDeck,false);
        mOpponentBoardStrength = calculateBoardStrength(mOpponentDeck,false);
        mRelativeBoardStrength = mBoardStrength - mOpponentBoardStrength;
        mCanWinThisTurn = mBoardStrengthRemaining >= mOpponent.getHealth();
        mBoardFull = (mAIDeck.getActiveCards().size() - mAIDeck.getCardsInHand()) >= Board.BOARDCAPACITY;
        if(mLowestCardCost >= mAIPlayer.getMana()|| mBoardFull) mHandMovesComplete = true;
        mBoardMovesComplete = mBoardStrengthRemaining == 0;
        if(mHandMovesComplete && mBoardMovesComplete) mTurnComplete = true;
        mDefendersOnOpponentsBoard = mOpponentDeck.getActiveDefenderCards().size() > 0;
        mConserveMana = ((mRelativeBoardStrength > mBoardstrengththreshold) && !isFirstTurn()) && mAIPlayer.getMana() < mManaththreshold;
        mAIMode = (mOpponentBoardStrength -mBoardStrength > mBoardstrengththreshold)?AIMode.DEFENSIVE:AIMode.AGGRESSIVE;
    }

    /**
     * find the lowest mana cost minion in the deck
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public int calculateLowestCardCost(Deck deck){
        int lowestCost = Integer.MAX_VALUE;
        mLowestDefendCardCost =Integer.MAX_VALUE;
        mLowestManaCardCost = Integer.MAX_VALUE;
        mLowestRushCardCost = Integer.MAX_VALUE;
        mHandContainsPlayableDefendCard = false;
        mHandContainsPlayableManaCard = false;
        mHandContainsPlayableRushCard =false;
        for(Card card:deck.getActiveCards()){
            if(card.getCost() < lowestCost && card.isInHand()) lowestCost = card.getCost();
            if(card.getCost() < mLowestDefendCardCost && card.isInHand() && card.getAbility()==AbilityType.DEFEND) mLowestDefendCardCost = card.getCost();
            if(card.getCost() < mLowestRushCardCost && card.isInHand() && card.getAbility()==AbilityType.RUSH) mLowestRushCardCost = card.getCost();
            if(card.getCost() < mLowestManaCardCost && card.isInHand() && card.getAbility()==AbilityType.MANA) mLowestManaCardCost = card.getCost();
        }
        if(mLowestDefendCardCost < mAIPlayer.getMana())mHandContainsPlayableDefendCard = true;
        if(mLowestRushCardCost < mAIPlayer.getMana())mHandContainsPlayableRushCard = true;
        if(mLowestManaCardCost < mAIPlayer.getMana())mHandContainsPlayableManaCard = true;
        return lowestCost;
    }

    public int calculateBoardStrength(Deck deck,boolean countMovable){
        int boardStrength = 0;
        for(Card card: deck.getActiveCards()){
            if(!card.isInHand()){
                if(countMovable){
                    if(card.isMovable())boardStrength+=card.getAttack();
                }else{
                    boardStrength+=card.getAttack();
                }
            }

        }
        return boardStrength;
    }

    /**
     * playCard chooses a card to play from the hand to the board
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private AIMoveInformation  playCard(AbilityType abilityType){
        boolean cardFound = false;
        boolean spacefound = false;
        int cardIndex =findCardToPlay(CardLocation.HAND,abilityType);
        if(cardIndex != -1)cardFound=true;
        int boardSpaceIndex = findBoardSpace();
        if(boardSpaceIndex !=-1) spacefound=true;
        if (spacefound && cardFound) {
            if(mAIDeck.getActiveCards().get(cardIndex).getAbility() == AbilityType.MANA)mBoardMovesComplete = false;
            return new  AIMoveInformation(mAIDeck.getActiveCards().get(cardIndex).position, mBoard.getBoardComponents().get(boardSpaceIndex).position,cardIndex);
        }
        return null;
    }

    /**
     * playCard creates a cardmove from the board which attacks the opponents avatar
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private AIMoveInformation  attackOpponent(){
        boolean cardFound = false;
        int cardIndex = findCardToPlay(CardLocation.BOARD,AbilityType.NONE);
        if(cardIndex != -1)cardFound=true;
        if (cardFound) {
            return new AIMoveInformation(mAIDeck.getActiveCards().get(cardIndex).position, mBoard.getBoardComponents().get(mOpponentsAvatarSpace).position,cardIndex);
        }
        return null;
    }

    /**
     * playCard creates a cardmove from the board which attacks the opponents cards on the board
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private AIMoveInformation  attackOpponentCard(){
        boolean cardFound = false;
        boolean targetFound = false;
        int targetIndex=findTarget();
        int cardIndex =findCardToPlay(CardLocation.BOARD,AbilityType.NONE);
        if(cardIndex != -1)cardFound=true;
        if(targetIndex != -1)targetFound=true;

        if (cardFound && targetFound) {
            return new AIMoveInformation(mAIDeck.getActiveCards().get(cardIndex).position, mOpponentDeck.getActiveCards().get(targetIndex).position,cardIndex);
        }
        return null;
    }

    /**
     * finds a card on the players board or hand that is available to use
     * returns the index for that card
     * public only for testing
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public int findCardToPlay(CardLocation cardLocation, AbilityType abilityType){

        boolean cardFound = false;
        int cardIndex = -1;

        for(int i = 0; i< mAIDeck.getActiveCards().size(); i++){
            if(cardLocation == CardLocation.BOARD){
                if(!mAIDeck.getActiveCards().get(i).isInHand()&& mAIDeck.getActiveCards().get(i).isMovable()) {
                    cardFound = true;
                    cardIndex = i;
                }
                if(cardFound) break;
            }else{
                if(mAIDeck.getActiveCards().get(i).isInHand()){
                    if(mAIPlayer.getMana() >= mAIDeck.getActiveCards().get(i).getCost()&& mAIDeck.getActiveCards().get(i).getAbility()==abilityType){
                        cardFound = true;
                        cardIndex = i;
                    }
                }
            }

        }
        return cardIndex;
    }

    /**
     * finds a card on the opponents board that can be attacked
     * chooses a defender if the opponent has any on the board
     * returns the index for that card
     * public only for testing
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public int findTarget(){
        boolean targetFound = false;
        int targetIndex = -1;
        for(int i = 0; i< mOpponentDeck.getActiveCards().size(); i++){
            if(mDefendersOnOpponentsBoard){
                if(!mOpponentDeck.getActiveCards().get(i).isInHand()&& mOpponentDeck.getActiveCards().get(i).getAbility() == AbilityType.DEFEND) {
                    targetFound = true;
                    targetIndex = i;
                }
            }else{
                if(!mOpponentDeck.getActiveCards().get(i).isInHand()) {
                    targetFound = true;
                    targetIndex = i;
                }
            }
            if(targetFound) break;
        }
        return targetIndex;
    }

    /**
     * finds a slot on the players side of the board
     * returns the index for that board slot if one exists or -1
     * public only for testing
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public int findBoardSpace(){
        boolean spacefound = false;
        int boardSpaceIndex = mBoardSpaceIndex;

        for(int i = mBoardSpaceIndex; i < mBoardSpaceIndex+ Board.BOARDCAPACITY; i++){
            spacefound = true;
            boardSpaceIndex = i;
            for(int n = 0; n < mAIDeck.getActiveCards().size(); n++){
                if(mBoard.getBoardComponents().get(i).getBound().intersects(mAIDeck.getActiveCards().get(n).getBound())){
                    spacefound = false;
                }
            }
            if(spacefound) break;
        }
        if(spacefound)return boardSpaceIndex;
        return -1;
    }

    public enum CardLocation {HAND,BOARD}
    public enum AIMode {AGGRESSIVE,DEFENSIVE}

}
