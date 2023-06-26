const PriorityQueue = require("./priority_queue");

const COUNTT = 3;

let MESSAGES = {
    "LCM": "One Cannibal & one missionary from Left to right..",
    "RCM": "One Cannibal & one missionary from Right to left..",
    "LCC": "Two Cannibals from Left to right..",
    "RCC": "Two Cannibals from Right to left..",
    "LMM": "Two Missionaries from Left to right..",
    "RMM": "Two Missionaries from Right to left..",
    "RC": "One Cannibal from Right to left..",
    "LC": "One Cannibal from Left to right..",
    "RM": "One Missionary from Right to left..",
    "LM": "One Missionary from Left to right.."
}

class MissionaryCannibalState {
    constructor(lc = COUNTT, lm = COUNTT, rc = 0, rm = 0, ltr = true) {
        this.lc = lc;
        this.lm = lm;
        this.rc = rc;
        this.rm = rm;
        this.ltr = ltr;
    }


    move(direction) {
        let nlc = this.lc, nlm = this.lm, nrc = this.rc, nrm = this.rm;
        let done = false;
        if (this.ltr) {
            if (direction == "LCM") {
                if (!this.isFeasible(nlc - 1, nlm - 1, nrc + 1, nrm + 1)) return null;
                done = true;
                console.log(MESSAGES[direction]);
                nlc--;
                nlm--;
                nrc++;
                nrm++;
            } else if (direction == "LMM") {
                if (!this.isFeasible(nlc, nlm - 2, nrc, nrm + 2)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlm -= 2;
                nrm += 2;
            } else if (direction == "LCC") {
                if (!this.isFeasible(nlc - 2, nlm, nrc + 2, nrm)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlc -= 2;
                nrc += 2;
            } else if (direction == "LM") {
                if (!this.isFeasible(nlc, nlm - 1, nrc, nrm + 1)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlm -= 1;
                nrm += 1;
            } else if (direction == "LC") {
                if (!this.isFeasible(nlc - 1, nlm, nrc + 1, nrm)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlc -= 1;
                nrc += 1;
            }
        } else {
            if (direction == "RCM") {
                if (!this.isFeasible(nlc + 1, nlm + 1, nrc - 1, nrm - 1)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlc++;
                nlm++;
                nrc--;
                nrm--;
            } else if (direction == "RMM") {
                if (!this.isFeasible(nlc, nlm + 2, nrc, nrm - 2)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlm += 2;
                nrm -= 2;
            } else if (direction == "RCC") {
                if (!this.isFeasible(nlc + 2, nlm, nrc - 2, nrm)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlc += 2;
                nrc -= 2;
            } else if (direction == "RM") {
                if (!this.isFeasible(nlc, nlm + 1, nrc, nrm - 1)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlm += 1;
                nrm -= 1;
            } else if (direction == "RC") {
                if (!this.isFeasible(nlc + 1, nlm, nrc - 1, nrm)) return null;
                done = true;
                console.log(MESSAGES[direction]);

                nlc += 1;
                nrc -= 1;
            }
        }

        if (!done) return null;
        let newstate = new MissionaryCannibalState(nlc, nlm, nrc, nrm, !this.ltr);
        return newstate;
    }

    isFeasible(lc, lm, rc, rm) {
        return ((lc >= 0) && (lm >= 0) && (rc >= 0) && (rm >= 0) && (lm == 0 || lc <= lm) && (rm == 0 || rc <= rm));
    }

    print(isheuri = false) {
        console.log(`M: ${this.lm} |---| M: ${this.rm}`);
        console.log(`C: ${this.lc} |---| C: ${this.rc}\n`);
    }

    heuristic() {
        // return 0;
        return Math.floor((this.lc + this.lm) / 2);
    }

    isGoalNode() { // SELF EXPLANATORY
        return (this.rc == COUNTT && this.rm == COUNTT);
    }

    convert() {
        return (this.ltr) ? 0 : 10000 + 1000 * this.lm + 100 * this.lc + 10 * this.rm + this.rc;
    }
}

function astar(startNode) {
    // INITIALIZING THE PRIORITY QUEUE AND INSERTING THE START NODE
    const pq = new PriorityQueue();
    pq.enqueue(startNode, startNode.heuristic());
    startNode.cost = 0;
    startNode.path = [];

    let vis = [];

    while (!pq.isEmpty()) {
        // TAKING OUT THE HIGH-PRIORITY NODE OUT AS CURRENT NODE
        const currentNode = pq.dequeue();
        currentNode.print(true);

        if (currentNode.isGoalNode()) {
            return currentNode;
        }

        const neighbors = ["LMM", "LCC", "LCM", "LM", "LC", "RMM", "RCC", "RCM", "RM", "RC"];
        const totalCost = currentNode.cost + 1;

        // GOING THROUGH ALL NEIGHBOURS(POSSIBLE) OF THE CURRENT NODE
        for (let i = 0; i < neighbors.length; i++) {
            let newnode = currentNode.move(neighbors[i]);
            if (!newnode) {
                // console.log(vis);
                continue;
            }
            //   vis.push(newnode.convert());

            newnode.cost = totalCost;
            newnode.path = [...currentNode.path];
            newnode.path.push(neighbors[i]);
            pq.enqueue(newnode, totalCost + newnode.heuristic());
        }
    }

    return null;
}

let state = new MissionaryCannibalState();

console.log("============ ORDER OF NODES VISITED ===============");
let ans = astar(state);
console.log("==========================================");

let finalstate = new MissionaryCannibalState();

console.log("SOLUTION PATH: " + ans.path);
console.log("COST: " + ans.cost + " movements");


console.log("============PATH===============");
finalstate.print();
for (let i = 0; i < ans.path.length; i++) {
    finalstate = finalstate.move(ans.path[i]);
    finalstate.print();
}
console.log("==========================================");


