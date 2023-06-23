#!/bin/bash
# ©2023 JFTF
# JFTF-Sdk Python virtual environment deployment script
# Prerequisites
# Ubuntu 22.04+
# Version 1.0

PARENT_DIR="$(cd .. && dirname "$(pwd)")"
PYTHON3_PACK_NAME="python3"
APT_PACKAGES="python3-venv python3-dev python3-pip"

if [[ $EUID -eq 0 ]]; then
  echo "This script cannot be executed as root. Please run it without root privileges."
  exit 1
fi

function install_apt_package() {
  echo "Checking installation of $1"
  if [ "$(dpkg-query -W -f='${Status}' "$1" 2>/dev/null | grep -c "ok installed")" -eq 0 ]; then
    sudo apt-get install "$1" -y
  else
    echo "Package $1 already installed on the system!"
  fi
}

function generate_python_venv() {
  echo "Generating Python virtual environment"
  VENV_DIR="$PARENT_DIR/.venv"
  install_apt_package $PYTHON3_PACK_NAME
  echo "Checking for Python venv existence"
  if [ -d "$VENV_DIR" ]; then
    echo "Found Python venv, skipping generation step!"
  else
    echo "No Python venv found, generating..."
    python3 -m venv "$VENV_DIR"
  fi
  echo "Activating Python venv"
  if source "$VENV_DIR/bin/activate"; then
    echo "Python venv activated successfully!"
    pip3 list
  else
    echo "Python venv failed to activate!"
    exit 1
  fi
}

function install_apt_dependencies() {
  echo "Installing required apt packages"
  for PACKAGE in $APT_PACKAGES; do
    install_apt_package "$PACKAGE"
  done
}

function install_pip_dependencies() {
  echo "Resolving pip dependencies from requirements.txt"
  PIP_REQ_TXT="$PARENT_DIR/requirements.txt"
  if [ -f "$PIP_REQ_TXT" ]; then
    if pip3 install -r "$PIP_REQ_TXT"; then
      echo "Installed Python packages successfully!"
      pip3 list
    else
      echo "Failed to install Python packages, aborting!"
      exit 1
    fi
  else
    echo "Requirements.txt not found, aborting!"
    exit 1
  fi
}

echo
echo "JFTF-Sdk Python virtual environment deployment script"
echo

while true; do
  read -r -p 'Do you want to start the setup process? Y(y)/N(n) ' choice
  case "$choice" in
  n | N)
    exit 0
    break
    ;;
  y | Y)
    echo
    install_apt_dependencies
    echo
    generate_python_venv
    echo
    install_pip_dependencies
    break
    ;;
  *) echo 'Response not valid' ;;
  esac
done

echo
echo "JFTF-Sdk Python virtual environment deployment executed successfully!"
