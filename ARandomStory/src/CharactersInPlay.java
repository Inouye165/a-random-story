import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
// import java.awt.Font; // Optional: For setting text area font

/**
 * Combines analysis for Word Frequencies (Assignment 1) and
 * Character Speaking Parts (Assignment 2).
 * Reads a single file, performs both analyses, and displays
 * combined results in a GUI window.
 */
public class CharactersInPlay {

    // --- Fields for Assignment 1: Word Frequencies ---
    private ArrayList<String> wordList;     // Stores unique words
    private ArrayList<Integer> wordFreqs;    // Stores corresponding word frequencies

    // --- Fields for Assignment 2: Character Names ---
    private ArrayList<String> characterNames; // Stores unique character names (uppercase)
    private ArrayList<Integer> nameCounts;     // Stores corresponding speaking part counts

    /**
     * Constructor: Initializes all ArrayLists.
     */
    public CharactersInPlay() {
        // Initialize Assignment 1 lists
        wordList = new ArrayList<>();
        wordFreqs = new ArrayList<>();
        // Initialize Assignment 2 lists
        characterNames = new ArrayList<>();
        nameCounts = new ArrayList<>();
    }

    // --- Methods for Assignment 1: Word Frequencies ---

    /**
     * Reads words from file, counts frequencies (case-insensitive).
     * Populates wordList and wordFreqs.
     * @param filename Path to the input file.
     */
    public void findUniqueWords(String filename) {
        wordList.clear(); //
        wordFreqs.clear(); //
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase(); // [cite: 3]
                int index = wordList.indexOf(word);
                if (index == -1) {
                    wordList.add(word);
                    wordFreqs.add(1);
                } else {
                    int currentFreq = wordFreqs.get(index);
                    wordFreqs.set(index, currentFreq + 1);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Word Frequencies Error: File not found - " + filename);
            // Consider adding this error to the output string
        }
    }

    /**
     * Finds the index of the most frequent word in wordList.
     * Handles ties by returning the first max index found. [cite: 2]
     * @return Index of the most frequent word, or -1.
     */
    public int findIndexOfMaxWord() { // Renamed to avoid conflict if max needed for chars later
        int maxFreq = 0;
        int maxIndex = -1;
        if (wordFreqs != null && !wordFreqs.isEmpty()) {
             maxFreq = wordFreqs.get(0);
             maxIndex = 0;
             for (int k = 1; k < wordFreqs.size(); k++) {
                if (wordFreqs.get(k) > maxFreq) {
                    maxFreq = wordFreqs.get(k);
                    maxIndex = k;
                }
             }
        }
        return maxIndex;
    }

    // --- Methods for Assignment 2: Character Names ---

    /**
     * Updates character lists (adds new or increments count). [cite: 46]
     * Assumes person is already uppercase.
     * @param person Uppercase character name.
     */
    public void updateCharacter(String person) { // Renamed for clarity
        int index = characterNames.indexOf(person);
        if (index == -1) {
            characterNames.add(person);
            nameCounts.add(1);
        } else {
            int currentCount = nameCounts.get(index);
            nameCounts.set(index, currentCount + 1);
        }
    }

    /**
     * Reads file line by line, finds character names based on "ALL CAPS." format. [cite: 48]
     * Populates characterNames and nameCounts.
     * @param filename Path to the input file.
     */
    public void findAllCharacters(String filename) {
        characterNames.clear(); // [cite: 49]
        nameCounts.clear(); // [cite: 49]
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trimmedLine = line.trim();
                int periodIndex = trimmedLine.indexOf('.'); // [cite: 37]
                if (periodIndex > 0) {
                    String potentialName = trimmedLine.substring(0, periodIndex);
                    boolean isAllCaps = !potentialName.isEmpty() && potentialName.matches("^[A-Z ]+$"); // User specified rule refinement
                    if (isAllCaps) {
                        String finalName = potentialName.trim().toUpperCase();
                        updateCharacter(finalName); // [cite: 48]
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Character Analysis Error: File not found - " + filename);
             // Consider adding this error to the output string
        }
    }

    /**
     * Builds string of characters with speaking parts within a range. [cite: 54, 55]
     * @param num1 Min count (inclusive).
     * @param num2 Max count (inclusive).
     * @return Formatted string of characters in range.
     */
    public String getCharactersWithNumParts(int num1, int num2) { // [cite: 54]
        StringBuilder sb = new StringBuilder();
        sb.append("Characters with speaking parts between ").append(num1).append(" and ").append(num2).append(":\n");
        boolean found = false;
        for (int i = 0; i < characterNames.size(); i++) {
            int count = nameCounts.get(i);
            if (count >= num1 && count <= num2) { // [cite: 55]
                sb.append(characterNames.get(i)).append(": ").append(count).append("\n");
                found = true;
            }
        }
        if (!found) {
             sb.append("(None found in this range)\n");
        }
        return sb.toString();
    }


    // --- Combined Analysis and Output Generation ---

    /**
     * Runs both word frequency and character analyses on the file.
     * @param filename Path to the input file.
     * @return A single string containing formatted results for both analyses.
     */
     public String runFullAnalysisAndGetOutput(String filename) {
         // Run both analyses
         findUniqueWords(filename);       // Assignment 1 logic
         findAllCharacters(filename);    // Assignment 2 logic

         StringBuilder output = new StringBuilder();

         // --- Format Assignment 1 Output ---
         output.append("--- Word Frequency Analysis (Assignment 1) ---\n");
         output.append("Processing complete for: ").append(filename).append("\n\n");

         if (wordList.isEmpty() && wordFreqs.isEmpty()) {
              output.append("Word Frequency: No words found or file could not be processed.\n");
         } else {
             output.append("Number of unique words: ").append(wordList.size()).append("\n"); // [cite: 15] (Implicitly, part of tester logic)
             // Add the full word list
             for (int k = 0; k < wordList.size(); k++) {
                 output.append(wordFreqs.get(k)).append("\t").append(wordList.get(k)).append("\n"); // [cite: 15] (Implicitly, part of tester logic)
             }
             // Add the most frequent word
             int maxIndex = findIndexOfMaxWord(); // [cite: 20] (Implicitly, part of tester logic)
             if (maxIndex != -1) {
                  output.append("\nThe word that occurs most often and its count are: ")
                        .append(wordList.get(maxIndex)).append(" ")
                        .append(wordFreqs.get(maxIndex)).append("\n"); // [cite: 19] (Implicitly, part of tester logic)
             } else {
                  output.append("\nCould not determine the most frequent word.\n");
             }
         }

         // --- Separator ---
         output.append("\n========================================\n\n");

         // --- Format Assignment 2 Output ---
         output.append("--- Character Speaking Parts Analysis (Assignment 2) ---\n");

         if (characterNames.isEmpty()) {
              output.append("Character Analysis: No speaking characters found matching the criteria or file could not be processed.\n");
         } else {
             output.append("Total unique character names found: ").append(characterNames.size()).append("\n\n");

             // Main Character Logic [cite: 51, 52]
             int mainCharacterThreshold = 10; // Estimate this threshold [cite: 52]
             output.append("Main Characters (Speaking >= ").append(mainCharacterThreshold).append(" times):\n");
             boolean foundMain = false;
             for (int i = 0; i < characterNames.size(); i++) {
                 int count = nameCounts.get(i);
                 if (count >= mainCharacterThreshold) {
                     output.append(characterNames.get(i)).append(": ").append(count).append("\n"); // [cite: 51]
                     foundMain = true;
                 }
             }
              if (!foundMain) {
                 output.append("(None found above threshold)\n");
             }
             output.append("\n");

             // charactersWithNumParts call [cite: 55]
             output.append(getCharactersWithNumParts(10, 15)); // Example range test [cite: 55]
         }

         return output.toString();
     }


    /**
     * Main entry point. Handles file selection, runs combined analysis,
     * and displays results in a window.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // --- File Selection Logic (remains the same) ---
        JFileChooser chooser = new JFileChooser();
        File startingDir = null;
        String oneDrivePath = System.getenv("OneDrive");
        if (oneDrivePath == null || oneDrivePath.isEmpty()) { oneDrivePath = System.getenv("OneDriveConsumer"); }
        if (oneDrivePath != null && !oneDrivePath.isEmpty()) {
            File oneDriveDocuments = new File(oneDrivePath + File.separator + "Documents");
            if (oneDriveDocuments.isDirectory()) startingDir = oneDriveDocuments;
        }
        if (startingDir == null) {
            String localDocumentsPath = System.getProperty("user.home") + File.separator + "Documents";
            File localDocuments = new File(localDocumentsPath);
            if (localDocuments.isDirectory()) startingDir = localDocuments;
        }
        if (startingDir == null) { startingDir = new File(System.getProperty("user.home")); }
        chooser.setCurrentDirectory(startingDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnValue = chooser.showOpenDialog(null);
        String filename = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            filename = chooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("File selection cancelled by user.");
            return;
        }
        // --- End File Selection Logic ---

        if (filename != null) {
            // *** Create ONE instance and run the combined analysis ***
            CharactersInPlay analyzer = new CharactersInPlay(); // Changed instance name for clarity
            String finalOutput = analyzer.runFullAnalysisAndGetOutput(filename);

            // --- Display combined results in a GUI window (remains the same) ---
            JFrame frame = new JFrame("Combined Text Analysis Results");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(600, 700)); // Adjusted size

            JTextArea textArea = new JTextArea(finalOutput);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            // Optional: Set font
            // textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);

            frame.getContentPane().add(scrollPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}