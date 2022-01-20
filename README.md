# Introduction
During shooting competitions, the time for the atheletes to fire their shots is usually limited. There is either an overall limit for all shots, includig the sightings, or sightings excluded, or for single heats, or even for a single shot. The latter is especially true for most pistol competitions, and for archery competitions, althought these are currently not supported.

This program was developed to support the training for these competitions. You select the discipline, the respective program (if the discipline provides more than one), press the start button, wait until the red light goes off and the green light goes on, than you shoot until green is off again and red is on.

# Limitations
The program is written for a Raspberry PI with a touch screen as the target platform, and it works there – if it was build on that platform …

That is because the UI is built on base of JavaFX, and this relies on a bunch of platform specific libraries.

Currently, the [provided Tar file](https://tquadrat.githup.io/shootingtimer/build/distributions/org.tquadrat.shootingtime-0.3.0,tar) works for x64 Linux only. The provided Zip file is intended for Windows, but is does not work on that platform.

I need to create builds for all platforms (Linux x64, Raspberry PI and Windows), but this is work in progress.

# Documentation

- [Javadoc Reference](https://tquadrat.github.io/shootingtimer/javadoc/index.html)