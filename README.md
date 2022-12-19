Gradle FLAML Plugin
===================

[![CI](https://github.com/m2ci-msp/gradle-flaml-plugin/actions/workflows/main.yml/badge.svg)](https://github.com/m2ci-msp/gradle-flaml-plugin/actions/workflows/main.yml)
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

FLAML Tasks
-----------

Applying this plugin to a project adds several tasks, which are configured as follows.

### `extractLabFiles` - Extracts XWaves lab files from YAML
#### Inputs
- `yamlFile`, default: `flaml.yamlFile`
#### Outputs
- `destDir`, default: `layout.buildDirectory.dir('lab')`

### `extractTextFiles` - Extracts text files from YAML
#### Inputs
- `yamlFile`, default: `flaml.yamlFile`
#### Outputs
- `destDir`, default: `layout.buildDirectory.dir('text')`

### `extractTextGrid` - Converts YAML to single Praat TextGrid file
#### Inputs
- `flacFile`, default: `flaml.flacFile`
- `yamlFile`, default: `flaml.yamlFile`
#### Outputs
- `textGridFile`, default: `layout.buildDirectory.file("${project.name}.TextGrid")`

### `extractWavFiles` - Extracts WAV files from FLAC+YAML
#### Inputs
- `flacFile`, default: `flaml.flacFile`
- `yamlFile`, default: `flaml.yamlFile`
#### Outputs
- `destDir`, default: `layout.buildDirectory.dir('wav')`

### `generateComments` - Generates comments as Properties file
#### Inputs
- `properties`, default: (empty)
#### Outputs
- `destFile`, default: `layout.buildDirectory.file('comments.properties')`

### `generateFlac` - Generates FLAC from WAV file collection
#### Inputs
- `srcFiles`, default: `layout.buildDirectory.dir('wav')`
- `commentsFile`, default: `generateComments.destFile`
#### Outputs
- `flacFile`, default: `layout.buildDirectory.file("${project.name}.flac")`

### `generateYaml` - Generates YAML from WAV file collection
#### Inputs
- `srcFiles`, default: `generateFlac.srcFiles`
- `commentsFile`, default: `generateComments.destFile`
#### Outputs
- `yamlFile`, default: `layout.buildDirectory.file("${project.name}.yaml")`

### `generateYamlFromTextGrid` - Generates YAML from TextGrid
#### Inputs
- `srcFile`, default: `extractTextGrid.textGridFile`
#### Outputs
- `yamlFile`, default: `layout.buildDirectory.file("${project.name}.yaml")`

### `injectSegments` - Injects lab files into YAML
#### Inputs
- `labDir`, default: `extractLabFiles.destDir`
- `yamlSrcFile`, default: `flaml.yamlFile`
#### Outputs
- `yamlDestFile`, default: `layout.buildDirectory.file("${project.name}.yaml")`

### `injectText` - Injects text files into YAML
#### Inputs
- `textDir`, default:, `extractTextFiles.destDir`
- `yamlSrcFile`, default: `flaml.yamlFile`
#### Outputs
- `yamlDestFile`, default: `layout.buildDirectory.file("${project.name}.yaml")`

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
[YAML]: https://yaml.org/
[Java]: https://java.com/download/
[SoX]: https://sox.sourceforge.net/
