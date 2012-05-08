package de.triagens;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		startDijkstra();
	}
	
	public static void startDijkstra() {
		DijkstraTest dijkstra_test = new DijkstraTest("/tmp/neo4j-graph-algorithms");
		
		new DataImporter("/Users/moonglum/Desktop/test-data/vertices.csv", "/Users/moonglum/Desktop/test-data/edges.csv", dijkstra_test);
		
		System.out.println(dijkstra_test.shortestPathes("14", "0"));
		
		dijkstra_test.close();
	}
}
