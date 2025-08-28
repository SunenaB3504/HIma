# Hima — Phase 2 UAT (User Acceptance Test)

Date: 2025-08-28

Purpose
-------
This document defines the User Acceptance Test cases and pass criteria for Phase 2 of the Hima app (Alphabet learning, Letter practice, Combined sounds, Progress persistence, and Settings/TTS behavior). Use this to test the app in a test tab and record results.

Scope
-----
---
# Hima — Phase 2 UAT (User Acceptance Test)

Date: 2025-08-28

Purpose
-------
This document defines the User Acceptance Test cases and pass criteria for Phase 2 of the Hima app. It is intended for manual testers and QA to validate:
- Alphabet learning and navigation
- Letter practice flows (Listen, Trace placeholder, star awarding)
- Combined sounds playback and cycling
- Settings/TTS behavior and persistence
- Audio fallback logic (asset-first, TTS fallback)

Quick overview
--------------
- Deliverables in this doc: smoke checklist, detailed test cases (TC-01..TC-10), test templates, logging/troubleshooting tips, acceptance criteria, and post-UAT follow-ups.

Scope
-----
In scope:
- Alphabet browsing and letter selection
- Letter practice screen (Listen, Trace placeholder, award star, persistence)
- Combined sounds screen (cycle vowels, play phoneme)
- Settings: toggle device TTS preference
- Progress persistence via `ProgressStore`

Out of scope:
- Story/reader flows
- Full interactive tracing analytics or automated tracing scoring

Test environment
----------------
- Host: Windows 10/11 (PowerShell) for build/install
- Devices/emulators (recommended): Android 13 (API 33) physical device; Android 11 (API 30) emulator
- Build type: Debug (assembleDebug / installDebug)

Build & install (PowerShell)
----------------------------
From the project root:

```powershell
# build
.\gradlew assembleDebug
# install on a connected device/emulator
.\gradlew installDebug
```

Files & pointers for testers
---------------------------
- UI: `app/src/main/java/com/example/hima/ui/`
- Nav: `app/src/main/java/com/example/hima/ui/NavGraph.kt`
- Audio: `app/src/main/java/com/example/hima/media/AudioPlayer.kt`, `TTSManager.kt`
- Storage: `app/src/main/java/com/example/hima/data/ProgressStore.kt`, `SettingsStore.kt`
- Assets: `app/src/main/assets/audio/letters/`, `app/src/main/assets/audio/combined/`

Preparatory steps
-----------------
- Ensure device volume is up and not muted.
- To test asset playback, add sample MP3s under the `assets/audio/` paths and rebuild.
- For clean persistence tests, clear app data between runs through device Settings.

Smoke test (quick checks)
------------------------
Run these before full UAT. If a smoke test fails, stop and file a blocker.
1. App launches and main menu displays.
2. Alphabet opens and a letter navigates to Letter Practice.
3. With "Use device TTS" = ON, Listen produces audible speech.
4. Trace -> Mark Complete increments the star count.
5. Settings toggle persists after app restart.

Detailed test cases
-------------------
TC-01 — Alphabet navigation
- Preconditions: App installed.
- Steps: Open Main -> Learn the Alphabet -> Tap letter 'अ'.
- Expected: Letter Practice opens and displays 'अ'.

TC-02 — Letter Practice: Listen (TTS enabled)
- Preconditions: Settings -> Use device TTS = ON.
- Steps: On Letter Practice for 'अ', tap Listen.
- Expected: Device TTS speaks the letter; Logcat shows TTS activity.

TC-03 — Letter Practice: Listen (TTS disabled, asset present)
- Preconditions: Settings -> Use device TTS = OFF; `assets/audio/letters/अ.mp3` present.
- Steps: Tap Listen.
- Expected: App plays the packaged MP3 via `AudioPlayer` (no TTS).

TC-04 — Letter Practice: Trace placeholder & award star
- Steps: Tap Trace → Mark Complete.
- Expected: Stars increment and UI returns to non-tracing state.

TC-05 — Persistence across restart
- Steps: Award a star for 'अ', close the app, re-open, verify stars.
- Expected: Star count persists.

TC-06 — Combined sounds: cycle & play
- Steps: Open Combined Sounds → use Prev/Next → Play for multiple indices.
- Expected: Display updates and audio plays (asset or TTS per Settings).

TC-07 — Settings persistence & behavior
- Steps: Toggle "Use device TTS", restart app, verify persisted state and playback behavior.

TC-08 — Audio fallback
- Preconditions: Use device TTS = OFF (or choose letter without asset).
- Steps: Tap Listen for a phoneme without an asset.
- Expected: App attempts asset playback; upon failure, if TTS is allowed it falls back to TTS; no crash.

TC-09 — Navigation/back-stack
- Steps: Navigate Main → Alphabet → Letter → Back → Main → Combined → Back.
- Expected: Back returns to previous logical screens reliably.

TC-10 — Edge/stress cases
- Cases: rapid Mark Complete presses, invalid `letter/{letter}` params, toggling TTS while audio plays.
- Expected: No crashes; logged errors if failures occur.

Traceability matrix
-------------------
- Alphabet UI & navigation → TC-01
- Letter practice flows → TC-02, TC-03, TC-04, TC-05
- Combined sounds → TC-06
- Settings/TTS → TC-02, TC-03, TC-07
- Audio fallback → TC-03, TC-06, TC-08

Defect severity & triage
-----------------------
- Blocker: app crashes, data loss, navigation dead-ends.
- Major: core flows unusable (Listen/Play, Trace-mark, persistence failures).
- Minor: visual/wording issues, non-critical logs.

Logging & troubleshooting
-------------------------
- Filter Logcat by tags: `TTS`, `AudioPlayer`, `ProgressStore`.
- Verify assets in source `app/src/main/assets/` before building; inspect APK if needed.
- Capture full Logcat + screenshot on failures.

Automated checks (recommended)
-----------------------------
- Instrumentation tests for navigation and mocked audio/TTS.
- Unit tests for `ProgressStore` read/write behavior.

UAT result template (copy per case)
----------------------------------
- Tester:
- Date/time:
- Device (model / API level):
- App build (APK):
- Test case ID:
- Preconditions:
- Steps executed:
- Expected result:
- Actual result:
- Pass / Fail:
- Severity:
- Logs/screenshots attached: (Y/N)
- Notes:

Smoke run checklist
-------------------
- [ ] Build succeeded and app installed
- [ ] App launches
- [ ] Main menu visible
- [ ] Alphabet opens and letter navigates
- [ ] Listen (TTS on) audible
- [ ] Trace -> Mark Complete increments star
- [ ] Settings toggle persists after restart

Acceptance criteria
-------------------
Phase 2 accepted when:
- No Blocker defects remain open.
- All Major test cases pass or have documented mitigations.
- Remaining Minor defects have owners and plans.

Sign-off
--------
Tester: ______________________
Date: ______________________
QA Lead: ______________________
Product Owner: ______________________
Phase 2 Acceptance: (circle) ACCEPT / REJECT

Post-UAT follow-ups
-------------------
- Implement interactive tracing (Canvas + stroke capture) in a feature branch and add UAT cases.
- Normalize and expand offline audio assets; consider an `assets/manifest.json` for coverage.
- Add instrumentation tests for navigation and persistence.

Contacts
--------
- Dev lead: (add developer contact)
- QA lead: (add QA contact)

End of document

---
  - Device (model / API level):
