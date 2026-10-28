package jbdl.session02.abstraction;

/**
 * The diamond problem with default methods: Camera and MusicPlayer both define describe(). Java
 * refuses to guess, so the class MUST override it and may call either parent with X.super.method().
 */
public class SmartPhone implements Camera, MusicPlayer {

    @Override
    public String describe() {
        return Camera.super.describe() + " + " + MusicPlayer.super.describe();
    }
}

interface Camera {
    default String describe() {
        return "takes photos";
    }
}

interface MusicPlayer {
    default String describe() {
        return "plays music";
    }
}
