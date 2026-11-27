import java.util.ArrayList;

/** Keywords used to match an owner's lost item with found reports. */
public class Keywords {
    private ArrayList<String> words = new ArrayList<>();

    public void add(String word) {
        word = word.toLowerCase().trim();
        if (!word.isEmpty() && !words.contains(word)) words.add(word);
    }

    public void addFromText(String text) {
        for (String word : text.split("\\W+")) {
            if (word.length() > 2) add(word);
        }
    }

    public int matchScore(String text) {
        text = text.toLowerCase();
        int score = 0;
        for (String word : words)
            if (text.contains(word)) score++;
        return score;
    }

    public ArrayList<String> getWords() { return words; }
}
