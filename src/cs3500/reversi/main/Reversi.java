package cs3500.reversi.main;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import cs3500.reversi.main.controller.Controller;
import cs3500.reversi.main.controller.IController;
import cs3500.reversi.main.model.BasicReversi;
import cs3500.reversi.main.model.MutableReversiModel;
import cs3500.reversi.main.model.ROReversiModel;
import cs3500.reversi.main.model.SquareReversi;
import cs3500.reversi.main.player.AIPlayer;
import cs3500.reversi.main.player.HumanPlayer;
import cs3500.reversi.main.player.Player;
import cs3500.reversi.main.strategy.infallible.AvoidCornerAdjacencyMaxScore;
import cs3500.reversi.main.strategy.infallible.CaptureMostPieces;
import cs3500.reversi.main.strategy.infallible.CherryPickerCMSOptimizer;
import cs3500.reversi.main.strategy.infallible.OptimizeCornerStratMaxScore;
import cs3500.reversi.main.strategy.infallible.PlayCornersMaxScore;
import cs3500.reversi.main.strategy.infallible.ProviderReversiStrategyToInfallibleReversiStrategyAdapter;
import cs3500.reversi.main.view.graphical.AbstractGameBoardPanel;
import cs3500.reversi.main.view.graphical.GraphicalFrameView;
import cs3500.reversi.main.view.graphical.HexagonalBoardPanel;
import cs3500.reversi.main.view.graphical.JFrameView;
import cs3500.reversi.main.view.graphical.SquareBoardPanel;
import cs3500.reversi.provider.hexagonalreversistrategies.HexagonalStrategyAvoidNextToCorners;
import cs3500.reversi.provider.hexagonalreversistrategies.HexagonalStrategyGoForCorners;
import cs3500.reversi.provider.hexagonalreversistrategies.HexagonalStrategyMostPointsGreedy;

/**
 * Game driver class for the Reversi game.
 */
public final class Reversi {

  /**
   * Main method for running the entire Reversi game.
   *
   * @param args arguments for specifying the players who are playing the game
   */
  public static void main(String[] args) {
    // initialize model
    MutableReversiModel model = getModel(args, 0, 1,
            "Hexagonal", 6);

    // initialize views
    GraphicalFrameView viewPlayer1 = new JFrameView(model,
            getPanel(model, args, 0, new HexagonalBoardPanel(model)));
    GraphicalFrameView viewPlayer2 = new JFrameView(model,
            getPanel(model, args, 0, new HexagonalBoardPanel(model)));

    // initialize player options
    Map<String, Player> playerOptions = getPlayerOptions(model);

    // initialize players
    Player player1 = getPlayer(playerOptions, args, 2,
            playerOptions.get("CaptureMostPieces"));
    Player player2 = getPlayer(playerOptions, args, 3,
            playerOptions.get("CaptureMostPieces"));

    // initialize controllers
    IController controller1 = new Controller(model, player1, viewPlayer1);
    IController controller2 = new Controller(model, player2, viewPlayer2);

    // start the game
    model.startGame();
  }

  /**
   * Gets model options that can be used to initialize models. Each model option maps a model
   * String to a reference to the construction of a {@link MutableReversiModel} object. A list of
   * arguments that may consist of the String representation of the game board side length is
   * given. The possible location in the list of arguments where the game board side length may be
   * found is given. If no proper value can be found for the game board side length for whatever
   * reason, a given default value will take its place.
   *
   * @param args list of arguments that may consist of a String representation of the game board
   *             side length
   * @param index the possible location in the given list of arguments where the game board side
   *              length may be found
   * @param defaultValue the default game board side length to use if no proper game board side
   *                     length can be found by the given arguments
   * @return all model options
   * @throws IllegalArgumentException if the given list of arguments is <code>null</code>
   */
  private static Map<String, Supplier<MutableReversiModel>>
      getModelOptions(String[] args, int index, int defaultValue)
          throws IllegalArgumentException {
    if (args == null) {
      throw new IllegalArgumentException("Given arguments cannot be null.");
    }

    int gameBoardSideLength;
    if (index < 0 || index >= args.length) {
      gameBoardSideLength = defaultValue;
    } else {
      try {
        gameBoardSideLength = Integer.parseInt(args[index]);
      } catch (NumberFormatException e) {
        gameBoardSideLength = defaultValue;
      }
    }

    Map<String, Supplier<MutableReversiModel>> modelOptions = new HashMap<>();
    int finalGameBoardSideLength = gameBoardSideLength;
    modelOptions.put("Hexagonal", () -> new BasicReversi(finalGameBoardSideLength));
    modelOptions.put("Square", () -> new SquareReversi(finalGameBoardSideLength));

    return modelOptions;
  }

  /**
   * Initiates and returns a new Reversi game model based on the given arguments. A list of
   * arguments that may consist of a String representation of a model along with the side length of
   * its game board is given. The possible location in the list of arguments where the model String
   * and game board side length may be found is given. If no proper value can be found for the
   * model String or the game board side length for whatever reason, a given default value will
   * take its place.
   *
   * @param args list of arguments that may consist of a String representations of a model and its
   *             game board side length
   * @param modelIndex the possible location in the given list of arguments where the model String
   *                   may be found
   * @param boardSizeIndex the possible location in the given list of arguments where the game
   *                       board side length may be found
   * @param defaultModel the default model to use if no proper model can be found by the given
   *                     arguments
   * @param defaultBoardSize the default game board side length to use if no proper game board side
   *                         length can be found by the given arguments
   * @return an initialized model based either on the given list of arguments or the default
   *         arguments
   * @throws IllegalArgumentException if the given list of arguments is <code>null</code> OR the
   *                                  given default model is <code>null</code>
   */
  private static MutableReversiModel getModel(String[] args, int modelIndex, int boardSizeIndex,
                                              String defaultModel, int defaultBoardSize)
          throws IllegalArgumentException {
    if (args == null) {
      throw new IllegalArgumentException("Given arguments cannot be null.");
    } else if (defaultModel == null) {
      throw new IllegalArgumentException("The default model cannot be null.");
    }

    Map<String, Supplier<MutableReversiModel>> modelOptions = getModelOptions(args, boardSizeIndex,
            defaultBoardSize);

    if (modelIndex < 0 || modelIndex >= args.length) {
      return modelOptions.get(defaultModel).get();
    }

    Supplier<MutableReversiModel> model = modelOptions.get(args[modelIndex]);

    try {
      return model == null ? modelOptions.get(defaultModel).get() : model.get();
    } catch (IllegalStateException e) {
      return modelOptions.get(defaultModel).get();
    }
  }

  /**
   * Gets a specific game board panel based on the given arguments. The model that the panel will
   * represent is given. A list of arguments that may consist of a String representation of a panel
   * is given. The possible location in the list of arguments where the panel String may be found
   * is given. If no proper value can be found for the game board panel for whatever reason, a
   * given default value will take its place.
   *
   * @param model the model that the panel will represent
   * @param args list of arguments that may consist of a String representation of a panel
   * @param index the possible location in the given list of arguments where the panel String may
   *              be found
   * @param defaultValue the default panel to use if no proper game board panel can be found by
   *                     the given arguments
   * @return a game board panel based either on the given list of arguments or the default argument
   * @throws IllegalArgumentException if the given list of arguments is <code>null</code> OR the
   *                                  given default value to return is <code>null</code>
   */
  private static AbstractGameBoardPanel getPanel(MutableReversiModel model, String[] args,
                                                 int index, AbstractGameBoardPanel defaultValue)
          throws IllegalArgumentException {
    if (args == null) {
      throw new IllegalArgumentException("Given arguments cannot be null.");
    } else if (defaultValue == null) {
      throw new IllegalArgumentException("The default value cannot be null.");
    }

    Map<String, AbstractGameBoardPanel> panelOptions = new HashMap<>();
    panelOptions.put("Hexagonal", new HexagonalBoardPanel(model));
    panelOptions.put("Square", new SquareBoardPanel(model));

    if (index < 0 || index >= args.length) {
      return defaultValue;
    }

    AbstractGameBoardPanel panel = panelOptions.get(args[index]);
    return panel == null ? defaultValue : panel;
  }

  /**
   * Gets player options that can be used to initialize players. Each player option maps a player
   * String to the actual {@link Player} object.
   *
   * @param model a read-only Reversi model
   * @return the player options
   * @throws IllegalArgumentException if the given model is <code>null</code>
   */
  private static Map<String, Player> getPlayerOptions(ROReversiModel model)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }

    Map<String, Player> playerOptions = new HashMap<>();

    // human player
    playerOptions.put("Human", new HumanPlayer());

    // AI players using strategies from main package
    playerOptions.put("CaptureMostPieces", new AIPlayer(new CaptureMostPieces(), model));
    playerOptions.put("PlayCornersMaxScore", new AIPlayer(new PlayCornersMaxScore(), model));
    playerOptions.put("AvoidCornerAdjacencyMaxScore",
            new AIPlayer(new AvoidCornerAdjacencyMaxScore(), model));
    playerOptions.put("OptimizeCornerStratMaxScore",
            new AIPlayer(new OptimizeCornerStratMaxScore(), model));
    playerOptions.put("CherryPickerCMSOptimizer",
            new AIPlayer(new CherryPickerCMSOptimizer(), model));

    // AI players using provided strategies from provider package
    playerOptions.put("HexagonalStrategyAvoidNextToCorners",
            new AIPlayer(new ProviderReversiStrategyToInfallibleReversiStrategyAdapter(
                    new HexagonalStrategyAvoidNextToCorners()), model));
    playerOptions.put("HexagonalStrategyGoForCorners",
            new AIPlayer(new ProviderReversiStrategyToInfallibleReversiStrategyAdapter(
                    new HexagonalStrategyGoForCorners()), model));
    playerOptions.put("HexagonalStrategyMostPointsGreedy",
            new AIPlayer(new ProviderReversiStrategyToInfallibleReversiStrategyAdapter(
                    new HexagonalStrategyMostPointsGreedy()), model));

    return playerOptions;
  }

  /**
   * Gets a specific player based on the given arguments. The player options that map a String
   * representation of a player to the actual player is given. A list of arguments that may consist
   * of String representations of players is given. The possible location in the list of arguments
   * where the player String may be found is given. If no proper value can be found for the
   * player for whatever reason, a given default value will take its place.
   *
   * @param playerOptions player options that map a String representation of a player to the actual
   *                      player
   * @param args list of arguments that may consist of String representations of players
   * @param index the possible location in the given list of arguments where the player String may
   *              be found
   * @param defaultValue the default player to use if no proper player can be found by the given
   *                     arguments
   * @return a player based either on the given list of arguments or the default argument
   * @throws IllegalArgumentException if the given player options is <code>null</code> OR the given
   *                                  list of arguments is <code>null</code> OR the given default
   *                                  value to return is <code>null</code>
   */
  private static Player getPlayer(Map<String, Player> playerOptions, String[] args, int index,
                                  Player defaultValue) throws IllegalArgumentException {
    if (playerOptions == null) {
      throw new IllegalArgumentException("Player options cannot be null.");
    } else if (args == null) {
      throw new IllegalArgumentException("Given arguments cannot be null.");
    } else if (defaultValue == null) {
      throw new IllegalArgumentException("The default value cannot be null.");
    } else if (index < 0 || index >= args.length) {
      return defaultValue;
    }

    Player player = playerOptions.get(args[index]);
    return player == null ? defaultValue : player;
  }
}
