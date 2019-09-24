# Games For The Blind
Games for the Blind is a computer game program specifically designed for the blind. The two games currently featured on this software are Sudoku and Mastermind. Both games are navigable using a traditional QWERTY keyboard (with a number pad) and use prerecorded audio files to help explain the current game state to the player.


## Features

**Self-Contained Program:**
This program is not internet dependent and only uses libraries contained in version 12 of Oracle's Java Development Kit.

**Software GUI (Frontend):** The frontend of the software consists of Java Swing components. It serves as a visual representation of the current game state, with the game state being updated as the game is played.

**Software Backend:** The backend of our software consists of Java code that generates a new game state in the form of a Sudoku board or Mastermind 4-digit code for the user to solve. For Sudoku, the backend ensures that only valid numbers are placed on the board,as blind users would likely otherwise become very frustrated.

**Software Synthesizer:** This Java code calls into a series of prerecorded audio files (spoken in English), which will allow a blind user to navigate and understand the current state of the game that is being played.



## Installation 
Games for the Blind uses izPack 5.1 to create a JAR installer file for the  application. The software uses launch4j to: 
1. wrap this JAR installer file into a Windows executable  
2. embed JDK 12 into this executable installer file.
Games for the Blind can be easily installed by blind users. 


## Sudoku User Manual
### Rules  
The goal of Sudoku is to fill up the missing numbers on the board so that no number is repeated in each block, row, and column. 
 
**4x4 Board:** 
A 4x4 Sudoku board is broken into four blocks. Each block contains four squares for numbers to be placed. The squares will be filled in with the numbers 1-4 such that no number is repeated in each block, row, and column. 
 
**6x6 Board:** 
A 6x6 Sudoku board is somewhat different. The board is broken into six rectangles, each with six squares for numbers to be placed. Each block has two rows and 3 columns of squares to place the numbers 1-6. The squares are arranged so that there are two columns and three rows of squares on the board. 
 
**9x9 Board:** 
A 9x9 Sudoku board is broken into nine blocks. Each block contains squares for numbers to be placed. The squares must be filled with the numbers 1-9 such that no number is repeated twice in each block, row, and column. 
 
### How to Play 
 
**How to Play Sudoku Using Mouse:** Simply click on the square where you would like to place a number and press the corresponding number on the number pad. 
 
**How to Play Sudoku Using Keyboard:** For a 4x4 board, imagine the ‘X’, ‘C’, ‘S,’ and ‘D’ keys as the board with each key as a square. Once one of those keys is pressed, imagine those same keys as the squares where each number will be placed. To check if a space is empty or determine which number is in each square, simply press the key again to hear the number or that the square is blank. If the square is empty, no number will be read. Press the desired key and then press the number that you think belongs there. If the number is correct, it will be placed in the square. If the number is incorrect, that space will remain empty. To return the board to its default, zoomed-out position, press the space bar.  
 
To play with a 6x6 or a 9x9 board, repeat these same instructions with the following modifications: 
* For the 6x6 board, use the ‘S’, ‘D’, ‘F’, ‘X’, ‘C,’ and V’ keys. 
* For the 9x9 board, use the ‘W’, ‘E’, ‘R’, ‘S,’ ‘D’, ‘F’, ‘X’, ‘C’, and ‘V’ keys.
Page Break
 
Example: Place a ‘3’ into the top-right square in the bottom-left block in a 4x4 board: 
1. Press the ‘X’ key to go to the bottom-left block. 
2. Press the ‘D’ key to hear what is in the top-right square. 
3. Audio feedback will play to disclose what number is in this square. 
4. Assuming that the square is empty, press the number ‘3’ on the number pad to insert a 3. 
5. If this placement is correct, there will be audio feedback that acknowledges that the number 3 was placed correctly. 
6. Press the space bar to return to the fully zoomed-out position. 

## Mastermind User Manual
**Rules of Mastermind:** The goal of Mastermind is to correctly guess a pre-determined code. Typically, this code is made up of a series of colored pegs; however, in this version of Mastermind, numbered pegs will be used instead. To guess the code, the player must place four numbers from 1-6 (numbers may be used more than once) into four spaces. After making the guess, the player is notified about how much of their guess is correct. This is done by adding four small pegs off to the side. A black peg means that a numbered peg is the right number in the right place. A white peg means that a peg with that number is in the code, but not in the correct place. No peg means that a peg is not the correct number nor is it in the correct place. This is repeated until the code is correctly guessed. 
 
**How to Play Mastermind Using Mouse:** Click and drag a numbered peg into a peg hole, then click “Make Guess.” 
 
**How to Play Mastermind Using Keyboard:** Use the ‘A’, ‘S’, ‘D’, and ‘F’ keys to select a hole to place the peg. Use the number pad to enter a number from 1 to 6. Hit the spacebar to make a guess. The player will be given feedback on the accuracy of their guess. Pressing “Q” followed by a number will read back that row. This will allow users to look back row by row and remember past guesses




## License
This code is publicly available and licensed under GPLv3. 
