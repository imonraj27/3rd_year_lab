// Example input transition table
const transitionTable = [
    { from: 0, to: 1, symbol: 'a' },
    { from: 0, to: 2, symbol: 'b' },
    { from: 1, to: 3, symbol: 'a' },
    { from: 1, to: 4, symbol: 'b' },
    { from: 2, to: 4, symbol: 'a' },
    { from: 2, to: 3, symbol: 'b' },
    { from: 3, to: 5, symbol: 'a' },
    { from: 4, to: 5, symbol: 'b' },
];

// Create NFA data from transition table
const data = { nodes: [], links: [] };
const states = [...new Set(transitionTable.map((t) => t.from).concat(transitionTable.map((t) => t.to)))];
states.forEach((state) => {
    data.nodes.push({ id: state, isAccepting: false });
});
transitionTable.forEach((transition) => {
    data.links.push({
        source: transition.from,
        target: transition.to,
        symbol: transition.symbol
    });
});

// Create SVG container
const svg = d3.select("svg");

// Add links to SVG
const link = svg.append("g")
    .attr("class", "links")
    .selectAll("line")
    .data(data.links)
    .enter()
    .append("line")
    .attr("class", "link");

// Add link labels to SVG
const linkLabels = svg.append("g")
    .attr("class", "linkLabels")
    .selectAll("text")
    .data(data.links)
    .enter()
    .append("text")
    .attr("class", "linkLabel")
    .text((d) => d.symbol);

// Add nodes to SVG
const node = svg.append("g")
    .attr("class", "nodes")
    .selectAll("g")
    .data(data.nodes)
    .enter()
    .append("g")
    .attr("class", "node");

node.append("circle")
    .attr("r", 10)
    .classed("accepting", (d) => d.isAccepting);

node.append("text")
    .text((d) => d.id);

// Update node and link positions
const width = 500;
const height = 500;
const circleRadius = 25;

const simulation = d3.forceSimulation(data.nodes)
    .force("link", d3.forceLink(data.links).id((d) => d.id))
    .force("charge", d3.forceManyBody().strength(-100))
    .force("center", d3.forceCenter(width / 2, height / 2));

simulation.on("tick", () => {
    link.attr("x1", (d) => d.source.x)
        .attr("y1", (d) => d.source.y)
        .attr("x2", (d) => d.target.x)
        .attr("y2", (d) => d.target.y);

    linkLabels.attr("x", (d) => (d.source.x + d.target.x) / 2)
        .attr("y", (d) => (d.source.y + d.target.y) / 2);

    node.attr("transform", (d) => `translate(${d.x}, ${d.y})`);
});