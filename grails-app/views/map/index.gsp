<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>GameEngine</title>
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-1.7.min.js"></script>
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

		<style type="text/css">
			/*demo page css*/
			body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
			.demoHeaders { margin-top: 2em; }
			#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
			#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
			ul#icons {margin: 0; padding: 0;}
			ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
			ul#icons span.ui-icon {float: left; margin: 0 4px;}
			div#tabs {width: 520px;}
			.ui-tabs-nav {cursor:crosshair;}
		</style>

<style type="text/css">

	table.computerList
	{
		font-size:11px;
		border-collapse: collapse;
	}
	
	table.computerList td
	{
		background-color:#DDDDDD;
		height:30px;
		border-top:1px solid #CCCCCC;
	}
	
	img
	{
		border:0;
	}
	
	a:link, a:visited
	{
		color:black;
	}

	
	h3
	{
		margin:0px;
		padding:0px;
	}
	
	body
	{
		font-family:Arial, Helvetica, sans-serif;
	}
	
	.textBox
	{
		background-image:url(images/textFrame.gif);
		height:32px;
		width:224px;
		margin:4px 0px 4px 0px;
		padding:5px;
		font-size:12px;
		font-family:Verdana, Arial, Helvetica, sans-serif;
	}
	.chatBox
	{
		width:300px;
		height:250px;
		overflow:auto;
		border:1px solid #000000;
		margin-bottom:4px;
		font-size:12px;
	}
	.chatTime
	{
		font-size:10px;
		font-family:"Times New Roman", Times, serif;
	}
	.online	
	{
		border: #00FF00 solid 1px;
		background-color:#C6FFC6;
	}
	.offline
	{
		border:1px #FF0000 solid;
		background-color: #FF9999;
	}
	
	#whois
	{
		font-size:12px;
	}
	
	.userData
	{
		font-size:12px;
		border:#999999 1px solid;
	}
	
	h3
	{
		margin:0px;
		padding:0px;
	}
	
	ul.menu
	{
		padding:0px;
		margin:0px;
	}
	
	li.menu
	{
		padding:2px;
		margin:0px 2px 0 0;
		float:left;
		list-style: none;
		font-size:10px;
		border:1px solid #666666;
		background-color: #CCCCCC;
	}
	
	li.menu:hover
	{
		background:#FFFFFF;
		cursor:pointer;
	}
	
	div.newsItem
	{
		border:#666666 solid 1px;
		background-color:#F1FED6;
		padding:2px 1px 2px 1px;
		margin:2px 0px 2px 0px;
		max-width:300px;
	}
</style>
</head>

<body>
<script type="text/javascript" src="js/tooltip.js"></script> 
<h3 id="firebug" style="color:#FF0000;"></h3>
<div style="width:300px; font-size:18px; color:#00FF00;"><marquee id="serverMessage" scrollamount="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</marquee></div>

<a href="logOut.php">LogOut</a> | 
<g:if test="${1==1}">
    <a href="adminPannel.php"> Admin panel </a>
</g:if>
<table border="0">
    <tr>
    	<td valign="top">
            <div id="theMap">
            </div>
        </td>
        <td valign="top">
        	<div class="chatBox" id="chatBox">
            </div>
			<div><input type="text" id="chatMessage" size="46" /></div>
        </td>
        <td valign="top">
			<ul class="menu">
            	<li class="menu" onclick="loadLink('online');">Online</li>
                <li class="menu" onclick="loadLink('pokemon');">Pokemon</li>
                <li class="menu" onclick="loadLink('news');">News</li>
                <li class="menu" onclick="loadLink('quests');">Quests</li>
            </ul>
        	<table cellpadding="0" cellspacing="0" border="0" style="clear:left;">
                <tr>
                    <td><img src="images/border/border-00.gif" /></td>
                    <td background="images/border/border-01.gif" style="background-repeat:repeat-x;"></td>
                    <td><img src="images/border/border-02.gif" /></td>
                </tr>
                <tr>
                    <td background="images/border/border-10.gif"></td>
                    <td>
                    <div class="userData">
                        <div id="mapName"></div>
                        <div id="location"></div>
                    </div>
                    <!-- <div id="whois2" style="clear:both;">	 -->
                    </div>
                    </td>
                    <td background="images/border/border-12.gif"></td>
                </tr>
                <tr>
                    <td><img src="images/border/border-20.gif" /></td>
                    <td background="images/border/border-21.gif" style="background-repeat:repeat-x;"></td>
                    <td><img src="images/border/border-22.gif" /></td>
                </tr>
            </table>
        </td>
        <td valign="top" id="party">
        </td>
    </tr>
    <tr>
    	<td colspan="3"><div class="textBox" id="textBox"></div></td>
    </tr>
</table> 
        
</body>
</html>
