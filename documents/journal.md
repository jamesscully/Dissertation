# Journal of Development

#### Table of Contents:
  * Overview
  * Specification / Planning Documents
  * Development changes
    - Design changes
    - Backend changes
    - Frontend changes
    - Resource changes
    - General changes
    - Miscellaneous
  * Bug fixes




#### Overview
This document is intended to record changes in development, whether it be from swapping over API's or approaching things differently compared to original plans.


It shall also document things such as bug fixes and how technologies have helped these come to light, or alleviated them.

This will assist later on when write-ups are due.

### Specification / Planning Documents
#### Things to Talk about
	- Tests that outsmart me; looking at tests, believing I have the right result, when the tests show I'm wrong.


### Development changes

** Pseudocode was a bad idea **
Generally, for smaller algorithms pseudocode is a given. However, in this case it was not feasible as evaluating a TexasHM hand is extremely difficult with no output; it is very easy to overlook certain edge cases when no output or tests are there to prove you right.


#### Design changes
** TDD and how I wasn't going to fully adhere to it **
After developing some methods in the actual program itself, I've noticed that going 50/50 and writing tests alongside code is extremely useful.
Some things to probably put into it would be that it really benefits finding edge cases or cases out of the ordinary where they aren't easily noticeable when you're designing them.


#### Backend changes

**20/10/19 : Refactoring Hand into TexasHand**
Changed the Hand class to TexasHand specifically for that gamemode. Moreover, using polymorphism to enable generic functions for each hand (get suits + amt, get face values + amt)

- Changed our evaluator to use TResults. This allows for in the future, where we will want to relay the highest card within a result, i.e. Pair (King High)



#### Frontend changes

#### Resource changes

#### General changes

#### Miscellaneous changes





### Bug fixes

Bug fixes should be in the format of:

>**Bug fixed at commit:** HASH \
>**Bug description:** This is an example description \
>**Discovery notes:** This bug was found with magic
