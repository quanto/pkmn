
    function mapdata()
    {
        data = "{" + height + "," + width + "}";
        // For layers
        for (var l=0;l<2;l++)
        {
            data += "{";
            for (var h=0;h<height;h++)
            {
                for (var w=0;w<width;w++)
                {
                	// background  
                    data += field[l][h][w] + ",";
                }
            }
            data = data.substr(0,data.length - 1);
            data += "}";
        }
        
        document.getElementById("mapdata").value = data;	 
    }
     
    // create mapdata
    function gamedata()
    {
        // mapdata
        var data = "";
        
        data += "var height = " + height + ";";
        data += "var width = " + width + ";";
        
        var foregound = "";
        for (var h=0;h<height;h++)
        {
            for (var w=0;w<width;w++)
            {
            	// foreground  
                if (field[1][h][w] != "")
                {
                    // position of foreground block
                    foregound += "new Array(" + h + "," + w + "),";
                    
                }
            }
        }	
        if (foregound != "")
        {
            data += "var foreground = new Array(" + foregound.substr(0,foregound.length - 1) + ");";
        }
        
        document.getElementById("mapdata").value = data;
    }