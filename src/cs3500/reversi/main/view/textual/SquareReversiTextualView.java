package cs3500.reversi.main.view.textual;

import java.awt.Point;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.SquareReversi;

/**
 * A simple text-based rendering of a square Reversi game.
 *
 * @see SquareReversi
 */
public class SquareReversiTextualView implements TextView {
  private final ROReversiModel model;

  /**
   * Constructs a SquareReversiTextualView object given a square Reversi model.
   *
   * @param model the model
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  public SquareReversiTextualView(ROReversiModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    }

    this.model = model;
  }

  @Override
  public String toString() {
    Map<Point, Disc> gameBoard = this.model.getBoard();
    int boardSideLength = this.model.getGameBoardSideLength();
    String[][] boardString = new String[boardSideLength][boardSideLength];

    for (Point tile : gameBoard.keySet()) {
      boardString[tile.y][tile.x] = discToString(gameBoard.get(tile));
    }

    return Arrays.stream(boardString).map(row -> String.join(" ", row))
            .collect(Collectors.joining("\n"));
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
