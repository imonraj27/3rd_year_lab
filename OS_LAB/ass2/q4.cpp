/*
 * Copyright (c) IMON RAJ, JADAVPUR UNIVERSITY
 * All rights reserved.
 *
 * SOLUTION TO THE PRODUCER CONSUMER PROBLEM IN CPP
 * CAN ONLY BE EXECUTED IN LINUX BASED MACHINES
 */

#include <iostream>
#include <unistd.h>
#include <stdlib.h>
#include <semaphore.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <time.h>

#define BUF_SIZE 5

using namespace std;

int main()
{

	// THE SEMAPHORES
	sem_t *mutex, *empty, *full;

	// SOME SHARED VARIABLES
	int *total, *itemsConsumed, *rear, *que, p, c;
	int type = 0, id;
	pid_t pid;

	cin >> p >> c;

	/*============INITIALIZING THE SHARED VARIABLES===============*/
	key_t k1 = ftok("/dev", 65), k2 = ftok("/dev", 64), k3 = ftok("/dev", 60), k4 = 893248628;

	int sid1 = shmget(k1, sizeof(int), 0644 | IPC_CREAT);
	int sid2 = shmget(k2, sizeof(int), 0644 | IPC_CREAT);
	int sid3 = shmget(k3, sizeof(int), 0644 | IPC_CREAT);
	int sid4 = shmget(k4, BUF_SIZE * sizeof(int), 0644 | IPC_CREAT);

	itemsConsumed = (int *)shmat(sid1, NULL, 0);
	total = (int *)shmat(sid2, NULL, 0);
	rear = (int *)shmat(sid3, NULL, 0);
	que = (int *)shmat(sid4, NULL, 0);

	*itemsConsumed = 0;
	*total = 0;
	*rear = 0;
	/*==============================================================*/

	/*======INITIALIZING THE SEMAPHORES FOR SYNCHRONIZATION=======*/
	mutex = sem_open("mut", O_CREAT | O_EXCL, 0644, 1);
	empty = sem_open("emp", O_CREAT | O_EXCL, 0644, BUF_SIZE);
	full = sem_open("ful", O_CREAT | O_EXCL, 0644, 0);
	/*============================================================*/

	// FORKING ALL PRODUCERS
	for (int i = 0; i < p; i++)
	{
		pid = fork();
		if (pid == 0)
		{
			type = 1;
			id = i;
			break;
		}
	}

	// FORKING ALL CONSUMERS
	for (int i = 0; i < c; i++)
	{
		if (pid == 0)
			break;
		pid = fork();
		if (pid == 0)
		{
			type = 2;
			id = i;
			break;
		}
	}

	if (type == 1)
	{ // IF IT IS PRODUCER--------------------------------
		srand(time(NULL));
		sleep(rand() % 6 + 1);
		sem_wait(empty);
		sem_wait(mutex);

		/*====PRODUCING RANDOM VALUE STORE IN QUEUE=====*/
		srand(time(NULL));
		int temp = rand() % 80 + 1;
		cout << "Producing.." << temp << endl;
		que[*rear] = temp;
		*rear += 1;
		*rear %= BUF_SIZE;
		/*=============================================*/

		sleep(1);

		sem_post(mutex);
		sem_post(full);
		sleep(1);
	}
	else if (type == 2)
	{ // IF A CONSUMER-------------------------------
		while (true)
		{
			sem_wait(full);
			sem_wait(mutex);

			if ((*itemsConsumed) == 0)
				*total = 0;
			*itemsConsumed += 1;
			if ((*itemsConsumed) > p)
			{
				sem_post(mutex);
				sem_post(full);
				break;
			}

			/*======CONSUMING ITEM========*/
			int data = que[(*itemsConsumed - 1) % BUF_SIZE];
			cout << "Consumer " << id << " consuming.." << data << endl;
			*total += data;
			*rear += 0;
			/*============================*/

			if ((*itemsConsumed) == p)
			{
				sem_post(mutex);
				sem_post(full);
				break;
			}

			sleep(1);

			sem_post(mutex);
			sem_post(empty);
			sleep(1);
		}
	}
	else
	{ // THE PARENT ---------------------------------------
		int status, wpid;
		while ((wpid = wait(&status)) > 0)
			;
		cout << "TOTAL: " << *total << endl;
		sem_unlink("mut");
		sem_close(mutex);

		sem_unlink("ful");
		sem_close(full);

		sem_unlink("emp");
		sem_close(empty);

	} //----------------------------------------------------------

	return 0;
}
