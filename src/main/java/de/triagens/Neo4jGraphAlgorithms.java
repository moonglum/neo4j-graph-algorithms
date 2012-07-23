package de.triagens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		System.out.println("Start: Import");
		DijkstraTest dijkstra_test = new DijkstraTest("/tmp/neo4j-graph-algorithms");
		new DataImporter(args[1], dijkstra_test);
		
		System.out.println("Start: Processing");
		if (args[0].equals("logger")) {
			startDijkstraWithLogger(dijkstra_test, args[1] + "/neo4j-dijkstra.csv");
		} else if (args[0].equals("timer")) {
			startDijkstraWithTimer(dijkstra_test);
		}
		
		dijkstra_test.close();
	}
	
	public static void startDijkstraWithLogger(DijkstraTest dijkstra_test, String location) {
		try {
			FileWriter fstream = new FileWriter(location);
			BufferedWriter log = new BufferedWriter(fstream);
			
			dijkstra_test.runTestsWithLogger(log);
			
			log.close();
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}
	}
	
	public static void startDijkstraWithTimer(DijkstraTest dijkstra_test) {
		System.out.println(dijkstra_test.runTestsWithTimer() + " ms");
	}
	
	
}
