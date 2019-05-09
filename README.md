# Bidimensional-Cellular-Automata
Simple Cellular Automata algorithm implementation for Dungeon-like map generation

This small Java library generates dungeon-like maps using a Cellular Automata approach.

Generally speaking, Cellular Automata transformations can be applied on single dimension arrays filled with 0s and a single 1 in the middle
(as in this example: {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}), representing an initial status. It is not mandatory to start
with a predefined array like this, it is possible to choose any "custom" initial status, but that kind of sequence is known for good
results. Following transformations applied on the initial status will significantly change that line through each step of application,
resulting in interesting patterns, in some case, once all the steps are taken and analysed as a whole at the end of the process.
Those specific "cases of interest" can be "selected" (or kind of "forced") by choosing a precise rule to be applied during each step.
Rules are just integer numbers from 0 to 255 expressed as binary (so, for example, one of the most common rules is "Rule 30", which is
nothing but number "30" written like a binary: "11110"). Application of the same rule will always produce the same pattern.

To use Cellular Automata transformations in map generation, we need to think in two dimensions, cause a map is, at least, a two
dimensions coord system and a single dimension transformation is not useful anymore to solve this problem: we can't use a predefined rule
as we did before, cause it only effectively works on a linear initial status (monodimensional array), while now we are working an a map,
a two dimension array. We need to define a self-made rule able to transform our map initial state in some interesting pattern (let's say
a "dungeon cave like one") and an algorithm to conveniently arrange the map found in a useful and comfortable result.

This is the exact purpose of this library, it creates dungeon-cave-like maps starting from a bidimensional array (a map essentially) and
generating a final nice looking single cave map. The process is customizable through user's inputs using arguments when launching the main
application. Arguments can be inserted as follow:

  - args[0] = map height;
  - args[1] = map width;
  - args[2] = number of transformation steps
  - args[3] = seed for initial state random fill;
  - args[4] = border to be added in the final map;
  
  During the process there'll be several printouts of maps and data that can be useful to understand the elaboration made by the algorithm:
  
    - Initial map status;
    - Integer map representation for Flooding analysis;
    - Map stats (walls count, rooms (caves) count and size, total map size match verify, biggest room selection);
    - Single biggest cave map isolation (other rooms will be filled with walls);
    - Single biggest cave map cropped and border added;
    
To identify and then isolate the biggest room it has been used a Flooding recursive function, a simple algorithm able to fill each room
of the map with a different index while taking into account how many cells that room counts. The second printout shows clearly each
room of the map filled with a number (starting from 1) representing the index for that room. In this way tha last index will automatically
be the total rooms number.
