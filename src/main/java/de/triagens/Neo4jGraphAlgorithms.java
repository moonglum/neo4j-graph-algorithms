package de.triagens;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.impl.shortestpath.Dijkstra;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipExpander;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public abstract class Neo4jGraphAlgorithms {

	public static void main(String[] args) {
		
	}
	
	public static void importData() {
		GraphDatabaseService graphdb = new EmbeddedGraphDatabase("/tmp/neo4j-graph-algorithms");
		
		Node node_1 = graphdb.createNode();
		Node node_2 = graphdb.createNode();
		Node node_3 = graphdb.createNode();
		
		node_1.setProperty("name", "node_1");
		node_2.setProperty("name", "node_2");
		node_3.setProperty("name", "node_3");
		
		//Relationship edge_1 = 
		node_1.createRelationshipTo(node_2, null);
		//Relationship edge_2 = 
		node_2.createRelationshipTo(node_3, null);
		
		//Dijkstra shortest_path = new Dijkstra();
		Dijkstra dijkstra = GraphAlgoFactory.dijkstra(null, null);
	}

}
