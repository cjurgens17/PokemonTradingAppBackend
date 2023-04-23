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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"user"})
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class UserController {

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
        public ResponseEntity<User> addMessage(@PathVariable String username, @RequestBody Message message){
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
            newMessage.setUser(user);
            messageService.save(newMessage);

            System.out.println(newMessage);

            return ResponseEntity.ok().build();

        }

        @DeleteMapping({"/deleteMessage"})
        public ResponseEntity<Message> deleteMessage(@RequestBody Message sentMessage){
            System.out.println("in the delete Message service   message ID: " + sentMessage.getId());
            Optional<Message> optMessage = messageService.findMessageById(sentMessage.getId());

            if(optMessage.isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Message deleteMessage = optMessage.get();
            messageService.deleteMessageById(deleteMessage);

            return ResponseEntity.ok(deleteMessage);
        }

        @GetMapping({"/{username}/{currentUsername}/{userPokemon}/{tradePokemon}/checkPokemon"})
        public ResponseEntity<Boolean> checkUsersPokemon(@PathVariable String username, @PathVariable String currentUsername,
        @PathVariable String userPokemon, @PathVariable String tradePokemon){
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

        List<Pokemon> tradeUser1Pokemon = pokemonService.getUserPokemon(tradeUser1);
        List<Pokemon> tradeUser2Pokemon = pokemonService.getUserPokemon(tradeUser2);


        for(Pokemon poke: tradeUser1Pokemon){
            if(poke.getName().equals(dataTrade.getTradePokemon())){
                tradeUser2Pokemon.add(poke);
                tradeUser1Pokemon.remove(poke);
                tradeUser1.setUserPokemon(tradeUser1Pokemon);
                trade1 = true;
                break;
            }
        }

        //just to test
        for(Pokemon poke: tradeUser1Pokemon){
            System.out.println("user 1 Pokemon: "+poke.getName());
        }



        for(Pokemon poke: tradeUser2Pokemon){
            if(poke.getName().equals(dataTrade.getUserPokemon())){
                tradeUser1Pokemon.add(poke);
                tradeUser2Pokemon.remove(poke);
                tradeUser2.setUserPokemon(tradeUser2Pokemon);
                trade2 = true;
                break;
            }
        }

        //just to test
        for(Pokemon poke: tradeUser2Pokemon){
            System.out.println("user 2 Pokemon: "+poke.getName());
        }

        if(trade1 && trade2){
            userService.save(tradeUser1);
            userService.save(tradeUser2);
        }





        return ResponseEntity.ok(trade1 && trade2);

    }

}
