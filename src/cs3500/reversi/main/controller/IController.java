package cs3500.reversi.main.controller;

/**
 * Interface for the controller of a Reversi game. Every controller should subscribe itself as an
 * observer to both the model and view to be notified when certain model and view events occur.
 * Each controller represents the game control for one player and is responsible for playing the
 * game through that player.
 *
 * @see ModelFeatures
 * @see ViewFeatures
 */
public interface IController extends ModelFeatures, ViewFeatures {
}
