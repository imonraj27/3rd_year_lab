#include<iostream>
#include <string>
#include <sys/types.h>
#include<sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

int fd[2];
int data;

void createListenerProcesses(int n, int id){
	while(n--){
		if(id!=0){
			int id2 = fork();
			id = id2;
			if(id==0){
				close(fd[1]);
				cout << "\nrr" << read(fd[0],&data,sizeof(int));
				close(fd[0]);
				cout << "Child: " << data << endl;
			}
		}
	}
}

int main(){
	
	pipe(fd);
	
	int id = 22;
	int data = 120;
	
	write(fd[1],&data,sizeof(int));
	close(fd[1]);
int n=5;
	
	createListenerProcesses(n,id);
	
	for(int i=0; i<n; i++){
	wait(NULL);
	
	}
		
	
	return 0;
}
