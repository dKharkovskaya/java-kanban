package util;

import task.*;

import java.time.LocalDateTime;
import java.util.HashMap;

import static task.Task.DATE_TIME_FORMATTER;

public class FormatterUtil {

    public static String toString(Task task) {
        String epicId = "";
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            Subtask sbTask = (Subtask) task;
            epicId = String.valueOf(sbTask.getEpicTask().getId());
        } else if (task.getTaskType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            epic.calculateTime();
        }
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + epicId + "," + task.getDuration().toMinutes() + "," + startTime.format(DATE_TIME_FORMATTER) + "," + endTime.format(DATE_TIME_FORMATTER);
    }

    public static Task fromString(String line, HashMap<Long, Epic> epicTasks) {
        String[] arrParam = line.split(",");
        for (int i = 0; i < arrParam.length; i++) {
            if (arrParam[1].equals("TASK")) {
                Task task = new Task(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4], Long.parseLong(arrParam[6]), arrParam[7]);
                task.setStatus(Status.valueOf(arrParam[3]));
                return task;
            } else if (arrParam[1].equals("EPIC")) {
                Epic epic = new Epic(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4], Long.parseLong(arrParam[6]), arrParam[7]);
                epic.setEndTime(LocalDateTime.parse(arrParam[8], DATE_TIME_FORMATTER));
                epic.setStatus(Status.valueOf(arrParam[3]));
                return epic;
            } else if (arrParam[1].equals("SUBTASK")) {
                Subtask subtask = new Subtask(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4], Long.parseLong(arrParam[6]), arrParam[7]);
                try {
                    subtask.setEpicTask(epicTasks.get(Long.parseLong(arrParam[5])));
                } catch (NumberFormatException e) {
                    System.out.println("Эпик с id = " + arrParam[5] + " ещё не был добавлен. Проверьте формат csv файла");
                    throw new RuntimeException(e);
                }
                subtask.setStatus(Status.valueOf(arrParam[3]));
                return subtask;

            }
        }
        return null;
    }
}
