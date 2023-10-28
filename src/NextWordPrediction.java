import java.util.*;
public class NextWordPrediction {

    static class TrigramModel {
        private Map<String, List<String>> trigrams;

        public TrigramModel() {
            trigrams = new HashMap<>();
        }

        public void train(String[] words) {
            for (int i = 0; i < words.length - 2; i++) {
                String key = words[i] + " " + words[i + 1];
                String value = words[i + 2];

                trigrams.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }

        public String predict(String word1, String word2) {
            String key = word1 + " " + word2;
            List<String> possibleNextWords = trigrams.getOrDefault(key, Collections.emptyList());

            if (possibleNextWords.isEmpty()) {
                return "No prediction available.";
            }

            // Randomly select a next word from the list of possibilities.
            Random rand = new Random();
            int randomIndex = rand.nextInt(possibleNextWords.size());
            return possibleNextWords.get(randomIndex);
        }

        public static void main(String[] args) {
            TrigramModel trigramModel = new TrigramModel();

            // Train the model with some sample text (you can use your own text).
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the sample text for training the model: ");
            String[] sampleText = scanner.nextLine().split(" ");
            trigramModel.train(sampleText);

            // Test the model by providing two words for prediction.
            System.out.print("Test the model by providing two words for prediction: ");
            String word1 = scanner.nextLine();
            String word2 = scanner.nextLine();
            String nextWord = trigramModel.predict(word1, word2);
            System.out.println("Next word prediction: " + nextWord);
        }
    }
}
