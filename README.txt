



                               _                  _____                       __   ____
                  _____ _____ (_)____ ___   ___  / ___/ ____   __  __        / /  / __ \
                 / ___// ___// // __ `__ \ / _ \ \__ \ / __ \ / / / /       / /  / / / /
                / /__ / /   / // / / / / //  __/___/ // /_/ // /_/ /       / /_ / /_/ /
                \___//_/   /_//_/ /_/ /_/ \___//____// .___/ \__, /       /_/(_)\____/
                                                    /_/     /____/




Submission Structure
~~~~~~~~~~~~~~~~~~~~
.zip
    crimeSpy.jar
    README.txt
    [classes]
    [javadoc]
    [src]
    [target]



Import Into Eclipse
~~~~~~~~~~~~~~~~~~~
1. Unzip the contents of the .zip file into the desired directory
2. From Eclipse, go File -> Import -> Maven -> Existing Maven Projects
3. Select the directory of the unzipped project for the root directory
4. Check the box next to crimeSpy project
5. Click finish
6. In the folder navigator, open the lib folder and right click the .jar files and click 'Add to build path'
7. Right click the pom.xml file and go Run as -> 4 Maven generate-sources


Import Into Intellij
~~~~~~~~~~~~~~~~~~~
1. Unzip the contents of the .zip file into the desired directory
2. From Intellij, go File -> Import Project -> Select unzipped directory
3. Deal with dependencies using Maven by going Maven Projects -> Download sources and documentation
4. If on UC lab machine, use the program Python Internet Enabler to allow Maven to connect to the Internet
5. Ensure the Target bytecode version is set to 1.6 by going File -> Settings -> Build, Execution, Deployment -> Compiler -> Java Compiler -> Adjust the 2nd column of the table accordingly


Execution of .jar From Command Line
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1. From command prompt
2. Navigate to the folder containing the crimeSpy.jar file
3. Run crimeSpy Program
    Linux  :> java -jar crimeSpy.jar
    Windows:> java -jar crimeSpy.jar


Developers
~~~~~~~~~~
Zachary Todd
Nicolas Robinson-O'Brien
Patrick Nicholls
Ben Moskovitz
James Bayly
