package de.triagens;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataImporter {
	GraphWrapper graph_wrapper;
	String vertices_file;
	String edges_file;
	String test_file;
	
	public DataImporter(String base_directory, GraphWrapper graph_wrapper) {
		this.vertices_file = base_directory + "/generated_vertices.csv";
		this.edges_file = base_directory + "/generated_edges.csv";
		this.test_file = base_directory + "/generated_testcases.csv";
		this.graph_wrapper = graph_wrapper;
	}
	
	public void importVertices() {
		try {
			FileInputStream fstream = new FileInputStream(vertices_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				this.graph_wrapper.addVertexWithName(strLine);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Vertexfile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importEdges() {
		try {
			FileInputStream fstream = new FileInputStream(edges_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				String[] a = strLine.split(",");
				this.graph_wrapper.addEdge(a[0], a[1], a[2]);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Edgefile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importTestCases() {
		try {
			FileInputStream fstream = new FileInputStream(test_file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			
			br.readLine(); //Skip the first line
			
			while ((strLine = br.readLine()) != null) {
				String[] a = strLine.split(",");
				this.graph_wrapper.addTest(a[0], a[1]);
			}

			fstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("TestcaseFile not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
