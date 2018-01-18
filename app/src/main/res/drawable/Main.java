package View;
import Controller.*;
import Model.Character.GameCharacter;
import Model.Dice.PairofDice;
import Model.Dice.TwoPairofDice;
import Model.Game.GameBroad;
import Model.Item.Item;
import Model.Playable.Playable;
import Model.Room.Room;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.layout.BackgroundImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static Model.Game.Game.characterCorrectSelectForCertianList;

public class Main extends Application {
    final Button startGame = new Button(), howTOPlay = new Button(),
            viewRoomButtonRed = new Button(), viewItemListButtonRed = new Button(), okButtonRed = new Button(),
            viewRoomButtonYellow = new Button(), viewItemListButtonYellow = new Button(), okButtonYellow = new Button(),
            viewRoomButtonBlue = new Button(), viewItemListButtonBlue = new Button(), okButtonBlue = new Button(),
            viewRoomButtonGreen = new Button(), viewItemListButtonGreen = new Button(), okButtonGreen = new Button(),
            viewRoomButtonBrown = new Button(), viewItemListButtonBrown = new Button(), okButtonBrown = new Button(),
            viewRoomButtonBlack = new Button(), viewItemListButtonBlack = new Button(), okButtonBlack = new Button();
    List<Scene> playersenes = new ArrayList<>();
    Scene welcomeScene, playerRedScene, playerYellowScene, playerBlueScene, playerGreenScene, playerBrownScene, playerBlackScene,
            getStartItemScene, parkingSearchScene, chiefSelectScene, movingScene, zombieAttackScene,fallenRoomScene ,winnerScene;
    Stage mainWindow;
    static int numberOfPlayers;
    final static int WIDTH = 800;
    final static int HEIGHT = 600;
    final GameBroad gameBroad = new GameBroad(0);
    List<Scene> allPlayerScenes = new ArrayList<>();
    final Label welcome = new Label();
    final static List<Scene> actualPlayerScenes = new ArrayList<>();
    List<Button> allOkButtons = new ArrayList<>();
    final static List<Button> actualOkButton = new ArrayList<>();
    final static List<String> colors = new ArrayList<>();
    final static List<String> actualcolors= new ArrayList<>();

    ;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        //mainWidow setup
        mainWindow = primaryStage;
        mainWindow.setTitle("Mall of Horror");

        mainWindow.setOnCloseRequest(event -> {
            event.consume();
            closePrograme();
        });

        //startgameScene
        List<Integer> numberOfPlayersOptions = new ArrayList<>();
        numberOfPlayersOptions.add(4);
        numberOfPlayersOptions.add(5);
        numberOfPlayersOptions.add(6);

        startGame.setText("");
        startGame.setOnAction(event -> {
            gameBroad.setPlayersNumber(numberWindow.display(numberOfPlayersOptions, "please select how many players"));
            welcomeScene();
            for (int i = 0; i < gameBroad.getPlayersNumber(); i++) {
                actualPlayerScenes.add(allPlayerScenes.get(i));
                actualOkButton.add(allOkButtons.get(i));
                actualcolors.add(colors.get(i));
            }
        });
        startGame.setId("startGame");



        howTOPlay.setId("howToPlay");

        AnchorPane firstlayout = new AnchorPane();
        AnchorPane.setBottomAnchor(startGame,190.0);
        AnchorPane.setLeftAnchor(startGame,350.0);
        AnchorPane.setBottomAnchor(howTOPlay,115.0);
        AnchorPane.setLeftAnchor(howTOPlay,350.0);


        firstlayout.setId("firstLayout");
        howTOPlay.setText("");
        howTOPlay.setOnAction(event -> {
            tutorial();
        });

//        VBox firstlayout = new VBox();
//        firstlayout.setAlignment(Pos.CENTER);
//        firstlayout.getChildren().addAll(startGame, howTOPlay);
//        Scene firstscene = new Scene(firstlayout, WIDTH, HEIGHT);
//        primaryStage.setScene(firstscene);
//        mainWindow.show();


        firstlayout.getChildren().addAll(startGame, howTOPlay);
        Scene firstscene = new Scene(firstlayout, 800, 600);
        firstscene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());
        primaryStage.setScene(firstscene);
        primaryStage.show();

        Button ok1 = new Button(    "");
        ok1.setOnAction(event -> {
            playerselectRoom();
        });
        ok1.setId("ok1");
        AnchorPane welcomePlayerslayout = new AnchorPane();
        welcomePlayerslayout.setId("welcome");
        AnchorPane.setBottomAnchor(ok1,75.0);
        AnchorPane.setRightAnchor(ok1,75.0);


        welcomePlayerslayout.getChildren().addAll(welcome, ok1);
        welcomeScene = new Scene(welcomePlayerslayout, WIDTH, HEIGHT);
        welcomeScene.getStylesheets().add(this.getClass().getResource("welcome.css").toExternalForm());


        //playersSceneRedSetUp;
        viewRoomButtonRed.setText("");
        viewRoomButtonRed.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonRed.setId("redRmBttn");
        viewItemListButtonRed.setText("");
        viewItemListButtonRed.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[0]);
        });
        viewItemListButtonRed.setId("redItemBttn");
        AnchorPane playerRedLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonRed,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonRed,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonRed,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonRed,125.0);
        playerRedLayout.getChildren().addAll(viewRoomButtonRed, viewItemListButtonRed);
        playerRedLayout.setId("playerRed");
        playerRedScene = new Scene(playerRedLayout, 800, 600);
        playerRedScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        //playersSceneYellowSetUp;
        viewRoomButtonYellow.setText("");
        viewRoomButtonYellow.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonYellow.setId("yellowRmBttn");
        viewItemListButtonYellow.setText("");
        viewItemListButtonYellow.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[1]);
        });
        viewItemListButtonYellow.setId("yellowItemBttn");
        AnchorPane playerYellowLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonYellow,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonYellow,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonYellow,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonYellow,125.0);
        playerYellowLayout.getChildren().addAll(viewRoomButtonYellow, viewItemListButtonYellow);
        playerYellowLayout.setId("playerYellow");
        playerYellowScene = new Scene(playerYellowLayout, 800, 600);
        playerYellowScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //playersSceneBlueSetUp;
        viewRoomButtonBlue.setText("Blue - Rooms");
        viewRoomButtonBlue.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonBlue.setId("blueRmBttn");
        viewItemListButtonBlue.setText("Items");
        viewItemListButtonBlue.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[2]);
        });
        viewItemListButtonBlue.setId("blueItemBttn");
        AnchorPane playerBlueLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonBlue,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonBlue,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonBlue,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonBlue,125.0);
        playerBlueLayout.getChildren().addAll(viewRoomButtonBlue, viewItemListButtonBlue);
        playerBlueLayout.setId("playerBlue");
        playerBlueScene = new Scene(playerBlueLayout, 800, 600);
        playerBlueScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //playersSceneGreenSetUp;
        viewRoomButtonGreen.setText("");
        viewRoomButtonGreen.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonGreen.setId("greenRmBttn");
        viewItemListButtonGreen.setText("");
        viewItemListButtonGreen.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[3]);
        });
        viewItemListButtonGreen.setId("greenItemBttn");
        AnchorPane playerGreenLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonGreen,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonGreen,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonGreen,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonGreen,125.0);
        playerGreenLayout.getChildren().addAll(viewRoomButtonGreen, viewItemListButtonGreen);
        playerGreenLayout.setId("playerGreen");
        playerGreenScene = new Scene(playerGreenLayout, 800, 600);
        playerGreenScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //playersSceneBrownSetUp;
        viewRoomButtonBrown.setText("Brown - Rooms");
        viewRoomButtonBrown.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonBrown.setId("brownRmBttn");
        viewItemListButtonBrown.setText("Items");
        viewItemListButtonBrown.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[4]);
        });
        viewItemListButtonBrown.setId("brownItemBttn");
        AnchorPane playerBrownLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonBrown,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonBrown,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonBrown,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonBrown,125.0);
        playerBrownLayout.getChildren().addAll(viewRoomButtonBrown, viewItemListButtonBrown);
        playerBrownLayout.setId("playerBrown");
        playerBrownScene = new Scene(playerBrownLayout, 800, 600);
        playerBrownScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        //playersSceneBlackSetUp;
        viewRoomButtonBlack.setText("Black - Rooms");
        viewRoomButtonBlack.setOnAction(event -> {
            viewRooms();
        });
        viewRoomButtonBlack.setId("blackRmBttn");
        viewItemListButtonBlack.setText("Items");
        viewItemListButtonBlack.setOnAction(event -> {
            viewItem(gameBroad.getTotalPlayerslist()[5]);
        });
       viewItemListButtonBlack.setId("blackItemBttn");
        AnchorPane playerBlackLayout = new AnchorPane();
        AnchorPane.setBottomAnchor(viewRoomButtonBlack,125.0);
        AnchorPane.setRightAnchor(viewRoomButtonBlack,270.0);
        AnchorPane.setBottomAnchor(viewItemListButtonBlack,125.0);
        AnchorPane.setRightAnchor(viewItemListButtonBlack,125.0);
        playerBlackLayout.getChildren().addAll(viewRoomButtonBlack, viewItemListButtonBlack);
        playerBlackLayout.setId("playerBlack");
        playerBlackScene = new Scene(playerBlackLayout, 800, 600);
        playerBlackScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        allPlayerScenes.add(playerRedScene);
        allPlayerScenes.add(playerYellowScene);
        allPlayerScenes.add(playerBlueScene);
        allPlayerScenes.add(playerGreenScene);
        allPlayerScenes.add(playerBrownScene);
        allPlayerScenes.add(playerBlackScene);

        colors.add("Red");
        colors.add("Yellow");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Brown");
        colors.add("Black");

        allOkButtons.add(okButtonRed);
        allOkButtons.add(okButtonYellow);
        allOkButtons.add(okButtonBlue);
        allOkButtons.add(okButtonGreen);
        allOkButtons.add(okButtonBrown);
        allOkButtons.add(okButtonBlack);

        //getStarterItemScene;
        AnchorPane starterItemLayout = new AnchorPane();
        Label starterItemLabel = new Label();
        starterItemLabel.setText("Each player gonna have one starter Item, and the information should be kept to the player himself/herself.");
        starterItemLabel.setId("text");
        starterItemLabel.setWrapText(true);
        starterItemLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(starterItemLabel,100.0);
        AnchorPane.setLeftAnchor(starterItemLabel,70.0);
        AnchorPane.setRightAnchor(starterItemLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
            InputStream is = numberWindow.class.getResourceAsStream("digital-7.ttf");
            final Font f1 = Font.loadFont(is, 25);
            starterItemLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button ok2 = new Button();
        ok2.setText("");
        ok2.setId("msgokbttn");
        ok2.setOnAction(event -> {
            eachplayerGetStarterItem();
        });
        AnchorPane.setLeftAnchor(ok2,362.5);
        AnchorPane.setBottomAnchor(ok2,35.0);
        starterItemLayout.getChildren().addAll(starterItemLabel, ok2);
        starterItemLayout.setId("starterItemLayout");
        getStartItemScene = new Scene(starterItemLayout, WIDTH, HEIGHT);
        getStartItemScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //ParkingSearchScene;
        AnchorPane parkingSeachLayout = new AnchorPane();

        Label parkingSeachLabel = new Label();
        parkingSeachLabel.setText("Welcome to a new round." +
                "\n"+
                "\nWe will proceed parking seach first, " +
                "\nplayers that in ROOM No.4 parking will vote to decide who do the searching, " +
                "\nThe winner will search around the parking and will find" +
                " three items, " +
                "\nhe/she will be able to keep one item, and give one item to the other player, and return one back to the Parking. " +
                "\n(Note: if the vote result in TIE, no searching will perform) " +
                "\n"+
                "\nIf you understand, please click CONTINUE to proceed.");

        parkingSeachLabel.setId("text");
        parkingSeachLabel.setWrapText(true);
        parkingSeachLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(parkingSeachLabel,100.0);
        AnchorPane.setLeftAnchor(parkingSeachLabel,70.0);
        AnchorPane.setRightAnchor(parkingSeachLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            parkingSeachLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        Button viewRoomParking = new Button();
        viewRoomParking.setText("");

        viewRoomParking.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomParking,150.0);
        AnchorPane.setBottomAnchor(viewRoomParking,35.0);

        viewRoomParking.setOnAction(event -> {
            viewRooms();
        });
        Button ok3 = new Button();
        ok3.setText("");

        ok3.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok3,150.0);
        AnchorPane.setBottomAnchor(ok3,35.0);

        ok3.setOnAction(event -> {
            searchStart();
        });
        parkingSeachLayout.getChildren().addAll(parkingSeachLabel,viewRoomParking, ok3);

        parkingSeachLayout.setId("parkingSeachLayout");

        parkingSearchScene = new Scene(parkingSeachLayout, WIDTH, HEIGHT);

        parkingSearchScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        //ChiefSelectScene
        AnchorPane chiefSelectLayout = new AnchorPane();
        Label chiefSelectLabel = new Label();
        chiefSelectLabel.setText("Now we proceed to chief selection, players that in ROOM No.5 SecurityHQ will vote to decide who will be the chief, " +
                "\n"+
                "\nThe chief will be able to look at the cameras and " +
                "find out where the zombies are coming (only chief has this information." +
                "\n(Note: if the vote result in TIE, no chief will be elected) " +
                "\n"+
                "\nIf you understand, please click CONTINUE to proceed.");
        chiefSelectLabel.setId("text");
        chiefSelectLabel.setWrapText(true);
        chiefSelectLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(chiefSelectLabel,100.0);
        AnchorPane.setLeftAnchor(chiefSelectLabel,70.0);
        AnchorPane.setRightAnchor(chiefSelectLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            chiefSelectLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button viewRoomButtonChief = new Button();
        viewRoomButtonChief.setText("");
        viewRoomButtonChief.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomButtonChief,150.0);
        AnchorPane.setBottomAnchor(viewRoomButtonChief,35.0);
        viewRoomButtonChief.setText("");
        viewRoomButtonChief.setOnAction(event -> {
            viewRooms();
        });
        Button ok4 = new Button();
        ok4.setText("");
        ok4.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok4,150.0);
        AnchorPane.setBottomAnchor(ok4,35.0);
        ok4.setOnAction(event -> {
            chiefSelect();
        });
        chiefSelectLayout.getChildren().addAll(chiefSelectLabel,viewRoomButtonChief, ok4);
        chiefSelectLayout.setId("chiefSelectLayout");
        chiefSelectScene = new Scene(chiefSelectLayout, WIDTH,HEIGHT);
        chiefSelectScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //ChracterMovingScene
        AnchorPane movingLayout = new AnchorPane();
        Label movingLabel = new Label();
        movingLabel.setText("The chief is looking at the result now. If no chief is eleced, a ramdom player will start the turn first");
        movingLabel.setId("text");
        movingLabel.setWrapText(true);
        movingLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(movingLabel,100.0);
        AnchorPane.setLeftAnchor(movingLabel,70.0);
        AnchorPane.setRightAnchor(movingLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            movingLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button viewRoomMoving = new Button();
        viewRoomMoving.setText("");
        viewRoomMoving.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomMoving,150.0);
        AnchorPane.setBottomAnchor(viewRoomMoving,35.0);
        viewRoomMoving.setOnAction(event -> {
            viewRooms();
        });
        Button ok5 = new Button();
        ok5.setText("");
        ok5.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok5,150.0);
        AnchorPane.setBottomAnchor(ok5,35.0);
        movingLayout.getChildren().addAll(movingLabel,viewRoomMoving, ok5);
        movingLayout.setId("movingLayout");
        movingScene = new Scene(movingLayout,WIDTH,HEIGHT);
        movingScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

        //ZombieAttackScene
        AnchorPane zombieAttackLayout = new AnchorPane();
        Label zombieAttackLabel = new Label();
        zombieAttackLabel.setText("Now we will reveal where the zomies will go to ");
        zombieAttackLabel.setId("text");
        zombieAttackLabel.setWrapText(true);
        zombieAttackLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(zombieAttackLabel,100.0);
        AnchorPane.setLeftAnchor(zombieAttackLabel,70.0);
        AnchorPane.setRightAnchor(zombieAttackLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            zombieAttackLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button viewRoomAttack = new Button();
        viewRoomAttack.setText("");
        viewRoomAttack.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomAttack,150.0);
        AnchorPane.setBottomAnchor(viewRoomAttack,35.0);
        viewRoomAttack.setOnAction(event -> {
            viewRooms();
        });
        Button ok6 = new Button();
        ok6.setText("");
        ok6.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok6,150.0);
        AnchorPane.setBottomAnchor(ok6,35.0);
        zombieAttackLayout.getChildren().addAll(zombieAttackLabel,viewRoomAttack, ok6);
        zombieAttackLayout.setId("zombieAttackLayout");
        zombieAttackScene = new Scene(zombieAttackLayout,WIDTH,HEIGHT);
        zombieAttackScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        //fallenRoomScene
        AnchorPane fallenRoomLayout = new AnchorPane();
        Label fallenRoomLabel =  new Label();
        fallenRoomLabel.setText("--------------------Zombies attacked ----------------------"+
                "\n"+
                "\nFor each room, when characters' strength is less than zombies number, zombies attacked successfully." +
                "\nIf zombies attacked successfully, one character will be eaten by the zombies"+
                "\n"+
                "\nNote: Parking has no defends, so as long as there is zombies there, one character will be eaten(and no item except thread is allowed)"+
                "\nNote: Supermarket has too many entraces (weak defends), so as long as there are more than four zombies there, one character will be eaten (less than four: the same rule as the other room"+
                "\n"+
                "\nIf you have understood, please type CONTINUE to move to the next step");
        fallenRoomLabel.setId("text");
        fallenRoomLabel.setWrapText(true);
        fallenRoomLabel.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(fallenRoomLabel,100.0);
        AnchorPane.setLeftAnchor(fallenRoomLabel,70.0);
        AnchorPane.setRightAnchor(fallenRoomLabel,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            fallenRoomLabel.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button viewRoomFallen = new Button();
        viewRoomFallen.setText("");
        viewRoomFallen.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomFallen,150.0);
        AnchorPane.setBottomAnchor(viewRoomFallen,35.0);
        viewRoomFallen.setOnAction(event -> {
            viewRooms();
        });
        Button ok7 = new Button("");
        ok7.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok7,150.0);
        AnchorPane.setBottomAnchor(ok7,35.0);
        ok7.setOnAction(event -> {
            zombieAttacked();
        });
        fallenRoomLayout.getChildren().addAll(fallenRoomLabel,viewRoomFallen, ok7);
        fallenRoomLayout.setId("fallenRoomLayout");
        fallenRoomScene = new Scene(fallenRoomLayout,WIDTH,HEIGHT);
        fallenRoomScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());


        AnchorPane winnerLayout = new AnchorPane();
        Label winner = new Label();
        winner.setText("After a long ... we have only four survivor in the mall"+
        "\nClick Continue to find out who's the winner");
        winner.setId("text");
        winner.setWrapText(true);
        winner.setPadding(new Insets(30, 10, 30, 10));
        AnchorPane.setTopAnchor(winner,100.0);
        AnchorPane.setLeftAnchor(winner,70.0);
        AnchorPane.setRightAnchor(winner,70.0);
//        try {
//            // load a custom font from a specific location (change path!)
//            // 12 is the size to use
//            final Font f = Font.loadFont(new FileInputStream(new File("..\\MallofHorrorFX\\src\\Controller\\digital-7.ttf")), 25);
            winner.setFont(f1); // use this font with our label
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Button viewRoomWinner = new Button("");
        viewRoomWinner.setId("viewRoomCommon");
        AnchorPane.setLeftAnchor(viewRoomWinner,150.0);
        AnchorPane.setBottomAnchor(viewRoomWinner,35.0);
        viewRoomWinner.setOnAction(event -> {
            viewRooms();
        });
        Button ok8 = new Button("");
        ok8.setId("msgokbttn");
        AnchorPane.setRightAnchor(ok8,150.0);
        AnchorPane.setBottomAnchor(ok8,35.0);
        ok8.setOnAction(event -> {
            whosTheWinner();
        });
        winnerLayout.getChildren().addAll(winner,viewRoomWinner, ok8);
        winnerLayout.setId("winnerLayout");
        winnerScene = new Scene(winnerLayout, WIDTH,HEIGHT);
        winnerScene.getStylesheets().add(this.getClass().getResource("firstLayout.css").toExternalForm());

    }

//    public void openRule(){
//        Stage Window = new Stage();
//        Image ruleImg = new Image("Images/Rule.jpg", 1200, 2300, false, false);
//
//        StackPane layout = new StackPane();
//        ScrollPane scroll = new ScrollPane();
//        layout.setAlignment(Pos.CENTER);
//        scroll.setContent(new ImageView(ruleImg));
//        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//
//        Scene rule = new Scene(layout, 1200, 750);
//        layout.getChildren().addAll(scroll);
//        Window.setScene(rule);
//        Window.show();
//    }

    private void closePrograme(){
        Boolean answer= YesNoWindow.display("Sure you want to exit");
        if (answer){
            mainWindow.close();
        }

    }

    public void tutorial(){
        Stage Window = new Stage();

        Button next1 = new Button("Next");
        next1.setId("nextBtn");
        AnchorPane t_layout1 = new AnchorPane();
        t_layout1.setId("tutorial_1");
        AnchorPane.setBottomAnchor(next1,30.0);
        AnchorPane.setLeftAnchor(next1,350.0);
        Scene tutorial_1 = new Scene(t_layout1, 800, 600);
        tutorial_1.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout1.getChildren().addAll(next1);


        Button next2 = new Button("Next");
        Button previous2 = new Button("Previous");
        next2.setId("nextBtn");
        previous2.setId("previousBtn");
        AnchorPane t_layout2 = new AnchorPane();
        t_layout2.setId("tutorial_2");
        AnchorPane.setBottomAnchor(next2,30.0);
        AnchorPane.setLeftAnchor(next2,600.0);
        AnchorPane.setBottomAnchor(previous2,30.0);
        AnchorPane.setLeftAnchor(previous2,100.0);
        Scene tutorial_2 = new Scene(t_layout2, 800, 600);
        tutorial_2.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout2.getChildren().addAll(next2,previous2);



        Button next3 = new Button("Next");
        Button previous3 = new Button("Previous");
        next3.setId("nextBtn");
        previous3.setId("previousBtn");
        AnchorPane t_layout3 = new AnchorPane();
        t_layout3.setId("tutorial_3");
        AnchorPane.setBottomAnchor(next3,30.0);
        AnchorPane.setLeftAnchor(next3,600.0);
        AnchorPane.setBottomAnchor(previous3,30.0);
        AnchorPane.setLeftAnchor(previous3,100.0);
        Scene tutorial_3 = new Scene(t_layout3, 800, 600);
        tutorial_3.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout3.getChildren().addAll(next3,previous3);

        Button next4 = new Button("Next");
        Button previous4 = new Button("Previous");
        next4.setId("nextBtn");
        previous4.setId("previousBtn");
        AnchorPane t_layout4 = new AnchorPane();
        t_layout4.setId("tutorial_4");
        AnchorPane.setBottomAnchor(next4,30.0);
        AnchorPane.setLeftAnchor(next4,600.0);
        AnchorPane.setBottomAnchor(previous4,30.0);
        AnchorPane.setLeftAnchor(previous4,100.0);
        Scene tutorial_4 = new Scene(t_layout4, 800, 600);
        tutorial_4.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout4.getChildren().addAll(next4,previous4);

        Button next5 = new Button("Next");
        Button previous5 = new Button("Previous");
        next5.setId("nextBtn");
        previous5.setId("previousBtn");
        AnchorPane t_layout5 = new AnchorPane();
        t_layout5.setId("tutorial_5");
        AnchorPane.setBottomAnchor(next5,30.0);
        AnchorPane.setLeftAnchor(next5,600.0);
        AnchorPane.setBottomAnchor(previous5,30.0);
        AnchorPane.setLeftAnchor(previous5,100.0);
        Scene tutorial_5 = new Scene(t_layout5, 800, 600);
        tutorial_5.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout5.getChildren().addAll(next5,previous5);

        Button next6 = new Button("Next");
        Button previous6 = new Button("Previous");
        next6.setId("nextBtn");
        previous6.setId("previousBtn");
        AnchorPane t_layout6 = new AnchorPane();
        t_layout6.setId("tutorial_6");
        AnchorPane.setBottomAnchor(next6,30.0);
        AnchorPane.setLeftAnchor(next6,600.0);
        AnchorPane.setBottomAnchor(previous6,30.0);
        AnchorPane.setLeftAnchor(previous6,100.0);
        Scene tutorial_6 = new Scene(t_layout6, 800, 600);
        tutorial_6.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout6.getChildren().addAll(next6,previous6);

        Button next7 = new Button("Next");
        Button previous7 = new Button("Previous");
        next7.setId("nextBtn");
        previous7.setId("previousBtn");
        AnchorPane t_layout7 = new AnchorPane();
        t_layout7.setId("tutorial_7");
        AnchorPane.setBottomAnchor(next7,30.0);
        AnchorPane.setLeftAnchor(next7,600.0);
        AnchorPane.setBottomAnchor(previous7,30.0);
        AnchorPane.setLeftAnchor(previous7,100.0);
        Scene tutorial_7 = new Scene(t_layout7, 800, 600);
        tutorial_7.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout7.getChildren().addAll(next7,previous7);

        Button next8 = new Button("Next");
        Button previous8 = new Button("Previous");
        next8.setId("nextBtn");
        previous8.setId("previousBtn");
        AnchorPane t_layout8 = new AnchorPane();
        t_layout8.setId("tutorial_8");
        AnchorPane.setBottomAnchor(next8,30.0);
        AnchorPane.setLeftAnchor(next8,600.0);
        AnchorPane.setBottomAnchor(previous8,30.0);
        AnchorPane.setLeftAnchor(previous8,100.0);
        Scene tutorial_8 = new Scene(t_layout8, 800, 600);
        tutorial_8.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout8.getChildren().addAll(next8,previous8);

        Button next9 = new Button("Next");
        Button previous9 = new Button("Previous");
        next9.setId("nextBtn");
        previous9.setId("previousBtn");
        AnchorPane t_layout9 = new AnchorPane();
        t_layout9.setId("tutorial_9");
        AnchorPane.setBottomAnchor(next9,30.0);
        AnchorPane.setLeftAnchor(next9,600.0);
        AnchorPane.setBottomAnchor(previous9,30.0);
        AnchorPane.setLeftAnchor(previous9,100.0);
        Scene tutorial_9 = new Scene(t_layout9, 800, 600);
        tutorial_9.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout9.getChildren().addAll(next9,previous9);

        Button next10 = new Button("Next");
        Button previous10 = new Button("Previous");
        next10.setId("nextBtn");
        previous10.setId("previousBtn");
        AnchorPane t_layout10 = new AnchorPane();
        t_layout10.setId("tutorial_10");
        AnchorPane.setBottomAnchor(next10,30.0);
        AnchorPane.setLeftAnchor(next10,600.0);
        AnchorPane.setBottomAnchor(previous10,30.0);
        AnchorPane.setLeftAnchor(previous10,100.0);
        Scene tutorial_10 = new Scene(t_layout10, 800, 600);
        tutorial_10.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout10.getChildren().addAll(next10,previous10);

        Button next11 = new Button("Next");
        Button previous11 = new Button("Previous");
        next11.setId("nextBtn");
        previous11.setId("previousBtn");
        AnchorPane t_layout11 = new AnchorPane();
        t_layout11.setId("tutorial_11");
        AnchorPane.setBottomAnchor(next11,30.0);
        AnchorPane.setLeftAnchor(next11,600.0);
        AnchorPane.setBottomAnchor(previous11,30.0);
        AnchorPane.setLeftAnchor(previous11,100.0);
        Scene tutorial_11 = new Scene(t_layout11, 800, 600);
        tutorial_11.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout11.getChildren().addAll(next11,previous11);

        Button next12 = new Button("Next");
        Button previous12 = new Button("Previous");
        next12.setId("nextBtn");
        previous12.setId("previousBtn");
        AnchorPane t_layout12 = new AnchorPane();
        t_layout12.setId("tutorial_12");
        AnchorPane.setBottomAnchor(next12,30.0);
        AnchorPane.setLeftAnchor(next12,600.0);
        AnchorPane.setBottomAnchor(previous12,30.0);
        AnchorPane.setLeftAnchor(previous12,100.0);
        Scene tutorial_12 = new Scene(t_layout12, 800, 600);
        tutorial_12.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout12.getChildren().addAll(next12,previous12);

        Button next13 = new Button("Next");
        Button previous13 = new Button("Previous");
        next13.setId("nextBtn");
        previous13.setId("previousBtn");
        AnchorPane t_layout13 = new AnchorPane();
        t_layout13.setId("tutorial_13");
        AnchorPane.setBottomAnchor(next13,30.0);
        AnchorPane.setLeftAnchor(next13,600.0);
        AnchorPane.setBottomAnchor(previous13,30.0);
        AnchorPane.setLeftAnchor(previous13,100.0);
        Scene tutorial_13 = new Scene(t_layout13, 800, 600);
        tutorial_13.getStylesheets().add(this.getClass().getResource("tutorial.css").toExternalForm());
        t_layout13.getChildren().addAll(next13,previous13);

        Window.setScene(tutorial_1);
        Window.show();

        next1.setOnAction(e ->{
            Window.setScene(tutorial_2);
        });
        previous2.setOnAction(e ->{
            Window.setScene(tutorial_1);
        });
        next2.setOnAction(e ->{
            Window.setScene(tutorial_3);
        });
        previous3.setOnAction(e ->{
            Window.setScene(tutorial_2);
        });
        next3.setOnAction(e ->{
            Window.setScene(tutorial_4);
        });
        previous4.setOnAction(e ->{
            Window.setScene(tutorial_3);
        });
        next4.setOnAction(e ->{
            Window.setScene(tutorial_5);
        });
        previous5.setOnAction(e ->{
            Window.setScene(tutorial_4);
        });
        next5.setOnAction(e ->{
            Window.setScene(tutorial_6);
        });
        previous6.setOnAction(e ->{
            Window.setScene(tutorial_5);
        });
        next6.setOnAction(e ->{
            Window.setScene(tutorial_7);
        });
        previous7.setOnAction(e ->{
            Window.setScene(tutorial_6);
        });
        next7.setOnAction(e ->{
            Window.setScene(tutorial_8);
        });
        previous8.setOnAction(e ->{
            Window.setScene(tutorial_7);
        });
        next8.setOnAction(e ->{
            Window.setScene(tutorial_9);
        });
        previous9.setOnAction(e ->{
            Window.setScene(tutorial_8);
        });
        next9.setOnAction(e ->{
            Window.setScene(tutorial_10);
        });
        previous10.setOnAction(e ->{
            Window.setScene(tutorial_9);
        });
        next10.setOnAction(e ->{
            Window.setScene(tutorial_11);
        });
        previous11.setOnAction(e ->{
            Window.setScene(tutorial_10);
        });
        next11.setOnAction(e ->{
            Window.setScene(tutorial_12);
        });
        previous12.setOnAction(e ->{
            Window.setScene(tutorial_11);
        });
        next12.setOnAction(e ->{
            Window.setScene(tutorial_13);
        });
        previous13.setOnAction(e ->{
            Window.setScene(tutorial_12);
        });

    }

    public void welcomeScene() {
        mainWindow.setScene(welcomeScene);
    }

    public void playerselectRoom() {
        mainWindow.setScene(playerRedScene);
        availableRoomSelect();
    }

    public void viewRooms(){
          RoomView.display(gameBroad.getRooms());

//        List<String> rooms = new ArrayList<>();
//        for (int i=0; i<gameBroad.getRooms().size(); i++){
//            rooms.add(gameBroad.getRooms().get(i).toString());
//        }
//        MultiMessagesWindow.display(rooms, "Room Current Status");
    }

    public void viewItem(Playable player){
       ItemViewWindow.display(player.getCurrentItem());
    }

    public void availableRoomSelect() {
        for (int i = 0; i < gameBroad.totalCharatersRemain(); i += gameBroad.getPlayersNumber()) {
            for (int q = 0; q < gameBroad.getPlayers().size(); q++) {
                mainWindow.setScene(actualPlayerScenes.get(q));
                PairofDice pairofDice = new PairofDice();
                pairofDice.rollDieOne();
                pairofDice.rollDieTwo();
                List<Integer> dices = new ArrayList<>();
                dices.add(pairofDice.getDieOneFace());
                dices.add(pairofDice.getDieTwoFace());
                List<GameCharacter> characterOpitons = new ArrayList<>();
                int selectedRoom = 0;
                boolean yes;
                do {
                    yes = true;
                    selectedRoom = ChoosingRoomWindow.display(dices, gameBroad.getPlayers().get(q) + " get " + pairofDice.getDieOneFace() + " and "
                            + pairofDice.getDieTwoFace() + " please select your room");
                    if (gameBroad.matchRoom(selectedRoom).isFull()) {
                        yes = YesNoWindow.display(gameBroad.matchRoom(selectedRoom).getName() +
                                " is Full, are you sure? (Your character will be asigned to Parking instead)");
                    }
                }
                while (!yes);

                for (int k = 0; k < gameBroad.getPlayers().get(q).getCharactersselect().size(); k++) {
                    characterOpitons.add(gameBroad.getPlayers().get(q).getCharactersselect().get(k));
                }
                String selectedCharacter = GameCharacterWindow.display(gameBroad.getPlayers().get(q), characterOpitons, gameBroad.getPlayers().get(q) +
                        "please select one of these characters into " + ((gameBroad.matchRoom(selectedRoom).isFull())? "Parking":gameBroad.matchRoom(selectedRoom).getName()));
                if (gameBroad.matchRoom(selectedRoom).isFull()) {
                    gameBroad.matchRoom(4).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                    gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
                } else {
                    gameBroad.matchRoom(selectedRoom).enter(gameBroad.getPlayers().get(q).selectchoose(selectedCharacter));
                    ;
                    gameBroad.getPlayers().get(q).selectchooseremove(selectedCharacter);
                }

            }
        }
        mainWindow.setScene(getStartItemScene);
    }


    public void eachplayerGetStarterItem() {
        gameBroad.getItemDeck().shuffle();
        for (int i = 0; i < gameBroad.getPlayers().size(); i++) {
            mainWindow.setScene(actualPlayerScenes.get(i));
            SimpleMessageWindow.display(gameBroad.getPlayers().get(i) + " have received an starter item (Keep it to yourself)");
            Item starterItem = gameBroad.getItemDeck().deal();
            gameBroad.getPlayers().get(i).getItem(starterItem);
            ItemGettingWindow.display(starterItem, "You get");
        }
        mainWindow.setScene(parkingSearchScene);
    }

    public void searchStart() {
        if (gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size() < 3) {
            SimpleMessageWindow.display("Due to Parking is empty (or no more item avaiable), no searching will be performed");
        } else {
            //voting begins
            HashSet<Playable> searchteam = gameBroad.WhoCan(gameBroad.matchRoom(4).existCharacterColor());
            List<Playable> searchTeam = new ArrayList<>();
            for (Playable player : searchteam) {
                searchTeam.add(player);
            }
            SimpleMessageWindow.display(searchteam + " is/are in the parking");
            List<Scene> playersScenes = matchThePlayerScenes(searchTeam);
            List<String> votes = new ArrayList<>();
            String vote = "";
            for (int i = 0; i < searchteam.size(); i++) {
                mainWindow.setScene(playersScenes.get(i));
                Playable teammember = searchTeam.get(i);
                vote = ChoosingColorWindow.display(searchTeam, teammember + " please vote who can search");
                votes.add(teammember.getColor());
                votes.add(vote);
            }
            gameBroad.matchRoom(4).resetVoteResult();
            gameBroad.matchRoom(4).voteResultAfterVote(votes);
            List<String> results = new ArrayList<>();
            for (int l = 0; l < votes.size(); l += 2) {
                results.add(gameBroad.matchPlayer(votes.get(l)) + " has voted " + votes.get(l + 1));
            }
            results.add("Summary: " + gameBroad.matchRoom(4).getCurrentVoteResult());
            MultiMessagesWindow.display(results, "Voting Result");
            //using Threat to change result;
            if (teamHasThreat(searchteam)) {
                boolean voteYes = YesNoWindow.display("Voting result can be changed by item THREAT, anyone want to change the result?");
                if (voteYes) {
                    boolean moreThreatused ;
                    do {
                        moreThreatused =false;
//                        int numThreat = 0;
                        boolean memberUsedThreat = false;
                        for (int i=0; i<searchTeam.size();i++) {
                            mainWindow.setScene(playersScenes.get(i));
                            Playable teammember = searchTeam.get(i);
                            List<Integer> threadOptions = new ArrayList<>();
                            for (int q=0; q<=teammember.threatNum(); q++){
                                threadOptions.add(q);
                            }
//                            numThreat = numberWindow.display(threadOptions,teammember + " please select how many THREAT you want use?" );
                            memberUsedThreat = YesNoWindow.display(teammember + " please confirm you want to use THREAT");
                            if (memberUsedThreat){
                                if (teammember.hasThreat()){
                                    String effectedColor = "";
                                    for (int q = 0; q < votes.size(); q += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(q))) {
                                            effectedColor = votes.get(q + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    gameBroad.matchRoom(4).voteResultAfterItem(effectedColor, 1);
//                                    for (int q = 0; q < numThreat; q++) {
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);
                                    SimpleMessageWindow.display("You vote has increased by one");
//                                    }
                                } else {
                                    SimpleMessageWindow.display("You do not have THREAT");
                                }

                            }

                        }
                        SimpleMessageWindow.display("Result after the THREAT used: " + gameBroad.matchRoom(4).getCurrentVoteResult());
                        moreThreatused = YesNoWindow.display("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
                    }
                    while (moreThreatused);
                }
            }
            //result print
            if (gameBroad.matchRoom(4).winner().equals("TIE")) {
                SimpleMessageWindow.display("Result is TIE. " + " No Searching will be performed");
            } else {
                //searching begins
                mainWindow.setScene(parkingSearchScene);
                String winnercolor = gameBroad.matchRoom(4).winner();
                SimpleMessageWindow.display("Winner is " + gameBroad.matchPlayer(winnercolor));
                SimpleMessageWindow.display(gameBroad.matchPlayer(winnercolor) + " searched the parking and " +
                            "found below items (only winning player can see the result and arrange items)");
                gameBroad.getItemDeck().shuffle();
                Item item1 = gameBroad.getItemDeck().deal();
                Item item2 = gameBroad.getItemDeck().deal();
                Item item3 = gameBroad.getItemDeck().deal();
                List<Item> itemtemplist = new ArrayList<>();
                itemtemplist.add(item1);
                itemtemplist.add(item2);
                itemtemplist.add(item3);
//                List<Integer> options = new ArrayList<>();
//                options.add(1);
//                options.add(2);
//                options.add(3);
//                int itemselect = numberWindow.display(options, "You get 1." + itemtemplist.get(0) + ", 2." + itemtemplist.get(1) + " and 3." +
//                        itemtemplist.get(2) + ". Please select the item you want to keep (1,2,or 3)");
                Item itemselect = ChoosingItemWindow.display(itemtemplist,"Please choose the item you want to keep");
                if (gameBroad.matchPlayer(winnercolor).getCurrentItem().size()<6){
                    ItemGettingWindow.display(itemselect, "You get");
                    gameBroad.matchPlayer(winnercolor).getItem(itemselect);
                }
                else {
                    SimpleMessageWindow.display("You get " + itemselect + ". However, due to your bag is full, you cannot carry more items.(Your throw the item on the ground)");
                }
//                itemtemplist.remove(itemselect - 1);
//                options.remove(2);
                itemtemplist.remove(itemselect);
//                int itemgiveselect =numberWindow.display(options,  "Remaining items: " + " 1." + itemtemplist.get(0) + ", 2." + itemtemplist.get(1)
//                + ". Please select the item you want to give (1 or 2)");
                Item itemgiveselect = ChoosingItemWindow.display(itemtemplist, "Please select the item you want to give");
                HashSet<Playable> others = gameBroad.RemainPlayers(gameBroad.matchPlayer(winnercolor));
                List<Playable> othersList = new ArrayList<>();
                for (Playable other: others){
                    othersList.add(other);
                }
                 String givecolor =ChoosingColorWindow.display(othersList, "Please select who you want give in the List: " + others);
//                Item giveItem = itemtemplist.get(itemgiveselect - 1);
                if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                    gameBroad.matchPlayer(givecolor).getItem(itemgiveselect);
                }
                itemtemplist.remove(itemgiveselect);
                gameBroad.getItemDeck().addBackItem(itemtemplist.get(0));
                String ok1 = "";
                SimpleMessageWindow.display(gameBroad.matchPlayer(givecolor) + " you have received an item from " + gameBroad.matchPlayer(winnercolor)
                    + " (keep it to yourself)");
                if (gameBroad.matchPlayer(givecolor).getCurrentItem().size()<6){
                    ItemGettingWindow.display(itemgiveselect, gameBroad.matchPlayer(givecolor) + ": You get");
                } else {
                    SimpleMessageWindow.display("You should have received " + itemgiveselect + ". Howver, due to your bag is full. You cannot carry more items.");
                }
                SimpleMessageWindow.display("Other players will be joining the game now");
                SimpleMessageWindow.display("Player " + givecolor + " get an item from player " + winnercolor);
            }
        }
        mainWindow.setScene(chiefSelectScene);
    }

    public void chiefSelect(){
        int currentVotingRoomNumber = 5;
        if (gameBroad.matchRoom(5).isEmpty() ){
           SimpleMessageWindow.display("Due to SecurityHQ is empty, no chief election will be performed");

        }
        else {
            //voting begins
            HashSet<Playable> team =  gameBroad.WhoCan(gameBroad.matchRoom(currentVotingRoomNumber).existCharacterColor());
            List <Playable> chiefTeam = new ArrayList<>();
            for (Playable player : team) {
                chiefTeam.add(player);
            }
            SimpleMessageWindow.display(chiefTeam + " are in the SecurityHQ");
            List<Scene> playersScenes = matchThePlayerScenes(chiefTeam);
            List<String> votes = new ArrayList<>();
            String vote = "";
            for (int i = 0; i < chiefTeam.size(); i++) {
                mainWindow.setScene(playersScenes.get(i));
                Playable teammember = chiefTeam.get(i);
                vote = ChoosingColorWindow.display(chiefTeam, teammember + " please vote who will be the chief." );
                votes.add(teammember.getColor());
                votes.add(vote);
            }
            gameBroad.matchRoom(currentVotingRoomNumber).resetVoteResult();
            gameBroad.matchRoom(currentVotingRoomNumber).voteResultAfterVote(votes);
            List<String> results = new ArrayList<>();
            for (int l = 0; l < votes.size(); l += 2) {
                results.add(gameBroad.matchPlayer(votes.get(l)) + " has voted " + votes.get(l + 1));
            }
            results.add("Summary: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
            MultiMessagesWindow.display(results, "Voting Result");
            //using Threat to change result;
            if (teamHasThreat(team)) {
                boolean voteYes = YesNoWindow.display("Voting result can be changed by item THREAT, anyone want to change the result?");
                if (voteYes) {
                    boolean moreThreatused = false;
                    do {
//                        int numThreat = 0;
                        boolean memberUsedThreat2 = false;
                        for (int i=0; i<chiefTeam.size();i++) {
                            mainWindow.setScene(playersScenes.get(i));
                            Playable teammember = chiefTeam.get(i);
//                            List<Integer> threadOptions = new ArrayList<>();
//                            for (int q=0; q<=teammember.threatNum(); q++){
//                                threadOptions.add(q);
//                            }
//                            numThreat = numberWindow.display(threadOptions,teammember + " please select how many THREAT you want use?" );
                            memberUsedThreat2 = YesNoWindow.display(teammember + " please confirm you want to use THREAT");
                            if (memberUsedThreat2){
                                if (teammember.hasThreat()){
                                    String effectedColor = "";
                                    for (int q = 0; q < votes.size(); q += 2) {
                                        if (teammember.getColor().equalsIgnoreCase(votes.get(q))) {
                                            effectedColor = votes.get(q + 1);
                                        }
                                    }
                                    effectedColor = effectedColor.toUpperCase();
                                    gameBroad.matchRoom(currentVotingRoomNumber).voteResultAfterItem(effectedColor, 1);
//                            for (int q = 0; q < numThreat; q++) {
                                    Item threat = gameBroad.matchItem(teammember, "Threat");
                                    teammember.usedItem(threat);
                                    SimpleMessageWindow.display("You vote has increased by one");
 //                            }
                                }else {
                                    SimpleMessageWindow.display("You do not have THREAT");
                                }


                            }

                        }
                        SimpleMessageWindow.display("Result after the THREAT used: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
                        moreThreatused = YesNoWindow.display("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
                    }
                    while (moreThreatused);
                }
            }
        }
        //result print
        if (!gameBroad.matchRoom(5).isEmpty() ) {
            if (gameBroad.matchRoom(5).winner().equals("TIE")) {
                SimpleMessageWindow.display("Result is TIE." + " No chief is elected");
            } else {
                String winnercolor = gameBroad.matchRoom(currentVotingRoomNumber).winner();
                SimpleMessageWindow.display("Voting Result: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
                SimpleMessageWindow.display("Winner is " + gameBroad.matchPlayer(winnercolor));
            }
        }
        //
        mainWindow.setScene(movingScene);
        //------------------------Room selecting begins-------------------------------
        TwoPairofDice fourdices = new TwoPairofDice();
        int DiceOne = fourdices.rollDieOne();
        int DiceTwo = fourdices.rollDieTwo();
        int DiceThree = fourdices.rollDieThree();
        int DiceFour = fourdices.rollDieFour();
        List<Integer> dices = new ArrayList<>();
        dices.clear();
        dices.add(DiceOne);
        dices.add(DiceTwo);
        dices.add(DiceThree);
        dices.add(DiceFour);
        int startplayer = 0;
        int startplayerroomnumber = 0;
        if (gameBroad.matchRoom(5).winner().equals("TIE") || gameBroad.matchRoom(5).isEmpty() ){
            Random generator = new Random();
            startplayer = generator.nextInt(gameBroad.getPlayers().size());
            SimpleMessageWindow.display("As a result, a random player will start first.");
            Playable startActPlayer = gameBroad.getPlayers().get(startplayer);
            mainWindow.setScene(actualPlayerScenes.get(startplayer));
            if (startActPlayer.hasSecurityCamera()) {
                boolean usedCamera = YesNoWindow.display("The results can be viewed by using item SecurityCamera, you want to use Security Camera?(y/n)");
                if (usedCamera) {
//                    List<String> messages = new ArrayList<>();
//                    messages.add(dices.toString());
//                    messages.add("Each number means the correspoding room will have one zombie");
//                    MultiMessagesWindow.display(messages, "Zombies will be approaching:");
                    ViewZombiesWindow.display(dices);
                    Item securityCamera = gameBroad.matchItem(startActPlayer, "SecurityCamera");
                    startActPlayer.usedItem(securityCamera);
                }
            }
            List <Integer> options = gameBroad.roomsOptions(startActPlayer);
            startplayerroomnumber = ChoosingRoomWindow.display(options,startActPlayer + " please choose your room");
        } else {
            String winnercolor = gameBroad.matchRoom(currentVotingRoomNumber).winner();
            startplayer = gameBroad.getPlayers().indexOf(gameBroad.matchPlayer(winnercolor));
            SimpleMessageWindow.display(gameBroad.matchPlayer(winnercolor) + " looked at the screens " +
                        "found zombies are approaching to rooms (only winning player can see the result)");
            List<String> messages = new ArrayList<>();
//            messages.add(dices.toString());
//            messages.add("If you have remember the results, please click CONTINUE");
//            MultiMessagesWindow.display(messages,"Each number means the correspoding room will have one zombie");
            ViewZombiesWindow.display(dices);
            mainWindow.setScene(actualPlayerScenes.get(startplayer));
            List <Integer> options = gameBroad.roomsOptions(gameBroad.matchPlayer(winnercolor));
            startplayerroomnumber = ChoosingRoomWindow.display(options,"Please choose the room that you will go to" );
            SimpleMessageWindow.display("After reviewing the monitor, the chief will go to Room " + startplayerroomnumber);
        }
        if (teamHasSecurityCamera(gameBroad.getPlayers())) {
           boolean usedCameraTeam = YesNoWindow.display("Other players: you can also view it by using item SecurityCamera, anyone want to use Security Camera?(y/n)");
            if (usedCameraTeam) {
                for (int i=0; i<gameBroad.getPlayers().size(); i++) {
                    Playable player = gameBroad.getPlayers().get(i);
                    mainWindow.setScene(actualPlayerScenes.get(i));
                    boolean usedCamera = YesNoWindow.display(player + " please confirm you want to use your SecurityCamera(y/n)");
                    if (usedCamera) {
                        if (player.hasSecurityCamera()) {
//                            List<String> messages = new ArrayList<>();
//                            messages.add(dices.toString());
//                            messages.add("Each number means the correspoding room will have one zombie");
//                            MultiMessagesWindow.display(messages, "Zombies will be approaching:");
                            ViewZombiesWindow.display(dices);
                            Item securityCamera = gameBroad.matchItem(player, "SecurityCamera");
                            player.usedItem(securityCamera);
                        } else {
                            SimpleMessageWindow.display("You do not have a Security Camera");
                        }
                    }
                }
            }
        }
        //-------------------------chief select and camera viewing ends------------------------
        //----------------------other 5 player choosing their desination---------------------
        List<Integer> roomspicked = new ArrayList<>();
        //first half of players
        for (int i = startplayer+1; i<gameBroad.getPlayers().size(); i++){
            mainWindow.setScene(actualPlayerScenes.get(i));
            List <Integer> options = gameBroad.roomsOptions(gameBroad.getPlayers().get(i));
            int roompicked = ChoosingRoomWindow.display(options,gameBroad.getPlayers().get(i) + " please choose your room");
            roomspicked.add(roompicked);
        }
        //other half players
        for (int i = 0; i<startplayer; i++){
            mainWindow.setScene(actualPlayerScenes.get(i));
            List <Integer> options = gameBroad.roomsOptions(gameBroad.getPlayers().get(i));
            int roompicked = ChoosingRoomWindow.display(options,gameBroad.getPlayers().get(i) + " please choose your room");
            roomspicked.add(roompicked);
        }
        SimpleMessageWindow.display("Moving Begins");
        //-------------------------- now players choose character and move to rooms;
        //start player move
        Playable startActualPlayer = gameBroad.getPlayers().get(startplayer);
        Room destination = gameBroad.matchRoom(startplayerroomnumber);
        mainWindow.setScene(actualPlayerScenes.get(startplayer));
        String charselect = GameCharacterWindow.display(startActualPlayer, characterNotInTheRoom(destination, startActualPlayer), gameBroad.getPlayers().get(startplayer) + " please choose your characters to Room " + startplayerroomnumber + ": " +
                gameBroad.matchRoom(startplayerroomnumber).getName() );
        GameCharacter selectedCharacter = gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect);
        Room leavingroom = gameBroad.inWhichRoom(selectedCharacter);
        gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect));
        if (gameBroad.matchRoom(startplayerroomnumber).isFull()){
            gameBroad.matchRoom(4).enter(selectedCharacter);
            SimpleMessageWindow.display(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect) + " left " +
                    leavingroom.getName() + " and wanted to enter " + gameBroad.matchRoom(startplayerroomnumber).getName()
            +"\nBut due to room is full, character is moved to Parking instead");
        }else {
            gameBroad.matchRoom(startplayerroomnumber).enter(selectedCharacter);
            SimpleMessageWindow.display(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect) + " left " +
                    leavingroom.getName() + " enter " + gameBroad.matchRoom(startplayerroomnumber).getName());
        }
        //other players, first half
        int k = 0;
        for (int i = startplayer+1 ,q=0; i<gameBroad.getPlayers().size(); i++,q++){
            Playable actualPlayer = gameBroad.getPlayers().get(i);
            Room destination2 = gameBroad.matchRoom(roomspicked.get(q));
            mainWindow.setScene(actualPlayerScenes.get(i));
            charselect = GameCharacterWindow.display(actualPlayer, characterNotInTheRoom(destination2, actualPlayer),actualPlayer + " please choose your characters to " +
                    destination2.getName());
            GameCharacter selectedCharacter2 =  gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect);
            Room leavingroom2 = gameBroad.inWhichRoom(selectedCharacter2);
            gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect));
            if (gameBroad.matchRoom(roomspicked.get(q)).isFull()){
                gameBroad.matchRoom(4).enter(selectedCharacter2);
                SimpleMessageWindow.display(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " left " +
                        leavingroom2.getName() + " and wanted enter " + gameBroad.matchRoom(roomspicked.get(q)).getName()+
                        "\nBut due to room is full, character is moved to Parking instead");
            }else {
                gameBroad.matchRoom(roomspicked.get(q)).enter(selectedCharacter2);
                SimpleMessageWindow.display(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " left " +
                        leavingroom2.getName() + " enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
            }
            k++;
        }
        for (int i = 0 ,q=k; i<startplayer; i++,q++){
            Playable actualPlayer2 = gameBroad.getPlayers().get(i);
            Room destination3 = gameBroad.matchRoom(roomspicked.get(q));
            mainWindow.setScene(actualPlayerScenes.get(i));
            charselect = GameCharacterWindow.display(actualPlayer2, characterNotInTheRoom(destination3, actualPlayer2),actualPlayer2 + " please choose your characters to "
                    + destination3.getName() );
            GameCharacter selectedCharacter2 = gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect);
            Room leavingRoom2 = gameBroad.inWhichRoom(selectedCharacter2);
            gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect));
            if (gameBroad.matchRoom(roomspicked.get(q)).isFull()){
                gameBroad.matchRoom(4).enter(selectedCharacter2);
                SimpleMessageWindow.display( gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " left " +
                        leavingRoom2.getName() + " and wanted to enter " + gameBroad.matchRoom(roomspicked.get(q)).getName()
                        +"\nDue to room is full, character is moved to Parking instead");
            }else {
                gameBroad.matchRoom(roomspicked.get(q)).enter(selectedCharacter2);
                SimpleMessageWindow.display(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " leave " +
                        leavingRoom2.getName() + " enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
            }
        }
       mainWindow.setScene(zombieAttackScene);
//       List<String> messages = new ArrayList<>();
//       messages.add("Zombies will be approaching rooms number: ");
//       messages.add(" " + dices);
//       messages.add("As a result");
        for (int dice: dices){
            gameBroad.matchRoom(dice).zombieApproached();
//            messages.add("A zombie has approached " + gameBroad.matchRoom(dice).getName() );
        }
//        MultiMessagesWindow.display(messages,"Now we will reveal where the zomies will go to");
        SimpleMessageWindow.display("Now we will reveal where the zombies will go to");
        ViewZombiesWindow.display(dices);

//        List<String> messages2 = new ArrayList<>();
//        messages2.add("For the room with most people, one more zombie will attracted to there (they can smell the flesh)");
        List<Integer> more = new ArrayList<>();
        if (gameBroad.mostPeople().getRoomNum()==7){
//            messages2.add("Result is TIE, no zombie will be attacted");
            more.add(0);
        }
        else {
            gameBroad.mostPeople().zombieApproached();
//            messages2.add("As a result, one zombie has approached " + gameBroad.mostPeople().getName());
            more.add(gameBroad.mostPeople().getRoomNum());
        }
//        messages2.add("----------------------------------------");
//        messages2.add("For the room with most models (they are more likely to scream), one more zombie will attracted to there");
        if (gameBroad.mostModel().getRoomNum()==7){
//            messages2.add("Result is TIE, no zombie will be attacted");
            more.add(0);
        }
        else {
            gameBroad.mostModel().zombieApproached();
//            messages2.add("As a result, one zombie has approached " + gameBroad.mostModel().getName());
            more.add(gameBroad.mostModel().getRoomNum());
        }
//        MultiMessagesWindow.display(messages2, "We may have more zombies approached due to below reasons");
        ViewMoreZombiesWindow.display(more);
        mainWindow.setScene(fallenRoomScene);
    }


    public void zombieAttacked() {
        int count = 0;
        for (int i = 0; i < gameBroad.getRooms().size(); i++) {
            if (gameBroad.getRooms().get(i).isFallen()) {
                count++;
                List<Item> usedItemsList = new ArrayList<>();
                Room fallenRoom = gameBroad.getRooms().get(i);
                HashSet<Playable> playersInTheRoom = gameBroad.WhoCan(fallenRoom.existCharacterColor());
                List<Playable> playersInTheRoomList = new ArrayList<>();
                for (Playable player : playersInTheRoom) {
                    playersInTheRoomList.add(player);
                }
                List<Scene> playerScenes = matchThePlayerScenes(playersInTheRoomList);
                SimpleMessageWindow.display(fallenRoom.getName() + " has fallen, one character will be eaten");
                //using items to save the room
                if (teamHasOtherItems(playersInTheRoom) && fallenRoom.getRoomNum() != 4) {
                    SimpleMessageWindow.display(playersInTheRoom + " is/are in the fallen room");
                    boolean usedItemTeam = YesNoWindow.display("This can be changed by using items, anyone in the room want to use Items?(y/n)");
                    if (usedItemTeam) {
                        boolean noMore;
                        do {
                            noMore = true;
                            for (int q = 0; q < playersInTheRoomList.size(); q++) {
                                Playable player = playersInTheRoomList.get(q);
                                mainWindow.setScene(playerScenes.get(q));
                                if (player.hasOthersItems()) {
//                                    int itemselected = 0;
                                    boolean usedItemBoolean = YesNoWindow.display(player + " please confirm you want to use item.");
                                    if (usedItemBoolean) {
//                                        List<Integer> options = new ArrayList<>();
//                                        for (int k = 1; k <= player.otherItemsList().size(); k++) {
//                                            options.add(k);
//                                        }
                                        Item itemselected = ChoosingItemWindow.display(player.otherItemsList(), "Please select the item you want to use");
//                                        Item usedItem = player.otherItemsList().get(itemselected - 1);
                                        usedItemsList.add(itemselected);
                                        itemselected.effect(player, fallenRoom);
                                        player.usedItem(itemselected);
                                    }
                                }
                                else {
                                    noMore=true;
                                }
                            }
                            mainWindow.setScene(zombieAttackScene);
                            noMore = YesNoWindow.display("Please confirm no more items will be used (y - no more item will be used/n - more item will be used)");
                        }
                        while (!noMore);
                    }
                }
                // items using ended
                if (fallenRoom.isFallen()) {
                    HashSet<Playable> newPlayersInTheRoom = gameBroad.WhoCan(fallenRoom.existCharacterColor());
                    List<Playable> newPlayersInTheRoomList = new ArrayList<>();
                    for (Playable player : newPlayersInTheRoom) {
                        newPlayersInTheRoomList.add(player);
                    }
                    List<Scene> newPlayerScenes = matchThePlayerScenes(newPlayersInTheRoomList);
                    SimpleMessageWindow.display(fallenRoom.getName() + " is still fallen, one character will be eaten");
                    String vote = "";
                    List<String> votes = new ArrayList<>();
                    for (int x = 0; x < newPlayersInTheRoomList.size(); x++) {
                        Playable player = newPlayersInTheRoomList.get(x);
                        mainWindow.setScene(newPlayerScenes.get(x));
                        vote = ChoosingColorWindow.display(newPlayersInTheRoomList, player + " please vote whose character will be eaten");
                        votes.add(player.getColor());
                        votes.add(vote);
                    }
                    fallenRoom.resetVoteResult();
                    fallenRoom.voteResultAfterVote(votes);
                    List<String> results = new ArrayList<>();
                    for (int l = 0; l < votes.size(); l += 2) {
                        results.add(gameBroad.matchPlayer(votes.get(l)) + " has voted " + votes.get(l + 1));
                    }
                    results.add("Summary: " + fallenRoom.getCurrentVoteResult());
                    MultiMessagesWindow.display(results, "Voting Result");
                    //using Threat to change result;
                    if (teamHasThreat(newPlayersInTheRoom)) {
                        boolean teamUsedThreat = YesNoWindow.display("Voting result can be changed by item THREAT, anyone want to change the result?(y/n)");
                        if (teamUsedThreat) {
                            boolean moreThreatused;
                            do {
                                moreThreatused = false;
//                                int numThreat = 0;
                                boolean memberUsedThreat3 = false;
                                for (int x = 0; x < newPlayersInTheRoomList.size(); x++) {
                                    Playable teammember = newPlayersInTheRoomList.get(x);
                                    mainWindow.setScene(newPlayerScenes.get(x));
//                                    List<Integer> threadOptions = new ArrayList<>();
//                                    for (int q = 0; q <= teammember.threatNum(); q++) {
//                                        threadOptions.add(q);
//                                    }
//                                    numThreat = numberWindow.display(threadOptions, teammember + " please select how many THREAT you want use?");
                                    memberUsedThreat3 = YesNoWindow.display(teammember  + " please confirm if you want to use THREAT");
                                    if (memberUsedThreat3){
                                        if (teammember.hasThreat()){
                                            String effectedColor = "";
                                            for (int q = 0; q < votes.size(); q += 2) {
                                                if (teammember.getColor().equalsIgnoreCase(votes.get(q))) {
                                                    effectedColor = votes.get(q + 1);
                                                }
                                            }
                                            effectedColor = effectedColor.toUpperCase();
                                            fallenRoom.voteResultAfterItem(effectedColor, 1);
//                                    for (int q = 0; q < numThreat; q++) {
                                            Item threat = gameBroad.matchItem(teammember, "Threat");
                                            teammember.usedItem(threat);
                                            SimpleMessageWindow.display("Your vote has increased by one");
//                                          }
                                        }else {
                                            SimpleMessageWindow.display("You do not have THREAT");
                                        }
                                    }

                                }
                                SimpleMessageWindow.display("Result after the THREAT used: " + gameBroad.matchRoom(4).getCurrentVoteResult());
                                moreThreatused = YesNoWindow.display("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
                            }
                            while (moreThreatused);
                        }
                    }
                    //result print
                    Playable loser = new Playable();
                    if (fallenRoom.winner().equals("TIE")) {
                        SimpleMessageWindow.display("Result is TIE" +
                                "\nThe result will be ramdomly choosed");
                        List<Playable> losers = new ArrayList<>();
                        for (Playable player : newPlayersInTheRoom) {
                            losers.add(player);
                        }
                        Random generator = new Random();
                        int num = generator.nextInt(losers.size());
                        loser = losers.get(num);
                        SimpleMessageWindow.display(loser + " has been chosed, one of character his/her character in the room will be eaten. ");
                    } else {
                        String winnercolor = fallenRoom.winner();
                        SimpleMessageWindow.display("Voting Result: " + fallenRoom.getCurrentVoteResult() +
                                "\n" + gameBroad.matchPlayer(winnercolor) + " get most vote and one of his/her character in the room will be eaten.");
                        ;
                        loser = gameBroad.matchPlayer(winnercolor);
                    }
                    //the chosen player lost his/her character in the room
                    String deathCharacterStr = "";
                    HashSet<GameCharacter> availableOptionsSet = fallenRoom.existChracterForThatPlayer(loser);
                    List<GameCharacter> availableOptions = new ArrayList<GameCharacter>();
                    for (GameCharacter character : availableOptionsSet) {
                        availableOptions.add(character);
                    }
                    deathCharacterStr = GameCharacterWindow.display(loser, availableOptions, loser + " please selected your character in this room");
                    GameCharacter deathCharacter = gameBroad.matchGameCharacter(loser, deathCharacterStr);
                    loser.removeCharacter(deathCharacter);
                    fallenRoom.leave(deathCharacter);
                    fallenRoom.setCurrentZombienumber(0);
                    SimpleMessageWindow.display(loser + " has lost his/her " + deathCharacterStr
                            + "\nZombies have their feast, and returned back to somewhere else to find their next target!" +
                            "\n(The number of zombies in this room has returned to zero)");
                }
                //after effect for certain items
                for (Item item : usedItemsList) {
                    item.afterEffect(gameBroad);
                }
                usedItemsList.clear();
            }
        }
                if (count == 0) {
                    SimpleMessageWindow.display("No room has fallen this turn, but more zombies are approaching.");
                        }
                HashSet<Playable> removedPlayers = new HashSet<>();
                HashSet<Integer> removedScenes = new HashSet<>();
                removedPlayers.clear();
                removedScenes.clear();
                for (int i=0; i<gameBroad.getPlayers().size();i++) {
                    Playable player = gameBroad.getPlayers().get(i);
                    if (player.remaingCharacter() == 0) {
                        removedPlayers.add(player);
                        removedScenes.add(i);
                    }
                }
                for (Playable player: removedPlayers){
                    SimpleMessageWindow.display("Due to " + player + " has no more character, " + player + " has been removed from the Game Broad.");
                    gameBroad.getPlayers().remove(player);
                }
                for (Integer num: removedScenes){
                    int index = num;
                    actualPlayerScenes.remove(index);
                    actualcolors.remove(index);
                }
                SimpleMessageWindow.display("A round is finished, game will move back to parking search, " +
                        "\nOnce there are four character in game, the player with most victory points won.");
                if (gameBroad.totalCharatersRemain()>4){
                mainWindow.setScene(parkingSearchScene);
                }else {
                    mainWindow.setScene(winnerScene);
                }

    }

    public void whosTheWinner(){
        int mostPoints = gameBroad.getPlayers().get(0).totalVictoryPoints();
        int q = 0;
        int count =0;
        for (int i=0; i<gameBroad.getPlayers().size();i++){
            if (mostPoints<gameBroad.getPlayers().get(i).totalVictoryPoints()){
                mostPoints = gameBroad.getPlayers().get(i).totalVictoryPoints();
                q = i;
            }
        }
        for (Playable player: gameBroad.getPlayers()){
            if(mostPoints == player.totalVictoryPoints()){
                count++;
            }
        }
        if (count>1){
            SimpleMessageWindow.display("Result is TIE, ");
        }
        else {
            Playable winner  = gameBroad.getPlayers().get(q);
            SimpleMessageWindow.display("Congratulations! Winner is " + winner + " with a victory points: " + mostPoints);
        }
    }




    public static boolean teamHasThreat(HashSet<Playable> players) {
        for (Playable player : players) {
            if (player.hasThreat())
                return true;
        }
        return false;
    }

    public static boolean teamHasSecurityCamera(List<Playable> players) {
        for (Playable player : players) {
            if (player.hasSecurityCamera()) {
                return true;
            }
        }
        return false;
    }

    public static boolean teamHasOtherItems(HashSet<Playable> players) {
        for (Playable player : players) {
            if (player.hasOthersItems()) {
                return true;
            }
        }
        return false;
    }

    /**
     * print the list in list form 1.xxx 2.yyyy 3........
     *
     * @param objects the list
     */

    public static String  printTheListWithNumber(List<Item> objects) {
        String fin ="";
        int start = 1;
        for (Object o : objects) {
            fin += start + "." + o + ", ";
            start++;
        }
        return  fin;
    }

    /**
     * print the list option 1. 2. 3. ...........
     *
     * @param objects the list
     */

    public static String  printTheOptions(List<Item> objects) {
        String fin = "";
        int start = 1;
        for (Object o : objects) {
            fin += start + ".";
            start++;
        }
        return fin;
    }

    public static List<GameCharacter> characterNotInTheRoom(Room room, Playable player) {
        List<GameCharacter> notInTheRoomList = new ArrayList<GameCharacter>();
        for (GameCharacter character : player.getGameCharacters()) {
            if (!room.inTheRoom(character)) {
                notInTheRoomList.add(character);
            }
        }
        return notInTheRoomList;
    }

    public static List<Scene> matchThePlayerScenes(List<Playable> players) {
        List<Scene> playerScenes = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            for (int q = 0; q < actualcolors.size(); q++) {
                if (players.get(i).getColor().equalsIgnoreCase(actualcolors.get(q))) {
                    playerScenes.add(actualPlayerScenes.get(q));
                }
            }
        }
        return playerScenes;
    }


}





