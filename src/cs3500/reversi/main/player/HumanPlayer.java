package cs3500.reversi.main.player;

import java.util.Optional;

import cs3500.reversi.main.strategy.Move;

/**
 * A human player is a player that chooses a move through the view. The human player does not
 * choose its move in this class. The player will instead choose their move through the view in
 * which they will directly interact with. In other words, this class simply exists to embody the
 * representation of the human player rather than the complete set of behaviors that the human
 * player should emit.
 */
public final class HumanPlayer implements Player {

  @Override
  public Optional<Move> selectMove() {
    return Optional.empty();
  }
}
