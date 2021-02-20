package ch.frankel.blog;

import javax.persistence.*;

@Entity
@Table(name="\"ORDER\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}