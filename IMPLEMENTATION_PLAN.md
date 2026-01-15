# Scenic (Android) Implementation Plan

This document outlines the plan to port the Vibes React web app to a native Android app named "Scenic" using Kotlin and Jetpack Compose.

## 1. Architecture Overview
We will use a modern Android architecture:
- **UI Layer**: Jetpack Compose (Declarative UI)
- **State Management**: standard `ViewModel` with `StateFlow`.
- **Data Layer**: Repository pattern.
- **Networking**: Retrofit for REST API calls (matching your Rails backend).
- **Local Storage**: DataStore for user preferences (volume, transition time).
- **Media**: ExoPlayer for audio, Coil for image loading.

## 2. dependencies
Already added to `build.gradle.kts`:
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.navigation:navigation-compose`
- `com.squareup.retrofit2:retrofit` & `converter-gson`
- `io.coil-kt:coil-compose` (Images)
- `com.google.android.exoplayer:exoplayer` (Audio)

## 3. Feature Mapping (React -> Kotlin)

| React Component | Android Component (Compose) | Implementation Details |
|-----------------|-----------------------------|------------------------|
| `App.tsx` (Routing) | `NavHost` | Setup routes: `Home`, `Login`, `Register`, `Settings` |
| `ImageRingBook` | `ImageRingBook` | Use `Box` with `AsyncImage` (Coil). Custom animation for "page flip" or simple crossfade initially. |
| `AudioPlayer` | `AudioPlayer` | `ExoPlayer` instance managed in `MainViewModel` or a `MediaService`. UI for Play/Pause/Volume. |
| `SettingsModal` | `SettingsDialog` | `AlertDialog` or `ModalBottomSheet` for adjusting timer/volume. |
| `api.ts` | `VibesApiService` | Retrofit interface defining endpoints (`/api/images`, `/users/tokens/*`). |
| `AuthContext` | `UserSessionManager` | Singleton/Hilt module to hold JWT token (encrypted). |

## 4. Phase 1: Core Foundation (Done)
1.  **Network Layer**: Setup `Retrofit` client and data models (`Image`, `Audio`, `User`). (Done)
2.  **Repository**: Create `VibesRepository` to handle API calls. (Done)
3.  **ViewModel**: Create `MainViewModel` to manage state (current image, audio playing, loading state). (Done)

## 5. Phase 2: UI Implementation (Done)
1.  **Home Screen**: Build the main meditation view with the ring book and player controls. (Done)
2.  **Settings**: Implement the dialog for transition interval and volume. (Partial - using Player controls on screen)
3.  **Authentication**: Login/Register screens. (Pending)

## 6. Phase 3: Polish & Offline (Done)
1.  **Offline Mode**: Implement caching (Room database) for images/audio. (Done)
2.  **Animations**: Refine the page tiling/flipping animation. (Done - Crossfade)
