

module.exports = class PriorityQueue {
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