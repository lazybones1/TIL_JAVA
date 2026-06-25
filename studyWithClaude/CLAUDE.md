# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "org.example.YourTestClass"

# Run a single test method
./gradlew test --tests "org.example.YourTestClass.yourTestMethod"

# Run main class
./gradlew run
```

## Project Structure

- Java project managed by Gradle (Gradle 8.2 wrapper included)
- Group: `org.example`, project name: `studyWithClaude`
- JUnit Jupiter (JUnit 5) for testing
- Source root: `src/main/java/org/example/`
- Test root: `src/test/java/org/example/`

## Context

This is a Java learning/practice project focused on concurrency, error handling, and design skills. New practice files are added to `src/main/java/org/example/` as standalone exercises.
