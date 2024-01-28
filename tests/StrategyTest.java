import org.junit.Test;

import java.awt.Point;
import java.util.Optional;

import cs3500.reversi.main.model.BasicReversi;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.strategy.infallible.AvoidCornerAdjacencyMaxScore;
import cs3500.reversi.main.strategy.fallible.AvoidTilesBorderingCorners;
import cs3500.reversi.main.strategy.infallible.CaptureMostPieces;
import cs3500.reversi.main.strategy.fallible.CherryPicker;
import cs3500.reversi.main.strategy.infallible.InfallibleReversiStrategy;
import cs3500.reversi.main.strategy.Move;
import cs3500.reversi.main.strategy.fallible.PlayAtCorners;
import cs3500.reversi.main.strategy.infallible.PlayCornersMaxScore;
import cs3500.reversi.main.strategy.fallible.FallibleReversiStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing class for testing the behaviors of the strategies in hexagonal Reversi.
 *
 * @see BasicReversi
 */
public class StrategyTest {

  @Test
  public void playAtCornersChecksAllCorners() {
    StringBuilder log = new StringBuilder();
    MutableReversiModel mockModel
            = new ReversiMockTranscript(log, new BasicReversi(5));
    FallibleReversiStrategy strategy = new PlayAtCorners();
    strategy.chooseMove(mockModel);

    StringBuilder expected = new StringBuilder().append("Place disc at (-4, 4)\n")
            .append("Place disc at (0, 4)\n").append("Place disc at (-4, 0)\n")
            .append("Place disc at (4, 0)\n").append("Place disc at (0, -4)\n")
            .append("Place disc at (4, -4)\n");

    assertEquals(expected.toString(), log.toString());

    log = new StringBuilder();
    mockModel = new ReversiMockTranscript(log, new BasicReversi(3));
    strategy.chooseMove(mockModel);


    expected = new StringBuilder().append("Place disc at (-2, 2)\n")
            .append("Place disc at (0, 2)\n").append("Place disc at (-2, 0)\n")
            .append("Place disc at (2, 0)\n").append("Place disc at (0, -2)\n")
            .append("Place disc at (2, -2)\n");

    assertEquals(expected.toString(), log.toString());
  }

  @Test
  public void captureMostPiecesChecksAllTiles() {
    StringBuilder log = new StringBuilder();
    MutableReversiModel mockModel
            = new ReversiMockTranscript(log, new BasicReversi(5));
    InfallibleReversiStrategy strategy = new CaptureMostPieces();
    strategy.chooseMove(mockModel);

    // number of total tiles on the game board = 3(n)(n-1) + 1
    // where n is the game board side length
    assertEquals(61, log.toString().split("\n").length);

    log = new StringBuilder();
    mockModel = new ReversiMockTranscript(log, new BasicReversi(3));
    strategy.chooseMove(mockModel);

    assertEquals(19, log.toString().split("\n").length);
  }

  @Test
  public void avoidTilesBorderingCornersChecksAllTiles() {
    StringBuilder log = new StringBuilder();
    MutableReversiModel mockModel
            = new ReversiMockTranscript(log, new BasicReversi(5));
    FallibleReversiStrategy strategy = new AvoidTilesBorderingCorners();
    strategy.chooseMove(mockModel);

    // number of total hexagonal tiles on the game board = 3(n)(n-1) + 1
    // where n is the game board side length
    // subtract 6 * 3 because there are 6 corners in a hexagon each with 3 bordering hexagons
    assertEquals(43, log.toString().split("\n").length);

    log = new StringBuilder();
    mockModel = new ReversiMockTranscript(log, new BasicReversi(3));
    strategy.chooseMove(mockModel);

    // subtract 6 * 3 - 6 because overlap
    assertEquals(7, log.toString().split("\n").length);
  }

  @Test
  public void legalMovesReturned() {
    ReversiMockMoves mockModel = new ReversiMockMoves(new BasicReversi(5),
            new Point(0, 0));

    // test CaptureMostPieces strategy
    InfallibleReversiStrategy strategy1 = new CaptureMostPieces();

    // illegal move will result in passing
    assertEquals(new Move(true), strategy1.chooseMove(mockModel));

    // legal move is played
    mockModel.setTarget(new Point(2, -1));

    assertEquals(new Point(2, -1),
            strategy1.chooseMove(mockModel).getTile().get());

    // test AvoidTilesBorderingCorners strategy
    FallibleReversiStrategy strategy2 = new AvoidTilesBorderingCorners();

    // illegal move doesn't return anything
    mockModel.setTarget(new Point(0, 0));
    assertEquals(Optional.empty(), strategy2.chooseMove(mockModel));

    // legal move is played
    mockModel.setTarget(new Point(2, -1));
    assertEquals(new Point(2, -1),
            strategy2.chooseMove(mockModel).flatMap(Move::getTile).get());
  }

  @Test
  public void captureMostPiecesOptimizesScore() {
    ReversiMockScore mockModel = new ReversiMockScore(new Point(0, 0),
            new BasicReversi(5));
    InfallibleReversiStrategy strategy = new CaptureMostPieces();

    // doesn't return illegal move even if it gives the highest score
    assertNotEquals(new Point(0, 0), strategy.chooseMove(mockModel).getTile().get());

    // returns move offering the highest score
    mockModel.setTarget(new Point(2, -1));
    assertEquals(new Point(2, -1),
            strategy.chooseMove(mockModel).getTile().get());
  }

  @Test
  public void avoidTilesBorderingCornersOptimizesCornerAdjacency() {
    ReversiMockScore mockModel = new ReversiMockScore(new Point(0, 0),
            new BasicReversi(5));
    FallibleReversiStrategy strategy = new AvoidTilesBorderingCorners();

    // doesn't return illegal move even if it gives the highest score
    assertNotEquals(new Point(0, 0),
            strategy.chooseMove(mockModel).flatMap(Move::getTile).get());

    // returns move offering the highest score given that it isn't adjacent to a corner
    mockModel.setTarget(new Point(2, -1));
    assertEquals(new Point(2, -1),
            strategy.chooseMove(mockModel).flatMap(Move::getTile).get());

    // doesn't return the move offering the highest score if it's adjacent to a corner
    // (in this case, it won't return anything at all since all valid moves are next to a corner)
    mockModel = new ReversiMockScore(new Point(2, -1),
            new BasicReversi(3));
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));
  }

  @Test
  public void cherryPickerPasses() {
    ReversiMockGameInfo mockModel = new ReversiMockGameInfo(new BasicReversi(3));
    FallibleReversiStrategy strategy = new CherryPicker();

    // returns nothing if passing doesn't end the game
    // test same score, lower score, and greater score
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));
    mockModel.setMockScore(mockModel.getTurn(), 1);
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));
    mockModel.setMockScore(mockModel.getTurn(), 15);
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));
    mockModel.resetMockScore(mockModel.getTurn());
    mockModel.resetMockScore(mockModel.getTurn().getOppositeDisc());

    // returns nothing if passing ends the game but the player has an equal or lower score than
    // the opponent
    mockModel.setMockConsecutivePasses(1);
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));
    mockModel.setMockScore(mockModel.getTurn(), 1);
    assertEquals(Optional.empty(), strategy.chooseMove(mockModel));

    // passes move if passing ends the game and the player has a higher score than the opponent
    mockModel.setMockScore(mockModel.getTurn(), 15);
    assertEquals(new Move(true), strategy.chooseMove(mockModel).get());
  }

  @Test
  public void multipleStrategiesCanBeCalled() {
    // tests revolving around only one hybrid strategy will suffice
    InfallibleReversiStrategy strategy = new PlayCornersMaxScore();
    MutableReversiModel model = new BasicReversi(4);

    // test the second strategy - the max score strategy
    model.placeDisc(new Point(1, -2));
    Point chosenTile = strategy.chooseMove(model).getTile().get();
    assertEquals(new Point(2, -3), chosenTile);
    model.placeDisc(chosenTile);

    // test the first strategy - the play corners strategy
    model.placeDisc(new Point(1, -3));
    assertEquals(new Point(0, -3),
            strategy.chooseMove(model).getTile().get());
  }

  @Test
  public void playFullGame() {
    // strategies aren't technically players, but for this test,
    InfallibleReversiStrategy black = new PlayCornersMaxScore();
    InfallibleReversiStrategy white = new AvoidCornerAdjacencyMaxScore();
    MutableReversiModel model = new BasicReversi(4);

    while (!model.gameOver()) {
      Move move = model.getTurn().equals(new Disc(Disc.DiscColor.BLACK))
              ? black.chooseMove(model) : white.chooseMove(model);
      if (move.getTile().isPresent()) {
        model.placeDisc(move.getTile().get());
      } else if (move.getPass()) {
        model.passTurn();
      }
    }

    assertTrue(model.getConsecutivePasses() == 2
            || (!model.canPlaceDisc(new Disc(Disc.DiscColor.BLACK))
            && !model.canPlaceDisc(new Disc(Disc.DiscColor.WHITE))));
  }
}
