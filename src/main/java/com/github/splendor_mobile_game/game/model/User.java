package com.github.splendor_mobile_game.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.github.splendor_mobile_game.game.enums.TokenType;

public class User {

    private final String name;

    private UUID uuid;

    private int connectionHasCode;

    private int points;

    //initialized tokens hashmap
    private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();

    //initialized purchased and reserved cards lists
    private ArrayList<Card> purchasedCards = new ArrayList<Card>();
    private ArrayList<Card> reservedCards = new ArrayList<Card>();

    public User(UUID uuid, String name, int connectionHasCode) {
        this.uuid = uuid;
        this.name = name;
        this.connectionHasCode = connectionHasCode;

        //putting every token type into the hashmap and setting its value to 0
        for (TokenType type : TokenType.values()) {
            tokens.put(type, 0);
        }
    }

    //method returning how many tokens user has
    public int getTokenCount() {
        int result = 0;

        for(Map.Entry<TokenType, Integer> set : this.tokens.entrySet()) {
            result += set.getValue();
        }

        return result;
    }

    //method for adding two tokens (still needs to be validated if there are at least 4 tokens of this type on the table)
    public void takeTwoTokens(TokenType type) throws Exception {
        if(this.getTokenCount() > 8) throw new Exception("You can't have more than 10 tokens");
        this.tokens.put(type, this.tokens.get(type) + 2);
    }

    //method for taking tokens from user
    public void putDownTokens(TokenType type, int amount) throws Exception {
        if(this.tokens.get(type) - amount < 0) throw new Exception("You can't have less than 0 tokens");
        this.tokens.put(type, this.tokens.get(type) - amount);
    }

    //method for adding three different tokens
    public void takeThreeTokens(TokenType type1, TokenType type2, TokenType type3) throws Exception {
        if(type1 == type2 || type1 == type3 || type2 == type3) throw new Exception("No three different types selected");
        this.tokens.put(type1, this.tokens.get(type1) + 1);
        this.tokens.put(type2, this.tokens.get(type2) + 1);
        this.tokens.put(type3, this.tokens.get(type3) + 1);
    }

    

    public int getConnectionHasCode() {
        return connectionHasCode;
    }

    public void setConnectionHasCode(int connectionHasCode) {
        this.connectionHasCode = connectionHasCode;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && uuid.equals(user.uuid) && connectionHasCode == user.getConnectionHasCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid, connectionHasCode);
    }
}
