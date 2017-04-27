package poker;

import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class GameOfPoker extends Thread{
    static final int NUMBER_OF_BOTS = 4;
    private static final int DISCARD_MINIMUM_RANGE = 15;
    private static final int DISCARD_MAXIMUM = 90;
    static final int HUMAN_INDEX = 0;
    private TwitterInterface twitter_interface;
    private ArrayList<PokerPlayer> player_list;
    private long tid;
    private DeckOfCards deck;
    String tname;

    public GameOfPoker(TwitterInterface t_interface, String name, long id) {
        twitter_interface = t_interface;
        tname = name;
        tid = id;
    }

    public void run() {
        createPlayers();
        try{
            playGame();
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public void createPlayers() {
        Random rand = new Random();
        player_list = new ArrayList<>();
        deck = new DeckOfCards();
        player_list.add(new HumanPlayer(twitter_interface, tname, tid));
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+(DISCARD_MAXIMUM-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum));
            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
    }

    public void playGame() throws TwitterException {
        HumanPlayer humanPlayer = (HumanPlayer) player_list.get(GameOfPoker.HUMAN_INDEX);
        Boolean game_over = false;
        String win_message = "";


        twitter_interface.postReply(humanPlayer.name +" You have tweeted the RoyalSampler hashtag to play Poker\nWelcome to 5 card draw Poker!", player_list.get(HUMAN_INDEX));
        String opponent_names = "";
        for(int i=HUMAN_INDEX + 1; i<player_list.size(); i++){
            opponent_names += player_list.get(i).getName() +"\n";
        }
        twitter_interface.postReply("Your opponents are:\n"+ opponent_names, player_list.get(HUMAN_INDEX));

        while(!game_over){
            deck.reset();
            RoundOfPoker round = new RoundOfPoker(player_list, deck,twitter_interface);
            round.playRound();

            String tweet_message = "";
            for(int i=0; i<player_list.size(); i++){
                if(player_list.get(i).chips < RoundOfPoker.ANTE) {
                    tweet_message += player_list.get(i).getName() + " has run out of chips and is eliminated\n";
                    if(i == HUMAN_INDEX) {
                        win_message = "You have been eliminated from the game.\nThanks for playing, better luck next time!";
                        game_over = true;
                        break;
                    }
                    else {
                        player_list.remove(i);
                        i--;
                    }
                }

            }

            if(!tweet_message.equals("")){
                twitter_interface.postReply(tweet_message, humanPlayer);
            }

            if(player_list.size()==1){
                game_over = true;
                win_message = "You have won. Congratulations!\nThank you for playing";
            }
        }
        twitter_interface.postReply(win_message, humanPlayer);
    }

    public void quitMessage(){
        twitter_interface.postReply("You have tweeted the hashtag to end the game\nThank You for playing!", player_list.get(GameOfPoker.HUMAN_INDEX));
    }

    // main method initialising twitter listener and waiting for game start request
    public static void main(String[] args) {
    /*  TwitterInterface twitterInterface = null;
        try {
            twitterInterface = new TwitterInterface();
        } catch(IOException e) {
            e.printStackTrace();
        }
        GameOfPoker gameOfPoker = new GameOfPoker(twitterInterface);
        gameOfPoker.start();
        boolean flag;
        do {
            flag = gameOfPoker.checkwanttoplay();
            gameOfPoker.runRounds(flag);
        }
        while (flag != true);
        gameOfPoker.exit();*/
    }
}
