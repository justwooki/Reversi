package cs3500.reversi.main.view.graphical;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * Represents a Java Swing panel that visually displays a Reversi hexagonal game board. The board
 * to be displayed includes each individual hexagon that makes up the board along with every disc
 * that has been placed on the board.
 *
 * <p>Each hexagonal tile is positioned using x-y coordinates with the origin at the center of the
 * board (0, 0). The x coordinate determines the row position (increasing from top to bottom), and
 * the y coordinate determines the column position (increasing from bottom-left to top-right) on
 * the game board.</p>
 *
 * <p>For more information on discs, see {@link Disc}.</p>
 */
public final class HexagonalBoardPanel extends AbstractGameBoardPanel {

  /**
   * Constructs a HexagonalBoardPanel object that takes a model of the Reversi game.
   *
   * @param game the Reversi model
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public HexagonalBoardPanel(ROReversiModel game) throws IllegalArgumentException {
    super(game);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    // set background color
    Color oldColor = g2d.getColor();
    g2d.setColor(this.backgroundColor);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setColor(oldColor);

    for (Point tile : this.game.getBoard().keySet()) {
      paintTile(g2d, tile);
      paintDisc(g2d, tile);
    }
  }

  @Override
  protected double calcTileSideLength() {
    return 2.0 * Math.min(getWidth(), getHeight())
            / (3 * (2 * this.game.getGameBoardSideLength() + 1));
  }

  @Override
  protected Point tileCoordsToPixelCoords(Point tile) throws IllegalArgumentException {
    super.tileCoordsToPixelCoords(tile);
    double tileSideLength = calcTileSideLength();
    int newX = (int) ((2 * tile.x + tile.y) * (Math.cos(Math.PI / 6))
            * tileSideLength);

    // our y coordinate value increases bottom to top in our hexagonal grid coordinate system;
    // since the pixel coordinate system increases the y coordinate value top to bottom, we must
    // multiply newY here by -1 to account for this factor
    int newY = (int) -(3 * tile.y * (Math.sin(Math.PI / 6)) * tileSideLength);
    return new Point(newX, newY);
  }

  @Override
  protected Path2D.Double buildTile(Point tile) throws IllegalArgumentException {
    super.buildTile(tile);
    Path2D.Double tileLines = new Path2D.Double();
    double tileSideLength = calcTileSideLength();

    // gets corner coordinates of tile to build
    for (int i = 0; i < 6; i++) {
      double theta = Math.PI / 3;
      double newX = (tile.x + tileSideLength * Math.cos(theta * i + Math.PI / 6))
              + (getWidth() * 0.5);
      double newY = (tile.y + tileSideLength * Math.sin(theta * i + Math.PI / 6))
              + (getHeight() * 0.5);

      // move to starting point on the first loop
      if (i == 0) {
        tileLines.moveTo(newX, newY);
      } else {
        tileLines.lineTo(newX, newY);
      }
    }

    tileLines.closePath();
    return tileLines;
  }

  @Override
  protected void paintDisc(Graphics2D g2d, Point tile) throws IllegalArgumentException {
    super.paintDisc(g2d, tile);
    Disc disc = this.game.getDisc(tile);

    if (!disc.hasDisc()) {
      return;
    }

    Point discCoordinate = tileCoordsToPixelCoords(tile);
    double discDiameter = calcTileSideLength();

    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate((getWidth() - discDiameter) / 2, (getHeight() - discDiameter) / 2);

    Color oldColor = g2d.getColor();
    g2d.setColor(disc.getDiscColor() == Disc.DiscColor.BLACK
            ? this.player1DiscColor : this.player2DiscColor);
    g2d.fillOval(discCoordinate.x, discCoordinate.y, (int) discDiameter,
            (int) discDiameter);
    g2d.setColor(oldColor);
    g2d.setTransform(oldTransform);
  }

  @Override
  protected Point getPixelCenterCoordForTile(Point tile) throws IllegalArgumentException {
    super.getPixelCenterCoordForTile(tile);
    Point pixelCoord = tileCoordsToPixelCoords(tile);
    return new Point((int) (pixelCoord.x + (getWidth() - calcTileSideLength()) / 2),
            (int) (pixelCoord.y + (getHeight() / 2.0)));
  }
}