package cs3500.reversi.provider.hexagonalreversistrategies;

import java.awt.Point;

import cs3500.reversi.provider.board.ReversiGameBoard;
import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy in which the player should try to place a disc in a cell that will increase
 * their score the most in that turn. (if there are more than one, it picks the upper-left most).
 */
public class HexagonalStrategyMostPointsGreedy implements ReversiStrategy {
  @Override
  public Point chooseMove(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    return this.coordToPlaceDiscAtForMostPoints(model.getCopyOfModel());
  }

  /**
   * Returns the best coords to place a disc at according to strategy one (the upper-left most coord
   * that will give the current player the highest score possible on that turn).
   *
   * @return the coord that will give the current player the most points if they place a disc there
   *         (returns null if no such coord exists).
   */
  private Point coordToPlaceDiscAtForMostPoints(ReversiModel model) {

    // make copy of board to save old state before testing best coords

    ReversiGameBoard actualBoard = model.getGameBoard().getCopyOfBoard();

    int maxCurrentTurnPlayerScore = model.getPlayerScore(model.getCurrentTurnPlayer());
    Point bestCoord = null;
    int size = model.getBoardSize();

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        Point curCoord = new Point(col, row);
        if (model.currentTurnPlayerCanPlaceAtGivenCoord(curCoord)) {
          model.placeDisc(curCoord);
          int newScore = model.getPlayerScore(model.getCurrentTurnPlayer());

          if (newScore > maxCurrentTurnPlayerScore) {
            maxCurrentTurnPlayerScore = newScore;
            bestCoord = curCoord;
          }

          model.setBoard(actualBoard.getCopyOfBoard());
        }
      }
    }

    return bestCoord;
  }
}