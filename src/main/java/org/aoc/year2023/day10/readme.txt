Part 2
Modeling the grid at double resolution.
This causes the cases where you need to "squeeze between" the pipes to just become narrow passages of width 1,
so a standard flood fill captures all of them.
After that it's just a matter of only counting the grid points that actually correspond to a position within the original grid.