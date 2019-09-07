<#import "common/bootstrap.ftl" as b>

<@b.page>
    <div class="row">
                    <p>Turns into:</p>

                    <div class="kotlin-code">
                    <code class="hljs language-kotlin">
                    fun main() {

                    }
                    </code>
                    </div>

                    <div id="myid" class="code-blocks-selector">
                    AAAAAAA
                    </div>

                    <script>
                    function onChange(code) {
                      console.log("Editor code was changed:" + code);
                      // 取れたとしてコードを保存しましょう。

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

                    var mydiv = document.getElementById("myid")
                    //onChange: onChange(mydiv.innerHTML),

                    const options = {
                      onChange: onChange("code"),
                      onTestPassed: onTestPassed,
                      onCloseConsole: onCloseConsole,
                      onOpenConsole: onOpenConsole,
                      onTestFailed: onTestFailed
                    };

                    document.addEventListener('DOMContentLoaded', function() {
                      KotlinPlayground('.code-blocks-selector', options);
                      //KotlinPlayground('.code-blocks-selector', options);
                      //KotlinPlayground('code', options);
                    });

                    </script>
    </div>
</@b.page>