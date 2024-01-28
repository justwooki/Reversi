package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A read-only Reversi board in which information regarding the board and status of the game can
 * be retrieved, but never modified. The progress of the game can only be simply viewed upon.
 */
public interface ROReversiModel {

  /**
   * Gets the board for the current game.
   *
   * @return the game board
   */
  Map<Point, Disc> getBoard();

  /**
   * Determines whether any move can be made on the board by the given disc color.
   *
   * @param color the disc color
   * @return <code>true</code> if the disc of the given disc color can be placed on at least one
   *         position on the board and <code>false</code> otherwise
   * @throws IllegalArgumentException if the given disc color is <code>null</code> OR the given
   *                                  disc's color isn't black or white
   */
  boolean canPlaceDisc(Disc color) throws IllegalArgumentException;

  /**
   * Determines whether a move can be made at a specific tile on the board by the given
   * disc color.
   *
   * @param color the disc color
   * @param tile the tile
   * @return <code>true</code> if the disc of the given disc color can be placed on the given
   *         tile and <code>false</code> otherwise
   * @throws IllegalArgumentException if the given disc color is <code>null</code> OR the given
   *                                  disc's color isn't black or white OR the given tile is
   *                                  <code>null</code>
   */
  boolean canPlaceDiscAtTile(Disc color, Point tile) throws IllegalArgumentException;

  /**
   * Checks whether the game is over or not. The game can be declared over when the board is
   * completely filled, no moves can be made on the board, or two consecutive passes occur.
   *
   * @return <code>true</code> if the game is over and <code>false</code> otherwise
   */
  boolean gameOver();

  /**
   * Get the disc at the given tile.
   *
   * @param tile the tile
   * @return the disc corresponding to the given position
   * @throws IllegalArgumentException if the given tile is <code>null</code> or invalid
   */
  Disc getDisc(Point tile) throws IllegalArgumentException;

  /**
   * Gets the current turn.
   *
   * @return the current turn
   */
  Disc getTurn();

  /**
   * Gets the current score of the game for the current disc. Each tile on the game board with a
   * matching disc counts for one point.
   *
   * @param turn the current disc whose turn it is
   * @return the total score for the current disc whose turn it is
   * @throws IllegalArgumentException if the given disc is <code>null</code> OR the given disc's
   *                                  color isn't black or white
   */
  int getScore(Disc turn) throws IllegalArgumentException;

  /**
   * Gets the side length of the game board.
   *
   * @return the side length
   */
  int getGameBoardSideLength();

  /**
   * Gets the number of consecutive passes that are occurring in the game.
   *
   * @return the number of consecutive passes
   */
  int getConsecutivePasses();

  /**
   * Gets the maximum number of consecutive passes that are allowed in the game.
   *
   * @return the maximum number of consecutive passes that are allowed in the game, if there is one
   */
  Optional<Integer> getMaxNumConsecutivePassesAllowed();

  /**
   * Gets the disc whose color won the game. A winner is determined at the end of the game by
   * whichever disc color has the highest score. In the case that a tie has occurred and there is
   * no definite winner, an empty disc is returned.
   *
   * @return the winner
   * @throws IllegalStateException if the game has not ended
   */
  Disc getWinner() throws IllegalStateException;

  /**
   * Builds a direct copy of the Reversi game.
   *
   * @return a copy of the Reversi game
   */
  MutableReversiModel copyGame();

  /**
   * Gets a list of all corner tiles on the game board.
   *
   * @return a list of all corner tiles on the game board
   */
  List<Point> getCornerTiles();

  /**
   * Checks to see if two given tiles are adjacent to one another on the game board.
   *
   * @param tile1 one tile
   * @param tile2 another tile
   * @return <code>true</code> if both tiles are adjacent to one another and <code>false</code>
   *         otherwise
   * @throws IllegalArgumentException if any of the given tiles are <code>null</code>
   */
  boolean isAdjacent(Point tile1, Point tile2) throws IllegalArgumentException;
}
