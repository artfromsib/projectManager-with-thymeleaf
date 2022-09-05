CREATE TABLE IF NOT EXISTS item
(
    id BIGSERIAL PRIMARY KEY,
    name  VARCHAR(254),
    quantity INTEGER,
    price DECIMAL,
    status VARCHAR(254),
    url_image VARCHAR(254),
    variations VARCHAR(254),
    listing_id BIGSERIAL DEFAULT 0,
    section_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS track_parcel
(
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(254),
    date DATE
);

CREATE TABLE IF NOT EXISTS parcel
(
    id BIGSERIAL PRIMARY KEY,
    track_number VARCHAR(25) NOT NULL,
    last_status VARCHAR(254),
    delivered BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS "order"
(
    id BIGSERIAL PRIMARY KEY,
    date_sale  DATE,
    date_posted DATE,
    etsy_order_id BIGSERIAL,
    order_value INTEGER,
    coupon_id BIGSERIAL,
    use_coupon BOOLEAN DEFAULT FALSE,
    discount_amount DECIMAL,
    customer_id BIGSERIAL,
    delivery_cost DECIMAL,
    order_total DECIMAL,
    count_items INTEGER,
    status VARCHAR(254),
    parcel_id BIGSERIAL REFERENCES parcel,
    shipping_date TIMESTAMP
);




CREATE TABLE IF NOT EXISTS customer
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL,
    full_name VARCHAR(254),
    address VARCHAR(254),
    count_orders INTEGER
);

CREATE TABLE IF NOT EXISTS shop_purchase
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(254),
    link VARCHAR(254)
);

CREATE TABLE IF NOT EXISTS materials
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(254),
    quantity DECIMAL,
    price DECIMAL,
    storage VARCHAR(254),
    shop_purchase_id BIGSERIAL REFERENCES shop_purchase

);

CREATE TABLE IF NOT EXISTS item_material
(
    id BIGSERIAL PRIMARY KEY,
    item_id BIGSERIAL REFERENCES item,
    material_id BIGSERIAL REFERENCES materials,
    count DECIMAL
);

CREATE TABLE IF NOT EXISTS order_item
(
    id BIGSERIAL PRIMARY KEY,
    item_id BIGSERIAL REFERENCES item,
    order_id BIGSERIAL REFERENCES "order",
    count INTEGER,
    price DECIMAL,
    sum_value DECIMAL
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;

CREATE TABLE IF NOT EXISTS section
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(254)
);
















