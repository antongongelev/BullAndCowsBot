import org.apache.commons.lang3.text.StrBuilder;

public class Game {
    private boolean isActive;
    private final String number;
    private int steps;
    private int bulls;
    private int cows;

    public Game() {
        isActive = true;
        number = generate();
    }

    public void count(String answer) {
        bulls = 0;
        cows = 0;
        boolean[] marked = new boolean[number.length()];
        // Посчитаем быков:
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == number.charAt(i)) {
                marked[i] = true;
                bulls++;
            }
        }
        // Посчитаем коров:
        outerLoop:
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) != number.charAt(i)) {
                for (int j = 0; j < number.length(); j++) {
                    if (answer.charAt(i) == number.charAt(j) && !marked[j]) {
                        marked[j] = true;
                        cows++;
                        continue outerLoop;
                    }
                }
            }
        }
    }

    public static String getTryNumberMsg(int steps) {
        if (String.valueOf(steps).endsWith("1")) {
            return String.format("потребовалась %s попытка", steps);
        }
        if (String.valueOf(steps).endsWith("2") || String.valueOf(steps).endsWith("3") || String.valueOf(steps).endsWith("4")) {
            return String.format("потребовалось %s попытки", steps);
        }
        return String.format("потребовалось %s попыток", steps);
    }

    public void endGame() {
        isActive = false;
        steps = 0;
        bulls = 0;
        cows = 0;
    }

    public static boolean isGameNumber(String value) {
        if (value.length() == Constants.NUMBER_LENGTH) {
            return value.chars().mapToObj(c -> (char) c).allMatch(Character::isDigit);
        }
        return false;
    }

    public boolean isAnswerValid(String answer) {
        return number.equals(answer);
    }

    private String generate() {
        StrBuilder builder = new StrBuilder(Constants.NUMBER_LENGTH);
        for (int i = 0; i < Constants.NUMBER_LENGTH; i++) {
            builder.append(String.valueOf((int) (Math.random() * 10)));
        }
        return builder.toString();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getNumber() {
        return number;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getBulls() {
        return bulls;
    }

    public void setBulls(int bulls) {
        this.bulls = bulls;
    }

    public int getCows() {
        return cows;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }
}
