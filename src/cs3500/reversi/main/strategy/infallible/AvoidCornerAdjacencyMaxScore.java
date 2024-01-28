package cs3500.reversi.main.strategy.infallible;

import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.fallible.AvoidTilesBorderingCorners;
import cs3500.reversi.main.strategy.Move;
import cs3500.reversi.main.strategy.fallible.FallibleReversiStrategy;

/**
 * Reversi strategy that focuses on playing at tiles that aren't next to corners that maximize the
 * score. If this strategy fails, the strategy will simply select whichever tile maximizes the
 * score regardless of where it is.
 *
 * @see AvoidTilesBorderingCorners
 * @see CaptureMostPieces
 */
public final class AvoidCornerAdjacencyMaxScore implements InfallibleReversiStrategy {
  private final FallibleReversiStrategy avoidAdjToCorners;
  private final InfallibleReversiStrategy maxScoreStrategy;

  /**
   * Constructs AvoidCornerAdjacencyMaxScore object that initializes the two strategies to stack
   * onto each other.
   */
  public AvoidCornerAdjacencyMaxScore() {
    this.avoidAdjToCorners = new AvoidTilesBorderingCorners();
    this.maxScoreStrategy = new CaptureMostPieces();
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    // IllegalArgumentException thrown by avoidAdjToCorners and maxScoreStrategy
    // IllegalStateException thrown by maxScoreStrategy
    return this.avoidAdjToCorners.chooseMove(model).orElseGet(()
        -> this.maxScoreStrategy.chooseMove(model));
  }
}
