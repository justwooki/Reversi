package cs3500.reversi.main.strategy.fallible;

import java.awt.Point;
import java.util.Optional;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * Reversi strategy that maximizes the score by making moves only on tiles that aren't next to
 * any corner tiles because that would give the opponent the ability to get a corner on their
 * next turn.
 */
public final class AvoidTilesBorderingCorners implements FallibleReversiStrategy {

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int score = model.getScore(turn);
    int piecesGained = 0;
    Point chosenTile = null;

    for (Point tile : model.getBoard().keySet()) {
      // skip tiles that are next to any corners
      if (tileBordersCornerTiles(tile, model)) {
        continue;
      }

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
          if (tile.x < chosenTile.x) {
            chosenTile = tile;
          }
        }
      }
    }

    return chosenTile == null ? Optional.empty() : Optional.of(new Move(chosenTile));
  }

  /**
   * Determines whether a specific tile is adjacent to any one of the tiles that are the corners
   * of the game board.
   *
   * @param tile the tile that may or may not border a corner tile
   * @param model the model
   * @return <code>true</code> if the given tile borders a corner tile and <code>false</code>
   *         otherwise
   * @throws IllegalArgumentException if the given tile is <code>null</code> OR the given model is
   *                                 <code>null</code>
   */
  private boolean tileBordersCornerTiles(Point tile, ROReversiModel model)
          throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    } else if (model == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    for (Point corner : model.getCornerTiles()) {
      if (model.isAdjacent(tile, corner)) {
        return true;
      }
    }

    return false;
  }
}
