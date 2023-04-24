package com.poolstore.quickclean.bootstrap;


import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.Product;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.PokemonRepository;
import com.poolstore.quickclean.repository.ProductRepository;
import com.poolstore.quickclean.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoad implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PokemonRepository pokemonRepository;


    public DataLoad(ProductRepository productRepository, UserRepository userRepository, PokemonRepository pokemonRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pokemonRepository = pokemonRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        getPokemon();
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
        pump.setId(1L);
        pump.setName("Hayward Super-pump 1.5HP");
        pump.setPrice(599.99d);
        pump.setQuantity(10);
        pump.setStock(true);

        productRepository.save(pump);

    }
    @Transactional
    private void getUsers(){

        User john = new User();
        john.setFirstName("John");
        john.setLastName("Michaels");
        john.setAdmin(false);
        john.setId(1L);
        john.setEmail("John@example.com");
        john.setPassword("password");
        john.setUsername("john17");
        john.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRT6NaU4Ur8LgGKKc8KqSQoX1KhcMnKgxVYQA&usqp=CAU");
        userRepository.save(john);
        Pokemon charizard = new Pokemon();
        charizard.setName("charizard");
        charizard.setUser(john);
        pokemonRepository.save(charizard);

        User mike = new User();
        mike.setFirstName("Mike");
        mike.setLastName("Myers");
        mike.setAdmin(true);
        mike.setId(2L);
        mike.setEmail("Mike@example.com");
        mike.setPassword("password");
        mike.setUsername("mike17");
        mike.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsE7YhmsSgX1GkJCoCzOjbx7n2Je6w7dlwew&usqp=CAU");
        userRepository.save(mike);
        Pokemon poke = new Pokemon();
        poke.setName("butterfree");
        poke.setUser(mike);
        pokemonRepository.save(poke);
    }
    @Transactional
    public void getPokemon(){

        Pokemon pickachu = new Pokemon();
        pickachu.setName("Pickachu");
        pickachu.setId(1L);

        pokemonRepository.save(pickachu);

    }
}
