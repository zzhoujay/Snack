package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import zhou.app.snake.interfaces.Drawable;
import zhou.app.snake.interfaces.Recyclable;

/**
 * Created by zhou on 16-1-11.
 */
public class Food implements Drawable, Recyclable {

    public Point position;
    private int color;
    private int score;
    public int life;
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
        int xx = position.x * blockSize;
        int yy = position.y * blockSize;
        canvas.drawRect(xx, yy, xx + blockSize, yy + blockSize, paint);
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void recycle() {
    }

    public void reset(Point point, int score, int life, int blockSize, Paint paint) {
        this.position = point;
        this.score = score;
        this.life = life;
        this.blockSize = blockSize;
        this.paint = paint;
        this.color = paint.getColor();
    }

    public void reset(Point position, int score, int life, int blockSize, int color) {
        reset(position, score, life, blockSize, new Paint());
        paint.setColor(color);
        this.color = color;
    }

    public void reset(int x, int y, int score, int life, int blockSize, Paint paint) {
        reset(new Point(x, y), score, life, blockSize, paint);
    }

    public void reset(int x, int y, int score, int life, int blockSize, int color) {
        reset(new Point(x, y), score, life, blockSize, color);
    }
}
