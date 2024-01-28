package cs3500.reversi.main.view.graphical;

import java.awt.Point;

import cs3500.reversi.main.controller.ViewFeatures;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.provider.view.ReversiView;

/**
 * An adapter that adapts one Reversi view to another. The "adaptee" being adapted is an interface
 * given by the providers representing a Reversi view. The "target" that the adaptee is being
 * adapted to is a graphical frame view.
 *
 * <p>Note: All class/interfaces given by the providers can be found in the provider package.</p>
 *
 * @see ReversiView
 * @see GraphicalFrameView
 */
public final class ProviderReversiViewToGraphicalFrameViewAdapter implements GraphicalFrameView {
  private final ReversiView adaptee;
  private ViewFeatures viewListener;

  /**
   * Constructs a ProviderReversiViewToGraphicalFrameViewAdapter object that takes in a Reversi
   * view adaptee to adapt to the target and an immutable Reversi model that will be needed to set
   * up a default command callback.
   *
   * @param adaptee the Reversi view adaptee to adapt to the target
   * @param model an immutable Reversi model
   * @throws IllegalArgumentException if the given view is <code>null</code> OR the given model is
   *                                  <code>null</code>
   */
  public ProviderReversiViewToGraphicalFrameViewAdapter(ReversiView adaptee, ROReversiModel model)
          throws IllegalArgumentException {
    if (adaptee == null) {
      throw new IllegalArgumentException("The given view cannot be null.");
    } else if (model == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.adaptee = adaptee;
    this.viewListener = null;
    this.adaptee.setCommandCallback((Point point) -> {
      if (this.viewListener != null) {
        if (point == null) {
          this.viewListener.pass();
        } else {
          this.viewListener.makeMove(providerCoordToTileCoord(point, model));
        }
      }
    });
    this.adaptee.makeVisible();
  }

  @Override
  public void setFeature(ViewFeatures feature) throws IllegalArgumentException {
    if (feature == null) {
      throw new IllegalArgumentException("The feature cannot be null.");
    }

    this.viewListener = feature;
  }

  @Override
  public void updateView() {
    this.adaptee.refresh();
  }

  @Override
  public void displayErrorMessage(String message) throws IllegalArgumentException {
    this.adaptee.showErrorMessage(message);
  }

  /**
   * Converts the given provider's point coordinate to a tile coordinate.
   *
   * @param coord the given provider's point coordinate to convert
   * @param model an immutable Reversi model necessary for providing the game board side length
   *              needed for conversion calculations
   * @return the tile coordinate converted from the given provider's point coordinate
   * @throws IllegalArgumentException if the given provider's point coordinate is <code>null</code>
   *                                  OR the given model is <code>null</code>
   */
  private Point providerCoordToTileCoord(Point coord, ROReversiModel model)
          throws IllegalArgumentException {
    if (coord == null) {
      throw new IllegalArgumentException("Given coordinate cannot be null.");
    } else if (model == null) {
      throw new IllegalArgumentException("Given model cannot be null.");
    }

    int tileCoordY = model.getGameBoardSideLength() - 1 - coord.y;
    int tileCoordX = (int) (coord.x - tileCoordY - coord.y - Math.ceil(tileCoordY / 2.0));
    return new Point(tileCoordX, tileCoordY);
  }
}
