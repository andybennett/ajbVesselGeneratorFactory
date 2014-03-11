package ajb.factory;

import java.awt.Point;

import ajb.domain.Pixel;
import ajb.random.RandomInt;
import ajb.utils.PixelGridUtils;

public class VesselGeneratorFactory {

	private final int ROWS = 600;
	private final int COLS = 600;

	public Pixel[][] create() {

		Pixel[][] grid = createBaseGrid();
		addExtras(grid);

		grid = PixelGridUtils.floor(grid);
		grid = PixelGridUtils.mirrorCopyGridHorizontally(grid);
		grid = PixelGridUtils.addBorders(grid);
		grid = PixelGridUtils.floor(grid);
		PixelGridUtils.fillEmptySurroundedPixelsInGrid(grid);
		PixelGridUtils.addNoiseToFlatPixels(grid);	
		PixelGridUtils.setPixelDepth(grid);

		if (validateGrid(grid)) {		
			return grid;
		} else {
			return create();
		}
	}

	private boolean validateGrid(Pixel[][] grid) {
		
		boolean result = true;
		
		int noOfFilledPixels = 0;
		int noOfSecondaryPixels = 0;
		
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y].value == Pixel.FILLED) {
					noOfFilledPixels++;
				} else if (grid[x][y].value == Pixel.SECONDARY) {
					noOfSecondaryPixels++;
				}
			}
		}

		if (noOfSecondaryPixels == 0) {
			result = false;
		}
		
		if (noOfSecondaryPixels > (noOfFilledPixels / 4)) {
			result = false;
		}	
		
		return result;
	}
	
	private Pixel[][] createBaseGrid() {

		Pixel[][] grid = new Pixel[ROWS][COLS];
		PixelGridUtils.initEmptyGrid(grid, ROWS, COLS);

		Point point = new Point(ROWS / 2, COLS - 1);

		int steps = RandomInt.anyRandomIntRange(5, 50);
		int subSteps = RandomInt.anyRandomIntRange(5, 120);

		for (int i = 0; i < steps; i++) {

			if (point == null) {
				// we are passed the first step lets find the lowest most pixel
				// that is closest to the middle, and go again from there...

				// top down
				for (int x = 0; x < ROWS; x++) {
					// left to right
					for (int y = 0; y < COLS; y++) {
						if (grid[x][y].value == Pixel.FILLED) {
							point = new Point(x, y);
						}
					}
				}
			}

			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}

			point = null;
		}

		return grid;
	}

	private void addExtras(Pixel[][] grid) {

		int steps = RandomInt.anyRandomIntRange(0, 20);
		int subSteps = RandomInt.anyRandomIntRange(5, 50);

		for (int i = 0; i < steps; i++) {
			Point point = PixelGridUtils.getRandomFilledPoint(grid);

			for (int y = 0; y < subSteps; y++) {
				point = processPoint(point, grid);
			}
		}
	}

	private Point processPoint(Point point, Pixel[][] grid) {

		if (grid[point.x][point.y].value == Pixel.EMPTY) {
			grid[point.x][point.y].value = Pixel.FILLED;			
		}

		return PixelGridUtils.getRandomAdjacentPoint(point, grid);
	}
}
