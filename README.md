# 2D Point Triangulation

This project is dedicated to positioning (triangulating) points in a two-dimensional Cartesian system, where the origin is always fixed at `(0, 0)`. The algorithms reconstruct coordinates using a matrix of pairwise Manhattan distances between points.

## Implementation details

The main idea of this algorithm is to try every possible combination of calculated coordinates until the correct answer(s) is found. This is a recursive algorithm with backtracking approach.

## Available solvers

- Python implementation: find the first possible solution
- Java implementation
  - Simple single-thread approach
  - Rotation approach — optimized `Simple` one
  - Concurrent
    - Based on ForkJoin task — use concurrent technique for solving the task
    - Based on CountedCompleter task: similar to `ForkJoin` but with minimal interprocess communication

## Available visualization

Java implementation has a JavaFX2 visualization:

![JavaFX2 visualization](https://i.imgur.com/k9qvC9f.gif)

## Example

The following matrix has been specified:

|  0 |  2 |  2 |  4 |  4 |  6 |  6 |  8 |  8 |  8 |
|----|----|----|----|----|----|----|----|----|----|
|  2 |  0 |  4 |  2 |  6 |  4 |  8 |  6 | 10 |  8 |
|  2 |  4 |  0 |  6 |  2 |  8 |  4 | 10 |  6 |  8 |
|  4 |  2 |  6 |  0 |  8 |  2 | 10 |  4 | 12 |  8 |
|  4 |  6 |  2 |  8 |  0 | 10 |  2 | 12 |  4 |  8 |
|  6 |  4 |  8 |  2 | 10 |  0 | 12 |  2 | 14 |  8 |
|  6 |  8 |  4 | 10 |  2 | 12 |  0 | 14 |  2 |  8 |
|  8 |  6 | 10 |  4 | 12 |  2 | 14 |  0 | 16 |  8 |
|  8 | 10 |  6 | 12 |  4 | 14 |  2 | 16 |  0 |  8 |
|  8 |  8 |  8 |  8 |  8 |  8 |  8 |  8 |  8 |  0 |

> **Note**: Matrix must be **symmetric and square**.  
> We skip the first row and column which are in range `[0; rows_length or cols_length)`.

According to the matrix, we can say the distance between two dots:

- From point `[0]` to point `[0]` = 0 (distance to itself is zero — obvious)
- From point `[0]` to point `[1]` = 2
- From point `[0]` to point `[2]` = 2
- ...
- From point `[0]` to point `[9]` = 8
- From point `[1]` to point `[0]` = 2
- From point `[1]` to point `[1]` = 0 (distance to itself is zero — obvious)
- ...

The initial dot 0 always has coordinates `(0, 0)`.  
After running the program, one of the results is:
<br>[(0, 0), (-1, 1), (1, -1), (-2, 2), (2, -2), (-3, 3), (3, -3), (-3, 5), (5, -3), (4, 4)]<br>

If we visualize the result using Cartesian coordinate system, we will get the picture:

![Result visualization](./ex.jpg)

> _Note_: Blue color was used to indicate rules of calculating the distance.  
> __Pay attention__: The distance is not the weight of the line which directly connects two dots (lines may be angular).
