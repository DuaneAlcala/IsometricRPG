package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookMaker {

    private List<String> bookContents = new ArrayList<>();

    public BookMaker() {
        File file = new File("src/utility/res/bookTexts.txt");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String bookContent;
            while((bookContent = reader.readLine()) != null) {
                bookContents.add(bookContent);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandContent() {
        Random rand = new Random();
        int index = rand.nextInt(bookContents.size());
        return bookContents.get(index);
    }
}
