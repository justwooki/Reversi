package cs3500.reversi.main.view.graphical;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;

import cs3500.reversi.main.controller.ViewFeatures;
import cs3500.reversi.main.model.Disc;
import cs3500.reversi.main.model.ROReversiModel;

/**
 * A graphics-based rendering of the Reversi game. This class utilizes Java Swing to act as the
 * main JFrame view of the game.
 */
public final class JFrameView extends JFrame implements GraphicalFrameView {
  private final ROReversiModel model;
  private final AbstractGameBoardPanel gameBoardPanel;
  private ViewFeatures viewListener;
  private final JLabel gameInfo;

  /**
   * Constructs a JFrameView object that sets up the frame, listeners, and other elements
   * of the GUI based on the given unmodifiable model of the Reversi game and the panel.
   *
   * @param model the read-only model of the Reversi game
   * @param gameBoardPanel the game board panel
   * @throws IllegalArgumentException if the given model is <code>null</code> OR the given panel
   *                                  is <code>null</code>
   */
  public JFrameView(ROReversiModel model, AbstractGameBoardPanel gameBoardPanel)
          throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("The given model cannot be null.");
    } else if (gameBoardPanel == null) {
      throw new IllegalArgumentException("The given panel cannot be null.");
    }

    setSize(800, 800);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    this.model = model;
    this.viewListener = null;
    this.gameBoardPanel = new HintsDecorator(gameBoardPanel);
    this.gameBoardPanel.setPreferredSize(new Dimension(800, 800));
    getContentPane().add(this.gameBoardPanel, BorderLayout.CENTER);

    this.gameInfo = new JLabel();
    this.gameInfo.setHorizontalAlignment(JLabel.CENTER);
    JPanel gameStatusPanel = new JPanel();
    gameStatusPanel.add(this.gameInfo);
    getContentPane().add(gameStatusPanel, BorderLayout.NORTH);

    setTitle("Reversi");
    setVisible(true);

    // set hotkeys
    this.gameBoardPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "setMove");
    this.gameBoardPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0),
            "setPass");
    this.gameBoardPanel.calcTileSideLength();
  }


  @Override
  public void setFeature(ViewFeatures feature) throws IllegalArgumentException {
    if (feature == null) {
      throw new IllegalArgumentException("The feature cannot be null.");
    }

    this.viewListener = feature;
    this.gameBoardPanel.setFeature(feature);
  }

  @Override
  public void updateView() {
    this.gameBoardPanel.repaint();
    displayGameStats();
  }

  @Override
  public void displayErrorMessage(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("The error message to display cannot be null.");
    }

    JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays a message regarding the current game status. Such information includes the name of
   * the game, Reversi, the player whose view it is, whose turn it is if the game is still going,
   * and the winner if the game is over.
   */
  private void displayGameStats() {
    if (this.viewListener != null) {
      StringBuilder displayMessage = new StringBuilder()
              .append(String.format("Reversi -- Player %d",
                      this.viewListener.getPlayerDisc().getDiscColor().equals(Disc.DiscColor.BLACK)
                              ? 1 : 2));
      if (this.model.gameOver()) {
        displayMessage.append(" -- Game Over: ");
        switch (this.model.getWinner().getDiscColor()) {
          case BLACK:
            displayMessage.append("Winner - BLACK");
            break;
          case WHITE:
            displayMessage.append("Winner - WHITE");
            break;
          default:
            displayMessage.append("TIE");
            break;
        }
      } else if (this.model.getTurn().equals(this.viewListener.getPlayerDisc())) {
        displayMessage.append(" -- Your turn to move");
      } else {
        displayMessage.append(" -- Waiting for other player to move");
      }
      displayMessage.append(String.format(" -- Black: %d, White: %d",
              this.model.getScore(new Disc(Disc.DiscColor.BLACK)),
              this.model.getScore(new Disc(Disc.DiscColor.WHITE))));

      this.gameInfo.setText(displayMessage.toString());
    }
  }
}
