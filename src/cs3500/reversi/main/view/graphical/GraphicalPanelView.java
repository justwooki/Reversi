package cs3500.reversi.main.view.graphical;

import cs3500.reversi.main.controller.ViewFeatures;

/**
 * The interface for the panel component of a graphical-based view, to be used in the Reversi game.
 */
public interface GraphicalPanelView {

  /**
   * Sets the view listener to the given feature. Each panel can have only one listener.
   *
   * @param feature the given feature to set
   * @throws IllegalArgumentException if the given feature is <code>null</code>
   */
  void setFeature(ViewFeatures feature) throws IllegalArgumentException;
}
