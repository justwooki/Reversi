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
 * Represents a Java Swing panel that visually displays a Reversi square game board. The board to
 * be displayed includes each individual square that makes up the board along with every disc that
 * has been placed on the board.
 *
 * <p>Each square tile is positioned using x-y coordinates with the origin at the top-left corner
 * of the board (0, 0). The x-coordinate determines the row position (increasing from the left side
 * of the board to the right side), and the y-coordinate determines the column position (increasing
 * from the top of the board to the bottom) on the game board.</p>
 *
 * <p>For more information on discs, see {@link Disc}.</p>
 */
public class SquareBoardPanel extends AbstractGameBoardPanel {

  /**
   * Constructs a SquareBoardPanel object that takes a model of the Reversi game.
   *
   * @param game the Reversi model
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public SquareBoardPanel(ROReversiModel game) throws IllegalArgumentException {
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
    return Math.min(getWidth(), getHeight()) / (this.game.getGameBoardSideLength() + 1.0);
  }

  @Override
  protected Point tileCoordsToPixelCoords(Point tile) throws IllegalArgumentException {
    super.tileCoordsToPixelCoords(tile);
    double tileSideLength = calcTileSideLength();

    int pixelX = (int) (tile.x * tileSideLength);
    int pixelY = (int) (tile.y * tileSideLength);

    return new Point(pixelX, pixelY);
  }

  @Override
  protected Path2D.Double buildTile(Point tile) throws IllegalArgumentException {
    super.buildTile(tile);
    Path2D.Double tileLines = new Path2D.Double();
    double tileSideLength = calcTileSideLength();

    // subtract the number of tiles * tileLength from the screenWidth
    // . This returns the margins.
    // Divide by two for the left margin.

    int spacingX = (int) (tileSideLength * 0.5
            + (getWidth() - game.getGameBoardSideLength() * tileSideLength) / 2);
    int spacingY = (int) (tileSideLength * 0.5
            + (getHeight() - game.getGameBoardSideLength() * tileSideLength) / 2);


    // gets corner coordinates of tile to build
    tileLines.moveTo(tile.x + tileSideLength / 2 + spacingX,
            tile.y + tileSideLength / 2 + spacingY);
    tileLines.lineTo(tile.x - tileSideLength / 2 + spacingX,
            tile.y + tileSideLength / 2 + spacingY);
    tileLines.lineTo(tile.x - tileSideLength / 2 + spacingX,
            tile.y - tileSideLength / 2 + spacingY);
    tileLines.lineTo(tile.x + tileSideLength / 2 + spacingX,
            tile.y - tileSideLength / 2 + spacingY);
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
    double discDiameter = calcTileSideLength() / 2;
    int unit = (int) (0.5 * discDiameter);

    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate(unit + (getWidth() - game.getGameBoardSideLength() * calcTileSideLength()) / 2,
            unit + (getHeight() - game.getGameBoardSideLength() * calcTileSideLength()) / 2);

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
    int unit = (int) (calcTileSideLength() / 4);
    return new Point((int) (pixelCoord.x
            + (unit + (getWidth() - game.getGameBoardSideLength() * calcTileSideLength()) / 2)),
            (int) (pixelCoord.y + (unit
                    + (getHeight() - game.getGameBoardSideLength() * calcTileSideLength()) / 2)));
  }
}
