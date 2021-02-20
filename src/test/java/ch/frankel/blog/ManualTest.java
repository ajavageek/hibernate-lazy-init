package ch.frankel.blog;

import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManualTest {

    private EntityManagerFactoryFactory factory;

    @BeforeEach
    protected void setUp() {
        factory = new EntityManagerFactoryFactory();
    }

    @Test
    public void should_not_throw_lazy_initialization_exception_with_explicit_get() {
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
        var size = orders.size();
        anotherManager.detach(customer);
        assertEquals(1, size);
    }
}