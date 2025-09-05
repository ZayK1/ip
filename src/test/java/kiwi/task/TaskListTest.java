package kiwi.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the TaskList class.
 */
public class TaskListTest {
    private TaskList taskList;
    private Todo todo;
    private Deadline deadline;
    private Event event;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo = new Todo("read book");
        deadline = new Deadline("submit report", "2019-12-02");
        event = new Event("meeting", "2019-12-01", "2019-12-02");
    }

    @Test
    public void addTask_singleTask_increasesSize() {
        assertEquals(0, taskList.size());
        taskList.addTask(todo);
        assertEquals(1, taskList.size());
        assertEquals(todo, taskList.getTask(0));
    }

    @Test
    public void addTask_multipleTasks_maintainsOrder() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        taskList.addTask(event);

        assertEquals(3, taskList.size());
        assertEquals(todo, taskList.getTask(0));
        assertEquals(deadline, taskList.getTask(1));
        assertEquals(event, taskList.getTask(2));
    }

    @Test
    public void deleteTask_validIndex_removesTask() {
        taskList.addTask(todo);
        taskList.addTask(deadline);
        taskList.addTask(event);

        Task deleted = taskList.deleteTask(1);
        assertEquals(deadline, deleted);
        assertEquals(2, taskList.size());
        assertEquals(todo, taskList.getTask(0));
        assertEquals(event, taskList.getTask(1));
    }



    @Test
    public void getTasksOnDate_deadlineMatches_returnsMatchingTasks() {
        Deadline deadline1 = new Deadline("report 1", "2019-12-02");
        Deadline deadline2 = new Deadline("report 2", "2019-12-03");
        taskList.addTask(deadline1);
        taskList.addTask(deadline2);
        taskList.addTask(todo);

        LocalDate searchDate = LocalDate.of(2019, 12, 2);
        List<Task> result = taskList.getTasksOnDate(searchDate);

        assertEquals(1, result.size());
        assertEquals(deadline1, result.get(0));
    }

    @Test
    public void getTasks_returnsDefensiveCopy() {
        taskList.addTask(todo);
        List<Task> tasks = taskList.getTasks();

        tasks.clear();
        assertEquals(1, taskList.size());
    }

    @Test
    public void toString_emptyList_returnsNoTasksMessage() {
        String result = taskList.toString();
        assertTrue(result.contains("No tasks"));
    }

    @Test
    public void toString_withTasks_returnsFormattedList() {
        taskList.addTask(todo);
        taskList.addTask(deadline);

        String result = taskList.toString();
        assertTrue(result.contains("Here are the tasks"));
        assertTrue(result.contains("1."));
        assertTrue(result.contains("2."));
        assertTrue(result.contains("read book"));
        assertTrue(result.contains("submit report"));
    }
}