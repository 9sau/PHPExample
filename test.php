<?php

$json = file_get_contents('php://input');
$request = json_decode($json, true);

$book = array(
    ["name" => $request["name"],
    "email" => $request["email"],
    "edition" => 6]
    );


print(json_encode($book));

?>