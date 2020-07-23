package de.tum.in.ase.eist.audio;

public interface AudioPlayerInterface {

    public static final String BACKGROUND_MUSIC_FILE = "audio/happy_bg.mp3";
    public static final String SHARK_MUSIC_FILE = "audio/shark_approaching.mp3";
    public static final String CRASH_MUSIC_FILE = "audio/chomp.wav";
    public static final String LEVEL_UP_SFX_FILE = "audio/level_up.wav";
    
    String getCrashSoundFilePath();
    String getBackgroundMusicFilePath();
    default String getLevelUpSoundFilePath() {return LEVEL_UP_SFX_FILE;};
    default String getSharkMusicFilePath() {return SHARK_MUSIC_FILE;}; //default so that artemis tests can work
    void playBackgroundMusic();
    void stopBackgroundMusic();
    default void fadeOutToSharkMusic() {}; //default so that artemis tests can work
    boolean isPlayingBackgroundMusic();
    void playCrashSound();
    default void playLevelUpSound() {}; //default so that artemis tests can work
    boolean getCrashSoundPlayed();

}
