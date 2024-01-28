package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.provider.board.ReversiGameBoard;
import cs3500.reversi.provider.player.ReversiPlayer;

/**
 * An adapter that adapts one game board representation to another. The "adaptee" being adapted is
 * a game board represented as a map of tiles to discs, with the tiles representing each tile on
 * the board, and the discs representing the physical disc that is placed on top of its respective
 * tile. The "target" that the adaptee is being adapted to is an interface given by the providers
 * representing a Reversi game board.
 *
 * <p>Note: All class/interfaces given by the providers can be found in the provider package.</p>
 *
 * @see Disc
 * @see ReversiGameBoard
 */
public final class GameBoardToProviderReversiGameBoardAdapter implements ReversiGameBoard {
  private final Map<Point, Disc> gameBoardAdaptee;
  private final int gameBoardAdapteeSideLength;

  /**
   * Constructs a GameBoardToProviderReversiGameBoardAdapter object that takes in a game board
   * adaptee to adapt to the target.
   *
   * @param gameBoardAdaptee the game board adaptee to adapt to the game board target
   * @throws IllegalArgumentException if the given game board is <code>null</code>
   */
  public GameBoardToProviderReversiGameBoardAdapter(Map<Point, Disc> gameBoardAdaptee)
          throws IllegalArgumentException {
    if (gameBoardAdaptee == null) {
      throw new IllegalArgumentException("Given game board cannot be null.");
    }

    this.gameBoardAdaptee = copyBoard(gameBoardAdaptee);
    this.gameBoardAdapteeSideLength = calculateBoardSideLength(this.gameBoardAdaptee);
  }

  @Override
  public int getPlayerScore(ReversiPlayer player) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addDiscToBoard(Point coordinates, cs3500.reversi.provider.board.Disc discToPlace)
          throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public cs3500.reversi.provider.board.Disc getDiscAt(Point coordinates)
          throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<Point, cs3500.reversi.provider.board.Disc> getBoardData() {
    Map<Point, cs3500.reversi.provider.board.Disc> boardData = new HashMap<>();

    for (Point tile : this.gameBoardAdaptee.keySet()) {
      boardData.put(tileCoordToProviderCoord(tile),
              discToProviderDisc(this.gameBoardAdaptee.get(tile), tile));
    }

    return boardData;
  }

  @Override
  public List<Point> getCoordsForPlacedDiscs() {
    List<Point> coords = new ArrayList<>();
    Map<Point, cs3500.reversi.provider.board.Disc> boardData = getBoardData();
    for (Point p : boardData.keySet()) {
      if (boardData.get(p) == cs3500.reversi.provider.board.Disc.BLACK
              || boardData.get(p) == cs3500.reversi.provider.board.Disc.WHITE) {
        coords.add(p);
      }
    }
    return coords;
  }

  @Override
  public ReversiGameBoard getCopyOfBoard() {
    return new GameBoardToProviderReversiGameBoardAdapter(this.gameBoardAdaptee);
  }

  /**
   * Provides a deep copy of the given board.
   *
   * @param board the board to copy
   * @return a deep copied version of the given board
   * @throws IllegalArgumentException if the given board is <code>null</code>
   */
  private Map<Point, Disc> copyBoard(Map<Point, Disc> board) throws IllegalArgumentException {
    if (board == null) {
      throw new IllegalArgumentException("Given board cannot be null.");
    }

    Map<Point, Disc> boardCopy = new HashMap<>();
    for (Map.Entry<Point, Disc> entry : board.entrySet()) {
      Point tile = entry.getKey();
      Disc originalDisc = entry.getValue();
      boardCopy.put(new Point(tile.x, tile.y),
              new Disc(originalDisc.getDiscColor()));
    }

    return Map.copyOf(boardCopy);
  }

  /**
   * Calculates the side length of the given board.
   *
   * @param board the given board to calculate the side length of
   * @return the calculated side length of the given board
   * @throws IllegalArgumentException if the given board is <code>null</code>
   */
  private int calculateBoardSideLength(Map<Point, Disc> board) throws IllegalArgumentException {
    if (board == null) {
      throw new IllegalArgumentException("Given board cannot be null.");
    }

    int largestYVal = 0;

    for (Point tile : board.keySet()) {
      if (tile.y > largestYVal) {
        largestYVal = tile.y;
      }
    }

    return largestYVal + 1;
  }

  /**
   * Converts the given disc at the specified tile to the provider's disc.
   *
   * @param disc the given disc to convert
   * @param tile the given tile
   * @return the provider's disc converted from the given disc
   * @throws IllegalArgumentException if the given disc is <code>null</code> OR the given tile is
   *                                  <code>null</code>
   */
  private cs3500.reversi.provider.board.Disc discToProviderDisc(Disc disc, Point tile)
          throws IllegalArgumentException {
    if (disc == null) {
      throw new IllegalArgumentException("Given disc cannot be null.");
    } else if (tile == null) {
      throw new IllegalArgumentException("Given tile cannot be null.");
    } else if (disc.getDiscColor() == Disc.DiscColor.BLACK) {
      return cs3500.reversi.provider.board.Disc.BLACK;
    } else if (disc.getDiscColor() == Disc.DiscColor.WHITE) {
      return cs3500.reversi.provider.board.Disc.WHITE;
    } else if (this.gameBoardAdaptee.containsKey(tile)) {
      return cs3500.reversi.provider.board.Disc.EMPTY;
    } else {
      return cs3500.reversi.provider.board.Disc.NONEXISTENT;
    }
  }

  /**
   * Converts the given tile coordinate to the provider's point coordinate.
   *
   * @param tile the given tile coordinate to convert
   * @return the provider's point coordinate converted from the given tile coordinate
   * @throws IllegalArgumentException if the given tile is <code>null</code>
   */
  private Point tileCoordToProviderCoord(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("Given tile cannot be null.");
    }

    int coordY = this.gameBoardAdapteeSideLength - 1 - tile.y;
    int coordX = (int) (tile.x + tile.y + coordY
            + Math.ceil(tile.y / 2.0));
    return new Point(coordX, coordY);
  }
}
