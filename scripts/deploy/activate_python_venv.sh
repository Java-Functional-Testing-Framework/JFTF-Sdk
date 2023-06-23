#!/bin/bash
# ©2023 JFTF
# JFTF-Sdk virtual environment activation script
# Prerequisites
# Ubuntu 22.04+
# Virtual environment generated with the "deploy_python_venv.sh" script
# Version 1.0

# Usage: source activate_dev_venv.sh

VENV_PATH="../../.venv"

# Check if the virtual environment path is provided
if [ -z "$VENV_PATH" ]; then
  echo "Error: Please provide the path to the virtual environment."
  exit 1
fi

# Check if the virtual environment exists
if [ ! -d "$VENV_PATH" ]; then
  echo "Error: Virtual environment not found in the provided path."
  exit 1
fi

# Activate the virtual environment
source $VENV_PATH/bin/activate

echo "Virtual environment activated successfully!"
