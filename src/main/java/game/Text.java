package game;

import java.util.Random;

public class Text {

    private static String[] TextLibrary = {
            "Why don't scientists trust atoms? Because they make up everything!",
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "Why don't skeletons fight each other? They don't have the guts.",
            "What do you call fake spaghetti? An impasta!",
            "How does a penguin build its house? Igloos it together.",
            "Why did the math book look sad? Because it had too many problems.",
            "What do you call cheese that isn't yours? Nacho cheese!",
            "Why can't you give Elsa a balloon? Because she will let it go.",
            "How do you make a tissue dance? Put a little boogie in it!",
            "Why don't some couples go to the gym? Because some relationships don't work out.",
            "What do you call an alligator in a vest? An investigator.",
            "Why don't programmers like nature? It has too many bugs.",
            "How do you organize a space party? You planet.",
            "Why did the bicycle fall over? Because it was two-tired.",
            "Why was the math book always worried? Because it had too many problems.",
            "What do you call a bear with no teeth? A gummy bear.",
            "Why did the golfer bring extra pants? In case he got a hole in one.",
            "What did one ocean say to the other ocean? Nothing, they just waved.",
            "Why did the coffee file a police report? It got mugged.",
            "Why don't eggs tell jokes? They'd crack each other up."
    };

    public static String getRandomText() {
        return TextLibrary[new Random().nextInt(TextLibrary.length)];
    }
}


