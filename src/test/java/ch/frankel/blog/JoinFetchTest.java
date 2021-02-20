package ch.frankel.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.JoinType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoinFetchTest {

    private EntityManagerFactoryFactory factory;

    @BeforeEach
    protected void setUp() {
        factory = new EntityManagerFactoryFactory();
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_when_join_fetched_with_jpql() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var query = anotherManager.createQuery("SELECT c FROM Customer c JOIN FETCH c.orders o WHERE c.id = :id")
                .setParameter("id", id);
        var customer = (Customer) query.getSingleResult();
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_when_join_fetched_with_criteria() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var builder = anotherManager.getCriteriaBuilder();
        var criteria = builder.createQuery(Customer.class);
        var root = criteria.from(Customer.class);
        root.fetch("orders", JoinType.LEFT);
        criteria.where(builder.equal(root.get("id"), id));
        var customer = anotherManager.createQuery(criteria).getSingleResult();
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }
}