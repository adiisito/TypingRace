package game;

import java.util.Random;

public class Text {

    private static String[] TextLibrary ={
            "What they do not comprehend is the helplessness of a man. I am weak, small, and of no consequence to the universe.  It does not notice me; I live on unseen.  But why is that bad?  Isn’t it better that way?  Whom the gods notice they destroy.  But small...and you will escape the jealousy of the great.",
            "They picked a way among the trees, and their ponies plodded along, carefully avoiding the many writhing and interlacing roots.  There was no undergrowth.  The ground was rising steadily, and as they went forward it seemed that the trees became taller, darker, and thicker. There was no sound, except an occasional drip of moisture falling through the still leaves.",
            "The old man drank his coffee slowly.  It was all he would have all day and he knew that he should take it.  For a long time now eating had bored him and he never carried a lunch.  He had a bottle of water in the bow of the skiff and that was all he needed for the day.\n" +
            "The four young faces on which the firelight shone brightened at the cheerful words, but darkened again as Jo said sadly, \"We haven't got Father, and shall not have him for a long time.\" She didn't say \"perhaps never,\" but each silently added it, thinking of Father far away, where the fighting was."

    };


    public static String getRandomText() {
        //gets a random texts from the list of texts we have
        return TextLibrary[new Random().nextInt(TextLibrary.length)];
    }
}
