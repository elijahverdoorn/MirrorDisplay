# Mirror Display

MirrorDisplay is a simple application for Android created to provide customized functionality to a "Magic Mirror". The application is heavily inspired by the original Raspberry Pi MagicMirror project, but instead of using a Pi and a separate screen, this app runs on an Android device. 

When developing this application I focused on simply getting things to work rapidly, preferring iterating quickly over investing in long-term optimizations. I did my best to avoid preventing future improvements, but I know that there are still numerous areas of the code that are suboptimal and could be made more efficient/cleaner.

The application currently supports the following "modules":

### Local Time

Simple, 12-hour digital clock showing the current time of day.

### Local Weather

Shows the current weather, as well as the forecast for tomorrow. This component requires the user to obtain a (free) API key from and provide the desired latitude and longitude in the settings menu.

### Random Bible Verse

Show a single random verse from The Bible as well as the reference for that verse.

### Quote of the Day

Shows a quote, and the speaker of said quote. Users of the application should provide a URL to a file containing quotes that they would like to be displayed. This file should be in the following JSON format:

```
{
  "quotes": [
    {
      "quote": "Hello, World!",
      "speaker": "Elijah Verdoorn"
    },
    {
      "quote": "Another Quote!",
      "speaker": "Someone Famous"
    },
    // ...
  ]
}
```

For an example, see the `data/` directory of this repository. If you're looking for a host for this file, I recommend creating a Gist on GitHub.

Individual modules can be enabled and disabled in the settings menu.

## Contributions

Code contributions to this repository are welcome in the form of Pull Requests. If you use this software and find a bug, feel free to create an issue here on GitHub and I'll do my best to address the problem.
