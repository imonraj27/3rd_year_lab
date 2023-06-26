
// GLOBAL STEPCOST VALUE 
let stepCost = 0.01;

//STATE CLASS
class State {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    // OBJECTIVE FUNCTION
    objectiveFunction() {
        // RETURNS THE OBJECTIVE BASED ON SOME MATHEMATICAL FUNCTION
        return Math.sin(this.x) * Math.cos(2 * this.y + Math.PI / 4);
        // return (2 - this.x ** 2 - this.y ** 2 + this.x + this.y) + Math.sin(this.x) * Math.cos(2 * this.y + Math.PI / 4)
    }

    // RETURNS THE NEIGHBOURS
    getNeighbours() {
        let neighbours = [];
        let arr = [stepCost, -stepCost, 0];

        for (let i = 0; i < arr.length; i++) {
            for (let j = 0; j < arr.length; j++) {
                if (arr[i] == 0 && arr[j] == 0) continue;
                neighbours.push(new State(this.x + arr[i], this.y + arr[j]));
            }
        }
        return neighbours;
    }

    // prints a state
    print() {
        console.log(`\nSTATE(X:${this.x}, Y:${this.y}) --> OBJECTIVE FUNCTION: ${this.objectiveFunction()}`);
        return [this.x, this.y, this.objectiveFunction()];
    }
}


module.exports = { State, stepCost }