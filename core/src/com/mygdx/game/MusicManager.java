package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager { //отвечает за музыку в игре
    public static Music music;
    private static Music sound;
    private static Music voiceSound;
    private static Music clicksong;
    private static Music fonmusic;
    public static void playMusic(String filePath, boolean loop) { //проиграть фоновую музыку
        if (music != null) {
            music.stop();
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        music.setLooping(loop);
        music.play();
    }

    public static void playSound(SoundType soundType, boolean loop) {//String filePath, boolean loop) { //проиграть звук (аги,двери, и тд)
        if (sound != null) {
            sound.stop();
            sound.dispose();
        }
        sound = Gdx.audio.newMusic(Gdx.files.internal(soundType.getFilepath()));
        sound.setLooping(loop);
        sound.play();
    }

    public static void playVoiceSound(VoiceSoundType soundType, boolean loop) {//String filePath, boolean loop) { //проиграть горлоса перснажей
        if (voiceSound != null) {
            voiceSound.stop();
            voiceSound.dispose();
        }
        voiceSound = Gdx.audio.newMusic(Gdx.files.internal(soundType.getFilepath()));
        voiceSound.setLooping(loop);
        voiceSound.play();
    }


    public static void playSound(String filePath, boolean loop) {//String filePath, boolean loop) { проиграть музыку предметов
        if (clicksong != null) {
            clicksong.stop();
            clicksong.dispose();
        }
        clicksong = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        clicksong.setLooping(loop);
        clicksong.play();
    }
    public static void pauseMusic() {//String filePath, boolean loop) { проиграть музыку предметов
            if (music != null && music.isPlaying()) {
                music.pause();
            }}

    public static void resumeMusic() {//String filePath, boolean loop) { проиграть музыку предметов
        if (music != null && !music.isPlaying()) {
            music.play();
        }}
    public static void playFonMusic (String filePath, boolean loop) {//String filePath, boolean loop) { проиграть музыку предметов
        if (fonmusic != null) {
            fonmusic.stop();
            fonmusic.dispose();
        }
        fonmusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        fonmusic.setLooping(loop);
        fonmusic.play();
    }

    public static void stopSound() {
        if (sound != null && sound.isPlaying()) {
            sound.stop();
            sound.dispose();
            sound = null;
        }
    }
    public static void stopFonMusic() {
        if (fonmusic != null && fonmusic.isPlaying()) {
            fonmusic.stop();
            fonmusic.dispose();
            fonmusic = null;
        }
    }

    public static void pauseSound() {
        if (sound != null && sound.isPlaying())
            sound.pause();
    }

    public static void playSound() {
        if (sound != null)
            sound.play();
    }

    public static void dispose() {
        if (music != null) {
            music.stop();
            music.dispose();
        }
        if (sound != null) {
            sound.stop();
            sound.dispose();
        }
    }

    public enum SoundType {
        DOOR("songs/door.mp3"), STEPS("songs/step.mp3"), DOORLOCK("songs/doorlock.mp3"),  IVA ("songs/iva.mp3"), OLEANDR ("songs/oleandr.mp3"), SUCSESS("songs/success.wav");
        private String filepath;

        SoundType(String filepath) {
            this.filepath = filepath;
        }

        public String getFilepath() {
            return filepath;
        }
    }

    public enum VoiceSoundType {
        IVA ("songs/iva.mp3"), OLEANDR ("songs/oleandr.mp3");
        private String filepath;

        VoiceSoundType(String filepath) {
            this.filepath = filepath;
        }

        public String getFilepath() {
            return filepath;
        }
    }
}
