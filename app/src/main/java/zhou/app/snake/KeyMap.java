package zhou.app.snake;

import android.graphics.Point;

/**
 * Created by zhou on 16-1-4.
 */
public enum KeyMap {
    up(new Point(0, -1)), down(new Point(0, 1)), left(new Point(-1, 0)), right(new Point(1, 0));

    public Point direction;

    KeyMap(Point direction) {
        this.direction = direction;
    }
}
