package cs3500.reversi.provider.view;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import cs3500.reversi.provider.board.Disc;
import cs3500.reversi.provider.model.ReadonlyReversiModel;

/**
 * Represents a hexagon to be used in the HexagonalReversiGraphicsView to represent a cell on the
 * hexagonal board.
 */
public class HexagonsPanel extends JPanel {

  private ReadonlyReversiModel model;

  private Point previouslySelectedCellInRegularCoords;
  private Point selectedCell; // Track selected cell
  private Map<Polygon, Point> hexagonsToCoordsList;
  private Map<Ellipse2D, Point> circToCoord;

  private double size;

  /**
   * Constructor which initializes the x value, y value, width of the hexagon, and the size of the
   * board that the hexagon is on.
   */
  public HexagonsPanel(ReadonlyReversiModel model) {
    this.model = model;
    this.hexagonsToCoordsList = new HashMap<>();
    this.circToCoord = new HashMap<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    int x = 250;
    int y = 250;
    int shift = 250;
    this.size = (250.0 / this.model.getBoardSize());
    this.hexagonsToCoordsList = new HashMap<>();
    Graphics2D g2d = (Graphics2D) g.create();


    for (int i = 0; i <= this.model.getBoardSize() + 1; i++) {
      for (int j = 0; j <= this.model.getBoardSize() + 1; j++) {
        if (this.model.getDiscAt(new Point(j, i)) != Disc.NONEXISTENT) {
          this.drawShape(g2d, x, y, new Point(j, i), size);
          x += (Math.sqrt(3) * size);
        }
      }
      if ((i < (model.getBoardSize()) / 2)) {
        shift -= (Math.sqrt(3) * size / 2);
      } else {
        shift += (Math.sqrt(3) * size / 2);
      }
      x = shift;
      y += (1.5 * size);
    }


  }

  private void drawShape(Graphics2D g2d, int x, int y, Point point, double size) {

    //Graphics2D graphics2D = (Graphics2D) g.create();

    int[] xPointsHexagon = new int[6];
    int[] yPointsHexagon = new int[6];

    for (int i = 0; i < 6; i++) {
      double angle = 2.0 * Math.PI * (i + 0.5) / 6;
      xPointsHexagon[i] = (int) (x + size * Math.cos(angle));
      yPointsHexagon[i] = (int) (y + size * Math.sin(angle));
    }

    Polygon polygon = new Polygon(xPointsHexagon, yPointsHexagon, 6);
    Rectangle bounds = polygon.getBounds();
    Ellipse2D.Double circle = new Ellipse2D.Double(x - bounds.width / 2, y - bounds.width / 2,
            bounds.width, bounds.width);


    // Fill the selected hexagon if it's selected
    if (selectedCell != null) {
      if (selectedCell.equals(point)) {
        Rectangle r = polygon.getBounds();
        //g2d.setColor(Color.BLUE);
        g2d.setColor(Color.CYAN); // Set the color to fill the selected hexagon
        System.out.println("CYAN");
        g2d.fillPolygon(polygon); // Fill the hexagon
        //g2d.drawRect(r.x, r.y, r.width, r.height);
        g2d.draw(circle);
      }
    }
    g2d.setColor(Color.black);
    g2d.drawPolygon(polygon);

    if (model.getDiscAt(point) == Disc.BLACK) {
      g2d.setColor(Color.BLACK);
      g2d.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
    } else if (model.getDiscAt(point) == Disc.WHITE) {
      g2d.setColor(Color.WHITE);
      g2d.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);
    }

    this.hexagonsToCoordsList.put(polygon, point);
    this.circToCoord.put(circle, point);
  }

  /**
   * Updates the selected cell.
   *
   * @param clickedPoint the point to be updated
   */
  public void updateSelectedCell(Point clickedPoint) {
    selectedCell = null;
    System.out.println(this.hexagonsToCoordsList.size());
    clickedPoint = new Point(clickedPoint.x, (int) (clickedPoint.y - 1.5 * this.size));

    for (Map.Entry<Polygon, Point> entry : hexagonsToCoordsList.entrySet()) {
      if (entry.getKey().contains(clickedPoint)) {
        if (previouslySelectedCellInRegularCoords == null
                || !previouslySelectedCellInRegularCoords.equals(entry.getValue())) {

          System.out.println("Clicked on coord (" + entry.getValue().x + ", " + entry.getValue().y
                  + ")");
          previouslySelectedCellInRegularCoords = entry.getValue();
          //selectedCell = clickedPoint;


          if (entry.getKey().contains(clickedPoint)) {
            selectedCell = entry.getValue();
          }

        } else {
          previouslySelectedCellInRegularCoords = null;
        }
        break;
      }
    }
    this.repaint();
  }

  /**
   * Gets the coordinate of the selected cell.
   *
   * @return coordinate of selected cell
   */
  public Point getSelectedCell() {
    return new Point(this.selectedCell.x, this.selectedCell.y);
  }

  /**
   * Gets the coordinate of the selected cell.
   *
   * @return coordinate of the previously selected cell
   */
  public Point getPreviouslySelectedCellInRegularCoords() {
    return new Point(this.previouslySelectedCellInRegularCoords.x,
            this.previouslySelectedCellInRegularCoords.y);
  }
}