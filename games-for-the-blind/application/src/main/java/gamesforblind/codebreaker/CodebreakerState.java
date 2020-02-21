package gamesforblind.codebreaker;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.awt.*;
import java.util.*;

import static gamesforblind.Constants.CODEBREAKER_MAX_CODE_INT;

/**
 * Class that contains information about the current state of the Codebreaker board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Codebreaker.
 */
public class CodebreakerState {
    private final CodebreakerType codebreakerType;
    private final AudioPlayerExecutor audioPlayerExecutor;

    private boolean gameOver;
    private int[] codeToBreak;
    private Integer[] currentGuess;
    private int[][] pastGuesses = new int[20][8];
    private ArrayList<CodebreakerGuess> guessList;
    private Point selectedCellPoint;

    private int j = 0;
    private int k = 0;
    public int h = 0;
    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor, @NotNull CodebreakerType codebreakerType) {
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.codebreakerType = codebreakerType;

        this.initNewCodebreakerGame();
    }

    public boolean initNewCodebreakerGame() {
        this.gameOver = false;
        this.codeToBreak = this.generateCorrectCode();
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
        this.guessList = new ArrayList<>();
        this.selectedCellPoint = new Point();

        return true;
    }

    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    public static boolean checkThatGameIsOver(
            @NotNull int[] codeToBreak, @NotNull ArrayList<CodebreakerGuess> guessList) {
        Objects.requireNonNull(codeToBreak, "Code to break cannot be null!");
        Objects.requireNonNull(guessList, "Guess list must not be null!");

        CodebreakerType codebreakerType = null;
        for (CodebreakerType type : CodebreakerType.values()) {
            if (codeToBreak.length == type.getCodeLength()) {
                codebreakerType = type;
            }
        }

        if (codebreakerType == null) {
            throw new IllegalArgumentException("Invalid code length passed to checker function!");
        }

        if (guessList.size() == codebreakerType.getCodeLength()) {
            return true;
        }

        return codeToBreak.length == guessList.get(guessList.size() - 1).getNumberInCorrectPosition();
    }

    private int[] generateCorrectCode() {
        final Random RANDOM = new Random();
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();

        int[] codeToBreak = new int[CODE_LENGTH];
        for (int i = 0; i < codeToBreak.length; i++) {
            codeToBreak[i] = RANDOM.nextInt(CODEBREAKER_MAX_CODE_INT) + 1;
        }
        return codeToBreak;
    }

    public void setCodebreakerGuess() {
        //creates a past guesses array for reading back past rows
        for(int j = 0; j < 1; j++){
            for(int k = 0; k < codeToBreak.length; k++){
                    pastGuesses[h][k] = this.currentGuess[k];
            }
            h++;
        }

        for (Integer i : this.currentGuess) {
            if (i == null) {
                this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_NEED_CODE);
                return;
            }
        }


        CodebreakerGuess currentCodebreakerGuess = new CodebreakerGuess(this.codeToBreak, this.currentGuess);
        this.guessList.add(currentCodebreakerGuess);

        //for reading back later
        pastGuesses[h-1][6] = currentCodebreakerGuess.getNumberInCorrectPosition();
        pastGuesses[h-1][7] = currentCodebreakerGuess.getNumberOfCorrectColor();

        ArrayList<Phrase> relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.PLACED_CODEBREAKER_CODE));
        for (int i : this.currentGuess) {
            relevantPhrases.add(Phrase.convertIntegerToPhrase(i));
        }


        Phrase[] phrasesToAdd = {
                Phrase.CODEBREAKER_GUESS_NUMBER,
                Phrase.convertIntegerToPhrase(this.guessList.size()),
                Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                Phrase.convertIntegerToPhrase(currentCodebreakerGuess.getNumberInCorrectPosition()),
                Phrase.CODEBREAKER_NUMBER_ONLY,
                Phrase.convertIntegerToPhrase(currentCodebreakerGuess.getNumberOfCorrectColor())
        };
        relevantPhrases.addAll(Arrays.asList(phrasesToAdd));

        this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrases);
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];

        this.selectedCellPoint.x = 0;
        this.selectedCellPoint.y++;
    }

    public void setSingleNumber(int numberToSet) {
        this.audioPlayerExecutor.replacePhraseAndPrint(
                new ArrayList<>(Arrays.asList(Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(numberToSet)))
        );

        this.currentGuess[this.selectedCellPoint.x] = numberToSet;
    }

    public void changeSelectedCellPoint(@NotNull ArrowKeyDirection arrowKeyDirection) {
        switch (arrowKeyDirection) {
            case LEFT:
                if (this.selectedCellPoint.x == 0) {
                    this.selectedCellPoint.x = this.codebreakerType.getCodeLength() - 1;
                } else {
                    this.selectedCellPoint.x--;
                }
                break;

            case RIGHT:
                if (this.selectedCellPoint.x == this.codebreakerType.getCodeLength() - 1) {
                    this.selectedCellPoint.x = 0;
                } else {
                    this.selectedCellPoint.x++;
                }
                break;
            case UP:
                if(this.selectedCellPoint.y == 0 ){}
                else {
                    this.selectedCellPoint.y--;
                }
                break;
            case DOWN:
                if(this.codeToBreak.length == 4 && this.selectedCellPoint.y == 11){}
                else if(this.codeToBreak.length == 5 && this.selectedCellPoint.y == 14){}
                else if(this.codeToBreak.length == 6 && this.selectedCellPoint.y == 19){}
                else {
                    this.selectedCellPoint.y++;
                }
                break;

        }
    }

    /**
     * Reads the instructions for the codebreaker game of difficulty 4, 5, 6 respectively
     */
    public void readInstructions() {
        ArrayList<Phrase> instructionsPhrases;
        switch (this.codebreakerType) {
            case FOUR:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_4,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_4_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case FIVE:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_5,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_5_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case SIX:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_6,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_6_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Invalid codebreaker type: '%s'!", this.codebreakerType)
                );
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(instructionsPhrases);
    }
    public void readBackRow() {

        ArrayList<Phrase> readBackPhrases;
        int a, b, c, d, e, f, g, o = 0;
        a = pastGuesses[selectedCellPoint.y][0];
        b = pastGuesses[selectedCellPoint.y][1];
        c = pastGuesses[selectedCellPoint.y][2];
        d = pastGuesses[selectedCellPoint.y][3];
        e = pastGuesses[selectedCellPoint.y][4];
        f = pastGuesses[selectedCellPoint.y][5];
        g = pastGuesses[selectedCellPoint.y][6];
        o = pastGuesses[selectedCellPoint.y][7];

        /* For reading off rows that havent been submitted yet, TODO
        for(int z = 0; z < 6; z++){
                if(this.currentGuess[z] == null || this.currentGuess[z] == 0) {
                    this.currentGuess[z] = 0;
                }
        }*/
        //read empty row
        if(pastGuesses[selectedCellPoint.y][0] == 0){
            if(codeToBreak.length == 4){
                readBackPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.CODEBREAKER_READ_ROW,
                        Phrase.convertIntegerToPhrase(selectedCellPoint.y+1),
                        Phrase.CODEBREAKER_UNKNOWN_GUESS,
                        Phrase.convertIntegerToPhrase(a),
                        Phrase.convertIntegerToPhrase(b),
                        Phrase.convertIntegerToPhrase(c),
                        Phrase.convertIntegerToPhrase(d)
                ));
            }
            else if(codeToBreak.length == 5){
                readBackPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.CODEBREAKER_READ_ROW,
                        Phrase.convertIntegerToPhrase(selectedCellPoint.y+1),
                        Phrase.CODEBREAKER_UNKNOWN_GUESS,
                        Phrase.convertIntegerToPhrase(a),
                        Phrase.convertIntegerToPhrase(b),
                        Phrase.convertIntegerToPhrase(c),
                        Phrase.convertIntegerToPhrase(d),
                        Phrase.convertIntegerToPhrase(e)
                ));
            }
            else{
                readBackPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.CODEBREAKER_READ_ROW,
                        Phrase.convertIntegerToPhrase(selectedCellPoint.y+1),
                        Phrase.CODEBREAKER_UNKNOWN_GUESS,
                        Phrase.convertIntegerToPhrase(a),
                        Phrase.convertIntegerToPhrase(b),
                        Phrase.convertIntegerToPhrase(c),
                        Phrase.convertIntegerToPhrase(d),
                        Phrase.convertIntegerToPhrase(e),
                        Phrase.convertIntegerToPhrase(f)
                ));
            }


        }
        else if(codeToBreak.length == 4){
            readBackPhrases = new ArrayList<>(Arrays.asList(
                    Phrase.CODEBREAKER_GUESS_NUMBER_RESPONSE,
                    Phrase.convertIntegerToPhrase(this.selectedCellPoint.y+1),
                    Phrase.CODEBREAKER_GUESS_WAS,
                    Phrase.convertIntegerToPhrase(a),
                    Phrase.convertIntegerToPhrase(b),
                    Phrase.convertIntegerToPhrase(c),
                    Phrase.convertIntegerToPhrase(d),
                    Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                    Phrase.convertIntegerToPhrase(g),
                    Phrase.CODEBREAKER_NUMBER_ONLY,
                    Phrase.convertIntegerToPhrase(o)
                ));
        }
        else if(codeToBreak.length == 5){
            readBackPhrases = new ArrayList<>(Arrays.asList(
                    Phrase.CODEBREAKER_GUESS_NUMBER_RESPONSE,
                    Phrase.convertIntegerToPhrase(this.selectedCellPoint.y+1),
                    Phrase.CODEBREAKER_GUESS_WAS,
                    Phrase.convertIntegerToPhrase(a),
                    Phrase.convertIntegerToPhrase(b),
                    Phrase.convertIntegerToPhrase(c),
                    Phrase.convertIntegerToPhrase(d),
                    Phrase.convertIntegerToPhrase(e),
                    Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                    Phrase.convertIntegerToPhrase(g),
                    Phrase.CODEBREAKER_NUMBER_ONLY,
                    Phrase.convertIntegerToPhrase(o)
            ));
        }
        else{
            readBackPhrases = new ArrayList<>(Arrays.asList(
                    Phrase.CODEBREAKER_GUESS_NUMBER_RESPONSE,
                    Phrase.convertIntegerToPhrase(this.selectedCellPoint.y+1),
                    Phrase.CODEBREAKER_GUESS_WAS,
                    Phrase.convertIntegerToPhrase(a),
                    Phrase.convertIntegerToPhrase(b),
                    Phrase.convertIntegerToPhrase(c),
                    Phrase.convertIntegerToPhrase(d),
                    Phrase.convertIntegerToPhrase(e),
                    Phrase.convertIntegerToPhrase(f),
                    Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                    Phrase.convertIntegerToPhrase(g),
                    Phrase.CODEBREAKER_NUMBER_ONLY,
                    Phrase.convertIntegerToPhrase(o)
            ));
        }
        this.audioPlayerExecutor.replacePhraseAndPrint(readBackPhrases);
    }

    /**
     * Reads the value of the currently selected cell
     */
    public void readSelectedSquare() {
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.convertPointToLocationPhrase(this.selectedCellPoint));
    }

    /**
     * Getter for gameOver
     *
     * @return true if the game is over (otherwise, false).
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    public int[] getCodeToBreak() {
        return this.codeToBreak;
    }

    public int hint(int value) {
        throw new UnsupportedOperationException("Hint not implemented yet!");
    }

    public CodebreakerType getCodebreakerType() {
        return this.codebreakerType;
    }

    public Integer[] getCurrentGuess() {
        return this.currentGuess;
    }

    public ArrayList<CodebreakerGuess> getGuessList() {
        return this.guessList;
    }

    public Point getSelectedCellPoint() {
        return this.selectedCellPoint;
    }
}
