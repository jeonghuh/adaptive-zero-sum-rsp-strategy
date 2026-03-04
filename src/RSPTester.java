package RSP;

import RSP.RSPPlayable;
import myutil.GenerateGainMatrix;
import TeamWinners.TeamWinnersRSP;
import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author KC Lee
 */
public class RSPTester extends Application {

    private static Properties prop;

    public static int HowManyRuns = 10000;
    public static String player1fullname;
    public static String player2fullname;
    public static boolean verbose = false;
    public static float[][] gainmatrix = GenerateGainMatrix.generateGainCentered();
    public static int player1Move, player2Move;
    public static float AdvancetempGain = 0;

    //--------------------------------------------
    //variables required when showing the GUI
    //GUI 보여줄 때 필요한 변수들
    static Text player1score = null;
    static Text player2score = null;
    static Text player1name = null;
    static Text player2name = null;
    //------------------------------------------------

    public void start(Stage primaryStage) {
        
        //-------------------------------------------
        // Welcome ... as follows : 와 gainmatrix의 원소를 저장한 Text instance 생성.
        // generating Text instances which store the descriptions : "Welcome ~ as follows:", "Total Rounds\n + HowManyRuns", "[Final Score]", and each element of gainmatrix 
        Text welcome = new Text(100, 40, "Welcome to Generalized ROCK-SCISSORS-PAPER!!"); // coordinates(좌표) : (100,40)
        Text rounds = new Text(150, 60, "This game plays " + HowManyRuns + " rounds.");        // coordinates(좌표) : (150,60)
        Text matrixDescription = new Text(100, 80, "The gain matrix for the row-side player is as follows:");   // coordinates(좌표) : (100,80)

        Text roundsinfo = new Text(200, 275, "Total Rounds\n" + HowManyRuns);   //coordinates(좌표) : (200,275)
        Text scoreinfo = new Text(200, 250, "[Final Score]");               //coordiantes(좌표) : (200,250)
        
        //placing each of the elements on the proper location corresponding to the matrix
        //gainmatrix에 대응하는 적절한 위치에 각 원소 위치시키기
        Text[][] elements = new Text[gainmatrix.length][gainmatrix[0].length];
        for (int i = 0; i < gainmatrix.length; i++) {
            for (int j = 0; j < gainmatrix[0].length; j++) {
                elements[i][j] = new Text(105 * (j + 1), 120 + 40 * i, Float.toString(gainmatrix[i][j]));   
            }
        }
        //-------------------------------------------

        
        //------------------------------
        //generating Group instance collecting all the Text instances made in advance
        //이전에 만든 모든 Text instance 모으는 Group instance 생성
        Group root = new Group(welcome, rounds, matrixDescription, elements[0][0], elements[0][1],
                elements[0][2], elements[1][0], elements[1][1], elements[1][2], elements[2][0], elements[2][1], elements[2][2],
                player1name, player2name, roundsinfo, scoreinfo, player1score, player2score);
        //------------------------------

        //generating Scene instance whose width, height, and color are 500, 350, skyblue respectively with root(generated above) as the paramter for the contents
        //내용물을 위한 paramter로서 위에서 만들어진 root을 가지고 있는 너비 500, 높이 350, 하늘색인 Scene 인스턴스 생성
        Scene scene = new Scene(root, 500, 350, Color.SKYBLUE);
        primaryStage.setTitle("RSP Test");  //setting the title "RSP Test" on the window
                                                 //창 이름은 RSP Test
        primaryStage.setScene(scene);       //filling the window with the contents from the Scene instance made above
                                                 //위에서 만든 scene 인스턴스로 창 채우기
        primaryStage.show();                    //showing the GUI for the result of RSP test
                                                //GUI 보여주기

    }

    public static void main(String[] args) {
        
        int player1Move, player2Move;
        float tempGain;
        float rspPlayer1Gain = 0.0f, rspPlayer2Gain = 0.0f;

//read information of two teams and the setting
        readProperyFile();

        //If config.properties is not properly set, we use the following setting
        if (prop == null) {
            if (args.length > 1) {
                player1fullname = args[0];
                player2fullname = args[1];
            }
        } else {
            if (player1fullname == null || player2fullname == null) {
                player1fullname = "GroupFairtest.FairtestRSP";
                player2fullname = "GroupRandomtest.RandomtestRSP";
            }
        }

        RSPPlayable rspPlayer1 = null;
        RSPPlayable rspPlayer2 = null;

        try {
            rspPlayer1 = createRSPPlayable(player1fullname);
            rspPlayer2 = createRSPPlayable(player2fullname);
            rspPlayer1.setGain(gainmatrix);
            rspPlayer2.setGain(gainmatrix);
        } catch (Exception e) {
            System.out.println("Cannot perform the test!!!");
            e.printStackTrace();
            //return 0.0f;
        }

        System.out.println("Welcome to Generalized ROCK-SCISSORS-PAPER!!");
        System.out.println("This game plays " + HowManyRuns + " rounds.");
        System.out.println("The gain matrix for the row-side player is as follows:");

        GenerateGainMatrix.display(gainmatrix);

        // Loop once for each round of the game.
        for (int rnd = 1; rnd <= RSPTester.HowManyRuns; rnd++) {

            // Get the moves for this round.
            int player1Mode = ((rnd + 1) % 2);
            int player2Mode = (rnd % 2);
            int Advanceplayer1Move, Advanceplayer2Move;

            Advanceplayer1Move = rspPlayer1.getUserMove(player1Mode);
            Advanceplayer2Move = rspPlayer2.getUserMove(player2Mode);
            AdvancetempGain = determineRoundGain(Advanceplayer1Move, Advanceplayer2Move, player1Mode, player2Mode);

            player1Move = rspPlayer1.getUserMove(player1Mode);
            player2Move = rspPlayer2.getUserMove(player2Mode);

            if (verbose) {
                System.out.println("[ROUND-" + rnd + "]");
                System.out.println("[Player 1]" + rspPlayer1.getYourGroupName() + "'s move: " + getMoveName(player1Move));
                System.out.println("[Player 2]" + rspPlayer2.getYourGroupName() + "'s move: " + getMoveName(player2Move));
            }

            // Determine the winner of this round.
            if (verbose) {
                System.out.println("player1Move:" + player1Move + ", player2Move:" + player2Move + ", player1Mode:" + player1Mode + ", player2Mode:" + player2Mode);
            }
            tempGain = determineRoundGain(player1Move, player2Move, player1Mode, player2Mode);

            //ResultOfRSP.
            if (player1Mode == RSPPlayable.RowPlayerMode) {
                rspPlayer1Gain = rspPlayer1Gain + tempGain;
                rspPlayer2Gain = rspPlayer2Gain - tempGain;
                if (verbose) {
                    System.out.println("[Player 1]" + rspPlayer1.getYourGroupName() + " earned " + tempGain);
                }
                if (verbose) {
                    System.out.println("[Player 2]" + rspPlayer2.getYourGroupName() + " earned " + (-1) * tempGain);
                }
            } else {
                rspPlayer1Gain = rspPlayer1Gain - tempGain;
                rspPlayer2Gain = rspPlayer2Gain + tempGain;
                if (verbose) {
                    System.out.println("[Player 1]" + rspPlayer1.getYourGroupName() + " earned " + (-1) * tempGain);
                }
                if (verbose) {
                    System.out.println("[Player 2]" + rspPlayer2.getYourGroupName() + " earned " + tempGain);
                }
            }

            if (verbose) {
                System.out.println("--------------------");

                System.out.println("[Cumulative Score]");
                System.out.println("[Player 1]" + rspPlayer1.getYourGroupName() + "=" + rspPlayer1Gain);
                System.out.println("[Player 2]" + rspPlayer2.getYourGroupName() + "=" + rspPlayer2Gain);
                System.out.println();
            }

            rspPlayer1.rememberOpponentMove(player2Move, player1Move);
            rspPlayer2.rememberOpponentMove(player1Move, player2Move);

        }

        System.out.println("----------------------------");
        System.out.println("Total Rounds=" + RSPTester.HowManyRuns);

        System.out.println("[Final Score]");
        System.out.println("[Player 1]" + rspPlayer1.getYourGroupName() + "=" + rspPlayer1Gain);
        System.out.println("[Player 2]" + rspPlayer2.getYourGroupName() + "=" + rspPlayer2Gain);
        System.out.println();

        //generating Text instances which store player1name, player2name, player1score, player2score respectively
        //player1name, player2name, player1score, player2score를 각각 저장한 Text 인스턴스 생성
        player1name = new Text(100, 250, rspPlayer1.getYourGroupName()); //coordinates(좌표) : (100,250)
        player2name = new Text(300, 250, rspPlayer2.getYourGroupName()); //coordinates(좌표) : (300,250)
        player1score = new Text(100, 275, Float.toString(rspPlayer1Gain)); //coordinates(좌표) : (100,275)
        player2score = new Text(300, 275, Float.toString(rspPlayer2Gain)); //coordinates(좌표) : (300,275)

        //return rspPlayer2Gain; // rspPlayer2Gain 값을 반환
        
        launch(args); //start() 메소드 실행. execute start() method
    }

    private static void readProperyFile() {
        prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property values
            player1fullname = prop.getProperty("player1");
            player2fullname = prop.getProperty("player2");
            HowManyRuns = Integer.parseInt(prop.getProperty("HowManyRuns"));
            verbose = Boolean.parseBoolean(prop.getProperty("verbose"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Change move numbers to appropriate string values
     *
     * @return The appropriate string values for the moveNumber
     */
    public static String getMoveName(int moveNumber) {
        String moveName = "";
        switch (moveNumber) {
            case RSPPlayable.ROCK:
                moveName = "Rock";
                break;
            case RSPPlayable.Scissors:
                moveName = "Scissors";
                break;
            case RSPPlayable.Paper:
                moveName = "Paper";
                break;
        }
        return moveName;
    }

    /**
     * Compare player 1's move to player 2's move to determine the gain of this
     * round.
     *
     * @param player1Move the player 1's move.
     * @param player2Move the player 2's move.
     * @param player1Mode the mode of player 1: RSPPlayable.RowPlayerMode or
     * RSPPlayable.ColumnPlayerMode
     * @return 1 if the computer wins. 0 if it is a tie. -1 if the user wins.
     */
    static float determineRoundGain(int player1Move,
            int player2Move, int player1Mode, int player2Mode) {
        if (player1Mode == RSPPlayable.RowPlayerMode) {
            return gainmatrix[player1Move][player2Move];
        } else {
            return gainmatrix[player2Move][player1Move];
        }
    }

    private static RSPPlayable createRSPPlayable(String fullPackageClassname) throws Exception {
        Object object = null;
        Class classDefinition = Class.forName(fullPackageClassname);
        object = classDefinition.newInstance();
        return (RSPPlayable) object;
    }

}