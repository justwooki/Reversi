import org.junit.Test;

import java.awt.Point;
import java.util.List;

import cs3500.reversi.main.model.AbstractReversi;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.SquareReversi;
import cs3500.reversi.main.view.textual.SquareReversiTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Testing class for testing publicly visible signatures of the square Reversi model. Testing will
 * be less detailed than {@link ModelInterfaceTest} since both square and hexagonal Reversi models
 * share the same code from their abstract class, {@link AbstractReversi}.
 *
 * @see SquareReversi
 */
public class SquareModelInterfaceTest {

  @Test
  public void ReversiConstructorThrows() {
    // Game board size must be less than 3
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(- 1));
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(0));
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(1));
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(2));

    // Game board size must always be even
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(3));
    assertThrows(IllegalArgumentException.class, () -> new SquareReversi(9));
  }

  @Test
  public void assertInitialBoardSize() {
    // size formula = n^2, where n is the side length of the game board
    // size 4 --> 16
    assertEquals(16, new SquareReversi(4).getBoard().size());
    // size 8 --> 64
    assertEquals(64, new SquareReversi(8).getBoard().size());
    // size 10 --> 100
    assertEquals(100, new SquareReversi(10).getBoard().size());
  }

  @Test
  public void getCornerTilesWork() {
    ROReversiModel model = new SquareReversi(8);
    List<Point> corners = model.getCornerTiles();

    assertEquals(4, corners.size());
    assertTrue(corners.contains(new Point(0, 0)));
    assertTrue(corners.contains(new Point(7, 0)));
    assertTrue(corners.contains(new Point(0, 7)));
    assertTrue(corners.contains(new Point(7, 7)));
  }

  @Test
  public void isAdjacentWorks() {
    SquareReversi model = new SquareReversi(8);

    // right adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(2, 1)));
    // left adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(0, 1)));
    // top adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(1, 0)));
    // bottom adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(1, 2)));
    // top-right adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(2, 0)));
    // bottom-left adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(0, 2)));
    // top-left adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(0, 0)));
    // bottom-right adjacency
    assertTrue(model.isAdjacent(new Point(1, 1), new Point(2, 2)));
    // non-adjacencies
    assertFalse(model.isAdjacent(new Point(1, 1), new Point(3, 0)));
    assertFalse(model.isAdjacent(new Point(1, 1), new Point(1, 3)));
    assertFalse(model.isAdjacent(new Point(1, 1), new Point(3, 3)));
  }

  @Test
  public void textualTestViewWorks() {
    MutableReversiModel model = new SquareReversi(6);
    SquareReversiTextualView view = new SquareReversiTextualView(model);

    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ X O _ _\n"
                    + "_ _ O X _ _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(4, 2)); // black move
    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ X X X _\n"
                    + "_ _ O X _ _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(4, 1)); // white move
    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ _ _ O _\n"
                    + "_ _ X O X _\n"
                    + "_ _ O X _ _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.passTurn(); // black move
    model.placeDisc(new Point(4, 3)); // white move
    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ _ _ O _\n"
                    + "_ _ X O O _\n"
                    + "_ _ O O O _\n"
                    + "_ _ _ _ _ _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(4, 4)); // black move
    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ _ _ O _\n"
                    + "_ _ X O O _\n"
                    + "_ _ O X O _\n"
                    + "_ _ _ _ X _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(2, 1)); // white move
    assertEquals(
            "_ _ _ _ _ _\n"
                    + "_ _ O _ O _\n"
                    + "_ _ O O O _\n"
                    + "_ _ O X O _\n"
                    + "_ _ _ _ X _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(4, 0)); // black move
    assertEquals(
            "_ _ _ _ X _\n"
                    + "_ _ O _ X _\n"
                    + "_ _ O O X _\n"
                    + "_ _ O X X _\n"
                    + "_ _ _ _ X _\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(5, 4)); // white move
    assertEquals(
            "_ _ _ _ X _\n"
                    + "_ _ O _ X _\n"
                    + "_ _ O O X _\n"
                    + "_ _ O X O _\n"
                    + "_ _ _ _ X O\n"
                    + "_ _ _ _ _ _",
            view.toString());

    model.placeDisc(new Point(1, 4)); // black move
    assertEquals(
            "_ _ _ _ X _\n"
                    + "_ _ O _ X _\n"
                    + "_ _ O X X _\n"
                    + "_ _ X X O _\n"
                    + "_ X _ _ X O\n"
                    + "_ _ _ _ _ _",
            view.toString());
  }
}
