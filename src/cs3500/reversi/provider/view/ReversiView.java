package cs3500.reversi.provider.view;

import java.awt.Point;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Represents a view for a game of Reversi. The view shows the user the state of the game that is
 * being stored in the model for the game.
 */
public interface ReversiView {
  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;

  /**
   * Make the view visible. This is usually called after the view is constructed.
   */
  void makeVisible();

  /**
   * Provide the view with a callback option to process a command.
   *
   * @param callback object
   */
  void setCommandCallback(Consumer<Point> callback);

  /**
   * Transmit an error message to the view, in case the command could not be processed correctly.
   *
   * @param error message to view
   */
  void showErrorMessage(String error);

  /**
   * Signal the view to draw itself.
   */
  void refresh();
}
