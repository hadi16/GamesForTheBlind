package gamesforblind.synthesizer;

import java.util.ArrayList;

public class AudioPlayerExecutor {
    private final AudioPlayer audioPlayer;

    public AudioPlayerExecutor(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void replacePhraseAndPrint(Phrase relevantPhrase) {
        System.out.println(relevantPhrase.getPhraseValue());
        this.audioPlayer.replacePhraseToPlay(relevantPhrase);
    }

    public void replacePhraseAndPrint(ArrayList<Phrase> relevantPhraseList) {
        ArrayList<String> phraseStringList = new ArrayList<>();
        relevantPhraseList.forEach(phrase -> phraseStringList.add(phrase.getPhraseValue()));
        System.out.println(String.join(" ", phraseStringList));

        this.audioPlayer.replacePhraseToPlay(relevantPhraseList);
    }

    public void terminateAudioPlayer() {
        this.audioPlayer.terminateAudioPlayer();
    }
}
