# The Planning for Reversi
This documentation will primarily consist of photos that display our thought process.

## Overall Planning

Data Structure Choices
- Board — Map<Hexagon, Disc> 
  - Key - Hexagon which has a unique coordinate (x, y)
  - Disc - A disc with the color: Black, White, None (there is no disc)
- Coordinate System used: The center hexagon has a coordinate of (0,0).
  - In the direction of top-left/bottom-right, both x and y are incremented.
  - In the direction of left-right, only the x-value is incremented.
  - In the direction of top-right/bottom-left, only the y-valye is incremented.

![](/Users/reneecai/Downloads/IMG_7797.JPG)

[Ideas that were deprecated]
- Command Build Pattern — ended up being easier without it

### Model
**Designing the board:**

Math Involved: The number of tiles on a board: 3(n)(n-1) + 1
![](/Users/reneecai/Downloads/IMG_6098 2.jpg)
- Uses (n)(n+1)/2 to calculate the number of tiles in each "triangle". 
- Multiply by 6 because there are 6 triangles. 
- Adds 1 to account for the center hexagon.

![](/Users/reneecai/Downloads/IMG_4B9B42E1C4BE-1.jpeg)
- The build is split into two parts:
  - Top Half: This includes the middle row. Uses a double for-loop to iterate through the y-values, 
  and then x-values. Starts from the left to the right.
  - Bottom Half: Starts from right beneath the middle row. Uses a double for-loop to iterate through 
  the x-values and then the y-values. Starts from right to left.

Testing the board:
![](/Users/reneecai/Downloads/IMG_6109.PNG)

### View

**Terminology used in this documentation will need to be changed to be consistent with the variables
used in the code implementation. Also screenshots will be added later of the written work.**

CONVERTING HEXAGON COORDINATES TO PIXEL COORDINATES:

Like mentioned earlier in the file, the coordinate system works as follows:

The center hexagon has a coordinate of (0,0).
- In the direction of top-left/bottom-right, both x and y are incremented.
- In the direction of left-right, only the x-value is incremented.
- In the direction of top-right/bottom-left, only the y-value is incremented.

The design of our pixel cartesian coordinate also works with the assumption that the center
coordinate of the screen is also going to be (0,0). 
- In the leftwards direction, x decreases (x can be negative).
- In the rightwards direction, x increases.
- In the upwards direction, y increases.
- In the downwards direction, y decreases (y can be negative).

This is the equation to obtain pixel coordinates from hexagon coordinates. The new coordinates will 
be referred to as newX and newY:

    newX = ((2 * hexagon.getX() + hexagon.getY()) * (Math.cos(Math.PI / 6)) * hexagonSideLength)

Math.cos(Math.PI / 6) * hexagonSideLength refers to half the width of the hexagon. 

    newY = -(3 * hexagon.getY() * (Math.sin(Math.PI / 6)) * hexagonSideLength)

**Math.sin(Math.PI / 6) * hexagonSideLength** refers to half the height of the hexagon.  Our y coordinate 
value increases bottom to top in our hexagonal grid coordinate system. Since the pixel coordinate 
system increases the y coordinate value top to bottom, we must  multiply newY here by -1 to account 
for this factor

BUILDING THE HEXAGON PIECE:

For us to build the hexagon piece, we draw it dependent on the center coordinate of the hexagon.
The variables/terms that I will be referring to for simplicity in explanation:
- center = the middle of the hexagon such that the length of the line drawn from the center
  to each of the corners of the hexagon is the same length.
- <span style="color:MediumSeaGreen">**x**</span> = the coordinate that represents the 
  horizontal position of the center on a cartesian graph
- <span style="color:MediumSeaGreen">**y**</span> = the coordinate that represents the vertical 
  position of the center on a cartesian graph
- <span style="color:MediumSeaGreen">**newX**</span> = the coordinate that represents the
  horizontal coordinate of the (current) corner of the hexagon.
- <span style="color:MediumSeaGreen">**newY**</span> = the coordinate that represents the
  horizontal coordinate of the (current) corner of the hexagon.
- angle = if a line was drawn from the center to the corner and a line was drawn from the 
  center straight to the right (as if there were an x-axis there). Angle refers to the angle in
  degrees created by these two lines.

We build the hexagon using a Path2D.double, which allows us to draw a line between a series of 
specified points, hence "drawing" out the hexagon shape. We obtain these points by using this
equation:

    cornerX = x + hexagonSideLength * Math.cos(Math.PI/3 * i + Math.PI / 6) ) + getWidth() * 0.5)

    cornerY = y + hexagonSideLength * Math.sin(Math.PI/3 * i + Math.PI / 6) ) + getHeight() * 0.5

<span style="color:MediumSeaGreen">**cornerX**</span> and 
<span style="color:MediumSeaGreen">**cornerY**</span> stand for the pixel coordinates of the corner
of a given hexagon on the screen. 

<span style="color:MediumSeaGreen">**hexagonSideLength**</span> = the length of one side of the 
hexagonPiece (different from the boardLength). This length is calculated depending on the window
so that when the window is resized, the board is also scaled to match it. The formula for that is
as follows:

    hexagonSideLength = 2.0 * Math.min(getWidth(), getHeight()) / (3 * (2 * boardLength + 1));

So, to obtain the x-distance from the center of each 
corner, we multiply it by **Math.cos(MATH.PI/3 * i + Math.PI / 6)**. The **MATH.PI/6** is there to 
rotate the hexagon so that it's a pointy top hexagon. Similar to getting the x-distance, we get 
y-distance from multiplying the hexagonSideLength by Math.sin(MATH.PI/3 * i + Math.PI / 6). The 
getWidth() * 0.5 and getHeight() * 0.5 are there to help translate the hexagon so that the hexagon 
will be centered
relative to the grid on the screen.

The i is to index through 0 - 5 in a for-loop. In order to connect the lines, we move the 
Path2D.double to the first corner. From then on, we use the lineTo method to connect the previous 
corner to the next found corner. Once we've found all 6 corners, we close the shape by using 
closePath() which connects a line from the last point to the first one. 

BUILDING THE GRID:

The paintHexagon() method displays a visual view of the hexagon on the board. So, the paintComponent
method will use the paintHexagon() method and go through all the elements in the keySet from the
game board and display it on the screen. This can be done because we use the hexagonal coordinates
from the board to directly translate into the pixel coordinates on the screen.

Each hexagon will be created as grey. The only time it will not be grey is when it is being selected
by the user as a possible location to place down a disc. In that case, it will *temporarily* turn
blue. 

BUILDING AND PLACING THE DISC:

Each disc will be a circular piece that is placed down right in the middle of a hexagon. It will
either be black or white, depending on the player that is placing it down. The center coordinate of 
the disc should match the center coordinate of the hexagon. The diameter of the disc will be the
same as hexagonSideLength. Using the fillOval method, we can create the circular disc that is
centered at the same pixel coordinates as the hexagon that we intend on placing the disc on.


SCREENSHOTS OF WORK:
// To be added in an upcoming update.

### Controller
*To Be Implemented*

### Strategy
Our approach to designing strategies started off with a simple strategy interface that any strategy
class can implement. The interface consisted of one method that would return a Hexagon object that
represented the hexagon tile at which the strategy selected to place a disc at. We should clarify
that strategies cannot make moves nor change the game in any way - they only return where to make
the move.

We eventually encountered an issue however as we were building the CherryPicker strategy:
CherryPicker was a strategy that focused on passing its current turn when advantageous rather than
simply making a move on a specific hexagon tile, and our strategy interface method for choosing a
move only had to return a hexagon tile. This pushed us to build a Move class that represented a
move that can be made. Adding such a class allowed us to combine choosing hexagon tiles and passing
turns into one object that can represent both. Now with this class, we were able to alter the
strategy interface method for choosing moves to return a Move object rather than a hexagon as it
did before.

Another issue we encountered was related to the organization of our strategies and the fact that
there was a mix between strategies that would always return a move of some sort and strategies that
simply didn't return any move if it could not find a suitable move. To deal with this, we
implemented the strategy pattern, in which strategies would be split between two interfaces
representing fallible and infallible strategies. Any fallible strategy would be a building block
for an infallible strategy that would stack multiple strategies on top of each other and prioritize
the one at the top (there were exceptions, but this was the general gist of the relationship
between fallible and infallible strategies).

In the following strategy descriptions, the term "piece(s)" refers to a players' discs and is often
used interchangeably with "disc."
- <span style="color:Aqua">**CaptureMostPieces**</span> is a simple strategy that will choose
  whichever hexagon gets the current player the most pieces captured, hence increasing the score
  the most. If there are two or more hexagons that will capture the same number of maximum pieces,
  it will choose the uppermost-leftmost hexagon, prioritizing the uppermost hexagons over leftmost
  hexagons.
- <span style="color:Aqua">**PlayAtCorners**</span> is a simple strategy that focuses on playing at
  the corners of the game board. The logic behind this strategy is that discs in corners cannot be
  captured because they don’t have hexagons on their other side, meaning that the opposing player
  cannot place a disc down that would capture the disc placed down. If there are more than one
  corners available to be played on, it will choose the hexagon that captures the most pieces. Like
  <span style="color:Aqua">**CaptureMostPieces**</span>, if there is more than one hexagon that
  captures the same number of maximum pieces,it will choose the uppermost-leftmost piece. This has
  been finalized in a specific order of the top-left, top-right, leftmost, rightmost, bottom-left,
  bottom-right corner. If there is no corner available, it will pass.
- <span style="color:Aqua">**AvoidHexagonsBorderingCorners**</span> is a simple strategy that
  maximizes the score, but focuses on avoiding the hexagons next to corners. The logic behind this
  strategy is that choosing a hexagon that borders a corner gives the  opponent the ability to
  easily get a corner on their next turn. We want to avoid doing so because a disc placed in a
  corner can never be captured again, so we want to reduce the number of opponent corner pieces.
- <span style="color:Aqua">**PlayCornersAndMaxScore**</span> is a strategy that chains on two
  simpler strategies. This strategy uses the <span style="color:Aqua">**CaptureMostPieces**</span>
  and <span style="color:Aqua">**PlayAtCorners**</span> strategy. This strategy will prioritize
  getting the corners first and if there are no corners, then it will focus on choosing the hexagon
  that captures the most pieces. If none of the hexagons can be chosen, then it will pass.
- <span style="color:Aqua">**OptimizeCorners**</span> is another chained on strategy. It uses the
  strategies, <span style="color:Aqua">**PlayAtCorners**</span> and
  <span style="color:Aqua">**AvoidHexagonsBorderingCorners**</span>. It focuses on playing at the
  corners of the game board. If this strategy fails, the strategy will focus on avoiding the
  hexagons next to corners before focusing on maximizing the score.

More strategies can be found in the strategy-specific class' JavaDoc. 

### Player
There are plans to add a Player Interface. This will allow us to add players such as a regular
player, AI player algorithms, etc. 

For AI players, we will create an AIPlayer Interface that extends the Player Interface. This is
because all AI players behave in a similar way, but they can be implemented in different ways. This
is why they deserve their own interface. 