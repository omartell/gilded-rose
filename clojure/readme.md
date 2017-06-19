## Gilded Rose Notes ##
### Task ###
“Conjured" items degrade in quality twice as fast as normal items.

### Assumptions ###
- +5 Dexterity Vest and Elixir of the Mongoose were considered as “normal” items.
- As the quality degrades by 1 for a normal item before the sell-by date, then the quality for a Conjured should degrade by 2.
- As the quality degrades by 2 for a normal item after the sell-by date, then the quality for a Conjured should degrade by 4.

### Refactoring ###
I’ve used Clojure multimethods as a way to separate the different quality implementations, which makes the code easier to follow as the logic for each of them is now independent. Multimethods make the system more extensible and give flexibility for adding new items with different quality rules in the future.

### Additional bug fixes ###
The original code didn’t take into account the following rules even though they were in the original specification:
- The quality of an item is never more than 50, except for sulfuras.
- The quality of an item is never negative.
