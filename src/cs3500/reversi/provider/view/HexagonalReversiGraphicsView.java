package cs3500.reversi.provider.view;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.reversi.provider.model.ReadonlyReversiModel;

/**
 * Represents a graphical view of a hexagonal Reversi game. This view implements MouseListener and
 * keyListener in order to allow for the user to interact with the game board in order to see the
 * state of the game and indicate which moves they want to make.
 */
public class HexagonalReversiGraphicsView extends JFrame implements ReversiView, MouseListener,
        KeyListener {
  private Consumer<Point> commandCallback;
  private final HexagonsPanel hPanel;

  /**
   * Constructor which initializes the model to be used to find out the state of the game in order
   * to display it.
   */
  public HexagonalReversiGraphicsView(ReadonlyReversiModel model) {
    super();
    int boardSize = model.getBoardSize();

    setTitle("Reversi");
    setSize(700, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //getContentPane().setBackground(Color.darkGray);

    addMouseListener(this);
    addKeyListener(this);

    this.hPanel = new HexagonsPanel(model);

    this.add(this.hPanel);
  }


  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.add(this.hPanel);
  }


  /**
   * Helper method that draws a circle in the center of a hexagon. This is used to show the discs
   * in the hexagonal cells of the game.
   */
  private void drawCircleInHexagonCenter(Polygon hexagon, Color color, Graphics2D g2d) {
    Rectangle bounds = hexagon.getBounds();
    int centerX = bounds.x + bounds.width / 2;
    int centerY = bounds.y + bounds.height / 2;
    int radius = Math.min(bounds.width, bounds.height) / 4; // Adjust the radius as needed

    g2d.setColor(color); // Set the color of the circle
    g2d.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius); // Draw the circle
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // Not Needed
  }

  @Override
  public void mousePressed(MouseEvent e) {
    System.out.println("IN HERE");
    Point clickedPoint = e.getPoint();
    this.hPanel.updateSelectedCell(clickedPoint);
    //repaint();

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Not Needed
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Not Needed
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // Not Needed
  }

  @Override
  public void render() {
    // Not Needed
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void setCommandCallback(Consumer<Point> callback) {
    this.commandCallback = callback;
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(null, error);
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not Needed
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();

    if (keyCode == KeyEvent.VK_ENTER) {
      if (this.hPanel.getSelectedCell() != null) {
        this.commandCallback.accept(this.hPanel.getPreviouslySelectedCellInRegularCoords());
      }
    } else if (keyCode == KeyEvent.VK_SPACE) {
      this.commandCallback.accept(null);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Not Needed
  }
}