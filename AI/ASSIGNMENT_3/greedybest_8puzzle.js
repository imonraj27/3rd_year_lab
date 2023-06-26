const PriorityQueue = require("./priority_queue");

class Board {
    constructor(values = null) {
        if (!values) {
            this.grid = [
                [1, 2, 3],
                [4, 5, 6],
                [7, 8, 0]
            ]
            return;
        } else {
            this.grid = [
                [values[0][0], values[0][1], values[0][2]],
                [values[1][0], values[1][1], values[1][2]],
                [values[2][0], values[2][1], values[2][2]]
            ]
        }
    }

    findBlankPosition() {
        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 3; j++) {
                if (this.grid[i][j] == 0) return [i, j];
            }
        }
    }

    move(direction) {
        let newboard = new Board(this.grid);
        let [i, j] = newboard.findBlankPosition();
        if (direction == "L") {
            if (j == 0) return null;
            let val = newboard.grid[i][j - 1];
            newboard.grid[i][j] = val;
            newboard.grid[i][j - 1] = 0;
        } else if (direction == "R") {
            if (j == 2) return null;
            let val = newboard.grid[i][j + 1];
            newboard.grid[i][j] = val;
            newboard.grid[i][j + 1] = 0;
        } else if (direction == "U") {
            if (i == 0) return null;
            let val = newboard.grid[i - 1][j];
            newboard.grid[i][j] = val;
            newboard.grid[i - 1][j] = 0;
        } else if (direction == "D") {
            if (i == 2) return null;
            let val = newboard.grid[i + 1][j];
            newboard.grid[i][j] = val;
            newboard.grid[i + 1][j] = 0;
        }
        return newboard;
    }

    printBoard(isheuri = false) {
        for (let i = 0; i < 3; i++) {
            let str = "";
            for (let j = 0; j < 3; j++) {
                if (this.grid[i][j] == 0) {
                    str += "  ";
                } else str += this.grid[i][j] + " ";
            }
            console.log(str);
        }
        if (isheuri)
            console.log("Heuristics: " + this.heuristic());
        else
            console.log("-----");
    }

    heuristic() {
        let distance = 0;
        for (let row = 0; row < this.grid.length; row++) {
            for (let col = 0; col < this.grid[row].length; col++) {
                const currentValue = this.grid[row][col];
                if (currentValue !== 0) {
                    const targetRow = Math.floor((currentValue - 1) / this.grid.length);
                    const targetCol = (currentValue - 1) % this.grid.length;
                    distance += Math.abs(targetRow - row) + Math.abs(targetCol - col);
                }
            }
        }
        return distance;
    }

    isGoalNode() {
        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 3; j++) {
                if (this.grid[i][j] != (i * 3 + j + 1) % 9) return false;
            }
        }
        return true;
    }

    convert() {
        let ans = 0;
        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 3; j++) {
                ans = ans * 10 + this.grid[i][j];
            }
        }
        return ans;
    }
}

function greedybest(startNode) {
    const pq = new PriorityQueue();
    pq.enqueue(startNode, startNode.heuristic());
    startNode.cost = 0;
    startNode.path = [];

    let visited = [];

    while (!pq.isEmpty()) {
        const currentNode = pq.dequeue();
        currentNode.printBoard(true);
        visited.push(currentNode.convert());

        if (currentNode.isGoalNode()) {
            return currentNode;
        }

        const neighbors = ["L", "R", "U", "D"];
        const totalCost = currentNode.cost + 1;

        for (let i = 0; i < 4; i++) {
            let newnode = currentNode.move(neighbors[i]);
            if (!newnode || visited.includes(newnode.convert())) {
                continue;
            }
            newnode.cost = totalCost;
            newnode.path = [...currentNode.path];
            newnode.path.push(neighbors[i]);
            pq.enqueue(newnode, newnode.heuristic());
        }
    }

    return null;
}

let gridd = [
    [2, 3, 8],
    [0, 5, 7],
    [1, 4, 6],
]
let board = new Board(
    gridd
);

console.log("============ ORDER OF NODES VISITED ===============");
let ans = greedybest(board);
console.log("==========================================");

let finalboard = new Board(
    gridd
)

console.log("BLANK MOVEMENT: " + ans.path);
console.log("COST: " + ans.cost + " movements");


console.log("============PATH===============");
finalboard.printBoard();
for (let i = 0; i < ans.path.length; i++) {
    finalboard = finalboard.move(ans.path[i]);
    finalboard.printBoard();
}
console.log("==========================================");


