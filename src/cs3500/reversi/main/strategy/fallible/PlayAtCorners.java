package cs3500.reversi.main.strategy.fallible;

import java.awt.Point;
import java.util.Optional;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * Reversi strategy that focuses on playing at the corners of the game board since discs in corners
 * cannot be captured because they don’t have tiles on their other side.
 */
public final class PlayAtCorners implements FallibleReversiStrategy {

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int score = model.getScore(turn);
    int piecesGained = 0;
    Point chosenTile = null;

    for (Point tile : model.getCornerTiles()) {
      MutableReversiModel branchedModel = model.copyGame();
      try {
        branchedModel.placeDisc(tile);
      } catch (IllegalStateException ignore) {
        continue;
      }

      int newPiecesGained = branchedModel.getScore(turn) - score;
      if (chosenTile == null || newPiecesGained > piecesGained) {
        chosenTile = tile;
        piecesGained = newPiecesGained;
      }
    }

    return chosenTile == null ? Optional.empty() : Optional.of(new Move(chosenTile));
  }
}
