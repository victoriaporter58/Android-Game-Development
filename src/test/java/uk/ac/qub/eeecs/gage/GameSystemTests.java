package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.cardDemo.AbilityType;
import uk.ac.qub.eeecs.game.cardDemo.BoardComponent;
import uk.ac.qub.eeecs.game.cardDemo.Board;
import uk.ac.qub.eeecs.game.cardDemo.Card;
import uk.ac.qub.eeecs.game.cardDemo.CardMove;
import uk.ac.qub.eeecs.game.cardDemo.Deck;
import uk.ac.qub.eeecs.game.cardDemo.GameLogic;
import uk.ac.qub.eeecs.game.cardDemo.GlobalMessage;
import uk.ac.qub.eeecs.game.cardDemo.MoveType;
import uk.ac.qub.eeecs.game.cardDemo.Player;
import uk.ac.qub.eeecs.game.cardDemo.PlayerTurn;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GameSystemTests {
    String gameScreenName = "Game Screen";
    @Mock
    private Game game;
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);
    @Mock
    AssetManager assetManager;
    @Mock
    AudioManager audioManager;


    private testBoardComponent boardComponent;
    private testBoard board;
    private Deck deck;
    private Card card;
    private Deck deck2;
    private Player player1;
    private Player player2;
    private GlobalMessage globalMessage;
    private GameLogic gameLogic;


    @Before
    public void setup() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(gameScreen.getGame()).thenReturn(game);


    }

    class testBoardComponent extends BoardComponent {
        public testBoardComponent(int x,int y, GameScreen gameScreen,int componentWidth,int componentHeight, String boardComponentBackground) {
            super(x, y,gameScreen, componentWidth, componentHeight,boardComponentBackground);
        }
    }
    class testBoard extends Board {
        public testBoard(int x,int y,int componentWidth,int componentHeight, GameScreen gameScreen) {
            super(x, y, componentWidth, componentHeight,gameScreen);
        }
    }

    @Test
    public void boardComponentCreate() {
        assetManager.loadAndAddBitmap("BoardCard", "img/GrassBoardCard.png");
        testBoardComponent boardComponent = new testBoardComponent(100, 100, gameScreen, 400, 400, "BoardCard");

        assertNotNull(boardComponent);
        assertEquals((int)boardComponent.getHeight(), 400);
        assertEquals((int)boardComponent.getWidth(), 400);
        assertEquals(boardComponent.position.x, 100, 0);
        assertEquals(boardComponent.position.y, 100, 0);
    }

    @Test
    public void boardCreate() {
        assetManager.loadAndAddBitmap("BoardCard", "img/GrassBoardCard.png");
        testBoardComponent boardComponent = new testBoardComponent(100, 100, gameScreen, 400, 400, "BoardCard");
        testBoard board = new testBoard(0,0,4000,4000,gameScreen);
        assertNotNull(board);
        assertNotEquals(board.getBoardComponents().size(),0);
    }

    @Before
    public void beforeTests() {
       boardComponent = new testBoardComponent(100, 100, gameScreen, 400, 400, null);
       board = new testBoard(0,0,4000,4000,gameScreen);
       deck = new Deck(15, gameScreen);
       card = deck.getTopOfDeck(100, 100);
       deck2 = new Deck(15, gameScreen);
       player1 = new Player(0,0,30,10,null,gameScreen,false);
       player2 = new Player(0,0,30,10,null,gameScreen,false);
       globalMessage = new GlobalMessage();

    }
    @Test
    public void testGlobalMessageAdd() {
        assertTrue(globalMessage.getMessage()=="");
        globalMessage.addMessage("test");
        assertTrue(globalMessage.getMessage()=="test");
    }

    @Test
    public void testGlobalSubMessageAdd() {
        assertTrue(globalMessage.getMessage()=="");
        globalMessage.setSubMessage("test");
        assertTrue(globalMessage.getSubMessage()=="test");
    }

    @Test
    public void testDefendEnumtoString() {
        assertTrue(AbilityType.DEFEND.toString().equals("Stop opponent from attacking your avatar."));
    }
    @Test
    public void testManaEnumtoString() {
        assertTrue(AbilityType.MANA.toString().equals("Gain 2 mana when you play this card."));

    }
    @Test
    public void testRushEnumtoString() {
        assertTrue(AbilityType.RUSH.toString().equals("These can attack on their first move."));

    }
    @Test
    public void testNoneEnumtoString() {
        assertTrue(AbilityType.NONE.toString().equals("No special abilities."));

    }

    @Test
    public void createGameLogic() {
        GameLogic gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(gameLogic.isEmpty());
    }

    @Test
    public void createCardMove() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDDECKSPACE).position,board.getBoardComponents().get(0).position,board,0,gameLogic,false, PlayerTurn.PLAYER);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void CardMoveSetOpponentsCard() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDDECKSPACE).position,board.getBoardComponents().get(0).position,board,0,gameLogic,false, PlayerTurn.PLAYER);
        Card card = new Card(100, 100, gameScreen, 2, 2, 1, "Opponents Card",
                "This card is for testing", "Tree",AbilityType.NONE);
        cardMove.setOpponentTargetCard(card);
        assertEquals(cardMove.getOpponentTargetCard().getCardName(),"Opponents Card");
    }
    @Test
    public void createCardMoveDraw() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDDECKSPACE).position,board.getBoardComponents().get(0).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.DRAWCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveAttackCard() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board.getBoardComponents().get(board.PLAYER2BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.ATTACKCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveAttackPlayer() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board.getBoardComponents().get(board.PLAYER2AVATARSPACE).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.ATTACKPLAYER);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMovePlayCard() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1HANDSPACE).position,board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.PLAYCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveInvalidMove() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(new Vector2(100,200),new Vector2(100,200),board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.INVALID);
        assertTrue(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveAutoReturn() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board.getBoardComponents().get(board.PLAYER2AVATARSPACE).position,board,0,gameLogic,true,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.RETURN);
        assertFalse(gameLogic.isEmpty());
    }


    @Test
    public void createCardMoveDrawPlayer2() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage, audioManager,gameScreen );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER2BOARDDECKSPACE).position,board.getBoardComponents().get(board.PLAYER2HANDSPACE).position,board,0,gameLogic,false,PlayerTurn.OPPONENT);
        assertTrue(cardMove.getMoveType() == MoveType.DRAWCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveAttackCardPlayer2() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER2BOARDSPACE).position,board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.OPPONENT);
        assertTrue(cardMove.getMoveType() == MoveType.ATTACKCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMoveAttackPlayerPlayer2() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER2BOARDSPACE).position,board.getBoardComponents().get(board.PLAYER1AVATARSPACE).position,board,0,gameLogic,false,PlayerTurn.OPPONENT);
        assertTrue(cardMove.getMoveType() == MoveType.ATTACKPLAYER);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void createCardMovePlayCardPlayer2() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER2HANDSPACE).position,board.getBoardComponents().get(board.PLAYER2BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.OPPONENT);
        assertTrue(cardMove.getMoveType() == MoveType.PLAYCARD);
        assertFalse(gameLogic.isEmpty());
    }

    @Test
    public void completePlayCard() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        player1.setMana(100);
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1HANDSPACE).position,board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.PLAYCARD);
        assertFalse(gameLogic.isEmpty());
        deck.getActiveCards().get(0).setPosition(board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position.x,board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position.y);
        gameLogic.update();
        gameLogic.clearCompleted();
        assertFalse(deck.getActiveCards().get(0).isInHand());
        assertTrue(gameLogic.isEmpty());
    }

    @Test
    public void completePlayCardNotEnoughMana() {
        gameLogic = new GameLogic(board,deck,deck2,player1,player2,globalMessage,audioManager,gameScreen  );
        player1.setMana(1);
        assertTrue(deck.drawCard(0,0));
        CardMove cardMove = new CardMove(board.getBoardComponents().get(board.PLAYER1HANDSPACE).position,board.getBoardComponents().get(board.PLAYER1BOARDSPACE).position,board,0,gameLogic,false,PlayerTurn.PLAYER);
        assertTrue(cardMove.getMoveType() == MoveType.PLAYCARD);
        gameLogic.update();
        assertEquals(globalMessage.getSubMessage(),"Not Enough Mana");
        assertTrue(deck.getActiveCards().get(0).isInHand());
        gameLogic.clearCompleted();
        assertTrue(gameLogic.isEmpty());
    }
}
