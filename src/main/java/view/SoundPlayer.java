package view;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

  private Clip clip;

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

  public void stopSound() {
    if (clip != null && clip.isRunning()) {
      clip.stop();
    }
  }
}
