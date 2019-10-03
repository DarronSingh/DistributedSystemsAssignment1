import java.net.*;
import java.io.*;
import java.util.Random;

/*
The connection class contains the main code for the Hangman Game.  Once the server class opens a new connection with the client
the connection class gets called to open up input/output streams and then the game automatically starts

@author Darron Singh, 100584624
@date modified October 2 2019
*/
public class Connection extends Thread {
    Socket client;
    PrintWriter out;
    BufferedReader in;
    DataInputStream input;
    DataOutputStream write;
    DataInputStream read;


    final String animals[] = {"fish", "cobra", "dolphin", "moose", "hummingbird", "jaguar", "squirrel"};    //arrays that set up the word list for the clients
    final String fruit[] = {"banana", "orange", "coconut", "cucumber", "huckleberry", "pineapple", "strawberry"};
    final String country[] = {"canada", "guyana", "finland", "australia", "kyrgyzstan", "jamaica", "madagascar"};
    String words[];
    String word = "";
    int lives = 6;
    Random random = new Random();
    int index = random.nextInt(animals.length);
    String chosenWord;
    String guess = "";
    boolean correctGuess = false;
    boolean wordCompleted = false;

    public Connection(Socket s) { // constructor called by the server class
        client = s;

        try {
            out = new PrintWriter(client.getOutputStream(), true);      //sets up input and output streams
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException ex) {
                System.out.println("Error while getting socket streams.." + ex);
            }
            return;
        }
        this.start(); // Thread starts here...this start() will call run()
    }

    /*
    Main method that gets called in the constructor, runs the hangman game once a client has connected to the server

    @ param none
    @ return continuously communicates with the client sending and receiving messages
     */
    public void run() {
        try {
            read = new DataInputStream(client.getInputStream());
            write = new DataOutputStream(client.getOutputStream());
            input = new DataInputStream(System.in);

            /*
            Takes the player's choice of theme and selects the appropriate array, stores those elements in the words array
             */
            int choice = read.readInt();
            if (choice == 1) {
                words = animals;
            } else if (choice == 2) {
                words = fruit;
            } else if (choice == 3) {
                words = country;
            }

            /*
            Takes the random word that was selected and replaces each letter with asterisks for the user to guess
             */
            for (int i = 0; i < words[index].length(); i++) {
                word = word + "*";
            }
            write.writeUTF(word);   //sends the masked word back to the client
            chosenWord = words[index];

            //While loop runs as long as client has enough lives and has not solved the word yet
            while (lives > 0 && wordCompleted == false) {

                int option = read.readInt();
                if (option == 1) {              //the user has 3 options, if the first option has been chosen they have selected for a random letter to be revealed
                    int randomChar = random.nextInt(chosenWord.length());
                    char charReveal = chosenWord.charAt(randomChar);
                    for (int i = 0; i < chosenWord.length(); i++) {
                        char c = chosenWord.charAt(i);
                        if (charReveal == c) {
                            word = word.substring(0, randomChar) + charReveal + word.substring(randomChar + 1);
                        }
                    }
                    lives = lives - 2;
                } else if (option == 2) {       //if the second option is selected they have decided to guess a letter in the word
                    guess = read.readUTF();
                    char guessedCharacter = guess.charAt(0);    //takes the letter that was sent by the client and converts to character to be compared with each character in the word
                    System.out.println(guess);
                    System.out.println(guessedCharacter);

                    for (int i = 0; i < chosenWord.length(); i++) {
                        char c = chosenWord.charAt(i);
                        if (guessedCharacter == c) {
                            correctGuess = true;
                            word = word.substring(0, i) + guess + word.substring(i + 1);

                        }
                    }
                    if (correctGuess == false) {    //takes a life if the submitted character is wrong
                        lives--;
                    } else {
                        correctGuess = false;
                    }
                } else if (option == 3) {       //if the user selects option 3 it means they wish to guess the word.
                    String guessWord = read.readUTF();
                    if (guessWord.equals(chosenWord)) {
                        wordCompleted = true;
                        System.out.println("option 3 " + wordCompleted);
                    } else {
                        lives--;
                    }
                }

                write.writeInt(lives);  //sends the current life count and word back to client
                write.writeUTF(word);

                if (word.equals(chosenWord)) {  //if the user's guess equals the word selected then word completed is true and the win condition is called, else the lose condition is called
                    wordCompleted = true;

                }
                write.writeBoolean(wordCompleted);
            }

            client.close();
        } catch (
                IOException e) {
            System.out.println("Exception caught...");
            System.out.println(e.getMessage());
        }
    }
}

