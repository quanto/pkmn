<!--//--><![CDATA[//><!--
sfHover = function() {
	var sfEls = document.getElementById("nav").getElementsByTagName("LI");
	for (var i=0; i<sfEls.length; i++) {
		sfEls[i].onmouseover=function() {
			this.className+=" sfhover";
		}
		sfEls[i].onmouseout=function() {
			this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
		}
	}
}
if (window.attachEvent) window.attachEvent("onload", sfHover);
//--><!]]>

function loadTiles()
{
	getTiles();
}

function clickIE4(){
	if (event.button==2){
		alert(message);
		return false;
	}
}

function clickNS4(e){
	if (document.layers||document.getElementById&&!document.all){
		if (e.which==2||e.which==3){
			alert(message);
			return false;
		}
	}
}

if (document.layers){
	document.captureEvents(Event.MOUSEDOWN);
	document.onmousedown=clickNS4;
}
else if (document.all&&!document.getElementById){
	document.onmousedown=clickIE4;
}

document.oncontextmenu = doAction;


function doAction(e)
{
	var rightclick;
	if (!e) var e = window.event;
	if (e.which) rightclick = (e.which == 3);
	else if (e.button) rightclick = (e.button == 2);
	
	if(rightclick)
	{
		selectTile(e.target.parentNode.tileNr);
	}
	
	return false;
}

function selectTile(tileNr)
{						
	tileData = document.getElementById(tileNr);
	currentTile = document.getElementById("currentTile");
	
	currentTile.tileNr = tileNr;
	currentTile.src = tileData.src;
	
	currentTile.innerHTML = "<img src='"+tileData.src+"' title='"+tileNr+"' />";
}

function setTile(mapNr)
{
	currentTile = document.getElementById("currentTile");
	mapTile = document.getElementById("map"+mapNr);		
	backgroundRadio = document.getElementById("backgroundRadio");
	foregroundRadio = document.getElementById("foregroundRadio");
	
	if(backgroundRadio.checked)
	{
		mapTile.style.background = "url("+currentTile.src+")";
		mapTile.lang = currentTile.tileNr;
	}
	else if(foregroundRadio.checked)
	{
		mapTile.innerHTML = currentTile.innerHTML;
		mapTile.align = currentTile.tileNr;
	}
}

function removeRawMap()
{
	rawMap = document.getElementById("rawMap");	
	
	rawMap.innerHTML = "";
}

function saveMap()
{
	createRawMap();
	foreground = document.getElementById("rawMapForeground");
	background = document.getElementById("rawMapBackground");	
	name = document.getElementById("mapName");
	savingDiv = document.getElementById("saving");
	
	savingDiv.innerHTML = "Saving...";
	
	$.ajax({
		type: "GET",
		url: "ajax.php",
		data: "ajax=1&name="+name.value+"&foregroundMap="+foreground.innerHTML+"&backgroundMap="+background.innerHTML,
		success: function(msg){
			savingDiv.innerHTML = "Saved!";
		}
	});
}

function submitAction()
{
	name = $("#name").val();
	type = $("#type").val();
	locx = $("#locx").val();
	locy = $("#locy").val();
	map = $("#map").val();
	d1 = $("#d1").val();
	d2 = $("#d2").val();
	d3 = $("#d3").val();
	d4 = $("#d4").val();
	d5 = $("#d5").val();
	
	$.ajax({
		type: "POST",
		url: "ajax.php",
		data: "ajax=2&name="+name+"&type="+type+"&locx="+locx+"&locy="+locy+"&map="+map+"&d1="+d1+"&d2="+d2+"&d3="+d3+"&d4="+d4+"&d5="+d5,
		success: function(msg){
			getActionList();
		}
	});
}

function deleteAction(id)
{
	$.ajax({
		type: "POST",
		url: "ajax.php",
		data: "ajax=4&id="+id,
		success: function(msg){
			getActionList();
		}
	});
}



function getTiles()
{
	$("#tiles").html("<img src='images/loading.gif' alt='loading' />");
	$("#loadTiles").attr({value: "loading...", disabled: "disabled"});
	
	$.ajax({
		type: "POST",
		url: "ajax.php",
		data: "ajax=5",
		async: true,
		cache: false,
		success: function(msg){
			$("#tiles").html(msg);
			$("#loadTiles").attr({value: "Load tiles", disabled: ""});
		}
	});
}

function sendAjaxRequest(target, data, handler){
	if (window.ActiveXObject) {
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	}
	xhr.open("GET", target, true)
	xhr.onreadystatechange = function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				handler(xhr.responseText);
			}else{
				alert("Ajax fout!");
			}
		}
	}
	xhr.send(data);
}

var down = false;

$(document).ready(function () {	
	$(document).click(function(e){
		//alert(e.target.id);
		//$("#bla").text(down);
	});
	
	$(document).mousedown(function(){
		//down = true;
		//$("#bla").text(down);
    });
	
	$(document).mouseup(function(){
		//down = false;
		//$("#bla").text(down);
    });
	
	$(document).mousemove(function(e){
		if(down == true)
		{
			//e.pageX
			if(e.target.id != "")
			{
				id = e.target.id;
				tile = remove(id, "map")
				setTile(tile);
			}
			else
			{
				id = e.target.lang;
				tile = remove(id, "map")
				setTile(tile);
			}
			$("#bla").text(id);
		}
	});
	
	$(document).keydown(function(e){
		if(e.which == 17)
		{
			down = true;
		}
		//$("#bla").text(down);
	});
	
	$(document).keyup(function(e){
		if(e.which == 17)
		{
			down = false;
		}
		//$("#bla").text(down);
	});

});

function remove(s, t) {
  /*
  **  Remove all occurrences of a token in a string
  **    s  string to be processed
  **    t  token to be removed
  **  returns new string
  */
  i = s.indexOf(t);
  r = "";
  if (i == -1) return s;
  r += s.substring(0,i) + remove(s.substring(i + t.length), t);
  return r;
 }