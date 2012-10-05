
function initTabs(tabname)
{
    var html = "";

    var i = 1;
    while ($("#" + tabname + i).attr("title") != undefined)
    {
        html += "<a href='#' onclick='return tabClick(\"" + tabname + "\"," + i + ")'>" + $("#tab" + i).attr("title") + "</a> | ";
        $("#" + tabname + i).css("display","none");
        i++;
    }

    $("#tabWrapper").html(html.substr(0,html.length-3));
}

function tabClick(tabname, tabNr)
{
    // hide tabs
    var i = 1;
    while ($("#" + tabname + i).attr("title") != undefined)
    {
        $("#" + tabname + i).css("display","none");
        i++;
    }
    $("#" + tabname + tabNr).css("display","block");
    
    return false;
}