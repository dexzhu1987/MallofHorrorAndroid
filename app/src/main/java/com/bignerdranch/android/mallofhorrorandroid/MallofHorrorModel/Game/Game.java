//package com.bignerdranch.android.mallofhorrorandroid.Model.Game;
//
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//import java.util.Scanner;
//
//import com.bignerdranch.android.mallofhorrorandroid.Model.*;
//
//
//public class Game  {
//
//
//
//    //deciding how many players phase;
//    public void MainLogic() {
//
//        boolean keeploop;
//        int numPlayers = 0;
//
//        do {
//            try {
//                do {
//                    keeploop = false;
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Welcome to Mall of Horror, Please select how many players (4 to 6)?");
//                    numPlayers = input.nextInt();
//                    if (numPlayers > 6 || numPlayers < 4) {
//                        System.out.println("Please enter a number between 4 to 6");
//                    }
//                }
//                while (numPlayers > 6 || numPlayers < 4);
//
//            } catch (Exception e) {
//                System.out.println("Please enter a number.");
//                keeploop = true;
//            }
//        }
//        while (keeploop);
//
//
//        //lauching the a new game broad with decided players and set up their names
//        GameBroad gameBroad = new GameBroad(numPlayers);
//        System.out.println("There will be " + numPlayers + " players in this game.");
//        for (int i = 0; i < numPlayers; i++) {
//            Scanner input = new Scanner(System.in);
//            System.out.println("Player " + gameBroad.getPlayers().get(i).getColor() + " please set up your name: ");
//            String name = input.nextLine();
//            gameBroad.getPlayers().get(i).setName(name);
//        }
//        System.out.println();
//        for (int i = 0; i < numPlayers; i++) {
//            System.out.println(gameBroad.getPlayers().get(i));
//        }
//
//        System.out.println();
//        System.out.println("-------------Pregame Phase: choosing rooms begins ---------------------");
//
//        //Pre-game phase, setting up charater locations
//        PairofDice pairofDice = new PairofDice();
//        String precharselect = "";
//        int preroomselect = 0;
//        for (int i = 0; i < gameBroad.totalCharatersRemain(); i += 4) {
//            for (int q = 0; q < gameBroad.getPlayers().size(); q++) {
//                pairofDice.rollDieOne();
//                pairofDice.rollDieTwo();
//                System.out.println(gameBroad.getPlayers().get(q) + " get " + pairofDice.getDieOneFace() + " and "
//                        + pairofDice.getDieTwoFace());
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Please select your charater (" + gameBroad.getPlayers().get(q).getCharactersselectstr()
//                            + ")");
//                    precharselect = input.nextLine();
//                    if (!charactercorrectselect(gameBroad.getPlayers().get(q), precharselect)){
//                        System.out.println("Please select in the list");
//                    }
//                }
//                while (!charactercorrectselect(gameBroad.getPlayers().get(q), precharselect));
//
//                do {
//                    try {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println(precharselect + " will go to Room number? (Either " + pairofDice.getDieOneFace()
//                                + " or " + pairofDice.getDieTwoFace() + ")");
//                        preroomselect = input.nextInt();
//                        if (preroomselect != pairofDice.getDieOneFace() && preroomselect != pairofDice.getDieTwoFace()) {
//                            System.out.println("Please select either " + pairofDice.getDieOneFace() +
//                                    " or " + pairofDice.getDieTwoFace());
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Please enter a number");
//                    }
//                    String yes = "";
//                    if (gameBroad.matchRoom(preroomselect).isFull() &&
//                            ((preroomselect == pairofDice.getDieOneFace()) || (preroomselect == pairofDice.getDieTwoFace()))) {
//                        do {
//                            do {
//                                Scanner input = new Scanner(System.in);
//                                System.out.println(gameBroad.matchRoom(preroomselect).getName() +
//                                        " is Full, are you sure? (Your character will be asigned to Parking instead)(choose y/n)");
//                                yes = input.nextLine();
//                                if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                                    System.out.println("Please enter y or n");
//                                }
//                            }
//                            while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                            if (yes.equalsIgnoreCase("n")) {
//                                try {
//                                    Scanner input = new Scanner(System.in);
//                                    System.out.println(precharselect + " will go to Model.Room number? (Either " + pairofDice.getDieOneFace()
//                                            + " or " + pairofDice.getDieTwoFace() + ")");
//                                    preroomselect = input.nextInt();
//                                    if (preroomselect != pairofDice.getDieOneFace() && preroomselect != pairofDice.getDieTwoFace()) {
//                                        System.out.println("Please select either " + pairofDice.getDieOneFace() +
//                                                " or " + pairofDice.getDieTwoFace());
//                                    }
//                                } catch (Exception e) {
//                                    System.out.println("Please enter a number");
//                                }
//                                if (!gameBroad.matchRoom(preroomselect).isFull()){
//                                    break;
//                                }
//                            }
//                        }
//                        while (yes.equalsIgnoreCase("n"));
//                    }
//                }
//                while (preroomselect != pairofDice.getDieOneFace() && preroomselect != pairofDice.getDieTwoFace());
//
//            if (gameBroad.matchRoom(preroomselect).isFull()){
//                gameBroad.matchRoom(4).enter(gameBroad.getPlayers().get(q).selectchoose(precharselect));
//                gameBroad.getPlayers().get(q).selectchooseremove(precharselect);
//            }
//            else{
//                gameBroad.matchRoom(preroomselect).enter(gameBroad.getPlayers().get(q).selectchoose(precharselect));     ;
//                gameBroad.getPlayers().get(q).selectchooseremove(precharselect);
//            }
//            }
//        }
//        System.out.println();
//
//        //-------------------pregame --select room phase ends---------------------------------
//        //-------------------pregame -- players get starter item --------------------
//        gameBroad.getItemDeck().shuffle();
//        for (int i=0; i<gameBroad.getPlayers().size(); i++)
//        {
//            String ok7 = "";
//            do {
//                Scanner input = new Scanner(System.in);
//                System.out.println(gameBroad.getPlayers().get(i) + " you have received a starter item (keep this to your yourself)");
//                System.out.println("Please type OK to move to the next step");
//                ok7 = input.nextLine();
//            }
//            while (!ok7.equalsIgnoreCase("ok"));
//            Item starterItem = gameBroad.getItemDeck().deal();
//            gameBroad.getPlayers().get(i).getItem(starterItem);
//            System.out.println(gameBroad.getPlayers().get(i) + " received " + starterItem);
//            String ok2 = "";
//            do {
//                Scanner input = new Scanner(System.in);
//                System.out.println("Please type OK to move to the next step");
//                ok2 = input.nextLine();
//            }
//            while (!ok2.equalsIgnoreCase("ok"));
//            System.out.println();
//        }
//
//        //---------------------gamephase-----------------------------
//        while (gameBroad.totalCharatersRemain()>4){
//        // -----------parking search--------------------------
//            System.out.println("------------------------ Welcome to a new round -----------------");
//        System.out.println("------------- Parking search begins ---------------------");
//        gameBroad.printRooms();
//        System.out.println();
//        if (gameBroad.matchRoom(4).isEmpty() || gameBroad.getItemDeck().getItemsDeck().size()<3){
//            System.out.println("Due to Parking is empty (or no more item avaiable), no searching will be performed");
//        }
//        else {
//            //voting begins
//            HashSet<Playable> searchteam = new HashSet<>();
//            searchteam = gameBroad.WhoCan(gameBroad.matchRoom(4).existCharacterColor());
//            for (Playable teammember: searchteam){
//                System.out.print(teammember + ", ");
//            }
//            System.out.println(" please vote who can search");
//            List<String> votes = new ArrayList<>();
//            String vote = "";
//            for (Playable teammember: searchteam){
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println(teammember + " please vote color in the list:" + searchteam );
//                    vote=input.nextLine();
//                    if (!colorcorrectselect(searchteam,vote)){
//                        System.out.println("Please select correct color.");
//                    }
//                    else {
//                        votes.add(teammember.getColor());
//                        votes.add(vote);
//
//                    }
//                }
//                while (!colorcorrectselect(searchteam,vote));
//            }
//            gameBroad.matchRoom(4).resetVoteResult();
//            gameBroad.matchRoom(4).voteResultAfterVote(votes);
//            System.out.println("---------------------------Voting Results---------------------------------------------------");
//            for (int l=0; l<votes.size(); l+=2){
//                System.out.println(gameBroad.matchPlayer(votes.get(l)) +  " has voted " + votes.get(l+1));
//            }
//            System.out.println("Summary: " + gameBroad.matchRoom(4).getCurrentVoteResult());
//            System.out.println("------------------------------------------------------------------------------");
//            //using Threat to change result;
//            if (teamHasThreat(searchteam)){
//                String yes = "";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Voting result can be changed by item THREAT, anyone want to change the result?(y/n)");
//                    yes = input.nextLine();
//                    if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                        System.out.println("Please enter y or n");
//                    }
//                }
//                while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                if (yes.equalsIgnoreCase("y")){
//                    String quitThreatused = "";
//                    do {
//                        int numThreat = 0;
//                        for (Playable teammember: searchteam){
//                            boolean loop6 = false;
//                            do {
//                                loop6=false;
//                                try {
//                                    Scanner input = new Scanner(System.in);
//                                    System.out.println(teammember + " please select how many THREAT you want use? from 0 to " + teammember.threatNum() );
//                                    System.out.println("Your current item list:" + teammember.getCurrentItem());
//                                    numThreat = input.nextInt();
//                                    if (numThreat<0 || numThreat>teammember.threatNum()){
//                                        System.out.println("Please enter a valid amount");
//                                        loop6 = true;
//                                    }
//
//                                }
//                                catch (Exception e){
//                                    System.out.println("Please enter a number");
//                                    loop6 = true;
//                                }
//                            }
//                            while (loop6);
//                            String effectedColor = "";
//                            for (int i = 0; i<votes.size();i+=2){
//                                if (teammember.getColor().equalsIgnoreCase(votes.get(i))){
//                                    effectedColor = votes.get(i+1);
//                                }
//                            }
//                            effectedColor = effectedColor.toUpperCase();
//                            gameBroad.matchRoom(4).voteResultAfterItem(effectedColor,numThreat);
//                            for (int q=0; q<numThreat; q++){
//                                Item threat = gameBroad.matchItem(teammember,"Threat");
//                                teammember.usedItem(threat);
//                            }
//                        }
//                        System.out.println("Result after the THREAT used: " + gameBroad.matchRoom(4).getCurrentVoteResult());
//                        do {
//                            Scanner input = new Scanner(System.in);
//                            System.out.println("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
//                            quitThreatused = input.nextLine();
//                            if (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y")) {
//                                System.out.println("Please enter y or n");
//                            }
//                        }
//                       while (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y"));
//                    }
//                    while (quitThreatused.equalsIgnoreCase("y"));
//                }
//            }
//            //result print
//            if (gameBroad.matchRoom(4).winner().equals("TIE")){
//                System.out.println("Result is TIE");
//                System.out.println("No Searching will be performed");
//            }
//            else {
//                //searching begins
//                String winnercolor = gameBroad.matchRoom(4).winner();
//                System.out.println("Winner is " + gameBroad.matchPlayer(winnercolor));
//                String ok="";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println(gameBroad.matchPlayer(winnercolor) + " searched the parking and " +
//                            "found below items(only winning player can see the result and arrange items),please type OK to move to next step)");
//                    ok = input.nextLine();
//                }
//                while (!ok.equalsIgnoreCase("ok"));
//                gameBroad.getItemDeck().shuffle();
//                Item item1 =  gameBroad.getItemDeck().deal();
//                Item item2 =  gameBroad.getItemDeck().deal();
//                Item item3 =  gameBroad.getItemDeck().deal();
//                List<Item> itemtemplist = new ArrayList<>();
//                itemtemplist.add(item1);
//                itemtemplist.add(item2);
//                itemtemplist.add(item3);
//                System.out.println("You get 1."+ itemtemplist.get(0) +", 2." + itemtemplist.get(1) +" and 3." +
//                        itemtemplist.get(2) );
//                int itemselect = 0;
//                boolean loop7 =false;
//                do {
//                    try {
//                        loop7 =false;
//                        Scanner input= new Scanner(System.in);
//                        System.out.println("Please select the item you want to keep (1,2,or 3)" );
//                        itemselect = input.nextInt();
//                        if (itemselect!=1 && itemselect !=2 && itemselect!=3){
//                            System.out.println("Please select (1, 2, or 3)");
//                            loop7=true;
//                        }
//                    }
//                    catch (Exception e){
//                        System.out.println("Please enter a number");
//                        loop7 = true;
//                    }
//                }
//                while (loop7 );
//                System.out.println("You get " + itemtemplist.get(itemselect-1) );
//                gameBroad.matchPlayer(winnercolor).getItem(itemtemplist.get(itemselect-1) );
//                itemtemplist.remove(itemselect-1);
//                System.out.println("Remaining items: " + " 1."+ itemtemplist.get(0) +", 2." + itemtemplist.get(1) );
//                int itemgiveselect = 0;
//                boolean loop8 = false;
//                do {
//                    try {
//                        loop8 =false;
//                        Scanner input= new Scanner(System.in);
//                        System.out.println("Please select the item you want to give (1 or 2)" );
//                        itemgiveselect = input.nextInt();
//                        if (itemgiveselect!=1 && itemgiveselect !=2){
//                            System.out.println("Please select (1 or 2");
//                            loop8=true;
//                        }
//                    }
//                    catch (Exception e){
//                        System.out.println("Please enter a number");
//                        loop8 = true;
//                    }
//                }
//                while (loop8);
//                String givecolor="";
//                HashSet<Playable> others = gameBroad.RemainPlayers(gameBroad.matchPlayer(winnercolor));
//                do {
//                        Scanner input= new Scanner(System.in);
//                        System.out.println("Please select who you want give (type in color) in the List: " +  others );
//                        givecolor = input.nextLine();
//                        if (!colorcorrectselect(others,givecolor)) {
//                            System.out.println("Please select correct color.");
//                        }
//                }
//                while (!colorcorrectselect(others,givecolor));
//                Item giveItem = itemtemplist.get(itemgiveselect-1);
//            gameBroad.matchPlayer(givecolor).getItem(itemtemplist.get(itemgiveselect-1));
//            itemtemplist.remove(itemgiveselect-1);
//            gameBroad.getItemDeck().addBackItem(itemtemplist.get(0));
//                String ok1="";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Other players will be joining the game now, type \"OK\" to continue");
//                    ok1 = input.nextLine();
//                }
//                while (!ok1.equalsIgnoreCase("ok"));
//                System.out.println();
//                String ok10="";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println(gameBroad.matchPlayer(givecolor) +  " you have received an item from " + gameBroad.matchPlayer(winnercolor) );
//                    System.out.println( "Pleaese type OK to view the item (keep it to yourself)");
//                    ok10 = input.nextLine();
//                }
//                while (!ok10.equalsIgnoreCase("ok"));
//                System.out.println("You have received " + giveItem);
//                String ok11="";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Other players will be joining the game now, type OK to continue");
//                    ok11 = input.nextLine();
//                }
//                while (!ok11.equalsIgnoreCase("ok"));
//                System.out.println();
//                System.out.println("Player " + givecolor + " get an item from player " + winnercolor);
//                System.out.println();
//            }
//        }
//        //--------------------------parking searching ends ---------------------------------------
//        System.out.println("---------------------- Security HQ chief election begins --------------");
//        gameBroad.printRooms();
//        System.out.println();
//        //--------------------------Security HQ chief election begins ---------------------------------------
//        int currentVotingRoomNumber = 5;
//        if (gameBroad.matchRoom(5).isEmpty() ){
//            System.out.println("Due to SecurityHQ is empty, no chief election will be performed");
//        }
//        else {
//            //voting begins
//            HashSet<Playable> team = new HashSet<>();
//            team = gameBroad.WhoCan(gameBroad.matchRoom(currentVotingRoomNumber).existCharacterColor());
//            for (Playable teammember : team) {
//                System.out.print(teammember + ", ");
//            }
//            System.out.println(" please vote who can view");
//            List<String> votes = new ArrayList<>();
//            String vote = "";
//            for (Playable teammember : team) {
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println(teammember + " please vote color in the list:" + team);
//                    vote = input.nextLine();
//                    if (!colorcorrectselect(team, vote)) {
//                        System.out.println("Please select correct color.");
//                    } else {
//                        votes.add(teammember.getColor());
//                        votes.add(vote);
//                    }
//                }
//                while (!colorcorrectselect(team, vote));
//            }
//            gameBroad.matchRoom(currentVotingRoomNumber).resetVoteResult();
//            gameBroad.matchRoom(currentVotingRoomNumber).voteResultAfterVote(votes);
//            System.out.println("-----------------------Voting Results-------------------------------------------------------");
//            for (int l=0; l<votes.size(); l+=2){
//                System.out.println(gameBroad.matchPlayer(votes.get(l)) +  " has voted " + votes.get(l+1));
//            }
//            System.out.println("Summary: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
//            System.out.println("------------------------------------------------------------------------------");
//            //using Threat to change result;
//            if (teamHasThreat(team)) {
//                String yes = "";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("Voting result can be changed by item THREAT, anyone want to change the result?(y/n)");
//                    yes = input.nextLine();
//                    if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                        System.out.println("Please enter y or n");
//                    }
//                }
//                while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                if (yes.equalsIgnoreCase("y")) {
//                    String quitThreatused = "";
//                    do {
//                        int numThreat = 0;
//                        for (Playable teammember : team) {
//                            boolean loop9 = false;
//                            do {
//                                loop9 = false;
//                                try {
//                                    Scanner input = new Scanner(System.in);
//                                    System.out.println(teammember + " please select how many THREAT you want use? from 0 to " + teammember.threatNum() );
//                                    System.out.println("Your current item list:" + teammember.getCurrentItem());
//                                    numThreat = input.nextInt();
//                                    if (numThreat < 0 || numThreat > teammember.threatNum()) {
//                                        System.out.println("Please enter a valid amount");
//                                        loop9 = true;
//                                    }
//
//                                } catch (Exception e) {
//                                    System.out.println("Please enter a number");
//                                    loop9 = true;
//                                }
//                            }
//                            while (loop9);
//                            for (int q=0; q<numThreat; q++){
//                                Item threat = gameBroad.matchItem(teammember,"Threat");
//                                teammember.usedItem(threat);
//                            }
//                            String effectedColor = "";
//                            for (int i = 0; i<votes.size();i+=2){
//                               if (teammember.getColor().equalsIgnoreCase(votes.get(i))){
//                                   effectedColor = votes.get(i+1);
//                               }
//                            }
//                            effectedColor = effectedColor.toUpperCase();
//                            gameBroad.matchRoom(currentVotingRoomNumber).voteResultAfterItem(effectedColor, numThreat);
//                        }
//                        System.out.println("Result after the THREAT used: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
//                        do {
//                            Scanner input = new Scanner(System.in);
//                            System.out.println("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
//                            quitThreatused = input.nextLine();
//                            if (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y")) {
//                                System.out.println("Please enter y or n");
//                            }
//                        }
//                        while (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y"));
//                    }
//                    while (quitThreatused.equalsIgnoreCase("y"));
//                }
//            }
//        }
//            //result print
//            TwoPairofDice fourdices = new TwoPairofDice();
//            int DiceOne = fourdices.rollDieOne();
//            int DiceTwo = fourdices.rollDieTwo();
//            int DiceThree = fourdices.rollDieThree();
//            int DiceFour = fourdices.rollDieFour();
//            List<Integer> dices = new ArrayList<>();
//            dices.add(DiceOne);
//            dices.add(DiceTwo);
//            dices.add(DiceThree);
//            dices.add(DiceFour);
//
//            int startplayer = 0;
//            int startplayerroomnumber = 0;
//            if (!gameBroad.matchRoom(5).isEmpty() ) {
//                if (gameBroad.matchRoom(5).winner().equals("TIE")) {
//                    System.out.println("Result is TIE");
//                    System.out.println("No chief is elected");
//                }
//            }
//            //------------------------Model.Room selecting begins-------------------------------
//        System.out.println();
//        System.out.println("-----------------------Model.Room selecting phase begins--------------------");
//        gameBroad.printRooms();
//        System.out.println();
//            if (gameBroad.matchRoom(5).winner().equals("TIE") || gameBroad.matchRoom(5).isEmpty() ){
//                Random generator = new Random();
//                startplayer = generator.nextInt(gameBroad.getPlayers().size());
//                System.out.println("As a result, a random player will start first.");
//                Playable startActPlayer = gameBroad.getPlayers().get(startplayer);
//
//                if (startActPlayer.hasSecurityCamera()) {
//                    String yes10 = "";
//                    do {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println("The results can be viewed by using item SecurityCamera, you want to use Security Camera?(y/n)");
//                        yes10 = input.nextLine();
//                        if (!yes10.equalsIgnoreCase("n") && !yes10.equalsIgnoreCase("y")) {
//                            System.out.println("Please enter y or n");
//                        }
//                    }
//                    while (!yes10.equalsIgnoreCase("n") && !yes10.equalsIgnoreCase("y"));
//                    if (yes10.equalsIgnoreCase("y")) {
//                            String yes11 = "";
//                            do {
//                                Scanner input = new Scanner(System.in);
//                                System.out.println(startActPlayer + " please confirm you want to use your SecurityCamera Model.Item to view the result(y/n)");
//                                System.out.println("Your item list:" + startActPlayer.getCurrentItem());
//                                yes11 = input.nextLine();
//                                if (!yes11.equalsIgnoreCase("n") && !yes11.equalsIgnoreCase("y")) {
//                                    System.out.println("Please enter y or n");
//                                }
//                            }
//                            while (!yes11.equalsIgnoreCase("n") && !yes11.equalsIgnoreCase("y"));
//                            if (yes11.equalsIgnoreCase("y")) {
//                                if (startActPlayer.hasSecurityCamera()) {
//                                    System.out.println("Zombies will be approaching rooms ");
//                                    System.out.println(dices);
//                                    System.out.println("Each number means the correspoding room will have one zombie");
//                                } else {
//                                    System.out.println("You do not have a Security Camera");
//                                }
//                            }
//                        }
//                    }
//                boolean loop10 = false;
//                do {
//                    loop10 = false;
//                    try {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println(gameBroad.getPlayers().get(startplayer) + " please choose your room number");
//                        startplayerroomnumber = input.nextInt();
//                        if (startplayerroomnumber < 0 || startplayerroomnumber > 6) {
//                            System.out.println("Please enter a valid room number");
//                            loop10 =true;
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Please enter a number");
//                        loop10 = true;
//                    }
//                }
//                while (loop10);
//            } else {
//                String winnercolor = gameBroad.matchRoom(currentVotingRoomNumber).winner();
//                System.out.println("Voting Result: " + gameBroad.matchRoom(currentVotingRoomNumber).getCurrentVoteResult());
//                System.out.println("Winner is " + gameBroad.matchPlayer(winnercolor));
//                startplayer = gameBroad.getPlayers().indexOf(gameBroad.matchPlayer(winnercolor));
//                String ok = "";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println(gameBroad.matchPlayer(winnercolor) + " looked at the screens " +
//                            "found zombies are approaching to rooms(only winning player can see the result),please type OK to move to next step)");
//                    ok = input.nextLine();
//                }
//                while (!ok.equalsIgnoreCase("ok"));
//                System.out.println("Each number means the correspoding room will have one zombie");
//                System.out.println(dices);
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("If you have remember the results, please type OK to continue");
//                    ok = input.nextLine();
//                }
//                while (!ok.equalsIgnoreCase("ok"));
//                System.out.println("Please choose the room number that you will go to");
//                boolean loop11 = false;
//                do {
//                    loop11 = false;
//                    try {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println(gameBroad.getPlayers().get(startplayer) + " please choose your room number");
//                        startplayerroomnumber = input.nextInt();
//                        if (startplayerroomnumber < 0 || startplayerroomnumber > 6) {
//                            System.out.println("Please enter a valid room number");
//                            loop11 = true;
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Please enter a number");
//                        loop11 = true;
//                    }
//                }
//                while (loop11);
//                System.out.println("After reviewing the monitor, the chief will go to Model.Room " + startplayerroomnumber);
//            }
//            if (teamHasSecurityCamera(gameBroad.getPlayers())) {
//                String yes = "";
//                do {
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("The results can be viewed by using item SecurityCamera, anyone want to use Security Camera?(y/n)");
//                    yes = input.nextLine();
//                    if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                        System.out.println("Please enter y or n");
//                    }
//                }
//                while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                if (yes.equalsIgnoreCase("y")) {
//                    for (Playable player : gameBroad.getPlayers()) {
//                        String yes1 = "";
//                        do {
//                            Scanner input = new Scanner(System.in);
//                            System.out.println(player + " please confirm you want to use your SecurityCamera Model.Item to view the result(y/n)");
//                            System.out.println("Your item list:" + player.getCurrentItem());
//                            yes1 = input.nextLine();
//                            if (!yes1.equalsIgnoreCase("n") && !yes1.equalsIgnoreCase("y")) {
//                                System.out.println("Please enter y or n");
//                            }
//                        }
//                        while (!yes1.equalsIgnoreCase("n") && !yes1.equalsIgnoreCase("y"));
//                        if (yes1.equalsIgnoreCase("y")) {
//                            if (player.hasSecurityCamera()) {
//                                System.out.println("Zombies will be approaching rooms ");
//                                System.out.println(dices);
//                                System.out.println("Each number means the correspoding room will have one zombie");
//                            } else {
//                                System.out.println("You do not have a Security Camera");
//                            }
//                        }
//                    }
//                }
//            }
//            //-------------------------chief select and camera viewing ends------------------------
//            //----------------------other 5 player choosing their desination---------------------
//
//            if (!gameBroad.matchRoom(5).winner().equals("TIE") || !gameBroad.matchRoom(5).isEmpty() ){
//                System.out.println("----------------Other players choosing destination phase------------");
//                gameBroad.printRooms();
//                System.out.println();
//                System.out.println("After reviewing the monitor, the chief will go to Model.Room " + startplayerroomnumber);
//
//            }
//
//            List<Integer> roomspicked = new ArrayList<>();
//            //first half of players
//            for (int i = startplayer+1; i<gameBroad.getPlayers().size(); i++){
//                int roompicked = 0;
//                boolean loop12 = false;
//                do {
//                    loop12=false;
//                    try {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println(gameBroad.getPlayers().get(i) + " please choose your room number");
//                        roompicked = input.nextInt();
//                        roomspicked.add(roompicked);
//                        if (roompicked<0 || roompicked>6){
//                            System.out.println("Please enter a valid room number");
//                            loop12 = true;
//                        }
//                    }
//                    catch (Exception e){
//                        System.out.println("Please enter a number");
//                        loop12 = true;
//                    }
//                }
//                while (loop12);
//            }
//            //other half players
//            for (int i = 0; i<startplayer; i++){
//                int roompicked = 0;
//                boolean loop13 = false;
//                do {
//                    loop13=false;
//                    try {
//                        Scanner input = new Scanner(System.in);
//                        System.out.println(gameBroad.getPlayers().get(i) + " please choose your room number");
//                        roompicked = input.nextInt();
//                        roomspicked.add(roompicked);
//                        if (roompicked<0 || roompicked>6){
//                            System.out.println("Please enter a valid room number");
//                            loop13 = true;
//                        }
//                    }
//                    catch (Exception e){
//                        System.out.println("Please enter a number");
//                        loop13 = true;
//                    }
//                }
//                while (loop13);
//            }
//            System.out.println();
//            System.out.println("--------------------------Moving begins---------------------");
//            gameBroad.printRooms();
//            System.out.println();
//            //-------------------------- now players choose character and move to rooms;
//            //start player move
//                    Playable startActualPlayer = gameBroad.getPlayers().get(startplayer);
//                    Room destination = gameBroad.matchRoom(startplayerroomnumber);
//                    System.out.println(gameBroad.getPlayers().get(startplayer) + " please choose your characters to Model.Room " + startplayerroomnumber + ": " +
//                    gameBroad.matchRoom(startplayerroomnumber).getName() );
//                    String charselect = "";
////                do {
////                    Scanner input = new Scanner(System.in);
////                    System.out.println("In the list: " + gameBroad.getPlayers().get(startplayer).getGameCharacters());
////                    charselect = input.nextLine();
////                    if (!characterCorrectSelectInGame(gameBroad.getPlayers().get(startplayer),charselect)){
////                        System.out.println("Please select correct character");
////                    }
////                    if (gameBroad.matchRoom(startplayerroomnumber).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect))){
////                        System.out.println("Model.Character already in the room, please select other character");
////                    }
////                }
////            while (!characterCorrectSelectInGame(gameBroad.getPlayers().get(startplayer),charselect) ||
////                        gameBroad.matchRoom(startplayerroomnumber).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect)));
//                boolean loop2 = false;
//                do {
//                loop2 = false;
//                characterNotInTheRoom(destination, startActualPlayer);
//                Scanner input = new Scanner(System.in);
//                System.out.println("In the list: " + characterNotInTheRoom(destination, startActualPlayer));
//                charselect = input.nextLine();
//                if (!characterCorrectSelectForCertianList(characterNotInTheRoom(destination, startActualPlayer),charselect)){
//                    System.out.println("Please select correct character");
//                    loop2 =true;
//                    }
//                }
//                 while (loop2);
//                GameCharacter selectedCharacter = gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect);
//                Room leavingroom = gameBroad.inWhichRoom(selectedCharacter);
//                gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect));
//                if (gameBroad.matchRoom(startplayerroomnumber).isFull()){
//                    gameBroad.matchRoom(4).enter(selectedCharacter);
//                    System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect) + " left " +
//                            leavingroom.getName() + " and wanted to enter " + gameBroad.matchRoom(startplayerroomnumber).getName());
//                    System.out.println("But due to room is full, character is moved to Parking instead");
//                }else {
//                gameBroad.matchRoom(startplayerroomnumber).enter(selectedCharacter);
//                System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(startplayer),charselect) + " leave " +
//                            leavingroom.getName() + " enter " + gameBroad.matchRoom(startplayerroomnumber).getName());
//                }
//                ;
//
//            System.out.println();
//            //other players, first half
//            int k = 0;
//            for (int i = startplayer+1 ,q=0; i<gameBroad.getPlayers().size(); i++,q++){
////                do {
////                    Scanner input = new Scanner(System.in);
////                    System.out.println(gameBroad.getPlayers().get(i) + " please choose your characters to Model.Room " + roomspicked.get(q) + ": " +
////                        gameBroad.matchRoom(roomspicked.get(q)).getName() );
////                    System.out.println( gameBroad.getPlayers().get(i).getGameCharacters());
////                    charselect = input.nextLine();
////                    if (!characterCorrectSelectInGame(gameBroad.getPlayers().get(i),charselect)){
////                        System.out.println("Please select correct character");
////                    }
////                    if (gameBroad.matchRoom(roomspicked.get(q)).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect))){
////                        System.out.println("Model.Character already in the room, please select other character");
////                    }
////                }
////                while (!characterCorrectSelectInGame(gameBroad.getPlayers().get(i),charselect) ||
////                        gameBroad.matchRoom(roomspicked.get(q)).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)));
//                Playable actualPlayer = gameBroad.getPlayers().get(i);
//                Room destination2 = gameBroad.matchRoom(roomspicked.get(q));
//                boolean loop15 = false;
//                do {
//                    loop15 = false;
//                    characterNotInTheRoom(destination2, actualPlayer);
//                    System.out.println(actualPlayer + " please choose your characters to Model.Room " + destination2.getName() + ": " +
//                        destination2.getName());
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("In the list: " + characterNotInTheRoom(destination2, actualPlayer));
//                    charselect = input.nextLine();
//                    if (!characterCorrectSelectForCertianList(characterNotInTheRoom(destination2, actualPlayer),charselect)){
//                        System.out.println("Please select correct character");
//                        loop15 =true;
//                    }
//                }
//                while (loop15);
//
//                GameCharacter selectedCharacter2 =  gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect);
//
//                Room leavingroom2 = gameBroad.inWhichRoom(selectedCharacter2);
//                gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect));
//
//                if (gameBroad.matchRoom(roomspicked.get(q)).isFull()){
//                    gameBroad.matchRoom(4).enter(selectedCharacter2);
//                    System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " lelf " +
//                            leavingroom2.getName() + " and wanted enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
//                    System.out.println("But due to room is full, character is moved to Parking instead");
//                }else {
//                    gameBroad.matchRoom(roomspicked.get(q)).enter(selectedCharacter2);
//                    System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " leave " +
//                            leavingroom2.getName() + " enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
//                }
//                k++;
//                System.out.println();
//            }
//            for (int i = 0 ,q=k; i<startplayer; i++,q++){
////                do {
////                    Scanner input = new Scanner(System.in);
////                    System.out.println(gameBroad.getPlayers().get(i) + " please choose your characters to Model.Room " + roomspicked.get(q) + ": " +
////                            gameBroad.matchRoom(roomspicked.get(q)).getName() );
////                    System.out.println(gameBroad.getPlayers().get(i).getGameCharacters());
////                    charselect = input.nextLine();
////                    if (!characterCorrectSelectInGame(gameBroad.getPlayers().get(i),charselect)){
////                        System.out.println("Please select correct character");
////                    }
////                    if (gameBroad.matchRoom(roomspicked.get(q)).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect))){
////                        System.out.println("Model.Character already in the room, please select other character");
////                    }
////                }
////                while (!characterCorrectSelectInGame(gameBroad.getPlayers().get(i),charselect) ||
////                        gameBroad.matchRoom(roomspicked.get(q)).inTheRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)));
//                Playable actualPlayer2 = gameBroad.getPlayers().get(i);
//                Room destination3 = gameBroad.matchRoom(roomspicked.get(q));
//                boolean loop16 = false;
//                do {
//                    loop16 = false;
//                    characterNotInTheRoom(destination3, actualPlayer2);
//                    System.out.println(actualPlayer2 + " please choose your characters to Model.Room " + destination3.getName() + ": " +
//                            destination3.getName());
//                    Scanner input = new Scanner(System.in);
//                    System.out.println("In the list: " + characterNotInTheRoom(destination3, actualPlayer2));
//                    charselect = input.nextLine();
//                    if (!characterCorrectSelectForCertianList(characterNotInTheRoom(destination3, actualPlayer2),charselect)){
//                        System.out.println("Please select correct character");
//                        loop16 =true;
//                    }
//                }
//                while (loop16);
//                GameCharacter selectedCharacter2 = gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect);
//                Room leavingRoom2 = gameBroad.inWhichRoom(selectedCharacter2);
//                gameBroad.inWhichRoom(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect)).leave(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect));
//
//                if (gameBroad.matchRoom(roomspicked.get(q)).isFull()){
//                    gameBroad.matchRoom(4).enter(selectedCharacter2);
//                    System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " left " +
//                            leavingRoom2.getName() + " and wanted to enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
//                    System.out.println("Due to room is full, character is moved to Parking instead");
//                }else {
//                    gameBroad.matchRoom(roomspicked.get(q)).enter(selectedCharacter2);
//                    System.out.println(gameBroad.matchGameCharacter(gameBroad.getPlayers().get(i),charselect) + " leave " +
//                            leavingRoom2.getName() + " enter " + gameBroad.matchRoom(roomspicked.get(q)).getName());
//                }
//
//                System.out.println();
//            }
//
//        System.out.println();
//        System.out.println("---------------Moving Ends------------------");
//        gameBroad.printRooms();
//        System.out.println();
//
//        System.out.println("---------------Zombie Apprears------------------");
//        System.out.println("Zombies will be approaching rooms ");
//        System.out.println("Dices result" + dices);
//        System.out.println("Each number means the correspoding room will have one zombie");
//        System.out.println("As a result, zomebie are entering");
//        for (int dice: dices){
//            gameBroad.matchRoom(dice).zombieApproached();
//            System.out.println("A zombie has approached " + gameBroad.matchRoom(dice).getName() );
//        }
//        System.out.println();
//        System.out.println("For the room with most people, one more zombie will attracted to there (they can smell the flesh)");
//        if (gameBroad.mostPeople().getRoomNum()==7){
//            System.out.println("Result is TIE, no zombie will be attacted");
//        }
//        else {
//            gameBroad.mostPeople().zombieApproached();
//            System.out.println("As a result, one zombie has approached " + gameBroad.mostPeople().getName());
//        }
//        System.out.println("For the room with most models (they are more likely to scream), one more zombie will attracted to there");
//        if (gameBroad.mostModel().getRoomNum()==7){
//            System.out.println("Result is TIE, no zombie will be attacted");
//        }
//        else {
//            gameBroad.mostModel().zombieApproached();
//            System.out.println("As a result, one zombie has approached " + gameBroad.mostModel().getName());
//        }
//        String ok = "";
//        do {
//            Scanner input = new Scanner(System.in);
//            System.out.println("Please type OK to move to the next step");
//            ok = input.nextLine();
//        }
//        while (!ok.equalsIgnoreCase("ok"));
//        gameBroad.printRooms();
//        System.out.println();
//        System.out.println("-------------------Zombies approached phase ends---------------------");
//        gameBroad.printRooms();
//        System.out.println();
//        System.out.println("--------------------Zombies attacked ----------------------");
//        System.out.println("For each room, when characters' strength is less than zombies number, zombies attacked successfully.");
//        System.out.println("If zombies attacked successfully, one character will be eaten by the zombies");
//        System.out.println("Note: Parking has no defends, so as long as there is zombies there, one character will be eaten");
//        System.out.println("Note: Supermarket has too many entraces (weak defends), so as long as there are more than four zombies there, one character will be eaten (less than four: the same rule as the other room");
//        String ok1 = "";
//        do {
//            Scanner input = new Scanner(System.in);
//            System.out.println("If you have understood, please type OK to move to the next step");
//            ok1 = input.nextLine();
//        }
//        while (!ok1.equalsIgnoreCase("ok"));
//        System.out.println();
//        System.out.println("--------------------Now the fallen room (defend unsucessfully) ------------------- ");
//        gameBroad.printRooms();
//        System.out.println();
//
//        for (int i=0; i<gameBroad.getRooms().size(); i++) {
//            if (gameBroad.getRooms().get(i).isFallen()) {
//                List<Item> usedItemsList = new ArrayList<>();
//                Room fallenRoom = gameBroad.getRooms().get(i);
//                HashSet<Playable> playersInTheRoom = gameBroad.WhoCan(fallenRoom.existCharacterColor());
//                System.out.println(fallenRoom.getName() + " has fallen, one character will be eaten");
//                //using items to save the room
//                if (teamHasOtherItems(playersInTheRoom)&& fallenRoom.getRoomNum()!=4) {
//                        String yes = "";
//                        do {
//                            Scanner input = new Scanner(System.in);
//                            System.out.println("This result can change by using item, anyone in the room want to use Items?(y/n)");
//                            System.out.println("---------------------------------------------------------------");
//                            System.out.println("Items function description");
//                            System.out.println("Axe: kill one zombie");
//                            System.out.println("ShortGun: kill two zombies");
//                            System.out.println("Hardware: add one defend for a turn");
//                            System.out.println("Sprint: escape to the other room");
//                            System.out.println("Hidden: your character will not be killed nor can joined the voting");
//                            System.out.println("---------------------------------------------------------------");
//                            System.out.println("This result can change by using item, anyone in the room want to use Items?(y/n)");
//                            yes = input.nextLine();
//                            if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                                System.out.println("Please enter y or n");
//                            }
//                        }
//                        while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                        if (yes.equalsIgnoreCase("y")) {
//                         String yes3="";
//                         do {
//                             for (Playable player : playersInTheRoom) {
//                                 if (player.hasOthersItems()) {
//                                     int itemselected = 0;
//                                     boolean loop3 = false;
//                                     do {
//                                         try {
//                                             loop3 = false;
//                                             System.out.println( player + " please select which items you want to use");
//                                             System.out.print("Your item list:");
//                                             printTheListWithNumber(player.otherItemsList());
//                                             System.out.println();
//                                             Scanner input1 = new Scanner(System.in);
//                                             System.out.print("please type in (");
//                                             printTheOptions(player.otherItemsList());
//                                             System.out.print(")");
//                                             System.out.println();
//                                             itemselected = input1.nextInt();
//                                             if (itemselected < 0 || itemselected > player.otherItemsList().size()) {
//                                                 System.out.println("please type in ");
//                                                 printTheOptions(player.otherItemsList());
//                                                 loop3 = true;
//                                             }
//                                         } catch (Exception e) {
//                                             System.out.println("Please enter a number");
//                                             loop3 = true;
//                                         }
//                                     }
//                                     while (loop3);
//                                     Item usedItem = player.otherItemsList().get(itemselected - 1);
//                                     usedItemsList.add(usedItem);
//                                     usedItem.effect(player, fallenRoom);
//                                     player.usedItem(usedItem);
//                                 }
//                             }
//                             System.out.println("After using the items, the room is ended in below result");
//                             gameBroad.printRooms();
//                             Scanner input = new Scanner(System.in);
//                             System.out.println("Please confirmed no more items will be used (y - no more item will be used/n - more item will be used)");
//                             yes3 = input.nextLine();
//                             if (!yes3.equalsIgnoreCase("n") && !yes3.equalsIgnoreCase("y")) {
//                                 System.out.println("Please enter y or n");
//                             }
//                         }
//                         while (yes3.equalsIgnoreCase("n"));
//                        }
//                        if (fallenRoom.isFallen()){
//                        System.out.println(fallenRoom.getName() + "is still fallen," );
//                            System.out.println("One character will be eaten");
//                         }
//                        }
//                        // items using ended
//                        if (fallenRoom.isFallen()){
//                            String vote= "";
//                            List<String> votes = new ArrayList<>();
//                         for (Playable player : playersInTheRoom){
//                             do {
//                                 Scanner input = new Scanner(System.in);
//                                 System.out.println(player + " please vote color in the list:" + playersInTheRoom );
//                                 vote=input.nextLine();
//                                 if (!colorcorrectselect(playersInTheRoom,vote)){
//                                     System.out.println("Please select correct color.");
//                                 }
//                                 else {
//                                     votes.add(player.getColor());
//                                     votes.add(vote);
//                                 }
//                             }
//                             while (!colorcorrectselect(playersInTheRoom,vote));
//                         }
//                            fallenRoom.resetVoteResult();
//                            fallenRoom.voteResultAfterVote(votes);
//                            System.out.println("------------------------------Voting Results------------------------------------------------");
//                            for (int l=0; l<votes.size(); l+=2){
//                                System.out.println(gameBroad.matchPlayer(votes.get(l)) +  " has voted " + votes.get(l+1));
//                            }
//                            System.out.println("Summary: " + fallenRoom.getCurrentVoteResult());
//                            System.out.println("------------------------------------------------------------------------------");
//                            //using Threat to change result;
//                            if (teamHasThreat(playersInTheRoom)){
//                                String yes = "";
//                                do {
//                                    Scanner input = new Scanner(System.in);
//                                    System.out.println("Voting result can be changed by item THREAT, anyone want to change the result?(y/n)");
//                                    yes = input.nextLine();
//                                    if (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y")) {
//                                        System.out.println("Please enter y or n");
//                                    }
//                                }
//                                while (!yes.equalsIgnoreCase("n") && !yes.equalsIgnoreCase("y"));
//                                if (yes.equalsIgnoreCase("y")){
//                                    String quitThreatused = "";
//                                    do {
//                                        int numThreat = 0;
//                                        for (Playable teammember: playersInTheRoom){
//                                            boolean loop4 = false;
//                                            do {
//                                                loop4=false;
//                                                try {
//                                                    Scanner input = new Scanner(System.in);
//                                                    System.out.println(teammember + " please select how many THREAT you want use? from 0 to " + teammember.threatNum() );
//                                                    System.out.println("Your current item list:" + teammember.getCurrentItem());
//                                                    numThreat = input.nextInt();
//
//                                                    if (numThreat<0 || numThreat>teammember.threatNum()){
//                                                        System.out.println("Please enter a valid amount");
//                                                    }
//
//                                                }
//                                                catch (Exception e){
//                                                    System.out.println("Please enter a number");
//                                                    loop4 = true;
//                                                }
//                                            }
//                                            while (loop4);
//                                            String effectedColor = "";
//                                            for (int j = 0; j<votes.size(); j+=2){
//                                                if (teammember.getColor().equalsIgnoreCase(votes.get(j))){
//                                                    effectedColor = votes.get(j+1);
//                                                }
//                                            }
//                                            effectedColor = effectedColor.toUpperCase();
//                                            fallenRoom.voteResultAfterItem(effectedColor,numThreat);
//                                            for (int q=0; q<numThreat; q++){
//                                                Item threat = gameBroad.matchItem(teammember,"Threat");
//                                                teammember.usedItem(threat);
//                                            }
//                                        }
//                                        System.out.println("Result after the THREAT used: " + fallenRoom.getCurrentVoteResult());
//                                        do {
//                                            Scanner input = new Scanner(System.in);
//                                            System.out.println("Please confirm no more THREAT will be used (y - there will be more THREAT/n - no more THREAT)");
//                                            quitThreatused = input.nextLine();
//                                            if (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y")) {
//                                                System.out.println("Please enter y or n");
//                                            }
//                                        }
//                                        while (!quitThreatused.equalsIgnoreCase("n") && !quitThreatused.equalsIgnoreCase("y"));
//                                    }
//                                    while (quitThreatused.equalsIgnoreCase("y"));
//                                }
//                            }
//                            //result print
//                            Playable loser = new Playable();
//                            if (fallenRoom.winner().equals("TIE")){
//                                System.out.println("Result is TIE");
//                                System.out.println("The result will be ramdomly choosed");
//                                List<Playable> losers = new ArrayList<>();
//                                for (Playable player: playersInTheRoom){
//                                    losers.add(player);
//                                }
//                                Random generator = new Random();
//                                int num = generator.nextInt(losers.size());
//                                loser = losers.get(num);
//                                System.out.println(loser +  " has been chosed, one of character his/her character in the room will be eaten. ");
//                            }
//                            else {
//                                System.out.println("Voting Result: " + fallenRoom.getCurrentVoteResult());
//                                String winnercolor = fallenRoom.winner();
//                                System.out.println(gameBroad.matchPlayer(winnercolor) + " get most vote and one of his/her character in the room will be eaten.");
//                                loser = gameBroad.matchPlayer(winnercolor);
//                            }
//                            //the chosen player lost his/her character in the room
//                            String deathCharacterStr = "";
//                            HashSet<GameCharacter> availableOptionsSet = fallenRoom.existChracterForThatPlayer(loser);
//                            List<GameCharacter> availableOptions =  new ArrayList<GameCharacter>();
//                            for (GameCharacter character: availableOptionsSet){
//                                availableOptions.add(character);
//                            }
//                            do {
//                                Scanner input = new Scanner(System.in);
//                                System.out.println(loser + " please selected your character in the list " + availableOptions);
//                                deathCharacterStr = input.nextLine();
//                                if (!characterCorrectSelectForCertianList(availableOptions, deathCharacterStr)){
//                                    System.out.println("Please select correct character");
//                                }
//                            }
//                            while (!characterCorrectSelectForCertianList(availableOptions, deathCharacterStr));
//                            GameCharacter deathCharacter = gameBroad.matchGameCharacter(loser,deathCharacterStr);
//                            loser.removeCharacter(deathCharacter);
//                            fallenRoom.leave(deathCharacter);
//                            System.out.println(loser + " has lost his/her " + deathCharacterStr);
//                            fallenRoom.setCurrentZombienumber(0);
//                            System.out.println("Zombies have their feast, and returned back to somewhere else to find their next target!");
//                            System.out.println();
//                            System.out.println("-------------------------------------------------------------------------");
//
//                        }
//                        //after effect for certain items
//                        for (Item item: usedItemsList){
//                            item.afterEffect(gameBroad);
//                        }
//                        usedItemsList.clear();
//                    }
//            }
//            for (Playable player: gameBroad.getPlayers()){
//            if (player.remaingCharacter()==0){
//                System.out.println("Due to " + player + " has no more character, " + player + " has been removed from the Model.Game Broad.");
//                gameBroad.removePlayer(player);
//            }
//            }
//
//
//        }
//        System.out.println("Current Model.Character has now reached four, the game has reached an end");
//        int mostPoints = gameBroad.getPlayers().get(0).totalVictoryPoints();
//        int q = 0;
//        int count =0;
//        for (int i=0; i<gameBroad.getPlayers().size();i++){
//        if (mostPoints<gameBroad.getPlayers().get(i).totalVictoryPoints()){
//            mostPoints = gameBroad.getPlayers().get(i).totalVictoryPoints();
//            q = i;
//        }
//        }
//        for (Playable player: gameBroad.getPlayers()){
//            if(mostPoints == player.totalVictoryPoints()){
//                count++;
//            }
//        }
//        if (count>1){
//            System.out.println("Result is TIE, ");
//        }
//        else {
//            Playable winner  = gameBroad.getPlayers().get(q);
//            System.out.println("Congratulations! Winner is " + winner + " with a victory points" + mostPoints);
//        }
//    }
//
//
//    public static boolean charactercorrectselect(Playable player, String selectchar){
//        for (int i=0; i<player.getCharactersselect().size(); i++){
//            if (selectchar.equalsIgnoreCase(player.getCharactersselect().get(i).getName())){
//                return true;
//            }
//        }
//      return false;
//    }
//
//    public static boolean characterCorrectSelectInGame(Playable player, String selectchar){
//        for (int i=0; i<player.getGameCharacters().size(); i++){
//            if (selectchar.equalsIgnoreCase(player.getGameCharacters().get(i).getName())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean characterCorrectSelectForCertianList(List<GameCharacter> theList, String selectchar){
//        for (int i=0; i<theList.size(); i++){
//            if (selectchar.equalsIgnoreCase(theList.get(i).getName())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public static boolean colorcorrectselect(HashSet<Playable> voteplayerlist, String votecolor){
//        for (Playable player: voteplayerlist){
//            if (votecolor.equalsIgnoreCase(player.getColor())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean teamHasThreat(HashSet<Playable> players){
//        for (Playable player: players){
//           if (player.hasThreat())
//               return true;
//        }
//        return false;
//    }
//
//    public static boolean teamHasSecurityCamera(List<Playable> players){
//        for (Playable player: players){
//            if (player.hasSecurityCamera()){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean teamHasOtherItems(HashSet<Playable> players){
//        for (Playable player: players){
//            if (player.hasOthersItems()){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * print the list in list form 1.xxx 2.yyyy 3........
//     * @param objects the list
//     */
//
//    public static void printTheListWithNumber(List<Item> objects){
//        int start = 1;
//        for (Object o: objects){
//            System.out.print(start + "." + o + ", ");
//        }
//
//    }
//
//    /**
//     * print the list option 1. 2. 3. ...........
//     * @param objects the list
//     */
//
//    public static void printTheOptions(List<Item> objects){
//        int start = 1;
//        for (Object o: objects){
//            System.out.print(start + ".");
//        }
//
//    }
//
//    public static List<GameCharacter> characterNotInTheRoom (Room room, Playable player) {
//        List<GameCharacter> notInTheRoomList = new ArrayList<GameCharacter>();
//        for (GameCharacter character: player.getGameCharacters()){
//            if (!room.inTheRoom(character)){
//                notInTheRoomList.add(character);
//            }
//        }
//
//        return notInTheRoomList;
//    }
//}
