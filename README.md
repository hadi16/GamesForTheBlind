# Games For The Blind
Games for the Blind is a computer game program specifically designed for the blind. The two games currently featured on this software are Sudoku and CodeBreaker. Both games are navigable using a traditional QWERTY keyboard (with a number pad) and use prerecorded audio files to help explain the current game state to the player.


## Features

**Self-Contained Program:**
This program is not internet dependent and only uses libraries contained in version 8 of Oracle's Java Development Kit.

**Software GUI (Frontend):** The frontend of the software consists of Java Swing components. It serves as a visual representation of the current game state, with the game state being updated as the game is played.

**Software Backend:** The backend of our software consists of Java code that generates a new game state in the form of a Sudoku board or CodeBreaker 4-digit code for the user to solve. For Sudoku, the backend ensures that only valid numbers are placed on the board,as blind users would likely otherwise become very frustrated.

**Software Synthesizer:** This Java code calls into a series of prerecorded audio files (spoken in English), which will allow a blind user to navigate and understand the current state of the game that is being played.



## Installation 
Games for the Blind uses izPack 5.1 to create a JAR installer file for the  application. The software uses launch4j to: 
1. wrap this JAR installer file into a Windows executable  
2. embed JDK 8 into this executable installer file.
Games for the Blind can be easily installed by blind users. 


## Sudoku User Manual
### Rules  
The goal of Sudoku is to fill up the missing numbers on the board so that no number is repeated in each block, row, and column. 
 
**4x4 Board:** 
A 4x4 Sudoku board is broken into four blocks. Each block contains four squares for numbers to be placed. The squares will be filled in with the numbers 1-4 such that no number is repeated in each block, row, and column. 
 
 ![Image_Of_4x4Board](https://github.com/hadi16/GamesForTheBlind/blob/master/images/4x4Board.png)

**6x6 Board:** 
A 6x6 Sudoku board is somewhat different. The board is broken into six rectangles, each with six squares for numbers to be placed. Each block has two rows and 3 columns of squares to place the numbers 1-6. The squares are arranged so that there are two columns and three rows of squares on the board. 
  
  ![Image_Of_6x6Board](https://github.com/hadi16/GamesForTheBlind/blob/master/images/6x6Board.png)

**9x9 Board:** 
A 9x9 Sudoku board is broken into nine blocks. Each block contains squares for numbers to be placed. The squares must be filled with the numbers 1-9 such that no number is repeated twice in each block, row, and column. 
  
  ![Image_Of_9x9Board](https://github.com/hadi16/GamesForTheBlind/blob/master/images/9x9Board.png)

### How to Play 

**How to Play Sudoku:** 

From the initial main menu page, choose Sudoku by either pressing space or clicking. 

  ![Image_Of_mainMenu](https://github.com/hadi16/GamesForTheBlind/blob/master/images/mainMenu.png)

Then choose the difficulty level desired or return to main menu.

  ![Image_Of_sudokuOptions](https://github.com/hadi16/GamesForTheBlind/blob/master/images/sudokuOptions.png)
 
**How to Play Sudoku Using Mouse:** Simply click on the square where you would like to place a number and press the corresponding number on the number pad. 
 
**How to Play Sudoku Using Keyboard:** The starting position is the top left of the board for all the board sizes.  Navigating the board is done by using the arrow keys and the hot keys. To check if a space is empty or determine which number is in each square, simply pass over the squares to hear the number or that the square is blank. If the square is empty, no number will be read. Press the desired key and then press the number that you think belongs there. If the number is correct, it will be placed in the square. If the number is incorrect, that space will remain empty.
 
Hot keys include:
* Space: go to the home position (top left square)
* Command-left: move to the leftmost column in the current row
* Command-right: move to the rightmost column in the current row
* Command-up: move to the top row in the current column
* Command-down: move to the bottom row in the current column
* 'J' : read the row 
* 'K' : read the column 
* 'I' : read the instructions
* 'H' : solve the current square
 
Example: Place a ‘3’ into the top-right square in the bottom-left block in a 4x4 board: 
1. Use the arrow keys to navigate to the desired square (down, down, right).
3. Audio feedback will play to disclose what number is in this square. 
4. Assuming that the square is empty, press the number ‘3’ on the number pad to insert a 3. 
5. If this placement is correct, there will be audio feedback that acknowledges that the number 3 was placed correctly. 
6. Press the space bar to return to the home position (top left square). 

**Menu Tab Features:**
* Hint: solves the currently selected square
* Instructions: read the instructions
* Language: change the language (Not yet implemented)
* Restart: restart the sudoku board
* Return to Main Menu: exit the game and return to main menu

![Image_Of_menuTab](https://github.com/hadi16/GamesForTheBlind/blob/master/images/menuTab.png)


## CodeBreaker User Manual
**Rules of CodeBreaker:** The goal of CodeBreaker is to correctly guess a pre-determined code. 
Typically, this code is made up of a series of colored pegs; however, in this version of CodeBreaker, numbered pegs will be used instead. 
The code length options are 4, 5, and 6 with the number of trials being 12, 15, and 20 respectively. 

![Image_Of_cb_options](https://github.com/hadi16/GamesForTheBlind/blob/master/images/cb_options.png)
 
To guess the code, the player must place the correct amount of numbers from 1-6 (numbers may be used more than once) into the empty spaces. 
After making the guess, the player is notified about how much of their guess is correct. 
This is done by adding four/five/six small pegs off to the side. 
 
 ![Image_Of_cb_4_solved](https://github.com/hadi16/GamesForTheBlind/blob/master/images/cb_4_solved.png)

A black peg means that a numbered peg is the right number in the right place. 
A red peg means that a peg with that number is in the code, but not in the correct place. 
No peg means that a peg is not the correct number nor is it in the correct place. 
This is repeated until the code is correctly guessed. 
 
![Image_Of_cb_5](https://github.com/hadi16/GamesForTheBlind/blob/master/images/cb_5.png)

![Image_Of_cb_6](https://github.com/hadi16/GamesForTheBlind/blob/master/images/cb_6.png)


**How to Play CodeBreaker Using Mouse:** Click and enter a number into a peg hole, then click “Make Guess.” 
 
**How to Play CodeBreaker Using Keyboard:** Use the arrow keys to select a hole to place the peg. Use the number pad to enter a number from 1 to 6. Hit the spacebar to make a guess. The player will be given feedback on the accuracy of their guess. Pressing “Q” followed by a number will read back that row. This will allow users to look back row by row and remember past guesses



## License
This code is publicly available and licensed under GPLv3. 
