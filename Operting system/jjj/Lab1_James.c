/* Lab 1: Creating process hierarchy */

/* John James: */

/* all necessary header files are included */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

/*******************************************/

/* complete the code to solve the problem as 
described in the lab1 problem description */

int main(){
    pid_t procid;
    procid = fork();

    if(procid > 0){
        printf("This is the parent process with ID %d\n", getpid());
        printf("This is the child process with ID %d\n", procid);
        wait(NULL);
        printf("The parent process with ID %d is terminating\n", getpid());
    }
    
    else if (procid == 0) {
        printf("This is the child process with ID %d\n", getpid());
        printf("The child is about to create a grandchild process\n");
        procid = fork();
        if(procid > 0) {
            wait(NULL);
            printf("The child process with ID %d is terminating\n", getpid());
        }
        else {
            printf("This is the grandchild process with ID %d\n", getpid());
        }
        else {
            fprintf (stderr, "Forking failed.. code terminating");
            return 1;
         }
    }
        
    else {
        fprintf (stderr, "Forking failed.. code terminating");
        return 1;
    }
    return 0; // return from main()
} 