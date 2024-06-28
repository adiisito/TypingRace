package game;

import java.util.Random;

public class Text {

    private static String[] TextLibrary ={
            "The ancient Egyptians built the pyramids as tombs for their pharaohs and queens. The most famous pyramid, the Great Pyramid of Giza, was built for the Pharaoh Khufu and remains one of the Seven Wonders of the Ancient World.",
            "The Roman Empire was one of the largest and most influential empires in history. At its height, it spanned three continents, covering much of Europe, North Africa, and the Middle East. Its legal and political systems have greatly influenced Western civilization.",
            "The Great Wall of China, built to protect Chinese states and empires against the raids and invasions of nomadic groups, is over 13,000 miles long. Construction began as early as the 7th century BC, with several walls being built and later joined together.",
            "The Renaissance was a period of great cultural and intellectual growth in Europe, spanning from the 14th to the 17th century. It saw advances in art, science, and literature, with figures like Leonardo da Vinci, Michelangelo, and William Shakespeare leading the way.",
            "The Industrial Revolution, which began in the late 18th century in Britain, transformed economies from agrarian-based to industrial and manufacturing-based. This period saw the invention of machines such as the steam engine, which greatly increased production capabilities.",
            "World War I, also known as the Great War, lasted from 1914 to 1918. It involved many of the world's great powers and was marked by significant battles, including the Battle of the Somme and the Battle of Verdun, and led to significant political changes.",
            "World War II, lasting from 1939 to 1945, was the deadliest conflict in human history. It saw the rise and fall of Nazi Germany, the widespread use of tanks and aircraft in warfare, and the eventual use of atomic bombs by the United States on Hiroshima and Nagasaki.",
            "The fall of the Berlin Wall in 1989 symbolized the end of the Cold War and the division between the communist Eastern Bloc and the Western democratic nations. This event led to the reunification of Germany and significant political changes in Eastern Europe.",
            "The American Civil War, fought from 1861 to 1865, was a pivotal conflict in United States history. It was primarily fought over issues of slavery and states' rights. The Union's victory led to the abolition of slavery and significant social and economic changes.",
            "The French Revolution, beginning in 1789, was a period of radical social and political upheaval in France. It led to the end of the monarchy, the rise of Napoleon Bonaparte, and had a profound impact on the course of modern history.",
            "The discovery of the New World by Christopher Columbus in 1492 opened up a new era of exploration and colonization. This event led to significant cultural exchanges and the eventual establishment of European colonies throughout the Americas.",
            "The signing of the Magna Carta in 1215 by King John of England was a critical moment in the development of constitutional government. It established the principle that the king was subject to the law and laid the groundwork for parliamentary democracy.",
            "The Black Death, which struck Europe in the 14th century, was one of the most devastating pandemics in human history. It killed an estimated one-third of Europe's population and had profound social, economic, and cultural effects.",
            "The Enlightenment, spanning the 17th and 18th centuries, was an intellectual movement that emphasized reason, individualism, and skepticism of traditional authority. Key figures included John Locke, Voltaire, and Immanuel Kant.",
            "The abolition of the transatlantic slave trade in the 19th century was a significant milestone in human rights. Britain passed the Abolition of the Slave Trade Act in 1807, followed by the United States in 1808, leading to the eventual end of slavery in many parts of the world.",
            "The rise and fall of the Byzantine Empire, lasting from the fall of the Western Roman Empire in the 5th century until the fall of Constantinople in 1453, was marked by significant achievements in art, culture, and law, including the codification of Roman law.",
            "The Meiji Restoration in Japan, beginning in 1868, marked the end of the Tokugawa shogunate and the restoration of imperial rule. This period saw rapid modernization and industrialization, transforming Japan into a major world power.",
            "The spread of the printing press, invented by Johannes Gutenberg in the mid-15th century, revolutionized the dissemination of information. It played a crucial role in the spread of the Renaissance, Reformation, and the Scientific Revolution.",
            "The independence movements of the 20th century led to the end of colonial rule in many parts of Africa and Asia. Figures like Mahatma Gandhi in India and Nelson Mandela in South Africa became symbols of the struggle for freedom and equality.",
            "The Space Race between the United States and the Soviet Union during the Cold War led to significant advancements in space exploration. The U.S. landing of Apollo 11 on the moon in 1969 was a landmark achievement in human history."
    };

    public static String getRandomText() {
        return TextLibrary[new Random().nextInt(TextLibrary.length)];
    }
}
