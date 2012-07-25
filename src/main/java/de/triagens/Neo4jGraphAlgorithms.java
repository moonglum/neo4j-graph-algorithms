package de.triagens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		String task_name = args[0];
		String folder_name = args[1];
		
		System.out.println("Start: Import");
		GraphWrapper graph_wrapper = new GraphWrapper("/tmp/neo4j-graph-algorithms");
		DataImporter data_importer = new DataImporter(folder_name, graph_wrapper);
		data_importer.importVertices();
		data_importer.importEdges();
		data_importer.importTestCases();
		
		System.out.println("Start: Processing");
		if (task_name.equals("logger")) {
			startDijkstraWithLogger(graph_wrapper, folder_name + "/neo4j-dijkstra.csv");
		} else if (task_name.equals("timer")) {
			startDijkstraWithTimer(graph_wrapper);
		}
		
		graph_wrapper.close();
	}
	
	public static void startDijkstraWithLogger(GraphWrapper graph_wrapper, String location) {
		try {
			FileWriter fstream = new FileWriter(location);
			BufferedWriter log = new BufferedWriter(fstream);
			
			graph_wrapper.runTestsWithLogger(log);
			
			log.close();
		} catch (IOException e) {
			System.out.println("Could not write to file");
		}
	}
	
	public static void startDijkstraWithTimer(GraphWrapper graph_wrapper) {
		System.out.println(graph_wrapper.runTestsWithTimer() + " ms");
	}
	
	
}
