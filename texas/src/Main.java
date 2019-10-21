import cards.*;
import enums.Suit;
import enums.Face;
import evaluators.TexasEvaluator;

public class Main {

    public static void main(String[] args) {

        Deck deck = Deck.getInstance();

        Card a = new Card(Suit.CLUBS    , Face.ACE  );
        Card b = new Card(Suit.DIAMONDS , Face.SIX  );

        Card c = new Card(Suit.SPADES   , Face.TWO  );
        Card d = new Card(Suit.HEARTS   , Face.FIVE );
        Card e = new Card(Suit.CLUBS    , Face.FOUR );
        Card f = new Card(Suit.SPADES   , Face.THREE);
        Card g = new Card(Suit.HEARTS   , Face.FOUR );



        TexasHand h = new TexasHand(a, b);

        TexasHand t = new TexasHand(c, d, e, f, g);

        t.IS_TABLE = true;

        TexasEvaluator eval = new TexasEvaluator(h, t);

        eval.isStraight();




    }

}
