package dev.ajay.ticket_blaster.ticketblaster_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TicketblasterBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
