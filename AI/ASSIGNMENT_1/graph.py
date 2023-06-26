class Graph:
    def __init__(self, n):
        self.n = n
        self.adj = {i: [] for i in range(n)}

    def add_edge(self, u, v):
        self.adj[u].append(v)
        self.adj[v].append(u)

    def DFS(self, node, dest):
        if self.vis[node]:
            return

        self.vis[node] = True
        self.order.append(node)
        self.path.append(node)

        if node == dest:
            self.flag = True
            return

        for it in self.adj[node]:
            self.DFS(it, dest)
            if self.flag == True:
                return

        self.path = self.path[:-1]

    def utilize_DFS(self, u, v):
        self.vis = [False for i in range(self.n)]
        self.flag = False
        self.order = []
        self.path = []
        self.DFS(u, v)
        self.order = " ".join([str(x) for x in self.order])
        self.path = " ".join([str(x) for x in self.path])
        print(f"ORDER OF NODES VISITED :\n{self.order}")

        if self.flag == True:
            print(f"SOLUTION PATH:\n{self.path}")
        else:
            print(f"THERE IS NO PATH FROM {u} TO {v}!!!")

    def utilize_BFS(self, u, v):
        queue = [(u, f"{u} ")]
        flag = False
        order = []
        vis = [False for i in range(self.n)]
        while len(queue) > 0:
            node = queue[0][0]
            path = queue[0][1]
            queue = queue[1:]
            vis[node] = True
            order.append(node)
            if node == v:
                flag = True
                break
            for it in self.adj[node]:
                if not(vis[it]):
                    newpath = path + f"{it} "
                    queue.append((it, newpath))

        order = [str(x) for x in order]
        order = " ".join(order)

        print(f"ORDER OF NODES VISITED:\n{order}")

        if flag == True:
            print(f"SOLUTION PATH:\n{path}")
        else:
            print(f"THERE IS NO PATH FROM {u} TO {v}!!!")


if __name__ == "__main__":
    n = int(input("NUMBER OF NODES:"))
    g = Graph(n)
    e = int(input("NUMBER OF EDGES:"))
    print("ENTER EDGES:")
    for i in range(e):
        u, v = [int(x) for x in input().split()]
        g.add_edge(u, v)

    while True:
        print("1. USE DFS")
        print("2. USE BFS")
        print("3. EXIT")
        x = int(input())
        if x == 3:
            break

        u, v = [int(y) for y in input("ENTER SOURCE AND DEST NODE: ").split()]

        if x == 1:
            g.utilize_DFS(u, v)
        else:
            g.utilize_BFS(u, v)
