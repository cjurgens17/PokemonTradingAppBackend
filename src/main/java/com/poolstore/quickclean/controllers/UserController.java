package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.dtos.Trade;
import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.MessageService;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"user"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private EntityManager entityManager;

    private final UserService userService;
    private final PokemonService pokemonService;
    private final MessageService messageService;

    public UserController(UserService userService, PokemonService pokemonService, MessageService messageService) {
        this.userService = userService;
        this.pokemonService = pokemonService;
        this.messageService = messageService;
    }

        @PostMapping({"/new"})
        public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest){
         System.out.println("In the create User controller method");
         User createUser = new User();

         createUser.setFirstName(registerRequest.getFirstName());
         createUser.setLastName(registerRequest.getLastName());
         createUser.setPhoneNumber(registerRequest.getPhoneNumber());
         createUser.setEmail(registerRequest.getEmail());
         createUser.setBirthDate(registerRequest.getBirthDate());
         createUser.setPassword(registerRequest.getPassword());
         createUser.setUsername(registerRequest.getUsername());
         //each user starts with 10 pokeBalls
         createUser.setPokeBalls(10);

         userService.save(createUser);
         return ResponseEntity.ok().build();
        }

        @PostMapping({"/login"})
        public ResponseEntity<User> userLogin(@RequestBody LoginRequest loginRequest){
            Optional<User> user = userService.findByCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            System.out.println("Username: " + loginRequest.getUsername());
            System.out.println("password: " + loginRequest.getPassword());

            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(user.get());
        }

        @GetMapping({"/{id}/userPokemon"})
        public ResponseEntity<List<Pokemon>> getUserPokemon(@PathVariable Long id){

        Optional<User> user = userService.findUserById(id);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user1 = user.get();

        List<Pokemon> pokemon = pokemonService.getUserPokemon(user1);

        return ResponseEntity.ok(pokemon);
        }

        @GetMapping({"/{id}/userMessages"})
        public ResponseEntity<List<Message>> getUserMessages(@PathVariable Long id){

        Optional<User> user = userService.findUserById(id);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User user1 = user.get();

        List<Message> inbox = messageService.getUserMessages(user1);

        return ResponseEntity.ok(inbox);

        }

        @PostMapping({"/{username}/updateIsTraded"})
        public ResponseEntity<Message> updateIsTraded(
                @RequestBody Message message,
                @PathVariable String username
        )
        {

            Optional<User> user = userService.findByCredentials(username);
            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            User setMsgUser = user.get();

            Message updateIsTrade = new Message();
            updateIsTrade.setId(message.getId());
            updateIsTrade.setTraded(true);
            updateIsTrade.setText(message.getText());
            updateIsTrade.setCurrentUsername(message.getCurrentUsername());
            updateIsTrade.setTradePokemon(message.getTradePokemon());
            updateIsTrade.setTradePokemonImage(message.getTradePokemonImage());
            updateIsTrade.setUserPokemon(message.getUserPokemon());
            updateIsTrade.setUserPokemonImage(message.getUserPokemonImage());
            updateIsTrade.setUser(setMsgUser);
            updateIsTrade.setUsername(message.getUsername());
            messageService.save(updateIsTrade);

            return ResponseEntity.ok(updateIsTrade);
        }

        @GetMapping({"/{id}/userInfo"})
        public ResponseEntity<User> getUserInfo(@PathVariable Long id){

            Optional<User> user = userService.findUserById(id);

            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            User user1 = user.get();
            System.out.println("in get user info controller method");

            return ResponseEntity.ok(user1);
        }

        @GetMapping({"/{username}"})
        public ResponseEntity<User> getUserByUsername(@PathVariable String username){
            Optional<User> user = userService.findByCredentials(username);

            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            User user1 = user.get();
            System.out.println("In the get by Username method");

            return ResponseEntity.ok(user1);
        }

        @GetMapping({"/getAllUsers"})
        public ResponseEntity<List<User>> getAllUsers() {
            List<User> users = userService.getUsers();

            if (users.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(users);
        }

        @PostMapping({"/{username}/addMessage"})
        public ResponseEntity<User> addMessage(
                @PathVariable String username,
                @RequestBody Message message
        )
        {
            System.out.println("In add message UserController");
            Optional<User> updateUser = userService.findByCredentials(username);
            User user;
            if(updateUser.isPresent()){
               user = updateUser.get();
            }else{
                throw new NotFoundException("User does not exist");
            }

            Message newMessage = new Message();
            newMessage.setText(message.getText());
            newMessage.setUserPokemon(message.getUserPokemon());
            newMessage.setTradePokemon(message.getTradePokemon());
            newMessage.setUsername(message.getUsername());
            newMessage.setTradePokemonImage(message.getTradePokemonImage());
            newMessage.setUserPokemonImage(message.getUserPokemonImage());
            newMessage.setCurrentUsername(message.getCurrentUsername());
            System.out.println(message.isTraded());
            newMessage.setTraded(message.isTraded());
            System.out.println(newMessage.isTraded());
            newMessage.setUser(user);
            messageService.save(newMessage);

            System.out.println(newMessage);

            return ResponseEntity.ok(user);

        }

        @DeleteMapping({"/deleteMessage"})
        public ResponseEntity<Boolean> deleteMessage(@RequestBody Message sentMessage){
            System.out.println("in the delete Message service   message ID: " + sentMessage.getId());
            Optional<Message> optMessage = messageService.findMessageById(sentMessage.getId());

            if(optMessage.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Message deleteMessage = optMessage.get();
            messageService.deleteMessageById(deleteMessage);

            return ResponseEntity.ok(true);
        }

        @GetMapping({"/{username}/{currentUsername}/{userPokemon}/{tradePokemon}/checkPokemon"})
        public ResponseEntity<Boolean> checkUsersPokemon(
                @PathVariable String username,
                @PathVariable String currentUsername,
                @PathVariable String userPokemon,
                @PathVariable String tradePokemon
        )
        {
            Optional<User> user1 = userService.findByCredentials(username);
            Optional<User> user2 = userService.findByCredentials(currentUsername);

            if(user1.isEmpty() || user2.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            User tradeUser1 = user1.get();
            User tradeUser2 = user2.get();

            // gets users pokemon
            List<Pokemon> tradeUser1Pokemon = pokemonService.getUserPokemon(tradeUser1);
            List<Pokemon> tradeUser2Pokemon = pokemonService.getUserPokemon(tradeUser2);

            boolean checkUser1 = false;
            boolean checkUser2 = false;
            //checks users pokemon to see if persisted
            for(Pokemon poke: tradeUser1Pokemon){
                if(poke.getName().equals(tradePokemon)){
                    checkUser1 = true;
                    break;
                }
            }

            for(Pokemon poke: tradeUser2Pokemon){
                if(poke.getName().equals(userPokemon)){
                    checkUser2 = true;
                    break;
                }
            }


           return ResponseEntity.ok(checkUser1 && checkUser2);
    }

    @PostMapping({"/tradePokemon"})
    @Transactional
    public ResponseEntity<Boolean> tradePokemon(@RequestBody Trade trade){

        Trade dataTrade = new Trade();
        dataTrade.setUsername(trade.getUsername());
        dataTrade.setUserPokemon(trade.getUserPokemon());
        dataTrade.setCurrentUsername(trade.getCurrentUsername());
        dataTrade.setTradePokemon(trade.getTradePokemon());

        Optional<User> user1 = userService.findByCredentials(dataTrade.getUsername());
        Optional<User> user2 = userService.findByCredentials(dataTrade.getCurrentUsername());

        if(user1.isEmpty() || user2.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        User tradeUser1 = user1.get();
        User tradeUser2 = user2.get();


        boolean trade1 = false;
        boolean trade2 = false;

        List<Pokemon> tradeUser1Pokemon = tradeUser1.getUserPokemon();
        List<Pokemon> tradeUser2Pokemon = tradeUser2.getUserPokemon();

        for(Pokemon poke: tradeUser1Pokemon){
            if(poke.getName().equals(dataTrade.getTradePokemon())){
                if(!entityManager.contains(poke)){
                    poke = entityManager.merge(poke);
                }

                poke.setUser(tradeUser2);
                pokemonService.savePokemon(poke);
                trade1 = true;
                break;
            }
        }

        for(Pokemon poke: tradeUser2Pokemon){
            if(poke.getName().equals(dataTrade.getUserPokemon())){
                if(!entityManager.contains(poke)){
                    poke = entityManager.merge(poke);
                }

                poke.setUser(tradeUser1);
                pokemonService.savePokemon(poke);
                trade2 = true;
                break;
            }
        }

        return ResponseEntity.ok(trade1 && trade2);
    }
    @PostMapping({"/{id}/updateProfilePicture"})
    @Transactional
    public ResponseEntity<Boolean> updateProfilePicture(
            @PathVariable Long id,
            @RequestParam String profilePicture
    )
    {
     Optional<User> optUser = userService.findUserById(id);
     if(optUser.isEmpty()){
         return ResponseEntity.badRequest().build();
     }

     User user = optUser.get();

     user.setProfilePicture(profilePicture);
     userService.save(user);

     return ResponseEntity.ok(true);
    }
}
