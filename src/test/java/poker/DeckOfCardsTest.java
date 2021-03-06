package poker;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

/** Created by Andy on 29/04/2017 **/

/**
 * Test class for DeckOfCards.
 * setUp() creates two DeckOfCards objects.
 * The @Before annotation is required to force setUp() to be executed before the tests themselves, ensuring both test
 * deck objects are already made and ready to have each of their returning methods called.
 * This verifies that the dealNext() method is working correctly.
 */
public class DeckOfCardsTest extends TestCase{

    private DeckOfCards test_deck_1;
    private DeckOfCards test_deck_2;

    @Before
    public void setUp(){
    	test_deck_1 = new DeckOfCards();
    	test_deck_2 = new DeckOfCards();
    }

    @After
    public void tearDown() {
        test_deck_1 = null;
        test_deck_2 = null;
    }

    /**Test method for dealNext(). Should return PlayingCards for the first 52 calls of dealNext() and null thereafter.*/
    public void testDealNext(){
    	for(int i = 0; i<DeckOfCards.CARDS_IN_DECK; i++){
            assertNotNull("DeckOfCards dealNext() is returning an unexpected null variable.", test_deck_1.dealNext());
            assertNotNull("DeckOfCards dealNext() is returning an unexpected null variable.", test_deck_2.dealNext());
    	}
    	assertNull("Empty DeckOfCards dealNext() is not returning a null variable.", test_deck_1.dealNext());
        assertNull("Empty DeckOfCards dealNext() is not returning a null variable.", test_deck_2.dealNext());
    }

}
