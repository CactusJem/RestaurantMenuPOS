```md
 RestaurantMenuPOS — Hybrid Android WebView (Java) + Firebase Hosting + Firestore

RestaurantMenuPOS is a hybrid mobile application where the restaurant ordering UI is delivered as a Firebase-hosted web app and rendered inside an Android WebView. The Android side (Java) provides mobile-specific responsibilities such as Activity lifecycle, WebView configuration, native back navigation, error handling, JavaScript-to-Java bridge, and OOP domain modeling (MenuItem, OrderItem, Order, OrderRepository).

---

 Key Features

 Web App (Firebase Hosting)
- Menu browsing by category (web UI)
- Cart management (add/remove items, quantity)
- Notes per item
- Order-level discount (fixed/percentage)
- Confirm Order flow
- Save order data to Cloud Firestore:
  - `orders/{orderId}`
  - `staff/{staffName}/orders/{orderId}`

 Android App (Java)
- WebView container to run the web app inside an APK
- Mobile lifecycle handling: `onCreate`, `onPause`, `onResume`, `onDestroy`
- Native back behavior (WebView history back before exiting app)
- Debugging support: JavaScript console output forwarded to Logcat
- WebView error handling with toast notification for main-frame failures
- JS ↔ Java bridge using `addJavascriptInterface`:
  - `AndroidApp.showToast(message)`
  - `AndroidApp.addOrder(orderId, tableNumber, itemName, price, quantity)`
  - `AndroidApp.getTotalOrders()`
- OOP domain layer (Java):
  - `MenuItem` → represents a menu item
  - `OrderItem` → represents a menu item + quantity
  - `Order` → represents an order containing multiple items
  - `OrderRepository` → Singleton repository pattern storing orders in-memory

> **Note:** The primary business UI runs in the web layer; the Android layer demonstrates Mobile Programming concepts (lifecycle, WebView, bridge, OOP, repository pattern).

---

 Architecture Overview

 Android (Java)
- `MainActivity` hosts a WebView, configures settings, injects JS bridge, handles back navigation and lifecycle.
- `WebAppInterface` exposes native Java methods to JavaScript (`AndroidApp.*`).
- OOP models (`MenuItem`, `OrderItem`, `Order`) and `OrderRepository` store and process order data on the Android side.

 Web (Firebase Hosting)
- Static web app (HTML/CSS/JS) served from Firebase Hosting.
- Handles cart UI, payment logic, and Firestore writes via Firebase JS SDK.

 Firebase (Cloud Firestore)
- Stores orders and staff-linked order history.

---

 ## Project Structure (Android)


app/
└── src/
    └── main/
        ├── java/
        │   └── com/example/restaurantmenupos/
        │       ├── MainActivity.java
        │       ├── WebAppInterface.java
        │       └── model/
        │           ├── MenuItem.java
        │           ├── OrderItem.java
        │           ├── Order.java
        │           └── OrderRepository.java
        ├── res/
        │   └── layout/
        │       └── activity_main.xml
        └── AndroidManifest.xml



---

 How It Works (End-to-End Flow)

1. Android app launches → `MainActivity` loads Firebase Hosting URL in WebView.
2. User interacts with the web menu/cart/payment UI.
3. On Confirm Order, the web app writes to Firestore:
   - `orders/{orderId}`
   - `staff/{staffName}/orders/{orderId}`
4. After successful Firestore writes, the web app calls native methods through the injected bridge:
   - `AndroidApp.showToast("Order submitted...")`
   - `AndroidApp.addOrder(orderId, 0, customerName, total, 1)`
5. Android receives the call in `WebAppInterface.addOrder(...)`, constructs Java OOP objects (`MenuItem → OrderItem → Order`), saves them in `OrderRepository`, and shows a native toast/log output.

---

 JavaScript ↔ Java Bridge

Android injects a Java object into WebView:

```java
webView.addJavascriptInterface(new WebAppInterface(this), "AndroidApp");
````

From JavaScript, the bridge can be used like:

```js
if (window.AndroidApp && AndroidApp.showToast) {
  AndroidApp.showToast("Hello from Web!");
}
```

### Exposed Methods

* `AndroidApp.showToast(message)`

  * Displays a native Android toast

* `AndroidApp.addOrder(orderId, tableNumber, itemName, price, quantity)`

  * Sends an order summary to Android native side for OOP processing & repository storage

* `AndroidApp.getTotalOrders()`

  * Returns repository count from the Android side (optional verification)

---

 Mobile Programming Concepts Demonstrated

This project intentionally includes native Android fundamentals beyond simply loading a website:

* Activity lifecycle management (`onCreate`, `onPause`, `onResume`, `onDestroy`)
* WebView configuration

  * JavaScript enabled for modern web app features
  * DOM storage enabled for cart persistence
* Back navigation integration

  * WebView history back before app exit
* Debugging

  * JavaScript console messages forwarded to Logcat (`WebChromeClient`)
* Error handling

  * Toast + logging when main-frame page loading fails
* OOP domain model & design patterns

  * `MenuItem`, `OrderItem`, `Order`
  * `OrderRepository` Singleton + Repository Pattern

---

 Requirements

* Android Studio (recommended latest stable)
* Minimum Android API depends on your project configuration
* Internet connection (loads Firebase Hosting URL and uses Firestore)
* Firebase project configured for Hosting + Firestore (web layer)

---

## Run (Android)

1. Open the project in Android Studio
2. Ensure `INTERNET` permission exists in `AndroidManifest.xml`
3. Update `FIREBASE_URL` in `MainActivity.java` if needed:

   ```java
   private static final String FIREBASE_URL = "https://<your-project>.web.app";
   ```
4. Run on emulator or physical device

---

 Notes / Known Limitations

* `OrderRepository` is in-memory only (data resets when app process is killed).
* The primary UI and database writes currently occur in the web app layer (Firebase JS SDK).
* `tableNumber` can be treated as optional/placeholder (e.g., `0`) if not used by the web UI.

---

 Future Improvements

* Add a native Android screen (e.g., Order History Activity) to display orders from `OrderRepository`
* Persist orders locally using Room/SQLite for offline support
* Restrict WebView navigation to trusted domains (additional security hardening)
* Implement native Firebase Firestore SDK usage on Android if migrating business logic to native

```
```
