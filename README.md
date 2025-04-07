# 📺 ShopCamera TV

**ShopCamera TV** is an Android TV application that allows users to view and interact with their [EzyKam](https://web.ezykam.com/playback) surveillance cameras using a WebView and remote control navigation. The app features a custom on-screen cursor, remote-friendly buttons, and JavaScript automation to simulate user interactions like hover and click.

![banner](./res/drawable/banner.png)

---

## 🚀 Features

- 🔐 Secure in-app WebView for EzyKam camera feed playback
- 🖱️ Custom DPAD-controlled cursor overlay for seamless TV navigation
- 🧠 JavaScript automation to click and hover on specific elements
- 🖥️ Single camera fullscreen viewing with one button
- 📱 Simple remote control interface with support for `DPAD_CENTER` actions
- ⚡ Optimized for Android TV leanback UI

---

## 📂 Project Structure

app/ ├── src/ │ └── main/ │ ├── java/com/theriyazo/shopcamera/ │ │ ├── MainActivity.kt │ │ └── MainFragment.kt │ ├── res/ │ │ ├── layout/fragment_webview.xml │ │ ├── drawable/banner.png │ │ └── mipmap/ic_launcher.png │ └── AndroidManifest.xml

yaml
Copy
Edit

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: AndroidX + WebView + FrameLayout overlay
- **Target**: Android TV with Leanback Launcher
- **Extras**: JavaScript injection to interact with DOM

---

## 🧑‍💻 Use Case

This app is perfect for:

- 📦 Shop owners using EzyKam IP cameras
- 👪 Home users monitoring surveillance feeds
- 🛋️ TV setups where mobile devices aren’t practical

---

## 🎮 Remote Control Navigation

| Button                  | Action                       |
| ----------------------- | ---------------------------- |
| DPAD Up/Down/Left/Right | Move on-screen cursor        |
| DPAD Center / Enter     | Click at cursor position     |
| Button 1                | Reload scripts & automation  |
| Button 2                | Enter fullscreen camera view |

---

## 🖼 TV Launcher Banner

Ensure this image exists:

```plaintext
res/drawable/banner.png
Size: 320x180 px (required for Android TV launcher display)

📦 Installation
Clone the repo:

bash
Copy
Edit
git clone https://github.com/yourusername/shopcamera-tv.git
Open in Android Studio

Connect an Android TV or emulator

Build and install:

bash
Copy
Edit
./gradlew installDebug
📋 License
This project is open-source and available under the MIT License.

🙏 Acknowledgements
EzyKam for the camera platform

Android TV developer docs

Kotlin + AndroidX libraries

yaml
Copy
Edit

---

Let me know if you want me to generate a matching `LICENSE`, or create badges and a GitHub Actions CI workflow for building this project.
```
