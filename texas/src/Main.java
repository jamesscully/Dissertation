import cards.*;
import enums.Suit;
import enums.Face;

public class Main {

    public static void main(String[] args) {

        Deck deck = Deck.getInstance();

        Card a = new Card(Suit.CLUBS    , Face.ACE  );
        Card b = new Card(Suit.DIAMONDS , Face.TEN  );
        Card c = new Card(Suit.SPADES   , Face.TWO  );
        Card d = new Card(Suit.HEARTS   , Face.FIVE );
        Card e = new Card(Suit.CLUBS    , Face.FOUR );

        TexasHand h = new TexasHand(a, b, c, d, e);


    }

}
