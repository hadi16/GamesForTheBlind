import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JFrame {
    Popup p;
    // constructor
    Board() {
        // create a frame
        JFrame frame = new JFrame("Sudoku");
        // create a label
        //JLabel l = new JLabel("This is a Sudoku puzzle");
        /*f.setSize(400, 400);
        PopupFactory pf = new PopupFactory();
        // create a panel
        JPanel p2 = new JPanel();
        // set Background of panel
        p2.setBackground(Color.red);
        p2.add(l);
        // create a popup
        p = pf.getPopup(f, p2, 180, 100);*/

        // create a panel
        JPanel p1 = new JPanel();

        frame.add(p1);
        SudokuPanel s = new SudokuPanel();
        frame.add(s);

         frame.show();
         frame.setSize( 500, 500 );
    }

    public static void main(String[] args) {
        new Board();
    }
}
