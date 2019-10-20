import card.Card;
import card.Hand;
import card.Suit;
import card.Value;

public class Main {

    public static void main(String[] args) {

        Card a = new Card(Suit.CLUBS    , Value.ACE  );
        Card b = new Card(Suit.DIAMONDS , Value.TEN  );
        Card c = new Card(Suit.SPADES   , Value.TWO  );
        Card d = new Card(Suit.HEARTS   , Value.FIVE );
        Card e = new Card(Suit.CLUBS    , Value.FOUR );

        Hand h = new Hand(a, b, c, d, e);


    }

}
