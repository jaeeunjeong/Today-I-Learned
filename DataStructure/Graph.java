import java.util.LinkedList;

class Graph {
	class Node {
		int data;
		LinkedList<Node> adjacent;
		boolean marked;

		Node(int data) {
			this.data = data;
			this.marked = false;
			adjacent = new LinkedList<>();
		}
	}

	Node[] nodes;
	int[] distance;

	Graph(int size) {
		nodes = new Node[size];
		for (int i = 0; i < size; i++)
			nodes[i] = new Node(i);
	}

	void addEdge(int idx1, int idx2) {
		Node node1 = nodes[idx1];
		Node node2 = nodes[idx2];

		if (!node1.adjacent.contains(node2))
			node1.adjacent.add(node2);
		if (!node2.adjacent.contains(node1))
			node2.adjacent.add(node1);
	}

	void initMarks() {
		for (Node n : nodes)
			n.marked = false;
	}

	int search(int idx1, int idx2) {
		Node start = nodes[idx1];
		Node end = nodes[idx2];
		distance = new int[nodes.length];
		initMarks();
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Node root = queue.poll();
			if (root == end)
				return distance[end.data];
			for (Node next : root.adjacent) {
				if (next.marked)
					continue;
				next.marked = true;
				distance[next.data] = distance[root.data] + 1;
				queue.add(next);
			}
		}
		return 0;
	}
}
public class test{
	public static void main(String[] args) {
		Graph g = new Graph(4+1);
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(4, 3);
		System.out.println(g.search(3, 4));
	}
}
