<html>
	<head>
		<title>GameEngine</title>
		<meta name='layout' content='main'/> 
	</head>
	<body>
		<g:include controller="admin" action="showNewsItems" />
		<g:form action="saveNewsItem" controller="admin">
			<input type="text" id="newsItem" name="newsItem" />
			<input type="submit" value="Add" />
		</g:form>
	</body>
</html>