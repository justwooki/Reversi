package cs3500.reversi.main.strategy.fallible;

import java.util.Optional;

import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * Reversi strategy that focuses on playing at the corners of the game board. If this strategy
 * fails, the strategy will focus on playing at tiles that aren't next to corners that maximize
 * the score.
 *
 * @see PlayAtCorners
 * @see AvoidTilesBorderingCorners
 */
public final class CornerStrat implements FallibleReversiStrategy {
  private final FallibleReversiStrategy playCorners;
  private final FallibleReversiStrategy avoidAdjToCorners;

  /**
   * Constructs PlayCornersAvoidCornerAdjacency object that initializes the two strategies to stack
   * onto each other.
   */
  public CornerStrat() {
    this.playCorners = new PlayAtCorners();
    this.avoidAdjToCorners = new AvoidTilesBorderingCorners();
  }

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException {
    // IllegalArgumentException thrown by playCorners and avoidAdjToCorners
    return this.playCorners.chooseMove(model).or(() -> this.avoidAdjToCorners.chooseMove(model));
  }
}
