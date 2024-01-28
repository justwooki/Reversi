package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import cs3500.reversi.main.controller.ModelFeatures;

/**
 * Represents an abstract Reversi class.
 */
public abstract class AbstractReversi implements MutableReversiModel {
  protected Map<Point, Disc> gameBoard;
  protected final Disc turn;
  // INVARIANT: The number of consecutive passes that occur in a game is always less than or equal
  // to two.
  protected int consecutivePasses;
  protected final int gameBoardSideLength;
  protected final List<ModelFeatures> modelListeners;
  protected boolean gameStarted;

  /**
   * Constructs an AbstractReversi object by building a brand new game board based on the given
   * board dimensions and its restrictions and the given disc representing the starting turn.
   *
   * @param gameBoardMinSideLength the minimum side length dimension of a game board that is
   *                               allowed to exist
   * @param gameBoardSideLength the side dimension of the game board
   * @param turn the starting turn
   * @throws IllegalArgumentException if the given board dimension is invalid OR the given disc is
   *                                  <code>null</code> OR the given disc is nonexistent
   */
  public AbstractReversi(int gameBoardMinSideLength, int gameBoardSideLength,
                         Disc turn) throws IllegalArgumentException {
    if (gameBoardSideLength < gameBoardMinSideLength) {
      throw new IllegalArgumentException("Invalid board dimensions.");
    } else if (turn == null) {
      throw new IllegalArgumentException("The given disc cannot be null.");
    } else if (!turn.hasDisc()) {
      throw new IllegalArgumentException("The given disc cannot be nonexistent.");
    }

    this.gameBoardSideLength = gameBoardSideLength;
    this.turn = turn;
    this.gameBoard = new HashMap<>();
    this.modelListeners = new ArrayList<>();
  }

  /**
   * Constructs an AbstractReversi object by building a direct copy of a Reversi game.
   *
   * @param game the Reversi game to be copied
   * @throws IllegalArgumentException if the given Reversi game is <code>null</code>
   */
  protected AbstractReversi(ROReversiModel game, boolean gameStarted) {
    if (game == null) {
      throw new IllegalStateException("The game cannot be null.");
    }

    this.gameBoardSideLength = game.getGameBoardSideLength();
    this.turn = game.getTurn();
    this.gameBoard = game.getBoard();
    this.modelListeners = new ArrayList<>();
    this.consecutivePasses = game.getConsecutivePasses();
    this.gameStarted = gameStarted;
  }

  @Override
  public void placeDisc(Point tile) throws IllegalStateException, IllegalArgumentException {
    throwExceptionIfGameOver();

    if (search(tile, this.turn, true)) {
      gameBoard.get(tile).makeDiscExisting(this.turn.getDiscColor());
      this.consecutivePasses = 0; // resets consecutive passes to 0
      updateTurn();
    } else {
      throw new IllegalStateException("Cannot place disc here!");
    }
  }

  @Override
  public void passTurn() throws IllegalStateException {
    throwExceptionIfGameOver();

    this.consecutivePasses++;
    updateTurn();
  }

  @Override
  public void setBoard(Map<Point, Disc> gameBoard) throws IllegalArgumentException {
    if (gameBoard == null) {
      throw new IllegalArgumentException("The given game board cannot be null.");
    } else if (gameBoard.size() != this.gameBoard.size()) {
      throw new IllegalArgumentException("Invalid dimensions for given game board.");
    }

    this.gameBoard = copyBoard(gameBoard);
  }

  @Override
  public void addFeatures(ModelFeatures feature) throws IllegalArgumentException {
    if (feature == null) {
      throw new IllegalArgumentException("The feature cannot be null.");
    } else if (this.modelListeners.contains(feature)) {
      throw new IllegalArgumentException("Cannot add feature twice.");
    } else if (this.modelListeners.size() >= 2) {
      throw new IllegalArgumentException("Max features added - cannot add anymore features.");
    }

    this.modelListeners.add(feature);
  }

  @Override
  public void startGame() throws IllegalStateException {
    if (this.gameStarted) {
      throw new IllegalStateException("Game has already started.");
    }

    this.gameStarted = true;

    if (this.modelListeners.size() == 2) {
      this.modelListeners.get(0).notifyGameStarted(new Disc(Disc.DiscColor.BLACK));
      this.modelListeners.get(1).notifyGameStarted(new Disc(Disc.DiscColor.WHITE));
      this.modelListeners.get(0).notifyTurnUpdated();
    }
  }

  @Override
  public Map<Point, Disc> getBoard() {
    return copyBoard(this.gameBoard);
  }

  @Override
  public boolean canPlaceDisc(Disc color) throws IllegalArgumentException {
    return this.gameBoard.keySet().stream().anyMatch(tile -> canPlaceDiscAtTile(color, tile));
  }

  @Override
  public boolean canPlaceDiscAtTile(Disc color, Point tile) throws IllegalArgumentException {
    if (color == null) {
      throw new IllegalArgumentException("The given disc cannot be null.");
    } else if (!color.hasDisc()) {
      throw new IllegalArgumentException("Nonexistent disc cannot be placed.");
    } else if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    } else if (this.gameBoard.get(tile) == null) {
      return false;
    }

    return search(tile, color, false);
  }

  @Override
  public boolean isAdjacent(Point tile1, Point tile2) throws IllegalArgumentException {
    if (tile1 == null || tile2 == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    }

    // stub return statement to be overridden
    return false;
  }

  @Override
  public boolean gameOver() {
    return !canPlaceDisc(new Disc(Disc.DiscColor.BLACK))
            && !canPlaceDisc(new Disc(Disc.DiscColor.WHITE));
  }

  @Override
  public Disc getDisc(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("Given tile cannot be null.");
    }

    Disc disc = gameBoard.get(tile);
    if (disc == null) {
      throw new IllegalArgumentException("Invalid tile.");
    }
    return new Disc(disc.getDiscColor());
  }

  @Override
  public Disc getTurn() {
    return new Disc(this.turn.getDiscColor());
  }

  @Override
  public int getScore(Disc turn) throws IllegalArgumentException {
    if (turn == null) {
      throw new IllegalArgumentException("Given disc cannot be null.");
    } else if (!turn.hasDisc()) {
      throw new IllegalArgumentException("Score cannot be returned for nonexistent disc.");
    }

    return (int) this.gameBoard.values().stream().filter(
        disc -> disc.getDiscColor() == turn.getDiscColor()).count();
  }

  @Override
  public int getGameBoardSideLength() {
    return this.gameBoardSideLength;
  }

  @Override
  public int getConsecutivePasses() {
    return this.consecutivePasses;
  }

  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return Optional.empty();
  }

  @Override
  public Disc getWinner() throws IllegalStateException {
    if (!gameOver()) {
      throw new IllegalStateException("Game isn't over yet!");
    }

    int blackScore = getScore(new Disc(Disc.DiscColor.BLACK));
    int whiteScore = getScore(new Disc(Disc.DiscColor.WHITE));
    if (blackScore == whiteScore) {
      return new Disc(Disc.DiscColor.NONE);
    } else if (blackScore < whiteScore) {
      return new Disc(Disc.DiscColor.WHITE);
    } else {
      return new Disc(Disc.DiscColor.BLACK);
    }
  }

  /**
   * Searches adjacent board tiles in all bordering directions from the given tile on the game
   * board. If a certain disc is found, this function will return <code>true.</code>
   *
   * @param color the disc color
   * @param tile the tile at which to start searching
   * @param flipDiscs if <code>true</code>, if a disc as described above is found in one or more
   *                  lanes bordering the given tile, all discs between the given tile and the
   *                  tile of the disc found will be flipped to the color of the given disc color
   * @return <code>true</code> if a specific disc is found and <code>false</code> otherwise
   * @throws IllegalArgumentException if the given tile is <code>null</code> or invalid OR the
   *                                  given disc color is <code>null</code>
   */
  protected boolean search(Point tile, Disc color, boolean flipDiscs)
          throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("The given tile cannot be null.");
    } else if (color == null) {
      throw new IllegalArgumentException("The given disc cannot be null.");
    }

    // stub return statement to be overridden
    return false;
  }

  /**
   * Helper method for {@link #search(Point, Disc, boolean)} that recursively searches through
   * every tile in a particular straight line of the original tile. The tile being searched for
   * must contain a disc matching the given disc color. There must be at least one tile with a disc
   * of the opposite color between the two discs, and there cannot be any empty tiles between both
   * discs.
   *
   * @param currTile the tile that is currently being searched
   * @param startTile the tile where the search originally started
   * @param getNextTile the function calculating the next tile to iterate through to ensure the
   *                    search is going in a particular straight line
   * @param color the disc color
   * @param flipDiscs if <code>true</code>, if a disc being searched by
   *                  {@link #search(Point, Disc, boolean)} is found, all discs between the start
   *                  tile and the current tile will be flipped to the color of the given disc
   *                  color
   * @return <code>true</code> if the search as described above is successful and
   *         <code>false</code> otherwise
   * @throws IllegalArgumentException if any given tile is <code>null</code> OR the given function
   *                                  is <code>null</code> OR the given disc color is
   *                                  <code>null</code>
   */
  protected boolean searchHelper(Point currTile, Point startTile,
                                 Function<Point, Point> getNextTile, Disc color,
                                 boolean flipDiscs) throws IllegalArgumentException {
    if (currTile == null) {
      throw new IllegalArgumentException("The given tile being searched cannot be null.");
    } else if (startTile == null) {
      throw new IllegalArgumentException("The given tile of origin cannot be null.");
    } else if (getNextTile == null) {
      throw new IllegalArgumentException("The given function cannot be null.");
    } else if (color == null) {
      throw new IllegalArgumentException("The given disc cannot be null.");
    }

    // Base case 1: Does the current tile or disc at the tile exist?
    try {
      if (!getDisc(currTile).hasDisc()) {
        return false;
      }
    } catch (IllegalArgumentException e) {
      return false;
    }

    // Base case 2: Is the current tile the one being searched for?
    boolean matching = color.equals(this.gameBoard.get(currTile));
    boolean nonAdj = !isAdjacent(startTile, currTile);
    if (matching && nonAdj) {
      return true;
    } else if (matching) {
      return false;
    }

    // Recall this function recursively
    if (searchHelper(getNextTile.apply(currTile), startTile, getNextTile, color, flipDiscs)) {
      // Flip the discs if asked
      if (flipDiscs && !color.equals(this.gameBoard.get(currTile))) {
        this.gameBoard.get(currTile).flipDiscColor();
      }
      return true;
    }
    return false;
  }

  /**
   * Throws an exception if the game is over.
   *
   * @throws IllegalStateException if the game is over
   */
  protected void throwExceptionIfGameOver() throws IllegalStateException {
    if (gameOver()) {
      throw new IllegalStateException("Game is over.");
    }
  }

  /**
   * Updates the current turn to the next turn and notifies all model listeners that the game has
   * mutated, and the turn has been updated.
   */
  protected void updateTurn() {
    this.turn.flipDiscColor();

    for (ModelFeatures listener : modelListeners) {
      listener.notifyModelStateUpdated();
    }

    for (ModelFeatures listener : modelListeners) {
      listener.notifyTurnUpdated();
    }
  }

  /**
   * Provides a deep copy of the given Reversi game board.
   *
   * @param board the game board to copy
   * @return a deep copied version of the given game board
   * @throws IllegalArgumentException if the given game board is <code>null</code>
   */
  protected Map<Point, Disc> copyBoard(Map<Point, Disc> board) throws IllegalArgumentException {
    if (board == null) {
      throw new IllegalArgumentException("Given board cannot be null.");
    }

    Map<Point, Disc> copyOfBoard = new HashMap<>();

    // must conduct a deep copy
    for (Map.Entry<Point, Disc> entry : board.entrySet()) {
      Point tile = entry.getKey();
      Disc originalDisc = entry.getValue();
      copyOfBoard.put(new Point(tile.x, tile.y),
              new Disc(originalDisc.getDiscColor()));
    }

    // use Map.copyOf to ensure the returned board is unmodifiable
    return Map.copyOf(copyOfBoard);
  }
}
