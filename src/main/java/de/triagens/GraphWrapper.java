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
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.index.lucene.ValueContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;

@SuppressWarnings("deprecation")
public class GraphWrapper {
	GraphDatabaseService graphdb;
	Transaction tx;
	PathFinder<WeightedPath> dijkstraPathFinder;
	Map<String,Long> vertex_mapper;
	ArrayList<Node[]> test_cases;
	Index<Node> name_index;
	Index<Node> age_index;
	Index<Node> bio_index;
	
	public GraphWrapper(String dbname) {
		graphdb = new EmbeddedGraphDatabase(dbname);
		tx = graphdb.beginTx();
		vertex_mapper = new HashMap<String,Long>();
		test_cases = new ArrayList<Node[]>();
		
		RelationshipExpander expander = Traversal.expanderForTypes(MyRelationshipType.REL, Direction.BOTH );
		CostEvaluator<Double> costEvaluator = CommonEvaluators.doubleCostEvaluator( "cost" );
		dijkstraPathFinder = GraphAlgoFactory.dijkstra( expander, costEvaluator );
		
		IndexManager index = graphdb.index();
		name_index = index.forNodes("name");
		age_index = index.forNodes("age");
		bio_index = index.forNodes("bio");
	}
	
	public Node addVertex(String name) {
		Node vertex = graphdb.createNode();
		vertex_mapper.put(name, vertex.getId());
		vertex.setProperty("name", name);
		return vertex;
	}
	
	public Node addVertex(String id, String name, Integer age, String bio) {
		Node vertex = graphdb.createNode();
		vertex_mapper.put(id, vertex.getId());
		
		vertex.setProperty("id", id);
		vertex.setProperty("name", name.substring(1, name.length() - 1));
		vertex.setProperty("age", age);
		vertex.setProperty("bio", bio.substring(1, bio.length() - 1));
		
		name_index.add(vertex, "name", vertex.getProperty("name"));
		age_index.add(vertex, "age", new ValueContext(vertex.getProperty("age")).indexNumeric());
		bio_index.add(vertex, "bio", vertex.getProperty("bio"));
		
		return vertex;
	}
	
	public Relationship addEdge(String name, String from_name, String to_name) {
		Node from = graphdb.getNodeById(vertex_mapper.get(from_name));
		Node to = graphdb.getNodeById(vertex_mapper.get(to_name));
		
		Relationship edge = from.createRelationshipTo(to, MyRelationshipType.REL);
		
		edge.setProperty("name", name);
		edge.setProperty("cost", 1);
		
		return edge;
	}
	
	public void addTest(String from_name, String to_name) {
		Node from = graphdb.getNodeById(vertex_mapper.get(from_name));
		Node to = graphdb.getNodeById(vertex_mapper.get(to_name));
		
		Node[] node_tuple = new Node[2];
		node_tuple[0] = from;
		node_tuple[1] = to;
		test_cases.add(node_tuple);
	}
	
	public void runTestsWithLogger(BufferedWriter logger) throws IOException {
		Iterator<Node[]> iterator = test_cases.iterator();
		
		while(iterator.hasNext()) {
			Node[] node_tuple = iterator.next();
			logger.append(shortestPathes(node_tuple[0], node_tuple[1]));
		}
	}
	
	public long runTestsWithTimer() {
		Iterator<Node[]> iterator = test_cases.iterator();
		
		long start_time = System.currentTimeMillis(); 
		while(iterator.hasNext()) {
			Node[] node_tuple = iterator.next();
			shortestPathes(node_tuple[0], node_tuple[1]);
		}
		long end_time = System.currentTimeMillis();
		
		return (end_time - start_time);
	}
	
	public void runEndlessTests() {
		while (true) {
			Iterator<Node[]> iterator = test_cases.iterator();
			
			while(iterator.hasNext()) {
				Node[] node_tuple = iterator.next();
				shortestPathes(node_tuple[0], node_tuple[1]);
			}
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
	
	public long runAgeQueryWithTimer(int times) {
		long start_time = System.currentTimeMillis();
		
		for (int i = 0; i < times; i += 1) {
			for (Node node : age_index.query( QueryContext.numericRange("age", 20, 30))) {
				node.getProperty("name");
			}
		}
		
		long end_time = System.currentTimeMillis();
		
		return (end_time - start_time);
	}
	
	public long runNameQueryWithTimer(int times) {
		long start_time = System.currentTimeMillis();
		
		for (int i = 0; i < times; i += 1) {
			for (Node node : name_index.get( "name", "John Doe" )) {
				node.getProperty("name");
			}
		}
		
		long end_time = System.currentTimeMillis();
		
		return (end_time - start_time);
	}
	
	public void runNameQueryAsInfiniteLoop() {
		while(true) {
			for (Node node : name_index.get( "name", "John Doe" )) {
				node.getProperty("name");
			}
		}
	}
	
	public long runBioQueryWithTimer(int times) {
		long start_time = System.currentTimeMillis();
		
		for (int i = 0; i < times; i += 1) {
			for (Node node : bio_index.query("bio:[\"Qui \" TO \"Quia\"]")) {
				node.getProperty("name");
			}
		}
		
		long end_time = System.currentTimeMillis();
		
		return (end_time - start_time);
	}
	
	public void close() {
		tx.finish();
		graphdb.shutdown();
	}

}
