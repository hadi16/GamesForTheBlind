package audio_player;

public enum AudioOption {
    PAUSE(1),
    STOP(2),
    RESUME(3);

    private final int optionValue;

    AudioOption(int optionValue) {
        this.optionValue = optionValue;
    }

    public int getOptionValue() {
        return this.optionValue;
    }
}
