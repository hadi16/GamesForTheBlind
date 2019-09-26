package synthesizer;

import com.google.cloud.texttospeech.v1.*;
import com.google.common.hash.Hashing;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GoogleCloudSynthesizer {
    public static void createPhraseAudioFiles() {
        ArrayList<String> allPhrases = new ArrayList<>();
        for (Phrase phrase : Phrase.values()) {
            allPhrases.add(phrase.getPhraseValue());
        }

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            for (String phraseValue : allPhrases) {
                String phraseSha256Hash = Hashing.sha256().hashString(phraseValue, StandardCharsets.UTF_8).toString();
                File phraseFile = new File("phrases/" + phraseSha256Hash + ".mp3");
                if (phraseFile.exists()) {
                    continue;
                }

                SynthesisInput input = SynthesisInput.newBuilder()
                        .setText(phraseValue)
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
