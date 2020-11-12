package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;

import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.AI;
import uk.ac.qub.eeecs.game.cardDemo.AIMoveInformation;
import uk.ac.qub.eeecs.game.cardDemo.AbilityType;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Card;
import uk.ac.qub.eeecs.game.cardDemo.CardMove;
import uk.ac.qub.eeecs.game.cardDemo.Deck;
import uk.ac.qub.eeecs.game.cardDemo.GameLogic;
import uk.ac.qub.eeecs.game.cardDemo.GlobalMessage;

import uk.ac.qub.eeecs.game.cardDemo.MoveType;
import uk.ac.qub.eeecs.game.cardDemo.Player;
import uk.ac.qub.eeecs.game.cardDemo.PlayerTurn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AITest {
    String gameScreenName = "Game Screen";
    @Mock
    private Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    AssetManager assetManager;

    private testBoard board;
    private Deck deck;
    private Deck deck2;
    private Player player1;
    private Player player2;
    private GameLogic gameLogic;
    private  GlobalMessage globalMessage;
    private AudioManager audioManager;



    @Before
    public void setup() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(gameScreen.getGame()).thenReturn(game);
    }

    class testBoard extends Board {
        public testBoard(int x,int y,int componentWidth,int componentHeight, GameScreen gameScreen) {
            super(x, y, componentWidth, componentHeight,gameScreen);
        }
    }

    @Before
    public void beforeTests() {
        board = new testBoard(0, 0, 400, 400, gameScreen);
        deck = new Deck(15, gameScreen);
        deck2 = new Deck(15, gameScreen);
        player1 = new Player(0,0,30,10,null,gameScreen,false);
        player2 = new Player(0,0,30,10,null,gameScreen,false);
        deck.drawCard(0,0);
        deck.getActiveCards().get(0).setPosition(board.getBoardComponents().get(Board.PLAYER2BOARDSPACE).position.x, board.getBoardComponents().get(Board.PLAYER2BOARDSPACE).position.y);
        deck.getActiveCards().get(0).setInHand(false);
        deck2.drawCard(0,0);
        deck2.getActiveCards().get(0).setPosition(board.getBoardComponents().get(Board.PLAYER1BOARDSPACE +1).position.x, board.getBoardComponents().get(Board.PLAYER1BOARDSPACE +1).position.y);
        deck2.getActiveCards().get(0).setInHand(false);
        deck2.getActiveCards().get(0).setMovable(true);
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage, audioManager,gameScreen );
        player1.setHealth(10);
        player2.setHealth(6);
        for(Card card :deck.getActiveCards()){
            card.setMovable(true);
        }

    }

    @Test
    public void AIMoveIsNotFinished() {
        AI testAI = new AI(board,deck,deck2,player1,player2,1);
        testAI.gatherInformation();
        assertFalse(testAI.isTurnComplete());
    }

    @Test
    public void AICalculateBoardStrengthCardPlacedOnBoard() {
        AI testAI = new AI(board,deck,deck2,player1,player2,1);
        assertEquals(testAI.calculateBoardStrength(deck,true),2);
        deck.drawCard(0,0);
        deck.getActiveCards().get(1).setPosition(board.getBoardComponents().get(Board.PLAYER2BOARDSPACE +1).position.x, board.getBoardComponents().get(Board.PLAYER2BOARDSPACE +1).position.y);
        deck.getActiveCards().get(1).setInHand(false);
        deck.getActiveCards().get(1).setMovable(true);
        assertEquals(testAI.calculateBoardStrength(deck,true),4);
    }
    @Test
    public void AICalculateBoardStrengthCardInHand() {
        AI testAI = new AI(board,deck,deck2,player1,player2,1);
        assertEquals(testAI.calculateBoardStrength(deck,true),2);
        deck.drawCard(0,0);
        assertEquals(testAI.calculateBoardStrength(deck,true),2);
    }
    @Test
    public void AICalculateBoardStrengthOpponent() {
        AI testAI = new AI(board,deck,deck2,player1,player2,1);
        assertEquals(testAI.calculateBoardStrength(deck2,true),2);
        deck2.drawCard(0,0);
        deck2.getActiveCards().get(1).setPosition(board.getBoardComponents().get(board.PLAYER1BOARDSPACE+1).position.x, board.getBoardComponents().get(board.PLAYER1BOARDSPACE+1).position.y);
        deck2.getActiveCards().get(1).setInHand(false);
        deck2.getActiveCards().get(1).setMovable(true);
        assertEquals(testAI.calculateBoardStrength(deck2,true),4);
    }

    @Test
    public void AITakeTurn() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        assertEquals(testAI.calculateBoardStrength(deck2,true), 2);
        AIMoveInformation AImove =testAI.takeTurn();
        assertEquals(AImove.getCardIndex(),0);
        assertEquals(AImove.getStartPosition().x,100,15);
        assertEquals(AImove.getStartPosition().y,160,15);
        CardMove cardMove = new CardMove(AImove.getStartPosition(),AImove.getEndPosition(), board, AImove.getCardIndex(), gameLogic, false, PlayerTurn.PLAYER);
        assertNotEquals(cardMove.getMoveType(), MoveType.INVALID);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void GatherInformationHealth() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.gatherInformation();
        assertEquals(testAI.getHealthDifference(),4);
    }
    @Test
    public void GatherInformationLowestCardCost() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck.drawCard(0,0);
        assertEquals(testAI.calculateLowestCardCost(deck),2);
    }
    @Test
    public void handMovesComplete() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck.drawCard(0,0);
        player1.setMana(0);
        testAI.gatherInformation();
        assertTrue(testAI.getHandMovesComplete());
    }
    @Test
    public void boardMovesComplete() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck.drawCard(0,0);
        for(Card card:deck.getActiveCards()){
            card.setMovable(false);
        }
        testAI.gatherInformation();
        assertTrue(testAI.getBoardMovesComplete());
    }

    @Test
    public void turnComplete() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck.drawCard(0,0);
        for(Card card:deck.getActiveCards()){
            card.setMovable(false);
        }
        player1.setMana(0);
        testAI.gatherInformation();
        assertTrue(testAI.isTurnComplete());
    }

    @Test
    public void turnStart() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.startTurn();
        assertFalse(testAI.getBoardMovesComplete());
        assertFalse(testAI.getHandMovesComplete());
        assertFalse(testAI.isTurnComplete());
    }

    @Test
    public void findCardtoPlayBoard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        assertEquals(testAI.findCardToPlay(AI.CardLocation.BOARD,AbilityType.NONE),0);
    }

    @Test
    public void findCardtoPlayHandNoCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        assertEquals(testAI.findCardToPlay(AI.CardLocation.HAND,AbilityType.NONE),-1);
    }
    @Test
    public void findCardtoPlayHand() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck.drawCard(0,0);
        assertEquals(testAI.findCardToPlay(AI.CardLocation.HAND,AbilityType.NONE),1);
    }


    @Test
    public void findTarget() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        assertEquals(testAI.findTarget(),0);
        deck2.getActiveCards().get(0).setInHand(true);
        deck2.drawCard(0,0);
        deck2.getActiveCards().get(1).setInHand(false);
        assertEquals(testAI.findTarget(),1);
    }

    @Test
    public void findTargetNoTargets() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck2.getActiveCards().get(0).setInHand(true);
        assertEquals(testAI.findTarget(),-1);
    }

    @Test
    public void findSpaceOnBoard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        assertEquals(testAI.findBoardSpace(), Board.PLAYER1BOARDSPACE);
    }

    @Test
    public void findLowestCostDefendNoDefendCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.gatherInformation();
        assertFalse(testAI.doesHandContainPlayableDefendCard());
        assertEquals(testAI.getLowestDefendCardCost(),Integer.MAX_VALUE);
    }

    @Test
    public void findLowestCostDefendCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        Card newCard = new Card(100, 100, gameScreen, 2, 2, 1, "Test Card", "This card is for testing", "Tree", AbilityType.DEFEND);
        deck.addToTopOfDeck(newCard);
        deck.drawCard(100,100);
        testAI.gatherInformation();
        assertTrue(testAI.doesHandContainPlayableDefendCard());
        assertEquals(testAI.getLowestDefendCardCost(),1);
    }

    @Test
    public void findLowestCostRushNoRushCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.gatherInformation();
        assertFalse(testAI.doesHandContainPlayableRushCard());
        assertEquals(testAI.getLowestRushCardCost(),Integer.MAX_VALUE);
    }

    @Test
    public void findLowestCostRushCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        Card newCard = new Card(100, 100, gameScreen, 2, 2, 1, "Test Card", "This card is for testing", "Tree", AbilityType.RUSH);
        deck.addToTopOfDeck(newCard);
        deck.drawCard(100,100);
        testAI.gatherInformation();
        assertTrue(testAI.doesHandContainPlayableRushCard());
        assertEquals(testAI.getLowestRushCardCost(),1);
    }

    @Test
    public void findLowestCostManaNoManaCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.gatherInformation();
        assertFalse(testAI.doesHandContainPlayableManaCard());
        assertEquals(testAI.getLowestManaCardCost(),Integer.MAX_VALUE);
    }

    @Test
    public void findLowestCostManaCard() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        Card newCard = new Card(100, 100, gameScreen, 2, 2, 1, "Test Card", "This card is for testing", "Tree", AbilityType.MANA);
        deck.addToTopOfDeck(newCard);
        deck.drawCard(100,100);
        testAI.gatherInformation();
        assertTrue(testAI.doesHandContainPlayableManaCard());
        assertEquals(testAI.getLowestManaCardCost(),1);
    }

    @Test
    public void testAIModeAggresive() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        testAI.gatherInformation();
        assertEquals(testAI.getAIMode(), AI.AIMode.AGGRESSIVE);
    }

    @Test
    public void testAIModeDefensive() {
        AI testAI = new AI(board, deck, deck2, player1, player2,1);
        deck2.drawCard(100,100);
        deck2.getActiveCards().get(1).setInHand(false);
        deck2.drawCard(100,100);
        deck2.getActiveCards().get(2).setInHand(false);
        testAI.gatherInformation();
        assertEquals(testAI.getAIMode(), AI.AIMode.DEFENSIVE);
    }
}
