package bullscows;

import java.util.Scanner;
import java.util.Random;

public class GameProcess {

    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private int turnCount = 1;
    private int uniqueSymbols;

    private String secretCode;

    private void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    private String getSecretCode() {
        return secretCode;
    }

    private void generateSecretCode() {

        int secretLength = askLength();

        uniqueSymbols = askUniqueSymbols(secretLength);

        RandomInRanges randomInRanges = new RandomInRanges();

        if (uniqueSymbols <= 10) {
            System.out.println(uniqueSymbols);
            randomInRanges.addRange(48, 57 - (10 - uniqueSymbols));
        } else {
            randomInRanges.addRange(48, 57);
            randomInRanges.addRange(97, 122 - (26 - (uniqueSymbols - 10)));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretLength; ++i) {
            char ch;
            while (true) {
                ch = (char)randomInRanges.getRandom();
                if (!sb.toString().contains("" + ch)) {
                    break;
                }
            }
            sb.append(ch);
        }
        setSecretCode(sb.toString());
    }

    public void makeTurn() {

        System.out.printf("Turn %d:\n", turnCount);

        String input;
        do {
             input = scanner.nextLine();
        } while (input.isEmpty());

        int[] arrayCowsBulls = checkCowsAndBulls(input);

        StringBuilder sb = new StringBuilder();

        if (arrayCowsBulls[0] == 0 && arrayCowsBulls[1] == 0) {
            sb.append("None");
        } else if (arrayCowsBulls[1] == secretCode.length()) {
            System.out.printf("Grade: %d bulls.\n", arrayCowsBulls[1]);
            System.out.println("Congratulations! You guessed the secret code.");
            return;
        } else if (arrayCowsBulls[0] != 0 && arrayCowsBulls[1] == 0) {
            sb.append("Grade: " + arrayCowsBulls[0] + " cow");
            if (arrayCowsBulls[0] > 1) {
                sb.append("s");
            }
            sb.append("\n");
        } else if (arrayCowsBulls[0] == 0 && arrayCowsBulls[1] != 0) {
            sb.append("Grade: " + arrayCowsBulls[1] + " bull");
            if (arrayCowsBulls[1] > 1) {
                sb.append("s");
            }
        } else {
            sb.append("Grade: " + arrayCowsBulls[0] + " bull");
            if (arrayCowsBulls[0] > 1) {
                sb.append("s");
            }
            sb.append(" and " + arrayCowsBulls[0] + " cow");
            if (arrayCowsBulls[1] > 1) {
                sb.append("s");
            }
        }

        System.out.println(sb);
        ++turnCount;
        makeTurn();
    }

    private int[] checkCowsAndBulls(String input) {

        int countBull = 0;
        int countCow = 0;

        int[] arr1 = new int[uniqueSymbols];
        int[] arr2 = new int[uniqueSymbols];

        for(int i = 0; i < secretCode.length(); i++){
            char ch1 = secretCode.charAt(i);
            char ch2 = input.charAt(i);

            if(ch1 == ch2)
                countBull++;
            else{
                if (ch1 >= 'a') {
                    arr1[ch1 - 'a' + 10]++;
                } else {
                    arr1[ch1 - '0']++;
                }
                if (ch2 >= 'a') {
                    arr1[ch2 - 'a' + 10]++;
                } else {
                    arr1[ch2 - '0']++;
                }
            }
        }

        for(int i = 0; i < uniqueSymbols; i++){
            countCow += Math.min(arr1[i], arr2[i]);
        }

        return new int[] {countCow, countBull};
    }

    public void start() {
        generateSecretCode();
        printSecretCodeLine();
        System.out.println("Okay, let's start a game!");
        makeTurn();
    }

    private int askLength() {
        System.out.println("Please, enter the secret code's length:");
        String inputString = scanner.next();
        int input = 0;
        try {
            input = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            System.out.printf("Error: %s isn't a valid number.\n", inputString);
            System.exit(0);
        }
        if (input > 36 || input < 1) {
            System.out.printf("Error: can't generate a secret number with a length of %s because there aren't enough unique characters.\n", input);
            System.exit(0);
        }
        return input;
    }

    private int askUniqueSymbols(int secretLength) {
        System.out.println("Input the number of possible symbols in the code:");
        String inputString = scanner.next();
        int input = 0;
        try {
            input = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            System.out.printf("Error: %s isn't a valid number.\n", inputString);
            System.exit(0);
        }
        if (input > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }
        if (input < secretLength) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.\n", secretLength, input);
            System.exit(0);
        }
        return input;
    }

    private void printSecretCodeLine() {
        StringBuilder sb = new StringBuilder();
        sb.append("The secret is prepared: ");
        for (int i = 0; i < getSecretCode().length(); ++i) {
            sb.append("*");
        }
        sb.append(" (0-");
        if (uniqueSymbols <= 10) {
            sb.append(uniqueSymbols - 1);
        } else {
            sb.append("9, ");
            sb.append("a-");
            sb.append((char)(97 + (uniqueSymbols - 11)));
        }
        sb.append(").");
        System.out.println(sb);
    }

}
