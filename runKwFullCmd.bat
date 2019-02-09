set projectName=Quantum

REM *** 1. We should already have the %projectName% on Klocwork Server

REM *** 2. Re-capture the build settings for ant, by default the output file will be named kwinject.out
kwmvn clean install compile

REM *** 3. Force a Full Analysis the build specification
kwbuildproject --url http://WIN10ENTVM64:8080/%projectName% -f -o kwtables kwinject.out

REM *** 4.  We load our result with kwadmin to the Klocwork Server
kwadmin --url http://WIN10ENTVM64:8080 load %projectName% kwtables
