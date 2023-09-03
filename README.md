
<div align="center">
  <img src="media/pluginIcon.png" alt="Plugin Icon" width="128">
  <h2>Adaptive Caret Scroll</h2>
  <h4>an Intellij IDEA Plugin</h4>
</div>

## Overview

Adaptive Caret Scroll is an IntelliJ IDEA plugin that enhances the editor's scrolling behavior by maintaining a user-defined distance from the caret to the top and bottom edges of the editor. The plugin aims to improve code readability and the overall coding experience by optimizing the visible area around the caret. This project is implemented in Kotlin and uses the IntelliJ SDK.

## Features

- Auto-scrolling that adjusts the editor's visible area based on the caret's position
- Configurable top and bottom padding settings
- Immediate integration with any newly-opened or existing editor instances within the IDE

## Installation

1. Download the `.jar` file from the GitHub releases.
2. Open IntelliJ IDEA, go to `File > Settings > Plugins`.
3. Click `Install Plugin from Disk` and select the downloaded `.jar` file.

Or install it directly from IntelliJ's Plugin Marketplace.

## Configuration

1. Go to `File > Settings > Adaptive Caret Scroll`.
2. Configure the distance from the top and bottom edges that you prefer.
3. Click `Apply`.

## How to Use

Once installed and configured, the plugin will automatically adjust the editor's scrolling behavior based on the configured settings and the caret's current position.

## Development

### Prerequisites

- Java JDK 11 or higher
- IntelliJ IDEA

### Building from Source

1. Clone the repository:  
   `git clone https://github.com/username/adaptive-caret-scroll.git`
2. Open the project in IntelliJ IDEA.
3. Navigate to `File > Project Structure > Project SDK` and ensure the JDK version is set correctly.
4. Build the project:  
   `Build > Build Project`

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgments

This plugin is inspired by the Vim `set so=15` setting, which serves a similar function in the Vim editor.

---

This README provides all essential details about the project, allowing both users and potential contributors to understand its functionality and architecture.