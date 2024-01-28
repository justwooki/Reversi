package cs3500.reversi.main.strategy.fallible;

import java.util.Optional;

import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * An interface describing incomplete or partial Reversi strategies that might successfully choose
 * a move and might not.
 */
public interface FallibleReversiStrategy {

  /**
   * Chooses a move used to make move on depending on strategy used.
   *
   * @param model the game board
   * @return a move, if one is found
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException;
}
