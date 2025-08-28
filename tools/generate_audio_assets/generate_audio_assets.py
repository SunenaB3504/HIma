#!/usr/bin/env python3
"""
Generate offline audio assets for letters and combined phonemes using local TTS (pyttsx3).

Behavior:
- Synthesizes WAV files using pyttsx3. Attempts to convert WAV to MP3 using pydub if available and ffmpeg is installed.
- Writes files into the chosen output directory, defaulting to app/src/main/assets/audio/letters and audio/combined.
- Produces safe filenames by replacing spaces with underscores and normalizing characters.

Usage (PowerShell):
> python tools\generate_audio_assets\generate_audio_assets.py --out app/src/main/assets/audio --format both

Notes:
- Install dependencies: pyttsx3 is required. pydub + ffmpeg optional for MP3 output.
- On Windows pyttsx3 uses SAPI; you can select a voice by name if available.

"""
import argparse
import os
import sys
import time
import unicodedata


def safe_filename(text: str) -> str:
    # normalize and replace spaces
    name = unicodedata.normalize('NFC', text)
    name = name.replace(' ', '_')
    # optionally remove any path-unfriendly characters
    name = ''.join(c for c in name if c.isalnum() or c in ('_', '-', '.'))
    if not name:
        name = 'item'
    # append codepoint suffix to ensure uniqueness for multi-char strings (matras etc.)
    codepoints = '-'.join(f'{ord(c):x}' for c in text)
    return f"{name}_{codepoints}"


def ensure_dir(path: str):
    os.makedirs(path, exist_ok=True)


def try_import(module_name: str):
    try:
        return __import__(module_name)
    except Exception:
        return None


def synthesize_with_pyttsx3(text: str, wav_path: str, voice_name: str = None, rate: int = None):
    pyttsx3 = try_import('pyttsx3')
    if not pyttsx3:
        raise RuntimeError('pyttsx3 is not installed. Install with: pip install pyttsx3')

    engine = pyttsx3.init()
    # optional configuration
    if voice_name:
        voices = engine.getProperty('voices')
        for v in voices:
            if voice_name.lower() in (v.name or '').lower() or voice_name.lower() in (v.id or '').lower():
                engine.setProperty('voice', v.id)
                break
    if rate:
        try:
            engine.setProperty('rate', rate)
        except Exception:
            pass

    # pyttsx3 save to file
    engine.save_to_file(text, wav_path)
    engine.runAndWait()


def wav_to_mp3(wav_path: str, mp3_path: str):
    # optional pydub + ffmpeg conversion
    pydub = try_import('pydub')
    if not pydub:
        raise RuntimeError('pydub not available; cannot convert to mp3. Install with: pip install pydub. Requires ffmpeg.')
    from pydub import AudioSegment
    AudioSegment.from_wav(wav_path).export(mp3_path, format='mp3')


def main():
    parser = argparse.ArgumentParser(description='Generate audio assets for Hima (letters and combined phonemes).')
    parser.add_argument('--out', default='app/src/main/assets/audio', help='Output base directory for audio files')
    parser.add_argument('--format', choices=['wav', 'mp3', 'both'], default='both', help='Output format')
    parser.add_argument('--voice', default=None, help='Preferred TTS voice name (optional)')
    parser.add_argument('--rate', type=int, default=None, help='Speech rate (optional)')
    parser.add_argument('--letters', default=None, help='Path to a file listing letters (one per line). If missing, built-in set used.')
    parser.add_argument('--consonant', default='क', help='Base consonant for combined sounds (default: क)')
    args = parser.parse_args()

    # default small set of Devanagari letters (adjustable)
    default_letters = ['अ','आ','इ','ई','उ','ए','ओ','क','ख','ग','घ','च','छ','ज','झ','ट','ठ','ड','ढ','त','थ','द','ध','न','प','फ','ब','भ','म','य','र','ल','व','स','ह']

    letters = default_letters
    if args.letters and os.path.isfile(args.letters):
        with open(args.letters, 'r', encoding='utf-8') as f:
            letters = [line.strip() for line in f if line.strip()]

    out_base = args.out.rstrip('/\\')
    letters_dir = os.path.join(out_base, 'letters')
    combined_dir = os.path.join(out_base, 'combined')

    ensure_dir(letters_dir)
    ensure_dir(combined_dir)

    print(f'Generating audio in {out_base} for {len(letters)} letters...')

    failed = []
    success = []
    for letter in letters:
        name = safe_filename(letter)
        wav_path = os.path.join(letters_dir, f'{name}.wav')
        mp3_path = os.path.join(letters_dir, f'{name}.mp3')
        try:
            synthesize_with_pyttsx3(letter, wav_path, voice_name=args.voice, rate=args.rate)
            if args.format in ('mp3', 'both'):
                try:
                    wav_to_mp3(wav_path, mp3_path)
                except Exception as e:
                    print(f'  mp3 conversion failed for {letter}: {e}')
            success.append((letter, wav_path))
            print(f'  OK: {letter} -> {wav_path}')
            # small delay to avoid spamming the TTS engine
            time.sleep(0.05)
        except Exception as e:
            print(f'  FAIL: {letter}: {e}')
            failed.append((letter, str(e)))

    # generate combined phonemes using simple Hindi vowel matras
    vowels = ['ा','ि','ी','ु','ू','े','ै','ो','ौ']
    consonant = args.consonant
    print(f'Generating combined phonemes for consonant: {consonant}')
    for v in vowels:
        phoneme = consonant + v
        name = safe_filename(phoneme)
        wav_path = os.path.join(combined_dir, f'{name}.wav')
        mp3_path = os.path.join(combined_dir, f'{name}.mp3')
        try:
            synthesize_with_pyttsx3(phoneme, wav_path, voice_name=args.voice, rate=args.rate)
            if args.format in ('mp3', 'both'):
                try:
                    wav_to_mp3(wav_path, mp3_path)
                except Exception as e:
                    print(f'  mp3 conversion failed for {phoneme}: {e}')
            success.append((phoneme, wav_path))
            print(f'  OK: {phoneme} -> {wav_path}')
            time.sleep(0.05)
        except Exception as e:
            print(f'  FAIL: {phoneme}: {e}')
            failed.append((phoneme, str(e)))

    print('\nSummary:')
    print(f'  Successes: {len(success)}')
    print(f'  Failures: {len(failed)}')
    if failed:
        print('Failed items:')
        for f_ in failed:
            print(' ', f_)

    # exit code
    if failed:
        sys.exit(2)
    sys.exit(0)
