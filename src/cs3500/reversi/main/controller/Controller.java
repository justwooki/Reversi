package cs3500.reversi.main.controller;

import java.awt.Point;
import java.util.Optional;

import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.player.Player;
import cs3500.reversi.main.strategy.Move;
import cs3500.reversi.main.view.graphical.GraphicalFrameView;

/**
 * The controller for the Reversi game.
 *
 * <p>For more details on the responsibilities of the controller, see {@link IController}.</p>
 */
public final class Controller implements IController {
  private final MutableReversiModel model;
  private final GraphicalFrameView view;
  private final Player player;
  private final Disc color;

  /**
   * Constructs a Controller object for the Reversi game for a given player using the given model,
   * player, and view of the game.
   *
   * @param model the mutable reversi model to be controlled
   * @param player the player associated with the controller
   * @param view the view associated with the controller
   * @throws IllegalArgumentException if the given model, player, or view is <code>null</code>
   */
  public Controller(MutableReversiModel model, Player player, GraphicalFrameView view)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    } else if (player == null) {
      throw new IllegalArgumentException("The player cannot be null.");
    } else if (view == null) {
      throw new IllegalArgumentException("The view cannot be null.");
    }

    this.model = model;
    this.player = player;
    this.view = view;
    this.view.setFeature(this);
    this.model.addFeatures(this);
    this.color = new Disc();
  }

  @Override
  public void makeMove(Point tile) {
    if (tile != null && canMove()) {
      try {
        this.model.placeDisc(tile);
        this.view.updateView();
      } catch (IllegalStateException e) {
        this.view.displayErrorMessage(String.format("Illegal move for player Player%d",
                this.color.getDiscColor().equals(Disc.DiscColor.BLACK) ? 1 : 2));
      }
    }
  }

  @Override
  public void pass() {
    if (canMove()) {
      this.model.passTurn();
    }
  }

  @Override
  public Disc getPlayerDisc() {
    return this.color;
  }

  @Override
  public void notifyGameStarted(Disc color) {
    this.color.makeDiscExisting(color.getDiscColor());
    this.view.updateView();
  }

  @Override
  public void notifyModelStateUpdated() {
    this.view.updateView();
  }

  @Override
  public void notifyTurnUpdated() {
    if (canMove()) {
      Optional<Move> playerMove = this.player.selectMove();
      if (playerMove.isPresent()) {
        if (playerMove.get().getTile().isEmpty()) {
          pass();
        } else {
          makeMove(playerMove.get().getTile().get());
        }
      }
    }
  }

  /**
   * Determines whether it is currently the player's turn to move.
   *
   * @return <code>true</code> if it's the player's turn to move and <code>false</code> otherwise
   */
  private boolean isCurrentTurn() {
    return this.color != null && this.color.equals(this.model.getTurn());
  }

  /**
   * Determines whether the player can move. A player can move only if the game isn't over, and
   * it's their turn to move
   *
   * @return <code>true</code> if the player can move and <code>false</code> otherwise
   */
  private boolean canMove() {
    return !this.model.gameOver() && isCurrentTurn();
  }
}
