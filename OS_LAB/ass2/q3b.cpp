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
	char str[50];
			
	sem_t *s1 = sem_open("semaphore1", O_CREAT | O_EXCL, 0644, 0);
	sem_t *s2 = sem_open("semaphore2", O_CREAT | O_EXCL, 0644, 0);
	
	int fd1[2]; //sends from child to parent
	int fd2[2]; // sends from parent to child
	
	
	pipe(fd1);
	pipe(fd2);
	
	pid_t pid = fork();
	
	if(pid==0){
		// child process
		for(int i=0; i<4; i++){
			
		//	char str[50];
			
			sem_wait(s1);
			sleep(1);
			
			//close(fd2[1]);
			read(fd2[0],str,sizeof(char)*50);
		//	close(fd2[0]);

			printf("\nChild Got: %s",str);
			
			printf("\nEnter: ");
			scanf("%s",str);
			//close(fd1[0]);
			
			write(fd1[1],str,sizeof(char)*50);
			//close(fd1[1]);
			
			sem_post(s2);
			
		}
		sem_unlink("semaphore1");
		//sem_unlink("semaphore2");
		sem_close(s1);
		//sem_close(s2);
	}else{
		// parent process
		
		for(int i=0; i<4; i++){
			
		//	char str[50];
			
			printf("\nEnter: ");
			scanf("%s",str);
		//	close(fd2[0]);
			
			write(fd2[1],str,sizeof(char)*50);
		//	close(fd2[1]);
			sem_post(s1);
			
			sem_wait(s2);
			sleep(1);
			
			//close(fd1[1]);
			read(fd1[0],str,sizeof(char)*50);
		//	close(fd1[0]);

			printf("\nParent Got: %s",str);
			
		}
		//sem_unlink("semaphore1");
		sem_unlink("semaphore2");
		//sem_close(s1);
		sem_close(s2);
	}
	return 0;
}
