package PegSolitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;



public class Renderer extends JPanel {

    private Board game = new Board();
    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.white);
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.BLACK);

        for (int x = 0; x < game.size; x++) {
            for (int y = 0; y < game.size; y++) {
                g2.setPaint(Color.black);
                Rectangle2D rect = new Rectangle2D.Double(30 + y * 50, 30 + x * 50, 50, 50);
                g2.draw(rect);


                if (this.game.isOccupied(y, x)) {
                    g2.setPaint(Color.red);
                    Rectangle2D rectFill = new Rectangle2D.Double(30 + (y * 50) + 1, 30 + (x * 50) + 1, 49, 49);
                    g2.fill(rectFill);


                } else if (this.game.isBarrier(y, x)) {
                    g2.setPaint(Color.black);
                    Rectangle2D rectFill = new Rectangle2D.Double(30 + (y * 50) + 1, 30 + (x * 50) + 1, 49, 49);
                    g2.fill(rectFill);
                }
                g2.setPaint(Color.black);
                g2.drawString(y + "," + x, 44 + (y * 50), 12 + (x * 50) + 50);

                g2.drawString("Remaining Pegs: " + game.getRemainingPegs(), 20, 15);

            }
        }

    }

    /** *
     * Sets the game object to display.
     * @param g Board object to set from.
     */
    public void setGame(Board g) {
        g.copy(g, this.game);
    }

    /** *
     * Refreshes the display area.
     */
    public void refresh() {
        this.repaint();
    }


}
