<%@ page import="game.action.Action; game.MapLayout; game.Map" %>

<script type="text/javascript" src="${resource(uri:'')}/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript">

    $(document).ready( function () {
       // selectMap()
    });

    /**
    * Changing the selectbox of the new action type.
    */
    function actionTypeChanged(obj){
        $(obj).find("option").each(function(){
            $("#" + $(this).val()).css('display','none')
        });

        $("#" + $(obj).val()).css('display','block')

    }

    function getAction(x,y){
        fromMapPosition(x,y)
        $.ajax({
            type: "GET",
            url: "${createLink(action:'getAction')}",
            data: "id=${map.id}&altMapId=${altMap?.id}&positionX=" + x + "&positionY=" + y,
            cache: false,
            success: function(view){
                $("#ajax").html(view);
            }
        });
    }


    function selectMap(obj){
        $("#toMap").val($(obj).val())
        $.ajax({
            type: "GET",
            url: "${createLink(action:'choseMapTransition')}",
            data: "id=" + $("#toMap").val(),
            cache: false,
            success: function(view){
                $("#mapAjax").html(view);
            }
        });
    }

    function fromMapPosition(x,y){

        $("#current" + $("#fromX").val() + "-" + $("#fromY").val()).css("border","1px solid #ccc")

        $("#fromX").val(x)
        $("#fromY").val(y)

        $("#textBox").text("");

        $("#current" + x + "-" + y).css("border","1px solid red")

    }

    function toMapPosition(x,y){

        if (!$("#fromX").val() || !$("#fromY").val()){
            alert('select from position')
        }
        else {


            $("#toX").val(x)
            $("#toY").val(y)
            $.ajax({
                type: "GET",
                url: "${createLink(action:'saveMapTransition')}",
                data: $("#saveMapTransition").serialize(),
                cache: false,
                success: function(view){
                    // Reset selected
                    $("#to" + $("#toX").val() + "-" + $("#toY").val()).css("border","1px solid #444")
                    $("#current" + $("#fromX").val() + "-" + $("#fromY").val()).css("border","1px solid #444")
                    $("#fromX").val('')
                    $("#fromY").val('')
                    alert("MapTransition created");
                }
            });

        }
    }

</script>

<g:link controller='mapEditor' action="editor" id="${map.id}">Mapeditor</g:link>
-
<g:link controller="pokemonEditor" action="edit" id="${map.id}">Pokemon</g:link>
-
<g:link controller="actionEditor" action="actions" id="${map.id}">Actions</g:link>

<g:form action="saveMapTransition" name="saveMapTransition">
    <g:hiddenField name="fromMap" value="${map.id}" />
    <g:hiddenField name="fromAltMap" value="${altMap?.id}" />
    <g:hiddenField name="fromX" />
    <g:hiddenField name="fromY" />
    <g:hiddenField name="toMap" />
    <g:hiddenField name="toX" />
    <g:hiddenField name="toY" />
</g:form>

<div id="actionMap">

    <table cellpadding="0">

        <g:each in="${mapLayout.background}" var="row" status="y">
            <tr>
                <g:each in="${row}" var="tileNr" status="x">
                    <%
                        game.action.Action action = actions.find { it.positionX == x && it.positionY == y }
                    %>
                    <td style="${action?'border:1px solid #444;':'border:1px solid #ccc;'}">
                        <div id="current${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');" title="${x}-${y}" onclick="getAction(${x},${y})"> <%-- fromMapPosition(${x},${y}) --%>
                            <g:if test="${mapLayout.background[y][x]}">
                                <g:if test="${mapLayout.foreground[y][x] != '0'}">
                                    <img src='${resource(uri:'')}/images/tiles/sheet1/${mapLayout.foreground[y][x]}.png' />
                                </g:if>
                                <g:if test="${action?.image}">
                                    <img src='${resource(uri:'')}${action.image}' style="height:16px;width:16px;" />
                                </g:if>
                            </g:if>
                        </div>
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>

    <g:if test="${altMap?.newActions == false}">
        <br/><span style="color:red;">Actions not editable</span>
    </g:if>

</div>
<br />
<div id="ajax">


</div>