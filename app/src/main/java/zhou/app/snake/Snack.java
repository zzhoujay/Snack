package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.squareup.otto.Subscribe;

/**
 * Created by zhou on 16-1-4.
 * 蛇
 * 采用记录蛇的每个节点的方式记录蛇，即记录每个拐角点
 */
public class Snack {

    public Point direction;
    public int size;
    public Paint paint;


    private SnackNode head, tail;

    private boolean directionChanged = false;

    private int color;

    public Snack(Point direction, int size, int color) {
        this.direction = direction;
        this.size = size;
        this.color = color;

        paint = new Paint();
        paint.setColor(color);

        head = new SnackNode(7, 7, size);
        tail = new SnackNode(7, 52, size);

        head.setNext(tail);
        tail.setPrev(head);

        App.getApp().getBus().register(this);
    }

    public void draw(Canvas canvas) {
        SnackNode node = head;
        do {
            canvas.drawRect(node.node, paint);
        } while ((node = node.getNext()) != null);
    }

    /**
     * 下一步，蛇前进
     */
    public void next() {
        // 如果方向被改变过了的话就会产生一个拐角点，因此需要创建一个节点
        if (directionChanged) {
            SnackNode newHead = new SnackNode(head.getX() + direction.x, head.getY() + direction.y, size, head);
            head.setPrev(newHead);
            head = newHead;
            directionChanged = false;
        } else {
            head.offset(direction.x, direction.y);
        }

        SnackNode tailPrev = tail.getPrev();
        int dx = 0, dy = 0;
        boolean merge;
        if (tail.getY() == tailPrev.getY()) {
            dx = tailPrev.getX() - tail.getX();
            merge = Math.abs(dx) <= 1;
            dx = dx / Math.abs(dx);
        } else {
            dy = tailPrev.getY() - tail.getY();
            merge = Math.abs(dy) <= 1;
            dy = dy / Math.abs(dy);
        }
        // 如果需要合并倒数第二个节点和尾节点则进行合并
        if (merge) {
            tailPrev.setNext(null);
            tail = tailPrev;
        } else {
            tail.offset(dx, dy);
            tailPrev.setNext(tail);
        }

    }

    @Subscribe
    public void changeDirection(KeyMap keyMap) {
        if (direction.x * keyMap.direction.x == 0 && direction.y * keyMap.direction.y == 0) {
            direction = keyMap.direction;
            directionChanged = true;
        }
    }
}
