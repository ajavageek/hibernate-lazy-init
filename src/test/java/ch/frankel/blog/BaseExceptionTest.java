package ch.frankel.blog;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseExceptionTest {

    private EntityManagerFactoryFactory factory;

    @BeforeEach
    protected void setUp() {
        factory = new EntityManagerFactoryFactory();
    }

    @Test
    public void should_throw_lazy_initialization_exception_when_detached() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var customer = anotherManager.find(Customer.class, id);
        anotherManager.detach(customer);
        var orders = customer.getOrders();
        assertThrows(LazyInitializationException.class, orders::isEmpty);
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_when_not_detached() {
        var manager = factory.getEntityManager();
        var transaction = manager.getTransaction();
        transaction.begin();
        var newCustomer = new Customer();
        newCustomer.addOrder(new Order());
        manager.persist(newCustomer);
        transaction.commit();
        var id = newCustomer.getId();
        var anotherManager = factory.getEntityManager();
        var customer = anotherManager.find(Customer.class, id);
        var orders = customer.getOrders();
        assertEquals(1, orders.size());
    }
}