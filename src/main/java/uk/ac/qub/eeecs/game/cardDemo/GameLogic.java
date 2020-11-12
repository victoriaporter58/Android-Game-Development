package uk.ac.qub.eeecs.game.cardDemo;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class GameLogic {

    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    private Queue<CardMove> mCardMoves;
    private Board mBoard;
    private  Deck mDeck;
    private  Deck mOpponentsDeck;
    private static int ROTATESPEED = 700;
    private static int ANIMATIONSPEED = 3;
    private static int DRAWANIMATIONSPEED = 7;
    private Player mPlayer;
    private Player mPlayerOpponent;
    private GlobalMessage mGlobalMessage;
    private AudioManager mAudioManager;
    private  GameScreen mGameScreen;
    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * Game logic processes card moves and handles the animation for those moves
     * also handles resources and sound
     * Created by &Justin johnston <40237507>
     * @version 1.1
     */

    public GameLogic(Board board, Deck deck, Deck opponentsDeck, Player currentPlayer, Player opponentPlayer, GlobalMessage globalMessage, AudioManager audiomanager,GameScreen gameScreen){
       mCardMoves = new LinkedTransferQueue<>();
       mBoard = board;
       mDeck = deck;
       mOpponentsDeck = opponentsDeck;
       mPlayer = currentPlayer;
       mPlayerOpponent = opponentPlayer;
       mGlobalMessage = globalMessage;
       mAudioManager = audiomanager;
       mGameScreen = gameScreen;

    }
    // /////////////////////////////////////////////////////////////////////////
    // Getters // Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean isEmpty(){
        return mCardMoves.size() == 0;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * adds a Cardmove to the queue system to be processed, resets cards to their previous location
     * if they are invalid
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void add(CardMove cardMove){
        if(cardMove.getMoveType() == MoveType.INVALID){
            mDeck.getActiveCards().get(cardMove.getCardHandPosition()).setPosition(cardMove.getStartPosition().x,cardMove.getStartPosition().y);
        }else{
            mCardMoves.add(cardMove);
        }
    }


    /**
     * remove completed cardMoves from the Queue
     * creates a return animation for moves which require them
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void clearCompleted(){
        if(mCardMoves.size() > 0){
            for(CardMove cardMove:mCardMoves){
                if(cardMove.isMoveCompleted() || cardMove.getMoveType() == MoveType.INVALID){
                    if((cardMove.getMoveType() == MoveType.ATTACKCARD || cardMove.getMoveType() == MoveType.ATTACKPLAYER) && cardMove.isMoveCompleted() ){
                        new CardMove(cardMove.getEndPosition(),cardMove.getStartPosition(),mBoard,cardMove.getCardHandPosition(),this,true, cardMove.getPlayer());
                    }
                    mCardMoves.remove(cardMove);

                }
            }
        }
    }

    /**
     * a switch which processes each move based on the type
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void update(){
        if(mCardMoves.size() > 0){
            CardMove cardMove =mCardMoves.peek();
                switch (cardMove.getMoveType()){
                    case PLAYCARD:  calculatePlayCard(cardMove); break;
                    case ATTACKCARD: calculateAttackCard(cardMove);break;
                    case DRAWCARD: calculateDrawCard(cardMove);break;
                    case ATTACKPLAYER: calculateAttackPlayer(cardMove);break;
                    case RETURN: calculateReturn(cardMove); break;
                }
        }
    }

    /**
     * creates a new cardMove which returns a card to its previous position
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void calculateReturn(CardMove cardMove){
        Card card = mDeck.getActiveCards().get(cardMove.getCardHandPosition());
        cardMove.setMoveStarted(true);
        if(cardMove.isMoveStarted()){
            animateMove(card,cardMove);
        }
    }

    /**
     * checks the validity of any attack on the opposing players avatar
     * invalidates moves when certain conditions are met, such as defenders on the board.
     * then sends it for animation
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void calculateAttackPlayer(CardMove cardMove){
        Card card = mDeck.getActiveCards().get(cardMove.getCardHandPosition());
        card.setMovable(false);
        boolean defendersOnOpponentsBoard = (mOpponentsDeck.getActiveDefenderCards().size()!=0);
        if(defendersOnOpponentsBoard){
            card.setPosition(cardMove.getStartPosition().x,cardMove.getStartPosition().y);
            mGlobalMessage.setSubMessage("Cannot Attack,Defenders on the Board");
            cardMove.setMoveType(MoveType.INVALID);
            card.setMovable(true);
        }else{
            if(cardMove.isMoveStarted()){
                animateMove(card,cardMove);

            }else{
                cardMove.setMoveStarted(true);
                animateMove(card,cardMove);

            }
        }

    }

    /**
     * checks the validity of any attack on the opposing players cards
     * invalidates moves when certain conditions are met, such as an invalid target
     * then sends it for animation
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void calculateAttackCard(CardMove cardMove){
        Card card = mDeck.getActiveCards().get(cardMove.getCardHandPosition());
        if(!cardMove.isMoveStarted()){
            for(Card touchedCard:mOpponentsDeck.getActiveCards()){
                if(touchedCard.getBound().intersects(mBoard.getBoardComponents().get(cardMove.getEndBoardPiece()).getBound())){
                    cardMove.setOpponentTargetCard(touchedCard);
                    card.setMovable(false);
                 }
            }
            if(cardMove.getOpponentTargetCard()==null){
                card.setPosition(cardMove.getStartPosition().x,cardMove.getStartPosition().y);
                mGlobalMessage.setSubMessage("Invalid Target");
                cardMove.setMoveType(MoveType.INVALID);
            }
        }
        if(!cardMove.isMoveStarted()&& cardMove.getOpponentTargetCard()!=null){
            cardMove.setMoveStarted(true);
        }
        if(cardMove.isMoveStarted()){
            animateMove(card,cardMove);
        }
    }

    /**
     * plays a sound when a card is drawn,
     * then sends it for animation
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void calculateDrawCard(CardMove cardMove){
        Card card = mDeck.getActiveCards().get(cardMove.getCardHandPosition());
        card.setMovable(true);
        if(!cardMove.isMoveStarted())mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("DrawCard"));
        cardMove.setMoveStarted(true);
        if(cardMove.isMoveStarted()){
            animateMove(card,cardMove);
        }
    }

    /**
     * checks that a card being played is a valid move
     * invalidates moves when certain conditions are met, such as not enough mana
     * or the board slot is already occupied
     * then sends it for animation
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    public void calculatePlayCard(CardMove cardMove) {
        Card card = mDeck.getActiveCards().get(cardMove.getCardHandPosition());
        boolean spaceBlocked = false;
        if (!cardMove.isMoveStarted()) {
            for (int i = 0; i < mDeck.getActiveCards().size(); i++) {
                Card checkCard = mDeck.getActiveCards().get(i);
                if ((i != cardMove.getCardHandPosition())) {
                    if (mBoard.getBoardComponents().get(cardMove.getEndBoardPiece()).getBound().intersects(checkCard.getBound())) {
                        spaceBlocked = true;
                        cardMove.setMoveType(MoveType.INVALID);
                        card.setPosition(cardMove.getStartPosition().x, cardMove.getStartPosition().y);
                        mGlobalMessage.setSubMessage("slot in use");
                        break;
                    }
                }
                if (spaceBlocked) break;
            }
        }
        if ((mPlayer.getMana() >= card.getCost()) && !spaceBlocked) {
            if (!cardMove.isMoveStarted()) {
                card.setMovable(false);
                if (cardMove.getPlayer() == PlayerTurn.OPPONENT) {
                    card.flipCard();
                }
                cardMove.setMoveStarted(true);
                mPlayer.reduceMana(card.getCost());
                mDeck.removeCardFromHand(card);
            }
        } else if (card.getCost() > mPlayer.getMana() && !cardMove.isMoveStarted()) {
            mGlobalMessage.setSubMessage("Not Enough Mana");
            card.setPosition(cardMove.getStartPosition().x, cardMove.getStartPosition().y);
            cardMove.setMoveType(MoveType.INVALID);
        }
        if (cardMove.isMoveStarted() && !spaceBlocked) {
            animateMove(card, cardMove);
        }
    }

    /**
     * checks if cards have reached their target
     * if they have processes damage and sounds.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void animateMove(Card card, CardMove cardMove){
            if(mBoard.getBoardComponents().get(cardMove.getEndBoardPiece()).getBound().contains(card.position.x,card.position.y) && (!cardMove.isMoveCompleted())){
                completeCardMove(card,cardMove);
                processSoundAndActions(card,cardMove);
            }else{
                if(cardMove.getMoveType() == MoveType.DRAWCARD||cardMove.getMoveType() == MoveType.RETURN){
                    if(cardMove.getMoveType() == MoveType.DRAWCARD)card.angularVelocity = ROTATESPEED;
                    cardMove.getMoveVector().set(((cardMove.getEndPosition().x - card.position.x )*DRAWANIMATIONSPEED),((cardMove.getEndPosition().y - card.position.y )*DRAWANIMATIONSPEED));
                }else{
                    cardMove.getMoveVector().set(((cardMove.getEndPosition().x - card.position.x )*ANIMATIONSPEED),((cardMove.getEndPosition().y - card.position.y )*ANIMATIONSPEED));
                }

                card.velocity.set(cardMove.getMoveVector());
            }
    }

    /**
     * snaps a Card into position, resets velocity and
     * corrects orientation, sets move completed to true;
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void completeCardMove(Card card,CardMove cardMove){
        card.velocity.set(Vector2.Zero);
        card.position.set(Vector2.Zero);
        cardMove.getMoveVector().set(Vector2.Zero);
        card.position.x = cardMove.getEndPosition().x;
        card.position.y = cardMove.getEndPosition().y;
        card.angularVelocity = 0;
        card.orientation = 0;
        cardMove.setMoveCompleted(true);
    }

    /**
     * a simple switch which sends a completed CardMove to the correct method
     * to play sounds and calculate resource and play correct sound
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void processSoundAndActions(Card card, CardMove cardMove) {
        switch (cardMove.getMoveType()){
            case ATTACKCARD:processCompletedAttackCard(card,cardMove); break;
            case PLAYCARD: processCompletedPlayCard(card);break;
            case ATTACKPLAYER:processCompletedAttackPlayer(card); break;
            case DRAWCARD:if(cardMove.getPlayer() == PlayerTurn.PLAYER)card.flipCard();
            default:
        }
    }

    /**
     * when a player attack move is completed,this method applies damage
     * to the opposing player
     * then plays the corresponding sound.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void processCompletedAttackPlayer(Card card) {
        mPlayerOpponent.reduceHealth(card.getAttack());
        mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Attack2"));
    }

    /**
     * when a PlayCard move is completed,this method applies card abilities
     * then plays the corresponding sound.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void processCompletedPlayCard(Card card) {
            switch(card.getAbility()){
                case RUSH:card.setMovable(true); mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Rush"));break;
                case MANA:mPlayer.incrementMana(2);mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Mana"));break;
                case DEFEND:mDeck.addCardtoDefenderList(card);mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Defend"));break;
                default:mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("PlayCard"));
            }

    }

    /**
     * when a AttackCard move is completed,this method applies damage to both cards
     * then plays the corresponding sound.
     *  Created by &Justin johnston <40237507>
     * @version 1.0
     */
    private void processCompletedAttackCard(Card card, CardMove cardMove) {
        if(cardMove.getOpponentTargetCard() != null){
            card.damageCard(cardMove.getOpponentTargetCard().getAttack());
            cardMove.getOpponentTargetCard().damageCard(card.getAttack());
            mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Attack"));
            if(card.getHealth()==0){
                card.setMovable(false);
                mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Death"));
                cardMove.setMoveType(MoveType.INVALID);
            }
            if(cardMove.getOpponentTargetCard().getHealth()== 0){
                mAudioManager.play(mGameScreen.getGame().getAssetManager().getSound("Death"));
            }
        }

    }

}
