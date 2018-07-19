Gradle FLAML Plugin
===================

[![Build Status](https://travis-ci.org/m2ci-msp/gradle-flaml-plugin.svg?branch=master)](https://travis-ci.org/m2ci-msp/gradle-flaml-plugin)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

A utility plugin for Gradle, allowing lightweight management of speech data in [FLAC] format and corresponding metadata in [YAML] format (see below).

Prerequisites
-------------

[Java] (8 or newer) and [SoX] must be installed.
[FLAC] must be installed for testing.

Plugin Usage
------------

See https://plugins.gradle.org/plugin/org.m2ci.msp.flaml

Configuration
-------------

This plugin adds a `flaml` extension, which is configured with the (relative) paths to a [FLAC] and [YAML] file like this:

```gradle
flaml {
  flacFile = 'foobar.flac'
  yamlFile = 'foobar.yaml'
}
```

FLAML (FLAC+YAML) Convention
----------------------------

The FLAML convention assumes a [FLAC] file and corresponding [YAML] file describing its contents.

The YAML file is expected to contain a list of utterances, each of which is a map that must have
- a `prompt` key, where the prompt is used as the basename for individual utterance files
- a `text` key, containing the orthographic text contents of the utterance
- a `start` key providing the start time (in seconds) of the utterance in the FLAC file
- an `end` key, providing the end time (in seconds) of the utterance in the FLAC file
- *optionally* a `segments` key, providing a list of segments in the utterance, where each segment is a map that must have
  - a `lab` key, providing the label of the segment
  - a `dur` key, providing the duration (in seconds) of the segment

Below is an example:
```yaml
- prompt: foo
  text: Foo.
  start: 0.1
  end: 0.4
  segments:
  - lab: f
    dur: 0.1
  - lab: u
    dur: 0.2
- prompt: bar
  text: Bar.
  start: 0.6
  end: 0.9
  segments:
  - lab: b
    dur: 0.1
  - lab: a
    dur: 0.1
  - lab: r
    dur: 0.1
```

[FLAC]: https://xiph.org/flac/
[YAML]: http://yaml.org/
[Java]: https://java.com/download/
[SoX]: http://sox.sourceforge.net/
