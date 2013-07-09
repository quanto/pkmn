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
            data: "id=" + $("#fromMap").val() + "&positionX=" + x + "&positionY=" + y,
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

<g:form action="saveMapTransition" name="saveMapTransition">
    <g:hiddenField name="fromMap" value="${map.id}" />
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
                        game.action.Action action = map.actions.find { it.positionX == x && it.positionY == y }
                    %>
                    <td style="${action?'border:1px solid #444;':'border:1px solid #ccc;'}">
                        <div id="current${x}-${y}" style="height:16px; width:16px; background:url('${resource(uri:'')}/images/tiles/sheet1/${tileNr}.png');" title="${x}-${y}" onclick="getAction(${x},${y})"> <%-- fromMapPosition(${x},${y}) --%>
                            <g:if test="${mapLayout.background[x][y]}">
                                <g:if test="${mapLayout.foreground[y][x] != '0'}">
                                    <img src='${resource(uri:'')}/images/tiles/sheet1/${mapLayout.foreground[y][x]}.png' />
                                </g:if>
                                <g:if test="${action?.tileImage}">
                                    <img src='${resource(uri:'')}/images/tiles/sheet1/${action.tileImage}.png' />
                                </g:if>
                            </g:if>
                        </div>
                    </td>
                </g:each>
            </tr>
        </g:each>
    </table>
</div>
<br />
<div id="ajax">


</div>