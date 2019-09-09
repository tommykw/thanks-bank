<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if playgrounds?? && (playgrounds?size > 0)>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Playground</th>
                </tr>
            </thead>
            <tbody>
                <#list playgrounds as playground>
                    <tr>
                        <td style="vertical-align:middle"><h3>${playground.name}</h3></td>
                        <td style="vertical-align:middle"><h3>${playground.code}</h3></td>
                        <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                            <form method="post" action="/playground">
                                <input type="hidden" name="date" value="${date?c}">
                                <input type="hidden" name="code" value="${code}">
                                <input type="hidden" name="id" value="${playground.id}">
                                <input type="hidden" name="action" value="delete">
                                <input type="image" src="/static/tommykw.png" width="24" height="24" border="0" alt="Delete" />
                            </form>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </#if>
</@b.page>