<?php
require 'flight/Flight.php';

Flight::route('/', function(){ 
	echo 'shoppinglist server is up and running :)'; 
	echo Flight::request()->data->text;
	}
);

Flight::route('GET /shoppinglist', 'getShoppingList');
Flight::route('POST /delete', 'deleteItem');
Flight::route('POST /create', 'createItem');

Flight::start();

function getShoppingList() {
	try {
		$pdo = new PDO('sqlite:database.db');
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		Flight::json($pdo->query('select * from shoppinglist')->fetchAll(PDO::FETCH_ASSOC));
	} catch(PDOException $e) {
		Flight::halt(500, 'Database error: ' . $e->getMessage());
	}
}

function createItem() {
	$text 	= Flight::request()->data->text;
	$author	= Flight::request()->data->author;
	$id = 0;
	
	if (empty($text)) Flight::halt(400, 'Text must not be empty');
	try {
		$pdo = new PDO('sqlite:database.db');
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$stmt = $pdo->prepare('insert into shoppinglist (text, author) values (?, ?)');
		if ($stmt->execute([$text, $author]) == false)
			Flight::halt(500, 'Insert not successful');
		$id = $pdo->lastInsertId();
	} catch(PDOException $e) {
		Flight::halt(500, 'Database error: ' . $e->getMessage());
	}
	
	Flight::halt(200, $id);
}

function deleteItem() {
	$id = Flight::request()->data->id;
		
	if (empty($id)) Flight::halt(400, 'ID must not be empty');
	
	try {
		$pdo = new PDO('sqlite:database.db');
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$stmt = $pdo->prepare('delete from shoppinglist where id = ?');
		if ($stmt->execute([$id]) == false)
			Flight::halt(500, 'Deletion not successful.');
	} catch(PDOException $e) {
		Flight::halt(500, 'Database error: ' . $e->getMessage());
	}
	Flight::halt(200, 'OK');
}
