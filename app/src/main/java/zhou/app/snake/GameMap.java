package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import com.annimon.stream.Stream;

import java.util.HashSet;
import java.util.Random;

import zhou.app.snake.interfaces.Callable;
import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-11.
 */
public class GameMap implements Drawable, Callable {

    private Random random;
    private int colNum, rowNum, blockSize;
    private Snack snack;
    private HashSet<Food> foods;

    public GameMap(int colNum, int rowNum, int blockSize) {
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.blockSize = blockSize;
        foods = new HashSet<>();
        random = new Random();

        foods.add(new Food(random.nextInt(colNum), random.nextInt(rowNum), 10, 1, blockSize, Color.BLUE));
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

    @Override
    public void call() {
        Point head = snack.getHead().getPosition();
        Stream.of(foods).filter(value -> value.getPosition().equals(head.x, head.y)).forEach(value1 -> {
            snack.growUp();
            value1.life--;
            if (value1.life <= 0) {
                value1.recycle();
                value1.reset(random.nextInt(colNum), random.nextInt(rowNum), 10, 1, blockSize, Color.BLUE);
            }
        });
    }
}
