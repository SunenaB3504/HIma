Generate offline audio assets for Hima

This tool synthesizes TTS audio for letters and combined phonemes and writes them into the project's assets folder.

Prerequisites
- Python 3.8+
- Install required packages:
  - Required: pyttsx3
  - Optional (for MP3 output): pydub and ffmpeg on PATH

Install (example):

```powershell
python -m pip install -r tools\generate_audio_assets\requirements.txt
```

Usage examples

Generate WAV + MP3 (if pydub+ffmpeg available) into the app assets:

```powershell
python tools\generate_audio_assets\generate_audio_assets.py --out app/src/main/assets/audio --format both
```

Generate using a custom list of letters (UTF-8 file, one per line):

```powershell
python tools\generate_audio_assets\generate_audio_assets.py --letters docs/sample_letters.txt --out app/src/main/assets/audio --format wav
```

Notes
- On Windows pyttsx3 uses SAPI voices. If you need Hindi voices, ensure appropriate voices are installed and pass --voice "VoiceName".
- Filenames are normalized to remove problematic characters.
