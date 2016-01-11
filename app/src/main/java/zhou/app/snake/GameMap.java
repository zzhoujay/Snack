package zhou.app.snake;

import android.graphics.Canvas;

import java.util.HashSet;

import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-11.
 */
public class GameMap implements Drawable {

    private int colNum, rowNum, blockSize;

    private Snack snack;
    private HashSet<Food> foods;

    public GameMap(int colNum, int rowNum, int blockSize) {
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.blockSize = blockSize;
        foods = new HashSet<>();
    }

    public void resetSize(int colNum, int rowNum, int blockSize) {
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.blockSize = blockSize;

        snack.setSize(blockSize);
        for (Food food : foods) {
            food.setSize(blockSize);
        }
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    @Override
    public void draw(Canvas canvas) {
        snack.draw(canvas);
        for (Food food : foods) {
            food.draw(canvas);
        }
    }

}
