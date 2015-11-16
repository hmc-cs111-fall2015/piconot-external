# SimpleEMS
A text based event management system

## Current State: 
An API for basic event management. 
Can handle about a week of events that must be delineated by hours.

Currently, users can make rooms and then reserve those rooms for periods of time with hour-granularity. 

## Implementation:
The API is provided through the room manager class.
The room manager should be initialized and then can be altered by making method calls.

Other objects should not be accessed, and all desirable changes should be changeable using Strings and Ints.

