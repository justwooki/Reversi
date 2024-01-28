package cs3500.reversi.main.model;

import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Testing class for testing package visible functionality in {@link SquareReversi} that's not part
 * of the Reversi model interface.
 */
public class SquareImplementationTest {

  @Test
  public void createGameBoardWorks() {
    // Setup
    SquareReversi model = new SquareReversi(4);
    Map<Point, Disc> actualBoard = model.createGameBoard(4);
    List<Point> expectedKeys = new ArrayList<>();

    expectedKeys.add(new Point(0, 0));
    expectedKeys.add(new Point(0, 1));
    expectedKeys.add(new Point(0, 2));
    expectedKeys.add(new Point(0, 3));

    expectedKeys.add(new Point(1, 0));
    expectedKeys.add(new Point(1, 1));
    expectedKeys.add(new Point(1, 2));
    expectedKeys.add(new Point(1, 3));

    expectedKeys.add(new Point(2, 0));
    expectedKeys.add(new Point(2, 1));
    expectedKeys.add(new Point(2, 2));
    expectedKeys.add(new Point(2, 3));

    expectedKeys.add(new Point(3, 0));
    expectedKeys.add(new Point(3, 1));
    expectedKeys.add(new Point(3, 2));
    expectedKeys.add(new Point(3, 3));

    // Actual test
    assertEquals(expectedKeys.size(), actualBoard.size());

    for (Point tile : expectedKeys) {
      assertNotNull(actualBoard.get(tile));
      assertFalse(actualBoard.get(tile).hasDisc());
    }
  }

  @Test
  public void initiateInitialDiscsWorks() {
    // Setup
    SquareReversi model = new SquareReversi(8);
    Map<Point, Disc> actualBoard =
            model.addInitialDiscs(model.createGameBoard(8));

    // Actual test
    assertEquals(actualBoard.get(new Point(3, 3)).getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(actualBoard.get(new Point(4, 4)).getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(actualBoard.get(new Point(4, 3)).getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(actualBoard.get(new Point(3, 4)).getDiscColor(), Disc.DiscColor.WHITE);
  }
}
