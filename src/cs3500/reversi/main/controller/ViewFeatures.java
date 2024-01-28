package cs3500.reversi.main.controller;

import java.awt.Point;

import cs3500.reversi.main.model.Disc;

/**
 * Represents various features and abilities of the view for any view listener to respond to.
 */
public interface ViewFeatures {

  /**
   * Makes a move on the Reversi board at the specified tile. This method is to be called when
   * a move is made through the view. If any exception is thrown, this method should handle them
   * accordingly such that the game can continue normally.
   *
   * @param tile the tile where the move is to be made
   */
  void makeMove(Point tile);

  /**
   * Passes the turn in the Reversi game. This method is called when a move is passed through the
   * view. If any exception is thrown, this method should handle them accordingly such that the
   * game can continue normally.
   */
  void pass();

  /**
   * Gets the disc (holding the disc color) of the player whom the view belongs to. This is to be
   * used by the view to specifically ask its view listener for a specific disc. This is useful
   * when the view needs to display information specific to a player regarding their disc, and the
   * view listener can decide which disc to provide the view back with. If the view listener
   * decides not to provide a disc for whatever reason, <code>null</code> will be returned.
   *
   * @return the disc of the player for the view to use, or <code>null</code> if this view listener
   *         decides not to provide a disc
   */
  Disc getPlayerDisc();
}
