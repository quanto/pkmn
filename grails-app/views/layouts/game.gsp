<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <script type="text/javascript" src="${resource(uri:'')}/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="${resource(uri:'')}/js/tablet/move.js"></script>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <script type="text/javascript">

        $(document).ready(function() {
            $('#right').attr('height',($(window).height() - $("#top").height()) + "px");
        });

        $(window).resize(function() {
            $('#right').attr('height',($(window).height() - $("#top").height()) + "px");
        });

    </script>
    <style>



    </style>
    <g:layoutHead/>
    <r:layoutResources />

</head>
<body>

<table id="layout" cellspacing="0" rowspacing="0">
    <tr>
        <td colspan="2" id="top">
            <g:render template="/layouts/menu" />
        </td>
    </tr>
    <tr>

        <td valign="top" id="left">
            <g:layoutBody/>
        </td>
        <g:if test="${pageProperty(name: 'page.showRightContent')}">
            <td valign="top" id="right" rowspan="2">

                <div id="accordion">
                    <h1>Map</h1>
                    <div class="userData">
                        <div id="mapName"></div>
                        <div id="location"></div>
                    </div>
                    <h1>Online</h1>
                    <div id="online">

                    </div>
                    <h1>Party</h1>
                    <div id="party">

                    </div>
                    <h1>News</h1>
                    <div id="news">

                    </div>
                    <h1>Stats</h1>
                    <div id="stats">

                    </div>
                    <h1>Items</h1>
                    <div id="items">

                    </div>
                </div>
            </td>
        </g:if>
    </tr>
    <g:if test="${pageProperty(name: 'page.showBottomContent')}">
        <tr>
            <td id="bottom" valign="bottom">
                <div class="chatBox" id="chatBox">
                </div>
                <div>
                    <input type="text" id="chatMessage" />
                </div>
            </td>
        </tr>
    </g:if>
</table>

<g:javascript library="application"/>
<r:layoutResources />
</body>
</html>