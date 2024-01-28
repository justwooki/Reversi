package cs3500.reversi.main.strategy.infallible;

import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.Move;
import cs3500.reversi.main.strategy.fallible.FallibleReversiStrategy;

/**
 * Represents a Reversi strategy whose return value cannot fail. This strategy will always return a
 * non-null {@link Move}, or else throw an exception if they're called on a game that cannot have
 * a move.
 *
 * @see FallibleReversiStrategy
 */
public interface InfallibleReversiStrategy {

  /**
   * Chooses a move used to make move on depending on strategy used.
   *
   * @param model the game board
   * @return a move
   * @throws IllegalArgumentException if the given model is <code>null</code>
   * @throws IllegalStateException if a move cannot be found for whatever reason
   */
  Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException;
}
