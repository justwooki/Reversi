import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.main.controller.ModelFeatures;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * Mock for the Reversi model that lies about the value of a move played (a.k.a. the score).
 * This mock is mainly to be used for testing.
 */
public class ReversiMockScore implements MutableReversiModel {
  private final MutableReversiModel functionalModel;
  private Point target;
  private final Map<Disc, Integer> addOnMockScore;

  /**
   * Constructs a ReversiMockScore object mocks a given model's behavior. It also keeps track of a
   * target tile on which to manipulate the score of whichever player played on it. The score is
   * manipulated by adding a mock score onto the legitimate score. This mock score is stored in a
   * map for each respective player.
   *
   * @param target the target tile on which to manipulate the score of whoever played on it
   * @param functionalModel the model to mock
   * @throws IllegalArgumentException if the given target tile is <code>null</code> OR if the
   *                                  given model is <code>null</code>
   */
  public ReversiMockScore(Point target, MutableReversiModel functionalModel)
          throws IllegalArgumentException {
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null.");
    } else if (functionalModel == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.functionalModel = functionalModel;
    this.target = target;

    this.addOnMockScore = new HashMap<>();
    this.addOnMockScore.put(new Disc(Disc.DiscColor.BLACK), 0);
    this.addOnMockScore.put(new Disc(Disc.DiscColor.WHITE), 0);
  }

  /**
   * Constructs a ReversiMockScore object by building a direct copy of a Reversi game.
   *
   * @param target         the target tile on which to manipulate the score of whoever played on it
   * @param addOnMockScore the mock score used to manipulate the legitimate score
   * @param game           the Reversi game to be copied
   * @throws IllegalArgumentException if the given target tile is <code>null</code>> OR the mock
   *                                  score is <code>null</code> OR the given Reversi game is
   *                                  <code>null</code>
   */
  public ReversiMockScore(Point target, Map<Disc, Integer> addOnMockScore, ROReversiModel game)
          throws IllegalArgumentException {
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null.");
    } else if (addOnMockScore == null) {
      throw new IllegalArgumentException("Mock score cannot be null.");
    } else if (game == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    }

    this.functionalModel = game.copyGame();
    this.target = new Point(target.x, target.y);
    this.addOnMockScore = new HashMap<>(addOnMockScore);
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
    return new ReversiMockScore(this.target, this.addOnMockScore, this.functionalModel);
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
    return this.functionalModel.getScore(turn) + this.addOnMockScore.get(turn);
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
    if (this.target.equals(tile)) {
      // choose an arbitrary large number to increase the score by
      this.addOnMockScore.put(getTurn(), this.addOnMockScore.get(getTurn()) + 1000);
    }
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

  /**
   * Sets a new target tile to target.
   *
   * @param newTarget the new target tile
   * @throws IllegalArgumentException if the given target tile is <code>null</code>>
   */
  public void setTarget(Point newTarget) throws IllegalArgumentException {
    if (newTarget == null) {
      throw new IllegalArgumentException("Target cannot be null.");
    }

    this.target = new Point(newTarget.x, newTarget.y);
  }
}