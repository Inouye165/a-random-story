import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser; // Import JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter; // Import FileNameExtensionFilter

/**
 * Determines the frequency of words in a file selected by the user.
 * Identifies and prints the word that occurs most often.
 * Based on Assignment 1 requirements from Programming Exercise: Telling a Random Story[cite: 1].
 */
public class WordFrequencies {

    // Private variables to store unique words and their frequencies [cite: 8, 9]
    private ArrayList<String> myWords;
    private ArrayList<Integer> myFreqs;

    /**
     * Constructor: Initializes the ArrayLists[cite: 10].
     */
    public WordFrequencies() {
        myWords = new ArrayList<String>();
        myFreqs = new ArrayList<Integer>();
    }

    /**
     * Reads words from a file, converts them to lowercase[cite: 3],
     * and counts the frequency of each unique word[cite: 13].
     * Punctuation is considered part of a word[cite: 4].
     * @param filename The path to the file to be processed.
     */
    public void findUnique(String filename) {
        // Clear previous data before processing a new file [cite: 11]
        myWords.clear();
        myFreqs.clear();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            // Iterate over every word in the file [cite: 12]
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase(); // Read word and make lowercase [cite: 3]

                int index = myWords.indexOf(word);

                if (index == -1) {
                    // If word is new, add it to myWords and add frequency 1 to myFreqs [cite: 12, 13]
                    myWords.add(word);
                    myFreqs.add(1);
                } else {
                    // If word exists, increment its frequency count in myFreqs [cite: 13]
                    int currentFreq = myFreqs.get(index);
                    myFreqs.set(index, currentFreq + 1);
                }
            }
            scanner.close(); // Close the scanner resource

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + filename);
            // e.printStackTrace(); // Optional for more detailed error info
        }
    }

    /**
     * Finds the index of the word with the highest frequency in myFreqs[cite: 17].
     * If there's a tie, returns the index of the first occurrence[cite: 18].
     * @return The index of the most frequent word, or -1 if lists are empty/null.
     */
    public int findIndexOfMax() {
        int maxFreq = 0;
        int maxIndex = -1; // Initialize to -1

        if (myFreqs != null && !myFreqs.isEmpty()) {
             maxFreq = myFreqs.get(0); // Start with the first frequency
             maxIndex = 0;
             for (int k = 1; k < myFreqs.size(); k++) {
                if (myFreqs.get(k) > maxFreq) {
                    maxFreq = myFreqs.get(k);
                    maxIndex = k; // Found a new max, update index
                }
             }
        }
        return maxIndex;
    }

    /**
     * Main method to run the frequency analysis.
     * Uses JFileChooser to let the user select a .txt file.
     * Attempts to default to the OneDrive Documents folder by checking environment variables,
     * otherwise falls back to local Documents or user home.
     * Prints the full list of words and frequencies, then the most frequent word[cite: 14, 15, 19, 20].
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // --- File Selection Logic ---
        JFileChooser chooser = new JFileChooser();
        File startingDir = null; // Variable to hold the chosen starting directory

        // 1. Try finding OneDrive path via Environment Variables
        String oneDrivePath = System.getenv("OneDrive"); // Check %OneDrive% variable
        if (oneDrivePath == null || oneDrivePath.isEmpty()) {
             oneDrivePath = System.getenv("OneDriveConsumer"); // Check %OneDriveConsumer% as alternative
        }

        if (oneDrivePath != null && !oneDrivePath.isEmpty()) {
            // If OneDrive path found, try appending "Documents"
            File oneDriveDocuments = new File(oneDrivePath + File.separator + "Documents");
            if (oneDriveDocuments.isDirectory()) {
                startingDir = oneDriveDocuments;
                System.out.println("Setting starting directory to OneDrive Documents (via environment variable).");
            }
        }

        // 2. If OneDrive Documents not found, try standard local Documents folder
        if (startingDir == null) {
            String localDocumentsPath = System.getProperty("user.home") + File.separator + "Documents";
            File localDocuments = new File(localDocumentsPath);
            if (localDocuments.isDirectory()) {
                startingDir = localDocuments;
                System.out.println("Setting starting directory to Local Documents.");
            }
        }

        // 3. If neither Documents path worked, fall back to user home directory
        if (startingDir == null) {
            startingDir = new File(System.getProperty("user.home"));
            System.out.println("Setting starting directory to User Home.");
        }

        // Set the determined starting directory for the chooser
        chooser.setCurrentDirectory(startingDir);

        // Filter for .txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false); // Don't show "All Files" option by default

        // Show the file chooser dialog
        int returnValue = chooser.showOpenDialog(null); // Pass null for parent component

        String filename = null;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // User selected a file
            File selectedFile = chooser.getSelectedFile();
            filename = selectedFile.getAbsolutePath();
        } else {
            // User cancelled or closed the dialog
            System.out.println("File selection cancelled by user.");
            return; // Exit if no file selected
        }
        // --- End File Selection Logic ---


        // Proceed only if a filename was successfully obtained
        if (filename != null) {
            WordFrequencies wf = new WordFrequencies();
            System.out.println("Processing file: " + filename);
            wf.findUnique(filename); // Process the selected file [cite: 14]

            // --- Print the Results ---
            System.out.println("\nNumber of unique words: " + wf.myWords.size()); // [cite: 15]

            // Loop to print all words and their frequencies [cite: 15]
            for (int k = 0; k < wf.myWords.size(); k++) {
                // Print frequency then the word, separated by a tab
                System.out.println(wf.myFreqs.get(k) + "\t" + wf.myWords.get(k));
            }

            // Find and print the most frequent word [cite: 19, 20]
            int maxIndex = wf.findIndexOfMax(); // [cite: 20]
            if (maxIndex != -1) {
                 // Example output format: "The word that occurs most often and its count are: a 3" [cite: 22]
                 System.out.println("\nThe word that occurs most often and its count are: " +
                                   wf.myWords.get(maxIndex) + " " + wf.myFreqs.get(maxIndex));
            } else if (wf.myWords.isEmpty()){
                 System.out.println("No words found in the file or file was empty.");
            } else {
                 System.out.println("Could not determine the most frequent word.");
            }
        }
    }
}