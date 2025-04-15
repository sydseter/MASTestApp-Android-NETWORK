# Uncrakcable - Level 1-2

An app with a really bad networking issue

## Install the app

1. Open Android Studio and Device Manager
2. Clik Add a new device and create virtual device
3. Make sure "Show obsolete device profiles" are checked
4. Find: Nexus 6P API 23
5. Select and click Next
6. Under API select: API 23 "Marshmallow"; Android 6.0
7. Select system image: x86 (not x86_64)
8. Click finish
9. install adb https://developer.android.com/tools/adb
10. Start the device in device manager
11. Open a console and navigate to the base of this repository
12. Download the apks release
13. Install the apks like this:

    java -jar .github/lib/bundletool.jar install-apks --apks={path to the downloaded apks}/UnCrackable-Level1-MASTG-NETWORK.apks
    # or
    adb install {path to the downloaded apks}/UnCrackable-Level1-MASTG-NETWORK.apk

14. If you want to pentest the app, you can install frida and following this guide: https://medium.com/@SecureWithMohit/getting-started-with-frida-setting-up-on-an-emulator-47980170d2b2

## Insecure CA

The CA is created from this guide: https://gist.github.com/soarez/9688998
All certificate use the passphrase: test


### Creating a (soon to be) invalid ICA for tlsrevocation.org

see [CA.md](CA.md)


### Emulator

Choose Nexus 6P API 23

### Logs

    Debug is on by default

### Semgrep

Install semgrep docker

    docker pull semgrep/semgrep

Run:
    # MainActivity.kt
    docker run --rm -v "${PWD}:/src" semgrep/semgrep semgrep -c rules/mstg-network.yml app/src/main/java/com/example/uncrackable_level1_MASTG_NETWORK/MainActivity.kt
    # App configuration
    docker run --rm -v "${PWD}:/src" semgrep/semgrep semgrep -c rules/mstg-network.yml app/build.gradle.kts
    # Networking rules
    docker run --rm -v "${PWD}:/src" semgrep/semgrep semgrep -c rules/mstg-network-trusted-anchors.yml app/src/main/res/xml/network_security_config.xml
    # App is debuggable
    docker run --rm -v "${PWD}:/src" semgrep/semgrep semgrep -c rules/mstg-network-debuggable.yml app/src/main/AndroidManifest.xml