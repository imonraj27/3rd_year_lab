// IMPLEMENTATION OF THE LOCAL SEARCH METHOD - HILL CLIMBING
const fs = require('fs');
const { State } = require("../State")

let points = [];

// THE HILL CLIMBING FUNCTION
// TAKES AN INITIAL STATE
// RETURNS A STATE (SOME LOCAL/GLOBAL MAXIMA)
function hillClimbing(initial) {
    let current = initial;
    let found = false;

    while (!found) {
        let currentObjective = current.objectiveFunction();
        let point = current.print();
        points.push(point);
        let neighbours = current.getNeighbours();

        // FINDS OUT THE NEIGHBOUR WITH HIGHEST OBJECTIVE
        let highestNeighbour = neighbours.reduce((max, obj) => {
            return obj.objectiveFunction() > max.objectiveFunction() ? obj : max;
        });

        if (highestNeighbour.objectiveFunction() > currentObjective) {
            current = highestNeighbour;
        } else found = true;
    }

    return current;
}


let initialState = new State(0, 0);
hillClimbing(initialState);

const jsonData = JSON.stringify(points);

// Write the JSON data to a file
fs.writeFile('data.json', jsonData, (err) => {
    if (err) {
        console.error(err);
    } else {
        console.log('Data written to file');
    }
});
