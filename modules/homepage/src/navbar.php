<?php
require __DIR__ . '/vendor/autoload.php';
use \Firebase\JWT\JWT;

$jwtCookieName = getenv('JWT_COOKIE_NAME');
$jwtSecret = getenv('JWT_SECRET');

$isLogedIn = false;
if(isset($_COOKIE[$jwtCookieName])) {
    $jwt = $_COOKIE[$jwtCookieName];
    try {
        $decoded = JWT::decode($jwt, $jwtSecret, array('HS256'));
        $decoded_array = (array)$decoded;
        $userid = $decoded_array['sub'];
        $name = $decoded_array['name'];
        $isLogedIn = true;
    } catch (Exception $e) {
    }
}

if(isset($_GET["active"])) $active = $_GET["active"];
else $active = "";

?>
<div class="navbar navbar-inverse" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle Navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">FLEXess</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li<?= $active == "games" ? " class=\"active\"" : "" ?>><a href="/games/"><span class="glyphicon glyphicon-th" aria-hidden="true"></span> Games</a></li>
                <li<?= $active == "players" ? " class=\"active\"" : "" ?>><a href="/players/"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Players</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <?php
                if(!$isLogedIn) {
                ?>
                    <li><a href="/players/register">Register</a></li>
                    <li><a href="/players/login">Login</a></li>
                <?php
                } else {
                ?>
                    <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><?= $name ?> <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/players/profile_<?= $userid ?>">Profile <?= $userid ?></a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="/players/logoff"><span class="glyphicon glyphicon-log-out"></span> Log off</a></li>
                    </ul>
                    </li>
                <?php
                }
                ?>
            </ul>
        </div>
    </div>
</div>