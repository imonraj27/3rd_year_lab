#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <fcntl.h>

#include <time.h>

#include <semaphore.h>
#include <sys/shm.h>


#include <errno.h>
#include <sys/wait.h>

using namespace std;

int main(){
	
	sem_t *s1 = sem_open("semaphore1", O_CREAT | O_EXCL, 0644, 1);
	sem_t *s2 = sem_open("semaphore2", O_CREAT | O_EXCL, 0644, 0);
	
	int id1 = fork(),id2;
	if(id1){
	 id2 = fork();
	}
	
	if(id1==0){
		for(int i=0; i<10; i++){
			sem_wait(s1);
			cout << "Process X - ITERATION: " << i << endl;
			srand(time(NULL));
			sleep(rand()%4+1);
			sem_post(s2);
		}
	}else if(id2==0){
		for(int i=0; i<10; i++){
			sem_wait(s2);
			cout << "Process Y - ITERATION: " << i << endl;
			srand(time(NULL));
			sleep(rand()%2+1);
			sem_post(s1);
		}
	}else{
		int wpid, status=0;
		while((wpid=wait(&status))>0);
		sem_unlink("semaphore1");
		sem_unlink("semaphore2");
		sem_close(s1);
		sem_close(s2);
	} 
	
}
