import java.util.Scanner;


public class HangmanGame {

    public static void main(String args[]) {
        Scanner player1Input = new Scanner(System.in);
        System.out.println("Enter the word you wish to use");
        String wordInput = player1Input.nextLine();
        playGame(wordInput);
    }


    public static void playGame(String wordInput) {
        int lives = 6;
        String word = "";

        for (int i = 0; i < wordInput.length(); i++) {
            word = word + "*";
        }

        while (lives > 0) {

            System.out.println("Please guess a letter in the word");
            Scanner player2Input = new Scanner(System.in);
            char guess = player2Input.next(".").charAt(0); //takes player input and converts to char
            int match = 0;

            for (int i = 0; i < wordInput.length(); i++) {
                char c = wordInput.charAt(i);
                if (guess == c) {
                    word = word.substring(0, i) + guess + word.substring(i + 1);
                    match++;
                }
            }

            if (match == 0) {
                lives--;
                System.out.println("Incorrect guess. " + lives + " lives remaining");
            } else {
                System.out.println(match + " match(es) found");
                System.out.println(word);
            }


        }
    }
}
