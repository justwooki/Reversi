package cs3500.reversi.main.view.graphical;

import cs3500.reversi.main.controller.ViewFeatures;

/**
 * The interface for the frame component of a graphical-based view, to be used in the Reversi game.
 */
public interface GraphicalFrameView {

  /**
   * Sets the view listener to the given feature. Each view can have only one listener.
   *
   * @param feature the given feature to set
   * @throws IllegalArgumentException if the given feature is <code>null</code>
   */
  void setFeature(ViewFeatures feature) throws IllegalArgumentException;

  /**
   * Updates the view.
   */
  void updateView();

  /**
   * Displays a specific error message somewhere on the view.
   *
   * @param message the error message to display
   * @throws IllegalArgumentException if the given error message to display is <code>null</code>
   */
  void displayErrorMessage(String message) throws IllegalArgumentException;
}