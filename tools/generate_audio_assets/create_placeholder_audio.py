"""
Create short placeholder WAV files for letters and combined phonemes.
This script writes simple 16-bit PCM sine tones (0.5s) named using safe filenames.
No external Python packages required.
"""
import os
import wave
import math
import struct
import unicodedata

OUT_BASE = 'app/src/main/assets/audio'
LETTERS_DIR = os.path.join(OUT_BASE, 'letters')
COMBINED_DIR = os.path.join(OUT_BASE, 'combined')

LETTERS = ['अ','आ','इ','ई','उ','ए','ओ','क','ख','ग','घ','च']
VOWELS = ['ा','ि','ी','ु','ू','े','ै','ो','ौ']
CONSONANT = 'क'

os.makedirs(LETTERS_DIR, exist_ok=True)
os.makedirs(COMBINED_DIR, exist_ok=True)

RATE = 22050
DURATION = 0.5  # seconds
AMPLITUDE = 16000


def safe_filename(text: str) -> str:
    name = unicodedata.normalize('NFC', text)
    name = name.replace(' ', '_')
    name = ''.join(c for c in name if c.isalnum() or c in ('_', '-', '.'))
    if not name:
        name = 'item'
    return name


def write_sine(path, freq=440.0):
    n_samples = int(RATE * DURATION)
    with wave.open(path, 'w') as wf:
        wf.setnchannels(1)
        wf.setsampwidth(2)
        wf.setframerate(RATE)
        for i in range(n_samples):
            t = float(i) / RATE
            val = int(AMPLITUDE * math.sin(2.0 * math.pi * freq * t))
            data = struct.pack('<h', val)
            wf.writeframesraw(data)
        wf.writeframes(b'')


print('Generating placeholder letter WAVs...')
for idx, letter in enumerate(LETTERS):
    name = safe_filename(letter)
    path = os.path.join(LETTERS_DIR, f'{name}.wav')
    freq = 400.0 + (idx * 20)
    write_sine(path, freq=freq)
    print('  Created', path)

print('Generating placeholder combined WAVs...')
for idx, v in enumerate(VOWELS):
    phoneme = CONSONANT + v
    name = safe_filename(phoneme)
    path = os.path.join(COMBINED_DIR, f'{name}.wav')
    freq = 700.0 + (idx * 30)
    write_sine(path, freq=freq)
    print('  Created', path)

print('Done')
