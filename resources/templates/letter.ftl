<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if thanks?? && (thanks?size > 0)>
        <div class="row">
        <#list thanks as thank>
            <div class="column">
                <div class="content dashboard">
                    <span>画像</span><span>${thank.slackUserId}名前さんへ</span>
                    <span>${thank.body}</span>
                    <span>リアクション数</span>
                    <span>画像</span><span>XXXより</span>
                </div>
            </div>
        </#list>
        </div>
    </#if>
</@b.page>