#!/bin/bash
# ©2023 JFTF
# JFTF-Sdk JFTF test application configuration for gradle build file in ~/.gradle/gradle.properties
# Version 1.0

# Prompt for class name
read -p "Enter the class name: " className

# Prompt for test group
read -p "Enter the test group: " testGroup
echo

# Path to gradle.properties file
gradleProperties="$HOME/.gradle/gradle.properties"

# Check if gradle.properties file exists
if [ -f "$gradleProperties" ]; then
  # Update existing jftf.class_name and jftf.test_group properties
  if grep -q "^jftf\.class_name=" "$gradleProperties"; then
    sed -i "s/^jftf\.class_name=.*/jftf.class_name=$className/" "$gradleProperties"
  else
    echo "jftf.class_name=$className" >>"$gradleProperties"
  fi

  if grep -q "^jftf\.test_group=" "$gradleProperties"; then
    sed -i "s/^jftf\.test_group=.*/jftf.test_group=$testGroup/" "$gradleProperties"
  else
    echo "jftf.test_group=$testGroup" >>"$gradleProperties"
  fi
else
  # Create new gradle.properties file with jftf.class_name and jftf.test_group properties
  echo "jftf.class_name=$className" >>"$gradleProperties"
  echo "jftf.test_group=$testGroup" >>"$gradleProperties"
fi

echo "Class name and test group have been set in ~/.gradle/gradle.properties"

echo
# Display the contents of gradle.properties
echo "Contents of gradle.properties:"
cat "$gradleProperties"
