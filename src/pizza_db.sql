-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Apr 02, 2024 at 06:16 PM
-- Server version: 5.7.39-log
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pizza_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `bonus_coins` int(11) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_activated` tinyint(1) DEFAULT '0',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pwd` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `restaurant_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `accounts_addresses`
--

CREATE TABLE `accounts_addresses` (
  `account_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `accounts_roles`
--

CREATE TABLE `accounts_roles` (
  `account_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `id` int(11) NOT NULL,
  `apartment` int(11) DEFAULT NULL,
  `building` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `door_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `entrance` int(11) DEFAULT NULL,
  `floor` int(11) DEFAULT NULL,
  `has_intercom` bit(1) DEFAULT NULL,
  `street` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `restaurant_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`id`, `apartment`, `building`, `comment`, `door_code`, `entrance`, `floor`, `has_intercom`, `street`, `restaurant`) VALUES
(1, NULL, '21', NULL, NULL, 1, 1, b'1', 'K. Marksa', 1),
(2, NULL, '4', '', '', 7, 5, b'0', 'Мележа', NULL),
(3, 107, '22', '', '', 3, 7, b'1', 'Лукьяновича', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `feedbacks`
--

CREATE TABLE `feedbacks` (
  `id` int(11) NOT NULL,
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `pizza_size` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ingredients`
--

INSERT INTO `ingredients` (`id`, `name`, `price`, `pizza_size`) VALUES
(1, 'Thin dough', 4.25, 'S'),
(2, 'Thin dough', 5.2, 'M'),
(3, 'Thin dough', 6.4, 'L'),
(4, 'Red sauce', 0.15, 'S'),
(5, 'Red sauce', 0.42, 'M'),
(6, 'Red sauce', 0.61, 'L'),
(7, 'Tomatos', 0.3, 'S'),
(8, 'Tomatos', 0.71, 'M'),
(9, 'Tomatos', 0.91, 'L'),
(10, 'Mozarella', 3.2, 'S'),
(11, 'Mozarella', 5.8, 'M'),
(12, 'Mozarella', 6.52, 'L'),
(14, 'Pepperoni', 5.2, 'S'),
(15, 'Pepperoni', 9, 'M'),
(16, 'Pepperoni', 13, 'L');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `assembled` datetime(6) DEFAULT NULL,
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `confirmed` datetime(6) DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL,
  `delivered` datetime(6) DEFAULT NULL,
  `to_deliver` tinyint(1) DEFAULT '0',
  `account_id` int(11) DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  `restaurant_id` int(11) DEFAULT NULL,
  `feedback_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders_pizzas`
--

CREATE TABLE `orders_pizzas` (
  `order_id` int(11) NOT NULL,
  `pizzza_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders_side_items`
--

CREATE TABLE `orders_side_items` (
  `order_id` int(11) NOT NULL,
  `side_item_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pizzas`
--

CREATE TABLE `pizzas` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `menu_item` bit(1) DEFAULT NULL,
  `pizza_size` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `pizzas`
--

INSERT INTO `pizzas` (`id`, `name`, `price`, `menu_item`, `pizza_size`) VALUES
(25, 'Margherita', 25.5, '1', 'S');

-- --------------------------------------------------------

--
-- Table structure for table `pizzas_ingredients`
--

CREATE TABLE `pizzas_ingredients` (
  `pizza_id` int(11) NOT NULL,
  `ingredient_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `pizzas_ingredients`
--

INSERT INTO `pizzas_ingredients` (`pizza_id`, `ingredient_id`) VALUES
(25, 1),
(25, 4),
(25, 7),
(25, 10);

-- --------------------------------------------------------

--
-- Table structure for table `restaurants`
--

CREATE TABLE `restaurants` (
  `id` int(11) NOT NULL,
  `open_hours` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`id`, `open_hours`, `address_id`) VALUES
(1, '9.00-22.00', 1),
(5, '7-20', 3);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_BOSS'),
(3, 'ROLE_COOK'),
(4, 'ROLE_DELIVERYMAN'),
(5, 'ROLE_CALL_OP'),
(6, 'ROLE_CLIENT');

-- --------------------------------------------------------

--
-- Table structure for table `side_items`
--

CREATE TABLE `side_items` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `menu_item` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `side_items`
--

INSERT INTO `side_items` (`id`, `name`, `price`, `menu_item`) VALUES
(1, 'Cola 0.5', 5, b'1'),
(2, 'Sprite 0.5', 5, b'1'),
(3, 'Fanta 0.5', 5, b'1');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfc6joutvqkc1nwwfcflco98x3` (`restaurant_id`);

--
-- Indexes for table `accounts_addresses`
--
ALTER TABLE `accounts_addresses`
  ADD PRIMARY KEY (`account_id`,`address_id`),
  ADD KEY `FKr1v5fr17f7vn8el37epwqug9u` (`address_id`);

--
-- Indexes for table `accounts_roles`
--
ALTER TABLE `accounts_roles`
  ADD PRIMARY KEY (`account_id`,`role_id`),
  ADD KEY `FKpwest19ib22ux5gk54esw9qve` (`role_id`);

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_dvfu820vikpy2nymtmu6c2wy2` (`restaurant_id`);

--
-- Indexes for table `feedbacks`
--
ALTER TABLE `feedbacks`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_hmlh8kxpicstcu589yif47wah` (`order_id`),
  ADD KEY `FKhm852dv0iikg9r35cvoajhf3g` (`account_id`);

--
-- Indexes for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_nk9tq56sqr0hebwnye6pfcpkf` (`feedback_id`),
  ADD KEY `FKagh5svlor3slbay6tq5wqor1o` (`account_id`),
  ADD KEY `FKhlglkvf5i60dv6dn397ethgpt` (`address_id`),
  ADD KEY `FK2m9qulf12xm537bku3jnrrbup` (`restaurant_id`);

--
-- Indexes for table `orders_pizzas`
--
ALTER TABLE `orders_pizzas`
  ADD PRIMARY KEY (`order_id`,`pizzza_id`),
  ADD KEY `FKk8hki9uwgslksaiwff3j4e2n7` (`pizzza_id`);

--
-- Indexes for table `orders_side_items`
--
ALTER TABLE `orders_side_items`
  ADD PRIMARY KEY (`order_id`,`side_item_id`),
  ADD KEY `FKpxlikouyk9vdpkp3d923pfl7v` (`side_item_id`);

--
-- Indexes for table `pizzas`
--
ALTER TABLE `pizzas`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pizzas_ingredients`
--
ALTER TABLE `pizzas_ingredients`
  ADD PRIMARY KEY (`pizza_id`,`ingredient_id`),
  ADD KEY `FKjwn4lvj8ajaggx1nxospim99u` (`ingredient_id`);

--
-- Indexes for table `restaurants`
--
ALTER TABLE `restaurants`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_qmof4bn52u3t3qtpqlt80ypej` (`address_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `side_items`
--
ALTER TABLE `side_items`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `feedbacks`
--
ALTER TABLE `feedbacks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pizzas`
--
ALTER TABLE `pizzas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `restaurants`
--
ALTER TABLE `restaurants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `side_items`
--
ALTER TABLE `side_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `FKfc6joutvqkc1nwwfcflco98x3` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`);

--
-- Constraints for table `accounts_addresses`
--
ALTER TABLE `accounts_addresses`
  ADD CONSTRAINT `FKr1v5fr17f7vn8el37epwqug9u` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`),
  ADD CONSTRAINT `FKs0u1rgddadnlslhlnctrwiqg1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `accounts_roles`
--
ALTER TABLE `accounts_roles`
  ADD CONSTRAINT `FKpwest19ib22ux5gk54esw9qve` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKt44duw96d6v8xrapfo4ff2up6` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `FK835rq84xrqnu6pw8voqcb4dqu` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`);

--
-- Constraints for table `feedbacks`
--
ALTER TABLE `feedbacks`
  ADD CONSTRAINT `FKbdhoov2mv332ks2m84owt5tv3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKhm852dv0iikg9r35cvoajhf3g` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK2m9qulf12xm537bku3jnrrbup` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`),
  ADD CONSTRAINT `FKagh5svlor3slbay6tq5wqor1o` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  ADD CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`),
  ADD CONSTRAINT `FKsi8aflg05d0l1wmus7pdshcbq` FOREIGN KEY (`feedback_id`) REFERENCES `feedbacks` (`id`);

--
-- Constraints for table `orders_pizzas`
--
ALTER TABLE `orders_pizzas`
  ADD CONSTRAINT `FK4mgtb5seylr2996bnqfdei2if` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKk8hki9uwgslksaiwff3j4e2n7` FOREIGN KEY (`pizzza_id`) REFERENCES `pizzas` (`id`);

--
-- Constraints for table `orders_side_items`
--
ALTER TABLE `orders_side_items`
  ADD CONSTRAINT `FKa06l4mgqxpoh5m7rhiyfds4v5` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKpxlikouyk9vdpkp3d923pfl7v` FOREIGN KEY (`side_item_id`) REFERENCES `side_items` (`id`);

--
-- Constraints for table `pizzas_ingredients`
--
ALTER TABLE `pizzas_ingredients`
  ADD CONSTRAINT `FKjwn4lvj8ajaggx1nxospim99u` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`),
  ADD CONSTRAINT `FKql1q3n3i1m94it69jc7c1840w` FOREIGN KEY (`pizza_id`) REFERENCES `pizzas` (`id`);

--
-- Constraints for table `restaurants`
--
ALTER TABLE `restaurants`
  ADD CONSTRAINT `FKf77xr396uxbl0crr0pq0qj2q7` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
