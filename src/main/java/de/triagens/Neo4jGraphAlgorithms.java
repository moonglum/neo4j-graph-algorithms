package de.triagens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		startDijkstra();
	}
	
	public static void startDijkstra() {
		DijkstraTest dijkstra_test = new DijkstraTest("/tmp/neo4j-graph-algorithms");
		
		new DataImporter("/Users/moonglum/Desktop/test-data", dijkstra_test);
		
		try {
			FileWriter fstream = new FileWriter("/Users/moonglum/Desktop/test-data/output.log");
			BufferedWriter log = new BufferedWriter(fstream);
			
			dijkstra_test.runTests(log);
			
			log.close();
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}
		
		dijkstra_test.close();
	}
}
