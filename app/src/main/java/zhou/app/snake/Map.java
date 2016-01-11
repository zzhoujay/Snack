package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.HashSet;

import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-11.
 */
public class Map implements Drawable {

    private int colNum, rowNum, blockSize;

    private Snack snack;
    private HashSet<Point> foods;

    @Override
    public void draw(Canvas canvas) {

    }

}
