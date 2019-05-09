package it.aiv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Dungeon {
	int _height;
	int _width;
	int _steps;
	char[][] _matrixChar;
	int[][] _matrixInt;
	char wallChar = 'X';
	char freeChar = ' ';
	int _roomCount;
	Map<Integer, Integer> _roomMap;
	int prevCellCount = 0;
	int _wallCount = 0;

	public Dungeon(int height, int width, int steps, int seed) {
		_height = height;
		_width = width;
		_steps = steps;
		_matrixChar = new char[_height][_width];
		_matrixInt = new int[_height][width];
		int _seed = _height + _width + _steps + seed;
		_roomMap = new HashMap<Integer, Integer>();
		
		initialize(_matrixChar, _seed);
	}

	public char[][] initialize(char[][] matrix, int seed) {
		Random rand = new Random(seed);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i == 0 || j == 0 || i == matrix.length - 1 || j == matrix[0].length - 1) {
					matrix[i][j] = wallChar;
					_wallCount += 1;
				} else {
					int perc = rand.nextInt(2);
					if(perc == 1) {						
						matrix[i][j] = wallChar;
						_wallCount += 1;
					} else if(perc == 0){
						matrix[i][j] = freeChar;
					}
				}
			}
		}
		
		return matrix;
	}
	
	public char[][] flooding(char[][] matrix) {
		System.out.println("\n- FLOODING ROOMS -\n");
		_roomCount = 0;
		int cellCount = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] == freeChar) {
					cellCount = floodFill(matrix, i, j, freeChar, Character.forDigit(++_roomCount, 10), cellCount);
					_roomMap.put(_roomCount, cellCount - prevCellCount);
					prevCellCount = cellCount;
					_matrixInt[i][j] = _roomCount;
				}
			}
		}
		
		return matrix;
	}

	private int floodFill(char[][] matrix, int row, int col, char target, char replacement, int cellCount) {		
		if(target == replacement) {
			return cellCount;
		}

		if(matrix[row][col] != target) {
			return cellCount;
		}
		
		cellCount += 1;
		matrix[row][col] = replacement;
		_matrixInt[row][col] = _roomCount;
		cellCount = floodFill(matrix, row, col + 1, target, replacement, cellCount);
		cellCount = floodFill(matrix, row, col - 1, target, replacement, cellCount);
		cellCount = floodFill(matrix, row + 1, col, target, replacement, cellCount);
		cellCount = floodFill(matrix, row - 1, col, target, replacement, cellCount);
		
		return cellCount;
	}

	public char[][] generate(int steps) {
		System.out.println("\n- GEERATING MAP -\n");
		for (int i = 0; i < steps; i++) {
			_matrixChar = elaborate(_matrixChar);
		}
		_roomMap.put(0,  _wallCount);
		return _matrixChar;
	}

	private char[][] elaborate(char[][] matrix) {
		// turns matrix into a data stream and returns its clone as an array
		char[][] result = Arrays.stream(matrix).map(r -> r.clone()).toArray(char[][]::new);

		for (int i = 1; i < matrix.length - 1; i++) {
			for (int j = 1; j < matrix.length - 1; j++) {
				int wallAround = countWallAround(matrix, i, j);
				char current = matrix[i][j];

				if (current == wallChar) {
					if (wallAround >= 4) {
						result[i][j] = wallChar;
					} else if (wallAround < 2) {
						result[i][j] = freeChar;
						_wallCount -= 1;
					} else {
						result[i][j] = freeChar;
						_wallCount -= 1;
					}
				} else {
					if (wallAround >= 5) {
						result[i][j] = wallChar;
						_wallCount += 1;
					} else {
						result[i][j] = freeChar;
					}
				}
			}
		}

		return result;
	}

	private int countWallAround(char[][] matrix, int row, int col) {
		int count = 0;

		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if(matrix[i][j] == wallChar) {
					count += 1;
				}
			}
		}
		
		return count;
	}
	
	public int checkBiggestRoom() {
		int roomIndex = 0;
		int roomSize = Integer.MIN_VALUE;
		
		for(int key : _roomMap.keySet()) {
			if(_roomMap.get(key) > roomSize) {
				if(key == 0) {
					continue;
				}
				roomIndex = key;
				roomSize = _roomMap.get(key);
			}
		}
		
		return roomIndex;
	}
	
	public void fillAllRoomsExceptOne(char[][] matrix, int roomIndex, char replacement) {
		System.out.println("\n- ISOLATING BIGGEST ROOM -\n");
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] == replacement) {
					continue;
				}
				
				if(_matrixInt[i][j] != roomIndex) {
					matrix[i][j] = replacement;
				}
				else {
					matrix[i][j] = ' ';
				}
			}
		}
	}
	
	public char[][] cropMap(int border) {
		System.out.println("\n- CROPPING AND ADDING BORDER -\n");
		int left = Integer.MAX_VALUE;
		int right = -1;
		int top = Integer.MAX_VALUE;;
		int bottom = -1;
		
		for(int i = 0; i < _matrixChar.length; i++) {
			for(int j = 0; j < _matrixChar[i].length; j++) {
				if(_matrixChar[i][j] != wallChar) {
					if(left > j) {
						left = j;
					}
					if(right < j) {
						right = j;
					}
					if(top > i) {
						top = i;
					}
					if(bottom < i) {
						bottom = i;
					}
				}
			}
		}
		
		char[][] croppedMap = new char[bottom - top + 1 + border * 2][right - left + 1 + border * 2];
		
		for(int i = 0; i < croppedMap.length; i++) {
			for(int j = 0; j < croppedMap[i].length; j++) {
				croppedMap[i][j] = 'X';
			}
		}
		
		for(int i = top, y = border; i <= bottom; i++, y++) {
			for(int j = left, x = border; j <= right; j++, x++) {
				croppedMap[y][x] = _matrixChar[i][j];
			}
		}
		
		return croppedMap;
	}
	
	public void print(char[][] matrix) {
		System.out.println("\n\nMAP STATUS:\n");
		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[i].length; j++) {
				char value = matrix[i][j];
				System.out.print(value + " ");
			}

			System.out.println();
		}
		
		System.out.println();
	}
	
	public void printMapStats() {
		System.out.println("\n\nMAP STATS:\n");
		int totSize = 0;
		for(int key : _roomMap.keySet()) {
			if(key == 0) {
				System.out.println("Walls: " + _roomMap.get(key));
			} else {				
				System.out.println("Room: " + key + " - Size: " + _roomMap.get(key));
			}
			
			totSize += _roomMap.get(key);
		}
		
		System.out.println("\nMap Size (map width * map height): " + (_height * _width));
		System.out.println("Confirmed Map Size (walls + rooms cells): " + totSize + "\n");
	}
	
	public void printMatrixInt() {
		System.out.println("\n\nMAP INTEGER REPRESENTATION FOR FLOODING ANALYSIS (0 = wall; n = room index):\n");
		int digits = String.valueOf(_roomMap.size()).length() + 1;
		
		for(int i = 0; i < _matrixInt.length; i++) {
			for(int j = 0; j < _matrixInt[i].length; j++) {
				String value = String.format("% " + digits + "d", _matrixInt[i][j]);
				System.out.print(value);
			}
			
			System.out.println();
		}
		
		System.out.println();
	}
}
