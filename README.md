# Meowthentication

Welcome to Meowthentication, the premier OAuth service solution brought to you by stray-cat-developers. Meowthentication offers sleek, purr-fectly designed OAuth integrations with Kakao, Apple, Google, and Naver, powered by the robust environment of JDK 21 and utilizing Gradle for seamless builds and management.

## Prerequisites

Before embarking on your journey with Meowthentication, ensure you have the following installed on your system:

- JDK 21: Required for running the Java environment.
- Gradle: Used for building and managing dependencies.

You can check your JDK installation by running:

```bash
java -version
```

If you do not have these installed, follow the installation guides appropriate for your operating system for [JDK 21](https://jdk.java.net/21/) and [Gradle](https://gradle.org/install/).

## Getting Started

To dive into Meowthentication, clone this repository to your local machine:

```bash
git clone https://github.com/stray-cat-developers/meowthentication.git
cd Meowthentication
```

## Installation

After cloning the repository, navigate to the project directory. Use Gradle to build the project:

```bash
gradlew build
```

This command compiles the project and prepares it for execution.

## Configuration

Meowthentication requires specific configurations for each OAuth provider. Below are the steps to set up each service:

### Kakao

1. Register your application on the Kakao Developers site.
2. Obtain the `Client ID` and `Client Secret`.
3. Set the callback URL in your application settings.

### Apple, Google, Naver, Etc...

Follow similar steps for Apple, Google, and Naver, ensuring you have the necessary credentials and configuration for each platform.

## Running Meowthentication

To launch Meowthentication, use Gradle to run the application:

```bash
gradlew run
```

This command starts the Meowthentication service, ready to handle your OAuth needs.

## Contributing

Contributions are always welcome at Meowthentication! Whether you're suggesting new features, reporting bugs, or contributing code, please feel free to open an issue or submit a pull request.

## License

Meowthentication is proudly open-sourced under the MIT license. See the `LICENSE` file for more details.

## Contact

For any inquiries or feedback related to Meowthentication, please don't hesitate to open an issue in this repository or reach out through our official communication channels.

Thank you for choosing Meowthentication. Let's make OAuth integration smoother and more secure together!