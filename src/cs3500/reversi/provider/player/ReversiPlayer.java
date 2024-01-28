package cs3500.reversi.provider.player;

import java.awt.Point;
import java.util.function.Consumer;

import cs3500.reversi.provider.board.Disc;

/**
 * Represents a player for a game of Reversi. A player is typically used in the model for a game of
 * Reversi to keep track of the players and their possible attributes such as their player type and
 * disc color.
 */
public interface ReversiPlayer {

  /**
   * Gets the disc color of this player.
   *
   * @return this player's disc color
   */
  Disc getDiscColor();

  /**
   * Selects the cell and attempts to place a disc at the given coords.
   *
   * @param cell the cell in which the player wants to place their disc
   */
  void selectCellAndPlaceDisc(Point cell);

  /**
   * Indicates that the player wants to pass.
   */
  void indicatePass();

  /**
   * Represents how the player makes a move (for AI, it will use strategy, and for a human, they
   * will most likely use some type of view).
   */
  void makeMove();

  /**
   * Returns a copy of the player.
   *
   * @return a copy of the player
   */
  ReversiPlayer getCopyOfPlayer();

  /**
   * Sets the command callback for the player to interact with the controller.
   */
  void setCommandCallback(Consumer<Point> callback);

  /**
   * Updates the view.
   */
  void updateView();

  /**
   * Sets the command callback for the view to refresh.
   *
   * @param callback the command callback
   */
  void setViewUpdateCallback(Consumer<String> callback);
}