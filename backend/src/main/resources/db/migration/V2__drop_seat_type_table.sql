--Drop the fk in the seats table
ALTER TABLE seats DROP CONSTRAINT IF EXISTS seats_seat_type_id_fkey;
ALTER TABLE seats DROP COLUMN seat_type_id;

DROP TABLE IF EXISTS seat_types;