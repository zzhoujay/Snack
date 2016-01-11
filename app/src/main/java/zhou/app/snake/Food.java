package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-11.
 */
public class Food implements Drawable {

    private Point position;
    private int color;
    private int score;
    private int life;
    private int blockSize;
    private Paint paint;

    public Food(Point position, int score, int life, int blockSize, Paint paint) {
        this.position = position;
        this.score = score;
        this.life = life;
        this.blockSize = blockSize;
        this.paint = paint;
        this.color = paint.getColor();
    }

    public Food(Point position, int score, int life, int blockSize, int color) {
        this(position, score, life, blockSize, new Paint());
        this.color = color;
        paint.setColor(color);
    }

    public Food(int x, int y, int score, int life, int blockSize, Paint paint) {
        this(new Point(x, y), score, life, blockSize, paint);
    }

    public Food(int x, int y, int score, int life, int blockSize, int color) {
        this(new Point(x, y), score, life, blockSize, color);
    }


    public void setSize(int size) {
        this.blockSize = size;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(position.x, position.y, position.x + blockSize, position.y + blockSize, paint);
    }
}
