package cs3500.reversi.provider.controller;

import java.awt.Point;

/**
 * Represents a controller for a game of Reversi. This should be used as the entry point
 * of the application by being used in the main method.
 */
public interface ReversiController extends ControllerFeatures {

  /**
   * Process a given string command and return status or error as a string.
   *
   * @param command the command given
   * @return status or error message
   */
  String processCommand(Point command);
}