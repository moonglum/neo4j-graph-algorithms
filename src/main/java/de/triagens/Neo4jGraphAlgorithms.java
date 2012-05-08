package de.triagens;

import org.neo4j.graphdb.Node;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		startDijkstra();
	}
	
	public static void startDijkstra() {
		DijkstraTest dijkstra_test = new DijkstraTest("/tmp/neo4j-graph-algorithms");
		
		Node node_1 = dijkstra_test.addNode("node_1");
		Node node_2 = dijkstra_test.addNode("node_2");
		Node node_3 = dijkstra_test.addNode("node_3");
		
		dijkstra_test.addEdge(node_1, node_2);
		dijkstra_test.addEdge(node_2, node_3);
		
		System.out.println(dijkstra_test.shortestPathesFor(node_1, node_3));
		
		dijkstra_test.close();
	}

}
