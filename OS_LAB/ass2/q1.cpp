#include <bits/stdc++.h>
using namespace std;

class Process
{
public:
    int job_id;
    int priority;
    // arrives first time in the ready queue
    int arrival_time;
    // when the process goes back to ready queue after a pre-emption
    int secondary_arrtime;
    vector<int> cpu_bursts;
    vector<int> io_bursts;
    // index for the upcoming CPU / IO burst
    int ci;

    Process(int jid, int p, int at)
    {
        job_id = jid;
        priority = p;
        arrival_time = at;
        secondary_arrtime = at;
        ci = 0;
    }

    void print()
    {
        cout << "PID:" << job_id << "\t" << priority << "\t" << arrival_time << endl;
    }
};

vector<Process> plist;
// maps a process from its job_id to the pointer to itself
unordered_map<int, Process *> mp;

struct comp
{
    bool operator()(Process *const &p1, Process *const &p2)
    {
        // high value high priority
        if (p1->priority < p2->priority)
        {
            return false;
        }
        else if (p1->priority > p2->priority)
        {
            return true;
        }
        else
            // tie-breaker when priorities are equal - check for arrival time
            return p1->secondary_arrtime > p2->secondary_arrtime;
    }
};

void inputAllProcess(string filename)
{
    ifstream f;
    f.open(filename);
    if (!f)
    {
        cout << "File Error!" << endl;
        return;
    }

    int jobid, prio, arrtime;

    while (!f.eof())
    {
        f >> jobid;
        f >> prio;
        f >> arrtime;

        Process p(jobid, prio, arrtime);

        int k, cnt = 0;

        do
        {
            f >> k;
            if (k == -1)
                break;
            if (cnt == 0)
            {
                p.cpu_bursts.push_back(k);
            }
            else
            {
                p.io_bursts.push_back(k);
            }
            cnt = (cnt + 1) % 2;
        } while (true);
        plist.push_back(p);
    }

    f.close();
}

void fcfs()
{
    // a Min Heap - takes out the process with minimal arrival time
    // the pair <arrtime, jobid>
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;

    for (int i = 0; i < plist.size(); i++)
    {
        minHeap.push({plist[i].arrival_time, plist[i].job_id});
        // setting the map to point to the process through its job_id
        mp[plist[i].job_id] = &plist[i];
    }

    // the consumed time so far in CPU execution
    int curtime = 0;
    int i = 0;
    while (!minHeap.empty())
    {
        // taking out the process with minimum arrival time
        // or minimum job_id if tie in arrival time
        auto it = minHeap.top();
        minHeap.pop();

        // finding the reference to the process
        // using its job_id
        Process &p = *mp[it.second];

        // if the next process to be executed
        // is later than the current CPU time then
        // fast forward CPU time - The CPU actually
        // remains idle in the fast-forwarded time
        if (p.secondary_arrtime > curtime)
        {
            curtime = p.secondary_arrtime;
        }

        cout << "------------------------------------" << endl;
        cout << "Time: " << curtime << endl;
        p.print();

        cout << "Executes from " << curtime << " to " << (curtime + p.cpu_bursts[p.ci]) << endl;
        // maintain the CPU clock accordingly
        curtime += p.cpu_bursts[p.ci];
        p.secondary_arrtime = curtime;

        if (p.ci != p.io_bursts.size())
        {
            // if there is a IO burst to be handled next
            cout << "Process going for IO" << endl;
            // when the process returns to ready state after IO
            p.secondary_arrtime += p.io_bursts[p.ci];
        }

        // no more CPU burst left
        if (p.ci == p.cpu_bursts.size() - 1)
        {
            cout << "Process Finished!!" << endl;
        }
        else
        {
            minHeap.push({p.secondary_arrtime, p.job_id});
        }

        p.ci++;
    }
}

void round_robin()
{
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;
    queue<pair<int, int>> waitingQue;

    int QUANTUM = 20; // TIME QUANTUM FOR RR SCHE..

    for (int i = 0; i < plist.size(); i++)
    {
        minHeap.push({plist[i].arrival_time, plist[i].job_id});
        mp[plist[i].job_id] = &plist[i];
    }

    int curtime = 0;
    int i = 0;

    while (!minHeap.empty() || !waitingQue.empty())
    {
        while (!minHeap.empty())
        {
            auto it = minHeap.top();

            if (waitingQue.empty())
            {
                curtime = it.first;
            }
            if (it.first > curtime)
                break;
            minHeap.pop();
            Process &p = *mp[it.second];

            waitingQue.push(it);
        }

        auto it = waitingQue.front();
        Process &p = *mp[it.second];
        waitingQue.pop();

        cout << "------------------------------------" << endl;
        cout << "Time: " << curtime << endl;

        p.print();

        if (p.cpu_bursts[p.ci] > QUANTUM)
        {
            p.cpu_bursts[p.ci] -= QUANTUM;
            cout << "Executed From " << curtime << " to " << (curtime + QUANTUM) << endl;
            curtime += QUANTUM;
            waitingQue.push({curtime, it.second});
        }
        else
        {
            cout << "Executed From " << curtime << " to " << (curtime + p.cpu_bursts[p.ci]) << endl;
            curtime += p.cpu_bursts[p.ci];
            if (p.ci == p.io_bursts.size())
            {
                cout << "Process Finished" << endl;
            }
            else
            {
                cout << "Going for IO" << endl;
                if (p.ci + 1 == p.cpu_bursts.size())
                    cout << "Process Finished!!" << endl;
                else
                    minHeap.push({curtime + p.io_bursts[p.ci], it.second});
                p.ci++;
            }
        }
    }
}

void preemptive_priority()
{
    priority_queue<Process *, vector<Process *>, comp> waiting_queue;

    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> minHeap;

    for (int i = 0; i < plist.size(); i++)
    {
        minHeap.push({plist[i].arrival_time, plist[i].job_id});
        mp[plist[i].job_id] = &plist[i];
    }

    int curtime = 0;

    while (!waiting_queue.empty() || !minHeap.empty())
    {
        if (waiting_queue.empty())
        {
            curtime = minHeap.top().first;
        }

        while (true)
        {
            if (minHeap.empty())
                break;
            auto it = minHeap.top();
            int ttt = it.first;
            if (ttt > curtime)
                break;
            minHeap.pop();
            Process *p = mp[it.second];

            waiting_queue.push(p);
        }

        Process *pp = waiting_queue.top();
        waiting_queue.pop();

        cout << "-----------------------------------------------\n";
        cout << "Time: " << curtime << endl;
        pp->print();

        int endtime = curtime + pp->cpu_bursts[pp->ci];
        int counter = curtime;
        while (!minHeap.empty() && counter < endtime)
        {
            auto it = minHeap.top();
            Process *temp = mp[it.second];
            if (counter == temp->secondary_arrtime)
            {
                minHeap.pop();
                waiting_queue.push(temp);
                if (temp->priority < pp->priority)
                {
                    endtime = counter;
                    break;
                }
            }
            else
                counter++;
        }

        cout << "Executes from " << curtime << " to " << endtime << endl;

        if (endtime == curtime + pp->cpu_bursts[pp->ci])
        {
            if (pp->ci < pp->io_bursts.size())
            {
                cout << "Going for IO..\n";
            }
            else
            {
                curtime = endtime;
                cout << "Process Finished..\n";
                continue;
            }

            pp->secondary_arrtime = endtime + pp->io_bursts[pp->ci];
            pp->ci++;
            if (pp->ci == pp->cpu_bursts.size())
            {
                curtime = endtime;
                cout << "Process Finished..\n";
                continue;
            }
            minHeap.push({pp->secondary_arrtime, pp->job_id});
        }
        else
        {
            cout << "Process Pre-empted..\n";
            pp->secondary_arrtime = endtime;
            pp->cpu_bursts[pp->ci] = (-endtime + curtime + pp->cpu_bursts[pp->ci]);
            waiting_queue.push(pp);
        }

        curtime = endtime;
    }
}

int main()
{
    string filename;
    cin >> filename;
    inputAllProcess(filename);

    // fcfs();
    // round_robin();
    preemptive_priority();

    return 0;
}