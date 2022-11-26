Gradle FLAML Plugin
===================

[Unreleased]
------------

### Added

- Testing on Java 11 and 17

### Removed

- Testing on Java 10, 13, and 14

### Changed

- Build with Gradle v7.5.1
- Upgraded dependencies
- Migrated from Travis CI to GitHub Actions
- Refactored functional tests
- [all changes since v0.3.0]

[v0.3.0] - 2018-07-23
---------------------

### Added

- new tasks to
  - extract TextGrid from YAML (and FLAC)
  - generate FLAC by concatenating WAV files
  - generate YAML from WAV files or TextGrid
  - inject text from text files into YAML
  - inject segments from XWaves lab files into YAML

### Changed

- switched license to LGPL
- upgrade Gradle to v4.9
- upgrade jtgt to v0.6.1
- deferred configuration via Provider API
- [all changes since v0.2.0]

[v0.2.0] - 2018-06-18
---------------------

### Added

- improved documentation
- functional tests
- support for Java 9 and 10

### Changed

- upgrade Gradle to v4.8
- [all changes since v0.1.0]

[v0.1.0] - 2017-03-02
---------------------

Initial release

[Unreleased]: https://github.com/m2ci-msp/gradle-flaml-plugin/tree/master
[all changes since v0.3.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/compare/v0.3.0...HEAD
[v0.3.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/releases/tag/v0.3.0
[all changes since v0.2.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/compare/v0.2.0...v0.3.0
[v0.2.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/releases/tag/v0.2.0
[all changes since v0.1.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/compare/v0.1.0...v0.2.0
[v0.1.0]: https://github.com/m2ci-msp/gradle-flaml-plugin/releases/tag/v0.1.0
