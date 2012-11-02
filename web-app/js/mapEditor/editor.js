 
    var height = 0;
    var width = 0;
    var field = new Array();
    // layers

    // background layer
    field[0] = new Array();
    // foreground
    field[1] = new Array();
    // Object/Action layer
    field[2] = new Array();
    // Top layer
    field[3] = new Array();
    
    // single selected tile
    var selectedTile = "01";
    
    // 0 background | 1 foreground
    selectedLayer = 1;
    
    // Startpoint from clicks
    var startPointX = -1;
    var startPointY = -1;
    
    // Steps for the path
    var steps = Array();
    
    // History of the field for undo
    var historyFields = new Array();
    
    // pattern | random | stamp | path | tile | select
    var action = "path";
    
    // for path & tile, option to select single or multiple tiles
    var singleSelection = false;
    
    /*
    var randomTiles = new Array(
                    // tile, chance
                     "01",90
                    ,"02",10
                    ); 
    */                
    var randomTiles = new Array(
                    // tile, chance
                     "00",91
                    ,"10",3
                    ,"20",3
                    ,"30",3
                    );    
                       
              
    // Forrest
    /*           
    // repeating treas
    var pattern = new Array(
                    new Array("54",0,"64",0,"74",0),
                    new Array("57",0,"65",0,"77",0)
                    );  
       */               
    var pattern = new Array(
                    new Array("60",1,"70",1),
                    new Array("41",1,"51",1),
                    new Array("42",1,"52",1)
                    );                  

    // path pattern                  
    
    // Mud
    

    var pathPattern = new Array(
                    new Array("59","69","79"),
                    new Array("510","610","710"),
                    new Array("511","611","711"), 
                    // Hoeken: left top | right top  | left bottom | right bottom
                    new Array("310","410","311","411")
                    ); 	

	function hideBorders()
    {
        for (var h=0;h<height;h++)
        {
            for (var w=0;w<width;w++)
            {
            	document.getElementById(h + "-" + w).style.border = "0";
            }
        }	
	} 
	
    
    function fillPath()
    {
    	// Loop path
		for (var i=0;i<steps.length;i++)
        {
        	var top = false;
        	var bottom = false;
        	var left = false;
        	var right = false;
        	
            // find surroundings by looping through the steps
           	for (var a=0;a<steps.length;a++)
           	{
           		// check top
				if (steps[i][0] - 1 == steps[a][0] && steps[i][1] == steps[a][1])
				{
					top = true;
				}
				// check bottom
				if (steps[i][0] + 1 == steps[a][0] && steps[i][1] == steps[a][1])
				{
					bottom = true;
				}
				// check left
				if (steps[i][0] == steps[a][0] && steps[i][1] - 1  == steps[a][1])
				{
					left = true;
				}
				// check right
				if (steps[i][0] == steps[a][0] && steps[i][1] + 1  == steps[a][1])
				{
					right = true;
				}
				
				// apply tile
				
				// right
				if ((top || steps[i][0] == 0) && (bottom || steps[i][0] == height - 1) && left && (!right && steps[i][1] != width - 1))
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[1][2];
				}
                // left
				else if ((top || steps[i][0] == 0) && (bottom || steps[i][0] == height - 1) && (!left && steps[i][1] != 0) && right)
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[1][0];
				}
                // top
				else if ((!top && steps[i][0] != 0) && bottom && (left || steps[i][1] == 0) && (right || steps[i][1] == width - 1))
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[0][1];
				}
                // bottom
				else if (top && (!bottom && steps[i][0] != height - 1) && (left || steps[i][1] == 0) && (right || steps[i][1] == width - 1))
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[2][1];
				}
				
				// bottom right
				else if (top && !bottom && left && (!right && steps[i][1] != width - 1))
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[2][2];
				}
				
				// left bottom
				else if (top && !bottom && (!left && steps[i][1] != 0) && right)
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[2][0];
				}
				
				// top right
				else if (!top && bottom && left && (!right && steps[i][1] != width - 1))
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[0][2];
				}
				// top left
                //(!left && steps[i][1] != 0)
                else if (!top && bottom && (!left && steps[i][1] != 0) && right)
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[0][0];
				}
				// middle
				else
				{
					field[0][steps[i][0]][steps[i][1]] = pathPattern[1][1];
				}
			}
        }

        // Check if pattern has inside corners        
        if (pathPattern.length == 4)
        {
            // second check - apply inside corners
            for (var i=0;i<steps.length;i++)
           	{
           	    // check not on surrounded line
                if (steps[i][0] != 0 && steps[i][0] != height - 1 && steps[i][1] != 0 && steps[i][1] != width - 1)
                {
               	    // check middle title
                    
               	    if (field[0][steps[i][0]][steps[i][1]] == pathPattern[1][1])
                    {
                        // check left
                        if (field[0][steps[i][0] - 1][steps[i][1]] == pathPattern[0][0] || field[0][steps[i][0] - 1][steps[i][1]] == pathPattern[1][0])
                        {
                            field[0][steps[i][0]][steps[i][1]] = pathPattern[3][0];
                        }
                        
                        // bottom left
                        else if (field[0][steps[i][0] + 1][steps[i][1]] == pathPattern[2][0] || field[0][steps[i][0] + 1][steps[i][1]] == pathPattern[1][0])
                        {
                            field[0][steps[i][0]][steps[i][1]] = pathPattern[3][2];
                        }
                        
                        // top left
                        else if (field[0][steps[i][0] - 1][steps[i][1]] == pathPattern[0][2] || field[0][steps[i][0] - 1][steps[i][1]] == pathPattern[1][2])
                        {
                            field[0][steps[i][0]][steps[i][1]] = pathPattern[3][1];
                        }
                        
                        // bottom left
                        else if (field[0][steps[i][0] + 1][steps[i][1]] == pathPattern[2][2] || field[0][steps[i][0] + 1][steps[i][1]] == pathPattern[1][2])
                        {
                            field[0][steps[i][0]][steps[i][1]] = pathPattern[3][3];
                        }
                        
                    }
                }
            }
            
   	    }
        
        // reset the steps
        steps = Array();
        drawField();
        
        addHistoryAction();
	}
	
	function selectPathTile(y,x)
	{
    	// register the step
    	var index = steps.length;
    	steps[index] = new Array();
    	steps[index][0] = y;
    	steps[index][1] = x;
    	
    	// Border the tile
    	document.getElementById(y + "-" + x).style.border = "1px solid #444444";
	}
    
    function fillSingleTile(y,x)
    {
		field[selectedLayer][y][x] = selectedTile;
		drawField();
		addHistoryAction();
	}
    
    // perform action after clicking a tile on the map
    function fieldClick(y,x)
    {
    	if (action == "path" && singleSelection)
        {	
			selectPathTile(y,x);
        }
        // single tile
		else if (action == "tile" && singleSelection)
        {
        	fillSingleTile(y,x);
		}  	
		// Direct action
        else if (action == "stamp")
        {
            fillPattern(x,y,x + ((pattern[0].length - 1) / 2),y + pattern.length - 1);
        }
        // random or pattern
        else if (action == "random" || action == "pattern" || action == "path" || action == "tile" || action == "select")
        {
            
	        // See if this is the first point
	        if (startPointX == -1)
	        {
	            // Set start point
                startPointX = x;
                startPointY = y;
                // Border the first
                document.getElementById(y + "-" + x).style.border = "1px solid black";
                
	        }
	        // Take action
	        else
	        {
	           
	        	// switch if nessecery
	        	if (startPointX > x)
	        	{
					var temp = x;
					x = startPointX;
					startPointX = temp;
				}
			
				if (startPointY > y)
	        	{
					var temp = y;
					y = startPointY;
					startPointY = temp;
				}

	            // Do the action
			   	if (action == "random")
		        {
		            fillRandom(startPointX,startPointY,x,y);
		        }
		        else if (action == "pattern")
		        {
		            fillPattern(startPointX,startPointY,x,y);
		        }
                else if (action == "path")
		        {
		            selectPath(startPointX,startPointY,x,y);
		        }
                else if (action == "tile")
		        {
		            fillSquare(startPointX,startPointY,x,y);
		        }
                else if (action == "select")
                {
                    makeSelection(startPointX,startPointY,x,y);
                }
	            
                // Reset points
	            startPointX = -1;
	            startPointY = -1;
	            
	        }			
		}
        return false;
    }
    
    function selectPath(startPointX,startPointY,endPointX,endPointY)
    {
        for (var h=startPointY;h<=endPointY;h++)
        {
            for (var w=startPointX;w<=endPointX;w++)
            {
                selectPathTile(h,w);
            }
        }
    }

    /**
     * Make a selection on the map. The selection becomes an new array
     */
    function makeSelection(startPointX,startPointY,endPointX,endPointY)
    {
        var newPattern = new Array()
        for (var h=startPointY;h<=endPointY;h++)
        {
            newPattern[h - startPointY] = new Array()
            for (var w=startPointX;w<=endPointX;w++)
            {
                //alert ((w-startPointX)*2)
                newPattern[h-startPointY][(w-startPointX)*2] = field[selectedLayer][h][w]
                newPattern[h-startPointY][((w-startPointX)*2)+1] = selectedLayer
            }
        }
        setStamp(newPattern)
    }
    
    function fillSquare(startPointX,startPointY,endPointX,endPointY)
    {
        for (var h=startPointY;h<=endPointY;h++)
        {
            for (var w=startPointX;w<=endPointX;w++)
            {
                field[selectedLayer][h][w] = selectedTile;
            }
        }
        drawField();
		addHistoryAction();
    }

    function fillPattern(startPointX,startPointY,endPointX,endPointY)
    {
        var patternXCount = 0;
        var patternYCount = 0;
        
        for (var h=startPointY;h<=endPointY;h++)
        {
            for (var w=startPointX;w<=endPointX;w++)
            {
                if (w < width && h < height)
                {
                    // Get the layer for tile
                    var layer = pattern[patternYCount][(patternXCount * 2) + 1];
                    field[layer][h][w] = pattern[patternYCount][(patternXCount * 2)];
                    patternXCount++;
                    if (patternXCount >= (pattern[0].length / 2))
                    {
                        patternXCount = 0;
                    }
                }
            }
            
            patternYCount++;
            patternXCount = 0;
            if (patternYCount >= pattern.length)
            {
                patternYCount = 0;
            }
        }
        drawField();
        addHistoryAction();
    }
    
    function fillRandom(startPointX,startPointY,endPointX,endPointY)
    {
        // Count the random total
        randomCount = 0;
        for (var i=0;i<(randomTiles.length / 2);i++)
        {
            randomCount += randomTiles[i * 2 + 1];
        }

        // apply the tiles
        for (var h=startPointY;h<=endPointY;h++)
        {
            for (var w=startPointX;w<=endPointX;w++)
            {
                // Random number on total randomCount
                var randomNumber = Math.floor(Math.random() * randomCount);
                
                // Get the tile 
                var valueCount = 0; 
                for (var i=0;i<(randomTiles.length / 2);i++)
                {
                    // add up prev values 
                    valueCount += randomTiles[i * 2 + 1];
                    // see if this fals between random zone
                    if (randomNumber < valueCount)
                    {
                        field[0][h][w] = randomTiles[i * 2];
                        break;
                    }
                    
                }
            }
        }
        drawField();
    }
    
    function addHistoryAction()
    {
        // copy for redo
        var index = historyFields.length;

        historyFields[index] = copyFieldArray();
	}
	
	function copyFieldArray()
	{
		var newFieldArray = new Array();
		
        // layers (2)
        for (var l=0;l<2;l++)
        {
            newFieldArray[l] = new Array();
           
    		for (var h=0;h<height;h++)
            {
                 
                newFieldArray[l][h] = new Array();
                
                for (var w=0;w<width;w++)
                {
                    newFieldArray[l][h][w] = field[l][h][w];
                }
            } 
        }
        
        return newFieldArray;
	}
    
    function undo()
    {

        if (historyFields.length > 0)
        {
        	var index = historyFields.length - 2;

            field = historyFields[index];
            
            // reset size
            width = historyFields[index][0][0].length;
            height = historyFields[index][0].length;
            
            // redraw
            drawField();
            
            // Duplicate field for new references
            historyFields[index] = copyFieldArray()
            
            historyFields.pop();
            
        }
        else
        {
			var index = 0;
            field = historyFields[index];
            
            // reset size
            width = historyFields[index][0].length;
            height = historyFields[index].length;
            
            // redraw
            drawField();
		}
        return false
    }
    
    /*
        Shows current selection by applying black borders
    */
    function showSelection(startPointY,startPointX,y,x)
    {
        // Draw white borders
        
        $("#fieldWrapper td").css("border","1px solid white");
        
        // show step borders
        showStepSelection();
        
        // switch if nessecery
    	if (startPointX > x)
    	{
			var temp = x;
			x = startPointX;
			startPointX = temp;
		}
	
		if (startPointY > y)
    	{
			var temp = y;
			y = startPointY;
			startPointY = temp;
		}
        
        // Draw border
        for (var h=startPointY;h<=y;h++)
        {
            for (var w=startPointX;w<=x;w++)
            {
                if (w < width && h < height)
                {
                    //$(h + "-" + w).css("border","1px solid black");
                    document.getElementById(h + "-" + w).style.border = "1px solid black";
                }
            }
        }
        
    }
    
    /*
        Mouse over tile 
        Start action showing of selection
    */
    function mouseOverEvent(y,x)
    {
        // Only on mouse down
        if (startPointX > -1)
        {
            showSelection(startPointY,startPointX,y,x);
        }
        // Show stamp size
        else if (action == "stamp")
        {
            showSelection(y,x,y+pattern.length-1,x+(pattern[0].length / 2)-1);
        }
    }
    
    function drawField()
    {
        var html = "";
        for (var h=0;h<height;h++)
        {
            html += "<tr>";
            for (var w=0;w<width;w++)
            {
                html += "<td  style=\"background-image:url('/game/images/tiles/sheet1/" + field[0][h][w] + ".png');height:18px;width:16px;\" onmouseover=\"mouseOverEvent(" + h + "," + w + ")\" id='" + h + "-" + w + "'>";
                html += "<div title=\"" + w + "-" + h + "\" style='position:relative;'>";
                html += "<a onclick=\"return fieldClick(" + h + "," + w + ");\">";
                
                if (field[1][h][w] != "")
                {
                     html += "<img src='/game/images/tiles/sheet1/" + field[1][h][w] + ".png'>";
                }
                else
                {
                    html += "<img src='/game/images/mapEditor/empty.gif'>";
                    
                }
                html += "</a>";
                html += "</div>";
                
                
                html += "</td>";
            }
            html += "</tr>";
        }
        
        $("#fieldWrapper").html(html)

        showStepSelection();
    }
    
    function showStepSelection()
    {
        // Reselect steps
        for (var i=0;i<steps.length;i++)
    	{
    	   document.getElementById(steps[i][0] + "-" + steps[i][1]).style.border = "1px solid #444444";
        }
    }
    
    function setSizeAction()
    {
        setSize(document.getElementById("fieldWidth").value,document.getElementById("fieldHeight").value)
        addHistoryAction();
    }
    
    function setSize(newHeight,newWidth)
    {
        // Increase height arrays
        if (newHeight > height)
        {
            for (var h=height;h<newHeight;h++)
            {
                field[0][h] = new Array();
                
                field[1][h] = new Array();
                
                // Fill the width
                for (var w=0;w<newWidth;w++)
                {
                    field[0][h][w] = "00";
                    field[1][h][w] = "";
                }
            }
        }
        // increase width
        if (newWidth > width)
        {
            for (var h=0;h<height;h++)
            {
                // Fill the width
                for (var w=width;w<newWidth;w++)
                {
                    field[0][h][w] = "00";
                    field[1][h][w] = "";
                }
            }
        }
        
        height = newHeight;
        width = newWidth;
        
        // Increase width by adding empty fields
        drawField();
		
    }

    /*
    User functions
    */
    function setSelect()
    {
        startPointX = -1;
        startPointY = -1;
        action = 'select';
        //pathPattern = path;
    }

    function setPath(path)
    {
        startPointX = -1;
        startPointY = -1;
        action = 'path';
        pathPattern = path;
    }
    
    function setTile(tile)
    {
        startPointX = -1;
        startPointY = -1;
        action = "tile";
        selectedTile = tile;
        
        // Show tile
        document.getElementById("selectedTile").src = "/game/images/tiles/sheet1/" + tile + ".png";
        
        return false;
    }
    
    function setStamp(stamp)
    {
        startPointX = -1;
        startPointY = -1;
        action = 'stamp';
        pattern = stamp;
    }
    
    function toggleLayer(obj)
    { 
        if (selectedLayer == 0)
        {
            selectedLayer = 1;
            obj.value = "foreground";
        }
        // obj layer not supported
//        else if (selectedLayer == 1)
//        {
//            selectedLayer = 3;
//            obj.value = "top layer";
//        }
        else if (selectedLayer == 1)
        {
            selectedLayer = 0;
            obj.value = "background";
        }
    }
    
    function toggleSelection(obj)
    { 
        if (singleSelection)
        {
            singleSelection = false;
            obj.value = "multiple";
        }
        else
        {
            singleSelection = true;
            obj.value = "single";
        }
    }