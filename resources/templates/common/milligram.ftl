<#macro page>
    <!doctype html>
    <html lang="jp">
        <head>
            <title>thanks-bank</title>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP:400,700&subset=japanese" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.css">
<#--            <link href="/static/milligram.min.css" rel="stylesheet">-->
            <link href="/static/style.css" rel="stylesheet">
        </head>
        <body>
            <#include "navbar.ftl">
            <main class="main">
                <div class="container">
                    <#nested>
                </div>
            </main>
            <#include "footer.ftl">
        </body>
    </html>
</#macro>