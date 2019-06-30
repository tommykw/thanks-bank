<#macro page>
    <!doctype html>
    <html lang="en">
        <head>
            <title>EmojiPhrases</title>
            <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        </head>
        <body>
            <#include "navbar.ftl">
            <div class="container-fluid">
                <#nested>
                <#include "footer.ftl">
            </div>

            <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js">
            </script>
            <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js">
            </script>
        </body>
    </html>
</#macro>