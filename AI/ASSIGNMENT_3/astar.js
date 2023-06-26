const PriorityQueue = require("./priority_queue");

function astar(graph, startNode, endNode) {
    const pq = new PriorityQueue();
    pq.enqueue(startNode, 0 + heuristic(startNode));

    const visited = new Map();
    visited.set(startNode, { cost: 0, path: [startNode] });

    while (!pq.isEmpty()) {
        const currentNode = pq.dequeue()[0];

        if (currentNode === endNode) {
            return visited.get(currentNode);
        }

        const neighbors = graph[currentNode];

        for (let neighbor in neighbors) {
            const totalCost = visited.get(currentNode).cost + neighbors[neighbor];

            visited.set(neighbor, { cost: totalCost, path: visited.get(currentNode).path.concat(neighbor) });
            pq.enqueue(neighbor, totalCost + heuristic(neighbor));
        }
    }

    return null;
}


const graph = {
    A: { B: 1, F: 20 },
    B: { C: 1, D: 10 },
    C: { D: 1 },
    D: { E: 1, F: 1 },
    E: { F: 1 },
    F: {}
};

function heuristic(node) {
    const vals = {
        A: 1,
        B: 1,
        C: 15,
        D: 1,
        E: 15,
        F: 0
    };

    return vals[node];
}

const startNode = 'A';
const endNode = 'F';

const result = astar(graph, startNode, endNode);

if (result) {
    console.log(`The shortest path from ${startNode} to ${endNode} is ${result.cost}\nShotest path: ${result.path.join(' -> ')}.`);
} else {
    console.log(`There is no path from ${startNode} to ${endNode}.`);
}
