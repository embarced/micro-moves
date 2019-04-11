<?php
if(isset($_GET["module"])) $ABOUT_LINK = "/" . $_GET["module"] . "/about.html";
else $ABOUT_LINK = "/about.html";
?>
<hr>
<div class="row">
    <div class="col-lg-12">
        <ul class="nav nav-pills nav-justified">
            <li><a href="<?= $ABOUT_LINK ?>">About this module</a></li>
            <li><a href="https://www.embarc.de" target="_blank">&copy; <?php echo date("Y"); ?> embarc.de</a></li>
            <li><a href="https://www.embarc.de/micro-moves/" target="_blank">Micro Moves blog series</a></li>
        </ul>
    </div>
</div>

<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>