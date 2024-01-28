import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.awt.Point;

import cs3500.reversi.main.model.BasicReversi;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;

/**
 * Testing class to give readers a quick understanding of the Reversi model.
 *
 * @see BasicReversi
 */
public class ExamplesTest {

  @Test
  public void placeDiscOnUnconnectedTile() {
    MutableReversiModel model = new BasicReversi(5);
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(- 3, 3)));
    // bordering corner
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, 4)));
  }

  @Test
  public void placeDiscOnNonEmptyTile() {
    MutableReversiModel model = new BasicReversi(3);
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, 1)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(- 1, 0)));
  }

  @Test
  public void placeDiscOutOfBounds() {
    MutableReversiModel model = new BasicReversi(3);
    assertThrows(IllegalArgumentException.class, () -> model.placeDisc(new Point(3, 3)));
  }

  @Test
  public void placeDiscOnNonStraightLineFormingTile() {
    MutableReversiModel model = new BasicReversi(3);
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(2, 0)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, 0)));
  }

  @Test
  public void placeDiscBorderingOnlySameColors() {
    MutableReversiModel model = new BasicReversi(3);
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(- 2, 0)));
  }

  // Testing Legal Moves through a full game
  // full integrative tests with multiple methods
  @Test
  public void fullGamePlayed() {
    BasicReversi model = new BasicReversi(3);
    model.placeDisc(new Point(2, -1)); // black move
    model.placeDisc(new Point(-2, 1)); // white move
    model.placeDisc(new Point(-1, 2)); // black move
    model.placeDisc(new Point(1, 1)); // white move
    model.placeDisc(new Point(-1, -1)); // black move
    model.placeDisc(new Point(1, -2)); // white move --> tests "double move

    // Illegal Moves --> no moves left
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(-2, 2)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, 2)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, 0)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(-2, 0)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(0, -2)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(2, -2)));
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(2, 0)));

    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.BLACK)));
    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.WHITE)));

    assertTrue(model.gameOver());
  }

  @Test
  public void doublePassGameOver() {
    BasicReversi model = new BasicReversi(3);
    model.passTurn();
    model.passTurn();

    assertTrue(model.gameOver());
  }
}
