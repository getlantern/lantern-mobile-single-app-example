# Lantern for a Single Mobile Application

## Using the binary build

Download the [latest libflashlight.aar][8] build from our [release page][9].

## How to compile from source?

### Prerequisites

* An OSX or Linux box
* [docker][2]
* [Android Studio][3]
* [Go 1.5.1][4]
* [GNUMake][6]
* [Mercurial][7]: You can try installing it with `brew` or `macports`.

Get Lantern source:

```
mkdir -p ~/projects
cd ~/projects
git clone https://github.com/getlantern/lantern.git
cd lantern
git checkout 9311914a34cab4dc6b83575db0b00826a878d444
```

Get the code of this example:

```
mkdir -p ~/projects
cd ~/projects
git clone https://github.com/getlantern/lantern-mobile-single-app-example.git
```

Compile `libflashlight.aar` and build the APK file as well:

```
cd lantern-mobile-single-app-example
FIRETWEET_MAIN_DIR=$PWD/app make -C $PWD/../lantern android-lib
make
```

## Testing the example project

Open [Android Studio][3]. In the welcome screen choose "Open an existing
Android Studio project".

![Android Studio](https://cloud.githubusercontent.com/assets/385670/11543640/7089e632-9903-11e5-8ab9-e4c54a4a1cd6.png)

You'll be prompted with a file dialog, browse to the
"flashlight-android-tester" directory and select it, press *OK*.

![Selecting example app](https://cloud.githubusercontent.com/assets/385670/11543671/9fa6002c-9903-11e5-9456-058064e34ac9.png)

Wait a bit for the import to complete.

![Waiting for it](https://cloud.githubusercontent.com/assets/385670/11543834/72e40362-9904-11e5-941d-063fa14f0b74.png)

Hit the *Run app* action under the *Run* menu and deploy it to a real device or
to an ARM-based emulator (armeabi-v7a).

If everything goes OK, you'll have a simple app with two buttons, you can start
a Lantern proxy by touching the *ON* button.

## How to proxy a single app?

First, see the [MainActivity.java][11] example.

Basically you need to import the `libflashlight.aar` library into your project
and import the `go.client.Client` package:

```java
import import go.client.Client;
```

You can then launch a proxy with this piece of code:

```java
try {
  Client.RunClientProxy("127.0.0.1:9192",
    "MyApp",
    new Client.GoCallback.Stub() {
      @Override
      public void Do() {
      }
    });
} catch (Exception e) {
  throw new RuntimeException(e);
}
```

When the proxy launches you can set the application settings to use
`127.0.0.1:9192` as [HTTP
proxy](http://developer.android.com/reference/android/provider/Settings.Global.html#HTTP_PROXY).


```java
System.setProperty("http.proxyHost", "127.0.0.1");
System.setProperty("http.proxyPort", "9192");

System.setProperty("https.proxyHost", "127.0.0.1");
System.setProperty("https.proxyPort", "9192");
```

Please note that the Lantern example listens on `0.0.0.0` by default, so it
allows  any computer on your network to proxy through the phone. You probably
don't want that. Change the `0.0.0.0` address to `127.0.0.1` in order to open a
local port on your phone.

```java
Client.RunClientProxy("127.0.0.1:9192",
  ...
});
```

## Test run

Run the application on a real device, if you know the IP of the device you should
be able to proxy through the phone, like this:

```sh
curl -x 10.10.100.97:9192 http://www.google.com/humans.txt
# Google is built by a large team of engineers, designers, researchers, robots, and others in many different sites across the globe. It is updated continuously, and built with more tools and technologies than we can shake a stick at. If you'd like to help us out, see google.com/careers.
```

You may not want everyone proxying through your phone! Tune the
`RunClientProxy()` function on the `MainActivity.java` accordingly.

If you chose to run flashlight inside an emulator instead of a real device, you
can connect to it by first using telnet to set up port redirection:

Identify the port number your emulator is listening to

![screen shot 2015-01-30 at 6 40 52 pm](https://cloud.githubusercontent.com/assets/385670/5985952/6afa23e2-a8b0-11e4-942a-384f483d331a.png)

In this case its listening on the `5554` local port.

Open a telnet session to the emulator and write the instruction `redir add
tcp:9192:9192` to map the emulator's `9192` port to our local `9192` port.

```sh
telnet 127.0.0.1 5554
# Trying 127.0.0.1...
# Connected to localhost.
# Escape character is '^]'.
# Android Console: type 'help' for a list of commands
# OK
redir add tcp:9192:9192
# OK
```

Now you'll be able to connect to the emulator's flashlight proxy through your
local `9192` port:

```sh
curl -x 10.10.100.97:9192 https://www.google.com/humans.txt
# Google is built by a large team of engineers, designers, researchers, robots, and others in many different sites across the globe. It is updated continuously, and built with more tools and technologies than we can shake a stick at. If you'd like to help us out, see google.com/careers.
```

## Other example projects

* [Firetweet][10].

[2]: https://www.docker.com/
[3]: http://developer.android.com/tools/studio/index.html
[4]: http://golang.org/
[6]: http://www.gnu.org/software/make/
[7]: http://mercurial.selenic.com/wiki/Download
[8]: https://github.com/getlantern/lantern-mobile-single-app-example/releases/download/20151202.1/libflashlight.aar
[9]: https://github.com/getlantern/lantern-mobile-single-app-example/releases
[10]: https://github.com/getlantern/firetweet/
[11]: https://github.com/getlantern/lantern-mobile-single-app-example/blob/master/app/src/main/java/org/getlantern/flashlighttester/MainActivity.java
