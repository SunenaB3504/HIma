# Hima — Development Approach & Progress Tracker

Version: 1.0

Date: August 28, 2025

## Purpose

This document defines the recommended development approach for Hima, broken into phases with a clear MVP for each phase, acceptance criteria, estimated durations, and a progress tracker to mark status as work proceeds.

Use this file as the single source of truth for planning and short-term tracking before work begins in the repository issue tracker or project board.

## High-level approach

- Platform: Native Android (Kotlin + Jetpack Compose).
- Architecture: MVVM + Clean Architecture (UI / Domain / Data layers).
- Iteration style: Short iterations (1–2 week sprints) delivering an MVP per sprint where practical.
- Asset pipeline: Preprocess content (JSON) and audio during development; validate with JSON Schema and include assets in app bundle or Play Asset Delivery packs.

## Checklist (requirements coverage)

- FR-1: Onboarding & Selection — language, main menu, grade+chapter selector.
- FR-2: Alphabet Module — vowels/consonants, letter practice, audio, tracing, emoji examples.
- FR-3: Story Reader — library, page reader, vocab pop-ups, narration, navigation.
- FR-4: Post-Story Activities — vocab review, Q&A, reading & writing self-eval activities.
- FR-5: Combined Sounds — consonant+vowel module and practice drills.
- NFRs: Offline operation, Android 6.0+ compatibility, smooth performance, cheerful UI, gamification, bundled assets.

## Phases, MVPs and Acceptance Criteria

Phase 0 — Project Setup & Prototype (1 week)
- MVP: App skeleton with onboarding, language selection, and a mock main menu.
- Tasks:
  - Create Kotlin project skeleton, DI (Hilt), basic navigation.
  - Add placeholder UI for onboarding and main menu.
  - Create basic AssetLoader interface (mock returning sample JSON).
- Acceptance criteria:
  - App launches and shows onboarding → selecting language navigates to main menu.
  - Mock assets load without errors.

Phase 1 — Asset Pipeline & Minimal Content (2 weeks)
- MVP: Working AssetLoader, JSON Schema + validator, sample assets (1 letter, 1 combined sound, 1 story page), audio playback.
- Tasks:
  - Implement preprocessing tool (Python/Node) to produce assets.
  - Create JSON Schema for letters, combined sounds, story pages.
  - Add validator CLI and CI job to run schema validation.
  - Integrate audio playback (ExoPlayer/MediaPlayer) and play bundled sample audio.
- Acceptance criteria:
  - Validator passes sample assets.
  - App loads sample assets and plays audio offline.

Phase 2 — Alphabet & Combined Sounds (3 weeks)
- MVP: Alphabet grid (vowels/consonants), letter practice screen (large glyph, Listen, tracing with Lottie/sprite, emoji examples), combined-sounds practice drill.
- Tasks:
  - Implement Alphabet UI with tabs.
  - Implement Letter Practice screen with tracing canvas / Lottie integration.
  - Implement Combined Sound module (select consonant → cycle vowels).
  - Persist simple progress (stars) in Room/SharedPreferences.
   - Extend letter and vocab JSON schema to include, per example-word: `word` (native), `meaning` (English), `sentence` (Hindi/Marathi), and an optional `audio` path. Update the asset validator and sample assets accordingly.
   - Implement UI for Letter Detail to display at least three example words per letter (where available), each showing the English meaning and a short example sentence, plus a per-entry "Listen" button that uses bundled audio or offline TTS.
   - Implement an "Identify Word (listening)" exercise accessible from letter practice and post-story activities. The exercise shall play a target word (audio/TTS) and show 3–4 candidates; provide immediate feedback and persist stars on success.
- Acceptance criteria:
  - Each letter practice screen plays correct audio, shows tracing, and lists emoji examples.
  - Combined sounds display combined glyphs and play audio.
 - The letter data schema includes `meaning` and `sentence` fields; sample assets contain at least three words per letter demonstrating the fields.
 - Letter Detail UI displays ≥3 words with English meaning and example sentence, and per-word Listen button plays correct audio (or TTS fallback).
 - Identify Word exercise functions: app speaks a target, user selects from candidates, correct selection awards a star and persists progress; exercise works offline.

Phase 3 — Story Library & Reader (4 weeks)
- MVP: Grade+Chapter selector, story library, story reader (page-by-page), vocab pop-ups, master narration.
- Tasks:
  - Implement Grade + Chapter UI (lazy load assets per grade).
  - Implement Story Library grid with emoji covers.
  - Implement Reader with page navigation, vocab highlighting & pop-ups, narration playback.
  - Implement post-story redirect to activities screen.
- Acceptance criteria:
  - User can select grade+chapter → open story → navigate pages and play narration.
  - Tapping highlighted vocab shows meaning, emoji, and plays pronunciation.

Phase 4 — Post-Story Activities & Gamification (2 weeks)
- MVP: Vocabulary review, Q&A with Show Answer, reading & writing self-eval flows, star rewards and mascot prompts.
- Tasks:
  - Implement activities screen with listed vocabulary and Listen buttons.
  - Add comprehension Q&A with Show Answer toggle.
  - Add reading practice audio prompts and Show Me for writing practice.
  - Implement star awarding and simple mascot audio cues.
- Acceptance criteria:
  - Completing a story awards stars and updates progress.
  - Activities function offline and follow SRS flows.

Phase 5 — Polish, Performance & Release Prep (2–3 weeks)
- MVP: Accessibility, performance optimizations, CI, packaging with asset packs or OBB, final QA checklist.
- Tasks:
  - Optimize images/audio sizes; add lazy-loading.
  - Tune animations and frame rates for low-end devices.
  - Add instrumentation tests and run device smoke tests.
  - Prepare Play Asset Delivery config or OBB packaging.
- Acceptance criteria:
  - App passes smoke tests on API 23 device and on a tablet.
  - APK/Bundle size meets target (or assets split via asset packs).

## Progress Tracker (template)

Use this table to update status. Update `Status` as Not started / In progress / Blocked / Done and add short notes.

| Phase | Key deliverable (MVP) | Start | End | Status | Notes |
|---|---:|---:|---:|---:|---|
| Phase 0 | Project skeleton, onboarding, DI, navigation | 2025-08-28 | 2025-09-03 | Done | Project skeleton existed; added Hilt DI, Nav Compose, Onboarding & Main Menu screens. |
| Phase 1 | Asset pipeline + sample assets + app-side demo | 2025-09-04 | 2025-09-18 | Done | JSON schemas + validator + sample assets created and validated; AssetLoader and audio/TTS demo wired into app. |
| Phase 2 | Alphabet & Combined Sounds | 2025-09-19 | 2025-10-09 | Not started |  |
| Phase 3 | Story library & reader | 2025-10-10 | 2025-11-07 | Not started |  |
| Phase 4 | Post-story activities & gamification | 2025-11-08 | 2025-11-22 | Not started |  |
| Phase 5 | Polish & release prep | 2025-11-23 | 2025-12-07 | Not started |  |

### Sprint task checklist example (copy per sprint)

- [ ] Create/assign tickets for sprint
- [ ] Implement feature(s) for sprint MVP
- [ ] Unit & integration tests for changed modules
- [ ] Run schema validator on assets
- [ ] Produce demo APK and run smoke tests on device

## Risk register (short)

- Large APK size → Mitigation: split assets by grade using Play Asset Delivery / OBB and lazy-load.
- Missing/incorrect preprocessed content → Mitigation: strict JSON Schema + validator in CI; content sign-off step.
- Low-end device performance → Mitigation: Lottie/sprite optimizations, downscale assets, limit preloads.

## QA & CI recommendations

- CI steps for each PR: Gradle build, unit tests, run JSON schema validator, run linter (ktlint) and static analysis.
- Nightly or release pipeline: build app bundle, run instrumentation smoke tests on a small device matrix (emulator or farm), package assets.

## Deliverables for each phase

- Code: feature branches merged into main via PRs.
- Assets: validated JSON + audio files packaged per grade.
- Tests: unit tests and at least one instrumentation smoke test per major flow.
- Docs: update `docs/` with sample assets, schema, and changelog.

## How to use this tracker

- Edit the table above to reflect actual start/end dates and status.
- Add sprint-level task lists under `Sprint task checklist example` for ongoing sprints.
- Link PRs and build artifacts in the Notes column for traceability.

---

End of Development Plan & Tracker
