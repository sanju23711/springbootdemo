<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Error Reference | HibbertRestAuthenticationWebService</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <style>
            /*! normalize.css v2.1.3 | MIT License | git.io/normalize */article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary{display:block}audio,canvas,video{display:inline-block}audio:not([controls]){display:none;height:0}[hidden],template{display:none}html{font-family:sans-serif;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}a{background:transparent}a:focus{outline:thin dotted}a:active,a:hover{outline:0}h1{font-size:2em;margin:.67em 0}abbr[title]{border-bottom:1px dotted}b,strong{font-weight:700}dfn{font-style:italic}hr{-moz-box-sizing:content-box;box-sizing:content-box;height:0}mark{background:#ff0;color:#000}code,kbd,pre,samp{font-family:monospace,serif;font-size:1em}pre{white-space:pre-wrap}q{quotes:"\201C" "\201D" "\2018" "\2019"}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sup{top:-.5em}sub{bottom:-.25em}img{border:0}svg:not(:root){overflow:hidden}figure{margin:0}fieldset{border:1px solid silver;margin:0 2px;padding:.35em .625em .75em}legend{border:0;padding:0}button,input,select,textarea{font-family:inherit;font-size:100%;margin:0}button,input{line-height:normal}button,select{text-transform:none}button,html input[type=button],input[type=reset],input[type=submit]{-webkit-appearance:button;cursor:pointer}button[disabled],html input[disabled]{cursor:default}input[type=checkbox],input[type=radio]{box-sizing:border-box;padding:0}input[type=search]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}textarea{overflow:auto;vertical-align:top}table{border-collapse:collapse;border-spacing:0}
            html { background-color: #eee; }
            body { padding: 0 30px; }
            .container { max-width: 800px; margin: 0 auto; }
            h1 { color: #2A68A2; margin: 1em 0 0; }
            h2 { color: #ff0000; margin: 1em 0 0; }
            p { font-size: 1.25em; padding:0; margin: 0; color: #3F385A; }
            label{color: #2A68A2;}
        </style>
    </head>
    <body>
        <div class="container" style="padding-bottom:600px;">
            <h1>Error Reference</h1>
			<c:forEach var="error" items="${errorList}">
				<h2><a id="${error.subCode}"></a>Error ${error.subCode}</h2>
				<p><label>Root:</label> ${error.errorCode}</p>
				<p><label>Cause:</label> ${error.cause}</p>
				<p><label>Description:</label> <spring:message code="${error.subCode}"/></p>
				<hr/>
			</c:forEach>
        </div>
    </body>
</html>