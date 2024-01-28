package cs3500.reversi.main.view.textual;

import java.awt.Point;
import java.util.Map;

import cs3500.reversi.main.model.BasicReversi;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * A simple text-based rendering of a hexagonal Reversi game.
 *
 * @see BasicReversi
 */
public final class HexagonalReversiTextualView implements TextView {
  private final ROReversiModel model;

  /**
   * Constructs a HexagonalReversiTextualView object given a hexagonal Reversi model.
   *
   * @param model the model
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public HexagonalReversiTextualView(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.model = model;
  }

  @Override
  public String toString() {
    Map<Point, Disc> gameBoard = this.model.getBoard();
    int boardSideLength = this.model.getGameBoardSideLength();

    // builds the new string to add
    StringBuilder boardString = new StringBuilder();

    // build 2D List of String
    // deal with top half first
    int yCount = 1;

    // iterates through the rows
    for (int y = boardSideLength - 1; y >= 0; y--) {

      // initial spacing
      boardString.append(addStringSpaceTop(yCount));

      // adds the individual "tiles"
      for (int x = 1 - boardSideLength; x < yCount; x++) {
        boardString.append(discToString(gameBoard.get(new Point(x, y))));
        boardString.append(" ");
      }
      yCount++;
      // gets rid of the appended space from adding tiles
      boardString.deleteCharAt(boardString.length() - 1);

      // converts into string and moves to next line
      boardString.append("\n");
    }
    yCount = 1;
    // deal with bottom half after
    for (int y = -1; y > -boardSideLength; y--, yCount++) {
      boardString.append(addStringSpaceBottom(yCount));
      for (int x = 1 - boardSideLength + yCount; x < boardSideLength; x++) {
        boardString.append(discToString(gameBoard.get(new Point(x, y))));
        boardString.append(" ");
      }

      // gets rid of the appended space from adding tiles
      boardString.deleteCharAt(boardString.length() - 1);

      // converts into string and moves to next line
      boardString.append("\n");
    }

    // removes the trailing whitespace
    return boardString.toString().replaceFirst("\\s++$", "");
  }

  private String addStringSpaceTop(int row) {
    StringBuilder spaceHolder = new StringBuilder();
    for (int i = 0; i < model.getGameBoardSideLength() - row; i++) {
      spaceHolder.append(" ");
    }
    return spaceHolder.toString();
  }

  private String addStringSpaceBottom(int row) {
    StringBuilder spaceHolder = new StringBuilder();
    for (int i = 0; i < row; i++) {
      spaceHolder.append(" ");
    }
    return spaceHolder.toString();
  }

  /**
   * Converts a {@link Disc} object to a {@link String} representation for the textual view. The
   * {@link String} representation depends on the value of the {@link Disc} object.
   * The symbol '_' is used for empty cells, 'X' for the black discs, and 'O' for the white discs.
   *
   * @param disc the disc to convert
   * @return '_' if the disc doesn't exist, 'X' if the disc is black colored, and 'O' if the disc
   *         is white colored
   */
  private String discToString(Disc disc) {
    if (!disc.hasDisc()) {
      return "_";
    } else if (disc.getDiscColor() == Disc.DiscColor.BLACK) {
      return "X";
    } else {
      return "O";
    }
  }
}