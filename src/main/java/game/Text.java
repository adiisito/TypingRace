package game;

import java.util.Random;

public class Text {

    private static String[] TextLibrary ={
            "Example Text",
            "Example Text",
            "Example Text"

    };


    public static String getRandomText() {
        //gets a random texts from the list of texts we have
        return TextLibrary[new Random().nextInt(TextLibrary.length)];
    }
}
