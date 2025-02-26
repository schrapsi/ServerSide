package com.github.splendor_mobile_game.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.github.splendor_mobile_game.game.Exceptions.NotEnoughBonusPointsException;
import com.github.splendor_mobile_game.game.Exceptions.NotEnoughTokensException;
import com.github.splendor_mobile_game.game.Exceptions.SameTokenTypesException;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.utils.Log;

public class User {

    private final String name;

    private UUID uuid;

    private int connectionHasCode;

    private int points;

    //initialized tokens hashmap
    private Map<TokenType, Integer> tokens = new HashMap<TokenType, Integer>();

    //hashmap showing how many Bonuses user has
    private Map<TokenType, Integer> cardBonuses = new HashMap<TokenType, Integer>();

    //initialized purchased and reserved cards lists
    private ArrayList<Card> purchasedCards = new ArrayList<Card>();
    private ArrayList<Card> reservedCards = new ArrayList<Card>();

    //initialized nobles list
    private ArrayList<Noble> visitingNobles = new ArrayList<Noble>();
    private boolean hasPerformedAction;

    public User(UUID uuid, String name, int connectionHasCode) {
        this.uuid = uuid;
        this.name = name;
        this.connectionHasCode = connectionHasCode;

        //putting every token type into hashmaps and setting its value to 0
        for (TokenType type : TokenType.values()) {
            this.tokens.put(type, 0);
            if(type != TokenType.GOLD_JOKER) this.cardBonuses.put(type, 0);
        }
        this.hasPerformedAction = false;
    }

    //method returning how many tokens user has
    public int getTokenCount() {
        int result = 0;

        for(Map.Entry<TokenType, Integer> set : this.tokens.entrySet()) {
            result += set.getValue();
        }

        return result;
    }

    public int getTokenCount(TokenType type) {
        return this.tokens.get(type);
    }

    /** 
     * function which updates user's token amount by adding or subtracting their current amount by numbers listed in tokensChange map
     * It skips gold token type because users can't take gold tokens by themselves
     */
    public void changeTokens(Map<TokenType, Integer> tokensChange) {
        for(Map.Entry<TokenType, Integer> set : this.tokens.entrySet()) {
            // User can't take Gold tokens from table so we skip them in our this.tokens map
            if(set.getKey() == TokenType.GOLD_JOKER) continue;
            // Every token type value is added/subtracted by its corresponding value in tokensChange map
            this.tokens.put(set.getKey(), set.getValue() + tokensChange.get(set.getKey()));
        }
    }

    //method for buying cards
    public void buyCard(Card card) throws NotEnoughTokensException {

        int goldTokensUsed = 0;

        for(Map.Entry<TokenType, Integer> set : this.tokens.entrySet()) {
            if(set.getKey() == TokenType.GOLD_JOKER) continue;
            int tokensPlusBonuses = set.getValue() + this.cardBonuses.get(set.getKey());
            if(tokensPlusBonuses < card.getCost(set.getKey())) {
                int missingTokens = card.getCost(set.getKey()) - tokensPlusBonuses;
                if((this.getTokenCount(TokenType.GOLD_JOKER) - goldTokensUsed) >= missingTokens) {
                    goldTokensUsed += missingTokens;
                } else {
                    throw new NotEnoughTokensException("You don't have enough tokens to buy this card");
                }
            }
        }

        this.tokens.forEach((k,v) -> {
            if(k != TokenType.GOLD_JOKER) {
                int neededTokens = card.getCost(k) - this.cardBonuses.get(k);
                int changedValue = v > neededTokens ? v - neededTokens : 0;
                this.tokens.put(k, changedValue);
            }
        });

        this.tokens.put(TokenType.GOLD_JOKER, this.tokens.get(TokenType.GOLD_JOKER) - goldTokensUsed);

        this.purchasedCards.add(card);

        this.cardBonuses.put(card.getAdditionalToken(), this.cardBonuses.get(card.getAdditionalToken()) + 1);

        this.addPoints(card.getPoints());

    }

    public void takeNoble(Noble noble) throws NotEnoughBonusPointsException {
        for(Map.Entry<TokenType, Integer> set : this.cardBonuses.entrySet()) {
            if(set.getValue() < noble.getCost((set.getKey()))) throw new NotEnoughBonusPointsException("You don't have enough cards for this Noble to visit you");
        }

        this.visitingNobles.add(noble);
        this.addPoints(noble.getPoints());

        Log.INFO("Kupiono nobla o id " + noble.getUuid());
    }

    private void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return this.points;
    }

    public void reserveCard(Card card) {
        this.reservedCards.add(card);
        this.tokens.put(TokenType.GOLD_JOKER, this.tokens.get(TokenType.GOLD_JOKER) + 1);
    }


    public int getConnectionHashCode() {
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

    public boolean hasPerformedAction() {
        return hasPerformedAction;
    }

    public void setPerformedAction(boolean hasPerformedAction) {
        this.hasPerformedAction = hasPerformedAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uuid.equals(user.uuid) && connectionHasCode == user.getConnectionHashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, connectionHasCode);
    }
}
