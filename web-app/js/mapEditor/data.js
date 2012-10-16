
//    function createMapData()
//    {
//        data = "{" + height + "," + width + "}";
//        // For layers
//        for (var l=0;l<2;l++)
//        {
//            data += "{";
//            for (var h=0;h<height;h++)
//            {
//                for (var w=0;w<width;w++)
//                {
//                	// background
//                    data += field[l][h][w] + ",";
//                }
//            }
//            data = data.substr(0,data.length - 1);
//            data += "}";
//        }
//
//        document.getElementById("mapdata").value = data;
//    }

    function createMapData()
    {
        var data = "";

        for (var h=0;h<height;h++)
        {
            for (var w=0;w<width;w++)
            {
                // background
                data += field[0][h][w] + ",";
            }
            data = data.substr(0,data.length - 1);
            data += "-";
        }
        data = data.substr(0,data.length - 1);

        $("#background").val(data)

        data = "";

        for (var h=0;h<height;h++)
        {
            for (var w=0;w<width;w++)
            {
                if (field[1][h][w] != ''){
                    data += field[1][h][w] + ",";
                }
                else {
                    // Empty cell
                    data += "0,";
                }

            }
            data = data.substr(0,data.length - 1);
            data += "-";
        }
        data = data.substr(0,data.length - 1);

        $("#foreground").val(data)
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


    function createStampData(){
        var stampStr = "new Array("
//        alert(pattern)
        for (var h=0;h<pattern.length;h++)
        {
            stampStr += "new Array("

            for (var w=0;w<pattern[h].length;w++)
            {
                if (typeof pattern[h][w] === "string"){
                    stampStr += "'" + pattern[h][w] + "',"
                }
                else {
                    stampStr += pattern[h][w] + ","
                }

            }
            // remove last ,
            stampStr = stampStr.substr(0,stampStr.length - 1) + "),";
        }
        stampStr = stampStr.substr(0,stampStr.length - 1)
        stampStr += ");"
        $("#stampData").val(stampStr)
    }