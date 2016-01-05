package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 16-1-4.
 */
public class Snack {

    public List<Point> positions;
    public Point direction;
    public int size;
    public Paint paint;

    public int color;

    public Snack(Point direction, int size, int color) {
        this.direction = direction;
        this.size = size;
        this.color = color;

        paint = new Paint();
        paint.setColor(color);

        positions = new ArrayList<>();
        positions.add(new Point(5, 5));
        positions.add(new Point(5, 6));
        positions.add(new Point(5, 7));

        App.getApp().getBus().register(this);
    }

    public void draw(Canvas canvas) {
        for (Point p : positions) {
            canvas.drawRect(p.x * size, p.y * size, p.x * size + size, p.y * size + size, paint);
        }
    }

    public void next() {
        Point temp = null;
        for (int i = positions.size() - 1; i >= 0; i--) {
            Point p = positions.get(i);
            if (i == 0) {
                p.set(temp.x + direction.x, temp.y + direction.y);
            } else {
                temp = positions.get(i - 1);
                p.set(temp.x, temp.y);
            }
        }
    }

    @Subscribe
    public void changeDirection(KeyMap keyMap) {
        if (direction.x * keyMap.direction.x == 0 && direction.y * keyMap.direction.y == 0) {
            direction = keyMap.direction;
        }
    }
}
