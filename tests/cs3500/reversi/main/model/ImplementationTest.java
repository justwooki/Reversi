package cs3500.reversi.main.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Testing class for testing package visible functionality in {@link BasicReversi} that's not part
 * of the Reversi model interface.
 */
public class ImplementationTest {

  // Test if every tile that needs to be added has been added
  @Test
  public void createGameBoardWorks() {
    // Setup
    BasicReversi model = new BasicReversi(5);
    Map<Point, Disc> actualBoard = model.createGameBoard(5);
    List<Point> expectedKeys = new ArrayList<>();

    expectedKeys.add(new Point(-4, 4));
    expectedKeys.add(new Point(-3, 4));
    expectedKeys.add(new Point(-2, 4));
    expectedKeys.add(new Point(-1, 4));
    expectedKeys.add(new Point(0, 4));

    expectedKeys.add(new Point(-4, 3));
    expectedKeys.add(new Point(-3, 3));
    expectedKeys.add(new Point(-2, 3));
    expectedKeys.add(new Point(-1, 3));
    expectedKeys.add(new Point(0, 3));
    expectedKeys.add(new Point(1, 3));

    expectedKeys.add(new Point(-4, 2));
    expectedKeys.add(new Point(-3, 2));
    expectedKeys.add(new Point(-2, 2));
    expectedKeys.add(new Point(-1, 2));
    expectedKeys.add(new Point(0, 2));
    expectedKeys.add(new Point(1, 2));
    expectedKeys.add(new Point(2, 2));

    expectedKeys.add(new Point(-4, 1));
    expectedKeys.add(new Point(-3, 1));
    expectedKeys.add(new Point(-2, 1));
    expectedKeys.add(new Point(-1, 1));
    expectedKeys.add(new Point(0, 1));
    expectedKeys.add(new Point(1, 1));
    expectedKeys.add(new Point(2, 1));
    expectedKeys.add(new Point(3, 1));

    expectedKeys.add(new Point(-4, 0));
    expectedKeys.add(new Point(-3, 0));
    expectedKeys.add(new Point(-2, 0));
    expectedKeys.add(new Point(-1, 0));
    expectedKeys.add(new Point(0, 0));
    expectedKeys.add(new Point(1, 0));
    expectedKeys.add(new Point(2, 0));
    expectedKeys.add(new Point(3, 0));
    expectedKeys.add(new Point(4, 0));

    expectedKeys.add(new Point(-3, -1));
    expectedKeys.add(new Point(-2,  1));
    expectedKeys.add(new Point(-1,  1));
    expectedKeys.add(new Point(0, -1));
    expectedKeys.add(new Point(1, -1));
    expectedKeys.add(new Point(2, -1));
    expectedKeys.add(new Point(3, -1));
    expectedKeys.add(new Point(4, -1));

    expectedKeys.add(new Point(-2, - 2));
    expectedKeys.add(new Point(-1, -2));
    expectedKeys.add(new Point(0, -2));
    expectedKeys.add(new Point(1, -2));
    expectedKeys.add(new Point(2, -2));
    expectedKeys.add(new Point(3, -2));
    expectedKeys.add(new Point(4, -2));

    expectedKeys.add(new Point(-1, -3));
    expectedKeys.add(new Point(0, -3));
    expectedKeys.add(new Point(1, -3));
    expectedKeys.add(new Point(2, -3));
    expectedKeys.add(new Point(3, -3));
    expectedKeys.add(new Point(4, -3));
    expectedKeys.add(new Point(0, - 4));
    expectedKeys.add(new Point(1, -4));
    expectedKeys.add(new Point(2, -4));
    expectedKeys.add(new Point(3, -4));
    expectedKeys.add(new Point(4, -4));

    // Actual test
    for (Point tile : expectedKeys) {
      assertNotNull(actualBoard.get(tile));
      assertFalse(actualBoard.get(tile).hasDisc());
    }
  }

  // Test if every initial black and white disc that needs to be initiated is initiated
  @Test
  public void initiateInitialDiscsWorks() {
    // Setup
    BasicReversi model = new BasicReversi(5);
    Map<Point, Disc> actualBoard =
        model.addInitialDiscs(model.createGameBoard(5));

    // Actual test
    assertEquals(actualBoard.get(new Point(-1, 1)).getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(actualBoard.get(new Point(1, 0)).getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(actualBoard.get(new Point(0, -1)).getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(actualBoard.get(new Point(0, 1)).getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(actualBoard.get(new Point(1, -1)).getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(actualBoard.get(new Point(-1, 0)).getDiscColor(), Disc.DiscColor.WHITE);
  }
}
