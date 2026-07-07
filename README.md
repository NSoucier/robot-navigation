# Robot Navigation API

## Design overview
The service accepts a navigation request, validates the input, and simulates the robot command-by-command. Obstacles are stored in a HashSet so that lookups remain efficient even when the obstacle set is large.

## Tradeoffs
- Validation is intentionally minimal and returns HTTP 400 for clearly invalid input.

## Assumptions
- Coordinates use the requested convention where $(0,0)$ is bottom-left.
- The robot stops immediately if a forward move would hit an obstacle or leave the grid.
- The request uses a simple command string containing `F`, `L`, and `R`.

## Complexity analysis
- Obstacle lookup: O(1) average time per check due to HashSet.
- Command processing: O(number of commands) time.
- Memory usage: O(number of obstacles).

## What I would improve with more time
- More robust validation and more detailed error messages.
- Controller integration tests.
- Support for richer request and response schemas if the API evolves.
- Account for diagonal movements and more commands. 
