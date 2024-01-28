package cs3500.reversi.provider.model;

import java.awt.Point;

import cs3500.reversi.provider.board.ReversiGameBoard;
import cs3500.reversi.provider.player.ReversiPlayer;

/**
 * Represents a model for a game of Reversi. The model keeps track of the state of the game, and
 * allows for actions to be made that change the game.
 */
public interface ReversiModel extends ModelFeatures {

  /**
   * Places a disc on the game board of the model.
   *
   * @param coordinates the coordinates of where to place the disc on the board
   * @throws IllegalArgumentException if the coordinates or disc are invalid
   * @throws IllegalStateException    if the disc cannot be placed at the specified coordinates
   */
  void placeDisc(Point coordinates) throws IllegalArgumentException, IllegalStateException;

  /**
   * Changes player turns.
   */
  void changePlayerTurn();

  /**
   * Passes a turn for the current turn player and increases the number of passes that have been
   * made.
   */
  void pass();

  /**
   * Sets the current game board. Used for when a board in progress is needed to be set.
   */
  void setBoard(ReversiGameBoard board);

  /**
   * Starts the game by setting up the two players as observers that can receive notifications.
   */
  void startGame(ReversiPlayer player1, ReversiPlayer player2);
}