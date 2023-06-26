#include <iostream>
#include <unistd.h>
#include <sys/wait.h>

using namespace std;

int main()
{

	int id1 = fork(), id2;
	if (id1)
	{
		id2 = fork();
	}

	if (id1 == 0)
	{
		for (int i = 0; i < 10; i++)
		{
			cout << "Process X - ITERATION: " << i << endl;
			sleep(1);
		}
	}
	else if (id2 == 0)
	{
		for (int i = 0; i < 10; i++)
		{
			cout << "Process Y - ITERATION: " << i << endl;
			sleep(2);
		}
	}
	else
	{
		int wpid, status = 0;
		while ((wpid = wait(&status)) > 0)
			;
	}
}
