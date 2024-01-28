import org.junit.Test;

import java.awt.Point;
import java.util.Map;

import cs3500.reversi.main.model.BasicReversi;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.view.textual.HexagonalReversiTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Testing class for testing publicly visible signatures of the hexagonal Reversi model.
 *
 * @see BasicReversi
 */
public class ModelInterfaceTest {

  // Constructor tests
  @Test
  public void ReversiConstructorThrows() {
    // Game board size cannot be negative, zero, or one
    assertThrows(IllegalArgumentException.class, () -> new BasicReversi(- 1));
    assertThrows(IllegalArgumentException.class, () -> new BasicReversi(0));
    assertThrows(IllegalArgumentException.class, () -> new BasicReversi(1));
  }

  // Asserts game board size is equal to the number of tiles in the board.
  @Test
  public void assertInitialBoardSize() {
    // size formula = 3(n)(n-1) + 1, where n is the side length of the game board
    // size 2 --> 7
    assertEquals(7, new BasicReversi(2).getBoard().size());
    // size 3 --> 19
    assertEquals(19, new BasicReversi(3).getBoard().size());
    // size 10 -->
    assertEquals(271, new BasicReversi(10).getBoard().size());
  }

  @Test
  public void passTurnDoesNotChangeBoard() {
    // Setup
    BasicReversi model = new BasicReversi(3);
    model.placeDisc(new Point(2, - 1)); // black move
    model.placeDisc(new Point(- 2, 1)); // white move
    model.placeDisc(new Point(- 1, 2)); // black move
    model.placeDisc(new Point(1, 1)); // white move

    // test passTurn
    Map<Point, Disc> before = model.getBoard();
    model.passTurn();
    Map<Point, Disc> after = model.getBoard();
    assertEquals(before, after);
  }

  @Test
  public void testPassTurnSwapsTurn() {
    BasicReversi model = new BasicReversi(3);
    assertEquals(Disc.DiscColor.BLACK, model.getTurn().getDiscColor());
    model.passTurn();
    assertEquals(Disc.DiscColor.WHITE, model.getTurn().getDiscColor());
    model.passTurn();
    assertEquals(Disc.DiscColor.BLACK, model.getTurn().getDiscColor());
  }

  @Test
  public void cannotMakeMoveAfterGameOver() {
    BasicReversi model = new BasicReversi(3);
    model.passTurn();
    model.passTurn();
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(2, - 1)));
    assertThrows(IllegalStateException.class, () -> model.passTurn());
  }

  @Test
  public void getTurnWorks() {
    BasicReversi model = new BasicReversi(3);
    assertEquals(Disc.DiscColor.BLACK, model.getTurn().getDiscColor());
    model.placeDisc(new Point(2, - 1));
    assertEquals(Disc.DiscColor.WHITE, model.getTurn().getDiscColor());
    model.passTurn();
    assertEquals(Disc.DiscColor.BLACK, model.getTurn().getDiscColor());
    model.passTurn();
    assertEquals(Disc.DiscColor.WHITE, model.getTurn().getDiscColor());
  }

  @Test
  public void getDiscWorks() {
    BasicReversi model = new BasicReversi(3);
    assertEquals(Disc.DiscColor.BLACK, model.getDisc(new Point(- 1, 1)).getDiscColor());
    assertEquals(Disc.DiscColor.WHITE, model.getDisc(new Point(1, - 1)).getDiscColor());
    assertEquals(Disc.DiscColor.NONE, model.getDisc(new Point(2, - 1)).getDiscColor());

    // test getDisc after making move
    model.placeDisc(new Point(2, - 1));
    assertEquals(Disc.DiscColor.BLACK, model.getDisc(new Point(2, - 1)).getDiscColor());
    assertEquals(Disc.DiscColor.BLACK, model.getDisc(new Point(1, - 1)).getDiscColor());
  }

  @Test
  public void getDiscThrows() {
    BasicReversi model = new BasicReversi(3);
    assertThrows(IllegalArgumentException.class, () -> model.getDisc(new Point(2, 2)));
    assertThrows(IllegalArgumentException.class, () -> model.getDisc(new Point(3, 3)));
  }

  @Test
  public void getWinnerThrows() {
    BasicReversi model = new BasicReversi(3);
    assertThrows(IllegalStateException.class, () -> model.getWinner());
    model.placeDisc(new Point(2, - 1));
    assertThrows(IllegalStateException.class, () -> model.getWinner());
    model.passTurn();
    assertThrows(IllegalStateException.class, () -> model.getWinner());
  }

  @Test
  public void getWinnerTie() {
    BasicReversi model = new BasicReversi(3);
    model.passTurn();
    model.passTurn();
    assertEquals(Disc.DiscColor.NONE, model.getWinner().getDiscColor());
  }

  @Test
  public void isAdjacentWorks() {
    BasicReversi model = new BasicReversi(5);

    // right adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(2, 0)));
    // left adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(0, 0)));
    // top-right adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(1, 1)));
    // bottom-left adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(1, -1)));
    // top-left adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(0, 1)));
    // bottom-right adjacency
    assertTrue(model.isAdjacent(new Point(1, 0), new Point(2, -1)));
    // non-adjacencies
    assertFalse(model.isAdjacent(new Point(1, 0), new Point(1, 2)));
    assertFalse(model.isAdjacent(new Point(1, 0), new Point(2, 1)));
    assertFalse(model.isAdjacent(new Point(1, 0), new Point(2, -2)));
  }

  @Test
  public void textualTestViewWorks() {
    ROReversiModel model = new BasicReversi(6);
    HexagonalReversiTextualView view = new HexagonalReversiTextualView(model);
    assertEquals(
        "     _ _ _ _ _ _\n"
        + "    _ _ _ _ _ _ _\n"
        + "   _ _ _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _ _ _\n"
        + " _ _ _ _ X O _ _ _ _\n"
        + "_ _ _ _ O _ X _ _ _ _\n"
        + " _ _ _ _ X O _ _ _ _\n"
        + "  _ _ _ _ _ _ _ _ _\n"
        + "   _ _ _ _ _ _ _ _\n"
        + "    _ _ _ _ _ _ _\n"
        + "     _ _ _ _ _ _",
            view.toString());
  }


  @Test
  public void smallFullGamePlayed() {
    BasicReversi model = new BasicReversi(3);
    HexagonalReversiTextualView view = new HexagonalReversiTextualView(model);
    assertEquals(
        "  _ _ _\n"
        + " _ X O _\n"
        + "_ O _ X _\n"
        + " _ X O _\n"
        + "  _ _ _",
            view.toString()
    );

    System.out.println(view.toString());
    model.placeDisc(new Point(2, - 1)); // black move
    assertEquals(
        "  _ _ _\n"
        + " _ X O _\n"
        + "_ O _ X _\n"
        + " _ X X X\n"
        + "  _ _ _",
            view.toString()
    );
    model.placeDisc(new Point(- 2, 1)); // white move
    assertEquals(
        "  _ _ _\n"
        + " O O O _\n"
        + "_ O _ X _\n"
        + " _ X X X\n"
        + "  _ _ _",
            view.toString()
    );
    model.placeDisc(new Point(- 1, 2)); // black move
    assertEquals(
        "  _ X _\n"
        + " O O X _\n"
        + "_ O _ X _\n"
        + " _ X X X\n"
        + "  _ _ _",
            view.toString()
    );
    model.placeDisc(new Point(1, 1)); // white move
    assertEquals(
        "  _ X _\n"
        + " O O O O\n"
        + "_ O _ X _\n"
        + " _ X X X\n"
        + "  _ _ _",
            view.toString()
    );
    model.placeDisc(new Point(- 1, - 1)); // black move
    assertEquals(
        "  _ X _\n"
        + " O X O O\n"
        + "_ X _ X _\n"
        + " X X X X\n"
        + "  _ _ _",
            view.toString()
    );
    model.placeDisc(new Point(1, - 2)); // white move --> tests "double move
    assertEquals(
        "  _ X _\n"
        + " O X O O\n"
        + "_ O _ O _\n"
        + " X O O X\n"
        + "  _ O _",
            view.toString()
    );

    assertEquals(4, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(8, model.getScore(new Disc(Disc.DiscColor.WHITE)));
    assertEquals(Disc.DiscColor.WHITE, model.getWinner().getDiscColor());
  }

  // full integrative tests with multiple methods
  // tests getScore, getWinner, & gameOver
  @Test
  public void largeFullGamePlayed() {
    BasicReversi model = new BasicReversi(5);
    HexagonalReversiTextualView view = new HexagonalReversiTextualView(model);

    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ O _ X _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(3, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(3, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(2, - 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ O _ X _ _ _\n"
        + " _ _ _ X X X _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(5, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(2, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(3, - 2));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ O _ O _ _ _\n"
        + " _ _ _ X X O _ _\n"
        + "  _ _ _ _ _ O _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(3, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(5, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(- 1, - 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " _ _ X X X O _ _\n"
        + "  _ _ _ _ _ O _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(5, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(4, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(- 2, - 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " _ O O O O O _ _\n"
        + "  _ _ _ _ _ O _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(2, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(8, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(1, - 2));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " _ O O X O O _ _\n"
        + "  _ _ _ X _ O _\n"
        + "   _ _ _ _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(4, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(7, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(1, - 3));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " _ O O X O O _ _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(3, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(- 3, - 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ _ X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X X O O _ _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(6, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(7, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(- 2, 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ _ _ _ _\n"
        + " _ _ O O O _ _ _\n"
        + "_ _ _ O _ O _ _ _\n"
        + " X X X O O O _ _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(3, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(11, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(- 1, 2));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X O O O _ _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(6, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.passTurn();
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X O O O _ _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(6, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(3, - 1));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ _ _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X X X X X _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(10, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(6, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(- 2, 3));
    assertEquals(
        "    _ _ _ _ _\n"
        + "   _ _ O _ _ _\n"
        + "  _ _ _ O _ _ _\n"
        + " _ _ O X O _ _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X X X X X _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(8, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(- 3, 4));
    assertEquals(
        "    _ X _ _ _\n"
        + "   _ _ X _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O X X _ _ _\n"
        + "_ _ _ X _ X _ _ _\n"
        + " X X X X X X X _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(14, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(4, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(1, 1));
    assertEquals(
        "    _ X _ _ _\n"
        + "   _ _ X _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O O O O _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X X O X X _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(10, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.passTurn();
    assertEquals(
        "    _ X _ _ _\n"
        + "   _ _ X _ _ _\n"
        + "  _ _ _ X _ _ _\n"
        + " _ _ O O O O _ _\n"
        + "_ _ _ X _ O _ _ _\n"
        + " X X X X O X X _\n"
        + "  _ _ _ O _ O _\n"
        + "   _ _ O _ _ _\n"
        + "    _ _ _ _ _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(10, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(9, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(4, - 1));
    // black makes move
    model.placeDisc(new Point(4, - 3));
    // white makes move
    model.placeDisc(new Point(3, - 3));
    // black makes move
    model.placeDisc(new Point(2, - 3));
    // white makes move
    model.placeDisc(new Point(3, - 4));
    // black makes move
    model.placeDisc(new Point(0, - 3));
    // white makes move
    model.placeDisc(new Point(1, - 4));
    // black makes move
    model.passTurn();
    // white makes move
    model.placeDisc(new Point(- 1, - 2));
    // black makes move
    model.placeDisc(new Point(- 3, 2));
    // white makes move
    model.placeDisc(new Point(- 4, 3));
    assertEquals(
        "    _ X _ _ _\n"
        + "   O _ X _ _ _\n"
        + "  _ O _ X _ _ _\n"
        + " _ _ O O X O _ _\n"
        + "_ _ _ O _ X _ _ _\n"
        + " X X O O O X O O\n"
        + "  _ O _ O _ O _\n"
        + "   _ O O O X X\n"
        + "    _ O _ O _",
        view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.BLACK);
    assertEquals(10, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(19, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // black makes move
    model.placeDisc(new Point(- 1, - 3));
    // white makes move
    model.placeDisc(new Point(- 3, 0));
    // black makes move
    model.placeDisc(new Point(3, 0));
    // white makes move
    model.placeDisc(new Point(- 1, 3));
    // black makes move
    model.placeDisc(new Point(- 1, 4));
    // white makes move
    model.placeDisc(new Point(3, 1));
    // black makes move
    model.placeDisc(new Point(- 3, 1));
    // white makes move
    model.placeDisc(new Point(- 4, 1));
    // black makes move
    model.placeDisc(new Point(- 3, 3));
    assertEquals(
        "    _ X _ X _\n"
        + "   O X X X _ _\n"
        + "  _ X _ X _ _ _\n"
        + " O X O O O O _ O\n"
        + "_ X _ X _ X _ O _\n"
        + " X O X O O X O O\n"
        + "  _ X _ O _ O _\n"
        + "   X O X X O X\n"
        + "    _ O _ O _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(19, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(19, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // white makes move
    model.placeDisc(new Point(0, 3));
    // black makes move
    model.placeDisc(new Point(2, 1));
    // white makes move
    model.placeDisc(new Point(1, 2));
    // black makes move
    model.placeDisc(new Point(1, 3));
    assertEquals(
        "    _ X _ X _\n"
        + "   O O O O O X\n"
        + "  _ X _ X _ X _\n"
        + " O X X X X X O O\n"
        + "_ X _ X _ X _ O _\n"
        + " X O X O X X O O\n"
        + "  _ X _ X _ O _\n"
        + "   X O X X O X\n"
        + "    _ O _ O _",
            view.toString()
    );

    // test current turn and scores for both players
    assertEquals(model.getTurn().getDiscColor(), Disc.DiscColor.WHITE);
    assertEquals(24, model.getScore(new Disc(Disc.DiscColor.BLACK)));
    assertEquals(18, model.getScore(new Disc(Disc.DiscColor.WHITE)));

    // test if white and black can move
    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.WHITE)));
    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.BLACK)));
    assertTrue(model.gameOver());

    // test winner
    assertEquals(Disc.DiscColor.BLACK, model.getWinner().getDiscColor());
  }
}