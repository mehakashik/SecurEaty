# SecurEaty

## Description:
SECUREATY is a cutting-edge project that brings together a team of security experts and Android app developers. The project's primary objective is two-fold: to simulate attacks on the Android OS and develop a sophisticated security application designed to detect potential threats on users' devices. This project encompasses a total of seven simulated attacks, including MSFvenom, WishFish, Keylogger, Hackvoice, Stagefright, Overlay Attack, and Webview.

## Attack Implementation:
The attack team, consisting of four skilled individuals, has meticulously detailed and executed seven different attack scenarios, revealing vulnerabilities within the Android operating system. These attacks are not intended for malicious purposes but are invaluable tools for understanding Android security mechanisms and learning how attackers manipulate the system.

## Security Application (SECUREATY):
The security app development team, comprised of four dedicated members, has created a robust security application named SECUREATY, which serves as a shield against potential threats on Android devices. This application embodies two vital modules:

1. Malicious Code Detection: Users can open the SECUREATY application, select .apk files they wish to scan for suspicious behavior, and receive immediate results indicating whether the file is safe or potentially dangerous. In the background, the app extracts all .apk files from the user's device, which are then sent to a remote service for decompilation. The application receives a .zip file in return, extracts its contents, and thoroughly analyzes the source code to detect potential attacks.

2. Permission Checker: One of the primary vectors for attacks is unauthorized permissions granted by users. The SECUREATY app provides a Permission Checker module, enabling users to review the permissions granted to installed applications. It displays a comprehensive list of permissions each app has access to, empowering users to make informed decisions about their app usage.

## Signature-Based Heuristic Approach:
For this project, a simplified "proof-of-concept" heuristic approach has been employed. The intention is not to replicate the full capabilities of an antivirus but to use a concept of signaturing and detection similar to the heuristic approach and the colored Petri net concept taught in class.

## The Signature Concept used in the Project:
The .apk file is searched for occurrences of unique functionalities that enable malicious actions. The target of the analysis is the decompiled source code of the .apk, including all files containing Java code and the "android manifest" of the application. The "android manifest" is essential for checking the permissions required by an application.

## Detection Algorithms:
Three different algorithms were developed for this project, each responsible for detecting a specific type of attack. These algorithms were created after in-depth research on each type of attack, including observation of their behavior when installed and executed. Malicious applications were also installed on emulators to analyze their actions. Through this research, it was observed that certain portions of code, when combined, perform malicious actions. A string search was conducted by compiling regular expressions or simple substrings of malicious code. For each regex or substring, a "true" value was obtained if a match was found; otherwise, it was "false."

The final decision regarding the presence of an attack is made by combining all the values with a logical AND (&&) operator. This decision-making process could be further improved by computing the probability of attack presence. Values are gathered, and based on the number of "true" and "false" results, a floating-point number (0 < x < 1) is assigned, indicating how many conditions are met for an application to be classified as a potential attack.