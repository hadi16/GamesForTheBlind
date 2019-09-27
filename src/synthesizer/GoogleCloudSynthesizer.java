package synthesizer;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GoogleCloudSynthesizer {
    public static void createPhraseAudioFiles() {
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
                        .setAudioEncoding(AudioEncoding.MP3)
                        .build();

                SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
                ByteString audioContents = response.getAudioContent();

                try (OutputStream out = new FileOutputStream(phraseFile)) {
                    out.write(audioContents.toByteArray());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
