package cs3500.reversi.main.model;

/**
 * Represents disc game pieces used in Reversi that the player will use to place on hexagonal
 * tiles. Each disc is defined by its color. A disc may or may not "exist" depending on whether it
 * is assigned a color or not. Until a disc is assigned a color, it is considered "nonexistent".
 * Each color of a disc represents a specific player for the duration of the game.
 */
public final class Disc {

  /**
   * A disc can be either be black or white, or if the disc "doesn't exist", it cannot have color.
   */
  public enum DiscColor {
    NONE, BLACK, WHITE
  }

  private DiscColor color;

  /**
   * Constructs a Disc object that doesn't initially physically exist yet.
   */
  public Disc() {
    this.color = DiscColor.NONE;
  }

  /**
   * Constructs a Disc object with a given color.
   *
   * @param color the color of the disc
   */
  public Disc(DiscColor color) {
    this.color = color;
  }

  /**
   * Checks if <code>this</code> disc exists, that is, its {@link DiscColor} is an actual color.
   *
   * @return <code>true</code>> if the disc exists or <code>false</code> otherwise
   */
  public boolean hasDisc() {
    return this.color != DiscColor.NONE;
  }

  /**
   * Gets the {@link DiscColor} of <code>this</code> disc.
   *
   * @return the disc color
   */
  public DiscColor getDiscColor() {
    return this.color;
  }

  /**
   * Gets the opposite disc of <code>this</code> disc. The opposite of a disc is simply a disc with
   * the opposite disc color.
   *
   * @return a new disc with the opposite color of <code>this</code>> disc
   * @throws IllegalStateException if the disc doesn't exist
   */
  public Disc getOppositeDisc() throws IllegalStateException {
    if (!hasDisc()) {
      throw new IllegalStateException("Disc does not exist");
    } else if (getDiscColor() == DiscColor.BLACK) {
      return new Disc(DiscColor.WHITE);
    } else {
      return new Disc(DiscColor.BLACK);
    }
  }

  /**
   * Set <code>this</code> nonexistent disc to exist via assigning it a color.
   *
   * @param newColor the color to set <code>this</code>> nonexistent disc
   * @throws IllegalStateException if <code>this</code> disc already exists
   */
  public void makeDiscExisting(DiscColor newColor) throws IllegalStateException {
    if (!hasDisc()) {
      this.color = newColor;
    } else {
      throw new IllegalStateException("Disc already exists");
    }
  }

  /**
   * Flips <code>this</code> existing disc to its opposite color.
   *
   * @throws IllegalStateException if the disc doesn't exist
   */
  public void flipDiscColor() throws IllegalStateException {
    this.color = getOppositeDisc().color;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Disc)) {
      return false;
    }
    return this.color == ((Disc) o).getDiscColor();
  }

  @Override
  public int hashCode() {
    return this.color.hashCode();
  }
}
