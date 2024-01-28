package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.Map;

import cs3500.reversi.main.controller.ModelFeatures;

/**
 * A mutable Reversi board in which proper gameplay and modifications to the board can be made.
 */
public interface MutableReversiModel extends ROReversiModel {

  /**
   * Places the current disc at a specified tile, ending the current turn.
   *
   * @param tile the tile
   * @throws IllegalStateException if the game is over OR the move cannot be legally played
   * @throws IllegalArgumentException if the given tile is invalid
   */
  void placeDisc(Point tile) throws IllegalStateException, IllegalArgumentException;

  /**
   * Immediately ends the current turn without making a move.
   *
   * @throws IllegalStateException if the game is over
   */
  void passTurn() throws IllegalStateException;

  /**
   * Sets current game board to the given game board.
   *
   * @param gameBoard the new game board to set
   * @throws IllegalArgumentException if the given game board is <code>null</code> OR the
   *                                  dimensions of the given game board does not match the
   *                                  dimensions of the current game board
   */
  void setBoard(Map<Point, Disc> gameBoard) throws IllegalArgumentException;

  /**
   * Adds a feature to the model representing a model listener. The model can only hold two model
   * listeners at most. Since Reversi is a game solely between two players, there is no reason for
   * there to be more than two listeners.
   *
   * @param feature the feature to be added
   * @throws IllegalArgumentException if the feature to be added is <code>null</code> OR the
   *                                  feature has already been added OR the two features have
   *                                  already been added
   */
  void addFeatures(ModelFeatures feature) throws IllegalArgumentException;

  /**
   * Starts the Reversi game, notifies all model listeners that the game has started, and notifies
   * the first model listener that the game has started.
   *
   * @throws IllegalStateException if the game has already started
   */
  void startGame() throws IllegalStateException;
}
