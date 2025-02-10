# DawnHealthTestApp

DawnHealthTestApp is an Android application that allows users to search for GitHub repositories and view the latest release versions of selected repositories. The app utilizes the GitHub API to fetch repository data and provides a seamless user experience with a clean UI and smooth interactions.

## Features

- Search for GitHub repositories using keywords.
- Display a list of repositories matching the search query.
- View details of a selected repository, including its latest release version.
- Handle errors and provide appropriate user feedback.

## Technologies Used

- **Kotlin** - Primary programming language.
- **MVVM Architecture** - Used for a clean separation of concerns.
- **Kotlin Coroutines & Flow** - For asynchronous data handling.
- **Retrofit** - For making network requests to the GitHub API.
- **Hilt** - For dependency injection.
- **Timber** - For logging.
- **Gson** - For JSON serialization and deserialization.

## Separation of Logic

The application follows a clean architecture approach to maintain separation of concerns:

- **Presentation Layer**: Contains the ViewModel and UI-related logic.
- **Domain Layer**: Defines repository interfaces.
- **Data Layer**: Handles API calls, data parsing, and repository implementations.


## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/DawnHealthTestApp.git
   ```
2. Open the project in Android Studio.
3. Sync the Gradle dependencies.
4. Run the application on an emulator or physical device.

## API Configuration

This app interacts with the GitHub API. 

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to contribute, report issues, or suggest improvements!

