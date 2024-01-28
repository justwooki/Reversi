package cs3500.reversi.main.strategy.fallible;

import java.util.Optional;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.Move;

/**
 * Reversi strategy that focuses on passing the current turn to end the game if the score is higher
 * than the opponent's score. By forcing the game to end while the current player has the highest
 * score, the current player is guaranteed to win.
 *
 * <p>It's important to note that this strategy operates under the assumption that the game will
 * end after a certain number of consecutive passes take place, and the winner of the game is
 * determined solely by the highest score. It also assumes that passing will not directly affect
 * the current player's score.</p>
 *
 * <p>The name of this strategy, "Cherry Picker", is a sports term that refers to when a player
 * waits near the goal until an opportunity to score easy points arrives in front of the player.
 * Similarly, this strategy only acts if an easy and immediate win is explicitly available - hence
 * the name, "Cherry Picker".</p>
 */
public final class CherryPicker implements FallibleReversiStrategy {

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    final Disc turn = model.getTurn();
    final int playerScore = model.getScore(turn);
    final int opponentScore = model.getScore(model.getTurn().getOppositeDisc());
    final Optional<Integer> maxNumConsecutivePassesAllowed
            = model.getMaxNumConsecutivePassesAllowed();

    if (maxNumConsecutivePassesAllowed.isPresent() && playerScore > opponentScore
            && model.getConsecutivePasses() == maxNumConsecutivePassesAllowed.get() - 1) {
      return Optional.of(new Move(true));
    } else {
      return Optional.empty();
    }
  }
}
