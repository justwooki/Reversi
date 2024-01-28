package cs3500.reversi.main.player;

import java.util.Optional;

import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.infallible.InfallibleReversiStrategy;
import cs3500.reversi.main.strategy.Move;

/**
 * An AI Player is a player that chooses moves based on a specific strategy. Because the AI player
 * uses an infallible strategy, they will always return a move.
 *
 * @see InfallibleReversiStrategy
 */
public final class AIPlayer implements Player {
  private final ROReversiModel model;
  private final InfallibleReversiStrategy strategy;

  /**
   * Constructs an AIPlayer object using the given strategy and model.
   *
   * @param strategy the strategy in which the AI player will base its moves off
   * @param model the Reversi model
   * @throws IllegalArgumentException if the strategy or model is <code>null</code>
   */
  public AIPlayer(InfallibleReversiStrategy strategy, ROReversiModel model)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    } else if (strategy == null) {
      throw new IllegalArgumentException("The strategy cannot be null.");
    }

    this.model = model;
    this.strategy = strategy;
  }

  @Override
  public Optional<Move> selectMove() {
    return Optional.of(this.strategy.chooseMove(this.model));
  }
}
