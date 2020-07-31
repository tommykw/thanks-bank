<#import "common/milligram.ftl" as b>

<@b.page>
    <div class="row">
       <p id="file_name">No name</p>
    </div>

    <script>
    document.addEventListener('DOMContentLoaded', function() {
        KotlinPlayground('.kotlin-code-2', { onChange: (code) => {
            const obj = {name: "no name", code: code};
            const method = "POST";
            const body = JSON.stringify(obj);
            const headers = {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=utf-8'
            };
            fetch("./api/v1/playground", {method, headers, body}).then((res)=> res.json()).then(console.log).catch(console.error);
        }})
    });

    </script>

    <div id="kotlin-example" class="kotlin-code-2">
    <pre>
    <code class="hljs language-text">
    ${code}
    </code>
    </pre>
    </div>

        <div class="panel-body">
        <form method="post" action="/playground">
        <input type="hidden" name="action" value="add">
        Playground:<br>
        name:<input type="text" name="name" /><br>
        code:<input type="text" name="code" /><br>
        <input type="submit" value="submit" />
        </form>
        </div>

</@b.page>