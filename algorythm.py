# !/usr/bin/python3.4
# -*- coding: utf-8 -*-
# This program implements algorithm which finds dots' coordinates according the distance between them
# Saylenty 09.08.2014
__author__ = 'Saylenty'
import time
# Enter your matrix here

"""matrix = ((0, 4, 3, 4, 3, 3, 6, 3, 4, 2),
          (4, 0, 3, 4, 7, 7, 6, 5, 4, 2),
          (3, 3, 0, 3, 6, 6, 9, 6, 7, 5),
          (4, 4, 3, 0, 3, 5, 10, 7, 8, 6),
          (3, 7, 6, 3, 0, 4, 9, 6, 7, 5),
          (3, 7, 6, 5, 4, 0, 5, 2, 7, 5),
          (6, 6, 9, 10, 9, 5, 0, 3, 4, 4),
          (3, 5, 6, 7, 6, 2, 3, 0, 5, 3),
          (4, 4, 7, 8, 7, 7, 4, 5, 0, 2),
          (2, 2, 5, 6, 5, 5, 4, 3, 2, 0))
"""
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

"""matrix = ((0, 1, 1, 2, 3, 1, 2, 4, 3),
          (1, 0, 2, 3, 4, 2, 3, 5, 4),
          (1, 2, 0, 1, 2, 2, 3, 5, 4),
          (2, 3, 1, 0, 1, 3, 4, 6, 5),
          (3, 4, 2, 1, 0, 4, 5, 7, 6),
          (1, 2, 2, 3, 4, 0, 1, 3, 2),
          (2, 3, 3, 4, 5, 1, 0, 2, 1),
          (4, 5, 5, 6, 7, 3, 2, 0, 1),
          (3, 4, 4, 5, 6, 2, 1, 1, 0))
"""

lt = [set() for k in range(10)]


def check_distance(dot_number, coord, lst):
    """ Функция проверки дистанции между точками
        dot_number - номер точки [0 - 9]
        coord - текущая пара (x, y) координат для точки dot_number
        lst - список координат предыдущих точек по порядку
    """
    for i in reversed(range(dot_number)[1:]):
        if matrix[dot_number][i] != abs(coord[0] - lst[i][0]) + abs(coord[1] - lst[i][1]): return False
    return True


def gen_cord(dot_number):
    """ dot_number - номер точки """
    global lt
    if len(lt[dot_number]) > 0:  # Если просчитали уже координаты точки
        yield from lt[dot_number]  # Зависаем тут
        return  # Не идем пересчитывать уже имеющиеся координаты
    i = (matrix[0][dot_number])
    s = set()
    for j in range(i + 1):
        s.update(((j, i - j), (j, -(i - j)), (-j, -(i - j)), (-j, i - j)))
        if len(s) > i * 2:  # Для ускорения работы. Нет смысла считать все, считаем партиями
            yield from s  # Все равно что for p in s: yield p
            lt[dot_number] = lt[dot_number].union(s)
            s.clear()
    if len(s) > 0:  # Остатки
        yield from s
        lt[dot_number] = lt[dot_number].union(s)


def main_func(lst, i=1):
    """ lst - результирующий спосок координат всех точек """
    for d in gen_cord(i):  # Взяли следующую координату текущей точки
        if check_distance(i, d, lst):  # Проверяем подходит ли она
            lst.append(d)  # Если подходит => добавляем в список
            if i == len(matrix) - 1: return True  # Если только что добавили 9-ую точку, выходим с успехом
            if not main_func(lst, i + 1):
                lst.pop()  # Если невозможно поставить следующую точку - убираем текущую
            else:
                return True  # Подобрали 9 точку, выходим из рекурсии
    return False  # Все координаты точки перебраны и ни одна из точек не подошла => возвращаемся на одну точку


def wr():  # Функция где просто создается список с начальной точкой и вызывается основная функция (main_func)
    lst = [(0, 0)]
    main_func(lst)
    return lst


def main():
    time.perf_counter()
    print(wr())
    print(time.perf_counter())


if __name__ == "__main__": main()
