package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a basic Reversi game played on a hexagonal grid. Reversi is a two-player game played
 * on a regular grid of cells, each being a regular pointy-top hexagon. Each player has a black or
 * white color, and the game pieces are discs colored black on one side and white on the other.
 * Game play begins with equal numbers of both colors of discs arranged in the middle of the grid.
 * The black player moves first. On each turn, a player may either pass and let the other player
 * move, or select a legal empty cell and play a disc in that cell. The game ends when both players
 * can no longer move, or both players have passed their turn consecutively.
 *
 * <p>Each hexagonal tile is positioned using x-y coordinates with the origin at the center of the
 * board (0, 0). The x-coordinate determines the row position (increasing from the top of the board
 * to the bottom), and the y-coordinate determines the column position (increasing from the
 * bottom-left corner of the board to the top-right corner) on the game board.</p>
 *
 * <p>For more information on the disc game pieces, see {@link Disc}.</p>
 */
public class BasicReversi extends AbstractReversi {
  // chose 2 as the limit because a game board of size 2 is technically playable albeit brief
  private static final int GAME_BOARD_MIN_SIDE_LENGTH = 2;
  private final int maxNumOfConsecutivePassesAllowed = 2;

  /**
   * Constructs a BasicReversi object by building a brand new game board based on the given board
   * dimensions. The first move is always made by black.
   *
   * @param gameBoardSideLength the side dimension of the regular hexagonal game board
   * @throws IllegalArgumentException if the given board dimension is invalid
   */
  public BasicReversi(int gameBoardSideLength) throws IllegalArgumentException {
    // first turn is black
    super(GAME_BOARD_MIN_SIDE_LENGTH, gameBoardSideLength, new Disc(Disc.DiscColor.BLACK));
    this.gameBoard =
            Collections.unmodifiableMap(addInitialDiscs(createGameBoard(gameBoardSideLength)));
  }

  /**
   * Constructs an BasicReversi object by building a direct copy of a Reversi game.
   *
   * @param game the Reversi game to be copied
   * @throws IllegalArgumentException if the given Reversi game is <code>null</code>
   */
  protected BasicReversi(ROReversiModel game, boolean gameStarted) {
    super(game, gameStarted);
  }

  @Override
  public MutableReversiModel copyGame() {
    return new BasicReversi(this, this.gameStarted);
  }

  @Override
  public List<Point> getCornerTiles() {
    return List.of(new Point(-(this.gameBoardSideLength - 1), this.gameBoardSideLength - 1),
            new Point(0, this.gameBoardSideLength - 1),
            new Point(-(this.gameBoardSideLength - 1), 0),
            new Point(this.gameBoardSideLength - 1, 0),
            new Point(0, -(this.gameBoardSideLength - 1)),
            new Point(this.gameBoardSideLength - 1, -(this.gameBoardSideLength - 1)));
  }

  @Override
  public boolean isAdjacent(Point tile1, Point tile2) throws IllegalArgumentException {
    super.isAdjacent(tile1, tile2);
    int tile2X = tile2.x;
    int tile2Y = tile2.y;

    return tile1.equals(new Point(tile2X + 1, tile2Y))
            || tile1.equals(new Point(tile2X - 1, tile2Y))
            || tile1.equals(new Point(tile2X, tile2Y + 1))
            || tile1.equals(new Point(tile2X, tile2Y - 1))
            || tile1.equals(new Point(tile2X - 1, tile2Y + 1))
            || tile1.equals(new Point(tile2X + 1, tile2Y - 1));
  }

  @Override
  public boolean gameOver() {
    return consecutivePasses >= maxNumOfConsecutivePassesAllowed || super.gameOver();
  }

  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return Optional.of(maxNumOfConsecutivePassesAllowed);
  }

  @Override
  protected boolean search(Point tile, Disc color, boolean flipDiscs)
          throws IllegalArgumentException {
    super.search(tile, color, flipDiscs);
    if (getDisc(tile).hasDisc()) {
      return false;
    }

    Function<Point, Point> goRight =
        (Point currTile) -> new Point(currTile.x + 1, currTile.y);
    Function<Point, Point> goLeft =
        (Point currTile) -> new Point(currTile.x - 1, currTile.y);
    Function<Point, Point> goTopRight =
        (Point currTile) -> new Point(currTile.x, currTile.y + 1);
    Function<Point, Point> goBottomLeft =
        (Point currTile) -> new Point(currTile.x, currTile.y - 1);
    Function<Point, Point> goTopLeft =
        (Point currTile) -> new Point(currTile.x - 1, currTile.y + 1);
    Function<Point, Point> goBottomRight =
        (Point currTile) -> new Point(currTile.x + 1, currTile.y - 1);

    // store each search in a boolean rather than returning it to prevent short-circuiting
    boolean searchRight = searchHelper(goRight.apply(tile), tile, goRight, color, flipDiscs);
    boolean searchLeft = searchHelper(goLeft.apply(tile), tile, goLeft, color, flipDiscs);
    boolean searchTopRight = searchHelper(goTopRight.apply(tile), tile, goTopRight, color,
            flipDiscs);
    boolean searchBottomLeft = searchHelper(goBottomLeft.apply(tile), tile, goBottomLeft, color,
            flipDiscs);
    boolean searchTopLeft = searchHelper(goTopLeft.apply(tile), tile, goTopLeft, color, flipDiscs);
    boolean searchBottomRight = searchHelper(goBottomRight.apply(tile), tile, goBottomRight, color,
            flipDiscs);

    return searchRight || searchLeft || searchTopRight || searchBottomLeft || searchTopLeft
            || searchBottomRight;
  }

  /**
   * Creates a game board of set game board size in a regular hexagonal shape.
   *
   * @param gameBoardSideLength the length of one of the sides of the board
   * @return a map taking in a tile for its key and a disc for its value
   * @throws IllegalArgumentException if the given board dimension is invalid
   */
  Map<Point, Disc> createGameBoard(int gameBoardSideLength) throws IllegalArgumentException {
    if (gameBoardSideLength < GAME_BOARD_MIN_SIDE_LENGTH) {
      throw new IllegalArgumentException("Invalid board dimensions.");
    }

    int upperBound = gameBoardSideLength - 1; // the upper boundary of the board
    int lowerBound = 1 - gameBoardSideLength; // the lower boundary of the board
    Map<Point, Disc> gameBoard = new HashMap<>();
    // top half of hexagon board - includes middle longest row
    for (int currY = upperBound, xCount = 0; currY >= 0; currY--) {
      // iterates from start to end
      for (int currX = lowerBound; currX <= xCount; currX++) {
        gameBoard.put(new Point(currX, currY), new Disc());
      }
      xCount++;
    }

    // bottom half of hexagon board - starts from right under the middle longest row
    for (int currY = -1, xCount = 1; currY >= lowerBound; currY--) {
      // iterates from end to start
      for (int currX = upperBound; currX >= lowerBound + xCount; currX--) {
        gameBoard.put(new Point(currX, currY), new Disc());
      }
      xCount++;
    }

    return gameBoard;
  }

  /**
   * Initializes the first discs on the board in a ring around the center hexagonal tile,
   * interchanging the black and white discs. The current implementation explicitly adds black
   * discs on the tile directly to the top-left, bottom-left, and right of the middle hexagonal
   * tile, and explicitly adds white discs directly to the top-right, bottom-right, and left of
   * the middle hexagonal tile.
   *
   * @param gameBoard the game board
   * @return the updated game board
   * @throws IllegalArgumentException if the given game board is <code>null</code>
   */
  Map<Point, Disc> addInitialDiscs(Map<Point, Disc> gameBoard) throws IllegalArgumentException {
    if (gameBoard == null) {
      throw new IllegalArgumentException("The given game board cannot be null.");
    }

    // all the black discs
    gameBoard.replace(new Point(-1, 1), new Disc(Disc.DiscColor.BLACK));
    gameBoard.replace(new Point(1, 0), new Disc(Disc.DiscColor.BLACK));
    gameBoard.replace(new Point(0, -1), new Disc(Disc.DiscColor.BLACK));

    // all the white discs
    gameBoard.replace(new Point(0, 1), new Disc(Disc.DiscColor.WHITE));
    gameBoard.replace(new Point(1, -1), new Disc(Disc.DiscColor.WHITE));
    gameBoard.replace(new Point(-1, 0), new Disc(Disc.DiscColor.WHITE));

    return gameBoard;
  }
}