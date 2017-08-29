# !/usr/bin/python3.4
# -*- coding: utf-8 -*-
# This program implements algorithm which finds dots' coordinates according the distance between them
# Saylenty 09.08.2014
__author__ = 'Saylenty'
import time
# Enter your matrix here

# matrix = ((0, 4, 3, 4, 3, 3, 6, 3, 4, 2),
#          (4, 0, 3, 4, 7, 7, 6, 5, 4, 2),
#          (3, 3, 0, 3, 6, 6, 9, 6, 7, 5),
#          (4, 4, 3, 0, 3, 5, 10, 7, 8, 6),
#          (3, 7, 6, 3, 0, 4, 9, 6, 7, 5),
#          (3, 7, 6, 5, 4, 0, 5, 2, 7, 5),
#          (6, 6, 9, 10, 9, 5, 0, 3, 4, 4),
#          (3, 5, 6, 7, 6, 2, 3, 0, 5, 3),
#          (4, 4, 7, 8, 7, 7, 4, 5, 0, 2),
#          (2, 2, 5, 6, 5, 5, 4, 3, 2, 0))

# matrix = ((0, 1, 1, 2, 3, 1, 2, 4, 3),
#          (1, 0, 2, 3, 4, 2, 3, 5, 4),
#          (1, 2, 0, 1, 2, 2, 3, 5, 4),
#          (2, 3, 1, 0, 1, 3, 4, 6, 5),
#          (3, 4, 2, 1, 0, 4, 5, 7, 6),
#          (1, 2, 2, 3, 4, 0, 1, 3, 2),
#          (2, 3, 3, 4, 5, 1, 0, 2, 1),
#          (4, 5, 5, 6, 7, 3, 2, 0, 1),
#          (3, 4, 4, 5, 6, 2, 1, 1, 0))

matrix = ((0, 2, 2, 4, 4, 6, 6, 8, 8, 8),
          (2, 0, 4, 2, 6, 4, 8, 6, 10, 8),
          (2, 4, 0, 6, 2, 8, 4, 10, 6, 8),
          (4, 2, 6, 0, 8, 2, 10, 4, 12, 8),
          (4, 6, 2, 8, 0, 10, 2, 12, 4, 8),
          (6, 4, 8, 2, 10, 0, 12, 2, 14, 8),
          (6, 8, 4, 10, 2, 12, 0, 14, 2, 8),
          (8, 6, 10, 4, 12, 2, 14, 0, 16, 8),
          (8, 10, 6, 12, 4, 14, 2, 16, 0, 8),
          (8, 8, 8, 8, 8, 8, 8, 8, 8, 0))

lt = [set() for k in range(len(matrix))]  # create cache which reduces calculation number => speed up execution


def check_distance(dot_number, coord, lst):
    """ Function which checks the distance between dots
        dot_number - number of the dot 
        coord - current coordinates pair (x, y) for current dot dot_number
        lst - list of previous dots (ordered)
    """
    for i in reversed(range(dot_number)[1:]):
        if matrix[dot_number][i] != abs(coord[0] - lst[i][0]) + abs(coord[1] - lst[i][1]): return False
    return True


def gen_cord(dot_number):
    """ Function which generates possible coordinates for current dot
        dot_number - number of the dot 
    """
    global lt
    if len(lt[dot_number]) > 0:  # if we already have coord-s for current dot in cache
        yield from lt[dot_number]
        return  # we don't need to recalculate them again -> return
    i = (matrix[0][dot_number])
    s = set()
    for j in range(i + 1):
        s.update(((j, i - j), (j, -(i - j)), (-j, -(i - j)), (-j, i - j)))
        if len(s) > i * 2:  # to speed up the calculation we compute partially
            yield from s  # The same as "for p in s: yield p"
            lt[dot_number] = lt[dot_number].union(s)
            s.clear()
    if len(s) > 0:  # remains
        yield from s
        lt[dot_number] = lt[dot_number].union(s)


def main_func(lst, i=1):
    """ lst - contains the resulting list of all dots """
    for d in gen_cord(i):  # Take next coord of the current dot
        if check_distance(i, d, lst):  # Check if it satisfies the condition
            lst.append(d)  # If yes -> add to list
            if i == len(matrix) - 1: return True  # if we just added the last (9-th) dot -> return successfully
            if not main_func(lst, i + 1):
                lst.pop()  # if it is impossible to put the next dot -> remove the current one
            else:
                return True  # find the last (9-th) dot -> stop recursion
    return False  # if we have iterated over all coord-s of the current dot
    # and haven't found the right coord -> step back


def wr():
    """ Function where we create initial list and call the main function (main_func) """
    lst = [(0, 0)]
    main_func(lst)
    return lst


def main():
    time.perf_counter()
    print(wr())
    print(time.perf_counter())


if __name__ == "__main__": main()
