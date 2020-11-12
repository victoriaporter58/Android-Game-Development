package uk.ac.qub.eeecs.game.cardDemo;


import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

public class Board extends Sprite {
    // /////////////////////////////////////////////////////////////////////////
    // Properties:
    // /////////////////////////////////////////////////////////////////////////

    /**
     * an enum which represents the different types of components
     * used to make up the board
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    private enum mBoardComponentType {
        GRASSBOARDCARD, GRASSDISCARDPILE, GRASSAVATARSLOT, STONEBOARDCARD, STONEDISCARDPILE, STONEAVATARSLOT, HANDCARD, BOARDDECKSPACE, HSPACER, ENDROW;
    }

    private ArrayList<BoardComponent> mBoardComponents;
    private BoardComponent mBoardComponent;
    private ArrayList<mBoardComponentType> mBoardComponentList;

    //change the 3 variables below to change the board layout and starting hand
    public static int INITIALHANDSIZE = 5;
    public static int BOARDCAPACITY = 5;
    public static int HANDCAPACITY = 9;

    //change this to resize deck to suit new layout
    public static int COMPONENTSIZE = 65;
    private static int STARTYPOSITION = 40;
    private static int STARTXPOSITION = 40;

    public static int PLAYER1HANDSPACE = 0;
    public static int PLAYER1BOARDDECKSPACE = PLAYER1HANDSPACE + HANDCAPACITY;
    public static int PLAYER1DISCARDSPACE = PLAYER1BOARDDECKSPACE + 1;
    public static int PLAYER1BOARDSPACE = PLAYER1DISCARDSPACE + 1;
    public static int PLAYER1AVATARSPACE = PLAYER1BOARDSPACE + BOARDCAPACITY;
    public static int PLAYER2DISCARDSPACE = PLAYER1AVATARSPACE + 1;
    public static int PLAYER2BOARDSPACE = PLAYER2DISCARDSPACE + 1;
    public static int PLAYER2AVATARSPACE = PLAYER2BOARDSPACE + BOARDCAPACITY;
    public static int PLAYER2HANDSPACE = PLAYER2AVATARSPACE + 1;
    public static int PLAYER2BOARDDECKSPACE = PLAYER2HANDSPACE + HANDCAPACITY;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////
    /**
     * a grid based board, for use in the main game loop
     * Created by &Justin johnston <40237507>
     *
     * @version 1.1
     */
    public Board(int x, int y, float width, float height, GameScreen gameScreen) {
        super(x, y, width, height, null, gameScreen);
        mBoardComponents = new ArrayList<>(0);
        mBoardComponentList = new ArrayList<>(0);
        createBoardComponentList();
        createBoard(gameScreen);

    }
    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    // /////////////////////////////////////////////////////////////////////////

    public ArrayList<BoardComponent> getBoardComponents() {
        return mBoardComponents;
    }
    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * draw method which loops through each BoardComponent in a list
     * and calls its draw method
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        for (BoardComponent boardPiece : mBoardComponents) {
            boardPiece.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        }
    }

    /**
     * system to create a list of BoardComponents which will make up the board
     * this can be changed by adjusting the variables above.
     * Any changes are reflected in both the visuals of the board and the game logic and AI systems
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    private void createBoardComponentList() {
        //row 1 - players hand and deck
        for (int i = 0; i < HANDCAPACITY; i++) {
            mBoardComponentList.add(mBoardComponentType.HANDCARD);
        }
        mBoardComponentList.add(mBoardComponentType.HSPACER);
        mBoardComponentList.add(mBoardComponentType.BOARDDECKSPACE);
        mBoardComponentList.add(mBoardComponentType.ENDROW);
        //row 2 - players board
        mBoardComponentList.add(mBoardComponentType.GRASSDISCARDPILE);
        for (int i = 0; i < BOARDCAPACITY; i++) {
            mBoardComponentList.add(mBoardComponentType.GRASSBOARDCARD);
        }
        mBoardComponentList.add(mBoardComponentType.GRASSAVATARSLOT);
        mBoardComponentList.add(mBoardComponentType.ENDROW);
        //row 3 - opponents board
        mBoardComponentList.add(mBoardComponentType.STONEDISCARDPILE);
        for (int i = 0; i < BOARDCAPACITY; i++) {
            mBoardComponentList.add(mBoardComponentType.STONEBOARDCARD);
        }
        mBoardComponentList.add(mBoardComponentType.STONEAVATARSLOT);
        mBoardComponentList.add(mBoardComponentType.ENDROW);
        //row 4 - opponents hand and deck
        for (int i = 0; i < HANDCAPACITY; i++) {
            mBoardComponentList.add(mBoardComponentType.HANDCARD);
        }
        mBoardComponentList.add(mBoardComponentType.HSPACER);
        mBoardComponentList.add(mBoardComponentType.BOARDDECKSPACE);
        mBoardComponentList.add(mBoardComponentType.ENDROW);
    }

    /**
     * this takes the list of enums and creates the board
     * this system also controls the position of the board
     * Created by &Justin johnston <40237507>
     *
     * @version 1.0
     */
    private void createBoard(GameScreen gameScreen) {
        int x = STARTXPOSITION, y = STARTYPOSITION;

        for (mBoardComponentType type : mBoardComponentList) {
            switch (type) {
                case HANDCARD:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, (COMPONENTSIZE / 2), COMPONENTSIZE, "HandCard"));
                    x += COMPONENTSIZE / 2;
                    break;
                case GRASSBOARDCARD:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "GrassBoardCard"));
                    x += COMPONENTSIZE;
                    break;
                case GRASSDISCARDPILE:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "GrassDiscardPile"));
                    x += COMPONENTSIZE;
                    break;
                case GRASSAVATARSLOT:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "GrassAvatarSlot"));
                    x += COMPONENTSIZE;
                    break;
                case STONEBOARDCARD:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "StoneBoardCard"));
                    x += COMPONENTSIZE;
                    break;
                case STONEDISCARDPILE:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "StoneDiscardPile"));
                    x += COMPONENTSIZE;
                    break;
                case STONEAVATARSLOT:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "StoneAvatarSlot"));
                    x += COMPONENTSIZE;
                    break;
                case BOARDDECKSPACE:
                    mBoardComponents.add(mBoardComponent = new BoardComponent(x, y, gameScreen, COMPONENTSIZE, COMPONENTSIZE, "HandCard"));
                    x += COMPONENTSIZE;
                    break;
                case HSPACER:
                    x += COMPONENTSIZE/2;
                    break;
                case ENDROW:
                    y += COMPONENTSIZE;
                    x = STARTXPOSITION;
                    break;
                default:
            }
        }
    }
}
