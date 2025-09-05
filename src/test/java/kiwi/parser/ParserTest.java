package kiwi.parser;

import kiwi.exception.KiwiException;
import kiwi.exception.EmptyDescriptionException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Parser class.
 */
public class ParserTest {

    @Test
    public void getCommandType_validCommands_returnsCorrectType() {
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("todo read book"));
        assertEquals(Parser.CommandType.DEADLINE, Parser.getCommandType("deadline homework /by tomorrow"));
        assertEquals(Parser.CommandType.EVENT, Parser.getCommandType("event meeting /from 2pm /to 4pm"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("list"));
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("bye"));
        assertEquals(Parser.CommandType.MARK, Parser.getCommandType("mark 1"));
        assertEquals(Parser.CommandType.DELETE, Parser.getCommandType("delete 2"));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("random text"));
    }

    @Test
    public void getCommandType_caseInsensitive_returnsCorrectType() {
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("BYE"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("LIST"));
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("TODO read book"));
    }

    @Test
    public void parseTodoCommand_validInput_returnsDescription() throws KiwiException {
        assertEquals("read book", Parser.parseTodoCommand("todo read book"));
        assertEquals("finish homework", Parser.parseTodoCommand("todo finish homework"));
        assertEquals("spaced", Parser.parseTodoCommand("todo    spaced   "));
    }

    @Test
    public void parseTodoCommand_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseTodoCommand("todo");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseTodoCommand("todo    ");
        });
    }

    @Test
    public void parseDeadlineCommand_validInput_returnsDescriptionAndDeadline() throws KiwiException {
        String[] result = Parser.parseDeadlineCommand("deadline submit report /by 2019-12-02");
        assertEquals("submit report", result[0]);
        assertEquals("2019-12-02", result[1]);

        String[] result2 = Parser.parseDeadlineCommand("deadline homework /by tomorrow night");
        assertEquals("homework", result2[0]);
        assertEquals("tomorrow night", result2[1]);
    }

    @Test
    public void parseDeadlineCommand_missingBy_throwsException() {
        assertThrows(KiwiException.class, () -> {
            Parser.parseDeadlineCommand("deadline submit report");
        });
    }

    @Test
    public void parseDeadlineCommand_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseDeadlineCommand("deadline");
        });

        assertThrows(EmptyDescriptionException.class, () -> {
            Parser.parseDeadlineCommand("deadline /by tomorrow");
        });
    }

    @Test
    public void parseEventCommand_validInput_returnsDescriptionFromTo() throws KiwiException {
        String[] result = Parser.parseEventCommand("event meeting /from 2pm /to 4pm");
        assertEquals("meeting", result[0]);
        assertEquals("2pm", result[1]);
        assertEquals("4pm", result[2]);
    }

    @Test
    public void parseEventCommand_missingFromTo_throwsException() {
        assertThrows(KiwiException.class, () -> {
            Parser.parseEventCommand("event meeting /from 2pm");
        });

        assertThrows(KiwiException.class, () -> {
            Parser.parseEventCommand("event meeting");
        });
    }

    @Test
    public void parseTaskNumber_validInput_returnsNumber() throws KiwiException {
        assertEquals(1, Parser.parseTaskNumber("mark 1", "mark"));
        assertEquals(5, Parser.parseTaskNumber("delete 5", "delete"));
        assertEquals(10, Parser.parseTaskNumber("unmark 10", "unmark"));
    }

    @Test
    public void parseTaskNumber_invalidInput_throwsException() {
        assertThrows(KiwiException.class, () -> {
            Parser.parseTaskNumber("mark abc", "mark");
        });

        assertThrows(KiwiException.class, () -> {
            Parser.parseTaskNumber("mark", "mark");
        });
    }

    @Test
    public void parseOnCommand_validDate_returnsLocalDate() throws KiwiException {
        LocalDate result = Parser.parseOnCommand("on 2019-12-02");
        assertEquals(LocalDate.of(2019, 12, 2), result);
    }


}