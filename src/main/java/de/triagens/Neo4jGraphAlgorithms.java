package de.triagens;

import java.util.Iterator;

import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipExpander;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;

public abstract class Neo4jGraphAlgorithms {

	public enum MyDijkstraTypes implements RelationshipType
    {
        REL
    }
	
	public static void main(String[] args) {
		importData();
	}
	
	public static void importData() {
		GraphDatabaseService graphdb = new EmbeddedGraphDatabase("/tmp/neo4j-graph-algorithms");
		
		Transaction tx = graphdb.beginTx();
		
		Node node_1 = graphdb.createNode();
		Node node_2 = graphdb.createNode();
		Node node_3 = graphdb.createNode();
		
		node_1.setProperty("name", "node_1");
		node_2.setProperty("name", "node_2");
		node_3.setProperty("name", "node_3");
		
		Relationship edge_1 = node_1.createRelationshipTo(node_2, MyDijkstraTypes.REL);
		edge_1.setProperty("cost", 1);
		Relationship edge_2 = node_2.createRelationshipTo(node_3, MyDijkstraTypes.REL);
		edge_2.setProperty("cost", 1);
		
		RelationshipExpander expander = Traversal.expanderForTypes(MyDijkstraTypes.REL, Direction.BOTH );
		CostEvaluator<Double> costEvaluator = CommonEvaluators.doubleCostEvaluator( "cost" );
		PathFinder<WeightedPath> dijkstraPathFinder = GraphAlgoFactory.dijkstra( expander, costEvaluator );
		
		Iterator<WeightedPath> iterator = dijkstraPathFinder.findAllPaths(node_1, node_3).iterator();
		
		while(iterator.hasNext()) {
			WeightedPath a = iterator.next();
			System.out.println(a);
		}
		
		tx.finish();
		graphdb.shutdown();
	}

}
