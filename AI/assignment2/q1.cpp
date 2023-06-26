#include <bits/stdc++.h>

using namespace std;

/**
 * @brief
 * Program to implement DLS , IDS , IBS
 *
 */

bool pathStarted = false;
bool found = false;

// vector<int> order;

void limitError()
{
    cout << "LIMIT ENDS -> ";
}

void printPath(vector<int> &path)
{
    cout << "\nPATH: ";
    for (int i : path)
    {
        cout << i << " -> ";
    }
    cout << endl;
}

// IBS function
void IBS(vector<int> adj[], int start, int goal, int max_lev)
{
    int depthLimit = 0;
    queue<int> q;
    unordered_set<int> visited;

    q.push(start);
    visited.insert(start);

    while (!q.empty())
    {
        int size = q.size();

        for (int i = 0; i < size; i++)
        {
            int node = q.front();
            cout << node << "-";
            q.pop();

            if (node == goal)
            {
                cout << "\nFound............";
                cout << "In depth:... " << depthLimit;
                return;
            }

            for (int neighbor : adj[node])
            {
                if (visited.find(neighbor) == visited.end())
                {
                    q.push(neighbor);
                    visited.insert(neighbor);
                }
            }
        }

        if (depthLimit == max_lev)
            break;
        depthLimit++;
    }

    cout << "\nGoal Node Not Found......";
}

void DepthLimitedSearch(int remaining_level, vector<int> adj[], int node, vector<bool> &vis, int start, int end, vector<int> &path)
{
    if (vis[node])
        return;

    vis[node] = true;

    cout << node << " -> ";

    if (found)
    {
        cout << "found\n";
        return;
    }

    if (node == end)
        found = true;

    path.push_back(node);

    if (remaining_level == 0)
    {
        limitError();
        vis[node] = false;
        if (!found)
            path.pop_back();
        return;
    }

    for (int neighbour : adj[node])
    {

        DepthLimitedSearch(remaining_level - 1, adj, neighbour, vis, start, end, path);
    }

    if (!found)
    {
        path.pop_back();
    }
}

void idfsSearch(vector<int> adj[], int node, vector<bool> &vis, int start, int end, vector<int> &path, int MAX_DEPTH)
{
    for (int d = 0; d <= MAX_DEPTH; d++)
    {
        cout << "\nCurrent Limit: " << d << "\n";
        for (int i = 0; i < vis.size(); i++)
            vis[i] = false;
        found = false;
        path.clear();
        DepthLimitedSearch(d, adj, start, vis, start, end, path);
        if (path.size() != 0 && found)
        {
            cout << "\nPATH EXISTS FOR MAX_LEVEL " << d;
            return;
        }
    }
    cout << "\nFINAL CONCLUSION: Path Does Not Exist\n";
}

int main()
{
    cout << "Enter number of vertices: ";
    int v;
    cin >> v;
    vector<int> *adj = new vector<int>[v];
    vector<int> path;

    cout << "Enter number of edges: ";
    int e;
    cin >> e;
    cout << "Enter edges: ";

    for (int i = 0; i < e; i++)
    {
        int s, e;
        cin >> s >> e;
        adj[s].push_back(e);
        adj[e].push_back(s);
    }

    vector<bool> vis;
    int remlev, start, end, temp;
    cout << "Enter lvl,start,end: ";
    cin >> remlev >> start >> end;

    for (int i = 0; i < v; i++)
        vis.push_back(false);

    cout << "=======================DLS==================" << endl;
    DepthLimitedSearch(remlev, adj, start, vis, start, end, path);
    printPath(path);

    for (int i = 0; i < v; i++)
        vis[i] = false;

    cout << "=======================IDFS==================" << endl;
    idfsSearch(adj, start, vis, start, end, path, remlev);
    printPath(path);

    cout << "=======================IBS==================" << endl;
    IBS(adj, start, end, remlev);
}