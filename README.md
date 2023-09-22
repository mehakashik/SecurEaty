# SecurEaty

The Security application (SECUREATY) embodies two modules:
The first part of the application is dedicated for detection of malicious code in .apk files. Users open the application, choose files they want to scan for suspicious behavior, and then get the result, which indicates, whether the file is safe or dangerous.

Meanwhile in the background, the application extracts all .apk files from user’s phone and stores them. After user’s choice of files to scan, they are sent to a remote service, which performs decompilation and returns .zip file as a result. The application then performs file unzipping, and analyzes source code for detection of attacks.

The second part is responsible for checking permissions. Permissions are the key to various attacks, since the user/victim in many cases, grants permissions without being aware of the consequences. Using this permission checker, the user can check whether the applications installed in the device are safe to use based on the permissions granted. It displays a list of permissions that the app is currently granted access to.
