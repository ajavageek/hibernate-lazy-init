package ch.frankel.blog;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(
    name = "orders",
    attributeNodes = { @NamedAttributeNode("orders") }
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    private Set<Order> orders;

    public Cart getCart() {
        return cart;
    }

    public Long getId() {
        return id;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new HashSet<>();
        }
        orders.add(order);
    }
}