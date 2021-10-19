# NYCSchool
NYCSchool is a sample project to showcase different architectural approaches to developing Android apps in kotlin.

In this branch you'll find:

* MVVM architecture and data flow.
* Kotlin Coroutines for background operations.
* A single-activity architecture, using the Navigation component to manage fragment operations.
* A presentation layer that contains a fragment (View) and a ViewModel per screen (or feature).
* Reactive UIs using observables , Data Binding, cleared when the provided [Lifecycle]s [onDestroy] event is triggered..
* A data layer with a repository using retrofit2.
* Concept to sharedViewmodel, so that no unneccassary data is not shared with all the child views/fragments
* Unit test case for viewmodels.

# Concepts/Technology

* Kotlin
* MVVM
* Couroutine Dispatcher
* Navigation Component
* Retrofit(Basic) for network calls
* Base templete for the Application with MVVM, Lifecycle components and ioDispatchers
* ContrainstLayout for UI
* Junit for testing
* Works on both tablet and phone.

# Referred posts
* [Medium Post](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
* [CodePath for Glide](https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library)
* [Retrofit](https://howtodoandroid.com/retrofit-android-example-kotlin/)
