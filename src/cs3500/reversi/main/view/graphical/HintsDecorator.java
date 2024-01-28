package cs3500.reversi.main.view.graphical;

import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.AbstractAction;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;

/**
 * A decorator for the panel that introduces a "hint" mode that assists players. When this mode is
 * enabled, the selected cell shows how many discs would be flipped if that player chose that move.
 * Hints can be enabled and disabled anytime throughout the game.
 */
public final class HintsDecorator extends AbstractGameBoardPanel {
  private final AbstractGameBoardPanel decoratedPanel;
  private boolean showHints;

  /**
   * Constructs a HintsDecorator object using an already existing panel that may or may not be
   * decorated yet.
   *
   * @param decoratedPanel the panel that may or may not be decorated yet
   * @throws IllegalArgumentException if the given panel is <code>null</code>
   */
  public HintsDecorator(AbstractGameBoardPanel decoratedPanel) throws IllegalArgumentException {
    super(decoratedPanel);
    this.decoratedPanel = decoratedPanel;
    setLayout(new BorderLayout());
    setupHintsCheckbox();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.decoratedPanel.setSize(getWidth(), getHeight());
    this.decoratedPanel.selectedTile = this.selectedTile;
    this.decoratedPanel.paintComponent(g);

    if (this.showHints) {
      displayHints(g);
    }
  }

  @Override
  protected Point tileCoordsToPixelCoords(Point tile) throws IllegalArgumentException {
    return this.decoratedPanel.tileCoordsToPixelCoords(tile);
  }

  @Override
  protected double calcTileSideLength() {
    return this.decoratedPanel.calcTileSideLength();
  }

  @Override
  protected Path2D.Double buildTile(Point tile) throws IllegalArgumentException {
    return this.decoratedPanel.buildTile(tile);
  }

  @Override
  protected Point getPixelCenterCoordForTile(Point tile) throws IllegalArgumentException {
    return this.decoratedPanel.getPixelCenterCoordForTile(tile);
  }

  /**
   * Sets up a checkbox that will be used to toggle on and off the ability to show hints.
   */
  private void setupHintsCheckbox() {
    JPanel southPanel = new JPanel();
    JCheckBox hintsCheckbox = new JCheckBox("Hints");
    hintsCheckbox.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showHints = !showHints;
        repaint();
        requestFocus();
      }
    });
    southPanel.add(hintsCheckbox);
    add(southPanel, BorderLayout.SOUTH);
  }

  /**
   * Displays hints for the selected tile if there is a tile that has been selected. Hints will not
   * be shown if no tile has been selected, the Reversi game is over, no view listener exists
   * (what is the point of showing hints for a certain player if there is no one to view it at
   * all?), or it isn't the player's turn for the player that the view listener is listening for
   * (a player's hints can only be shown on their turn). The hint to be shown is the earned
   * score/the number of discs that can be flipped if a move were to be made on the selected tile.
   *
   * @param g the graphics object that will be used to display the hint
   * @throws IllegalArgumentException if the given graphics object is <code>null</code>
   */
  private void displayHints(Graphics g) throws IllegalArgumentException {
    if (g == null) {
      throw new IllegalArgumentException("The given graphics object cannot be null.");
    }

    if (this.selectedTile != null && !this.game.gameOver()
            && this.game.getTurn().equals(this.viewListener.getPlayerDisc())) {
      Graphics2D g2d = (Graphics2D) g;
      Point selectedTilePixelCoord = getPixelCenterCoordForTile(this.selectedTile);
      g2d.drawString(calculatePotentialPiecesScored(this.selectedTile) + "",
              selectedTilePixelCoord.x, selectedTilePixelCoord.y);
    }
  }

  /**
   * Calculates the potential earned score when placing a disc at a specific tile. The earned score
   * does not include the disc being placed down. It only calculates based on the discs that will
   * be flipped.
   *
   * @param tile the tile at which to calculate the potential earned score should a move be made
   *             there
   * @return the potential earned score
   * @throws IllegalArgumentException if the given tile is <code>null</code> OR the given tile is
   *                                  invalid
   */
  private int calculatePotentialPiecesScored(Point tile)
          throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    } else if (this.game.getBoard().get(tile) == null) {
      throw new IllegalArgumentException("Invalid tile.");
    }

    MutableReversiModel branchedGame = this.game.copyGame();

    try {
      branchedGame.placeDisc(tile);
      Disc player = this.game.getTurn();
      return branchedGame.getScore(player) - this.game.getScore(player) - 1;
    } catch (IllegalStateException e) {
      return 0;
    }
  }
}
