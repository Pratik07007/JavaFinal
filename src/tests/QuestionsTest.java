package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.Questions;

class QuestionsTest {

    private Questions question;

    @BeforeEach
    void setUp() {
        question = new Questions(1, "What is Java?", "Programming Language", "Snake", "Car", "Football", "Easy", "Programming Language");
    }

    @Test
    void testGetId() {
        assertEquals(1, question.getId());
    }

    @Test
    void testGetQuestionText() {
        assertEquals("What is Java?", question.getQuestionText());
    }

    @Test
    void testGetOpt1() {
        assertEquals("Programming Language", question.getOpt1());
    }

    @Test
    void testGetOpt2() {
        assertEquals("Snake", question.getOpt2());
    }

    @Test
    void testGetOpt3() {
        assertEquals("Car", question.getOpt3());
    }

    @Test
    void testGetOpt4() {
        assertEquals("Football", question.getOpt4());
    }

    @Test
    void testGetLevel() {
        assertEquals("Easy", question.getLevel());
    }

    @Test
    void testGetAnswer() {
        assertEquals("Programming Language", question.getAnswer());
    }

    @Test
    void testGetOptions() {
        String[] options = question.getOptions();
        assertArrayEquals(new String[] {"Programming Language", "Snake", "Car", "Football"}, options);
    }

    @Test
    void testToString() {
        String expectedString = "Question{id=1, questionText='What is Java?', opt1='Programming Language', opt2='Snake', opt3='Car', opt4='Football', level='Easy', answer='Programming Language'}";
        assertEquals(expectedString, question.toString());
    }
}
