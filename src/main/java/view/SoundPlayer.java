package view;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Sound player class to get the sounds ready to play and contains methods used to play and stop the
 * sounds.
 */
public class SoundPlayer {

  private Clip clip;

  /**
   * Play sound.
   *
   * @param soundFileName the sound file name
   */
  public void playSound(String soundFileName) {
    try {
      File soundFile = new File(getClass().getClassLoader().getResource(soundFileName).getFile());
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
      clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  /** Stop sound. */
  public void stopSound() {
    if (clip != null && clip.isRunning()) {
      clip.stop();
    }
  }
}
