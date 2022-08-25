import com.ym.projectManager.Main;
import com.ym.projectManager.repository.OrderRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class OrderServiceImplIntegrationTest {


    @MockBean
    private OrderRepository orderRepository;

    /*@Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String name = "alex";
        List<Order> found = orderRepository.findAll();
        Date shipDate = new Date();
        found.get(0).setShippingDate(shipDate);

        orderRepository.saveAndFlush(found.get(0));
                assertThat(found.get(0).getShippingDate())
                .isEqualTo(shipDate);
    }
*/

}
