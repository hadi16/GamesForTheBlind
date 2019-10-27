package gamesforblind.synthesizer;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AudioFileBuilder {
    private final File phraseDirectory;

    public AudioFileBuilder() {
        this.phraseDirectory = new File(Phrase.PHRASE_FILES_DIRECTORY.toString());
        if (!this.phraseDirectory.exists()) {
            this.phraseDirectory.mkdirs();
        }
    }

    public void deleteOldPhraseAudioFiles() {
        File[] filesInDirectory = this.phraseDirectory.listFiles();
        if (filesInDirectory == null) {
            return;
        }
        ArrayList<File> audioFiles = new ArrayList<>(Arrays.asList(filesInDirectory));

        for (Phrase phrase : Phrase.values()) {
            audioFiles.remove(phrase.getPhraseAudioFile());
        }

        for (File oldPhraseAudioFile : audioFiles) {
            if (!oldPhraseAudioFile.isDirectory()) {
                oldPhraseAudioFile.delete();
                System.out.println("Deleted audio file: " + oldPhraseAudioFile.getPath());
            }
        }
    }

    public void createPhraseAudioFiles() {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            for (Phrase phrase : Phrase.values()) {
                File phraseFile = phrase.getPhraseAudioFile();
                if (phraseFile.exists()) {
                    continue;
                }

                SynthesisInput input = SynthesisInput.newBuilder()
                        .setText(phrase.getPhraseValue())
                        .build();

                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode("en-US")
                        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                        .build();

                AudioConfig audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.LINEAR16)
                        .build();

                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                ByteString audioContents = response.getAudioContent();

                try (OutputStream out = new FileOutputStream(phraseFile)) {
                    out.write(audioContents.toByteArray());
                }
                System.out.println("Phrase audio file successfully saved: " + phrase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
