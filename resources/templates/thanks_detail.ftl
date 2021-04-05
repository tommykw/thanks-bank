<#global page_title = "サンクス詳細" />
<#import "common/container.ftl" as container>

<@container.page>
    <div class="thanks-detail-message">
        <div class="thanks-detail-message-user">
            <img class="thanks-detail-message-user-from" src="${thank.userImage}" width="40" height="40"/>
            <img class="thanks-detail-message-user-to" src="${thank.targetUserImage}" width="40" height="40"/>
            <p>${thank.realName}さんから${thank.targetRealName}さんへ</p>
        </div>
        <div class="thanks-detail-message-body">${thank.body}</div>
    </div>

    <#if reactions?? && (reactions?size > 0)>
        <div class="thanks-detail-reaction">
            <h2>集まったリアクション</h2>
            <p>${reactions?size}件</p>
        </div>
    </#if>

    <#if threads?? && (threads?size > 0)>
        <div class="thanks-detail-thread">
            <h2>スレッド</h2>
            <#list threads as thread>
                <div class="thanks-detail-thread-user">
                    <img src="${thread.userImage}" width="40" height="40"/>
                    <p>${thread.realName}</p>
                </div>
                <div class="thanks-detail-thread-message">${thread.body}</div>
            </#list>
        </div>
    </#if>
</@container.page>