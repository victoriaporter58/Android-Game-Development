package uk.ac.qub.eeecs.game.cardDemo;

import uk.ac.qub.eeecs.gage.util.Vector2;

public class CardMove {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private Vector2 mStartPosition;
    private Vector2 mEndPosition;
    private int mStartBoardPiece;
    private int mEndBoardPiece;
    private MoveType mMoveType;
    private boolean mMoveCompleted;
    private boolean mMoveStarted;
    private int mCardHandPosition;
    private boolean mAutoReturn;
    private PlayerTurn mPlayer;
    private Vector2 mMoveVector;
    private Card mOpponentTargetCard;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * the cardmove system is designed to facilitate both human players and an AI to
     * interact with the game world, when a Cardmove is created it is processed here
     * the full info is then sent to the Gamelogic system to create the animation and resource processing
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public CardMove(Vector2 startPosition, Vector2 endPosition, Board board, int cardHandPosition, GameLogic gameLogic, boolean autoReturn,PlayerTurn player){
       float x = startPosition.x;
       float y = startPosition.y;
       mStartPosition = new Vector2(x,y);
       x = endPosition.x;
       y = endPosition.y;
       mEndPosition = new Vector2(x,y);
       mMoveCompleted = false;
       mMoveStarted = false;
       mCardHandPosition = cardHandPosition;
       mAutoReturn = autoReturn;
       mPlayer = player;
       mMoveVector = new Vector2(0,0);
       mOpponentTargetCard = null;
       calculateMoveType(board);
       gameLogic.add(this);
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

    public int getEndBoardPiece() {
        return mEndBoardPiece;
    }

    public MoveType getMoveType() {
        return mMoveType;
    }

    public void setMoveType(MoveType mMoveType) {
        this.mMoveType = mMoveType;
    }

    public boolean isMoveStarted() {
        return mMoveStarted;
    }

    public int getCardHandPosition() {
        return mCardHandPosition;
    }

    public boolean isMoveCompleted() {
        return mMoveCompleted;
    }

    public void setMoveStarted(boolean mMoveStarted) {
        this.mMoveStarted = mMoveStarted;
    }

    public void setMoveCompleted(boolean mMoveCompleted) {
        this.mMoveCompleted = mMoveCompleted;
    }

    public PlayerTurn getPlayer() {
        return mPlayer;
    }

    public Vector2 getMoveVector() {
        return mMoveVector;
    }

    public Card getOpponentTargetCard() {
        return mOpponentTargetCard;
    }

    public void setOpponentTargetCard(Card mOpponentTargetCard) {
        this.mOpponentTargetCard = mOpponentTargetCard;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////
    /**
     * using a set of coordinates for the start and end positions of the intended move
     * this method determines what type of move the player is trying to make by comparing these
     * positions to positions of board components.
     * any move not recognised is determined to be invalid
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void calculateMoveType(Board board){

        for(int i = 0; i < board.getBoardComponents().size(); i++){
            if(board.getBoardComponents().get(i).getBound().contains(mStartPosition.x,mStartPosition.y)) mStartBoardPiece = i;
            if(board.getBoardComponents().get(i).getBound().contains(mEndPosition.x,mEndPosition.y)) mEndBoardPiece = i;
        }
        if(mAutoReturn){
            this.mMoveType = MoveType.RETURN;
        }else{
            if(mStartBoardPiece < Board.PLAYER1BOARDDECKSPACE && mStartBoardPiece >= Board.PLAYER1HANDSPACE &&(mEndBoardPiece < Board.PLAYER1AVATARSPACE && mEndBoardPiece >= Board.PLAYER1BOARDSPACE)
            || (mStartBoardPiece < Board.PLAYER2BOARDDECKSPACE && mStartBoardPiece > Board.PLAYER2AVATARSPACE &&(mEndBoardPiece < Board.PLAYER2AVATARSPACE && mEndBoardPiece >= Board.PLAYER2BOARDSPACE))){
                mMoveType = MoveType.PLAYCARD;
                mEndPosition.set(board.getBoardComponents().get(mEndBoardPiece).position);
            }else if(mStartBoardPiece == Board.PLAYER1BOARDDECKSPACE || (mStartBoardPiece == Board.PLAYER2BOARDDECKSPACE)){
                mMoveType = MoveType.DRAWCARD;
            }else if((mStartBoardPiece < Board.PLAYER1AVATARSPACE && mStartBoardPiece >= Board.PLAYER1BOARDSPACE) && (mEndBoardPiece < Board.PLAYER2AVATARSPACE && mEndBoardPiece >= Board.PLAYER2BOARDSPACE)
            ||(mStartBoardPiece < Board.PLAYER2AVATARSPACE && mStartBoardPiece >= Board.PLAYER2BOARDSPACE) && (mEndBoardPiece < Board.PLAYER1AVATARSPACE && mEndBoardPiece >= Board.PLAYER1BOARDSPACE)){
                mMoveType = MoveType.ATTACKCARD;
            }else if((mStartBoardPiece < Board.PLAYER1AVATARSPACE && mStartBoardPiece >= Board.PLAYER1BOARDSPACE) && (mEndBoardPiece == Board.PLAYER2AVATARSPACE)
            ||(mStartBoardPiece < Board.PLAYER2AVATARSPACE && mStartBoardPiece >= Board.PLAYER2BOARDSPACE) && (mEndBoardPiece == Board.PLAYER1AVATARSPACE)){
                mMoveType = MoveType.ATTACKPLAYER;
            }else {
                mMoveType = MoveType.INVALID;
            }
        }

    }

}
