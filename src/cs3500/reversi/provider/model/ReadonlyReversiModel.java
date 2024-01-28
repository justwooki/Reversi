package cs3500.reversi.provider.model;

import java.awt.Point;

import cs3500.reversi.provider.board.ReversiGameBoard;
import cs3500.reversi.provider.board.Disc;
import cs3500.reversi.provider.player.ReversiPlayer;

/**
 * Represents a model for a game of Reversi, but only the observable aspects of the game. The model
 * keeps track of the state of the game, and the observational methods below give information about
 * those states.
 */
public interface ReadonlyReversiModel {
  /**
   * Signals if the game is over or not.
   *
   * @return true if game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Returns the current score of the specified player, which is the sum of the specified player's
   * discs on the board.
   *
   * @return the score of the specified player
   * @throws IllegalArgumentException the specified player does not exist or is null
   */
  int getPlayerScore(ReversiPlayer whichPlayer) throws IllegalArgumentException;

  /**
   * Returns the size of the game board.
   *
   * @return the size of the game board
   */
  int getBoardSize();

  /**
   * Returns the disc that is on the game board at the specific coordinates.
   *
   * @param coordinates coordinates of which disc is being requested from the game board
   * @return the disc that is on the game board at the specific coordinates
   * @throws IllegalArgumentException if the specified coordinates are null
   */
  Disc getDiscAt(Point coordinates) throws IllegalArgumentException;

  /**
   * Returns a list of the coordinates of the discs placed on the game board.
   *
   * @return a list of the coordinates of the discs placed on the game board
   */
  java.util.List<Point> getCoordsForPlacedDiscs();

  /**
   * Returns whether the given player can make any moves or not.
   *
   * @param whichPlayer current player being checked
   * @return whether the current player can make any moves or not
   * @throws IllegalArgumentException the specified player does not exist or is null
   */
  boolean playerCanMakeAnyMoves(ReversiPlayer whichPlayer) throws IllegalArgumentException;

  /**
   * Returns whether the current turn player can place a disc at the given coords.
   *
   * @param coords the coords that the player wants to place their disc on
   * @return whether the current turn player can place the disc or not
   * @throws IllegalArgumentException if the specified coords are null
   */
  boolean currentTurnPlayerCanPlaceAtGivenCoord(Point coords) throws IllegalArgumentException;

  /**
   * Returns the current turn player. That is, the player whose turn it is.
   *
   * @return the current turn player
   */
  ReversiPlayer getCurrentTurnPlayer();

  /**
   * Returns a copy of the current game board.
   *
   * @return a copy of the current game board
   */
  ReversiGameBoard getGameBoard();

  /**
   * Returns a copy of this model.
   *
   * @return a copy of this model
   */
  ReversiModel getCopyOfModel();

  /**
   * Returns a copy of the player with most points at the current state of the game.
   *
   * @return a copy of the player with the most points
   */
  ReversiPlayer getCopyOfPlayerWithMostPoints();
}
