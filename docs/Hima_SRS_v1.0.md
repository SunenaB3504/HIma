# Software Requirements Specification (SRS)
for Hima (short for Hindi Marathi) (Native Android App)

Version: 1.0

Date: August 28, 2025

---

## 1. Introduction

### 1.1 Purpose

This document defines the functional and non-functional requirements for the "Hima" native Android application (Hima is short for Hindi Marathi). Its purpose is to provide a detailed description of the product's features, behavior, and constraints, serving as the foundational agreement between stakeholders for development.

### 1.2 Scope

The product will be a standalone, offline educational application for the Android platform, designed to teach Hindi and Marathi to children aged 5-10. The learning content will be derived from the CBSE syllabus, pre-processed, translated into Marathi where necessary, and bundled statically within the application. The core features include alphabet learning, interactive story reading from textbooks, and post-reading activities, all presented in a highly engaging, child-friendly user interface. The application will not require an internet connection or any real-time AI API calls to function; however, audio assets will be prepared during development using AI-assisted generation and bundled with the app.

### 1.3 Definitions, Acronyms, and Abbreviations

- SRS: Software Requirements Specification
- UI: User Interface
- UX: User Experience
- CBSE: Central Board of Secondary Education (India)
- APK: Android Package Kit, the file format used to distribute and install mobile apps on Android.
- Static Content: All data (text, images, audio) that is pre-built and included with the app at the time of installation.

## 2. Overall Description

### 2.1 Product Perspective

Hima will be a new, self-contained mobile application. It is designed from the ground up for the Android platform to ensure optimal performance, responsiveness, and offline capability. All learning content and features will be available immediately after installation.

### 2.2 Product Functions

The app will provide a gamified and structured learning path for young children. Key functions include:

- Language Selection: Allowing the user to choose between Hindi and Marathi.
- Grade-Based Learning: Structuring content based on CBSE grade levels (e.g., Standard 1, 2, 3).
- Alphabet Mastery: An interactive module for learning letters, sounds, and basic words.
- Interactive Storybooks: A digital reader for CBSE textbook chapters, enhanced with audio and vocabulary support.
- Practice and Reinforcement: A suite of activities to test comprehension and practice reading and writing skills.

### 2.3 User Characteristics

The primary users are children between the ages of 5 and 10. They have:

- Minimal to moderate reading skills in their native language.
- Basic familiarity with touch-based interfaces (tapping, swiping).
- A short attention span, requiring a highly stimulating and visually rewarding experience.

Secondary users are parents or guardians who will install the app and may occasionally assist the child.


### 2.4 General Constraints

- Platform: The application must run on the Android operating system, targeting Android 6.0 (Marshmallow, API 23) and above.
- Offline Operation: The application must be 100% functional without an internet connection.
- No Real-Time AI: The app must not make any external API calls for AI services at runtime. All content, including translations, examples, and audio, must be pre-generated during development and included as static assets in the build.
- No API Key Requirement: The application will not require the user to enter or manage any API keys.
- Content Bundling: All learning materials (JSON data files, emojis or simple artwork, audio files, videos, animations) must be bundled within the APK or an associated expansion file (OBB).
- On-device Language Packs: The app may use Android's on-device language packs for offline TTS and (optionally) on-device voice recognition; any runtime speech features must not rely on external network services.

### 2.5 Assumptions and Dependencies

- Content Provision: All necessary CBSE syllabus content will be provided in a usable format (e.g., PDF) prior to the content processing phase.
- Content Pre-Processing: A separate, one-time process will be established to extract, translate (Hindi to Marathi), and structure the provided content into JSON files before application development. This process is critical but external to the runtime of the final app.
- Asset Creation: All audio for pronunciations, narration, and sound effects will be generated with the assistance of AI tools during the development phase for both languages and stored as media files; these files will be bundled with the app. If desired, selected assets may be replaced with professional voice recordings prior to release. All audio must be available offline in the final build.

## 3. Specific Requirements

### 3.1 Functional Requirements

#### FR-1: Onboarding & Selection

1.1: Upon first launch, the app shall present a visually appealing screen to select the language of instruction: Hindi or Marathi.

1.2: After language selection, the main menu shall present two primary options: "Learn the Alphabet" and "Read Textbook Stories".

1.3: Selecting "Read Textbook Stories" shall lead to a screen where the user must select a grade and chapter (e.g., "Standard 1, Chapter 1", "Standard 1, Chapter 2", "Standard 2, Chapter 1").

#### FR-2: Alphabet Learning Module

2.1: This module shall display a grid of letters, separated by tabs for "Vowels" and "Consonants".

2.2: Tapping a letter shall navigate to a dedicated practice screen for that letter.

2.3 (Letter Practice Screen): This screen must feature:

- The selected letter displayed in a very large, clear font.
- A "Listen" button that plays a pre-generated pronunciation audio file for the letter's sound.
- An animated guide showing the correct stroke order for writing the letter (a tracing canvas).
- A section displaying 2-3 simple, pre-defined example words that start with the letter, each with an accompanying emoji illustration and a "Listen" button.

Additional expectations for example words and audio:

- Each letter's example-words section shall show at least three example words that begin with the selected letter where available.
- For every example word the UI shall display: the word (native script), a short English meaning, and a short example sentence in the chosen instruction language (Hindi or Marathi).
- Each example-word entry shall include a "Listen" control which plays either a bundled pre-generated audio clip for the word or, if such an asset is not available, shall use the device's offline TTS (Hindi/Marathi) to speak the word and its example sentence.
- The letter practice screen shall also provide a single "Listen Letter" control to speak the selected letter glyph itself (using bundled audio if present, otherwise TTS in the selected language).

These additions ensure that children see contextual vocabulary (word + meaning + sentence) and can hear natural pronunciation even when only TTS fallback is available.

#### FR-3: Story Reading Module

3.1 (Story Library): After selecting a grade and chapter, the app shall display a vibrant, grid-based library of all available chapters/stories for that grade and language. Each story will be represented by a "book cover" showing the title and an engaging emoji or simple artwork.

3.2 (Story Reader):

- The story reader shall display one page at a time.
- Each page will contain the story text in a large, legible font and a colorful illustration.
- Key vocabulary words within the text will be highlighted. Tapping a highlighted word shall open a pop-up displaying its English meaning, a representative emoji, and a "Listen" button for its pronunciation. Pronunciation audio files will be pre-generated during development and bundled in the app; the app may also use Android's on-device TTS language packs for fallback offline TTS.
- A master "Listen to Page" button will play a pre-recorded human narration of the entire page's text.
- Clear "Next" and "Previous" arrow buttons for page navigation.

#### FR-4: Post-Story Activities

4.1: After finishing the last page of a story, the user shall be guided to a dedicated activities section.

4.2 (Vocabulary Review): A list of all new vocabulary from the story will be displayed. Each entry will show the word, its meaning, and have a "Listen" button.

4.3 (Comprehension Q&A): The app will present pre-defined questions about the story. A "Show Answer" button will reveal the correct answer for self-evaluation.

4.4 (Reading Practice - Self-Evaluated): The app will display key sentences from the story. The child will be prompted by an audio cue to "Listen, then read this out loud!". The child can listen to the correct pronunciation as many times as they want. There will be no speech recognition.

4.5 (Writing Practice - Self-Evaluated): The app will prompt the child to write a specific word or sentence from the story in their notebook. After listening to the audio, the child can press a "Show Me" button, which will display the correct text on the screen for them to compare with their written work.

4.6 (Audio Identification Exercise): The app shall provide an optional listening-and-identify exercise where the app speaks a target vocabulary word (using bundled audio or offline TTS) and the child must select the correct word from a short list of candidate words displayed on screen. The exercise shall:

- Play the target word (and optionally a short example sentence) with a single tap to start the round.
- Present 3 or 4 candidate words (native script) with one correct option and others as distractors.
- Provide immediate feedback (correct / incorrect). If correct, award a star and play a short positive audio cue; if incorrect, allow at least one retry and show the correct answer after the attempt.
- Persist exercise scores or awards in the user's progress so that completed activities contribute to star totals.

This exercise is explicitly offline and does not require or rely on speech recognition; it tests listening comprehension and word recognition.

#### FR-5: Learning Combined Sounds (Consonant + Vowel)

5.1: The app shall include a module to teach combined sounds formed by consonant + vowel combinations specific to Devanagari and Marathi scripts (e.g., क + ा -> का).

5.2: For each consonant-vowel pair, the module shall display the combined glyph, play the pre-generated pronunciation audio, and show a simple emoji example word where applicable.

5.3: The module shall include a tracing canvas or animated stroke guide for combined glyphs when the combined form differs visually from the base consonant.

5.4: A practice drill shall let the child select a consonant and then cycle through vowels to hear and see the written combined forms.

5.5: All audio for combined sounds shall be pre-generated during development and bundled; Android on-device TTS packs may be used as a fallback only and must function offline.

### 3.2 Non-Functional Requirements

#### NFR-1: User Interface & Experience (UI/UX)

Design Language: The entire app must use a bright, cheerful, and cartoonish art style. Colors should be vibrant, and fonts must be large and rounded.

Interactivity: All buttons and interactive elements must be large and provide immediate visual and auditory feedback (e.g., a "pop" sound and slight animation on tap).

Gamification: Simple reward mechanisms like collecting stars for completing chapters or activities should be included to motivate the child.

Animated Mascot: A friendly animated animal or character shall act as a guide, providing instructions and encouragement through pre-recorded voice lines.

#### NFR-2: Performance

Responsiveness: The app must launch quickly. All screen transitions and animations must be smooth and fluid, with no noticeable lag.

Resource Management: The app should be optimized to run efficiently on low-to-mid-range Android devices, consuming minimal battery and memory.

#### NFR-3: Compatibility

The application must support a wide range of screen sizes and densities, from small smartphones to large tablets, ensuring a consistent and un-distorted layout.

#### NFR-4: Data Storage

All content (JSON data files, images, audio clips, etc.) will be stored locally within the application's assets. For larger datasets, an Android App Bundle with asset packs or an OBB expansion file will be used.

---

End of SRS
