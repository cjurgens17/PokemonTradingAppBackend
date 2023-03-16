package com.poolstore.quickclean.bootstrap;


import com.poolstore.quickclean.models.PokeIndex;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.Product;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.PokeIndexRepository;
import com.poolstore.quickclean.repository.ProductRepository;
import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
public class DataLoad implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PokeIndexRepository pokeIndexRepository;

    public DataLoad(ProductRepository productRepository, UserRepository userRepository, PokeIndexRepository pokeIndexRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pokeIndexRepository = pokeIndexRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        getProducts();
        getUsers();
    }


    @Transactional
    private void getProducts(){
        Product shock = new Product();
        shock.setName("Clear Again Shock");
        shock.setPrice(29.99d);
        shock.setQuantity(99);
        shock.setStock(true);
        shock.setId(1L);

        productRepository.save(shock);


        Product vacuum = new Product();
        vacuum.setId(2L);
        vacuum.setName("Sling-Vac");
        vacuum.setPrice(199.99d);
        vacuum.setStock(true);
        vacuum.setQuantity(25);

        productRepository.save(vacuum);

        Product filter = new Product();
        filter.setName("Hayward C4030 Cartridge Filter");
        filter.setId(3L);
        filter.setStock(true);
        filter.setPrice(999.99d);
        filter.setQuantity(15);

        productRepository.save(filter);

        Product pump = new Product();
        pump.setId(1l);
        pump.setName("Hayward Super-pump 1.5HP");
        pump.setPrice(599.99d);
        pump.setQuantity(10);
        pump.setStock(true);

        productRepository.save(pump);

    }
    @Transactional
    private void getUsers(){

        User john = new User();
        john.setFirstname("John");
        john.setLastName("Michaels");
        john.setAdmin(false);
        john.setId(1L);
        john.setEmail("John@example.com");
        john.setPassword("password");
        john.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRT6NaU4Ur8LgGKKc8KqSQoX1KhcMnKgxVYQA&usqp=CAU");
        PokeIndex index1 = new PokeIndex(new ArrayList<>(), new ArrayList<>());
        pokeIndexRepository.save(index1);
        index1.getPokemonList().add(new Pokemon());
        john.setPokeIndex(index1);

        userRepository.save(john);

        User mike = new User();
        mike.setFirstname("Mike");
        mike.setLastName("Myers");
        mike.setAdmin(true);
        mike.setId(2L);
        mike.setEmail("Mike@example.com");
        mike.setPassword("password");
        mike.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsE7YhmsSgX1GkJCoCzOjbx7n2Je6w7dlwew&usqp=CAU");

        userRepository.save(mike);
    }
}
