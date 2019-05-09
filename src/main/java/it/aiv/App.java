package it.aiv;

public class App {
	public static void main(String[] args) {
		int height;
		int width;
		int steps;
		int seed;
		int border;

		if (args.length > 0) {
			height = Integer.parseInt(args[0]);
			width = Integer.parseInt(args[1]);
			steps = Integer.parseInt(args[2]);
			seed = Integer.parseInt(args[3]);
			border = Integer.parseInt(args[4]);
		} else {
			height = 48;
			width = 48;
			steps = 3;
			seed = 7;
			border = 2;
		}

		Dungeon dungeon = new Dungeon(height, width, steps, seed);
		char[][] matrix = new char[height][width];
		matrix = dungeon.generate(steps);
		dungeon.print(matrix);
		matrix = dungeon.flooding(matrix);
		dungeon.printMatrixInt();
		dungeon.printMapStats();
		int biggestRoomIndex = dungeon.checkBiggestRoom();
		System.out.println("Biggest Room Index: " + biggestRoomIndex + "\n");
		dungeon.fillAllRoomsExceptOne(matrix, biggestRoomIndex, 'X');
		dungeon.print(matrix);
		char[][] cropped = dungeon.cropMap(border);
		dungeon.print(cropped);
	}
}
