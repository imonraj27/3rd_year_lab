const PriorityQueue = require("./priority_queue");

class JugStates {
    constructor(current_state, goal_state, volume) {
        this.current_state = current_state;
        this.goal_state = goal_state;
        this.volume = volume;
    }

    move(direction) {
        let [leftwater, rightwater] = this.current_state;
        let [leftvol, rightvol] = this.volume;
        if (direction == "LTR") {
            if (rightwater == rightvol || leftwater == 0) {
                return null;
            }

            if (leftwater + rightwater > rightvol) {
                leftwater -= (rightvol - rightwater);
                rightwater = rightvol;
            } else {
                rightwater += leftwater;
                leftwater = 0;
            }
        } else if (direction == "RTL") {
            if (leftwater == leftvol || rightwater == 0) return null;
            if (leftwater + rightwater > leftvol) {
                rightwater -= (leftvol - leftwater);
                leftwater = leftvol;
            } else {
                leftwater += rightwater;
                rightwater = 0;
            }
        } else if (direction == "EL") {
            if (leftwater == 0) return null;
            leftwater = 0;
        } else if (direction == "ER") {
            if (rightwater == 0) return null;
            rightwater = 0;
        } else if (direction == "FR") {
            if (rightwater == rightvol) return null;
            rightwater = rightvol;
        } else if (direction == "FL") {
            if (leftwater == leftvol) return null;
            leftwater = leftvol;
        }

        let newstate = new JugStates([leftwater, rightwater], this.goal_state, this.volume);
        return newstate;
    }

    printJugs() {
        console.log(`[${this.current_state[0]} , ${this.current_state[1]}]`);

        console.log("------");
    }

    isGoalNode() {
        return ((this.current_state[0] == this.goal_state[0]) && (this.current_state[1] == this.goal_state[1]));
    }
}

function ucs(startNode) {
    const pq = new PriorityQueue();
    pq.enqueue(startNode, 0);
    startNode.cost = 0;
    startNode.path = [];

    while (!pq.isEmpty()) {
        const currentNode = pq.dequeue();
        currentNode.printJugs();

        if (currentNode.isGoalNode()) {
            return currentNode;
        }

        const neighbors = ["LTR", "RTL", "EL", "ER", "FR", "FL"];
        const totalCost = currentNode.cost + 1;

        for (let i = 0; i < 6; i++) {
            let newnode = currentNode.move(neighbors[i]);
            if (!newnode) {
                continue;
            }
            newnode.cost = totalCost;
            newnode.path = [...currentNode.path];
            newnode.path.push(neighbors[i]);
            pq.enqueue(newnode, totalCost);
        }
    }

    return null;
}

let k = [[0, 0], [2, 0], [3, 5]];

let state = new JugStates(...k);

console.log("============ ORDER OF NODES VISITED ===============");
let ans = ucs(state);
console.log("==========================================");

let finalstate = new JugStates(...k)

console.log("MOVEMENTS: " + ans.path);
console.log("COST: " + ans.cost + " movements");


console.log("============PATH===============");
finalstate.printJugs();
for (let i = 0; i < ans.path.length; i++) {
    finalstate = finalstate.move(ans.path[i]);
    finalstate.printJugs();
}
console.log("==========================================");



