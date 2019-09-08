# FireMan

## The Puzzle

### Introduction

Some English words can be split into "sub-words". For example, the word "fireman" can be divided into the word "fire"
and the word "man". Often this is because the word is a composite word, built from the sub-words, but sometimes it is
just serendipitous, like constructing "panther" from "pant" and "her".

For the purposes of this puzzle, a valid disassembly of a word must only consist of legitimate English words. So, for
example, the word "geometry" doesn't work, because, although "me" and "try" are English words, "geo" is not.

We can define the "sub-word count" as the number of words contained in the larger word.

### The Problem

Given a dictionary, find the word in it with the largest "sub-word count". That is, find the word composed of the
largest number of sub-words (also from that dictionary).

For the purposes of this puzzle, we will only consider sub-words of three or more letters. So the word "cantaloupe"
cannot be decomposed into "cant-a-loupe", because the word "a" has fewer than three letters.

So, the task is, write a program that, in the most efficient way possible, solves the puzzle. The program will take no
inputs, and should find the word with the most sub-words using the supplied `words.txt` file as the dictionary, and
should print or display the found word when done.

It is not necessary \(but a nice to have\) to display the sub-words.

The `words.txt` dictionary is a text file with one word on every line. There are no blank lines and all lines have
exactly one word. The words are already alphabetized.

