package cs3500.reversi.main.view.graphical;

import java.awt.Point;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import javax.swing.JPanel;
import javax.swing.AbstractAction;

import cs3500.reversi.main.controller.ViewFeatures;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * Represents any graphical panel view that is also a custom Java Swing panel. Every graphical
 * panel view has access to a read-only model of the Reversi game and a list of all view
 * listeners to notify.
 *
 * @see GraphicalPanelView
 */
public abstract class AbstractGameBoardPanel extends JPanel implements GraphicalPanelView {
  protected final ROReversiModel game;
  protected ViewFeatures viewListener;
  protected Point selectedTile;
  protected Color backgroundColor;
  protected Color tileFillColor;
  protected Color selectedTileFillColor;
  protected Color tileBorderColor;
  protected int tileBorderStrokeWidth;
  protected Color player1DiscColor;
  protected Color player2DiscColor;

  /**
   * Constructs an AbstractGameBoardPanel object using a given read-only model of the Reversi game.
   *
   * @param game the read-only model of the Reversi game
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public AbstractGameBoardPanel(ROReversiModel game) throws IllegalArgumentException {
    if (game == null) {
      throw new IllegalArgumentException("The game cannot be null.");
    }

    this.game = game;
    this.viewListener = null;
    this.selectedTile = null;
    this.backgroundColor = new Color(64, 64, 64);
    this.tileFillColor = new Color(192, 192, 192);
    this.selectedTileFillColor = new Color(117, 251, 253);
    this.tileBorderColor = Color.BLACK;
    this.tileBorderStrokeWidth = 3;
    this.player1DiscColor = Color.BLACK;
    this.player2DiscColor = Color.WHITE;
    setLayout(new BorderLayout());
    setUpListeners();
  }

  /**
   * Constructs an AbstractGameBoardPanel object using an already existing panel that may or may
   * not be decorated yet. This constructor is only to be called on by decorators trying to
   * decorate the panel.
   *
   * @param decoratedPanel the Java panel that may or may not be decorated yet
   * @throws IllegalArgumentException if the given panel is <code>null</code>
   */
  public AbstractGameBoardPanel(AbstractGameBoardPanel decoratedPanel)
          throws IllegalArgumentException {
    if (decoratedPanel == null) {
      throw new IllegalArgumentException("The decorated panel cannot be null.");
    }

    this.game = decoratedPanel.game;
    this.viewListener = null;
    this.selectedTile = null;
    this.backgroundColor = new Color(64, 64, 64);
    this.tileFillColor = new Color(192, 192, 192);
    this.selectedTileFillColor = new Color(117, 251, 253);
    this.tileBorderColor = Color.BLACK;
    this.tileBorderStrokeWidth = 3;
    this.player1DiscColor = Color.BLACK;
    this.player2DiscColor = Color.WHITE;
    setUpListeners();
  }

  @Override
  public void setFeature(ViewFeatures feature) throws IllegalArgumentException {
    if (feature == null) {
      throw new IllegalArgumentException("The feature cannot be null.");
    }

    this.viewListener = feature;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  /**
   * Calculates the side length of a tile relative to the dimensions of the panel.
   *
   * @return tile side length
   */
  protected double calcTileSideLength() {
    // stub return statement to be overridden
    return 0.0;
  }

  /**
   * Converts tile coordinates from the game board to pixel coordinates that will specify
   * where any given tile would be located on display.
   *
   * <p>The origin - the tile at the center of the board will be represented as (0, 0) in pixel
   * coordinates. Directional movement along the board in pixel coordinates will be similar
   * to that of tile coordinates.</p>
   *
   * @param tile the tile whose coordinates are to be converted
   * @return a new x-y point representing the converted pixel coordinates
   * @throws IllegalArgumentException if the given tile is <code>null</code>
   */
  protected Point tileCoordsToPixelCoords(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    }

    // stub return statement to be overridden
    return null;
  }

  /**
   * Builds a tile shape based on the provided tile. The provided tile's coordinates (assumed to be
   * in pixel coordinates) are treated as the very center of the tile.
   *
   * @param tile the center pixel coordinates of the actual tile shape to be built
   * @return a tile shape represented by a {@link Path2D.Double}
   * @throws IllegalArgumentException if the given tile is <code>null</code>
   */
  protected Path2D.Double buildTile(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    }

    // stub return statement to be overridden
    return null;
  }

  /**
   * Displays a visual view of a tile on the game board.
   *
   * @param g2d  the {@link Graphics2D} context to paint on
   * @param tile the tile to be displayed
   * @throws IllegalArgumentException if the given graphics object is <code>null</code> OR the
   *                                  given tile is <code>null</code> OR the given tile does not
   *                                  exist on the game board
   */
  protected void paintTile(Graphics2D g2d, Point tile) throws IllegalArgumentException {
    if (g2d == null) {
      throw new IllegalArgumentException("The given graphics object cannot be null.");
    } else if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    }

    Disc disc = this.game.getDisc(tile);

    if (disc == null) {
      throw new IllegalArgumentException(
              "The given tile does not exist in the game board.");
    }

    // convert tile coordinates to pixel coordinates
    Point pixelCoords = tileCoordsToPixelCoords(tile);

    // build the actual tile shape to display
    Path2D.Double tileLines = buildTile(pixelCoords);

    // set stroke for displaying square
    Color oldColor = g2d.getColor();
    Stroke oldStroke = g2d.getStroke();
    g2d.setColor(this.tileBorderColor);
    g2d.setStroke(new BasicStroke(this.tileBorderStrokeWidth));
    g2d.draw(tileLines);
    g2d.setStroke(oldStroke);
    g2d.setColor(oldColor);

    // set color for displaying tile
    oldColor = g2d.getColor();

    // if the tile has been selected, highlight it in light blue
    g2d.setColor(tile.equals(this.selectedTile)
            ? this.selectedTileFillColor : this.tileFillColor);

    g2d.fill(tileLines);
    g2d.setColor(oldColor);
  }

  /**
   * Displays a visual view of the disc on the specified tile.
   *
   * @param g2d  the {@link Graphics2D} context to paint on
   * @param tile the tile representing the position where the disc is to be displayed
   * @throws IllegalArgumentException if the given graphics object is <code>null</code> OR the
   *                                  given tile is <code>null</code> OR the given tile does not
   *                                  exist on the game board
   */
  protected void paintDisc(Graphics2D g2d, Point tile) throws IllegalArgumentException {
    if (g2d == null) {
      throw new IllegalArgumentException("The given graphics object cannot be null.");
    } else if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    }

    Disc disc = this.game.getDisc(tile);

    if (disc == null) {
      throw new IllegalArgumentException(
              "The given tile does not exist in the game board.");
    }
  }

  /**
   * Calculates the pixel coordinate of a tile coordinate and finds the center coordinate of the
   * tile shape in relation to how it's displayed.
   *
   * @param tile the tile in tile coordinates to calculate
   * @return the center coordinate in pixel coordinates of the tile shape of the given tile in
   *         relation to how it's displayed
   * @throws IllegalArgumentException if the given tile is <code>null</code>
   */
  protected Point getPixelCenterCoordForTile(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null");
    }

    // stub return statement to be overridden
    return null;
  }

  /**
   * Selects a tile on the game board to be highlighted later. If the selected tile is deselected,
   * if it is selected again, another tile is selected, or out of bounds selection occurs. Note
   * that only one tile can be selected at a time. In a Reversi game, a tile would be
   * selected/highlighted to show which tile the player will make a move on.
   *
   * @param x the x position that has been selected
   * @param y the y position that has been selected
   */
  protected void selectTile(int x, int y) {
    for (Point tile : this.game.getBoard().keySet()) {
      if (buildTile(tileCoordsToPixelCoords(tile)).contains(x, y)) {
        if (this.game.getBoard().get(tile).hasDisc()) {
          this.selectedTile = null;
        } else if (this.selectedTile == null || !this.selectedTile.equals(tile)) {
          this.selectedTile = tile;
        } else {
          this.selectedTile = null;
        }

        System.out.printf("(%d, %d)\n", tile.x, tile.y);
        return;
      }
    }

    this.selectedTile = null;
  }

  /**
   * Sets up necessary mouse listeners that allow tiles to be selected and key listeners that
   * allow moves to be made.
   */
  private void setUpListeners() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        selectTile(mouseX, mouseY);

        repaint();
        requestFocus();
      }
    });

    getActionMap().put("setMove", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (viewListener != null) {
          viewListener.makeMove(selectedTile);
          selectedTile = null;
        }
        requestFocus();
      }
    });

    getActionMap().put("setPass", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (viewListener != null) {
          viewListener.pass();
        }
        requestFocus();
      }
    });
  }
}
