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
 * Mock for the Reversi model that lies about information regarding the game. Information it can
 * lie about include the maximum number of consecutive passes allowed, the game board, the current
 * turn, the number of consecutive passes that have occurred, the side length of the game board,
 * and the score of a specific player. This mock is mainly to be used for testing.
 *
 * <p>Note that the mock only provides misinformation regarding the current game status rather than
 * actively alter the game itself. That being said, while getting information regarding the game
 * results in faulty data, the actual gameplay is based off legitimate data.</p>
 */
public class ReversiMockGameInfo implements MutableReversiModel {
  private final MutableReversiModel functionalModel;
  private Optional<Optional<Integer>> mockMaxNumConsecutivePassesAllowed;
  private Optional<Map<Point, Disc>> mockGameBoard;
  private Optional<Disc> mockTurn;
  private Optional<Integer> mockConsecutivePasses;
  private Optional<Integer> mockGameBoardSideLength;
  private Map<Disc, Optional<Integer>> mockScore;

  /**
   * Constructs a ReversiMockGameInfo object that mocks a given model's behavior. This mock will
   * only lie about selected information regarding the game. The selected information is
   * customizable. As a result, all mock information regarding the game are initially set to
   * nothing.
   *
   * @param functionalModel the model to mock
   * @throws IllegalArgumentException if the given board dimension is invalid
   */
  public ReversiMockGameInfo(MutableReversiModel functionalModel) throws IllegalArgumentException {
    if (functionalModel == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.functionalModel = functionalModel;
    this.mockMaxNumConsecutivePassesAllowed = Optional.empty();
    this.mockGameBoard = Optional.empty();
    this.mockTurn = Optional.empty();
    this.mockConsecutivePasses = Optional.empty();
    this.mockGameBoardSideLength = Optional.empty();
    this.mockScore = new HashMap<>();
    this.mockScore.put(new Disc(Disc.DiscColor.BLACK), Optional.empty());
    this.mockScore.put(new Disc(Disc.DiscColor.WHITE), Optional.empty());
  }

  /**
   * Constructs a ReversiMockGameInfo object by building a direct copy of a Reversi game.
   *
   * @param mockMaxNumConsecutivePassesAllowed the mock information for the maximum number of
   *                                           consecutive passes allowed
   * @param mockGameBoard the mock information for the game board
   * @param mockTurn the mock information for the current turn
   * @param mockConsecutivePasses the mock information for the number of consecutive passes that
   *                              have occurred
   * @param mockGameBoardSideLength the mock information for the side length of the game board
   * @param mockScore the mock information for the score of all players
   * @param game the Reversi game to be copied
   * @throws IllegalArgumentException if the given score is <code>null</code>> OR the given Reversi
   *                                  game is <code>null</code>
   */
  public ReversiMockGameInfo(Optional<Optional<Integer>> mockMaxNumConsecutivePassesAllowed,
                             Optional<Map<Point, Disc>> mockGameBoard, Optional<Disc> mockTurn,
                             Optional<Integer> mockConsecutivePasses,
                             Optional<Integer> mockGameBoardSideLength,
                             Map<Disc, Optional<Integer>> mockScore, ROReversiModel game)
          throws IllegalArgumentException {
    if (mockScore == null) {
      throw new IllegalArgumentException("Mock scores cannot be null.");
    } else if (game == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    }

    this.functionalModel = game.copyGame();
    this.mockMaxNumConsecutivePassesAllowed = mockMaxNumConsecutivePassesAllowed;
    this.mockGameBoard = mockGameBoard;
    this.mockTurn = mockTurn;
    this.mockConsecutivePasses = mockConsecutivePasses;
    this.mockGameBoardSideLength = mockGameBoardSideLength;
    this.mockScore = mockScore;
  }

  @Override
  public void placeDisc(Point tile) throws IllegalStateException, IllegalArgumentException {
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

  @Override
  public MutableReversiModel copyGame() {
    return new ReversiMockGameInfo(this.mockMaxNumConsecutivePassesAllowed, this.mockGameBoard,
            this.mockTurn, this.mockConsecutivePasses, this.mockGameBoardSideLength,
            this.mockScore, this.functionalModel);
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
  public Map<Point, Disc> getBoard() {
    return this.mockGameBoard.orElseGet(this.functionalModel::getBoard);
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
  public boolean gameOver() {
    return this.functionalModel.gameOver();
  }

  @Override
  public Disc getDisc(Point tile) throws IllegalArgumentException {
    return this.functionalModel.getDisc(tile);
  }

  @Override
  public Disc getTurn() {
    return this.mockTurn.orElseGet(this.functionalModel::getTurn);
  }

  @Override
  public int getScore(Disc turn) throws IllegalArgumentException {
    return this.mockScore.get(turn).orElseGet(() -> this.functionalModel.getScore(turn));
  }

  @Override
  public int getGameBoardSideLength() {
    return this.mockGameBoardSideLength.orElseGet(this.functionalModel::getGameBoardSideLength);
  }

  @Override
  public int getConsecutivePasses() {
    return this.mockConsecutivePasses.orElseGet(this.functionalModel::getConsecutivePasses);
  }

  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return this.mockMaxNumConsecutivePassesAllowed
            .orElseGet(this.functionalModel::getMaxNumConsecutivePassesAllowed);
  }

  @Override
  public Disc getWinner() throws IllegalStateException {
    return this.functionalModel.getWinner();
  }

  /**
   * Sets the mock value for the maximum number of consecutive passes allowed to the given value.
   *
   * @param mockMaxNumConsecutivePassesAllowed the given maximum number of consecutive passes
   *                                           allowed in which to set the mock value for the
   *                                           maximum number of consecutive passes allowed
   */
  public void setMockMaxNumConsecutivePassesAllowed(
          Optional<Integer> mockMaxNumConsecutivePassesAllowed) {
    this.mockMaxNumConsecutivePassesAllowed = Optional.of(mockMaxNumConsecutivePassesAllowed);
  }

  /**
   * Sets the mock value for the game board to the given value.
   *
   * @param mockGameBoard the given game board in which to set the mock value for the game board
   * @throws IllegalArgumentException if the given game board is <code>null</code>
   */
  public void setMockGameBoard(Map<Point, Disc> mockGameBoard) throws IllegalArgumentException {
    if (mockGameBoard == null) {
      throw new IllegalArgumentException("Game board cannot be null.");
    }

    this.mockGameBoard = Optional.of(mockGameBoard);
  }

  /**
   * Sets the mock value for the current turn to the given value.
   *
   * @param mockTurn the given current turn in which to set the mock value for the current turn
   * @throws IllegalArgumentException if the given current turn is <code>null</code>
   */
  public void setMockTurn(Disc mockTurn) throws IllegalArgumentException {
    if (mockTurn == null) {
      throw new IllegalArgumentException("Current turn cannot be null.");
    }

    this.mockTurn = Optional.of(mockTurn);
  }

  /**
   * Sets the mock value for the number of consecutive passes that have occurred to the given
   * value.
   *
   * @param mockConsecutivePasses the given number of consecutive passes that have occurred in
   *                              which to set the mock value for the number of consecutive passes
   *                              that have occurred
   * @throws IllegalArgumentException if the given number of consecutive passes that have occurred
   *                                  is <code>null</code>
   */
  public void setMockConsecutivePasses(Integer mockConsecutivePasses)
          throws IllegalArgumentException {
    if (mockConsecutivePasses == null) {
      throw new IllegalArgumentException(
              "The number of consecutive passes that have occurred cannot be null.");
    }

    this.mockConsecutivePasses = Optional.of(mockConsecutivePasses);
  }

  /**
   * Sets the mock value for the game board side length to the given value.
   *
   * @param mockGameBoardSideLength the given game board side length in which to set the mock value
   *                                for the game board side length
   * @throws IllegalArgumentException if the given game board length is <code>null</code>
   */
  public void setMockGameBoardSideLength(Integer mockGameBoardSideLength)
          throws IllegalArgumentException {
    if (mockGameBoardSideLength == null) {
      throw new IllegalArgumentException("The side length of the game board cannot be null.");
    }

    this.mockGameBoardSideLength = Optional.of(mockGameBoardSideLength);
  }

  /**
   * Sets the mock value for the score of a specific player/disc to the given value.
   *
   * @param player the player/disc whose mock value for the score will be set
   * @param mockScore the given score in which to set the mock value for the score
   * @throws IllegalArgumentException if the given player is <code>null</code> OR the given player
   *                                  is not a black or white disc OR the given score is
   *                                  <code>null</code>
   */
  public void setMockScore(Disc player, Integer mockScore)
          throws IllegalArgumentException {
    if (player == null) {
      throw new IllegalArgumentException("The player cannot be null.");
    } else if (!player.hasDisc()) {
      throw new IllegalArgumentException("The player must be black or white.");
    } else if (mockScore == null) {
      throw new IllegalArgumentException("Score cannot be null.");
    }

    this.mockScore.put(new Disc(player.getDiscColor()), Optional.of(mockScore));
  }

  /**
   * Removes the mock value for the maximum number of consecutive passes allowed. Getting the
   * maximum number of consecutive passes allowed will now return the legitimate value rather than
   * the mock.
   */
  public void resetMockMaxNumConsecutivePassesAllowed() {
    this.mockMaxNumConsecutivePassesAllowed = Optional.empty();
  }

  /**
   * Removes the mock value for the game board. Getting the game board will now return the
   * legitimate value rather than the mock.
   */
  public void resetMockGameBoard() {
    this.mockGameBoard = Optional.empty();
  }

  /**
   * Removes the mock value for the current turn. Getting the current turn will now return the
   * legitimate value rather than the mock.
   */
  public void resetMockTurn() {
    this.mockTurn = Optional.empty();
  }

  /**
   * Removes the mock value for the number of consecutive passes that have occurred. Getting the
   * number of consecutive passes that have occurred will now return the legitimate value rather
   * than the mock.
   */
  public void resetMockConsecutivePasses() {
    this.mockConsecutivePasses = Optional.empty();
  }

  /**
   * Removes the mock value for the game board side length. Getting the game board will now return
   * the legitimate value rather than the mock.
   */
  public void resetMockGameBoardSideLength() {
    this.mockGameBoardSideLength = Optional.empty();
  }

  /**
   * Removes the mock value for the score for a specific player/disc. Getting the score for a
   * specific player will now return the legitimate value rather than the mock.
   *
   * @param player the player/disc whose mock value for the score will be reset
   * @throws IllegalArgumentException if the given player is <code>null</code> OR the given player
   *                                  is not a black or white disc
   */
  public void resetMockScore(Disc player) throws IllegalArgumentException {
    if (player == null) {
      throw new IllegalArgumentException("The player cannot be null.");
    } else if (!player.hasDisc()) {
      throw new IllegalArgumentException("The player must be black or white.");
    }

    this.mockScore.put(new Disc(player.getDiscColor()), Optional.empty());
  }
}