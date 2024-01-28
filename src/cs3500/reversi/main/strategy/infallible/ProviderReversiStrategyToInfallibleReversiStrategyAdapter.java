package cs3500.reversi.main.strategy.infallible;

import java.awt.Point;

import cs3500.reversi.main.model.MutableReversiModelToProviderReversiModelAdapter;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.strategy.Move;
import cs3500.reversi.provider.hexagonalreversistrategies.ReversiStrategy;

/**
 * An adapter that adapts a Reversi strategy to an infallible Reversi strategy. The "adaptee"
 * being adapted is an interface given by the providers representing a Reversi strategy that may
 * or may not be infallible. The "target" that the adaptee is being adapted to is an infallible
 * Reversi strategy.
 *
 * <p>Note: All class/interfaces given by the providers can be found in the provider package.</p>
 *
 * @see ReversiStrategy
 * @see InfallibleReversiStrategy
 */
public final class ProviderReversiStrategyToInfallibleReversiStrategyAdapter
        implements InfallibleReversiStrategy {
  private final ReversiStrategy adaptee;

  /**
   * Constructs a ProviderReversiStrategyToInfallibleReversiStrategyAdapter object that takes in a
   * Reversi strategy adaptee to adapt to the target.
   *
   * @param adaptee the Reversi strategy adaptee to adapt to the target
   * @throws IllegalArgumentException if the given strategy is <code>null</code>
   */
  public ProviderReversiStrategyToInfallibleReversiStrategyAdapter(ReversiStrategy adaptee)
          throws IllegalArgumentException {
    if (adaptee == null) {
      throw new IllegalArgumentException("Given strategy cannot be null.");
    }

    this.adaptee = adaptee;
  }

  @Override
  public Move chooseMove(ROReversiModel model)
          throws IllegalArgumentException, IllegalStateException {
    Point coord = this.adaptee.chooseMove(
            new MutableReversiModelToProviderReversiModelAdapter(model.copyGame()));
    return coord == null ? new Move(true) : new Move(providerCoordToTileCoord(coord, model));
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
