<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if thanks?? && (thanks?size > 0)>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>あなたへの宛てのおてがみ</th>
            </tr>
            </thead>
            <tbody>
            <#list thanks as thank>
                <tr>
                    <td style="vertical-align:middle"><h3>${thank.slackUserId}</h3></td>
                    <td style="vertical-align:middle"><h3>${thank.body}</h3></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</@b.page>