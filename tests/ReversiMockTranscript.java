import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.main.controller.ModelFeatures;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * Mock for the Reversi model that records a transcript of which tile coordinates have been played.
 * This mock is mainly to be used for testing.
 */
public class ReversiMockTranscript implements MutableReversiModel {
  private final StringBuilder log;
  private final MutableReversiModel functionalModel;

  /**
   * Constructs a ReversiMockTranscript object that mocks a given model's behavior. It also keeps
   * track of a transcript to log the object's behavior.
   *
   * @param log the transcript
   * @param functionalModel the model to mock
   * @throws IllegalArgumentException if the given transcript is <code>null</code> OR if the given
   *                                  model is <code>null</code>
   */
  public ReversiMockTranscript(StringBuilder log, MutableReversiModel functionalModel)
          throws IllegalArgumentException {
    if (log == null) {
      throw new IllegalArgumentException("The transcript cannot be null.");
    } else if (functionalModel == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.functionalModel = functionalModel;
    this.log = log;
  }

  /**
   * Constructs a ReversiMockTranscript object by building a direct copy of a Reversi game.
   *
   * @param log the transcript
   * @param game the Reversi game to be copied
   * @throws IllegalArgumentException if the given transcript is <code>null</code>> OR the given
   *                                  Reversi game is <code>null</code>
   */
  public ReversiMockTranscript(StringBuilder log, ROReversiModel game)
          throws IllegalArgumentException {
    if (log == null) {
      throw new IllegalArgumentException("The transcript cannot be null.");
    } else if (game == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    }

    this.functionalModel = game.copyGame();
    this.log = log;
  }

  @Override
  public Map<Point, Disc> getBoard() {
    return this.functionalModel.getBoard();
  }

  @Override
  public boolean canPlaceDisc(Disc color) throws IllegalArgumentException {
    return this.functionalModel.canPlaceDisc(color);
  }

  @Override
  public boolean canPlaceDiscAtTile(Disc color, Point tile) throws IllegalArgumentException {
    return false;
  }

  @Override
  public MutableReversiModel copyGame() {
    return new ReversiMockTranscript(this.log, this.functionalModel);
  }

  @Override
  public List<Point> getCornerTiles() {
    return this.functionalModel.getCornerTiles();
  }

  @Override
  public boolean isAdjacent(Point tile1, Point tile2) throws IllegalArgumentException {
    return this.functionalModel.isAdjacent(tile1, tile2);
  }

  @Override
  public boolean gameOver() {
    return this.functionalModel.gameOver();
  }

  @Override
  public Disc getDisc(Point tile) throws IllegalArgumentException {
    return this.functionalModel.getDisc(tile);
  }

  @Override
  public Disc getTurn() {
    return this.functionalModel.getTurn();
  }

  @Override
  public int getScore(Disc turn) throws IllegalArgumentException {
    return this.functionalModel.getScore(turn);
  }

  @Override
  public int getGameBoardSideLength() {
    return this.functionalModel.getGameBoardSideLength();
  }

  @Override
  public int getConsecutivePasses() {
    return this.functionalModel.getConsecutivePasses();
  }

  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return this.functionalModel.getMaxNumConsecutivePassesAllowed();
  }

  @Override
  public Disc getWinner() throws IllegalStateException {
    return this.functionalModel.getWinner();
  }

  @Override
  public void placeDisc(Point tile) throws IllegalStateException, IllegalArgumentException {
    this.log.append(String.format("Place disc at (%d, %d)\n", tile.x, tile.y));
    this.functionalModel.placeDisc(tile);
  }

  @Override
  public void passTurn() throws IllegalStateException {
    this.functionalModel.passTurn();
  }

  @Override
  public void setBoard(Map<Point, Disc> gameBoard) throws IllegalArgumentException {
    this.functionalModel.setBoard(gameBoard);
  }

  @Override
  public void addFeatures(ModelFeatures feature) throws IllegalArgumentException {
    this.functionalModel.addFeatures(feature);
  }

  @Override
  public void startGame() throws IllegalStateException {
    this.functionalModel.startGame();
  }
}
