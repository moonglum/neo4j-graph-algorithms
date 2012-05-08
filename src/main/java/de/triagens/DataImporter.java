package de.triagens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataImporter {
	DijkstraTest dijkstra;
	
	public DataImporter(String base_directory, DijkstraTest dijkstra) {
		String vertices_file = base_directory + "/generated_vertices.csv";
		String edges_file = base_directory + "/generated_edges.csv";
		String test_file = base_directory + "/generated_testcases.csv";
		
		this.dijkstra = dijkstra;
		
		importVertices(vertices_file);
		importEdges(edges_file);
		importTestCases(test_file);
	}
	
	public void importVertices(String vertices_file) {
		try {
			FileInputStream fstream = new FileInputStream(vertices_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				this.dijkstra.addNode(strLine);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Vertexfile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importEdges(String edges_file) {
		try {
			FileInputStream fstream = new FileInputStream(edges_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				String[] a = strLine.split(",");
				this.dijkstra.addEdge(a[0], a[1], a[2]);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Edgefile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importTestCases(String test_file) {
		try {
			FileInputStream fstream = new FileInputStream(test_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				String[] a = strLine.split(",");
				this.dijkstra.addTest(a[0], a[1]);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("TestcaseFile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
