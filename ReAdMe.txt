Welcome to AutoCM's SCM GIT Configuration Management System!

This ReAdMe describes what is required to set up your repository in
order to work with AutoCM and what is required for Project Owners.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
AutoCM Requirements

Build Requirement
   Create a build directory that houses the build wrapper script.
   Best practice is to name the directory "build" and the wrapper
   script either build.sh for UNIX or build.bat for Windows.
   See diagram below for example.

Deploy Requirement
   In order to deploy the built objects, the built objects must be
   copied to a directory/folder named "package" by the build script.
   This "package" directory is created by AutoCM so no need to create
   one in your projects repository. The "package" directory will be
   directly below the Project directory. See diagram below for example
   
Layout Diagram Example
   For your project FIRSTAPI_CONNECT_PAY_ANDROID, the directory
   structure will look similar to the following:

         FIRSTAPI_CONNECT_PAY_ANDROID
        /    |      \
     build source... package
      /        
  build.sh
   
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Project Owner Requirements

A project owner will have all the authority to manage his/her projects
access controls for all his/her developers. AutoCM creates the initial
project access controls and they currently allow the project owner the
following abilities:

Read access:        This gives the project owner the ability to view the
                    access settings in the gerrit console.
Push access:        This gives the project owner the ability to push to
                    the master repository without requiring a code review.
Label Code-Review : This gives the project owner the ability to have +2
                    authority during code reviews.
Submit:             This gives the ability to submit code reviews.
Push Annotated Tag: This gives the push annotated tag functionality to
                    developers.
Push Signed Tag:    This gives the push signed tag functionality to
                    developers.

Two groups are configured by AutoCM. One group contains the project owner.
The other group contains the project owner and is setup for the project
owner to manage his developers. The two groups are named:
   FIRSTAPI_CONNECT_PAY_ANDROID_OWNER
   FIRSTAPI_CONNECT_PAY_ANDROID_DEVELOPERS

When creating a branch that you want AutoCM to build and deploy from please
use the project id as a prefix when naming your branch. E.g.
   FIRSTAPI_CONNECT_PAY_ANDROID_NOV_RELEASE

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
References

   Gerrit AutoCM Production URL: http://n3pvap1003.1dc.com:8080/gerrit2
   
   Gerrit documentation URL: 
   http://n3pvap1003.1dc.com:8080/gerrit2/Documentation/index.html
   
   
If you have any questions contact your AutoCM representative.

Feel free to delete this ReAdMe.txt file at any time.

Enjoy
