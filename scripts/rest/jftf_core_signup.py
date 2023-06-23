#!/bin/python3
# ©2023 JFTF
# JFTF-Sdk interactive script for JFTF-Core RESTful API user signup
# Prerequisites
# Ubuntu 22.04+
# Virtual environment generated with the "deploy/deploy_python_venv.sh" script
# Virtual environment activated with the "deploy/activate_python_venv.sh" script
# Version 1.0

import requests
import json


def jftf_signup():
    url = "http://localhost:8000/api/rest-auth/signup/"

    print("Welcome to JFTF Signup!")
    print("Please provide the following details:")

    email = input("Enter your email: ")
    username = input("Enter your username: ")
    password1 = input("Enter your password: ")
    password2 = input("Confirm your password: ")

    data = {
        "email": email,
        "username": username,
        "password1": password1,
        "password2": password2
    }

    headers = {
        "Content-Type": "application/json"
    }

    print("\nSending signup request...\n")

    response = requests.post(url, data=json.dumps(data), headers=headers)

    print("API Response:")
    if response.status_code == 204:
        print("Signup successful!")
    else:
        response_json = response.json()
        response_pretty = json.dumps(response_json, indent=4)
        print(response_pretty)


if __name__ == "__main__":
    jftf_signup()
