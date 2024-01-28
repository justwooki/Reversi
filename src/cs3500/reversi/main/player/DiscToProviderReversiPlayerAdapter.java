package cs3500.reversi.main.player;

import java.awt.Point;
import java.util.function.Consumer;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.provider.player.ReversiPlayer;

/**
 * An adapter that adapts a disc representation to a player representation. The "adaptee" being
 * adapted is a disc. The "target" that the adaptee is being adapted to is an interface given by
 * the providers representing a Reversi player.
 *
 * <p>Note: All class/interfaces given by the providers can be found in the provider package.</p>
 *
 * @see Disc
 * @see ReversiPlayer
 */
public final class DiscToProviderReversiPlayerAdapter implements ReversiPlayer {
  private final Disc adaptee;

  /**
   * Constructs a DiscToProviderReversiPlayerAdapter object that takes in a disc adaptee to adapt
   * to the target.
   *
   * @param adaptee the disc adaptee to adapt to the target
   * @throws IllegalArgumentException if the given disc is <code>null</code>
   */
  public DiscToProviderReversiPlayerAdapter(Disc adaptee) throws IllegalArgumentException {
    if (adaptee == null) {
      throw new IllegalArgumentException("Given disc cannot be null.");
    }

    this.adaptee = adaptee;
  }

  @Override
  public cs3500.reversi.provider.board.Disc getDiscColor() {
    return discToProviderDisc(this.adaptee);
  }

  @Override
  public void selectCellAndPlaceDisc(Point cell) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void indicatePass() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void makeMove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ReversiPlayer getCopyOfPlayer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCommandCallback(Consumer<Point> callback) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateView() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setViewUpdateCallback(Consumer<String> callback) {
    throw new UnsupportedOperationException();
  }

  /**
   * Converts the given disc to the provider's disc. If the given disc is neither black nor white,
   * it will be converted to a provider's disc that is either empty or nonexistent. This helper
   * method converts the disc color of a player. Since a player should always be assigned some
   * disc color, whether the disc returned is empty or nonexistent doesn't really matter since
   * they both should be treated the same in this case.
   *
   * @param disc the given disc to convert
   * @return the provider's disc converted from the given disc
   * @throws IllegalArgumentException if the given disc is <code>null</code>
   */
  private cs3500.reversi.provider.board.Disc discToProviderDisc(Disc disc)
          throws IllegalArgumentException {
    if (disc == null) {
      throw new IllegalArgumentException("Given disc cannot be null.");
    } else if (disc.getDiscColor() == Disc.DiscColor.BLACK) {
      return cs3500.reversi.provider.board.Disc.BLACK;
    } else if (disc.getDiscColor() == Disc.DiscColor.WHITE) {
      return cs3500.reversi.provider.board.Disc.WHITE;
    } else {
      // Doesn't really matter whether EMPTY or NONEXISTENT is returned here since a player should
      // be assigned some color
      return cs3500.reversi.provider.board.Disc.NONEXISTENT;
    }
  }
}
