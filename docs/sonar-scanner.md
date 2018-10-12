# Using sonar-scanner from the command line

[Follow this link](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) for instructions on installing and running sonar-scanner from the command line.

Once installed, you can run 'sonar-scanner' in the UiPath project directory to start an analysis. You will need to specify two properties via a configuration file or through command line arguments.

For example:
`sonar-scanner -D sonar.projectKey="MyProject" -D sonar.sources="." -X`

The -D flag lets you define a property, and the -X flag enables debug output.