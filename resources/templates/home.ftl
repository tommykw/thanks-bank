<#import "common/bootstrap.ftl" as b>

<@b.page>
    <div class="row">
                    <p>Turns into:</p>

                    <div id="code" class="code-blocks-selector">
                    AAAAAA
                    </div>

                    <script>
                    function onChange(code) {
                       console.log("Editor code was changed:" + code);
                    }

                    function onTestPassed() {
                       console.log("Tests passed!");
                    }

                    function onCloseConsole() {
                       console.log("Close Console!");
                    }

                    function onOpenConsole() {
                       console.log("Open Console!");
                    }

                    function onTestFailed() {
                       console.log("Test Failed!");
                    }

                    var code = document.getElementById("code")

                    const options = {
                      onChange: onChange(code.textContent),
                      onTestPassed: onTestPassed,
                      onCloseConsole: onCloseConsole,
                      onOpenConsole: onOpenConsole,
                      onTestFailed: onTestFailed
                    };

                    document.addEventListener('DOMContentLoaded', function() {
                      KotlinPlayground('.code-blocks-selector', options);
                    });
                    </script>
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