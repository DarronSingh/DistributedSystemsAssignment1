import java.io.*;
import java.io.DataInputStream;
import java.net.*;
import java.util.Scanner;
/*
The client class contains the prompts for the users once they connect to the server and begin to play the hangman game
Client class uses sockets to establish the connection to the server

@author Darron Singh, 100584624
@date modified October 2 2019
*/

public class HangmanClient {

    /*
    This class only contains one main method that takes the clients through the game using Scanners
    and various input/output streams.

    @param arguments for the computer name and post number
    @return none since main method
     */

    public static void main(String args[]) throws Exception {
        int lives = 6;

        if (args.length != 2) {     //Makes sure that the user enters a host name and port number, for demonstration purposes host will be localhost
            System.err.println("Usage: java hangmanClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket hangmanSocket = new Socket(hostName, portNumber);    //creates a socket with the hostname and port number being required to be entered by the user
            DataInputStream read = new DataInputStream(hangmanSocket.getInputStream());
            DataOutputStream write = new DataOutputStream(hangmanSocket.getOutputStream());
            //DataInputStream in = new DataInputStream(System.in);
            Scanner choice = new Scanner(System.in);
            Scanner hint = new Scanner(System.in);
            Scanner guessWord = new Scanner(System.in);
            Scanner player2Input = new Scanner(System.in);
            boolean wordCompleted = false;

            System.out.println("Please select the number for the topic that you want to play:");    //First asks the user what theme they wish to select
            System.out.println("1. Animals");
            System.out.println("2. Fruits");
            System.out.println("3. Country");
            int themeChoice = choice.nextInt();
            write.writeInt(themeChoice);    //sends this choice to the connection class for processing

            String wordToGuess = read.readUTF();    //reads the random word that was chosen by the connection class and displays it to the client
            System.out.println(wordToGuess);

            //as long as the client has more than 6 lives and the word has not been completed they wll go through the loop below
            while (lives > 0 && wordCompleted == false) {
                System.out.println("Please choose an option, you start with 6 lives: ");
                System.out.println("1. Reveal a letter ");
                System.out.println("2. Guess a letter.");
                System.out.println("3. Guess the word.");
                System.out.println("NOTE: A reveal costs 2 lives");
                int playerOption = hint.nextInt();
                write.writeInt(playerOption);
                System.out.println("===================");

                //if the user selects option 2 they will be given a prompt to guess the letter, that value is then written to the connection class
                if (playerOption == 2) {
                    System.out.println("Please guess a letter in the word");
                    String playerGuess = player2Input.nextLine();
                    write.writeUTF(playerGuess);
                }
                //if the user selects option 3 they will be given a prompt to guess the word, that word will then be sent to the connection class to be checked.
                else if (playerOption == 3) {
                    System.out.println("Enter word that you wish to guess");
                    String guess = guessWord.nextLine();
                    write.writeUTF(guess);
                }

                int newLife = read.readInt();
                String newWord = read.readUTF();
                // lives count is updated
                lives = newLife;
                System.out.println("===================");
                System.out.println("Lives Remaining: " + lives);
                System.out.println(newWord);
                System.out.println("===================");

                wordCompleted = read.readBoolean();
            }
            //win/lose condition is called depending on whether to player solved the word or not
            if (wordCompleted) {
                System.out.println("  _____  _                        __          ___           _ _ ");
                System.out.println(" |  __ \\| |                       \\ \\        / (_)         | | |");
                System.out.println(" | |__) | | __ _ _   _  ___ _ __   \\ \\  /\\  / / _ _ __  ___| | |");
                System.out.println(" |  ___/| |/ _` | | | |/ _ \\ '__|   \\ \\/  \\/ / | | '_ \\/ __| | |");
                System.out.println(" | |    | | (_| | |_| |  __/ |       \\  /\\  /  | | | | \\__ \\_|_|");
                System.out.println(" |_|    |_|\\__,_|\\__, |\\___|_|        \\/  \\/   |_|_| |_|___(_|_)");
                System.out.println("                  __/ |                                         ");
                System.out.println("                 |___/                                          ");
            } else {
                System.out.println(" __     __           _                             ");
                System.out.println(" \\ \\   / /          | |                          ");
                System.out.println("  \\ \\_/ /__  _   _  | |     ___  ___  ___        ");
                System.out.println("   \\   / _ \\| | | | | |    / _ \\/ __|/ _ \\      ");
                System.out.println("    | | (_) | |_| | | |___| (_) \\__ \\  __/_ _ _  ");
                System.out.println("    |_|\\___/ \\__,_| |______\\___/|___/\\___(_|_|_) ");
                System.out.println("                                                     ");
                System.out.println("                                                    ");
            }


        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }
    }

}
