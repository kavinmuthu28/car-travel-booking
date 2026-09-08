USE car_travel_booking;
SELECT b.booking_number, u.name AS customer, CONCAT(c.brand, " ", c.name) AS car, b.pickup_address, b.dropoff_address, b.total_amount, b.booking_status FROM bookings b JOIN users u ON b.user_id = u.id JOIN cars c ON b.car_id = c.id ORDER BY b.created_at DESC;
SELECT SUM(amount) AS total_revenue FROM payments WHERE payment_status = 'successful';