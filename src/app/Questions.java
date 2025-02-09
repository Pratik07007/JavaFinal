package app;

public class Questions {
    private final int id;
    private final String questionText;
    private final String opt1;
    private final String opt2;
    private final String opt3;
    private final String opt4;
    private final String level;
    private final String answer;

    /**
     * Constructor to initialize a new question.
     * 
     * @param id the unique identifier for the question
     * @param questionText the text of the question
     * @param opt1 the first option for the question
     * @param opt2 the second option for the question
     * @param opt3 the third option for the question
     * @param opt4 the fourth option for the question
     * @param level the difficulty level of the question
     * @param answer the correct answer to the question
     */
    public Questions(int id, String questionText, String opt1, String opt2, String opt3, String opt4, String level, String answer) {
        this.id = id;
        this.questionText = questionText;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.level = level;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOpt1() {
        return opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public String getLevel() {
        return level;
    }

    public String getAnswer() {
        return answer;
    }

    public String[] getOptions() {
        return new String[] { opt1, opt2, opt3, opt4 };
    }

    /**
     * Provides a string representation of the question, useful for debugging.
     * 
     * @return a string representing the question and its options
     */
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", opt1='" + opt1 + '\'' +
                ", opt2='" + opt2 + '\'' +
                ", opt3='" + opt3 + '\'' +
                ", opt4='" + opt4 + '\'' +
                ", level='" + level + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
