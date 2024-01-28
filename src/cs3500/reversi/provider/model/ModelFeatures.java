package cs3500.reversi.provider.model;

/**
 * Represents the features that a model should have in a game of Reversi.
 */
public interface ModelFeatures extends ReadonlyReversiModel {

  /**
   * Notifies the current player that it's their turn to make a move.
   */
  void notifyCurrentPlayer();
}
