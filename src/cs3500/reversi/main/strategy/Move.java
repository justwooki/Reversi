package cs3500.reversi.main.strategy;

import java.awt.Point;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a Reversi move to be made. Every move consists of a tile on which to make a move
 * on and a boolean representing whether to pass the current turn. Because this class should
 * logically represent only one single move, if a tile is selected to make a move on, the current
 * turn should not be set to pass. Conversely, if the current turn is set to pass, there should be
 * no tile selected at all.
 */
public final class Move {
  private final Optional<Point> tile;
  private final boolean pass;

  /**
   * Constructs a Move object that consists of a tile on which to make a move on.
   *
   * @param tile the tile on which to make a move on
   */
  public Move(Point tile) {
    this.tile = Optional.of(tile);
    this.pass = false;
  }

  /**
   * Constructs a Move object that consists of a boolean representing whether to pass the
   * current turn.
   *
   * @param pass should the current turn be passed or not
   */
  public Move(boolean pass) {
    this.tile = Optional.empty();
    this.pass = pass;
  }

  /**
   * Gets the tile on which to make a move on.
   *
   * @return the tile on which to make a move on
   */
  public Optional<Point> getTile() {
    return this.tile;
  }

  /**
   * Gets whether current turn is to be passed or not.
   *
   * @return <code>true</code> if the current turn is to be passed and <code>false</code> otherwise
   */
  public boolean getPass() {
    return this.pass;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move)) {
      return false;
    }

    Move otherMove = (Move) o;
    return this.tile.equals(otherMove.tile) && this.pass == otherMove.pass;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tile, this.pass);
  }
}
