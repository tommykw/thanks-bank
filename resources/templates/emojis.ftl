<html>
    <body>
        <div>${name}</div>
        <ul>
            <#list emojis as emoji>
                <li>${emoji}</li>
            </#list>
        </ul>
        <form method="post" action="/emojis">
        Emoji:<br>
        <input type="text" name="emoji" /><br>
        <input type="submit" value="submit" />
        </form>
    </body>
</html>