package works.weave.socks.cart.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import works.weave.socks.cart.entities.Item;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class ITItemRepository {
    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void removeAllData() {
        itemRepository.deleteAll();
    }

    @Test
    public void testCustomerSave() {
        Item original = new Item("id", "itemId", 1, 0.99F);
        Item saved = itemRepository.save(original);

        assertEquals(1, itemRepository.count());
        assertEquals(original, saved);
    }
}
