package de.triagens;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		String type = args[0], task = args[1], folder_name = "";
		int times = 1;
		GraphWrapper graph_wrapper = new GraphWrapper("/tmp/neo4j-graph-algorithms");
		
		if (args.length > 2) {
			folder_name = args[2];
		}
		
		if (args.length == 4) {
			times = Integer.parseInt(args[3]);
		}
		
	if (task.equals("nothing")) {
		while (true);
	} else if (task.equals("dijkstra")) {
			System.out.println("Start: Import");
			importEntireGraph(folder_name, graph_wrapper);
			System.out.println("Start: Processing");
			if (type.equals("infinite")) {
				startDijkstraAsInfiniteLoop(graph_wrapper);
			} else if (type.equals("time")) {
				startDijkstraWithTimer(graph_wrapper);
			} else if (type.equals("results")) {
				startDijkstraWithLogger(graph_wrapper, folder_name + "/neo4j-dijkstra.csv");
			} else {
				System.out.println("Abort! Unknown task type.");				
			}
		} else if (task.equals("payload")) {
			System.out.println("Start: Import");
			importVerticesWithPayload(folder_name, graph_wrapper);
			System.out.println("Start: Processing");
			if (type.equals("infinite")) {
				startPayloadAsInfiniteLoop(graph_wrapper);
			} else if (type.equals("time")) {
				startPayloadWithTimer(graph_wrapper, times);
			} else if (type.equals("results")) {
				System.out.println("Abort! Not implemented.");
				System.exit(1);
			} else {
				System.out.println("Abort! Unknown task type.");				
			}
		} else {
			System.out.println("Abort! Unknown task name.");
			System.exit(1);
		}
				
		graph_wrapper.close();
		System.out.println("Done");
	}
	
	public static void importEntireGraph(String folder_name, GraphWrapper graph_wrapper) {
		DataImporter data_importer = new DataImporter(folder_name, graph_wrapper);
		data_importer.importVertices();
		data_importer.importEdges();
		data_importer.importTestCases();
	}
	
	public static void importVerticesWithPayload(String folder_name, GraphWrapper graph_wrapper) {
		DataImporter data_importer = new DataImporter(folder_name, graph_wrapper);
		data_importer.importPayload();
	}
	
	private static void startDijkstraAsInfiniteLoop(GraphWrapper graph_wrapper) {
		graph_wrapper.runEndlessTests();
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
	
	public static void startPayloadWithTimer(GraphWrapper graph_wrapper, int times) {
		System.out.println("Age: " + graph_wrapper.runAgeQueryWithTimer(times) + " ms");
		System.out.println("Name: " + graph_wrapper.runNameQueryWithTimer(times) + " ms");
		System.out.println("Bio: " + graph_wrapper.runBioQueryWithTimer(times) + " ms");
	}
	
	public static void startPayloadAsInfiniteLoop(GraphWrapper graph_wrapper) {
		graph_wrapper.runNameQueryAsInfiniteLoop();
	}
}
