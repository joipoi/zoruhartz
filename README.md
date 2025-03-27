# Description
This is a project for someone I know who works as a dentist. It allows them to add cases of their clients to keep track of dates and times and details.
This project uses javafx and maven.

You can also view the data as a Gantt Chart.

The Gantt Chart code was mostly stolen from a stackoverflow question that can be found here:
https://stackoverflow.com/questions/27975898/gantt-chart-from-scratch

# Packaging
I wanted to package this application as an exe file so somebody could run it easily without having java installed

I followed a guide that usese jlink and maven for this, which can be found here:
https://www.jetbrains.com/help/idea/javafx.html#package-app-with-jlink

The problem with this is it does not create a exe file, but it does make a bat file that runs the program so it kinda works.
The command to package is:
mvn javafx:jlink 

# Saving the data
I am saving the data in a csv file. A problem I had was making sure the path is the same both when I am developing it and when it is packaged.
This resulted in me doing a akward file structure for the packaged thing but whatever
