package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.dtos.Trade;
import com.poolstore.quickclean.exceptions.NotFoundException;
import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.models.Pokemon;
import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.MessageService;
import com.poolstore.quickclean.services.PokemonService;
import com.poolstore.quickclean.services.TimerService;
import com.poolstore.quickclean.services.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"user"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class UserController {

    private final EntityManager entityManager;
    private final UserService userService;
    private final PokemonService pokemonService;
    private final MessageService messageService;
    private final TimerService timerService;

    public UserController(EntityManager entityManager, UserService userService, PokemonService pokemonService, MessageService messageService, TimerService timerService) {
        this.entityManager = entityManager;
        this.userService = userService;
        this.pokemonService = pokemonService;
        this.messageService = messageService;
        this.timerService = timerService;
    }

        @PostMapping({"/new"})
        @Transactional
        public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest){
         User createUser = new User();

         createUser.setFirstName(registerRequest.getFirstName());
         createUser.setLastName(registerRequest.getLastName());
         createUser.setEmail(registerRequest.getEmail());
         createUser.setBirthDate(registerRequest.getBirthDate());
         createUser.setPassword(registerRequest.getPassword());
         createUser.setUsername(registerRequest.getUsername());
         createUser.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR82DN9JU-hbIhhkPR-AX8KiYzA4fBMVwjLAG82fz7GLg&s");
         //each user starts with 20 pokeBalls
         createUser.setPokeBalls(20);

         //creating a timer minus one day on new registration so user can collect poke ball on client side on init creation
            Timer timer = new Timer();
            LocalDateTime localDate = LocalDateTime.now().minusDays(1);
            timer.setPrevDate(localDate);
            User savedUser = userService.save(createUser);
            timer.setUser(createUser);
            timerService.saveTimer(timer);

            //Adding a welcome message when creating a new user
            Message message = new Message();
            message.setText("Welcome to our Pokemon Trading App!" +
                    " We're thrilled to have you join our community of trainers and collectors." +
                    " Here, you can trade, message, and collect your favorite Pokemon with fellow enthusiasts" +
                    " from all over the world. Below you will find an example of the trading system. Thank you for joining us and we look forward to seeing" +
                    " your collection grow!");
            message.setUserPokemon("Umbreon Example");
            message.setTradePokemon("Vileplume Example");
            message.setUserPokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS--yrrnAfZUHMSYNkzIbskqJNBn7weqJOqoSPsAGQgJAYJFzghSvnBWTmPBIiBjTThCtE&usqp=CAU");
            message.setTradePokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMWK3q3UDNpy3aK7TvWBfbzzgGrEzRh7LiJQ&usqp=CAU");
            message.setTraded(true);
            message.setCurrentUsername("The Pokemon Trading Team");
            message.setUser(createUser);
            messageService.save(message);

         return ResponseEntity.ok(savedUser);
        }

        @PostMapping({"/login"})
        public ResponseEntity<User> userLogin(@RequestBody LoginRequest loginRequest){
            Optional<User> user = userService.findByCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(user.get());
        }

        @PostMapping({"/checkCredentials"})
        @Transactional
        public ResponseEntity<Boolean> checkCredentials(@RequestBody LoginRequest loginRequest){
        //if user does not exist return false
        Optional<User> optUser = userService.findByCredentials(loginRequest.getUsername());
        if(optUser.isEmpty()){
            return ResponseEntity.ok(false);
        }
        //if passwords dont match return false
        User userExist = optUser.get();
        if(!userExist.getPassword().equals(loginRequest.getPassword())){
            return ResponseEntity.ok(false);
        }
        //user and password are a match
        return ResponseEntity.ok(true);
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

            return ResponseEntity.ok(user1);
        }

        @GetMapping({"/{username}"})
        public ResponseEntity<User> getUserByUsername(@PathVariable String username){
            Optional<User> user = userService.findByCredentials(username);

            if(user.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            User user1 = user.get();

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

            return ResponseEntity.ok(user);

        }

        @DeleteMapping({"/deleteMessage"})
        public ResponseEntity<Boolean> deleteMessage(@RequestBody Message sentMessage){
            Optional<Message> optMessage = messageService.findMessageById(sentMessage.getId());

            if(optMessage.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Message deleteMessage = optMessage.get();
            messageService.deleteMessageById(deleteMessage);

            return ResponseEntity.ok(true);
        }

        //This may be more suitable for Request params, possibly refactor in the future
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
    public ResponseEntity<List<Pokemon>> tradePokemon(@RequestBody Trade trade){

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

        if(trade1 && trade2){
            return ResponseEntity.ok(tradeUser2Pokemon);
        }else{
            return ResponseEntity.badRequest().build();
        }
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

    @GetMapping({"/{email}/checkEmail"})
    @Transactional
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email){
        boolean emailExists  = userService.getUsers().stream()
                .anyMatch(user -> user.getEmail().equals(email));

        return ResponseEntity.ok(emailExists);
    }

    @GetMapping({"/{username}/checkUsername"})
    @Transactional
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username){
        boolean usernameExists = userService.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        return ResponseEntity.ok(usernameExists);
    }
}
