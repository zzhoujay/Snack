package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import com.annimon.stream.Stream;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import zhou.app.snake.interfaces.Callable;
import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-4.
 */
public class Snack implements Drawable, Callable {

    public LinkedList<Point> body;
    public Point direction;
    public int size;
    public Paint paint;

    public int color;

    private Path path;

    public Snack(Point direction, int size, int color) {
        this.direction = direction;
        this.size = size;
        this.color = color;

        path = new Path();

        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(size);

        body = new LinkedList<>();
        body.add(new Point(5, 5));
        body.add(new Point(5, 6));
        body.add(new Point(5, 7));

        App.getApp().getBus().register(this);

//        analyze();
    }

    @Override
    public void draw(Canvas canvas) {
        for (Point p : body) {
            int xx = p.x * size;
            int yy = p.y * size;
            canvas.drawRect(xx, yy, xx + size, yy + size, paint);
        }
    }

    public void next() {
        Point head = head();
        Point end = body.removeLast();
        end.set(head.x + direction.x, head.y + direction.y);
        body.addFirst(end);

//        analyze();

    }

    private void analyze() {
        Point temp = null;
        ArrayList<Point> points = new ArrayList<>();
        boolean flagX = false, flagFirst = false;
        for (Point p : body) {
            if (temp == null) {
                points.add(p);
                flagFirst = true;
            } else if (flagFirst) {
                flagX = temp.x == p.x;
                flagFirst = false;
            } else {
                if (flagX && temp.x == p.x || !flagX && temp.y == p.y) {
                    continue;
                } else {
                    flagX = temp.x == p.x;
                    points.add(temp);
                }
            }
            temp = p;
        }

        points.add(body.getLast());

        path.reset();

        for (int i = 0, size = points.size(); i < size; i++) {
            Point p = points.get(i);
            if (i == 0) {
                path.moveTo(p.x * size, p.y * size);
            } else if (i == size - 1) {
                path.setLastPoint(p.x * size, p.y * size);
            } else {
                path.lineTo(p.x * size, p.y * size);
            }
        }
    }

    public Point head() {
        return body.getFirst();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void growUp() {
        Point newPoint = new Point(body.getLast());
        body.addLast(newPoint);
    }

    public int length() {
        return body.size();
    }

    @Subscribe
    public void changeDirection(KeyMap keyMap) {
        if (direction.x * keyMap.direction.x == 0 && direction.y * keyMap.direction.y == 0) {
            direction = keyMap.direction;
        }
    }

    @Override
    public void call() {
        next();
    }
}
