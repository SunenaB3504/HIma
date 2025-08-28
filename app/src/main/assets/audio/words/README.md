Placeholder audio files for sample assets.

adarak.mp3 is a placeholder. Replace with a real MP3 narration file at the same path:

app/src/main/assets/audio/words/adarak.mp3

To generate using TTS and ffmpeg (example):
1. Use Android TTS or any TTS service to create WAV/MP3.
2. Convert to MP3 using ffmpeg:
   ffmpeg -i adarak.wav -ar 22050 -ac 1 -b:a 64k adarak.mp3

Keep filenames and relative paths intact so the app's AssetLoader can find them.
