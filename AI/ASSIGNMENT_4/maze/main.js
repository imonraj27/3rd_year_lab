// Set up canvas and variables

class PriorityQueue {
    constructor() {
        this.heap = [];
    }

    enqueue(value, priority) {
        const node = { value, priority };
        this.heap.push(node);
        this.bubbleUp(this.heap.length - 1);
    }

    dequeue() {
        if (this.heap.length === 0) {
            return null;
        }
        const min = this.heap[0];
        const lastNode = this.heap.pop();
        if (this.heap.length > 0) {
            this.heap[0] = lastNode;
            this.bubbleDown(0);
        }
        return min.value;
    }

    bubbleUp(index) {
        const node = this.heap[index];
        while (index > 0) {
            const parentIndex = Math.floor((index - 1) / 2);
            const parent = this.heap[parentIndex];
            if (node.priority >= parent.priority) {
                break;
            }
            this.heap[parentIndex] = node;
            this.heap[index] = parent;
            index = parentIndex;
        }
    }

    bubbleDown(index) {
        const node = this.heap[index];
        while (true) {
            const leftChildIndex = 2 * index + 1;
            const rightChildIndex = 2 * index + 2;
            let leftChild, rightChild;
            let swapIndex = null;
            if (leftChildIndex < this.heap.length) {
                leftChild = this.heap[leftChildIndex];
                if (leftChild.priority < node.priority) {
                    swapIndex = leftChildIndex;
                }
            }
            if (rightChildIndex < this.heap.length) {
                rightChild = this.heap[rightChildIndex];
                if (
                    (swapIndex === null && rightChild.priority < node.priority) ||
                    (swapIndex !== null && rightChild.priority < leftChild.priority)
                ) {
                    swapIndex = rightChildIndex;
                }
            }
            if (swapIndex === null) {
                break;
            }
            this.heap[index] = this.heap[swapIndex];
            this.heap[swapIndex] = node;
            index = swapIndex;
        }
    }

    size() {
        return this.heap.length;
    }

    isEmpty() {
        return this.heap.length === 0;
    }
}




let ttt = 15;
const cellSize = 30;
const canvasWidth = ttt * cellSize;
const canvasHeight = ttt * cellSize;
const cols = Math.floor(canvasWidth / cellSize);
const rows = Math.floor(canvasHeight / cellSize);
let grid = [];
let currentCell;


let finished = false;
let dfsfinished = true;
let bfsfinished = true;
let astarfinished = true;


let bfspath = [];
let q = [];
let pq = new PriorityQueue();



function setup() {
    const myCanvas = createCanvas(canvasWidth, canvasHeight);
    myCanvas.parent('canvas-cont');
    frameRate(120);
    // Create a grid of cells
    for (let j = 0; j < rows; j++) {
        for (let i = 0; i < cols; i++) {
            let cell = new Cell(i, j);
            grid.push(cell);
        }
    }
    currentCell = grid[0];
}

function draw() {
    if (!finished) {
        background(255);
        // Draw the grid
        for (let i = 0; i < grid.length; i++) {
            grid[i].show();
        }
        // Mark the current cell as visited
        currentCell.createvisited = true;
        // Choose a random neighbor of the current cell
        let nextCell = currentCell.checkNeighbors();
        if (nextCell) {
            // Remove the wall between the current cell and the chosen cell
            removeWalls(currentCell, nextCell);
            // Move to the chosen cell
            currentCell = nextCell;
        } else if (stack.length > 0) {
            // If there are no unvisited neighbors, backtrack to the last visited cell
            currentCell = stack.pop();
        } else {
            currentCell = grid[0];
            finished = true;
        }
    }

    else if (!bfsfinished) {
        let n = q.length;

        for (let i = 0; i < n; i++) {
            currentCell = q.shift();
            currentCell.setPath();

            if (currentCell.isFinal()) {
                let p = grid[index(rows - 1, cols - 1)];
                let path = [];
                while (p) {
                    path.unshift(p);
                    p = p.parent;
                }

                grid.forEach(e => {
                    e.removePath()
                })

                for (let k = 0; k < path.length - 1; k++) {
                    path[k].setPath(true);
                    connectLine(path[k], path[k + 1])
                }
                bfsfinished = true;
                return;
            }

            let newcost = currentCell.cost + 1;
            let ns = currentCell.getAllNeighbours();

            for (let k = 0; k < ns.length; k++) {
                if (ns[k].bfsvisited) continue;
                ns[k].bfsvisited = true;
                ns[k].cost = newcost;
                ns[k].parent = currentCell;
                q.push(ns[k]);
            }
        }

    }


    else if (!astarfinished) {
        let n = pq.size();

        for (let i = 0; i < n; i++) {

            currentCell = pq.dequeue();
            currentCell.setPath();


            if (currentCell.isFinal()) {
                let p = grid[index(rows - 1, cols - 1)];
                let path = [];
                while (p) {
                    path.unshift(p);
                    p = p.parent;
                }

                grid.forEach(e => {
                    e.removePath()
                })

                for (let k = 0; k < path.length - 1; k++) {
                    path[k].setPath(true);
                    connectLine(path[k], path[k + 1])
                }
                astarfinished = true;
                return;
            }

            let newcost = currentCell.cost + 1;
            let ns = currentCell.getAllNeighbours();

            for (let k = 0; k < ns.length; k++) {
                if (ns[k].astarvisited) continue;
                ns[k].astarvisited = true;
                ns[k].cost = newcost;
                ns[k].parent = currentCell;
                let heiur = ns[k].heiuristic()
                pq.enqueue(ns[k], heiur + newcost)
            }
        }

    }



    else if (!dfsfinished) {
        if (!currentCell.dfsvisited) {
            currentCell.dfsvisited = true;
            currentCell.setPath();
        }

        if (currentCell.isFinal()) {
            stack.push(currentCell)
            for (let k = 0; k < stack.length - 1; k++) {
                connectLine(stack[k], stack[k + 1])
            }
            stack = []
            dfsfinished = true;
            return;
        }
        // Choose a random neighbor of the current cell
        let nextCell = currentCell.checkSearchNeighbours();
        if (nextCell) {
            // Move to the chosen cell
            currentCell = nextCell;
        } else if (stack.length > 0) {
            // If there are no unvisited neighbors, backtrack to the last visited cell
            currentCell.removePath();
            currentCell = stack.pop();
        }
    }

    else {
        console.log("skjdfkjsdf");
    }

}

class Cell {
    constructor(i, j) {
        this.i = i;
        this.j = j;
        this.cost = 0;
        this.parent = null;
        this.createvisited = false;
        this.dfsvisited = false;
        this.bfsvisited = false;
        this.astarvisited = false;
        this.walls = [true, true, true, true];
    }

    isFinal() {
        return (this.i == rows - 1) && (this.j == cols - 1);
    }

    heiuristic() {
        return Math.abs(rows - 1 - this.i) + Math.abs(cols - 1 - this.j)
        // return Math.sqrt(Math.pow(rows - 1 - this.i, 2) + Math.pow(cols - 1 - this.j, 2))
    }

    setPath(c = false) {
        let x = this.i * cellSize;
        let y = this.j * cellSize;
        noStroke();
        if (c)
            fill(0, 100, 0);
        else
            fill(200, 0, 0);
        rectMode(CENTER);
        rect(x + cellSize / 2, y + cellSize / 2, cellSize / 3, cellSize / 3);
    }

    removePath() {
        let x = this.i * cellSize;
        let y = this.j * cellSize;
        noStroke();
        fill(255, 255, 255);
        rectMode(CENTER);
        rect(x + cellSize / 2, y + cellSize / 2, cellSize / 3, cellSize / 3);
    }

    show() {
        let x = this.i * cellSize;
        let y = this.j * cellSize;
        stroke(100);
        this.bfsvisited = false;
        this.dfsvisited = false;
        this.astarvisited = false;

        strokeWeight(3);
        if (this.walls[0]) {
            line(x, y, x + cellSize, y);
        }
        if (this.walls[1]) {
            line(x + cellSize, y, x + cellSize, y + cellSize);
        }
        if (this.walls[2]) {
            line(x + cellSize, y + cellSize, x, y + cellSize);
        }
        if (this.walls[3]) {
            line(x, y + cellSize, x, y);
        }

        noStroke();
        fill(255, 170);
        rectMode(CENTER);
        rect(x + cellSize / 2, y + cellSize / 2, cellSize, cellSize);
    }

    checkNeighbors() {
        let neighbors = [];
        let top = grid[index(this.i, this.j - 1)];
        let right = grid[index(this.i + 1, this.j)];
        let bottom = grid[index(this.i, this.j + 1)];
        let left = grid[index(this.i - 1, this.j)];
        if (top && !top.createvisited) {
            neighbors.push(top);
        }
        if (right && !right.createvisited) {
            neighbors.push(right);
        }
        if (bottom && !bottom.createvisited) {
            neighbors.push(bottom);
        }
        if (left && !left.createvisited) {
            neighbors.push(left);
        }

        if (top && top.createvisited && Math.random() < 0.05) removeWalls(this, top);
        if (bottom && bottom.createvisited && Math.random() < 0.05) removeWalls(this, bottom);
        if (left && left.createvisited && Math.random() < 0.05) removeWalls(this, left);
        if (right && right.createvisited && Math.random() < 0.05) removeWalls(this, right);

        if (neighbors.length > 0) {
            // Choose a random neighbor to visit
            let r = floor(random(0, neighbors.length));
            // Push the current cell onto the stack
            stack.push(currentCell);
            return neighbors[r];
        } else {
            return undefined;
        }
    }

    checkSearchNeighbours() {
        let neighbors = [];
        let top = grid[index(this.i, this.j - 1)];
        let right = grid[index(this.i + 1, this.j)];
        let bottom = grid[index(this.i, this.j + 1)];
        let left = grid[index(this.i - 1, this.j)];
        if (top && !top.dfsvisited && !this.walls[0]) {
            neighbors.push(top);
        }
        if (right && !right.dfsvisited && !this.walls[1]) {
            neighbors.push(right);
        }
        if (bottom && !bottom.dfsvisited && !this.walls[2]) {
            neighbors.push(bottom);
        }
        if (left && !left.dfsvisited && !this.walls[3]) {
            neighbors.push(left);
        }
        if (neighbors.length > 0) {
            let r = floor(random(0, neighbors.length));
            stack.push(currentCell);
            return neighbors[0];
        }
        return undefined;
    }

    getAllNeighbours() {
        let neighbors = [];
        let top = grid[index(this.i, this.j - 1)];
        let right = grid[index(this.i + 1, this.j)];
        let bottom = grid[index(this.i, this.j + 1)];
        let left = grid[index(this.i - 1, this.j)];
        if (top && !top.bfsvisited && !this.walls[0]) {
            neighbors.push(top);
        }
        if (right && !right.bfsvisited && !this.walls[1]) {
            neighbors.push(right);
        }
        if (bottom && !bottom.bfsvisited && !this.walls[2]) {
            neighbors.push(bottom);
        }
        if (left && !left.bfsvisited && !this.walls[3]) {
            neighbors.push(left);
        }

        return neighbors;

    }
}

// Helper function to convert 2D array
// coordinates to a 1D array index
function index(i, j) {
    if (i < 0 || j < 0 || i > cols - 1 || j > rows - 1) {
        return -1;
    }
    return i + j * cols;
}

// Helper function to remove the wall between two adjacent cells
function removeWalls(a, b) {
    let x = a.i - b.i;
    if (x === 1) {
        a.walls[3] = false;
        b.walls[1] = false;
    } else if (x === -1) {
        a.walls[1] = false;
        b.walls[3] = false;
    }
    let y = a.j - b.j;
    if (y === 1) {
        a.walls[0] = false;
        b.walls[2] = false;
    } else if (y === -1) {
        a.walls[2] = false;
        b.walls[0] = false;
    }
}

// Create a stack to keep track of visited cells
let stack = [];

function connectLine(cell1, cell2) {
    let x1 = cell1.i * cellSize + cellSize / 2;
    let y1 = cell1.j * cellSize + cellSize / 2;
    let x2 = cell2.i * cellSize + cellSize / 2;
    let y2 = cell2.j * cellSize + cellSize / 2;

    stroke(60)
    strokeWeight(3);

    line(x1, y1, x2, y2);
}

