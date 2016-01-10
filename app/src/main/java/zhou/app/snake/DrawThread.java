package zhou.app.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by zhou on 16-1-6.
 */
public final class DrawThread implements Runnable {


    private final int interval_time;
    private boolean running;
    private final SurfaceHolder holder;
    private final int refreshColor;

    private final List<Task> tasks;

    public DrawThread(SurfaceHolder holder, int fps, int refreshColor) {
        this.holder = holder;
        tasks = new ArrayList<>();
        running = true;
        interval_time = 1000 / fps;
        this.refreshColor = refreshColor;
    }

    public DrawThread(SurfaceHolder holder, int fps) {
        this(holder, fps, Color.WHITE);
    }

    public DrawThread(SurfaceHolder holder) {
        this(holder, 30, Color.WHITE);
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();

            synchronized (holder) {
                synchronized (tasks) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        canvas.drawColor(refreshColor);
                        for (Task task : tasks) {
                            if (!running) {
                                return;
                            }
                            task.draw(canvas);
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

            }

            long endTime = System.currentTimeMillis();

            while (endTime - startTime < interval_time) {
                endTime = System.currentTimeMillis();
                Thread.yield();
            }
        }
    }

    public void finish() {
        running = false;
    }

    public void addTask(Action1<Canvas> task, int priority) {
        tasks.add(new Task(task, priority));
    }

    public void removeTask(Action1<Canvas> task) {
        for (Task t : tasks) {
            if (t.action.equals(task)) {
                tasks.remove(t);
                return;
            }
        }
    }

    private static class Task implements Comparable<Task> {

        public Action1<Canvas> action;
        public int priority;

        public Task(Action1<Canvas> action, int priority) {
            this.action = action;
            this.priority = priority;
        }

        public Task(Action1<Canvas> action) {
            this.action = action;
            priority = 0;
        }

        public void draw(Canvas canvas) {
            action.call(canvas);
        }

        @Override
        public int compareTo(@NonNull Task another) {
            return priority - another.priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Task)) return false;

            Task task = (Task) o;

            return priority == task.priority && action.equals(task.action);

        }

        @Override
        public int hashCode() {
            int result = action.hashCode();
            result = 31 * result + priority;
            return result;
        }
    }

}
