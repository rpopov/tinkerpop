package com.tinkerpop.gremlin.tinkergraph.structure.util.subgraph;

import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.structure.strategy.GraphStrategy;
import com.tinkerpop.gremlin.structure.strategy.StrategyWrappedGraph;
import com.tinkerpop.gremlin.structure.strategy.SubGraphGraphStrategy;
import com.tinkerpop.gremlin.structure.util.subgraph.SubGraph;
import com.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.junit.Test;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class SubGraphTest {

    @Test
    public void testVertexCriterion() throws Exception {
        Graph g = TinkerFactory.createClassic();

        final Predicate<Vertex> vertexCriterion = vertex -> (int) vertex.id() >= 3 && (int) vertex.id() <= 5;
        final Predicate<Edge> edgeCriterion = edge -> true;

        final GraphStrategy strategyToTest = new SubGraphGraphStrategy(vertexCriterion, edgeCriterion);
        final StrategyWrappedGraph sg = new StrategyWrappedGraph(g);
        sg.strategy().setGraphStrategy(strategyToTest);

        // three vertices are included in the subgraph
        assertEquals(6, g.V().count());
        assertEquals(3, sg.V().count());

        // only two edges are present, even though edges are not explicitly excluded
        // (edges require their incident vertices)
        assertEquals(6, g.E().count());
        assertEquals(2, sg.E().count());

        // from vertex

        assertEquals(2, g.v(4).outE().count());
        assertEquals(2, sg.v(4).outE().count());
        assertEquals(2, g.v(4).out().count());
        assertEquals(2, sg.v(4).out().count());

        assertEquals(1, g.v(4).inE().count());
        assertEquals(0, sg.v(4).inE().count());
        assertEquals(1, g.v(4).in().count());
        assertEquals(0, sg.v(4).in().count());

        assertEquals(3, g.v(4).bothE().count());
        assertEquals(2, sg.v(4).bothE().count());
        assertEquals(3, g.v(4).both().count());
        assertEquals(2, sg.v(4).both().count());

        // with label

        assertEquals(2, g.v(4).outE("created").count());
        assertEquals(2, sg.v(4).outE("created").count());
        assertEquals(2, g.v(4).out("created").count());
        assertEquals(2, sg.v(4).out("created").count());
        assertEquals(2, g.v(4).bothE("created").count());
        assertEquals(2, sg.v(4).bothE("created").count());
        assertEquals(2, g.v(4).both("created").count());
        assertEquals(2, sg.v(4).both("created").count());

        assertEquals(1, g.v(4).inE("knows").count());
        assertEquals(0, sg.v(4).inE("knows").count());
        assertEquals(1, g.v(4).in("knows").count());
        assertEquals(0, sg.v(4).in("knows").count());
        assertEquals(1, g.v(4).bothE("knows").count());
        assertEquals(0, sg.v(4).bothE("knows").count());
        assertEquals(1, g.v(4).both("knows").count());
        assertEquals(0, sg.v(4).both("knows").count());

        // with label and branch factor

        assertEquals(1, g.v(4).outE(1, "created").count());
        assertEquals(1, sg.v(4).outE(1, "created").count());
        assertEquals(1, g.v(4).out(1, "created").count());
        assertEquals(1, sg.v(4).out(1, "created").count());
        assertEquals(1, g.v(4).bothE(1, "created").count());
        assertEquals(1, sg.v(4).bothE(1, "created").count());
        assertEquals(1, g.v(4).both(1, "created").count());
        assertEquals(1, sg.v(4).both(1, "created").count());

        // from edge

        assertEquals(2, g.e(11).bothV().count());
        assertEquals(2, sg.e(11).bothV().count());

        assertEquals(2, g.e(12).bothV().count());
        assertEquals(1, sg.e(12).bothV().count());

        assertEquals(2, g.e(7).bothV().count());
        assertEquals(0, sg.e(7).bothV().count());
    }

    @Test
    public void testEdgeCriterion() throws Exception {
        Graph g = TinkerFactory.createClassic();

        final Predicate<Vertex> vertexCriterion = vertex -> true;
        final Predicate<Edge> edgeCriterion = edge -> (int) edge.id() >= 8 && (int) edge.id() <= 10;

        final GraphStrategy strategyToTest = new SubGraphGraphStrategy(vertexCriterion, edgeCriterion);
        final StrategyWrappedGraph sg = new StrategyWrappedGraph(g);
        sg.strategy().setGraphStrategy(strategyToTest);

        // all vertices are here
        assertEquals(6, g.V().count());
        assertEquals(6, sg.V().count());

        // only the given edges are included
        assertEquals(6, g.E().count());
        assertEquals(3, sg.E().count());

        assertEquals(2, g.v(1).outE("knows").count());
        assertEquals(1, sg.v(1).outE("knows").count());

        // wrapped Traversal<Vertex, Vertex> takes into account the edges it must pass through
        assertEquals(2, g.v(1).out("knows").count());
        assertEquals(1, sg.v(1).out("knows").count());
        assertEquals(2, g.v(4).out("created").count());
        assertEquals(1, sg.v(4).out("created").count());

        // from vertex

        assertEquals(2, g.v(4).outE().count());
        assertEquals(1, sg.v(4).outE().count());
        assertEquals(2, g.v(4).out().count());
        assertEquals(1, sg.v(4).out().count());

        assertEquals(1, g.v(4).inE().count());
        assertEquals(1, sg.v(4).inE().count());
        assertEquals(1, g.v(4).in().count());
        assertEquals(1, sg.v(4).in().count());

        assertEquals(3, g.v(4).bothE().count());
        assertEquals(2, sg.v(4).bothE().count());
        assertEquals(3, g.v(4).both().count());
        assertEquals(2, sg.v(4).both().count());

        // with label

        assertEquals(2, g.v(4).outE("created").count());
        assertEquals(1, sg.v(4).outE("created").count());
        assertEquals(2, g.v(4).out("created").count());
        assertEquals(1, sg.v(4).out("created").count());
        assertEquals(2, g.v(4).bothE("created").count());
        assertEquals(1, sg.v(4).bothE("created").count());
        assertEquals(2, g.v(4).both("created").count());
        assertEquals(1, sg.v(4).both("created").count());

        assertEquals(1, g.v(4).inE("knows").count());
        assertEquals(1, sg.v(4).inE("knows").count());
        assertEquals(1, g.v(4).in("knows").count());
        assertEquals(1, sg.v(4).in("knows").count());
        assertEquals(1, g.v(4).bothE("knows").count());
        assertEquals(1, sg.v(4).bothE("knows").count());
        assertEquals(1, g.v(4).both("knows").count());
        assertEquals(1, sg.v(4).both("knows").count());

        // with branch factor

        assertEquals(1, g.v(4).bothE(1).count());
        assertEquals(1, sg.v(4).bothE(1).count());
        assertEquals(1, g.v(4).both(1).count());
        assertEquals(1, sg.v(4).both(1).count());
        assertEquals(1, g.v(4).bothE(1, "knows", "created").count());
        assertEquals(1, sg.v(4).bothE(1, "knows", "created").count());
        assertEquals(1, g.v(4).both(1, "knows", "created").count());
        assertEquals(1, sg.v(4).both(1, "knows", "created").count());

        // from edge

        assertEquals(2, g.e(8).bothV().count());
        assertEquals(2, sg.e(8).bothV().count());

        assertEquals(3, g.e(8).outV().outE().count());
        assertEquals(2, sg.e(8).outV().outE().count());
    }

    @Test
    public void testMixedCriteria() throws Exception {
        Graph g = TinkerFactory.createClassic();

        final Predicate<Vertex> vertexCriterion = vertex -> (int) vertex.id() >= 3 && (int) vertex.id() <= 5;
        final Predicate<Edge> edgeCriterion = edge -> (int) edge.id() >= 9 && (int) edge.id() <= 11;

        final GraphStrategy strategyToTest = new SubGraphGraphStrategy(vertexCriterion, edgeCriterion);
        final StrategyWrappedGraph sg = new StrategyWrappedGraph(g);
        sg.strategy().setGraphStrategy(strategyToTest);

        // three vertices are included in the subgraph
        assertEquals(6, g.V().count());
        assertEquals(3, sg.V().count());

        // three edges are explicitly included, but one is missing its out-vertex due to the vertex criteria
        assertEquals(6, g.E().count());
        assertEquals(2, sg.E().count());

        // from vertex

        assertEquals(2, g.v(4).outE().count());
        assertEquals(2, sg.v(4).outE().count());
        assertEquals(2, g.v(4).out().count());
        assertEquals(2, sg.v(4).out().count());

        assertEquals(1, g.v(4).inE().count());
        assertEquals(0, sg.v(4).inE().count());
        assertEquals(1, g.v(4).in().count());
        assertEquals(0, sg.v(4).in().count());

        assertEquals(3, g.v(4).bothE().count());
        assertEquals(2, sg.v(4).bothE().count());
        assertEquals(3, g.v(4).both().count());
        assertEquals(2, sg.v(4).both().count());

        // with label

        assertEquals(2, g.v(4).outE("created").count());
        assertEquals(2, sg.v(4).outE("created").count());
        assertEquals(2, g.v(4).out("created").count());
        assertEquals(2, sg.v(4).out("created").count());
        assertEquals(2, g.v(4).bothE("created").count());
        assertEquals(2, sg.v(4).bothE("created").count());
        assertEquals(2, g.v(4).both("created").count());
        assertEquals(2, sg.v(4).both("created").count());

        assertEquals(1, g.v(4).inE("knows").count());
        assertEquals(0, sg.v(4).inE("knows").count());
        assertEquals(1, g.v(4).in("knows").count());
        assertEquals(0, sg.v(4).in("knows").count());
        assertEquals(1, g.v(4).bothE("knows").count());
        assertEquals(0, sg.v(4).bothE("knows").count());
        assertEquals(1, g.v(4).both("knows").count());
        assertEquals(0, sg.v(4).both("knows").count());

        // with branch factor

        assertEquals(1, g.v(4).bothE(1).count());
        assertEquals(1, sg.v(4).bothE(1).count());
        assertEquals(1, g.v(4).both(1).count());
        assertEquals(1, sg.v(4).both(1).count());
        assertEquals(1, g.v(4).bothE(1, "knows", "created").count());
        assertEquals(1, sg.v(4).bothE(1, "knows", "created").count());
        assertEquals(1, g.v(4).both(1, "knows", "created").count());
        assertEquals(1, sg.v(4).both(1, "knows", "created").count());

        // from edge

        assertEquals(2, g.e(9).bothV().count());
        assertEquals(1, sg.e(9).bothV().count());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExcludedVertex() throws Exception {
        Graph g = TinkerFactory.createClassic();

        final Predicate<Vertex> vertexCriterion = vertex -> (int) vertex.id() >= 3 && (int) vertex.id() <= 5;
        final Predicate<Edge> edgeCriterion = edge -> true;

        final GraphStrategy strategyToTest = new SubGraphGraphStrategy(vertexCriterion, edgeCriterion);
        final StrategyWrappedGraph sg = new StrategyWrappedGraph(g);
        sg.strategy().setGraphStrategy(strategyToTest);

        sg.e(1);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExcludedEdge() throws Exception {
        Graph g = TinkerFactory.createClassic();

        final Predicate<Vertex> vertexCriterion = vertex -> true;
        final Predicate<Edge> edgeCriterion = edge -> (int) edge.id() >= 8 && (int) edge.id() <= 10;

        final GraphStrategy strategyToTest = new SubGraphGraphStrategy(vertexCriterion, edgeCriterion);
        final StrategyWrappedGraph sg = new StrategyWrappedGraph(g);
        sg.strategy().setGraphStrategy(strategyToTest);

        sg.e(7);
    }
}
