# zcash-android-wallet
A sample Android wallet using the [Zcash Android SDK](https://github.com/zcash/zcash-android-wallet-sdk).

### Motivation
[Dogfooding](https://en.wikipedia.org/wiki/Eating_your_own_dog_food) - _transitive verb_ -  is the practice of an organization using its own product. This app was created to help us learn. 

Please take note: the wallet is not an official product by ECC, but rather a tool for learning about our libraries that it is built on. This means that we do not have robust infrasturcture or user support for this application. We open sourced it as a resource to make wallet development easier for the Zcash ecosystem. 

# Disclaimers
There are some known areas for improvement:

- This app is mainly intended for learning and improving the related libraries that it uses. There may be bugs.
- Traffic analysis, like in other cryptocurrency wallets, can leak some privacy of the user.
- The wallet requires a trust in the server to display accurate transaction information. 

See the [Wallet App Threat Model](https://zcash.readthedocs.io/en/latest/rtd_pages/wallet_threat_model.html)
for more information about the security and privacy limitations of the wallet.

If you'd like to sign up to help us test, reach out on discord and let us know! We're always happy to get feedback!

# Description
This a sample wallet for the following set of features:
- z2z transactions w/ encrypted memos
- reply-to formatted memos
- z2t transactions
- transparent receive-only
- autoshielding on threshold from receive only t-address

note: z means sapling shielded addresses.

# Prerequisites
- [The code](https://github.com/zcash/zcash-android-wallet)
- [Android Studio](https://developer.android.com/studio/index.html) or [adb](https://www.xda-developers.com/what-is-adb/)
- An Android device or emulator

# Building the App
To run, clone the repo, open it in Android Studio and press play. It should just work.™

## Install from Android Studio
1. [Install Android Studio](https://developer.android.com/studio/install) and setup an emulator
    1a. If using a device, be sure to [put it in developer mode](https://developer.android.com/studio/debug/dev-options) to enable side-loading apps
2. `Import` the zcash-android-wallet folder.  
    It will be recognized as an Android project.
3. Press play (once it is done opening and indexing)

## OR Install from the command line
To build from the command line, [setup ADB](https://www.xda-developers.com/install-adb-windows-macos-linux/) and connect your device. Then simply run this and it will both build and install the app:
```bash
cd /path/to/zcash-android-wallet
./gradlew
```
Note: The lack of an explicit Gradle task is not a typo. A default task is configured via [build.gradle.kts](build.gradle.kts).

Tip: On macOS and Linux, Gradle is invoked with `./gradlew`.  On Windows, Gradle is invoked with `gradlew`.

# Contributing

Contributions are very much welcomed! Please read our [Contributing Guidelines](/CONTRIBUTING.md) and [Code of Conduct](/CONDUCT.md). Our backlog has many Issues tagged with the `good first issue` label. Please fork the repo and make a pull request for us to review.

# Reporting an issue

If you wish to report a security issue, please follow our [Responsible Disclosure guidelines](https://github.com/zcash/zcash-android-wallet-sdk/blob/master/responsible_disclosure.md).

For other kind of inquiries, feel welcome to open an Issue if you encounter a bug or would like to request a feature.
