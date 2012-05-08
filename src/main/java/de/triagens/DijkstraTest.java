package de.triagens;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	Map<String,Long> node_mapper;
	ArrayList<Node[]> test_cases;
	
	public DijkstraTest(String dbname) {
		graphdb = new EmbeddedGraphDatabase(dbname);
		tx = graphdb.beginTx();
		node_mapper = new HashMap<String,Long>();
		test_cases = new ArrayList<Node[]>();
		
		RelationshipExpander expander = Traversal.expanderForTypes(MyRelationshipType.REL, Direction.BOTH );
		CostEvaluator<Double> costEvaluator = CommonEvaluators.doubleCostEvaluator( "cost" );
		dijkstraPathFinder = GraphAlgoFactory.dijkstra( expander, costEvaluator );
	}
	
	public Node addNode(String name) {
		Node node = graphdb.createNode();
		node_mapper.put(name, node.getId());
		
		node.setProperty("name", name);
		return node;
	}
	
	public Relationship addEdge(String name, String from_name, String to_name) {
		Node from = graphdb.getNodeById(node_mapper.get(from_name));
		Node to = graphdb.getNodeById(node_mapper.get(to_name));
		
		Relationship edge = from.createRelationshipTo(to, MyRelationshipType.REL);
		edge.setProperty("name", name);
		edge.setProperty("cost", 1);
		
		return edge;
	}
	
	public void addTest(String from_name, String to_name) {
		Node from = graphdb.getNodeById(node_mapper.get(from_name));
		Node to = graphdb.getNodeById(node_mapper.get(to_name));
		
		Node[] node_tuple = new Node[2];
		node_tuple[0] = from;
		node_tuple[1] = to;
		test_cases.add(node_tuple);
	}
	
	public void runTests(BufferedWriter logger) throws IOException {
		Iterator<Node[]> iterator = test_cases.iterator();
		
		while(iterator.hasNext()) {
			Node[] node_tuple = iterator.next();
			logger.append(shortestPathes(node_tuple[0], node_tuple[1]));
		}
	}
	
	private String shortestPathes(Node from, Node to) {
		Iterator<WeightedPath> iterator = dijkstraPathFinder.findAllPaths(from, to).iterator();
		
		String my_pathes = "";
		
		while(iterator.hasNext()) {
			Iterator<Node> my_path = iterator.next().nodes().iterator();
			
			while(my_path.hasNext()) {
				my_pathes = my_pathes.concat(my_path.next().getProperty("name").toString());
				if (my_path.hasNext()) {
					my_pathes = my_pathes.concat(",");
				}
			}
			
			my_pathes = my_pathes.concat("\n");
		}
		
		return my_pathes;
	}
	
	public void close() {
		tx.finish();
		graphdb.shutdown();
	}
}
