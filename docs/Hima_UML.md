# Hima — UML Diagrams

Version: 1.0

Date: August 28, 2025

## Purpose

This document provides high-level UML diagrams for Hima (short for Hindi Marathi). It captures the main classes, components, and a key sequence flow to guide implementation and ensure alignment with the SRS.

All diagrams are illustrative — refine them during design/implementation.

## Class Diagram (PlantUML)

Below is a PlantUML class diagram sketch describing core domain classes and relationships.

```plantuml
@startuml
skinparam classAttributeIconSize 0
class AppController {
  - currentUser: UserProfile
  - assetManager: AssetLoader
  + start(): void
}

class UserProfile {
  - id: String
  - language: String
  - stars: Map<String,int>
  + save(): void
}

class AssetLoader {
  - assetsPath: String
  + loadLetter(id): Letter
  + loadStory(storyId): Story
  + loadAudio(path): Audio
}

class Letter {
  - id: String
  - char: String
  - audioPath: String
  - strokeAnim: String
  - examples: List<WordExample>
}

class CombinedSound {
  - id: String
  - combinedGlyph: String
  - audioPath: String
}

class Story {
  - storyId: String
  - title: String
  - pages: List<Page>
}

class Page {
  - pageNo: int
  - text: String
  - narrationPath: String
  - vocab: List<VocabEntry>
}

class VocabEntry {
  - word: String
  - meaning: String
  - emoji: String
  - audioPath: String
}

class AudioPlayer {
  + play(path): void
  + stop(): void
}

class ProgressStore {
  - db: local
  + saveProgress(userId, data): void
  + loadProgress(userId): Map
}

AppController --> UserProfile
AppController --> AssetLoader
AppController --> AudioPlayer
AppController --> ProgressStore
AssetLoader --> Letter
AssetLoader --> Story
Story "1" o-- "*" Page
Page "1" o-- "*" VocabEntry
Letter "1" o-- "*" WordExample
CombinedSound <-- Letter
@enduml
```

Notes:
- `AssetLoader` reads the static JSON/audio assets (D1 in DFD).
- `ProgressStore` corresponds to D2 in the DFD (Room or SharedPreferences).

## Sequence Diagram — Reading a Story Page (PlantUML)

```plantuml
@startuml
actor Child
participant AppController
participant AssetLoader
participant AudioPlayer
participant ProgressStore

Child -> AppController: selectGradeChapter(grade, chapter)
AppController -> AssetLoader: loadStory(storyId)
AssetLoader --> AppController: Story
AppController -> AppController: renderPage(pageNo=1)
Child -> AppController: tapListenPage()
AppController -> AudioPlayer: play(page.narrationPath)
AudioPlayer --> Child: audioStream
Child -> AppController: tapVocab(word)
AppController -> AssetLoader: loadVocabAudio(word)
AssetLoader --> AppController: audioPath
AppController -> AudioPlayer: play(audioPath)
AppController -> ProgressStore: saveLastPage(userId, storyId, pageNo)
ProgressStore --> AppController: OK
@enduml
```

## Component Diagram (Mermaid)

```mermaid
graph LR
  UI[UI Layer (Activities/Fragments)] --> Controller[AppController]
  Controller --> Asset[Asset Loader]
  Controller --> Audio[Audio Player]
  Controller --> Store[Progress Store]
  Asset --> AssetsFolder[(Assets: JSON, audio, emoji)]
  Audio --> AndroidMedia[(Android Media APIs)]
  Audio --> AndroidTTS[(Android on-device TTS - fallback)]
```

## Notes on mapping to SRS

- Onboarding & Selection (FR-1) is handled by `AppController` + `UserProfile`.
- Alphabet & Combined Sounds (FR-2, FR-5) map to `Letter` and `CombinedSound` classes and flows through `AssetLoader` and `AudioPlayer`.
- Story Reader and Post-Story Activities (FR-3, FR-4) map to `Story`, `Page`, `VocabEntry`, and `ProgressStore`.
- Offline audio and fallback rules are handled by `AssetLoader` and `AudioPlayer` (use bundled audio; `AndroidTTS` as fallback).

## Next steps

1. Review diagrams and propose changes to class responsibilities (lightweight iteration).
2. Generate initial TypeScript/Kotlin data classes from the data contracts; create JSON Schema and validator (recommended next task).

End of UML
