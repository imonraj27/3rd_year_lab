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
	
	unsigned int sem_value = 1;
	sem_t *sem = sem_open("semaphore", O_CREAT | O_EXCL, 0644, sem_value);
	
	int id1 = fork(),id2;
	if(id1){
	 id2 = fork();
	}
	
	if(id1==0){
		for(int i=0; i<10; i++){
			sem_wait(sem);
			cout << "Process X - ITERATION: " << i << endl;
			srand(time(NULL));
			sleep(rand()%4+1);
			//sem_post(sem);
		}
	}else if(id2==0){
		for(int i=0; i<10; i++){
			//sem_wait(sem);
			cout << "Process Y - ITERATION: " << i << endl;
			srand(time(NULL));
			sleep(rand()%2+1);
			sem_post(sem);
		}
	}else{
		int wpid, status=0;
		while((wpid=wait(&status))>0);
		sem_unlink("semaphore");
		sem_close(sem);
	} 
	
}
