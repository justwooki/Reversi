package cs3500.reversi.main.strategy.infallible;

import java.awt.Point;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * Reversi strategy that focuses on maximizing the score by capturing as many pieces/discs on this
 * turn as possible. If two tiles return in a tie for the number of discs captured, then the
 * uppermost-leftmost tile is chosen with regard to the game board. Hence, the uppermost tile is
 * prioritized first. If some tiles are equally "upmost", then the left-most tile is chosen. If no
 * playable tiles can be found, the strategy will pass the move instead.
 */
public final class CaptureMostPieces implements InfallibleReversiStrategy {

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int score = model.getScore(turn);
    int piecesGained = 0;
    Point chosenTile = null;

    for (Point tile : model.getBoard().keySet()) {
      MutableReversiModel branchedModel = model.copyGame();
      try {
        branchedModel.placeDisc(tile);
      } catch (IllegalStateException ignore) {
        continue;
      }

      int newPiecesGained = branchedModel.getScore(turn) - score;

      if (chosenTile == null || newPiecesGained > piecesGained) {
        // no tile has been chosen yet or a better one has been found
        chosenTile = tile;
        piecesGained = newPiecesGained;
      } else if (newPiecesGained == piecesGained) {
        // if two tiles tie for the number of pieces captured
        if (tile.y > chosenTile.y) {
          chosenTile = tile;
        } else if (tile.y == chosenTile.y) {
          if (tile.y < chosenTile.x) {
            chosenTile = tile;
          }
        }
      }
    }

    return chosenTile == null ? new Move(true) : new Move(chosenTile);
  }
}
