package dev.ajay.ticket_blaster.ticketblaster_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TicketblasterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketblasterBackendApplication.class, args);
	}

}
