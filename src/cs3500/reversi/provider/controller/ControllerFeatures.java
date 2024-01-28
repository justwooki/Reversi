package cs3500.reversi.provider.controller;

import java.awt.Point;

/**
 * Represents the features that the controller are able to perform. These include the two primary
 * moves that can be made in Reversi.
 */
public interface ControllerFeatures {

  /**
   * Attempts to place a disc on a specified cell on behalf of the player in the controller.
   *
   * @param cell coordinates of the cell in which the player is trying to place their disc on
   */
  void placeDisc(Point cell) throws IllegalStateException;

  /**
   * Indicates that the player in the controller wants to pass.
   */
  void indicatePass();
}