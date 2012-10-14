<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>GameEngine</title>
    <meta name='layout' content='game'/>

    <script type="text/javascript" src="${resource(uri:'')}/js/main.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery_json.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/jqueryui.js"></script>

    <script type="text/javascript">
    $(function(){

        // Dialog Link
        $('#dialog_link').click(function(){
            $('#dialog').dialog('open');
            return false;
        });

        // Dialog
        $('#dialog').dialog({
            autoOpen: false
        });

    });
    </script>

    <link type="text/css" href="${resource(uri:'')}/css/jqueryui.css" rel="stylesheet">

</head>

<body>

    <h3 id="firebug" style="color:#FF0000;"></h3>

    <div id="theMap"></div>

    <div id="textBox"></div>
        
</body>
</html>
