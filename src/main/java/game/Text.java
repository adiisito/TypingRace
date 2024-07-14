package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/** The type Text. */
public class Text {

  private static final Map<String, String[]> textLibrary = new HashMap<>();

  static {
    textLibrary.put(
        "Random",
        new String[] {
          "Honey never spoils. Archaeologists have found pots of honey in ancient Egyptian"
              + " tombs that are over 3,000 years old and still edible.",
          "A group of flamingos is called a 'flamboyance'.",
          "Bananas are berries, but strawberries aren't.",
          "Russia is bigger than Pluto.",
          "Australia is wider than the moon.",
          "A day on Venus is longer than a year on Venus.",
          "There are more stars in the universe than grains of sand on all the Earth's beaches.",
          "More people live in Tokyo than in all of Canada.",
          "The shortest war in history was between Britain and Zanzibar on August 27, 1896."
              + " Zanzibar surrendered after 38 minutes.",
          "There are more fake flamingos in the world than real flamingos.",
          "The Eiffel Tower can be 15 cm taller during the summer.",
          "Octopuses have three hearts and blue blood.",
          "Venus is the hottest planet in our solar system, "
              + "even though Mercury is closer to the Sun.",
          "A bolt of lightning contains enough energy to toast 100,000 slices of bread.",
          "Polar bears have black skin under their white fur.",
          "Koalas have fingerprints that are almost indistinguishable from human fingerprints.",
          "The shortest war in history lasted only 38 minutes.",
          "Cleopatra lived closer in time to the Moon landing than to the construction "
              + "of the Great Pyramid of Giza.",
          "There are more public libraries in the U.S. than McDonald's restaurants.",
          "Antarctica is the only continent without a native species of ants."
        });
    textLibrary.put(
        "English",
        new String[] {
          "The best way to get started is to quit talking and begin doing.",
          "The pessimist sees difficulty in every opportunity. The optimist "
              + "sees opportunity in every difficulty.",
          "Don't let yesterday take up too much of today.",
          "You learn more from failure than from success. Don't let it stop you. "
              + "Failure builds character.",
          "It's not whether you get knocked down, it's whether you get up.",
          "If you are working on something that you really care about, you don't "
              + "have to be pushed. The vision pulls you.",
          "People who are crazy enough to think they can change the world, are the ones who do.",
          "Failure will never overtake me if my determination to succeed is strong enough.",
          "Entrepreneurs are great at dealing with uncertainty and also very "
              + "good at minimizing risk. That's the classic entrepreneur.",
          "We may encounter many defeats but we must not be defeated.",
          "Knowing is not enough; we must apply. Wishing is not enough; we must do.",
          "We generate fears while we sit. We overcome them by action.",
          "Whether you think you can or think you can't, you're right.",
          "Security is mostly a superstition. Life is either a daring adventure or nothing.",
          "The man who has confidence in himself gains the confidence of others.",
          "The only limit to our realization of tomorrow is our doubts of today.",
          "Creativity is intelligence having fun.",
          "What you lack in talent can be made up with desire, "
              + "hustle and giving 110% all the time.",
          "Do what you can with all you have, wherever you are.",
          "Develop an 'attitude of gratitude'. Say thank you to everyone you "
              + "meet for everything they do for you."
        });
    textLibrary.put(
        "German",
        new String[] {
          "Deutschland hat die groesste Bevoelkerung in der Europaeeischen Union.",
          "Der Rhein ist der laengste Fluss Deutschlands.",
          "Der Schwarzwald ist bekannt fuer seine dichten, immergruenen Waelder.",
          "In Deutschland gibt es ueber 1.500 verschiedene Biersorten.",
          "Die deutsche Autobahn hat keine allgemeine Geschwindigkeitsbegrenzung.",
          "Berlin ist neunmal groesser als Paris.",
          "Deutschland grenzt an neun Laender, mehr als jedes andere Land in Europa.",
          "In Deutschland gibt es mehr als 20.000 Burgen und Schloesser.",
          "Die deutsche Sprache hat etwa 5,3 Millionen Woerter.",
          "Deutschland ist das Geburtsland von Albert Einstein.",
          "Das Oktoberfest in Muenchen ist das groesste Volksfest der Welt.",
          "Die Donau fliesst durch 10 Laender, mehr als jeder andere Fluss der Welt.",
          "Deutschland hat die groesste Wirtschaft in Europa.",
          "Der Berliner Fernsehturm ist das hoechste Bauwerk Deutschlands.",
          "Deutschland hat mehr Zoos als jedes andere Land der Welt.",
          "Deutschland ist bekannt fuer seine Dichter wie Goethe und Schiller.",
          "Der Schwarzwald ist eine der beliebtesten Touristenattraktionen Deutschlands.",
          "In Deutschland gibt es ueber 300 verschiedene Brotsorten.",
          "Die deutsche Flagge besteht aus drei horizontalen Streifen: Schwarz, Rot und Gold.",
          "Deutschland ist bekannt fuer seine Autobahn, auf der es keine "
              + "Geschwindigkeitsbegrenzung gibt.",
          "Deutschland ist das Land der Dichter und Denker.",
          "Deutschland hat die hoechste Anzahl von UNESCO-Welterbestaetten in Europa.",
          "Deutschland hat mehr als 1.500 verschiedene Biersorten.",
          "Die deutsche Hauptstadt Berlin ist neunmal groesser als Paris.",
          "Deutschland ist das Geburtsland von Beethoven, Bach und Brahms.",
          "In Deutschland gibt es mehr als 20.000 Burgen und Schloesser.",
          "Die deutsche Autobahn hat keine allgemeine Geschwindigkeitsbegrenzung.",
          "Deutschland hat die groesste Wirtschaft in Europa.",
          "Der Rhein ist der laengste Fluss Deutschlands.",
          "Deutschland ist bekannt fuer seine Waelder und Schluchten."
        });

    textLibrary.put(
        "Dad Jokes",
        new String[] {
          "I'm reading a book on anti-gravity. It's impossible to put down!",
          "Why don't skeletons fight each other? They don't have the guts.",
          "What do you call fake spaghetti? An impasta!",
          "How does a penguin build its house? Igloos it together.",
          "Why don't some couples go to the gym? Because some relationships don't work out.",
          "I would avoid the sushi if I was you. It's a little fishy.",
          "Want to hear a joke about construction? I'm still working on it.",
          "Why don't programmers like nature? It has too many bugs.",
          "How do you organize a space party? You planet.",
          "Why did the scarecrow win an award? Because he was outstanding in his field!",
          "I'm on a seafood diet. I see food and I eat it.",
          "Why did the bicycle fall over? Because it was two-tired.",
          "I'm reading a book on anti-gravity. It's impossible to put down!",
          "Why don't some couples go to the gym? Because some relationships don't work out.",
          "Want to hear a joke about construction? I'm still working on it.",
          "Why don't skeletons fight each other? They don't have the guts.",
          "What do you call fake spaghetti? An impasta!",
          "How does a penguin build its house? Igloos it together.",
          "Why don't some couples go to the gym? Because some relationships don't work out.",
          "I would avoid the sushi if I was you. It's a little fishy."
        });
  }

  /**
   * Gets random text by category.
   *
   * @param category the category
   * @return the random text by category
   */
  public static String getRandomTextByCategory(String category) {
    String[] texts = textLibrary.getOrDefault(category, textLibrary.get("Random"));
    return texts[new Random().nextInt(texts.length)];
  }
}
