# CS346-project-fall-2023

## Wiki page
https://git.uwaterloo.ca/k4tieu/cs346-project-fall-2023/-/wikis

## To download the application:
Link to installer: https://git.uwaterloo.ca/k4tieu/cs346-project-fall-2023/-/tree/main/application_installer?ref_type=heads

##  To run the server:
Run this command to pull the docker image for the server: docker pull tk2k2/cs346-server:latest

Run this command to run the docker image for the server: docker run -d -p 8080:8080 tk2k2/cs346-server

##  To generate JAR file for the server:
https://www.jetbrains.com/help/idea/compiling-applications.html#package_into_jar


## To run the app:
After running the server from the Docker image, just run the application created from the installer.

## Meeting Minutes
CS 346: Meeting Minutes   
Date:  Monday, October 2, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda

- Create tasks to add to our backlog

- Think about the overall project design (classes, etc.)


Notes 

- Thought process for implementing UI:

- Local whiteboard displayed for all users, when a change (draw erase etc.) is inputted, data is sent to server and then updated for all users connected.

- Every shape/line/text is its own object (factory design pattern here)

- “Object” top class (member funcs like delete, resize, copy, etc.)

- Line, shape, stroke, text, etc. subclasses (will have diff overriden functions e.g. resizing, color, font)
Maybe a “selectedBy” property that is of type User object, and if its not empty, will highlight with a user color

- Thought process for connecting users:
shared room number code to enter and see whiteboard?
Users:
userid
Room:
roomid
list of users
Whiteboard (UI)
Etc. 

Decisions

- Implement whiteboard UI
- Window

CS 346: Meeting Minutes   
Date:  Monday, October 9, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda

- Ask TA/Prof about clarifications about project 

Notes 

- local whiteboard UI -> change
s go to backend -> get updated to the other clients whiteboard
can start with just one whiteboard (no room for now)

- create whiteboard and shapes classes

- later on need to serialize the shapes and classes to JSON to store it on the backend
use @Serializable to convert data class for shapes to JSON (with coordinates)

Decisions

- create backlog of tasks

- start with creating whiteboard canvas

- start creating base shapes and pen classes functionality


CS 346: Meeting Minutes   
Date:  Monday, October 16, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda

- Create tasks to add to our backlog
- Finalize project design outline


Notes 

- Will have just one overall canvas that all users who open whiteboard will see and use built in functions for the shapes (eg. drawRec), textbox, and sketch for lines
- Shape class with child rectangle, circle, and triangle

Actions
- Assign working on:
drawing sketch line - TK,
making toolbar – Cindy,
textbox – Ed,
shapes - Annalisa


CS 346: Meeting Minutes   
Date:  Monday, October 23, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda

- updates on ongoing tasks


Notes 

- whiteboard canvas is created and is working
toolbar and the associated buttons are working (some filler/placeholder ones)
- basic sketch function is working
all shapes classes are created and can be placed on canvas wherever tapped but not movable (static)
- problem: need to figure out how to track the shapes movement and drag
- given current implementation, complicated to track the coordinates to see which shape is being moved
- textbox created but cannot click in to type to change words but can be tapped and placed onto canvas
  
Actions
- Assign working on:
Making different colors and brush sizes for sketch
Make shapes movable -> may require reimplementation,
Fix textbox so that you can type in and also move


CS 346: Meeting Minutes   
Date:  Monday, October 30, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- sketch can now change color and brush sizes
- textbox can be typed in and placed onto canvas
- completely refactored shapes by not using the draw functions but using Box() and composable functions for each shape to create it’s own
- this way we can use the onDrag() to be able to track movement of what shapes are being moved
- made toolbar look prettier and cleaner
 
 
Actions
- start looking into the users and delete functionality
- look into backend functionality as well
 

CS 346: Meeting Minutes   
Date:  Monday, Nov 6, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- Looking into delete function
- Implemented undo functionality for delete instead because hard to track state of when to delete for each shape onClick() -> still looking into it
- Started setting up the backend server and database
  
Actions
- Keep looking into delete functionality
- Get more started on figuring out how to implement the backend


CS 346: Meeting Minutes   
Date:  Monday, Nov 13, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- Got backend for the sketch working
- Now saves the state of drawn lines on canvas
- Setup websockets
- Started implementing the UI for the users
- Textbox for entering username
- Display for active users
  
Actions
- Implement backend for users, shapes, and textbox
 
 

CS 346: Meeting Minutes   
Date:  Monday, Nov 20, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- Got backend for the users working
- Now can enter username and will update and save current users of the whiteboard
- Started working on creating backend for shapes and textbox
- Problem for figuring out how to delete in the backend because don’t know how to keep track and figure out how to delete for the shapes as well as eraser for the sketch
- How to erase sketch of multiple points connected to a single line in backend?
  
Actions
- Figure out more backend stuff
 

CS 346: Meeting Minutes   
Date:  Monday, Nov 27, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- Trying to get shapes and backend working in backend but running into problems
- Refactor class definitions and attributes to make it serializable
-  Points and shapes are being tracked and added in the logs but not appearing on the canvas
- Textboxes are working but very slow and running endless loop -> don’t know if it is most ideal (may need to refactor)
- Might not be able to delete
- May just need to have a clear all functionality
- For erase maybe just white over with the pen? (easier alternative rather than trying so hard and spending more time on figuring out how to erase in backend)


Actions
- Figure out more backend stuff
 

CS 346: Meeting Minutes   
Date:  Monday, Dec 4, 2023                Team:  210     
Present: Edward, Tieu, Annalisa, Cindy
Absent:   
Agenda
- Updates on ongoing tasks

Notes 
- Got backend for shapes and textbox to work!
- Implemented clear all functionality and erase
  
Actions
- Wrapping up final details and code details
- Making sure all requirements for final submissions are there and dividing up remaining tasks




# Diagram
[](url)
