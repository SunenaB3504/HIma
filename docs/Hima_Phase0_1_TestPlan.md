# Hima — Phase 0 & Phase 1 Test Plan (Tablet)

Date: August 28, 2025

Purpose
- Provide a clear, step-by-step testing methodology, test cases, and expected results to validate Phase 0 (project skeleton, onboarding/navigation/DI) and Phase 1 (asset pipeline, validator, sample assets, app-side demo) on an Android tablet.

Scope
- Manual testing on a physical Android tablet (recommended) or an emulator configured to match a target tablet profile (Android 6.0/API 23+).
- Verify UI flows from onboarding to main menu (Phase 0).
- Verify asset pipeline readiness and app ability to access bundled JSON and perform audio fallback (Phase 1).

Test environment & prerequisites
- Device: Android tablet with API 23+ (recommended 8.0+ for stability). Note device model, Android version, screen size and density in test logs.
- Host machine: development PC with Android SDK, adb, and Gradle.
- Build: Debug APK built from repository: run in repo root:

```powershell
./gradlew assembleDebug
```

-- Install & test over Wi‑Fi (preferred)

Use this method to pair and test the device over the same Wi‑Fi network instead of a USB install. Both the host and tablet must be on the same network.

1) Prepare device and host
- Enable Developer Options and USB Debugging on the tablet (Settings → About → tap Build Number 7 times → Developer options → USB debugging).
- Connect the tablet to the host once via USB to authorize the host for adb access (you may be prompted to accept the RSA key on the tablet).

2) Put adb into TCP/IP mode and get device IP

```powershell
# put adb daemon into tcpip mode on port 5555
adb tcpip 5555

# find the tablet's IP address (simplest: check Settings → About → Status → IP address)
# or run from the host (output includes the inet entry with the IP):
adb shell ip -f inet addr show wlan0
```

3) Connect adb over Wi‑Fi and install APK

Replace <DEVICE_IP> with the tablet IP found above, for example 192.168.1.42.

```powershell
# connect to device over network
adb connect <DEVICE_IP>:5555

# verify device is listed (should show <DEVICE_IP>:5555)
adb devices

# install the debug APK over Wi‑Fi
adb -s <DEVICE_IP>:5555 install -r app\build\outputs\apk\debug\app-debug.apk
```

4) When finished, disconnect and revert to USB mode if required

```powershell
# disconnect the network adb connection
adb disconnect <DEVICE_IP>:5555

# restore USB mode (optional)
adb usb
```

Notes:
- If your tablet supports "Wireless debugging" (Android 11+), you can use the built-in pairing flow in Developer Options → Wireless debugging instead of the tcpip method.
- If the adb connect step fails, ensure the host and device are on the same network and that no VPN/firewall is blocking traffic.
- For capture of logs/screenshots while testing over Wi‑Fi, `adb logcat` and `adb exec-out screencap -p` work the same as with USB.

- Ensure offline TTS language packs available (optional): Settings → Languages & input → Text-to-speech → Install voice data for Hindi/Marathi if available. This is for fallback verification.
- Ensure `docs` validator has been run on the host (already done):

```powershell
python tools\validate_assets\validate_assets.py --schemas docs\json_schemas --assets docs\sample_assets
```

Test methodology
- Manual exploratory + scripted test cases below.
- For each test case: follow steps exactly, record observed result, mark Pass/Fail, and capture screenshots/logs where useful.
- If an audio playback test is required, ensure audio files exist in `app/src/main/assets/audio/...` or expect TTS fallback to speak the text.

Reporting
- Use the following template when reporting a failed test:
  - Test ID:
  - Device (make/model/android version):
  - App build (versionCode/versionName):
  - Steps performed:
  - Expected result:
  - Actual result (attach screenshot and logcat excerpt):
  - Severity/Priority:

Phase 0 — Test cases (Onboarding & Navigation)

T0-01 — Launch & Onboarding screen
- Priority: High
- Steps:
  1. Install and launch the debug APK on tablet.
  2. Observe first screen after cold start.
- Expected result:
  - On first launch, the onboarding screen is shown with clear language choices (Hindi, Marathi) and a welcome message.
  - Buttons are large and tappable.
- Pass criteria: Onboarding screen visible and responsive.

T0-02 — Language selection navigates to Main Menu
- Priority: High
- Steps:
  1. From Onboarding screen, tap "Hindi".
  2. Observe navigation behavior.
- Expected result:
  - The app navigates from onboarding to the main menu screen.
  - Main menu shows two primary options: "Learn the Alphabet" and "Read Textbook Stories".
- Pass criteria: Navigation occurs and Main Menu is displayed.

T0-03 — Main Menu buttons respond
- Priority: Medium
- Steps:
  1. On Main Menu, tap "Learn the Alphabet". Observe behavior (placeholder or TODO action is acceptable but must not crash).
  2. Back to Main Menu, tap "Read Textbook Stories". Observe behavior.
- Expected result:
  - Buttons are tappable and either navigate to a placeholder screen or show an informative message. No crashes.
- Pass criteria: No crashes; navigation or placeholder shown.

T0-04 — App lifecycle basic stability (cold start / background / resume)
- Priority: Medium
- Steps:
  1. From Main Menu, press Home to background the app.
  2. Re-open the app from recent apps.
  3. Force-close the app and relaunch.
- Expected result:
  - App should resume to a stable state. No crashes during resume or relaunch.
- Pass criteria: App remains stable and returns to main flow.

Phase 1 — Test cases (Asset pipeline & App-side access)

Note: Phase 1 has two aspects: host-side validation (validator CLI) and device-side access (app can read bundled JSON and either play audio or use TTS). The host-side validation should be executed before device tests.

T1-01 — Host: JSON validator run (precondition)
- Priority: High
- Steps:
  1. On the host machine, run:

```powershell
python tools\validate_assets\validate_assets.py --schemas docs\json_schemas --assets docs\sample_assets
```

- Expected result: "Validation completed successfully" and exit code 0.
- Pass criteria: CLI validator returns success. (Already completed in this workspace.)

T1-02 — Device: App can load bundled JSON (Asset existence)
- Priority: High
- Steps:
  1. Install debug APK on tablet.
  2. Open a file explorer on the device or use Android Studio Device File Explorer to view `assets/` in the installed app, or use an in-app debug screen if available.
  3. Verify `letter_h_01.json` and `story_std1_ch1_page1.json` are present in the APK assets.
- Expected result:
  - Both JSON files are present in the app's packaged assets.
- Pass criteria: Files found in assets folder.

T1-03 — Device: AssetLoader reads JSON content
- Priority: High
- Steps:
  1. Launch app on tablet and navigate to a debug/test screen that reads `letter_h_01.json` (if present). If no debug screen exists, run an instrumentation test that calls `AssetLoader.loadJsonFromAssets("letter_h_01.json")` and asserts the returned JSON contains `"char": "अ"`.
  2. Observe result.
- Expected result:
  - AssetLoader successfully loads and parses the JSON and returns the expected fields.
- Pass criteria: JSON parsed and `char` equals `अ`.

T1-04 — Device: Audio playback (bundled audio path) with TTS fallback
- Priority: High
- Steps:
  1. Ensure either: (A) placeholder MP3 audio files exist under `app/src/main/assets/audio/...` paths referenced in JSON, or (B) no audio files exist and system TTS fallback is expected.
  2. From the app (on a debug/test screen) trigger playback for the letter audio path from `letter_h_01.json`.
  3. Observe whether MediaPlayer plays bundled audio; if not, verify TTS speaks the letter glyph.
- Expected result:
  - If bundled MP3 exists, MediaPlayer plays it. If not, TTS speaks the letter text (e.g., "अ"). No crashes.
- Pass criteria: Audible output via speaker or successful TTS utterance; no crash.

T1-05 — Device: Story narration playback or TTS fallback
- Priority: Medium
- Steps:
  1. Trigger playback for the story narration referenced in `story_std1_ch1_page1.json`.
  2. If bundled narration exists, it should play. Otherwise, TTS should speak the story text.
- Expected result:
  - Narration plays or TTS reads the story page text aloud. No crashes.
- Pass criteria: Audible playback via speaker; no crash.

T1-06 — Device: Negative scenario — missing asset handling
- Priority: Medium
- Steps:
  1. Intentionally rename a sample JSON asset in the APK (or simulate missing asset) and perform AssetLoader load for that file.
  2. Observe app behavior.
- Expected result:
  - AssetLoader returns null or error; app displays a friendly visual cue or fallback behavior (TTS fallback or skip). App must not crash.
- Pass criteria: Graceful failure and visible user-friendly cue; no crash.

Acceptance criteria (Phase 0 + Phase 1)
- All Phase 0 test cases T0-01..T0-04 pass on the tablet.
- All Phase 1 test cases T1-01..T1-06 pass; specifically host validator success and device-side JSON load + audio or TTS fallback working.

Test data & file references
- Sample JSON files (packaged): `letter_h_01.json`, `story_std1_ch1_page1.json` (located in `app/src/main/assets/` after packaging).
- Validator: `tools/validate_assets/validate_assets.py` and `docs/json_schemas/*`.

Logging guidance
- Capture logcat during tests to collect errors and Audio/Media/TTS warnings:

```powershell
adb logcat -c
adb logcat > logcat_hima.txt
```

- Capture screenshots of failures using the tablet's screenshot facility or `adb exec-out screencap -p > fail.png`.

Pass/Fail signoff
- After running all test cases, prepare a short signoff report listing each test ID and Pass/Fail status and attach logs/screenshots for any failed tests.

Appendix: Quick smoke test checklist (one-liner)
- Install debug APK → Launch → Onboarding shown → Select language → Main menu shown → Verify `letter_h_01.json` exists in assets → Trigger letter playback (play or TTS) → No crash.

---

End of Phase 0 & Phase 1 Test Plan
