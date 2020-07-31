<#import "common/milligram.ftl" as b>

<@b.page>
    <#if slack_messages?? && (slack_messages?size > 0)>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Slack Messages</th>
            </tr>
            </thead>
            <tbody>
            <#list slack_messages as slack_message>
                <tr>
                    <td style="vertical-align:middle"><h3>${slack_message.message}</h3></td>
                    <td style="vertical-align:middle"><h3>${slack_message.userName}</h3></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>
</@b.page>