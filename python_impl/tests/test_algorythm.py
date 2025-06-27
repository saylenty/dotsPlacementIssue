import unittest
import algorythm

class TestAlgorythm(unittest.TestCase):
    def test_triangulation(self):
        matrix = (
            (0, 1, 2),
            (1, 0, 3),
            (2, 3, 0),
        )
        algorythm.matrix = matrix
        coords = algorythm.wr()
        self.assertEqual(coords[0], (0, 0))
        n = len(matrix)
        self.assertEqual(len(coords), n)
        for i in range(n):
            for j in range(n):
                expected = matrix[i][j]
                dist = abs(coords[i][0] - coords[j][0]) + abs(coords[i][1] - coords[j][1])
                self.assertEqual(dist, expected)

if __name__ == '__main__':
    unittest.main()
