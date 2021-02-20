package ch.frankel.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityGraphTest {

    private EntityManagerFactoryFactory factory;

    @BeforeEach
    protected void setUp() {
        factory = new EntityManagerFactoryFactory();
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_with_entity_graph_api() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var entityGraph = anotherManager.createEntityGraph(Customer.class);
        entityGraph.addAttributeNodes("orders");
        var hints = Map.<String, Object>of("javax.persistence.loadgraph", entityGraph);
        var customer = anotherManager.find(Customer.class, id, hints);
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_with_entity_graph_annotations() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var entityGraph = anotherManager.getEntityGraph("orders");
        var hints = Map.<String, Object>of("javax.persistence.loadgraph", entityGraph);
        var customer = anotherManager.find(Customer.class, id, hints);
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_with_entity_graph_jpql() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var entityGraph = anotherManager.getEntityGraph("orders");
        var query = anotherManager.createQuery("SELECT c FROM Customer c WHERE c.id = :id")
                .setParameter("id", id)
                .setHint("javax.persistence.loadgraph", entityGraph);
        var customer = (Customer) query.getSingleResult();
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_with_entity_graph_criteria() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var entityGraph = anotherManager.getEntityGraph("orders");
        var builder = anotherManager.getCriteriaBuilder();
        var criteria = builder.createQuery(Customer.class);
        var root = criteria.from(Customer.class);
        criteria.where(builder.equal(root.get("id"), id));
        var customer = anotherManager
                .createQuery(criteria)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getSingleResult();
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }
}