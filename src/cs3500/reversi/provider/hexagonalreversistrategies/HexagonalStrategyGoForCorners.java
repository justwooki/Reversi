package cs3500.reversi.provider.hexagonalreversistrategies;

import java.awt.Point;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy in which the player should try to place a disc in a cell that is a corner
 * (if there are more than one, it picks the upper-left most).
 */
public class HexagonalStrategyGoForCorners implements ReversiStrategy {
  @Override
  public Point chooseMove(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    return this.coordToPlaceDiscAtCorner(model.getCopyOfModel());
  }

  private Point coordToPlaceDiscAtCorner(ReversiModel model) {
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(model.getBoardSize() / 4, 0))) {
      return new Point(model.getBoardSize() / 4, 0);
    }
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(model.getBoardSize() / 4
            + model.getBoardSize() / 2, 0))) {
      return new Point(model.getBoardSize() / 4 + model.getBoardSize() / 2, 0);
    }
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(0, model.getBoardSize() / 2))) {
      return new Point(0, model.getBoardSize() / 2);
    }
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(model.getBoardSize() - 1,
            model.getBoardSize() / 2))) {
      return new Point(model.getBoardSize() - 1, model.getBoardSize() / 2);
    }
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(model.getBoardSize() / 4,
            model.getBoardSize() - 1))) {
      return new Point(model.getBoardSize() / 4, model.getBoardSize() - 1);
    }
    if (model.currentTurnPlayerCanPlaceAtGivenCoord(new Point(model.getBoardSize() / 4
            + model.getBoardSize() / 2, model.getBoardSize() - 1))) {
      return new Point(model.getBoardSize() / 4 + model.getBoardSize() / 2,
              model.getBoardSize() - 1);
    }

    for (int row = 0; row < model.getBoardSize(); row++) {
      for (int col = 0; col < model.getBoardSize(); col++) {

        Point curCoord = new Point(col, row);
        if (model.currentTurnPlayerCanPlaceAtGivenCoord(curCoord)) {
          return curCoord;
        }

      }
    }

    return null;
  }
}