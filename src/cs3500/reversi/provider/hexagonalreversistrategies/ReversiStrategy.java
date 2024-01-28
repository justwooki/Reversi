package cs3500.reversi.provider.hexagonalreversistrategies;

import java.awt.Point;

import cs3500.reversi.provider.model.ReversiModel;

/**
 * Represents a strategy that a player, most likely an AI, could use in a game of Reversi.
 */
public interface ReversiStrategy {

  /**
   * Chooses the coord that is the best for the specific strategy the function object represents.
   *
   * @param model the model to be used in determining the best move to make
   * @return the coord that is the best for the specific strategy
   * @throws IllegalArgumentException if the specified model is null
   */
  Point chooseMove(ReversiModel model) throws IllegalArgumentException;
}
