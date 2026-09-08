USE car_travel_booking;

INSERT INTO users (name, email, password, phone, role) 
VALUES ('kavin', 'kavinmuthu84@gmail.com', 'kavinhari@03', '+918072007218', 'role_admin')
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO cars (name, brand, model_year, seating_capacity, fuel_type, transmission, price_per_km, image_url, is_available) VALUES
('Glanza', 'Toyota', 2023, 4, 'petrol', 'manual', 12.00, 'https://ackodrive-prod.ackoassets.com/image/toyota/glanza/cafe-white/images_cdn/small/Transparent.webp', TRUE),
('Innova Crysta', 'Toyota', 2023, 7, 'diesel', 'manual', 18.00, 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=600&q=80', TRUE),
('Swift Dzire', 'Maruti Suzuki', 2022, 4, 'diesel', 'manual', 11.50, 'https://images.unsplash.com/photo-1590362891991-f776e747a588?w=600&q=80', TRUE),
('Ertiga', 'Maruti Suzuki', 2023, 6, 'cng', 'manual', 14.00, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=600&q=80', TRUE);

INSERT INTO destinations (from_city, to_city, distance_km, estimated_duration, image_url, description) VALUES
('Coimbatore', 'Ooty', 86.00, '2 hours 45 mins', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=600&q=80', 'Queen of Hill Stations in the Nilgiri hills.'),
('Coimbatore', 'Munnar', 160.00, '4 hours 30 mins', 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600&q=80', 'Scenic rolling tea plantations and mist.'),
('Chennai', 'Pondicherry', 152.00, '3 hours 15 mins', 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&q=80', 'French quarters and serene coastline.');