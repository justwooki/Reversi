package cs3500.reversi.provider.hexagonalreversistrategies;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy in which the player should try to place a disc in a cell that is not next
 * to a corner (if there are more than one, it picks the upper-left most).
 */
public class HexagonalStrategyAvoidNextToCorners implements ReversiStrategy {
  @Override
  public Point chooseMove(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    return this.coordToPlaceDiscAtNotNextToCorner(model.getCopyOfModel());
  }

  /**
   * Returns the best coords to place a disc at according to strategy two (the upper-left most coord
   * that the current player can make that is not next to a corner).
   *
   * @return the coord that the current player can make that is not next to a corner (returns null
   *         if no such coord exists)
   */
  private Point coordToPlaceDiscAtNotNextToCorner(ReversiModel model) {
    for (int row = 0; row < model.getBoardSize(); row++) {
      for (int col = 0; col < model.getBoardSize(); col++) {

        Point curCoord = new Point(col, row);
        if (model.currentTurnPlayerCanPlaceAtGivenCoord(curCoord) && !this.isNextToCorner(curCoord,
                model.getBoardSize())) {
          return curCoord;
        }

      }
    }
    return null;
  }

  /**
   * Returns whether the given coord is next to a corner or not.
   */
  private boolean isNextToCorner(Point coord, int boardSize) throws IllegalArgumentException {
    if (coord == null) {
      throw new IllegalArgumentException("Coordinates cannot be null.");
    }
    List<Point> listOfCornerCoords = new ArrayList<>();
    listOfCornerCoords.add(new Point(boardSize / 4, 0));
    listOfCornerCoords.add(new Point(boardSize / 4 + boardSize / 2, 0));
    listOfCornerCoords.add(new Point(0, boardSize / 2));
    listOfCornerCoords.add(new Point(boardSize - 1, boardSize / 2));
    listOfCornerCoords.add(new Point(boardSize / 4, boardSize - 1));
    listOfCornerCoords.add(new Point(boardSize / 4 + boardSize / 2, boardSize - 1));

    for (Point p : listOfCornerCoords) {
      if (p.y == 0) {
        if ((p.y == coord.y && p.x == coord.x - 1) || (p.y == coord.y - 1 && p.x == coord.x)
                || (p.y == coord.y - 1 && p.x == coord.x - 1)
                || (p.y == coord.y && p.x == coord.x + 1)) {
          return true;
        }
      } else if (p.y == boardSize / 2) {
        if ((p.y == coord.y && p.x == coord.x - 1)
                || (p.y == coord.y - 1 && p.x == coord.x - 1)
                || (p.y == coord.y + 1 && p.x == coord.x + 1)
                || (p.y == coord.y + 1 && p.x == coord.x - 1)
                || (p.y == coord.y + 1 && p.x == coord.x)
                || (p.y == coord.y - 1 && p.x == coord.x + 1)
                || (p.y == coord.y - 1 && p.x == coord.x)
                || (p.y == coord.y && p.x == coord.x + 1)) {
          return true;
        }
      } else {
        if ((p.y == coord.y && p.x == coord.x - 1) || (p.y == coord.y + 1 && p.x == coord.x)
                || (p.y == coord.y + 1 && p.x == coord.x - 1)
                || (p.y == coord.y && p.x == coord.x + 1)) {
          return true;
        }
      }
    }

    return false;
  }
}