package app;

public class Questions {
    private int id;
    private String text;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String level;
    private String answer;

    public Questions(int id, String text, String opt1, String opt2, String opt3, String opt4, String level, String answer) {
        this.id = id;
        this.text = text;
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

    public String getText() {
        return text;
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

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String[] getOptions() {
        return new String[] { opt1, opt2, opt3, opt4 };
    }

}
