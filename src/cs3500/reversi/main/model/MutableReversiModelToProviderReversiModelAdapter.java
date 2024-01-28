package cs3500.reversi.main.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.main.player.DiscToProviderReversiPlayerAdapter;
import cs3500.reversi.provider.board.Disc;
import cs3500.reversi.provider.board.ReversiGameBoard;
import cs3500.reversi.provider.model.ReversiModel;
import cs3500.reversi.provider.player.ReversiPlayer;
import cs3500.reversi.main.model.Disc.DiscColor;

/**
 * An adapter that adapts one mutable Reversi model representation to another. The "adaptee" being
 * adapted is the "main" mutable Reversi model interface. The "target" that the adaptee is being
 * adapted to is an interface given by the providers representing a mutable Reversi model.
 *
 * <p>Note: The "main" mutable Reversi model interface refers to the interface that belongs to this
 * overall project - not the one that the provider has provided. All class/interfaces given by the
 * providers can be found in the provider package.</p>
 *
 * @see MutableReversiModel
 * @see ReversiModel
 */
public final class MutableReversiModelToProviderReversiModelAdapter implements ReversiModel {
  private final MutableReversiModel adaptee;

  /**
   * Constructs a MutableReversiModelToProviderReversiModelAdapter object that takes in a mutable
   * Reversi model adaptee to adapt to the target.
   *
   * @param adaptee the mutable Reversi model adaptee to adapt to the target
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public MutableReversiModelToProviderReversiModelAdapter(MutableReversiModel adaptee)
          throws IllegalArgumentException {
    if (adaptee == null) {
      throw new IllegalArgumentException("Given model cannot be null.");
    }

    this.adaptee = adaptee;
  }

  @Override
  public void notifyCurrentPlayer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isGameOver() {
    return this.adaptee.gameOver();
  }

  @Override
  public int getPlayerScore(ReversiPlayer whichPlayer) throws IllegalArgumentException {
    return this.adaptee.getScore(new cs3500.reversi.main.model.Disc(
            providerDiscToDiscColor(whichPlayer.getDiscColor())));
  }

  @Override
  public int getBoardSize() {
    return this.adaptee.getGameBoardSideLength() * 2 - 1;
  }

  @Override
  public Disc getDiscAt(Point coordinates) throws IllegalArgumentException {
    Point tile = providerCoordToTileCoord(coordinates);
    return discAtTileToProviderDisc(tile);
  }

  @Override
  public List<Point> getCoordsForPlacedDiscs() {
    return getGameBoard().getCoordsForPlacedDiscs();
  }

  @Override
  public boolean playerCanMakeAnyMoves(ReversiPlayer whichPlayer) throws IllegalArgumentException {
    return this.adaptee.canPlaceDisc(new cs3500.reversi.main.model.Disc(
            providerDiscToDiscColor(whichPlayer.getDiscColor())));
  }

  @Override
  public boolean currentTurnPlayerCanPlaceAtGivenCoord(Point coords)
          throws IllegalArgumentException {
    return this.adaptee.canPlaceDiscAtTile(this.adaptee.getTurn(),
            providerCoordToTileCoord(coords));
  }

  @Override
  public ReversiPlayer getCurrentTurnPlayer() {
    return new DiscToProviderReversiPlayerAdapter(this.adaptee.getTurn());
  }

  @Override
  public ReversiGameBoard getGameBoard() {
    return new GameBoardToProviderReversiGameBoardAdapter(this.adaptee.getBoard());
  }

  @Override
  public ReversiModel getCopyOfModel() {
    return new MutableReversiModelToProviderReversiModelAdapter(this.adaptee.copyGame());
  }

  @Override
  public ReversiPlayer getCopyOfPlayerWithMostPoints() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void placeDisc(Point coordinates) throws IllegalArgumentException, IllegalStateException {
    this.adaptee.placeDisc(providerCoordToTileCoord(coordinates));
  }

  @Override
  public void changePlayerTurn() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void pass() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBoard(ReversiGameBoard board) {
    Map<Point, Disc> boardData = board.getBoardData();
    Map<Point, cs3500.reversi.main.model.Disc> adapteeBoard = new HashMap<>();

    for (Point coord : boardData.keySet()) {
      adapteeBoard.put(providerCoordToTileCoord(coord),
              new cs3500.reversi.main.model.Disc(providerDiscToDiscColor(boardData.get(coord))));
    }

    this.adaptee.setBoard(adapteeBoard);
  }

  @Override
  public void startGame(ReversiPlayer player1, ReversiPlayer player2) {
    throw new UnsupportedOperationException();
  }

  private DiscColor providerDiscToDiscColor(Disc disc) {
    if (disc == Disc.BLACK) {
      return DiscColor.BLACK;
    } else if (disc == Disc.WHITE) {
      return DiscColor.WHITE;
    } else {
      return DiscColor.NONE;
    }
  }

  /**
   * Converts the disc at the given tile to the provider's disc.
   *
   * @param tile the given tile at which the disc to convert is located
   * @return the provider's disc converted from the disc at the given tile
   * @throws IllegalArgumentException if the given tile is <code>null</code>
   */
  private Disc discAtTileToProviderDisc(Point tile) throws IllegalArgumentException {
    if (tile == null) {
      throw new IllegalArgumentException("Given tile cannot be null.");
    } else if (!this.adaptee.getBoard().containsKey(tile)) {
      return Disc.NONEXISTENT;
    }

    cs3500.reversi.main.model.Disc disc = this.adaptee.getDisc(tile);

    if (disc.getDiscColor() == DiscColor.BLACK) {
      return Disc.BLACK;
    } else if (disc.getDiscColor() == DiscColor.WHITE) {
      return Disc.WHITE;
    } else {
      return Disc.EMPTY;
    }
  }

  /**
   * Converts the given provider's point coordinate to a tile coordinate.
   *
   * @param coord the given provider's point coordinate to convert
   * @return the tile coordinate converted from the given provider's point coordinate
   * @throws IllegalArgumentException if the given provider's point coordinate is <code>null</code>
   */
  private Point providerCoordToTileCoord(Point coord) throws IllegalArgumentException {
    if (coord == null) {
      throw new IllegalArgumentException("Given point coordinate cannot be null.");
    }

    int y = this.adaptee.getGameBoardSideLength() - 1 - coord.y;
    int x = (int) (coord.x - y - coord.y - Math.ceil(y / 2.0));
    return new Point(x, y);
  }
}
