package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;
import zhou.app.snake.interfaces.Drawable;

/**
 * Created by zhou on 16-1-6.
 */
public final class DrawThread implements Runnable {


    private final int interval_time;
    private final SurfaceHolder holder;
    private final int refreshColor;
    private final boolean showFps;

    private final List<DrawTask> drawTasks;
    private Rect fpsRect;
    private boolean running;
    private Paint fpsPaint;
    private long lastTime;


    public DrawThread(SurfaceHolder holder, int fps, int refreshColor, boolean showFps) {
        this.holder = holder;
        drawTasks = new ArrayList<>();
        running = true;
        interval_time = 1000 / fps;
        this.refreshColor = refreshColor;
        this.showFps = showFps;
        Rect rect = holder.getSurfaceFrame();
        fpsRect = new Rect(rect.right - 200, 0, rect.right, 100);
        fpsPaint = new Paint();
        fpsPaint.setColor(Color.BLACK);
        fpsPaint.setTextSize(32);
    }

    public DrawThread(SurfaceHolder holder, int fps) {
        this(holder, fps, Color.WHITE, false);
    }

    public DrawThread(SurfaceHolder holder) {
        this(holder, 30, Color.WHITE, false);
    }

    public DrawThread(SurfaceHolder holder, int fps, int refreshColor) {
        this(holder, fps, refreshColor, false);
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();

            synchronized (holder) {
                synchronized (drawTasks) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(refreshColor);
                        for (DrawTask drawTask : drawTasks) {
                            if (!running) {
                                return;
                            }
                            drawTask.draw(canvas);
                        }
                        if (showFps)
                            drawFps(canvas);
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }

            long endTime = System.currentTimeMillis();

            while (endTime - startTime <= interval_time) {
                endTime = System.currentTimeMillis();
                Thread.yield();
            }
        }
    }

    private void drawFps(Canvas canvas) {
        long currTime = System.currentTimeMillis();
        if (lastTime != 0) {
            int fps = (int) (currTime - lastTime);
            canvas.drawText(String.format("fps:%d", fps), fpsRect.centerX(), fpsRect.centerY(), fpsPaint);
        }
        lastTime = currTime;
    }

    public void finish() {
        running = false;
    }

    public void addTask(Drawable task, int priority, int intervalTime) {
        drawTasks.add(new DrawTask(task, priority, intervalTime));
    }

    public void addTask(Drawable task, int priority) {
        drawTasks.add(new DrawTask(task, priority));
    }

    public void addTask(Drawable task) {
        drawTasks.add(new DrawTask(task));
    }

    public void addTask(DrawTask drawTask) {
        drawTasks.add(drawTask);
    }

    public void removeTask(Drawable task) {
        for (DrawTask t : drawTasks) {
            if (t.action.equals(task)) {
                drawTasks.remove(t);
                return;
            }
        }
    }

    public void removeTask(DrawTask drawTask) {
        drawTasks.remove(drawTask);
    }

    private static class DrawTask implements Comparable<DrawTask>, Drawable {

        public Drawable action;
        public int priority;
        public int intervalTime;

        private long lastCallTime;

        public DrawTask(Drawable action, int priority, int intervalTime) {
            this.action = action;
            this.priority = priority;
            this.intervalTime = intervalTime;
        }

        public DrawTask(Drawable action, int priority) {
            this.action = action;
            this.priority = priority;
            intervalTime = 100 / 3;
        }

        public DrawTask(Drawable action) {
            this.action = action;
            priority = 0;
            intervalTime = 100 / 3;
        }

        @Override
        public void draw(Canvas canvas) {
            long currTime = System.currentTimeMillis();
            if (currTime - lastCallTime >= intervalTime) {
                action.draw(canvas);
                lastCallTime = currTime;
            }
        }

        @Override
        public int compareTo(@NonNull DrawTask another) {
            return priority - another.priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DrawTask)) return false;

            DrawTask drawTask = (DrawTask) o;

            return priority == drawTask.priority && action.equals(drawTask.action);

        }

        @Override
        public int hashCode() {
            int result = action.hashCode();
            result = 31 * result + priority;
            return result;
        }
    }

}
