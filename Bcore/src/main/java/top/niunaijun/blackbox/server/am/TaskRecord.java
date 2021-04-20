package top.niunaijun.blackbox.server.am;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Milk on 4/9/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class TaskRecord {
    public int id;
    public int userId;
    public String taskAffinity;
    public final List<ActivityRecord> activities = new LinkedList<>();

    public TaskRecord(int id, int userId, String taskAffinity) {
        this.id = id;
        this.userId = userId;
        this.taskAffinity = taskAffinity;
    }

    public boolean needNewTask() {
        for (ActivityRecord activity : activities) {
            if (!activity.finished) {
                return false;
            }
        }
        return true;
    }

    public void addTopActivity(ActivityRecord record) {
        activities.add(record);
    }

    public void removeActivity(ActivityRecord record) {
        activities.remove(record);
    }

    public ActivityRecord getTopActivityRecord() {
        if (activities.isEmpty())
            return null;
        return activities.get(activities.size() - 1);
    }
}
