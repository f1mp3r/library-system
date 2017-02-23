CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(80) NOT NULL,
  `password` varchar(60) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `student_id` varchar(10) NOT NULL,
  `permission` tinyint(1) NOT NULL DEFAULT '0',
  `debt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `phone_number` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

  
  
  ALTER TABLE `users` 
   MODIFY COLUMN `id` INT AUTO_INCREMENT;


