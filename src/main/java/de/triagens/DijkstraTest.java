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
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;

public class DijkstraTest {
	GraphDatabaseService graphdb;
	Transaction tx;
	PathFinder<WeightedPath> dijkstraPathFinder;
	
	public DijkstraTest(String dbname) {
		graphdb = new EmbeddedGraphDatabase(dbname);
		tx = graphdb.beginTx();
		
		RelationshipExpander expander = Traversal.expanderForTypes(MyRelationshipType.REL, Direction.BOTH );
		CostEvaluator<Double> costEvaluator = CommonEvaluators.doubleCostEvaluator( "cost" );
		dijkstraPathFinder = GraphAlgoFactory.dijkstra( expander, costEvaluator );
	}
	
	public Node addNode(String name) {
		Node node = graphdb.createNode();
		node.setProperty("name", name);
		return node;
	}
	
	public Relationship addEdge(Node from, Node to) {
		Relationship edge = from.createRelationshipTo(to, MyRelationshipType.REL);
		edge.setProperty("cost", 1);
		
		return edge;
	}
	
	public String shortestPathesFor(Node from, Node to) {
		Iterator<WeightedPath> iterator = dijkstraPathFinder.findAllPaths(from, to).iterator();
		
		String my_pathes = "";
		
		while(iterator.hasNext()) {
			WeightedPath a = iterator.next();
			my_pathes = my_pathes.concat(a.toString());
			my_pathes = my_pathes.concat("\n");
		}
		
		return my_pathes;
	}
	
	public void close() {
		tx.finish();
		graphdb.shutdown();
	}
}
