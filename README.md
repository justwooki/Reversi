# Reversi
    created by Renee Cai and Justin Kim
### Table of Contents
* [Reversi](#reversi)
    * [Reversi Overview](#reversi-overview)
    * [Quick Start](#quick-start)
    * [Key Components](#key-components)
    * [Key Subcomponents](#key-subcomponents)
    * [Changes Made for Part 2](#changes-made-for-part-2)
    * [Changes Made for Part 3](#changes-made-for-part-3)
    * [Changes Made for Part 4](#changes-made-for-part-4)
    * [What we got working successfully from provider
  code](#what-we-got-working-successfully-from-provider-code)
    * [Changes Made for Part 5](#changes-made-for-part-5)
    * [Credits](#credits)
<!-- TOC -->

## Reversi Overview
Reversi is a strategy board game. In this version, Reversi Hexagonal is a board game that is
played on a hexagonal board of *n* side length.

A game is initially set up with a ring of 6 black and white discs of equal number arranged in the 
middle of the grid.

Per turn, a player may do one of two things:
1) They may select an empty tile and *play* a disc in that tile. 
2) They may *pass*, and let the other player make a **legal** move.

A legal move is defined by:
- The disc played must be adjacent (share a side) to a straight line of the other players' discs,
in which at the far end is a disc from the current player. There must be no empty tiles in the 
straight line.

Once a move is played, all the discs in the straight line between the disc placed and the disc at 
the end of the line will flip to the color assigned to the current player. This is called 
*capturing* the other players' discs. If the play made completes a line in two directions such that
the end of other lines has only the other player's discs in between and ends with the current
player's disc, the discs on both lines will be captured.

Coordinate System used: The center hexagon has a coordinate of (0,0).
  - In the direction of top-left/bottom-right, both x and y are incremented.
  - In the direction of left-right, only the x-value is incremented.
  - In the direction of top-right/bottom-left, only the y-value is incremented.

If a player has no more legal moves, they *must* pass. 

If both players pass, the game **ends**. 

[Back to the top](#reversi)

## Quick Start
This quick start is a friendly introduction to the Reversi project codebase for newcomers. It will
walk through which parts of the project are most relevant to take a look at, and in what order.
This is to help newcomers quickly understand how this codebase works, but if you prefer to just hop
around, that is by all means completely fine. With that being said, here are the contents this
quick start will cover:
* [Quick Start](#quick-start)
  * [Details on overall game logic and rules](#details-on-overall-game-logic-and-rules)
  * [Game logic and rules in action](#game-logic-and-rules-in-action)
  * [Rendering the game](#rendering-the-game)
  * [Connecting the model and view together](#connecting-the-model-and-view-together)
  * [A new codebase](#a-new-codebase)
  * [Running the program](#running-the-program)

### Details on overall game logic and rules
Details on the model that make up the overall game logic and rules can be found in the
<span style="color:SlateBlue">**cs3500.reversi.main.model**</span> package. Start from
the <span style="color:Tomato">**ROReversiModel**</span> interface and work your way down to the
<span style="color:Tomato">**MutableReversiModel**</span> interface and
<span style="color:Aqua">**AbstractReversi**</span>.
<span style="color:Aqua">**AbstractReversi**</span> is an abstract class that currently has two
classes that extending it: <span style="color:Aqua">**BasicReversi**</span> and
<span style="color:Aqua">**SquareReversi**</span>. Check both classes out. As you work your way
down, you may find implemented simple value-classes like <span style="color:Aqua">**Disc**</span>
to be helpful to check out.

[Back to quick start contents](#quick-start)

### Game logic and rules in action
Examples of game logic and rules in action can be found in the
<span style="color:Aqua">**ExamplesTest**</span> class.

Moves can be made with the <span style="color:DarkGoldenRod">**placeDisc(Point tile)**</span>
method.

    model.placeDisc(new Point(2, -1)); // black move
    model.placeDisc(new Point(-2, 1)); // white move
        ... additional moves
    model.placeDisc(new Point(-1, -1)); // black move

A move that swaps discs in two different lines *will* work

    model.placeDisc(new Point(1, -2)); // white move --> tests "double move

Any moves once the game is over will not be allowed.

    // Illegal Moves --> no moves left
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(-2, 2)));
        ... all other discs have no moves
    assertThrows(IllegalStateException.class, () -> model.placeDisc(new Point(2, 0)));

The game will be able to check whether disc is placeable or not.

    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.BLACK)));
    assertFalse(model.canPlaceDisc(new Disc(Disc.DiscColor.WHITE)));
    assertTrue(model.gameOver());


_Additionally_, a game can end within two consecutive passes:

    model.passTurn();
    model.passTurn();
    assertTrue(model.gameOver());

[Back to quick start contents](#quick-start)

### Rendering the game
There are generally two ways to render the game. The first is a textual render that was used for
testing the model to check that the game progresses properly. Other than that, the textual view
plays no significant role in the overall project. If you would like, you may check out the
<span style="color:Aqua">**HexagonalReversiTextualView**</span> and
<span style="color:Aqua">**SquareReversiTextualView**</span> classes in the
<span style="color:SlateBlue">**cs3500.reversi.main.view.textual**</span> package along with its
brief marker interface, <span style="color:Tomato">**TextView**</span>.

The second way to render the game is graphically via a GUI. Start by checking out the view in the
<span style="color:SlateBlue">**cs3500.reversi.main.view.graphical**</span> package. The view is
split into two parts: the frame and the panel. The panel is the component that actually displays
the current view that the user can see and interact with. The frame is the main view that wraps
around the panel. Start at the <span style="color:Tomato">**GraphicalFrameView**</span> frame
interface, and then check out its implementation in the
<span style="color:Aqua">**JFrameView**</span> class. Afterwards, check out the
<span style="color:Tomato">**GraphicalPanelView**</span> panel interface and its implementation in
the <span style="color:Aqua">**AbstractGameBoardPanel**</span> abstract class.
<span style="color:Aqua">**AbstractGameBoardPanel**</span> currently has two main panel classes
extending it: <span style="color:Aqua">**HexagonalBoardPanel**</span> and
<span style="color:Aqua">**SquareBoardPanel**</span>. Check both classes out. Additionally,
<span style="color:Aqua">**AbstractGameBoardPanel**</span> has a third class that extends it, the
<span style="color:Aqua">**HintsDecorator**</span> decorator class. Feel free to check that one out
as well.

[Back to quick start contents](#quick-start)

### Connecting the model and view together
The controller brings everything together by connecting the model and view. With all parts
connected, the game is finally playable. To understand how the controller works, check out the
<span style="color:Tomato">**IController**</span> controller interface in the
<span style="color:SlateBlue">**cs3500.reversi.main.controller**</span> package. After you
understand its purpose, move on to understand the
<span style="color:Tomato">**ModelFeatures**</span> and
<span style="color:Tomato">**ViewFeatures**</span> interfaces and how they allow the controller to
directly communicate with both the model and the view. Finally, take a look at the implementation
of the <span style="color:Aqua">**Controller**</span> class.

You may notice that the controller relies heavily on a player. Therefore, you may find it useful to
check out the <span style="color:Tomato">**Player**</span> interface along with its
implementations, <span style="color:Aqua">**HumanPlayer**</span> and
<span style="color:Aqua">**AIPlayer**</span> in the
<span style="color:SlateBlue">**cs3500.reversi.main.player**</span> package.

Additionally, you may find that an AIPlayer relies on a strategy to help pick its move. All
classes/interfaces relevant to Reversi strategies can be found in the
<span style="color:SlateBlue">**cs3500.reversi.main.strategy**</span> package. This package
contains two more packages that further organize all strategies into two groups:
<span style="color:SlateBlue">**fallible**</span> and
<span style="color:SlateBlue">**infallible**</span>. Start with the former. The
<span style="color:Tomato">**FallibleReversiStrategy**</span> interface will give you a good idea
of what a fallible strategy is. If you want, you may briefly check out all its implementations.
Afterwards, move on to the <span style="color:SlateBlue">**infallible**</span> package and check
out the <span style="color:Tomato">**InfallibleReversiStrategy**</span>. If you'd like, you may
also briefly check out its implementations. You may also want to take a look at the implemented
simple value class that all strategies use, <span style="color:Aqua">**Move**</span>, which is
located outside both the <span style="color:SlateBlue">**fallible**</span> and
<span style="color:SlateBlue">**fallible**</span> packages, but still within the
<span style="color:SlateBlue">**strategies**</span> package.

[Back to quick start contents](#quick-start)

### A new codebase
You may have noticed a <span style="color:SlateBlue">**cs3500.reversi.provider**</span> package
that exists separately from the <span style="color:SlateBlue">**main**</span> package we've been
taking a look at. This package represents a different codebase focusing on the same Reversi
project. Feel free to explore this packages. After taking a brief look, we can dive into the
adapter classes next.

The adapter classes are a series of classes scattered throughout the
<span style="color:SlateBlue">**cs3500.reversi.main**</span> package that focus on adapting parts
of the main codebase with the providers' codebase and vice versa. To understand why this part
exists, please refer to [code exchange](#code-exchange). To dive into each adapter class,
please refer to [implementation of adapter](#implementation-of-adapters), which will list all of
the adapters built. It is recommended you take a look at each adapter in the order that it
introduces you in.

[Back to quick start contents](#quick-start)

### Running the program
The driver for starting the entire Reversi game is found in the
<span style="color:Aqua">**Reversi**</span> class. This is the main class that includes the main
method that can be run. Here, the model, views, players, and controllers are initialized, and the
game is started. The main method takes in a total of four arguments. If there are any more
arguments provided, they will simply be ignored. If there aren't any arguments provided, the
arguments will be set to their default value. The first two arguments are for configuring the
model, and the last two configure the players. To customize arguments, you can simply edit
configurations to alter the arguments the main method takes in. For more details, see below.

There are two types of boards that can be currently played on - hexagonal and square boards. The
first argument specifies which of these two boards will be played. If the argument for the type of
board is missing, or if the argument is invalid in any way, the type of board will automatically
default to a hexagonal board. The arguments for selecting the board to be played on are as
described below:
- The argument for a hexagonal Reversi board is "Hexagonal"
- The argument for a square Reversi board is "Square"

The second argument specifies the size of the board. Take note of the size constraints for each
board. Additionally, take note that "size" is referring to the side length of the board's shape.
A square Reversi board of size 4 is simply a 4x4 square. Similarly, a hexagonal board of size 6 is
a hexagon with 6 tiles on each side. To specify the size, simply enter a number. If no argument is
given, or if the argument given isn't a valid number at all, the board size will automatically
default to 6.

In this two player game, two people can casually play against one another. If you don't have a
partner to play with, that is completely fine. There are a variety of AI players one may choose to
play against. The last two arguments configure the players of the game, each representing each
player playing the game. If there are any missing arguments, no arguments, or invalid arguments in
any way, the player will be set to an AI player that uses the
<span style="color:Aqua">**CaptureMostPieces**</span> strategy by default. The first player
argument given will get to move first at the start of the game. The arguments for selecting players
are as described below:
- The argument for a human player is "Human"
- The argument for an AI player using the CaptureMostPieces strategy is "CaptureMostPieces"
- The argument for an AI player using the PlayCornersMaxScore strategy is "PlayCornersMaxScore"
- The argument for an AI player using the AvoidCornerAdjacencyMaxScore strategy is
  "AvoidCornerAdjacencyMaxScore"
- The argument for an AI player using the OptimizeCornerStratMaxScore strategy is
  "OptimizeCornerStratMaxScore"
- The argument for an AI player using the CherryPickerCMSOptimizer strategy is
  "CherryPickerCMSOptimizer"
- The argument for an AI player using the provided HexagonalStrategyAvoidNextToCorners strategy is
  "HexagonalStrategyAvoidNextToCorners"
- The argument for an AI player using the provided HexagonalStrategyGoForCorners strategy is
  "HexagonalStrategyGoForCorners"
- The argument for an AI player using the provided HexagonalStrategyMostPointsGreedy strategy is
  "HexagonalStrategyMostPointsGreedy"

For more information on the model, board sizes, and variety of boards, check out the
<span style="color:SlateBlue">**cs3500.reversi.main.model**</span> package.

For more information on both human and AI players, check out the
<span style="color:SlateBlue">**cs3500.reversi.main.player**</span> package.

For more information on the specific AI strategies, check out the
<span style="color:SlateBlue">**cs3500.reversi.main.strategy.infallible**</span> and
<span style="color:SlateBlue">**cs3500.reversi.provider.hexagonalreversistrategies**</span>
packages.

Each argument will of course be separated by a single whitespace. An example of the arguments for a
configured game in which a human plays against a CaptureMostPieces AI on a square Reversi board of
size 4 would simply look like:
<pre>Square 4 Human CaptureMostPieces</pre>

This follows the format for what arguments are taken in what order as was described above. Here is
a template you can follow if you ever get lost:
<pre>[BoardType] [BoardSize] [Player1] [Player2]</pre>

*NOTE: All arguments are case-sensitive.

[Back to quick start contents](#quick-start)

[Back to the top](#reversi)

## Key Components

A list of key components for each package are listed here. Further details on each component can be
found in [Quick Start](#quick-start) or in the Javadocs of each individual component.

### Model Components
- <span style="color:Tomato">**ROReversiModel**</span>
- <span style="color:Tomato">**MutableReversiModel**</span>

### View Components
- <span style="color:Tomato">**GraphicalFrameView**</span>
- <span style="color:Tomato">**GraphicalPanelView**</span>

### Player Components
- <span style="color:Tomato">**Player**</span>

### Strategy Components
- <span style="color:Tomato">**FallibleReversiStrategy**</span>
- <span style="color:Tomato">**InfallibleReversiStrategy**</span>

### Controller Components
- <span style="color:Tomato">**ViewFeatures**</span>
- <span style="color:Tomato">**ModelFeatures**</span>
- <span style="color:Tomato">**IController**</span>

[Back to the top](#reversi)

## Key Subcomponents

A list of key subcomponents for each package are listed here. Further details on each component can
be found in [Quick Start](#quick-start) or in the Javadocs/implementation of each individual
component.

### Model Subcomponents
- <span style="color:Aqua">**AbstractReversi**</span>
- <span style="color:Aqua">**BasicReversi**</span>
- <span style="color:Aqua">**SquareReversi**</span>
- <span style="color:Aqua">**Disc**</span>

### View Subcomponents
- <span style="color:Aqua">**JFrameView**</span>
- <span style="color:Aqua">**HexagonalBoardPanel**</span>
- <span style="color:Aqua">**SquareBoardPanel**</span>

### Player Subcomponents
- <span style="color:Aqua">**Human**</span>
- <span style="color:Aqua">**AI**</span>

### Strategies Subcomponents
- <span style="color:Aqua">**Move**</span>

### Controller Subcomponents
- <span style="color:Aqua">**Controller**</span>

[Back to the top](#reversi)

## Changes Made for Part 2
See the DocumentationFile for further details.
The changes made to the Reversi game are as follows:

### Relevant additions made in the "model" package
- Added another constructor method that allowed us to make a direct copy of the model that can be 
independently played.
- Minor bug fix in <span style="color:DarkGoldenRod">**BasicReversi#canPlaceDisc**</span> that 
prevented it from determining whether a move could be made for the correct player.
- Added two new methods -
<span style="color:DarkGoldenRod">**ROReversiModel#getConsecutivePasses**</span> and
<span style="color:DarkGoldenRod">**ROReversiModel#getMaxNumConsecutivePassesAllowed**</span> - to
the read-only Reversi model. These additions provide visibility into the game status of Reversi by
offering information on the number of consecutive passes that have occurred and the maximum number
of consecutive passes allowed. This information is essential for anticipating when the game should
conclude.
- Added a new method - <span style="color:DarkGoldenRod">**MutableReversiModel#copyGame**</span> -
to the mutable Reversi model to allow for the ability to branch out different gameplay variations
from a specific game state by copying the entire model in its current state.
- Added a new field property representing the maximum number of consecutive passes allowed simply
because it was a necessary data to keep track of after adding the
<span style="color:DarkGoldenRod">**ROReversiModel#getMaxNumConsecutivePassesAllowed**</span>
method.

### Relevant additions made in the "view" package:
- GUI view added.
- <span style="color:Tomato">**GameView**</span> interface is the interface for a graphical-based 
  view, to be used for displaying the Reversi game board visually.
- <span style="color:Tomato">**GraphicalView**</span> interface is the interface for a 
  graphical-based view, to be used in the Reversi game. Its methods help tie user input into the 
  view, such as setting a hotkey.
- <span style="color:Aqua">**GameBoardPanel**</span> class is the class that represents a custom 
  Swing panel that visually displays the current game board for Reversi. In this class contains 
  the methods that build the individual hexagons, builds the entire grid. 
- <span style="color:Aqua">**JFrameView**</span> class is the class that sets up the frame, panel, 
  listeners, and other elements of the GUI based on the given unmodifiable model of the Reversi 
  game.

### Relevant additions made in the "controller" package:
- Built an unfinished version of the controller for testing the view.
- <span style="color:Tomato">**Features**</span> interface just represents various features and 
  abilities of the view. These features also represent the responsibilities the controller has to 
  uphold for the view.
- <span style="color:Aqua">**Controller**</span> class constructs a Controller object for the 
  Reversi game using the given model and view of the game.

*Note: We built a controller that may or may not be finished to test the view briefly and play
several games (for our own sake). This does not affect the rest of the code.*

### Relevant user information
To select a hexagon, use the mouse to click on it. If selected, the hexagon should light up blue. 
If the mouse clicks on the hexagon again, another hexagon, or somewhere out of bounds, it will
deselect it. Moves can only be made on selected hexagons. 
Once a hexagon is selected, there are two keyboard commands that control the moves:
- Pressing the **return** or **ENTER** key places a disc of the current player on the selected 
hexagon.
- Pressing **p** passes the current player's turn.
Once a move has been made, whether a disc was placed or a turn was passed, the hexagon will deselect
and return to its original color.

### Strategies 
A new package named <span style="color:SlateBlue">**strategy**</span> holds strategies of the 
Reversi game. Some strategies chain other strategies together into a more complex, sophisticated 
strategy.

Every strategy should return a <span style="color:Aqua">**Move**</span> that would maximize a
player's Reversi move based on the purpose of the strategy used. A
<span style="color:Aqua">**Move**</span> consists of either a
<span style="color:Aqua">**Hexagon**</span> on which a move should be made, or a pass, in which the
turn is simply passed.

All strategies are split into two 
groups: <span style="color:Tomato">**FallibleReversiStrategy**</span> and
<span style="color:Tomato">**InfallibleReversiStrategy**</span>. Generally speaking, fallible
strategies are strategies that may not produce a guaranteed move given that it cannot find one.
Infallible strategies are strategies that are guaranteed to always return a move. More details on
these two interfaces can be found in their Javadocs.

Strategy Descriptions can be found under the DocumentationFile and/or in JavaDoc.

### Mocks
In the <span style="color:SlateBlue">**tests**</span> package, a series of mock classes have been 
built for testing purposes. To learn more about each mock class, their purpose, and how they may 
be used for testing, take a look at their JavaDoc.

[Back to the top](#reversi)

## Changes Made for Part 3

### Player
The player interface finally got implemented. Every player has the ability to choose a move to
make. For more details on players overall, please refer to the JavaDoc. There is an AI and human
player class that implements this interface. For more details on how each of these players
operates, please refer to the JavaDoc.

### Controller
In the controller package, a <span style="color:Tomato">**ModelFeatures**</span> interface has been 
added to represent model listeners. The Features interface representing view listeners has been 
renamed to <span style="color:Tomato">**ViewFeatures**</span> to add more detail to the naming. The 
<span style="color:Tomato">**IController**</span> interface has been added to act as an interface 
for all controllers. The <span style="color:Aqua">**Controller**</span> class has been updated to 
support these changes. The controller has also been updated such that each controller will 
represent only one single player. For more details on the controller overall, please refer to the 
JavaDoc for the following interfaces/classes(preferably in this order): 
<span style="color:Tomato">**ViewFeatures**</span>, 
<span style="color:Tomato">**ModelFeatures**</span>, 
<span style="color:Tomato">**IController**</span>, 
<span style="color:Aqua">**Controller**</span>.

### Relevant additions made in the "view" package:
- Two new subpackages - <span style="color:SlateBlue">**graphical**</span> and 
  <span style="color:SlateBlue">**textual**</span> - added to the 
<span style="color:SlateBlue">**view**</span> package for better organization.
- <span style="color:Aqua">**AbstractGameBoardPanel**</span> class added as an abstract class to 
support multiple game panel implementations.
- <span style="color:Aqua">**JFrameView**</span> updated to use 
<span style="color:Aqua">**AbstractGameBoard**</span> as its main panel field component rather than
  <span style="color:Tomato">**JFrameView**</span>.
- Three new methods introduced in the <span style="color:Aqua">**GraphicalFrameView**</span> class. 
The <span style="color:Aqua">**JFrameView**</span> class has been
updated to support these changes. For more information on these methods, please refer to the
JavaDoc.
  - <span style="color:DarkGoldenRod">**updateView()**</span> - allows for the view to update itself
  for any changes made
  - <span style="color:DarkGoldenRod">**displayMessage(String message)**</span> - allows for a 
  message to be displayed in the view
  - <span style="color:DarkGoldenRod">**displayErrorMessage(String message)**</span> - allows for 
  an error message to be displayed in the view

### Relevant additions made in the "model" package:
- The <span style="color:DarkGoldenRod">**copyGame()**</span> method from 
<span style="color:Tomato">**MutableReversiModel**</span> has been moved to 
<span style="color:Tomato">**ROReversiModel**</span> since the method
doesn't mutate the model in any way and should therefore belong in the read-only model.
- The access modifier for the second constructor for 
<span style="color:Aqua">**BasicReversi**</span> that allows the model to be built based off 
another model has been updated from public to protected. The reason behind this
change is that there already exists a <span style="color:DarkGoldenRod">**copyGame()**</span> 
method that allows the model to be copied. This
can be used instead over the constructor. The constructor cannot be removed though since the
implementation for <span style="color:DarkGoldenRod">**copyGame()**</span> is dependent on this 
constructor. Therefore, the second constructor was made protected rather than being deleted 
altogether.
- To support model listeners, a new field representing all model listeners has been added.
Additionally, new methods have been added to the
<span style="color:Tomato">**MutableReversiModel**</span> interface, and 
<span style="color:Aqua">**BasicReversi**</span> was updated to support these changes. The methods 
added are listed below, but details on them can be found in the JavaDoc:
  - <span style="color:DarkGoldenRod">**addFeatures(ModelFeatures features)**</span>
  - <span style="color:DarkGoldenRod">**startGame()**</span>
- Other changes throughout <span style="color:Aqua">**BasicReversi**</span> to support model 
listeners include:
  - Notifying all model listeners when the game started.
  - Notifying all model listeners whenever the model/game has been mutated.
  - Notifying all model listeners whenever the turn has changed.

### Relevant additions made in the "strategy" package:
- Two new subpackages = <span style="color:SlateBlue">**fallible**</span> and 
<span style="color:SlateBlue">**infallible**</span> - added to the 
<span style="color:SlateBlue">**fallible**</span> package for better organization.
- The <span style="color:Aqua">**InfallibleReversiStrategy**</span> class has been changed into an 
interface to support a variety of named infallible strategies that can be referred to.
- Multiple fallible strategies have been converted to infallible strategies. This includes
  <span style="color:Aqua">**AvoidCornerAdjacencyMaxScore**</span>, 
<span style="color:Aqua">**CaptureMostPieces**</span>, 
<span style="color:Aqua">**CherryPickerCMSOptimizer**</span>,
<span style="color:Aqua">**OptimizerCornerStratMaxScore**</span>, and 
<span style="color:Aqua">**PlayCornersMaxScore**</span>.
- All strategy interfaces/classes have been updated to utilize a 
<span style="color:Tomato">**ROReversiModel**</span> rather than a 
<span style="color:Tomato">**MutableReversiModel**</span>. This change was believed to be necessary 
since the strategy classes should be picking a move to play, not necessarily making the move itself 
or mutating the model in any way.
- All strategy classes have been updated to be final.

[Back to the top](#reversi)

## Changes Made for Part 4

### Code exchange
We were tasked to take in another codebase from another Reversi game implementation and make their
view and strategies work with our code. Our providers gave us access to their interfaces regarding
their model, game board, controller, view, players, and strategy. We were also given the
implementation details regarding their view and strategy, since we have to directly adapt them to
our codebase. Additionally, we were given access to their enum classes such as
<span style="color:Aqua">**Direction**</span> and <span style="color:Aqua">**Disc**</span>.

### Changes in package organization
To separate the main codebase from the providers' codebase, all entities in the
<span style="color:SlateBlue">**cs3500.reversi**</span> package have been divided into two distinct
packages - <span style="color:SlateBlue">**main**</span> for all the entities in the main codebase,
and <span style="color:SlateBlue">**provider**</span> for all the entities in the providers'
codebase.

### Implementation of adapters
In order to make the providers' view and strategies work with our main codebase, adapter classes
had to be built in order to adapt their classes/interfaces to ours. In doing so, we had to adapt
some of our classes/interfaces to theirs since their view and strategy classes depended on the
implementation of certain classes such as the model for which we had no implementation details of.
As a result, the following adapters were built:
- <span style="color:Aqua">**ProviderReversiStrategyToInfallibleReversiStrategyAdapter**</span>
  - Adapts the providers' <span style="color:Tomato">**ReversiStrategy**</span> interface to our
  <span style="color:Tomato">**InfallibleReversiStrategy**</span> interface.
- <span style="color:Aqua">**ProviderReversiViewToGraphicalFrameViewAdapter**</span>
  - Adapts the providers' <span style="color:Tomato">**ReversiView**</span> interface to our
  <span style="color:Tomato">**GraphicalFrameView**</span> interface.
- <span style="color:Aqua">**MutableReversiModelToProviderReversiModelAdapter**</span>
  - Adapts our <span style="color:Tomato">**MutableReversiModel**</span> interface to the
  providers' <span style="color:Tomato">**ReversiModel**</span> interface. This was necessary to
  adapt because both the <span style="color:Tomato">**ReversiStrategy**</span> and
  <span style="color:Tomato">**ReversiView**</span> interfaces from the providers, which we are
  trying to adapt, referenced <span style="color:Tomato">**ReversiModel**</span> in some way. Since
  we don't have implementation details of the class that implements this interface, an adapter had
  to be made.
- <span style="color:Aqua">**GameBoardToProviderReversiGameBoardAdapter**</span>
  - Adapts our game board that maps a hexagonal tile to a disc to the providers'
  <span style="color:Tomato">**ReversiGameBoard**</span> interface. Since our game board
  implementation is represented as a map data structure rather than an actual class/interface we
  aren't adapting a class/interface to another class/interface this time. This adaptation was
  necessary because the <span style="color:Tomato">**ReversiModel**</span> interface from the
  providers, which we are trying to adapt, referenced
  <span style="color:Tomato">**ReversiGameBoard**</span> in some way. Since we don't have
  implementation details of the class that implements this interface, an adapter had to be made.
- <span style="color:Aqua">**DiscToProviderReversiPlayerAdapter**</span>
  - Adapts our <span style="color:Aqua">**Disc**</span> class to the providers'
  <span style="color:Tomato">**ReversiPlayer**</span> interface. This adaptation was necessary
  because the <span style="color:Tomato">**ReversiModel**</span> interface from the providers,
  which we are trying to adapt, referenced <span style="color:Tomato">**ReversiPlayer**</span> in
  some way. Since we don't have implementation details of the class that implements this interface,
  an adapter had to be made. Additionally, because the providers'
  <span style="color:Tomato">**ReversiModel**</span> interface treats
  <span style="color:Tomato">**ReversiPlayer**</span> similarly to how we treat
  <span style="color:Aqua">**Disc**</span> in
  <span style="color:Tomato">**MutableReversiModel**</span>, the adapter should appropriately adapt
  our <span style="color:Aqua">**Disc**</span> class rather than our
  <span style="color:Tomato">**Player**</span> interface to the providers'
  <span style="color:Tomato">**ReversiPlayer**</span> interface.

### Relevant changes made in the "model" package
- Added <span style="color:DarkGoldenRod">**canPlaceDiscAtTile(Disc color, Hexagon tile)**</span>
method to <span style="color:Tomato">**ROReversiModel**</span> interface because it was a missing
observation that should be present in the model.
- The <span style="color:DarkGoldenRod">**addFeatures(ModelFeatures feature)**</span> method from
<span style="color:Tomato">**MutableReversiModel**</span> no longer returns an integer value. The
method of having <span style="color:DarkGoldenRod">**addFeatures**</span> return an integer to
allow model features label themselves was rather messy, so we decided to remove this behavior from
the method. Changes to the controller were made to adapt to this change (more details in "*Relevant
changes made in the 'controller' package*").
- Another change to the <span style="color:DarkGoldenRod">**addFeatures**</span> method was that
the model was only allowed to keep track of up to two model listeners. Since Reversi is a two
player game, it was unnecessary and unreasonable to have a third model listener when there were
only two players to listen to. It made it difficult for methods to treat the model listeners as if
there were more than two when we couldn't come up with any reason why there would be any more than
two listeners in the first place. Changes to the
<span style="color:DarkGoldenRod">**startGame()**</span> method implementation in the
<span style="color:Aqua">**BasicReversi**</span> class were made to adjust to this change.

### Relevant changes made in the "view" package
- The <span style="color:DarkGoldenRod">**addFeatures(ViewFeatures feature)**</span> method in both
the <span style="color:Tomato">**GraphicalPanelView**</span> and
<span style="color:Tomato">**GraphicalFrameView**</span> interfaces was changed to set a given
view listener rather than add one to a list of view listeners. It only made sense for there to be
only one view listener rather than multiple. After all, there are only two players, each of which
have their own separate view rather than sharing one. Changes were made to the classes that
implemented both of these view classes such that they would only keep track of one view listener
rather than a list of them. Additionally, the name of the method was changed to
<span style="color:DarkGoldenRod">**setFeature(ViewFeatures feature)**</span> for more accurate
naming.
- We thought details such as user-view interaction and the way the view displays itself was a
responsibility that belonged solely to the view, so the ability to set hotkeys and display its own
message was moved from the controller to the view. Since these abilities exist solely in the view,
the <span style="color:DarkGoldenRod">**setHotKey**</span> and
<span style="color:DarkGoldenRod">**displayMessage**</span> methods from the
<span style="color:Tomato">**GraphicalFrameView**</span> interface were removed since there was no
longer any need to reference them.
- A private method, <span style="color:DarkGoldenRod">**displayGameStats()**</span> was added in
the <span style="color:Aqua">**JFrameView**</span> class to give the view the ability to display
its own current game status. This private method gets called in
<span style="color:DarkGoldenRod">**updateView**</span>.
- To prepare for the eventual addition of decorator panel classes, which will be used to add new
features to the view without modifying the original code, specific private fields and methods have
been moved from <span style="color:Aqua">**GameBoardPanel**</span> to
<span style="color:Aqua">**AbstractGameBoardPanel**</span>. In order for any child class to have
access to these fields and helper methods, their access modifiers have been changed from *private*
to *protected*. Fields that have been moved include
<span style="color:Lime">**selectedHexagon**</span>. Methods that have been moved include
<span style="color:DarkGoldenRod">**hexGridToPixelCoords(Hexagon hexagon)**</span>,
<span style="color:DarkGoldenRod">**selectHexagon(int x, int y)**</span>,
<span style="color:DarkGoldenRod">**calcHexagonSideLength()**</span>, and
<span style="color:DarkGoldenRod">**buildHexagon(Hexagon hexagon)**</span>. Additionally, the setup
of event listeners has been moved as well from the
<span style="color:Aqua">**GameBoardPanel**</span> constructor to the
<span style="color:Aqua">**AbstractGameBoardPanel**</span> constructor and abstracted into its own
helper method, <span style="color:DarkGoldenRod">**setUpListeners()**</span>.
- To further prepare for the eventual addition of decorator panel classes, specific variables have
been abstracted from <span style="color:Aqua">**GameBoardPanel**</span> to the higher
<span style="color:Aqua">**AbstractGameBoardPanel**</span> class. A few examples of things that
were abstracted include things like the background color, the hexagon's border color, the stroke
width for the hexagon border, etc. Default values were given to these fields in the constructor,
but the point is that if a decorator wanted to change the style of how the view was presented
(i.e. change the background color to green), it absolutely could if it was stored as a field in the
higher abstract class.

### Relevant changes made in the "controller" package
- As explained above in "*Relevant changes made in the 'view' package*", the ability to set hotkeys
for user-view interaction and display messages for the view have been removed from the controller
and given entirely to the view.
- Because the <span style="color:DarkGoldenRod">**setFeature**</span> method from
<span style="color:Tomato">**MutableReversiModel**</span> no longer returned an integer value as
explained above in "*Relevant changes made in the 'model' package*", the
<span style="color:Lime">**playerId**</span> field from
<span style="color:Aqua">**Controller**</span> could no longer be properly set since it relied on
that integer return value. Additionally, the <span style="color:Lime">**color**</span> field can no
longer be properly set as well since it directly relies on
<span style="color:Lime">**playerId**</span>. We therefore decided to scrap the
<span style="color:Lime">**playerId**</span> field altogether in search of a better alternative to
let the view know what color disc the controller's player played in order for the view to properly
display whose turn it was.
- The method signature for the <span style="color:DarkGoldenRod">**notifyGameStarted()**</span>
method from <span style="color:Tomato">**ModelFeatures**</span> has been altered to
<span style="color:DarkGoldenRod">**notifyGameStarted(Disc color)**</span>. This allows the model
to directly pass in a disc that the controller can identify itself as. In other words, the
<span style="color:Aqua">**Controller**</span> class can now successfully assign its
<span style="color:Lime">**color**</span> field a value when the game has started.
- A new method, <span style="color:DarkGoldenRod">**getPlayerDisc()**</span>, has been added to the
<span style="color:Tomato">**ViewFeatures**</span> interface. This allows the view to ask for a
disc from its listener. The controller, in response, can present its own disc to the view. This
would allow the view to properly identify who's turn it currently is in order to properly display
the current game status.

### Relevant changes made in the game driver
- The datatype storing the controllers of the game have been changed from
<span style="color:Aqua">**Controller**</span> to <span style="color:Tomato">**IController**</span>
in order to decouple the code from a specific implementation of the
<span style="color:Tomato">**IController**</span> interface.
- The second view initialized has been changed from using the
<span style="color:Aqua">**JFrameView**</span> view to the adapted providers' view,
<span style="color:Aqua">**ProviderReversiViewToGraphicalFrameViewAdapter**</span>. This is to
simply utilize two separate unique views for each player in a single Reversi game.
- Added three new AI strategies to the player options map. The three new strategies are the
providers' strategies that have been adapted to infallible strategies. They include
<span style="color:Aqua">**HexagonalStrategyAvoidNextToCorners**</span>,
<span style="color:Aqua">**HexagonalStrategyGoForCorners**</span>, and
<span style="color:Aqua">**HexagonalStrategyMostPointsGreedy**</span>.

[Back to the top](#reversi)

## What we got working successfully from provider code. 
Everything works as anticipated. There were a few bugs from the provider that they were unable to 
correct in time, so not everything works perfectly. 

[Back to the top](#reversi)

## Changes Made for Part 5
### Implementing a new model
A new model representing a Reversi game with a square board was adapted to the project. Game rules
remained unchanged. However, changes in the game logic and the overall design of the entire project
took place. Here is a summary of all the changes:
- To make space for a square board, references to the <span style="color:Aqua">**Hexagon**</span>
shape had to be removed from the model interfaces, the controller, the views, the strategies, the
players, the test classes, and so on. We eventually came to realize that representing hexagons and
squares aren't so different since they only differ in their coordinate system. We therefore decided
to remove the <span style="color:Aqua">**Hexagon**</span> class and replace all references to it
with a simple *Point* object to represent a tile x-y coordinate.
- Built an abstract class, <span style="color:Aqua">**AbstractReversi**</span> in
<span style="color:SlateBlue">**cs3500.reversi.main.model**</span>.
<span style="color:Aqua">**BasicReversi**</span> and the square Reversi model implementation,
<span style="color:Aqua">**SquareReversi**</span>, share a lot of the same implementation in most
of their methods, so we decided an abstract class was appropriate.
- Implemented methods for the <span style="color:Aqua">**SquareReversi**</span> class.

### Testing the new model
After building the new model, we had to test it to see if it worked properly. Here is a summary
of what we did.
- Built the <span style="color:Aqua">**SquareReversiTextualView**</span> textual view class in
<span style="color:SlateBlue">**cs3500.reversi.main.view.textual**</span> to help with testing the
model. <span style="color:Aqua">**ReversiTextualView**</span> was renamed to
<span style="color:Aqua">**HexagonalReversiTextualView**</span>.
- Built the <span style="color:Aqua">**SquareModelInterfaceTest**</span> to test square Reversi
methods specific to the interface.
- Built the <span style="color:Aqua">**SquareImplementationTest**</span> to test square Reversi
methods non-specific to the interface.

### Adapting a new model into the view
Other than refactoring <span style="color:Aqua">**Hexagon**</span> out of the view, the following
changes have been made in the
<span style="color:SlateBlue">**cs3500.reversi.main.view.graphical**</span> package to add support
for the new model:
- <span style="color:Aqua">**GameBoardPanel**</span> has been renamed to
<span style="color:Aqua">**HexagonalBoardPanel**</span>.
- <span style="color:Aqua">**SquareBoardPanel**</span> was built to act as the main panel for the
square Reversi model.
- Some helper methods in <span style="color:Aqua">**AbstractGameBoardPanel**</span> that helped in
painting the board had their implementations moved back to
<span style="color:Aqua">**HexagonBoardPanel**</span>. These implementations in
<span style="color:Aqua">**AbstractGameBoardPanel**</span> became stub implementations. This
allowed both <span style="color:Aqua">**HexagonBoardPanel**</span> and
<span style="color:Aqua">**SquareBoardPanel**</span> to conduct their own calculations while
allowing <span style="color:Aqua">**AbstractGameBoardPanel**</span> to continue enforcing such
methods throughout all classes that try to extend it.

### Changes to the strategy
Other than refactoring <span style="color:Aqua">**Hexagon**</span> out of strategies, a few changes
have been made in the
<span style="color:SlateBlue">**cs3500.reversi.main.strategy.fallible**</span>
package to add support for the new model.

<span style="color:Aqua">**PlayAtCorners**</span> and
<span style="color:Aqua">**AvoidTilesBorderingCorners**</span> were the only two strategies that
were dependent on a hexagonal game board when it came to choosing moves.
<span style="color:Aqua">**PlayAtCorners**</span> was dependent on the specific corners of the
hexagonal board while <span style="color:Aqua">**AvoidTilesBorderingCorners**</span> was dependent
on the adjacency of hexagonal tiles from one another. In order to decouple the dependence on a
hexagonal Reversi from the strategy itself, both strategies would have to rely on the model for
this information rather than calculate it themselves. As a result, two new methods were added to
the immutable model interface - <span style="color:DarkGoldenRod">**getCornerTiles()**</span> and
<span style="color:DarkGoldenRod">**isAdjacent(Point tile1, Point tile2)**</span>. The former
method would automatically return a list of all corner coordinates from the specific model, which
would  help both <span style="color:Aqua">**PlayAtCorners**</span> and
<span style="color:Aqua">**AvoidTilesBorderingCorners**</span> decouple themselves from hexagonal
Reversi specifically since they both rely on corner coordinates to properly carry out their
strategy. The latter method would identify whether two tiles were adjacent or not, which would help
<span style="color:Aqua">**AvoidTilesBorderingCorners**</span> decouple itself from hexagonal
Reversi since it relies on checking tile adjacency to properly carry out its strategy. Changes to
both strategy and model took place to adjust to these changes. This includes but may not be limited
to changes in Javadoc, the removal/modification of specific helper methods, and the modification of
the implementation of interface methods.

### Changes to the controller
Other than refactoring <span style="color:Aqua">**Hexagon**</span> out of the controller, no major
changes were required in the
<span style="color:SlateBlue">**cs3500.reversi.main.controller**</span> package.

### Changes to the adapters
Other than refactoring <span style="color:Aqua">**Hexagon**</span> out all adapter classes, no
major changes were made.

### A new feature to the view: Hints
As an assist to players, a “hint” mode has been enabled where the selected cell shows how many
discs would be flipped if that player chose that move. Hints can be enabled and disabled at
runtime and are shown for each player independently. One should not have to restart the game to
turn hints on or off, and hints for one player should not be shown to the other player. For proper
design, the current rendering code should not be directly modified to add these hints - it should
be a separate decoration that can be added or removed.

Introducing the hints feature actually didn't require a lot of refactoring and modification in the
current view. A game board panel decorator class that extended
<span style="color:Aqua">**AbstractGameBoardPanel**</span>,
<span style="color:Aqua">**HintsDecorator**</span>, was added to the
<span style="color:SlateBlue">**cs3500.reversi.main.view.graphical**</span> package and
implemented.

By using the decorator pattern, a decorator (<span style="color:Aqua">**HintsDecorator**</span>)
could wrap around an object-to-be-decorated (another
<span style="color:Aqua">**AbstractGameBoardPanel**</span> object) and delegate its behavior
through composition while modifying/adding additional functionality on top of the already existing
behavior of the object-to-be-decorated.

To toggle hints on and off at runtime, a check box has been added to the south panel at the bottom
of the window. Checking this box will simply enable hints, and unchecking it will disable the
feature.

To actually reference <span style="color:Aqua">**HintsDecorator**</span> in the main view code in
some way, the constructor of <span style="color:Aqua">**JFrameView**</span> was slightly altered
to set the main panel as a <span style="color:Aqua">**HintsDecorator**</span> object wrapped around
the actual main panel object. Below is how the code roughly looked before and after this change
(Disclaimer: the code below is a simplified version of what the actual constructor looked like.
Only details relevant to the description above are shown).

Before:
<pre>
public JFrameView() {
  this.mainPanel = new HexagonalBoardPanel();
}
</pre>

After:
<pre>
public JFrameView() {
  this.mainPanel = new HintsDecorator(new HexagonalBoardPanel());
}
</pre>

### Changes to the game driver
With the addition of a new Reversi game mode, the game driver's main method was changed to take in
a total four arguments rather than two. The first argument specified which Reversi was being played
(hexagonal or square). The second argument took in the board game side length to allow
configuration for how large the board should be. The last two arguments remained as configuration
for the players. The main method was modified to adjust to this change, and new helper methods were
added to support the changes that occurred in the main method.

Additionally, because there are now two panels to choose from
(<span style="color:Aqua">**HexagonalBoardPanel**</span> and
<span style="color:Aqua">**SquareBoardPanel**</span>), the main method now is in charge of
initiating the game board panel. As a result, the constructor for the main view,
<span style="color:Aqua">**JFrameView**</span>, had to be slightly modified to take in a panel as
an argument rather than initialize one itself. Below is how the code roughly looked before and
after this change in <span style="color:Aqua">**JFrameView**</span> (Disclaimer: the code below is
a simplified version of what the actual constructor looked like. Only details relevant to the
description above are shown):

Before:
<pre>
public JFrameView() {
  this.mainPanel = new HexagonalBoardPanel();
}
</pre>

After:
<pre>
public JFrameView(AbstractGameBoardPanel mainPanel) {
  this.mainPanel = mainPanel;
}
</pre>

[Back to the top](#reversi)

## Credits
This project was created by Renee Cai and Justin Kim on October 24, 2023 for the class 'CS 3500 —
Object-Oriented Design' at *Northeastern University*.

[Back to the top](#reversi)