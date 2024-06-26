# Weigh bridge truck record

## Preview (please click the image)
[![weigh bridge](https://img.youtube.com/vi/6yDpRuYxHeE/0.jpg)](https://www.youtube.com/watch?v=6yDpRuYxHeE "Weigh bridge apps")

### Architecture & Dependency
- Compose for construct UI
- MVVM for separate UI and business logic
- Clean architecture (usecase, repository, and model)
- Hilt for dependency injection
- Room for handling local db and sync with Firebase Realtime Storage
- Unit testing for ViewModel & Repository
  
### Run the application

1) Clone the repository
2) Open the project with Android Studio first before run `/.gradlew build`.
3) If you run `/.gradlew build` directly from the terminal without open Android Studio, you will be encountered this [issue](https://stackoverflow.com/questions/27620262/sdk-location-not-found-define-location-with-sdk-dir-in-the-local-properties-fil). `local.properties` will be automatically created by Android Studio.
4) Run `/.gradlew test` to run unit test. (Recommend using Java 17. Please don't use Java 21, an error will occur)

## Contact me
* **Iqbal Ansyori** - [ansyori.iqbal@gmail.com](mailto:ansyori.iqbal@gmail.com)
