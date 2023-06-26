
#include <bits/stdc++.h>

using namespace std;

class Graph
{
private:
    int no_of_nodes;
    vector<vector<int>> adj_list;

public:
    Graph(int _no_of_nodes)
    {
        no_of_nodes = _no_of_nodes;
        adj_list = vector<vector<int>>(_no_of_nodes + 1);
    }

    void addEdge(int u, int v)
    {
        adj_list[u].push_back(v);
        adj_list[v].push_back(u);
    }

    vector<int> getADjNodes(int u)
    {
        return adj_list[u];
    }

    int getNoOfNodes()
    {
        return no_of_nodes;
    }

    void printadj_list()
    {
        string encode = "";
        for (int c = 1; c <= no_of_nodes; c++)
        {
            cout << (c) << ": ";
            for (auto x : adj_list[c])
            {
                encode += to_string(c);
                encode += "@";
                encode += to_string(x);
                encode += "@0#";
                cout << x << " ";
            }
            cout << endl;
        }
        encode.pop_back();
        string command = "python3 /Users/debasmitroy/Desktop/Test_prog/plot.py " + encode;
        //        cout<<command<<endl;
        //        fork();
        //        system(command.c_str());
    }
};

void dfsUtil(Graph &ug, vector<bool> &visited, int src)
{
    if (visited[src])
        return;

    cout << src << " ";
    visited[src] = true;

    for (auto x : ug.getADjNodes(src))
        dfsUtil(ug, visited, x);
}

void dfs(Graph &ug)
{
    int n = ug.getNoOfNodes();
    vector<bool> visited(n, false);
    for (int v = 1; v <= n; v++)
    {
        if (!visited[v])
            dfsUtil(ug, visited, v);
    }
}

void dfs_serach_util(Graph &ug, vector<bool> &visited, vector<int> &parentArr, vector<int> &order, bool &isFound, int src, int par, int target)
{
    if (visited[src])
        return;

    visited[src] = true;

    if (isFound == false)
    {
        order.push_back(src);
    }

    if (src == target)
        isFound = true;

    for (auto x : ug.getADjNodes(src))
    {
        if (visited[x] == false)
        {
            if (parentArr[x] == -1)
                parentArr[x] = src;
            dfs_serach_util(ug, visited, parentArr, order, isFound, x, src, target);
        }
        if (isFound == false && par != x)
            order.push_back(src);
    }
}

void getPath(vector<int> &parent, vector<int> &path, int cur, int source)
{
    path.push_back(cur);
    if (source == cur)
    {
        return;
    }
    if (cur == -1 || cur == 0)
    {
        path.clear();
        return;
    }
    getPath(parent, path, parent[cur], source);
}

void dfs_search(Graph &ug, int src, int target)
{
    int n = ug.getNoOfNodes();
    vector<bool> visited(n, false);
    vector<int> parentArr(n + 1, -1);
    vector<int> path;
    vector<int> order;
    bool isFound = false;

    dfs_serach_util(ug, visited, parentArr, order, isFound, src, -1, target);
    getPath(parentArr, path, target, src);

    if (isFound)
    {
        cout << "\nOrder  : ";
        for (auto x : order)
            cout << x << " ";

        //        cout<<"\nParents  : \n";
        //        for(int i = 1; i<=n; i++)
        //            cout<<i<<" "<<parentArr[i]<<endl;

        reverse(path.begin(), path.end());
        cout << "\nSolution Path  : ";
        for (auto x : path)
            cout << x << " ";
        cout << endl;
    }
    else
        cout << "\nPath Does Not Exist\n";
}

void bfs_search_Util(Graph &ug, vector<bool> &visited, int src, int target)
{
    queue<int> q;
    q.push(src);
    visited[src] = true;
    int n = ug.getNoOfNodes();
    vector<int> parentArr(n + 1, -1);
    parentArr[src] = 0;
    cout << "Order : ";
    while (!q.empty())
    {
        int nd = q.front();
        cout << nd << " ";
        if (nd == target)
            break;
        q.pop();
        for (auto x : ug.getADjNodes(nd))
        {
            if (parentArr[x] == -1)
                parentArr[x] = nd;
            if (!visited[x])
            {
                q.push(x);
                visited[x] = true;
            }
        }
    }

    vector<int> path;
    getPath(parentArr, path, target, src);
    reverse(path.begin(), path.end());

    cout << "\nSolution Path  : ";
    for (auto x : path)
        cout << x << " ";
    cout << endl;
}

void bfs_search(Graph &ug, int src, int target)
{
    int n = ug.getNoOfNodes();
    vector<bool> visited(n, false);
    bfs_search_Util(ug, visited, src, target);
}

void depthLimitedSearchUtil(Graph &ug,
                            vector<bool> &visited,
                            vector<int> &parentArr,
                            vector<int> &order,
                            bool &isFound,
                            int src,
                            int par,
                            int target,
                            vector<int> depthArr,
                            int cur_depth,
                            int LIMIT)
{

    if (visited[src])
        return;

    visited[src] = true;

    if (isFound == false)
        order.push_back(src);

    if (src == target)
        isFound = true;

    for (auto x : ug.getADjNodes(src))
    {
        if (visited[x] == false && cur_depth + 1 <= LIMIT)
        {
            if (parentArr[x] == -1)
                parentArr[x] = src;
            depthLimitedSearchUtil(ug, visited, parentArr, order, isFound, x, src, target, depthArr, cur_depth + 1, LIMIT);
        }
        if (isFound == false && par != x)
            order.push_back(src);
    }
}

vector<int> depthLimitedSearch(Graph &ug, int src, int target, int LIMIT, int PRINT_FLAG = true)
{
    int n = ug.getNoOfNodes();
    vector<bool> visited(n, false);
    vector<int> parentArr(n + 1, -1);
    vector<int> depthArr(n + 1, INT_MAX);
    vector<int> path;
    vector<int> order;
    bool isFound = false;

    depthLimitedSearchUtil(ug, visited, parentArr, order, isFound, src, -1, target, depthArr, 0, LIMIT);
    getPath(parentArr, path, target, src);

    if (isFound)
    {
        cout << "\nOrder  : ";
        for (auto x : order)
            cout << x << " ";
        cout << endl;
        reverse(path.begin(), path.end());
        if (PRINT_FLAG)
        {
            cout << "\nSolution Path  : ";
            for (auto x : path)
                cout << x << " ";
            cout << endl;
        }
        return path;
    }
    else
        cout << "\nPath Does Not Exist\n";
    return path;
}

void idfsSearch(Graph &ug, int src, int target, int MAX_DEPTH)
{
    for (int d = 0; d <= MAX_DEPTH; d++)
    {
        cout << "Current Limit: " << d;
        auto path = depthLimitedSearch(ug, src, target, d, false);
        if (path.size() != 0)
        {
            cout << "FINAL CONCLUSION: IDFS PATH => ";
            for (auto x : path)
                cout << x << " ";
            cout << endl;
            return;
        }
    }
    cout << "\nFINAL CONCLUSION: Path Does Not Exist\n";
}

int main(int argc, const char *argv[])
{
    // insert code here...
    int n, e;
    cout << "ENTER NUMBER OF NODES: ";
    cin >> n;
    cout << "\nENTER NUMBER OF EDGES: ";
    cin >> e;
    cout << "fuck off";
    Graph ug(n);
    cout << "\nENTER THE EDGES: ";
    for (int i = 0; i < e; i++)
    {
        int x, y;
        cin >> x >> y;
        ug.addEdge(x, y);
    }

    cout << "\nENTER START AND DEST NODE: ";
    int s, d;
    cin >> s >> d;

    ug.printadj_list();

    cout << "DFS";
    dfs_search(ug, s, d);
    cout << "BFS\n";
    bfs_search(ug, s, d);
    cout << "DLS\n";
    depthLimitedSearch(ug, s, d, 3);
    cout << "IDDFS\n\n";
    idfsSearch(ug, s, d, 10);
    return 0;
}
