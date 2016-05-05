package zhou.app.snake;

import android.graphics.Point;
import android.graphics.Rect;


/**
 * Created by zhou on 16-5-5.
 */
public class SnackNode{

    public Rect node;
    private Point position;
    private int size;

    private SnackNode next;
    private SnackNode prev;

    public SnackNode(int x, int y, int size) {
        this(new Point(x, y), size);
    }

    public SnackNode(int x, int y, int size, SnackNode next) {
        this(new Point(x, y), size, next);
    }

    public SnackNode(Point position, int size) {
        this.position = position;
        this.size = size;

        int nodeX = position.x * size;
        int nodeY = position.y * size;

        node = new Rect(nodeX, nodeY, nodeX + size, nodeY + size);
    }

    public SnackNode(Point position, int size, SnackNode next) {
        this.position = position;
        this.size = size;
        this.next = next;

        setNext(next);
    }

    public SnackNode(Point position, int size, SnackNode next, SnackNode prev) {
        this(position, size, next);
        this.prev = prev;
    }

    public void setPosition(Point position) {
        this.position = position;

        setNext(next);
    }


    public void setPosition(int x, int y) {
        if (position == null) {
            position = new Point();
        }
        position.set(x, y);
        setNext(next);
    }

    public void offset(int x, int y) {
        if (position == null) {
            position = new Point();
        }
        position.offset(x, y);
        setNext(next);
    }

    public void setNext(SnackNode next) {
        this.next = next;


        int nodeX = position.x * size;
        int nodeY = position.y * size;

        if (next == null) {
            node = new Rect(nodeX, nodeY, nodeX + size, nodeY + size);
            return;
        }

        Point nextPoint = next.position;

        if (position.y == nextPoint.y) {
            if (position.x < nextPoint.x) {
                node = new Rect(nodeX, nodeY, nextPoint.x * size, nextPoint.y * size + size);
            } else {
                node = new Rect(nextPoint.x * size + size, nextPoint.y * size, nodeX + size, nodeY + size);
            }
        } else {
            if (position.y < nextPoint.y) {
                node = new Rect(nodeX, nodeY, nextPoint.x * size + size, nextPoint.y * size);
            } else {
                node = new Rect(nextPoint.x * size, nextPoint.y * size + size, nodeX + size, nodeY + size);
            }
        }
    }

    public SnackNode getNext() {
        return next;
    }

    public void setPrev(SnackNode prev) {
        this.prev = prev;
    }

    public SnackNode getPrev() {
        return prev;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
