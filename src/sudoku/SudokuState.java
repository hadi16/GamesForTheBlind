package sudoku;

import org.apache.commons.lang3.ObjectUtils;
import sudoku.generator.Cell;
import sudoku.generator.Generator;
import sudoku.generator.Grid;
import synthesizer.AudioPlayer;
import synthesizer.Phrase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SudokuState{
    private final AudioPlayer audioPlayer;
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;
    private int count;
    private Point pointSelected;

    public SudokuState(int sudokuBoardSize, AudioPlayer audioPlayer) {
        int numberOfEmptyCells = (sudokuBoardSize * sudokuBoardSize) / 3;
        count = numberOfEmptyCells;

        this.sudokuBoardSize = sudokuBoardSize;
        this.sudokuGrid = new Generator(sudokuBoardSize).generate(numberOfEmptyCells);

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
        this.audioPlayer = audioPlayer;
    }

    public SudokuState(SudokuState originalState) {
        this.sudokuBoardSize = originalState.sudokuBoardSize;
        this.sudokuGrid = new Grid(originalState.sudokuGrid);

        // Enums are immutable set (don't need copy constructor for them).
        this.selectedBlockPoint = originalState.selectedBlockPoint;
        this.selectedSquarePoint = originalState.selectedSquarePoint;

        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (Point point : originalState.originallyFilledSquares) {
            originallyFilledSquares.add(new Point(point));
        }
        this.originallyFilledSquares = originallyFilledSquares;
        this.audioPlayer = originalState.audioPlayer;
    }

    private ArrayList<Point> initializeOriginallyFilledSquares() {
        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < this.sudokuBoardSize; rowIdx++) {
            for (int columnIdx = 0; columnIdx < this.sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, columnIdx);
                if (cellAtPosition.getValue() != 0) {
                    originallyFilledSquares.add(new Point(columnIdx, rowIdx));
                }
            }
        }
        return originallyFilledSquares;
    }

    public void readRemainingNumOfCells() {
        int counter = getCount();



        Phrase relevantPhrase = Phrase.CONGRATS;
        Phrase relevantPhrase1 = Phrase.EMPTY;
        Phrase relevantPhrase2 = Phrase.EMPTY;


        if ( counter == 0) {
            try {
                this.audioPlayer.replacePhraseToPlay(relevantPhrase);
                System.err.println(relevantPhrase.getPhraseValue());
                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println((e));
            }

            //end game
            Runtime.getRuntime().exit(1);
            return;
        }

        switch(counter){
            case 1:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_3;
                relevantPhrase = Phrase.ONE;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_4;
                break;
            case 2:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.TWO;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 3:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.THREE;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 4:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.FOUR;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 5:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.FIVE;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 6:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.SIX;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 7:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.SEVEN;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 8:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.EIGHT;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
            case 9:
                relevantPhrase1 = Phrase.EMPTY_PIECES_OF_BOARD_1;
                relevantPhrase = Phrase.NINE;
                relevantPhrase2 = Phrase.EMPTY_PIECES_OF_BOARD_2;
                break;
        }

        try {
            this.audioPlayer.replacePhraseToPlay(relevantPhrase1);
            System.err.println(relevantPhrase1.getPhraseValue());
            // thread to sleep for 1000 milliseconds
            Thread.sleep(1000);

            this.audioPlayer.replacePhraseToPlay(relevantPhrase);
            System.err.println(relevantPhrase.getPhraseValue());
            Thread.sleep(1000);

            this.audioPlayer.replacePhraseToPlay(relevantPhrase2);
            System.err.println(relevantPhrase2.getPhraseValue());
        } catch (Exception e) {
            System.out.println(e);
        }
        return;
    }

    public void readNumInSquare (int num) {
        Phrase relevantPhrase = Phrase.EMPTY;

        switch(num){
            case 1:
                relevantPhrase = Phrase.ONE;
                break;
            case 2:
                relevantPhrase = Phrase.TWO;
                break;
            case 3:
                relevantPhrase = Phrase.THREE;
                break;
            case 4:
                relevantPhrase = Phrase.FOUR;
                break;
            case 5:
                relevantPhrase = Phrase.FIVE;
                break;
            case 6:
                relevantPhrase = Phrase.SIX;
                break;
            case 7:
                relevantPhrase = Phrase.SEVEN;
                break;
            case 8:
                relevantPhrase = Phrase.EIGHT;
                break;
            case 9:
                relevantPhrase = Phrase.NINE;
                break;
        }
        this.audioPlayer.replacePhraseToPlay(relevantPhrase);
        System.err.println(relevantPhrase.getPhraseValue());
        return;
    }

    public void setSquareNumber(int numberToFill) {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            Phrase relevantPhrase = Phrase.NO_SELECTED_SQUARE;

            this.audioPlayer.replacePhraseToPlay(relevantPhrase);
            System.err.println(relevantPhrase.getPhraseValue());
            readRemainingNumOfCells();
            return;
        }

        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
        Point pointToSet = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        if (numberToFill == 0) {
            if (this.originallyFilledSquares.contains(pointToSet)) {
                try {
                    Phrase relevantPhrase = Phrase.CANNOT_DELETE_ORIGINAL;
                    this.audioPlayer.replacePhraseToPlay(relevantPhrase);
                    System.err.println(relevantPhrase.getPhraseValue());
                    Thread.sleep(1000);

                    Phrase relevantPhrase1 = Phrase.CURRENT_VALUE;
                    this.audioPlayer.replacePhraseToPlay(relevantPhrase1);
                    System.err.println(relevantPhrase1.getPhraseValue());
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(1000);

                    readNumInSquare (cellToSet.getValue());
                } catch (Exception e) {
                    System.out.println(e);
                }



            } else {
                cellToSet.setValue(0);
                try {
                    Phrase relevantPhrase2 = Phrase.PLACED_NUM;
                    this.audioPlayer.replacePhraseToPlay(relevantPhrase2);
                    System.err.println(relevantPhrase2.getPhraseValue());
                    Thread.sleep(1000);

                    readNumInSquare(cellToSet.getValue());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return;
        }

        if (!(numberToFill > 0 && numberToFill <= this.sudokuBoardSize)) {
            Phrase phrase;
            if (this.sudokuBoardSize == 9) {
                phrase = Phrase.INVALID_NUMBER_TO_FILL_9;
            } else {
                phrase = Phrase.INVALID_NUMBER_TO_FILL_4;
            }

            this.audioPlayer.replacePhraseToPlay(phrase);
            System.err.println(phrase.getPhraseValue());
            return;
        }

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            Phrase relevantPhrase3 = Phrase.CELL_VALUE_INVALID;
            this.audioPlayer.replacePhraseToPlay(relevantPhrase3);
            System.err.println(relevantPhrase3.getPhraseValue());
            return;
        }

        cellToSet.setValue(numberToFill);
        count--;
        readRemainingNumOfCells();
    }

    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        this.pointSelected = pointToSet;
        if (inputType == InputType.MOUSE) {
            int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
            Point blockPointToSet = new Point(pointToSet.x / numberOfBlocks, pointToSet.y / numberOfBlocks);
            Point squarePointToSet = new Point(pointToSet.x % numberOfBlocks, pointToSet.y % numberOfBlocks);

            if (blockPointToSet.equals(this.selectedBlockPoint) && squarePointToSet.equals(this.selectedSquarePoint)) {
                this.selectedBlockPoint = null;
                this.selectedSquarePoint = null;
                return;
            }

            this.selectedBlockPoint = blockPointToSet;
            this.selectedSquarePoint = squarePointToSet;
            return;
        }

        if (pointToSet == null) {
            if (this.selectedSquarePoint != null) {
                this.selectedSquarePoint = null;
            } else {
                this.selectedBlockPoint = null;
            }
            return;
        }

        if (this.selectedBlockPoint != null && this.selectedSquarePoint != null) {
            int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);

            Point newPoint = new Point(
                    this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                    this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
            );
            Cell cell = this.sudokuGrid.getCell(newPoint.y, newPoint.x);

            try {
                this.audioPlayer.replacePhraseToPlay(Phrase.CURRENT_VALUE);
                System.err.println(Phrase.CURRENT_VALUE.getPhraseValue());
                Thread.sleep(1500);

                readNumInSquare(cell.getValue());

            } catch (Exception e) {
                System.out.println(e);
            }



        return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }




    /*
    * reads the row that the player is currently in
    *
    *
    * */
    public void readTheRow(){
        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);

        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            Phrase relevantPhrase = Phrase.NO_SELECTED_SQUARE;

            this.audioPlayer.replacePhraseToPlay(relevantPhrase);
            System.err.println(relevantPhrase.getPhraseValue());
            return;
        }

        Point newPoint = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );
        try {
            int newYPoint = newPoint.y;
            this.audioPlayer.replacePhraseToPlay(Phrase.IN_ROW);
            System.err.println(Phrase.IN_ROW.getPhraseValue());
            Thread.sleep(3000);
            for (int columnIdx = 0; columnIdx < this.sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(newYPoint, columnIdx);

                if (cellAtPosition.getValue() != 0) {

                    readNumInSquare(cellAtPosition.getValue());
                    Thread.sleep(1000);
                }
                else
                {
                    this.audioPlayer.replacePhraseToPlay(Phrase.EMPTY_SQUARE);
                    System.err.println(Phrase.EMPTY_SQUARE.getPhraseValue());
                    Thread.sleep(1000);
                }
            }
        }catch (Exception e) {
                System.out.println(e);
            }

        return;
    }


    public void readTheColumn(){
        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);

        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            Phrase relevantPhrase = Phrase.NO_SELECTED_SQUARE;

            this.audioPlayer.replacePhraseToPlay(relevantPhrase);
            System.err.println(relevantPhrase.getPhraseValue());
            return;
        }

        Point newPoint = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );
        try {
            int newXPoint = newPoint.x;
            this.audioPlayer.replacePhraseToPlay(Phrase.IN_COLUMN);
            System.err.println(Phrase.IN_COLUMN.getPhraseValue());
            Thread.sleep(3000);
            for (int rowIdx = 0; rowIdx < this.sudokuBoardSize; rowIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, newXPoint);

                if (cellAtPosition.getValue() != 0) {

                    readNumInSquare(cellAtPosition.getValue());
                    Thread.sleep(1000);
                }
                else
                {
                    this.audioPlayer.replacePhraseToPlay(Phrase.EMPTY_SQUARE);
                    System.err.println(Phrase.EMPTY_SQUARE.getPhraseValue());
                    Thread.sleep(1000);
                }
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        return;
    }


    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }

    public Grid getSudokuGrid() {
        return this.sudokuGrid;
    }

    public Point getSelectedBlockPoint() {
        return this.selectedBlockPoint;
    }

    public Point getSelectedSquarePoint() {
        return this.selectedSquarePoint;
    }

    public ArrayList<Point> getOriginallyFilledSquares() {
        return this.originallyFilledSquares;
    }

    public int getCount(){
        return this.count;
    }
}
