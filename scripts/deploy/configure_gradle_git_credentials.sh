#!/bin/bash
# ©2023 JFTF
# JFTF-Sdk GitHub Packages credentials configuration in ~/.gradle/gradle.properties
# Version 1.0

# Prompt for GitHub username
read -p "Enter your GitHub username: " githubUsername

# Prompt for GitHub token
read -p "Enter your GitHub token: " githubToken
echo

# Path to gradle.properties file
gradleProperties="$HOME/.gradle/gradle.properties"

# Check if gradle.properties file exists
if [ -f "$gradleProperties" ]; then
  # Update existing GitHub username and token
  sed -i "s/^gpr\.user=.*/gpr.user=$githubUsername/" "$gradleProperties"
  sed -i "s/^gpr\.key=.*/gpr.key=$githubToken/" "$gradleProperties"
else
  # Create new gradle.properties file with GitHub username and token
  echo "gpr.user=$githubUsername" >>"$gradleProperties"
  echo "gpr.key=$githubToken" >>"$gradleProperties"
fi

echo "GitHub username and token have been set in ~/.gradle/gradle.properties"

echo
# Display the contents of gradle.properties
echo "Contents of gradle.properties:"
cat "$gradleProperties"
