const goalState = [1, 2, 3, 4, 5, 6, 7, 8, 0];

function getChildren(state) {
    const zeroIndex = state.indexOf(0);
    const children = [];

    // Move tile left
    if (zeroIndex % 3 !== 0) {
        const newState = [...state];
        [newState[zeroIndex], newState[zeroIndex - 1]] = [newState[zeroIndex - 1], newState[zeroIndex]];
        children.push(newState);
    }

    // Move tile right
    if (zeroIndex % 3 !== 2) {
        const newState = [...state];
        [newState[zeroIndex], newState[zeroIndex + 1]] = [newState[zeroIndex + 1], newState[zeroIndex]];
        children.push(newState);
    }

    // Move tile up
    if (zeroIndex >= 3) {
        const newState = [...state];
        [newState[zeroIndex], newState[zeroIndex - 3]] = [newState[zeroIndex - 3], newState[zeroIndex]];
        children.push(newState);
    }

    // Move tile down
    if (zeroIndex < 6) {
        const newState = [...state];
        [newState[zeroIndex], newState[zeroIndex + 3]] = [newState[zeroIndex + 3], newState[zeroIndex]];
        children.push(newState);
    }

    return children;
}

function depthLimitedSearch(node, depth, visited, path) {
    if (depth === 0 && node.toString() === goalState.toString()) {
        return path.concat(node);
    }

    if (depth > 0) {
        visited.add(node.toString());
        for (const child of getChildren(node)) {
            if (!visited.has(child.toString())) {
                const solutionPath = depthLimitedSearch(child, depth - 1, visited, path.concat(node));
                if (solutionPath !== null) {
                    return solutionPath;
                }
            }
        }
    }

    return null;
}

function solvePuzzle(startState, maxDepth) {
    for (let depth = 0; depth <= maxDepth; depth++) {
        const visited = new Set();
        const path = depthLimitedSearch(startState, depth, visited, []);
        if (path !== null) {
            return path;
        }
    }

    return null;
}

const startState = [1, 2, 3, 4, 0, 5, 7, 8, 6];
const maxDepth = 20;
const solutionPath = solvePuzzle(startState, maxDepth);

if (solutionPath === null) {
    console.log('No solution found within max depth');
} else {
    console.log(`Solution found in ${solutionPath.length - 1} moves:`);
    for (const state of solutionPath) {

        console.log(state);
    }
}
