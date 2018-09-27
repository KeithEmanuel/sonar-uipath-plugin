# Configuration

### SonarQube Server
To use this plugin, you will need to have a SonarQube Server instance running. It could on either a local or remote machine.
Please follow [this guide](https://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) to learn how to setup an instance of SonarQube server.

### Downloading the plugin
TODO

### Building the plugin from source
The plugin is built using Maven. You will need to install Maven if you do not have it already.
After cloning the repository, you can run "mvn clean package" in the project root directory to create the jar output. It will be located in PROJECT_ROOT/target, named sonar-uipath-plugin-*.jar.

### Installing the plugin
To install the plugin, simply copy the jar file to SONARQUBE_ROOT/extensions/plugins, then restart the SonarQube server.