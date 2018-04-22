package Java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class ReadNWrite implements Runnable {

    static int lastFileNum = 0;
    private int fileNumber = 0;
    private String filesPath = "";

    public ReadNWrite() {
        super();
        // set the file number
        setFileNumber();
        try {
            filesPath = new java.io.File("./resources/ProjectFiles/Files/file").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     */
    private void setFileNumber() {
        if (lastFileNum < 5 && fileNumber > -1) {
            fileNumber = lastFileNum + 1;
            lastFileNum++;
        } else
            fileNumber = -1;

    }

    @Override
    public void run() {
        List<String> toWrite = new ArrayList<>();
        try {
            // check if the file number is valid
            if (fileNumber > 0 && fileNumber < 5) {
                // Generates the file path to read a specific file
                String file = filesPath + fileNumber + Main.FILEEXTENTION;
                // Reader reads the whole line from the file, which is actually
                // a word and passes to the line String
                @SuppressWarnings("resource")
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                // creating the list of words /The whole File
                List<String> words = new ArrayList<>();
                while (line != null) {
                    String[] wordsLine = line.split(" ");
                    Collections.addAll(words, wordsLine);
                    line = reader.readLine();
                }

                // here we go through the list of words /The whole file, and
                // choose only 50 words randomly.
                for (int i = 0; i < 50; i++) {
                    Random rand;
                    String tempWord;
                    if (i == 0)
                        rand = new Random(System.currentTimeMillis());
                    else
                        rand = new Random(System.currentTimeMillis() * i);
                    tempWord = words.get(rand.nextInt(words.size()));
                    while (toWrite.contains(tempWord))
                        tempWord = words.get(rand.nextInt(words.size()));
                    toWrite.add(tempWord);
                }

                // write the 50 chosen word to allListsToFileContainer
                setAllListsToFileContainer(fileNumber, toWrite);

                // if the file number is not between 1 and 4, somebody must have
                // tampered our variable ...
            } else {
                System.out.println("Oops, Something is wrong with File Numbering!");
            }

        } catch (IOException e) {
            System.out.println("Oops, Something went wrong \n " + e.toString());
        }
    }

    private void setAllListsToFileContainer(int index, List<String> lsitToAdd) {
        // the index is always going to be from 1 to 4, need to make sure where
        // we write is 0 to 3
        Main.allListsToFileContainer.setFile(index, lsitToAdd);
    }

}
