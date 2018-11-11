# TrashBins App

App displays **Google map** with locations of all trash bins in Prague. User can **filter** and **display** locations by type of trash - glass, plastic, paper trash etc. After clicking on a marker **info window** will appear with detailed information about type of trash bin and percentage, how full the bin is. After clicking on the info window app will navigate you to **detail screen** with even more information on the location selected. In detail window, user can add selected location to the **watch list**.

**App Preview:**

![Alt text](readme/OdpadkyPreview.gif?raw=true "App Preview")

**Main features:**
- App uses **Retrofit library** - to pull and parse data about locations and parametres of all trash bins from **API**. App also pulls and shows demo data about how each trash bin is full.
- App uses **MVVM - ViewModel + LiveData** for storing data fetched from API.
- The list of watched locations is stored in local SQLite database using **Room library**. 
- App implements two **Google Play Services**: **Google Maps** - to display all locations via markers and clusters, and **Google Admob** - to test possible monetization of the app - banner ad is displayed in Detail Activity.
- App's **widget** navigates user quickly to the trash bins of selected type.
- App uses **Android Design Support library**  - to design UI according to Android Material Design.


(To be able to use app properly, insert Google maps API key in value property in **AndroidManifest.xml**)

![Alt text](readme/manifestGoogleMapsAPI.png?raw=true "AndroidManifest.xml")
