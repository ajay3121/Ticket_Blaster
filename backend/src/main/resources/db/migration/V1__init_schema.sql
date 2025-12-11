-- 1. The Inventory
CREATE TABLE theaters (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE screens (
    id UUID PRIMARY KEY,
    theater_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_screen_theater FOREIGN KEY (theater_id) REFERENCES theaters(id)
);

CREATE TABLE seat_types (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- Enum stored as String
    price DECIMAL(19, 2) NOT NULL
);

CREATE TABLE seats (
    id UUID PRIMARY KEY,
    screen_id UUID NOT NULL,
    seat_type_id UUID NOT NULL,
    row_num INT NOT NULL,
    col_num INT NOT NULL,
    seat_status VARCHAR(50) NOT NULL,
    version INT NOT NULL DEFAULT 0, -- Optimistic Lock
    CONSTRAINT fk_seat_screen FOREIGN KEY (screen_id) REFERENCES screens(id),
    CONSTRAINT fk_seat_type FOREIGN KEY (seat_type_id) REFERENCES seat_types(id)
);

-- Index for the "Seat Map" query (Find seats by screen)
CREATE INDEX idx_seat_screen ON seats(screen_id);

-- 2. The Transactions
CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    user_id INT NOT NULL,
    booking_status VARCHAR(50) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL
);

CREATE TABLE booking_seats (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL,
    seat_id UUID NOT NULL,
    price_snapshot DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_bs_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
    CONSTRAINT fk_bs_seat FOREIGN KEY (seat_id) REFERENCES seats(id)
);