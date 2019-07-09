<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if emojis?? && (emojis?size > 0)>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Emoji</th>
                </tr>
            </thead>
            <tbody>
                <#list emojis as emoji>
                    <tr>
                        <td style="vertical-align:middle"><h3>${emoji.name}</h3></td>
                        <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                            <form method="post" action="/emojis">
                                <input type="hidden" name="date" value="${date?c}">
                                <input type="hidden" name="code" value="${code}">
                                <input type="hidden" name="id" value="${emoji.id}">
                                <input type="hidden" name="action" value="delete">
                                <input type="image" src="/static/tommykw.png" width="24" height="24" border="0" alt="Delete" />
                            </form>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </#if>
    <div class="panel-body">
    <form method="post" action="/emojis">
    <input type="hidden" name="date" value="${date?c}">
    <input type="hidden" name="code" value="${code}">
    <input type="hidden" name="action" value="add">
    Emoji:<br>
    <input type="text" name="emoji" /><br>
    <input type="submit" value="submit" />
    </form>
    </div>
</@b.page>