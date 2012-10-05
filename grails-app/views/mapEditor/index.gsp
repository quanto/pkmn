<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>

    <g:link action="editor">New Map</g:link>
    <br/>

    <g:each in="${maps}" var="map">
        <g:link action="editor" id="${map.id}">${map.id} ${map.name}</g:link>
        <br />
    </g:each>


</body>
</html>