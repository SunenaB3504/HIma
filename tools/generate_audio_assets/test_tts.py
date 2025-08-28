import pyttsx3
import sys
import os

out = 'app/src/main/assets/audio/test_tts.wav'
try:
    engine = pyttsx3.init()
    voices = engine.getProperty('voices')
    print('voices found:', len(voices))
    for i, v in enumerate(voices):
        print(i, 'id=', getattr(v, 'id', None), 'name=', getattr(v, 'name', None))
    text = 'क'
    os.makedirs(os.path.dirname(out), exist_ok=True)
    print('saving to', out)
    engine.save_to_file(text, out)
    engine.runAndWait()
    print('done')
except Exception as e:
    print('error:', e)
    sys.exit(1)
