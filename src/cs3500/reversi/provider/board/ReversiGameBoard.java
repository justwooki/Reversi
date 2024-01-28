package cs3500.reversi.provider.board;

import java.awt.Point;
import java.util.Map;

import cs3500.reversi.provider.player.ReversiPlayer;

/**
 * Represents a game board for a game of Reversi. It is intended to be used in a model for a game of
 * Reversi.
 */
public interface ReversiGameBoard {

  /**
   * Returns the current score of the specified player, which is the sum of the specified player's
   * discs on the board.
   *
   * @return the score of the specified player
   * @throws IllegalArgumentException the specified player is null
   */
  int getPlayerScore(ReversiPlayer player) throws IllegalArgumentException;

  /**
   * Adds the specified disc to the board at the specified coordinates.
   *
   * @throws IllegalArgumentException the specified coordinates are null or if the disc to place
   *                                  is null
   */
  void addDiscToBoard(Point coordinates, Disc discToPlace) throws IllegalArgumentException;

  /**
   * Returns the disc type that is on the game board at the specific coordinates.
   *
   * @param coordinates coordinates of which disc is being requested from the game board
   * @return the disc that is on the game board at the specific coordinates
   * @throws IllegalArgumentException if the specified coordinates are null
   */
  Disc getDiscAt(Point coordinates) throws IllegalArgumentException;

  /**
   * Returns a map of the board data.
   *
   * @return the data about the board
   */
  Map<Point, Disc> getBoardData();

  /**
   * Returns a list of the coordinates for the discs that have been placed.
   *
   * @return the coordinates for the placed discs.
   */
  java.util.List<Point> getCoordsForPlacedDiscs();

  /**
   * Returns a copy of this game board.
   *
   * @return a copy of this game board
   */
  ReversiGameBoard getCopyOfBoard();
}
