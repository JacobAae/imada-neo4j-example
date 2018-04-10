package net.grydeske

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Label
import org.neo4j.graphdb.RelationshipType
import org.neo4j.graphdb.Result
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.Node

class SampleApplication {

    void runApplication() {

        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory()
        GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(
            new File("data/movies"))

        Scanner sc = new Scanner(System.in)

        printHeader("What do you want to do?")

        String line = sc.nextLine()
        while( line != 'exit' ) {
            switch (line) {
                case 'demo':
                    demo(graphDb)
                    break
                case 'load':

                    break
                default:
                    println "Unknown input: ${line}"
            }
            line = sc.nextLine()
        }

    }

    void demo(GraphDatabaseService graphDb) {
        graphDb.beginTx()
        Node car = graphDb.createNode(Label.label("Car"));
        car.setProperty("make", "tesla");
        car.setProperty("model", "model3");

        Node owner = graphDb.createNode(Label.label("Person"));
        owner.setProperty("firstName", "baeldung");
        owner.setProperty("lastName", "baeldung");

        owner.createRelationshipTo(car, RelationshipType.withName("owner"))

        Result result = graphDb.execute(
          "MATCH (c:Car) <-[owner]- (p:Person) " +
          "WHERE c.make = 'tesla'" +
          "RETURN p.firstName, p.lastName")
        printResult(result)

        Result result2 = graphDb.execute(
          "CREATE (baeldung:Company {name:\"Baeldung\"}) " +
          "-[:owns]-> (tesla:Car {make: 'tesla', model: 'modelX'})" +
          "RETURN baeldung, tesla");
        printResult(result2)

        Result result3 = graphDb.execute(
          "MATCH (company:Company)-[:owns]-> (car:Car)" +
          "WHERE car.make='tesla' and car.model='modelX'" +
          "RETURN company.name");
        printResult(result3)

    }

    void printResult(Result result) {
        result.each {
            println "$it"
            println "${it.dump()}"
        }
    }

    void printHeader(String text) {
        println ""
        println "*"*80
        println text.center(80)
        println "*"*80
    }
}
