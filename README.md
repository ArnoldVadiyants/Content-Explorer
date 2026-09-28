# Content Explorer

A native Android application built as part of the Android Code Challenge.

The app fetches hierarchical content from the provided API, displays it in a structured way, supports image details and selectable choice questions, and keeps previously fetched content available offline.

## Demo

https://github.com/user-attachments/assets/fc2f4166-77d2-4c63-807c-f7cede893b1f


## Features

- Fetches hierarchical content from the provided API.
- Displays Pages, Sections, and Questions while preserving their hierarchy.
- Supports:
  - Text questions
  - Image questions
  - Single-selection choice questions
  - Multiple-selection choice questions
- Displays images in a reduced size inside the content.
- Opens images on a dedicated detail screen with the full-sized image and title.
- Persists fetched content locally using Room.
- Supports offline access to previously fetched content.
- Supports pull-to-refresh.
- Gracefully handles network failures while preserving cached content.
- Displays loading, empty, and error states.
- Includes unit, UI, and integration tests.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Coroutines & Flow
- Kotlinx Serialization
- Ktor Client
- OkHttp
- Room
- Koin
- Navigation 3
- Coil
- JUnit 5
- MockK
- Turbine

## Architecture

The project follows Clean Architecture with MVI and a multi-module structure.

### Modules

```text
:app

:core:common
:core:domain
:core:data
:core:network
:core:database
:core:designsystem
:core:navigation

:feature:home
:feature:image-detail
```

### High-level data flow

```text
Remote API
    │
    ▼
Network / DTOs
    │
    ▼
Repository
    │
    ▼
Room Database
    │
    ▼
Domain Models
    │
    ▼
ViewModel / MVI
    │
    ▼
Compose UI
```

The UI observes local database data through `Flow`. Network refreshes update the database, which then updates the UI.

## Offline Support

Room is used as the local source of truth for displayed content.

When a refresh succeeds, the fetched snapshot replaces the local database content transactionally. This prevents partially updated content from being exposed to the UI.

If the network request fails, previously cached content remains available and the UI displays an appropriate error state.

## Navigation

The application has two destinations:

- Home
- Image Detail

The Home screen displays the complete hierarchical content from the API.

When an image is clicked, the app navigates to the Image Detail screen. Only the image ID is passed through navigation; the destination loads the corresponding image data from the repository.

## Choice Questions

Choice questions support both selection modes provided by the API:

- Single selection — only one response can be selected.
- Multiple selection — multiple responses can be selected simultaneously.

Selection state is kept in the Home ViewModel and is not persisted because it represents temporary UI state rather than fetched content.

## API

The application uses the endpoint provided by the coding challenge:

```text
https://gist.githubusercontent.com/aruana-lumiform/383e1291df3e0cc1d49aeb14d45f5b94/raw/lumiform-android-test.json
```

The network response is mapped through separate layers:

```text
JSON
  ↓
DTOs
  ↓
Repository
  ↓
Room
  ↓
Domain models
  ↓
UI state
```

Network DTOs and database entities do not leak into the presentation layer.

## Testing

The project includes tests covering the main application behavior.

### Unit tests

Tests cover:

- Repository behavior
- Data mapping
- ViewModel state handling
- Choice selection logic
- Error handling

### UI tests

Compose UI tests cover:

- Loading state
- Empty state
- Error state
- Hierarchical content rendering
- Choice interactions
- Image interactions
- Image detail screen
- Retry actions

### Integration test

An application-level integration test verifies image navigation through the actual application flow.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ArnoldVadiyants/Content-Explorer.git
cd Content-Explorer
```

### 2. Open the project

Open the project in Android Studio and allow Gradle to synchronize.

### 3. Run the application

Select the `app` configuration and run it on an Android device or emulator.

The application will fetch the remote content and store it locally for subsequent offline access.

## Build

```bash
./gradlew build
```

## Unit Tests

```bash
./gradlew test
```

## Instrumentation & UI Tests

Run on a connected Android device or emulator:

```bash
./gradlew connectedAndroidTest
```

## Project Structure

```text
Content-Explorer/
│
├── app/
│
├── core/
│   ├── common/
│   ├── database/
│   ├── data/
│   ├── designsystem/
│   ├── domain/
│   ├── navigation/
│   └── network/
│
├── feature/
│   ├── home/
│   └── image-detail/
│
├── build-logic/
│   └── convention/
│
├── gradle/
│   └── libs.versions.toml
│
└── settings.gradle.kts
```

## Design Decisions

### Room as the source of truth

The UI observes Room instead of directly observing network responses. This provides a single source of truth and supports offline access naturally.

### Transactional refresh

Local content is replaced only after a successful network request. Database updates are performed transactionally to avoid exposing a partially updated hierarchy.

### ID-based ordering

The API IDs are used to preserve the order of the provided content. No additional position field is introduced.

### Navigation by ID

Only the image ID is passed through navigation. The destination retrieves the complete image model through the repository instead of passing the entire domain object.

### Selection state

Choice selections are maintained in the ViewModel because they represent temporary UI interaction and are not part of the content that needs to be persisted.

## Author

**Arnold Vadiiants**

Senior Android Engineer
